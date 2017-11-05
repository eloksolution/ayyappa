package in.eloksolutions.ayyappa.dao;

import in.eloksolutions.ayyappa.config.MongoConfigaration;
import in.eloksolutions.ayyappa.model.Group;
import in.eloksolutions.ayyappa.model.User;
import in.eloksolutions.ayyappa.util.Util;
import in.eloksolutions.ayyappa.vo.GroupMember;

import java.text.SimpleDateFormat;
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

@Repository("groupDAO")
public class GroupDAO {
	MongoClient mongoClient;
	DBCollection collection;
	
	@Autowired
	public void setMongoClient(MongoClient mongoClient){
		this.mongoClient=mongoClient;
		collection = MongoConfigaration.getDb().getCollection("groups");
	}
	
	public String addGroup(Group group, User user){
		DBObject dbgroup = toDBObject(group);
		collection.insert(dbgroup);
		ObjectId id = (ObjectId)dbgroup.get( "_id" );
		join(new GroupMember(id.toString(),user.getUserId(),user.getFirstName(),user.getLastName(),user.getImgPath()));
		DBObject dbUserGroup= toDBUserGroup(group,id.toString());
		BasicDBObject update = new BasicDBObject();
		update.put( "$push", new BasicDBObject( "GROUPS", dbUserGroup ) );
		BasicDBObject match = new BasicDBObject();
		match.put( "_id",new ObjectId(group.getOwner()) );
		WriteResult rs=collection.update(match,update);
		System.out.println("result is "+rs.getUpsertedId());
		return id.toString();
	}
	private DBObject toDBUserGroup(Group group, String groupId) {
		 return new BasicDBObject("GROUPID", groupId)
			.append("IMGPATH", group.getImagePath())
			.append("GROUPNAME", group.getName());
	}

	public static final DBObject toDBObject(Group group) {
	    return new BasicDBObject("name", group.getName())
	                     .append("description", group.getDescription())
	                     .append("owner", group.getOwner())
	                     .append("numberOfMembers", group.getNumberOfMembers())
	                     .append("imagePath", group.getImagePath())
	                     .append("catagory", group.getCatgory());
	}
	
	public static final DBObject toUpdateDBObject(Group group) {
	    return new BasicDBObject("name", group.getName())
	                     .append("description", group.getDescription())
	                     .append("imagePath", group.getImagePath())
	                     .append("catagory", group.getCatgory());
	}
	
	public List<Group> getGroups(String userId){
        DBCursor  cursor = collection.find().sort(new BasicDBObject("createDate", -1));
        List<Group> groups = getGroupsDB(cursor,userId);
        return groups;
	}

	public List<Group> getTopGroups(String userId){
        DBCursor  cursor = collection.find().sort(new BasicDBObject("createDate", -1)).limit(5);
        List<Group> groups = getGroupsDB(cursor,userId);
        return groups;
	}

	private List<Group> getGroupsDB(DBCursor cursor, String userId) {
		List<Group> groups=new ArrayList<>();
        while (cursor.hasNext()) { 
           DBObject group = cursor.next();
        	ObjectId mobjid=(ObjectId)group.get("_id");
			System.out.println("id is  "+mobjid);
			Group dbgroup=new Group((String)mobjid.toString(),(String)group.get("name"),(String)group.get("description"),(String)group.get("owner"),(String)group.get("imagePath"),(Date)group.get("createDate"));
			List<User> groupMember=getGroupMembers(group,userId,dbgroup);
			if(!Util.isListEmpty(groupMember))
				dbgroup.setGroupMembers(groupMember);
           groups.add(dbgroup);
        }
        cursor.close();
		return groups;
	}

	
	public Group searchById(String groupid, String userId) {
		BasicDBObject query=new BasicDBObject("_id",new ObjectId(""+groupid));
		System.out.println("query issiihhkkh is  "+query);
		 DBCursor  cursor = collection.find(query);
		Group dbgroup=null;
	        while (cursor.hasNext()) { 
	           DBObject group = cursor.next();
	           ObjectId mobjid=(ObjectId)group.get("_id");
	           System.out.println("description "+group.get("description"));
	           dbgroup=new Group((String)mobjid.toString(),(String)group.get("name"),(String)group.get("description"),(String)group.get("owner"),(String)group.get("imagePath"),(Date)group.get("createDate"));
	           List<User> groupMembers=getGroupMembers(group,userId,dbgroup);
	           dbgroup.setGroupMembers(groupMembers);
	        }
	        cursor.close();
	        return dbgroup;

	}
	
