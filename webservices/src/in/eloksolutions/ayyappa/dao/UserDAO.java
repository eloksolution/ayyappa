package in.eloksolutions.ayyappa.dao;

import in.eloksolutions.ayyappa.config.MongoConfigaration;
import in.eloksolutions.ayyappa.model.User;
import in.eloksolutions.ayyappa.util.Util;
import in.eloksolutions.ayyappa.vo.GroupMember;
import in.eloksolutions.ayyappa.vo.UserConnectionVO;
import in.eloksolutions.ayyappa.vo.UserPadis;
import in.eloksolutions.ayyappa.vo.UserTopics;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

@Repository("userDAO")
public class UserDAO {
	MongoClient mongoClient;
	DBCollection collection;
	
	@Autowired
	public void setMongoClient(MongoClient mongoClient){
		this.mongoClient=mongoClient;
		collection = MongoConfigaration.getDb().getCollection("users");
	}
	
	public String addUser(User user){
		DBObject dbuser = toDBObject(user);
		collection.insert(dbuser);
		ObjectId id = (ObjectId)dbuser.get( "_id" );
		return id.toString();
	}
	private  final DBObject toDBObject(User user) {
	    return new BasicDBObject("FIRSTNAME", user.getFirstName())
	                     .append("LASTNAME", user.getLastName())
	                     .append("MOBILE", user.getMobile())
	                     .append("EMAIL", user.getEmail())
	                     .append("AREA", user.getArea())
	                     .append("CITY", user.getCity())
	                     .append("STATE", user.getState())
	                     .append("LOC", toDBLoc(user.getLoc().getLon(),user.getLoc().getLat()))
	    				 .append("CREATEDATE", new Date());
	   
	                    
	}
	private DBObject toDBLoc(String lon,String lat) {
		Double dlon=Double.parseDouble(lon);
		Double dlat=Double.parseDouble(lat);
		ArrayList<Double> loc=new ArrayList<>();
		loc.add(dlon);
		loc.add(dlat);
		 return new BasicDBObject("type","Point")
       .append("coordinates", loc);
	}
	public List<User> getUsers(){
        DBCursor  cursor = collection.find();
        List<User> users=new ArrayList<>();
        while (cursor.hasNext()) { 
           DBObject user = cursor.next();
        	ObjectId mobjid=(ObjectId)user.get("_id");
			System.out.println("id is  "+mobjid);
           users.add(new User((String)mobjid.toString(),(String)user.get("FIRSTNAME"),(String)user.get("LASTNAME"),(String)user.get("MOBILE")
        		   ,(String)user.get("EMAIL")
        		   ,(String)user.get("AREA")
        		    ,(String)user.get("CITY")
        		    ,(String)user.get("STATE")
        		     ,(String)user.get("LAT")
        		      ,(String)user.get("LON")));
        }
        cursor.close();
        return users;
	}

	public User searchById(String userid) {
		BasicDBObject query = new BasicDBObject("_id",new ObjectId("" + userid));
		System.out.println("query issiihhkkh is  " + query);
		DBCursor cursor = collection.find(query);
		User dbuser = null;
		while (cursor.hasNext()) {
			DBObject user = cursor.next();
			dbuser = getUser(user);
		}
		cursor.close();
		return dbuser;
	}

	public User getUserWithGroups(String userid) {
		DBCursor dbUserCursor = getDBUser(userid);
		if (dbUserCursor == null)return null;
		DBObject dbuser = dbUserCursor.next();
		User dbUserVO = getUser(dbuser);
		List<GroupMember> memberGroups = getGroups(dbuser);
		if (!Util.isListEmpty(memberGroups))
			dbUserVO.setGroups(memberGroups);
		return dbUserVO;
	}

	public User getUserWithTopics(String userid) {
		DBCursor dbUserCursor = getDBUser(userid);
		if (dbUserCursor == null)return null;
		DBObject dbuser = dbUserCursor.next();
		User dbUserVO = getUser(dbuser);
		List<UserTopics> userTopics = getTopics(dbuser);
		if (!Util.isListEmpty(userTopics))
			dbUserVO.setUserTopics(userTopics);
		return dbUserVO;
	}
	
	public User getUserWithPaids(String userid) {
		DBCursor dbUserCursor = getDBUser(userid);
		if (dbUserCursor == null)return null;
		DBObject dbuser = dbUserCursor.next();
		User dbUserVO = getUser(dbuser);
		List<UserPadis> userPadis = getPadis(dbuser);
		if (!Util.isListEmpty(userPadis))
			dbUserVO.setUserPadis(userPadis);
		return dbUserVO;
	}
	
	public List<UserPadis> getPadis(DBObject dbuser) {
		BasicDBList padis = ( BasicDBList ) dbuser.get( "PADIS" );
		if(padis==null)return null;
		List<UserPadis> padisDB=new ArrayList<>();
		for( Iterator< Object > it = padis.iterator(); it.hasNext(); ){
			BasicDBObject dbo     = ( BasicDBObject ) it.next();
			UserPadis  userPadis = new UserPadis();
			userPadis.setPadoId(dbo.getString("PADIID"));
			userPadis.setPadiName(dbo.getString("PADI"));
			padisDB.add(userPadis);
		}
		return padisDB;
	}

	private List<UserTopics> getTopics(DBObject dbuser) {
		BasicDBList topics = ( BasicDBList ) dbuser.get( "TOPICS" );
		if(topics==null)return null;
		List<UserTopics> topicsDB=new ArrayList<>();
		for( Iterator< Object > it = topics.iterator(); it.hasNext(); ){
			BasicDBObject dbo     = ( BasicDBObject ) it.next();
			UserTopics  topic = new UserTopics();
			topic.setTopicId(dbo.getString("TOPICID"));
			topic.setTopicName(dbo.getString("TOPIC"));
			topicsDB.add(topic);
		}
		return topicsDB;
	}

