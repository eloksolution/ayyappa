package in.eloksolutions.ayyappa.dao;

import in.eloksolutions.ayyappa.config.MongoConfigaration;
import in.eloksolutions.ayyappa.model.User;
import in.eloksolutions.ayyappa.util.Util;
import in.eloksolutions.ayyappa.vo.GroupMember;
import in.eloksolutions.ayyappa.vo.LocationVO;
import in.eloksolutions.ayyappa.vo.UserConnectionVO;
import in.eloksolutions.ayyappa.vo.UserPadis;
import in.eloksolutions.ayyappa.vo.UserTopics;
import in.eloksolutions.ayyappa.vo.UserVo;

import java.util.ArrayList;
import java.util.Arrays;
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
		String userid=isUserExist(user);
		System.out.println("User id is "+userid);
		DBObject dbuser = toDBObject(user);
		if(userid!=null && userid.trim().length()>0){
			WriteResult wr=collection.update(new BasicDBObject("_id", new ObjectId(userid)),dbuser);
			return userid;
		}
		
		collection.insert(dbuser);
		ObjectId id = (ObjectId)dbuser.get( "_id" );
		return id.toString();
	}
	private String isUserExist(User user) {
		System.out.println("USER IS "+user);
		DBObject email = new BasicDBObject("EMAIL", user.getEmail());  
		DBObject mobile = new BasicDBObject("MOBILE", user.getMobile());    
		BasicDBList or = new BasicDBList();
		or.add(email);
		or.add(mobile);
		DBObject query = new BasicDBObject("$or", or);
		DBCursor cur = collection.find(query);
		System.out.println("got the cursor");
		if(cur.hasNext()){
			DBObject dbuser = cur.next();
			ObjectId mobjid=(ObjectId)dbuser.get("_id");
			return mobjid.toString();
		}
		return null;
	}

	private  final DBObject toDBObject(User user) {
		BasicDBObject obj= new BasicDBObject("FIRSTNAME", user.getFirstName())
	                     .append("LASTNAME", user.getLastName())
	                     .append("MOBILE", user.getMobile())
	                     .append("EMAIL", user.getEmail())
	                     .append("AREA", user.getArea())
	                     .append("CITY", user.getCity())
	                     .append("STATE", user.getState())
	                     .append("IMGPATH", user.getImgPath())
	                   
	    				 .append("CREATEDATE", new Date());
		if(user.getLoc()!=null)
			obj.append("LOC", toDBLoc(user.getLoc().getLon(),user.getLoc().getLat()));
	   return obj;                 
	}
	private DBObject toDBLoc(String lon,String lat) {
		if(Util.isEmpty(lon) || Util.isEmpty(lat)) return null;
		Double dlon=Double.parseDouble(lon);
		Double dlat=Double.parseDouble(lat);
		ArrayList<Double> loc=new ArrayList<>();
		loc.add(dlon);
		loc.add(dlat);
		 return new BasicDBObject("type","Point")
       .append("coordinates", loc);
	}

	public List<User> getUsers() {
		DBCursor cursor = collection.find();
		List<User> users = new ArrayList<>();
		while (cursor.hasNext()) {
			DBObject user = cursor.next();
			ObjectId mobjid = (ObjectId) user.get("_id");
			User dbuser = new User((String) mobjid.toString(),
					(String) user.get("FIRSTNAME"),
					(String) user.get("LASTNAME"), (String) user.get("MOBILE"),
					(String) user.get("EMAIL"), (String) user.get("AREA"),
					(String) user.get("CITY"), (String) user.get("STATE"), (String) user.get("IMGPATH"));
			DBObject dbo = (DBObject) user.get("LOC");
			if(dbo!=null){
				BasicDBList locs = (BasicDBList) dbo.get("coordinates");
				if (locs != null) {
					dbuser.setLoc(locs.get(0).toString(), locs.get(1).toString());
				}
			}
			users.add(dbuser);
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
		return collection.find(query);
	}

	private User getUser(DBObject user) {
		ObjectId mobjid=(ObjectId)user.get("_id");
		   User dbuser=new User((String)mobjid.toString(),(String)user.get("FIRSTNAME"),(String)user.get("LASTNAME"),(String)user.get("MOBILE")
				   ,(String)user.get("EMAIL")
				   ,(String)user.get("AREA")
				    ,(String)user.get("CITY")
				   ,(String)user.get("STATE")
				   ,(String)user.get("IMGPATH"));
		   DBObject dbo=(DBObject) user.get("LOC");
		   BasicDBList locs = (BasicDBList) dbo.get("coordinates");
		   if(locs!=null){
			   dbuser.setLoc(locs.get(0).toString(), locs.get(1).toString());
		   }
		return dbuser;
	}
	
	private LocationVO getUserLocation(String userid) {
		DBCursor dbUserCursor = getDBUser(userid);
		if (dbUserCursor == null)return null;
		DBObject user = dbUserCursor.next();
		   DBObject dbo=(DBObject) user.get("LOC");
		   BasicDBList locs = (BasicDBList) dbo.get("coordinates");
		   if(locs==null)return null;
		return new LocationVO(userid, (Double)locs.get(0), (Double)locs.get(1));
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
		System.out.println("Write result is "+rs.getUpsertedId());
	}
	
	private DBObject toDBDissObject(GroupMember groupMember) {
		 return new BasicDBObject("GROUPID", groupMember.getGroupId())
        .append("GROUPNAME", groupMember.getGroupName());
	}

	public String update(User user) {
		DBObject dbGroup=toDBObject(user);
		BasicDBObject newDocument = new BasicDBObject();
		newDocument.append("$set", dbGroup);
		WriteResult wr=collection.update(
		    new BasicDBObject("_id", new ObjectId(user.getUserId())),
		    dbGroup
		);
		return wr.getUpsertedId().toString();
	}
	
	public String updateToken(User user) {
		BasicDBObject newDocument = new BasicDBObject();
		newDocument.append("$set", new BasicDBObject().append("TOKENFCM", user.getTokenFCM()));
		WriteResult wr=collection.update(new BasicDBObject("_id", new ObjectId(user.getUserId())),newDocument);
		return wr.getUpsertedId().toString();
	}
	
	public String requestConnect(UserConnectionVO user) {
		DBObject dbFromUsers= toDBUser(user);
		BasicDBObject update = new BasicDBObject();
		update.put( "$push", new BasicDBObject( "SENTCONNECTIONS", dbFromUsers ) );
		BasicDBObject match = getIdObject(user.getUserId());
		WriteResult rs=collection.update(match,update);
		
		DBObject dbToUsers= toDBFromUser(user);
		dbToUsers.put( "$push", new BasicDBObject( "RECEIVEDCONNECTIONS", dbToUsers ) );
		BasicDBObject match1 = getIdObject(user.getConnectedToId());
		WriteResult rs1=collection.update(match1,dbToUsers);
		return rs.getUpsertedId().toString();
	}
	
	public List<UserVo> getReceivedConnection(String userId){
		DBCursor cur=getDBUser(userId);
		if (cur == null)return null;
		DBObject user = cur.next();
		 BasicDBList users = (BasicDBList) user.get("RECEIVEDCONNECTIONS");
		 ArrayList<UserVo> userReceivedList=new ArrayList<>();
		 for(Object obj:users){
			 DBObject bdb=(DBObject)obj;
			 UserVo u=new UserVo();
			 u.setUserId((String)bdb.get("FROMUSERID"));
			 u.setFirstName((String)bdb.get("FROMFIRSTNAME"));
			 u.setLastName((String)bdb.get("FROMLASTNAME"));
			 u.setStatus((String)bdb.get("STATUS"));
			 userReceivedList.add(u);
		 }
		 cur.close();
		 return userReceivedList;
	}
	
	public List<UserVo> getSentConnection(String userId){
		DBCursor cur=getDBUser(userId);
		if (cur == null)return null;
		DBObject user = cur.next();
		 BasicDBList users = (BasicDBList) user.get("SENTCONNECTIONS");
		 ArrayList<UserVo> userSentList=new ArrayList<>();
		 for(Object obj:users){
			 DBObject bdb=(DBObject)obj;
			 UserVo u=new UserVo();
			 u.setUserId((String)bdb.get("TOUSERID"));
			 u.setFirstName((String)bdb.get("TOFIRSTNAME"));
			 u.setLastName((String)bdb.get("TOLASTNAME"));
			 u.setStatus((String)bdb.get("STATUS"));
			 userSentList.add(u);
		 }
		 cur.close();
		 return userSentList;
	}
	
	public String connect(UserConnectionVO user) {
		DBObject dbUsers= toDBUser(user);
		BasicDBObject update = new BasicDBObject();
		update.put( "$push", new BasicDBObject( "CONNECTIONS", dbUsers ) );
		BasicDBObject match = getIdObject(user.getConnectedToId());
		WriteResult rs=collection.update(match,update);
		
		DBObject dbFromUsers= toDBFromUser(user);
		BasicDBObject fromUpdate = new BasicDBObject();
		fromUpdate.put( "$push", new BasicDBObject( "CONNECTIONS", dbFromUsers ) );
		BasicDBObject fromMatch = getIdObject(user.getUserId());
		rs=collection.update(fromMatch,dbFromUsers);
		System.out.println("Write result is "+rs.getUpsertedId());
		
		changeConnectionStatus(user);
		return rs.getUpsertedId().toString();
	}

	private void changeConnectionStatus(UserConnectionVO user) {
		BasicDBObject andQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(getIdObject(user.getConnectedToId()));
		obj.add(new BasicDBObject("SENTCONNECTIONS.TOUSERID",user.getUserId() ));
		andQuery.put("$and", obj);
		BasicDBObject set = new BasicDBObject(
			    "$set", 
			    new BasicDBObject("SENTCONNECTIONS.STATUS", "Connected")
			);
		WriteResult rs=collection.update(andQuery,set);
		System.out.println("Updating connection status "+rs.getUpsertedId());
	}

	private BasicDBObject getIdObject(String id) {
		BasicDBObject match = new BasicDBObject();
		match.put( "_id",new ObjectId(id) );
		return match;
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
			UserConnectionVO u = getUserConnectionVO(dbo);
			userConnections.add(u);
		}
		dbUserCursor.close();
		dbUserVO.setUserConnections(userConnections);
		return dbUserVO;
	}

	private UserConnectionVO getUserConnectionVO(BasicDBObject dbo) {
		UserConnectionVO  u = new UserConnectionVO();
		u.setConnectedToId(dbo.getString("TOUSERID"));
		u.setToFirstName(dbo.getString("TOFIRSTNAME"));
		u.setToLastName(dbo.getString("TOLASTNAME"));
		return u;
	}
	
	public User getReceivedConnections(String userId) {
		DBCursor dbUserCursor = getDBUser(userId);
		if (dbUserCursor == null)return null;
		DBObject dbuser = dbUserCursor.next();
		User dbUserVO = getUser(dbuser);
		BasicDBList connections = ( BasicDBList ) dbuser.get( "RECEIVEDCONNECTIONS" );
		if(connections==null)return null;
		List<UserConnectionVO> userConnections=new ArrayList<>();
		for( Iterator< Object > it = connections.iterator(); it.hasNext(); ){
			BasicDBObject dbo     = ( BasicDBObject ) it.next();
			UserConnectionVO u = getFromUserConnection(dbo);
			userConnections.add(u);
		}
		dbUserCursor.close();
		dbUserVO.setUserConnections(userConnections);
		return dbUserVO;
	}

	private UserConnectionVO getFromUserConnection(BasicDBObject dbo) {
		UserConnectionVO  u = new UserConnectionVO();
		u.setConnectedToId(dbo.getString("FROMUSERID"));
		u.setToFirstName(dbo.getString("FROMFIRSTNAME"));
		u.setToLastName(dbo.getString("FROMLASTNAME"));
		u.setStatus(dbo.getString("STATUS"));
		return u;
	}
	
	private static final DBObject toDBUser(UserConnectionVO userConnection) {
	    return new BasicDBObject("CONNECTIONDATE", new Date())
	    					.append("TOUSERID", userConnection.getConnectedToId())
						 .append("TOFIRSTNAME", userConnection.getToFirstName())
						    .append("STATUS", "")
					     .append("TOLASTNAME", userConnection.getToLastName());
	}
	
	private static final DBObject toDBFromUser(UserConnectionVO userConnection) {
	    return new BasicDBObject("FROMFIRSTNAME", userConnection.getFirstName())
	                     .append("FROMLASTNAME", userConnection.getLastName())
	                      .append("FROMUSERID", userConnection.getLastName())
	                        .append("STATUS", "")
	                     .append("CONNECTIONDATE", new Date());
	}
	
	public List<User> findNearMe(String userid) {
		LocationVO loc=getUserLocation(userid);
		BasicDBObject query = new BasicDBObject("LOC",getNearObj(loc.getLat(), loc.getLon()));
		System.out.println("query issiihhkkh is  " + query);
		DBCursor cursor = collection.find(query);
		ArrayList<User> users=new ArrayList<User>();
		while (cursor.hasNext()) {
			DBObject user = cursor.next();
			users.add(getUser(user));
		}
		cursor.close();
		return users;
	}

	private DBObject getNearObj(double d, double e) {
		BasicDBObject query = new BasicDBObject("$near",getGeoObj(d,e));
		return query;
	}

	private Object getGeoObj(double d, double e) {
		return new BasicDBObject("$geometry",getCoordObj(d,e));
	}

	private Object getCoordObj(double d, double e) {
		return new BasicDBObject("type","Point" )
								.append("coordinates", Arrays.asList(new Double[]{78.5369922, 17.4495948}));
	}
}