	public List<Group> getUserGroups(String userId) {
		BasicDBObject query = new BasicDBObject("owner", userId);
		System.out.println("query issiihhkkh is  " + query);
		DBCursor cursor = collection.find(query);
		ArrayList<Group> groups=new ArrayList<Group>();
		while (cursor.hasNext()) {
			DBObject group = cursor.next();
			ObjectId mobjid = (ObjectId) group.get("_id");
			System.out.println("description " + group.get("description"));
			Group dbgroup = new Group((String) mobjid.toString(),
					(String) group.get("name"),
					(String) group.get("description"),
					(String) group.get("owner"),
					(String) group.get("imagePath"),(Date)group.get("createDate"));
			groups.add(dbgroup);
		}
		cursor.close();
		return groups;
	}
	
	private List<User> getGroupMembers(DBObject group, String userId, Group dbGroup) {
		BasicDBList dbMembers = ( BasicDBList ) group.get( "members" );
		if(dbMembers==null)return null;
		List<User> groupMembers=new ArrayList<>();
		for( Iterator< Object > it = dbMembers.iterator(); it.hasNext(); ){
			BasicDBObject dbo     = ( BasicDBObject ) it.next();
			User  user = new User();
			user.setUserId(dbo.getString("userId"));
			if(userId.equals(user.getUserId()))dbGroup.setIsMember("Y");
			user.setFirstName(dbo.getString("firstName"));
			user.setLastName(dbo.getString("lastName"));
			user.setImgPath(dbo.getString("imgPath"));
			Date joinDate=dbo.getDate("joinDate");
			if(joinDate!=null){
				user.setCreateDate((String)(new SimpleDateFormat("dd/MM/yyyy").format(joinDate)));
			}
			groupMembers.add(user);
		}
		return groupMembers;
	}
	

	public String join(GroupMember groupMem ){
		System.out.println("Updating topic "+groupMem);
		DBObject groupUser= toDBDissObject(groupMem.getUserId(),groupMem.getFirstName(),groupMem.getLastName(),groupMem.getImgPath());
		BasicDBObject update = new BasicDBObject();
		update.put( "$push", new BasicDBObject( "members", groupUser ) );
		BasicDBObject match = new BasicDBObject();
		match.put( "_id",new ObjectId(groupMem.getGroupId()) );
		WriteResult rs=collection.update(match,update);
		System.out.println("Write result is "+rs.getUpsertedId());
		return rs.getUpsertedId().toString();
	}

	public String leave(GroupMember groupMem ){
		System.out.println("Updating topic "+groupMem);
		DBObject groupUser= new BasicDBObject("userId", groupMem.getUserId());
		BasicDBObject update = new BasicDBObject();
		update.put( "$pull", new BasicDBObject( "members", groupUser ) );
		BasicDBObject match = new BasicDBObject();
		match.put( "_id",new ObjectId(groupMem.getGroupId()) );
		WriteResult rs=collection.update(match,update);
		System.out.println("Write result is "+rs.getUpsertedId());
		return rs.getUpsertedId().toString();
	}
	private DBObject toDBDissObject(String userId, String firstName, String lastName, String imgPath) {
		 return new BasicDBObject("userId", userId)
         .append("firstName", firstName)
         .append("lastName", lastName)
         .append("imgPath", imgPath)
         .append("joinDate", new Date());
	}

	public String update(Group group) {
		DBObject dbGroup=toUpdateDBObject(group);
		BasicDBObject newDocument = new BasicDBObject();
		newDocument.append("$set", dbGroup);
		WriteResult wr=collection.update(
		    new BasicDBObject("_id", new ObjectId(group.getGroupId())),
		    newDocument
		);
		return wr.getUpsertedId().toString();
	}
	
	public List<Group> getJoinedGroups(String userId) {
		BasicDBObject query = new BasicDBObject("members.userId", userId);
		System.out.println("query issiihhkkh is  " + query);
		DBCursor cursor = collection.find(query);
		ArrayList<Group> groups=new ArrayList<Group>();
		while (cursor.hasNext()) {
			DBObject group = cursor.next();
			ObjectId mobjid = (ObjectId) group.get("_id");
			System.out.println("description " + group.get("description"));
			Group dbgroup = new Group((String) mobjid.toString(),
					(String) group.get("name"),
					(String) group.get("description"),
					(String) group.get("owner"),
					(String) group.get("imagePath"),(Date)group.get("createDate"));
			groups.add(dbgroup);
		}
		cursor.close();
		return groups;
	}
}