	private DBCursor getDBUser(String userid) {
		BasicDBObject query=new BasicDBObject("_id",new ObjectId(""+userid));
		System.out.println("query issiihhkkh is  "+query);
		return collection.find(query);
	}

	private User getUser(DBObject user) {
		User dbuser;
		ObjectId mobjid=(ObjectId)user.get("_id");
		   System.out.println("description "+user.get("description"));
		   dbuser=new User((String)mobjid.toString(),(String)user.get("FIRSTNAME"),(String)user.get("LASTNAME"),(String)user.get("MOBILE")
				   ,(String)user.get("EMAIL")
				   ,(String)user.get("AREA")
				    ,(String)user.get("CITY")
				   ,(String)user.get("STATE"));
		return dbuser;
	}
	private List<GroupMember> getGroups(DBObject user) {
		BasicDBList groups = ( BasicDBList ) user.get( "GROUPS" );
		if(groups==null)return null;
		List<GroupMember> groupsDB=new ArrayList<>();
		for( Iterator< Object > it = groups.iterator(); it.hasNext(); ){
			BasicDBObject dbo     = ( BasicDBObject ) it.next();
			GroupMember  group = new GroupMember();
			group.setGroupId(dbo.getString("GROUPID"));
			group.setGroupName(dbo.getString("GROUPNAME"));
			groupsDB.add(group);
		}
		return groupsDB;
	}
	
	public void addUserGroup(GroupMember groupMember){
		System.out.println("Updating groupMember "+groupMember);
		DBObject dbGroup= toDBDissObject(groupMember);
		BasicDBObject update = new BasicDBObject();
		update.put( "$push", new BasicDBObject( "GROUPS", dbGroup ) );
		BasicDBObject match = new BasicDBObject();
		match.put( "_id",new ObjectId(groupMember.getUserId()) );
		WriteResult rs=collection.update(match,update);
		System.out.println("Write result is "+rs.getLastError());
	}
	
	private DBObject toDBDissObject(GroupMember groupMember) {
		 return new BasicDBObject("GROUPID", groupMember.getGroupId())
        .append("GROUPNAME", groupMember.getGroupName());
	}

	public String update(User user) {
		DBObject dbGroup=toDBObject(user);
		WriteResult wr=collection.update(
		    new BasicDBObject("_id", new ObjectId(user.getUserId())),
		    dbGroup
		);
		return wr.getError();
	}
	
	public String requestConnect(UserConnectionVO user) {
		DBObject dbUsers= toDBUser(user);
		BasicDBObject update = new BasicDBObject();
		update.put( "$push", new BasicDBObject( "REQUESTCONNECTIONS", dbUsers ) );
		BasicDBObject match = new BasicDBObject();
		match.put( "_id",new ObjectId(user.getUserId()) );
		WriteResult rs=collection.update(match,update);
		return rs.getError();
	}
	
	public String connect(UserConnectionVO user) {
		DBObject dbUsers= toDBUser(user);
		BasicDBObject update = new BasicDBObject();
		update.put( "$push", new BasicDBObject( "CONNECTIONS", dbUsers ) );
		BasicDBObject match = new BasicDBObject();
		match.put( "_id",new ObjectId(user.getUserId()) );
		WriteResult rs=collection.update(match,update);
		DBObject dbFromUsers= toDBFromUser(user);
		update = new BasicDBObject();
		update.put( "$push", new BasicDBObject( "CONNECTIONS", dbFromUsers ) );
		match = new BasicDBObject();
		match.put( "_id",new ObjectId(user.getConnectedToId()) );
		rs=collection.update(match,update);
		System.out.println("Write result is "+rs.getLastError());
		return rs.getError();
	}
	public User getConnections(String userId) {
		DBCursor dbUserCursor = getDBUser(userId);
		if (dbUserCursor == null)return null;
		DBObject dbuser = dbUserCursor.next();
		User dbUserVO = getUser(dbuser);
		BasicDBList connections = ( BasicDBList ) dbuser.get( "CONNECTIONS" );
		if(connections==null)return null;
		List<UserConnectionVO> userConnections=new ArrayList<>();
		for( Iterator< Object > it = connections.iterator(); it.hasNext(); ){
			BasicDBObject dbo     = ( BasicDBObject ) it.next();
			UserConnectionVO  u = new UserConnectionVO();
			u.setConnectedToId(dbo.getString("TOUSERID"));
			u.setToFirstName(dbo.getString("TOFIRSTNAME"));
			u.setToLastName(dbo.getString("TOLASTNAME"));
			userConnections.add(u);
		}
		dbUserCursor.close();
		dbUserVO.setUserConnections(userConnections);
		return dbUserVO;
	}
	
	private static final DBObject toDBUser(UserConnectionVO userConnection) {
	    return new BasicDBObject("CONNECTIONDATE", new Date())
	    					.append("TOUSERID", userConnection.getConnectedToId())
						 .append("TOFIRSTNAME", userConnection.getToFirstName())
					     .append("TOLASTNAME", userConnection.getToLastName());
	}
	
	private static final DBObject toDBFromUser(UserConnectionVO userConnection) {
	    return new BasicDBObject("FIRSTNAME", userConnection.getFirstName())
	                     .append("LASTNAME", userConnection.getLastName())
	                      .append("USERID", userConnection.getLastName())
	                     .append("CONNECTIONDATE", new Date());
	}

}
