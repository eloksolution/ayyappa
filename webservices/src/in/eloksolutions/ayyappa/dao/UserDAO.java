package in.eloksolutions.ayyappa.dao;

import in.eloksolutions.ayyappa.config.MongoConfigaration;
import in.eloksolutions.ayyappa.model.Group;
import in.eloksolutions.ayyappa.model.Padipooja;
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
	
	public String  getImagePath( String userId) {
		System.out.println("query setImagePath is userId " + userId);
		String imgPath=null;
		if(userId==null)return null;
		BasicDBObject query = new BasicDBObject("_id",new ObjectId( userId));
		DBCursor cursor = collection.find(query);
		if (cursor.hasNext()) {
			DBObject user = cursor.next();
			imgPath=(String)user.get("IMGPATH");
		}
		cursor.close();
		return imgPath;
	}
	
	
	public User getUserWithGroups(String userid) throws Exception {
		DBCursor dbUserCursor = getDBUser(userid);
		if (dbUserCursor == null)return null;
		DBObject dbuser = dbUserCursor.next();
		User dbUserVO = getUser(dbuser);
		List<GroupMember> memberGroups = getGroups(dbuser);
		if (!Util.isListEmpty(memberGroups))
			dbUserVO.setGroups(memberGroups);
		return dbUserVO;
	}

	public User getUserWithTopics(String userid) throws Exception {
		DBCursor dbUserCursor = getDBUser(userid);
		if (dbUserCursor == null)return null;
		DBObject dbuser = dbUserCursor.next();
		User dbUserVO = getUser(dbuser);
		List<UserTopics> userTopics = getTopics(dbuser);
		if (!Util.isListEmpty(userTopics))
			dbUserVO.setUserTopics(userTopics);
		return dbUserVO;
	}
	
	public User getUserWithPaids(String userid) throws Exception {
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
			userPadis.setImgPath(dbo.getString("IMGPATH"));
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

	private DBCursor getDBUser(String userid) throws Exception{
		if(userid==null) throw new Exception("User Id cannot be null");
		BasicDBObject query=new BasicDBObject("_id",new ObjectId(userid));
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
		   if(dbo!=null){
			   BasicDBList locs = (BasicDBList) dbo.get("coordinates");
			   if(locs!=null){
				   dbuser.setLoc(locs.get(0).toString(), locs.get(1).toString());
				   dbuser.setLon(locs.get(1).toString());
				   dbuser.setLat(locs.get(0).toString());
			   }
		   }
		return dbuser;
	}
	
	private LocationVO getUserLocation(String userid) throws Exception {
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
	
	public void addUserPadis(Padipooja padipooja){
		System.out.println("Updating padipooja "+padipooja);
		DBObject dbPadi= toDBPadiObject(padipooja);
		BasicDBObject update = new BasicDBObject();
		update.put( "$push", new BasicDBObject( "PADIS", dbPadi ) );
		BasicDBObject match = new BasicDBObject();
		match.put( "_id",new ObjectId(padipooja.getMemId()) );
		WriteResult rs=collection.update(match,update);
		System.out.println("Write result is "+rs.getUpsertedId());
	}
	private DBObject toDBPadiObject(Padipooja padipooja ) {
		 return new BasicDBObject("PADIID", padipooja.getPadipoojaId())
		 .append("IMGPATH", padipooja.getImgPath())
       .append("PADI", padipooja.getEventName());
	}
	
	public void addUserGroup(Group group){
		System.out.println("Updating group "+group);
		DBObject dbGroup= toDBDissObject(group);
		BasicDBObject update = new BasicDBObject();
		update.put( "$push", new BasicDBObject( "GROUPS", dbGroup ) );
		BasicDBObject match = new BasicDBObject();
		match.put( "_id",new ObjectId(group.getOwner()) );
		WriteResult rs=collection.update(match,update);
		System.out.println("Write result is "+rs.getUpsertedId());
	}
	
	private DBObject toDBDissObject(Group group) {
		 return new BasicDBObject("GROUPID", group.getGroupId())
		 .append("IMGPATH", group.getImagePath())
        .append("GROUPNAME", group.getName());
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
	
	public String updateUserToken(String userId,String fcmToken) {
		BasicDBObject newDocument = new BasicDBObject();
		newDocument.append("$set", new BasicDBObject().append("TOKENFCM", fcmToken));
		WriteResult wr=collection.update(new BasicDBObject("_id", new ObjectId(userId)),newDocument);
		return wr.getUpsertedId().toString();
	}
	
	public String requestConnect(UserConnectionVO user) throws Exception {
		if(checkIfAlreadySentRequest(user.getUserId(),user.getConnectedToId())) return "2";
		DBObject dbToUsers= toDBUser(user);
		BasicDBObject update = new BasicDBObject();
		update.put( "$push", new BasicDBObject( "SENTCONNECTIONS", dbToUsers ) );
		BasicDBObject match = getIdObject(user.getUserId());
		WriteResult rs=collection.update(match,update);
		
		DBObject dbFromUsers= fromDBUser(user);
		BasicDBObject update1 = new BasicDBObject();
		update1.put( "$push", new BasicDBObject( "RECEIVEDCONNECTIONS", dbFromUsers ) );
		BasicDBObject match1 = getIdObject(user.getConnectedToId());
		WriteResult rs1=collection.update(match1,update1);
		return "1";
	}
	
	public boolean checkIfAlreadySentRequest(String userId, String toUserId) throws Exception {
		BasicDBObject andQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(getIdObject(userId));
		if(toUserId!=null)obj.add(new BasicDBObject("SENTCONNECTIONS.USERID",toUserId ));
		else
			obj.add(new BasicDBObject("SENTCONNECTIONS.USERID","" ));
		andQuery.put("$and", obj);
		DBCursor cur= collection.find(andQuery);
		if (cur == null || !cur.hasNext())return false;
		return true;
	}

	public boolean isConnected(String fromUserId, String toUserId) throws Exception {
		System.out.println("are they connected fromUserId "+fromUserId+" toUserId "+toUserId);
		BasicDBObject andQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(getIdObject(fromUserId));
		if(toUserId!=null)obj.add(new BasicDBObject("CONNECTIONS.USERID",toUserId ));
		else
			obj.add(new BasicDBObject("CONNECTIONS.USERID","" ));
		andQuery.put("$and", obj);
		DBCursor cur= collection.find(andQuery);
		if (cur == null || !cur.hasNext())return false;
		return true;
	}
	public List<UserVo> getReceivedConnection(String userId) throws Exception{
		DBCursor cur=getDBUser(userId);
		if (cur == null || !cur.hasNext())return null;
		DBObject user = cur.next();
		 BasicDBList users = (BasicDBList) user.get("RECEIVEDCONNECTIONS");
		 ArrayList<UserVo> userReceivedList=new ArrayList<>();
		 if(Util.isListEmpty(users))return userReceivedList;
		 for(Object obj:users){
			 DBObject bdb=(DBObject)obj;
			 UserVo u=new UserVo();
			 u.setUserId((String)bdb.get("USERID"));
			 u.setFirstName((String)bdb.get("FIRSTNAME"));
			 u.setLastName((String)bdb.get("LASTNAME"));
			 u.setStatus((String)bdb.get("STATUS"));
			 userReceivedList.add(u);
		 }
		 cur.close();
		 return userReceivedList;
	}
	
	public List<UserVo> getSentConnection(String userId) throws Exception{
		DBCursor cur=getDBUser(userId);
		if (cur == null)return null;
		DBObject user = cur.next();
		 BasicDBList users = (BasicDBList) user.get("SENTCONNECTIONS");
		 ArrayList<UserVo> userSentList=new ArrayList<>();
		 if(Util.isListEmpty(users))return userSentList;
		 for(Object obj:users){
			 DBObject bdb=(DBObject)obj;
			 UserVo u=new UserVo();
			 u.setUserId((String)bdb.get("USERID"));
			 u.setFirstName((String)bdb.get("FIRSTNAME"));
			 u.setLastName((String)bdb.get("LASTNAME"));
			 u.setStatus((String)bdb.get("STATUS"));
			 userSentList.add(u);
		 }
		 cur.close();
		 return userSentList;
	}
	
	public String connect(UserConnectionVO user) {
		System.out.println("Updating "+user);
		DBObject dbUsers= toDBUser(user);
		BasicDBObject update = new BasicDBObject();
		update.put( "$push", new BasicDBObject( "CONNECTIONS", dbUsers ) );
		BasicDBObject match = getIdObject(user.getUserId());
		WriteResult rs=collection.update(match,update);
		
		DBObject dbFromUsers= fromDBUser(user);
		BasicDBObject fromUpdate = new BasicDBObject();
		fromUpdate.put( "$push", new BasicDBObject( "CONNECTIONS", dbFromUsers ) );
		BasicDBObject fromMatch = getIdObject(user.getConnectedToId());
		rs=collection.update(fromMatch,dbFromUsers);
		System.out.println("Write result is "+rs.getUpsertedId());
		changeConnectionStatus(user);
		return "success";
	}

	private void changeConnectionStatus(UserConnectionVO user) {
		BasicDBObject andQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(getIdObject(user.getConnectedToId()));
		obj.add(new BasicDBObject("SENTCONNECTIONS.USERID",user.getUserId() ));
		andQuery.put("$and", obj);
		BasicDBObject set = new BasicDBObject(
			    "$set", 
			    new BasicDBObject("SENTCONNECTIONS.$.STATUS", "Connected")
			);
		WriteResult rs=collection.update(andQuery,set);
		System.out.println("Updating connection status "+rs.getUpsertedId());
	}

	private BasicDBObject getIdObject(String id) {
		BasicDBObject match = new BasicDBObject();
		match.put( "_id",new ObjectId(id) );
		return match;
	}
	public User getConnections(String userId) throws Exception {
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
		u.setConnectedToId(dbo.getString("USERID"));
		u.setToFirstName(dbo.getString("FIRSTNAME"));
		u.setToLastName(dbo.getString("LASTNAME"));
		return u;
	}
	
	public User getReceivedConnections(String userId) throws Exception {
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
		u.setConnectedToId(dbo.getString("USERID"));
		u.setToFirstName(dbo.getString("FIRSTNAME"));
		u.setToLastName(dbo.getString("LASTNAME"));
		u.setStatus(dbo.getString("STATUS"));
		return u;
	}
	
	private static final DBObject toDBUser(UserConnectionVO userConnection) {
	    return new BasicDBObject("CONNECTIONDATE", new Date())
	    					.append("USERID", userConnection.getConnectedToId())
						 .append("FIRSTNAME", userConnection.getToFirstName())
						    .append("STATUS", "")
					     .append("LASTNAME", userConnection.getToLastName());
	}
	
	private static final DBObject fromDBUser(UserConnectionVO userConnection) {
	    return new BasicDBObject("FIRSTNAME", userConnection.getFirstName())
	                     .append("LASTNAME", userConnection.getLastName())
	                      .append("USERID", userConnection.getUserId())
	                        .append("STATUS", "")
	                     .append("CONNECTIONDATE", new Date());
	}
	
	public List<User> findNearMe(String userid) throws Exception {
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
								.append("coordinates", Arrays.asList(new Double[]{d, e}));
	}

	public User searchUserWithConnection(String fromUserId, String toUserId) {
		User user=searchById(toUserId);
		boolean isReqSent=false;
		try {
			isReqSent = checkIfAlreadySentRequest(fromUserId, toUserId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		user.setRequestSent(isReqSent);
		boolean isConnected=false;
		try {
			isConnected=isConnected(fromUserId, toUserId);
			user.setConnected(isConnected);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	public List<User> search(String tok) {
		List<BasicDBObject> or = new ArrayList<BasicDBObject>();
		BasicDBObject firstNameRegQuery = new BasicDBObject("$regex", ".*"+tok+".*").append("$options", "i");
		BasicDBObject firstNameQuery = new BasicDBObject("FIRSTNAME",firstNameRegQuery);
		BasicDBObject lastNameQuery = new BasicDBObject("LASTNAME", firstNameRegQuery);
		or.add(firstNameQuery);
		or.add(lastNameQuery);
		BasicDBObject orQuery=new BasicDBObject("$or", or);
		System.out.println("Search wury "+orQuery);
		DBCursor cursor = collection.find(orQuery);
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
}
