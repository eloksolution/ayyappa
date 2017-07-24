package in.eloksolutions.ayyappa.dao;

import in.eloksolutions.ayyappa.config.MongoConfigaration;
import in.eloksolutions.ayyappa.model.User;
import in.eloksolutions.ayyappa.vo.GroupMember;

import java.util.ArrayList;
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
	
	public String adduser(User user){
		DBObject dbuser = toDBObject(user);
		collection.insert(dbuser);
		ObjectId id = (ObjectId)dbuser.get( "_id" );
		return id.toString();
	}
	public static final DBObject toDBObject(User user) {
	    return new BasicDBObject("FIRSTNAME", user.getFirstName())
	                     .append("LASTNAME", user.getLastName())
	                     .append("MOBILE", user.getMobile())
	                     .append("EMAIL", user.getEmail())
	                     .append("AREA", user.getArea())
	                     .append("CITY", user.getCity())
	                     .append("STATE", user.getState());
	   
	                    
	}
	public List<User> getuser(){
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
        		   ,(String)user.get("STATE")));
        }
        cursor.close();
        return users;
	}

	public User searchById(String userid) {
		BasicDBObject query=new BasicDBObject("_id",new ObjectId(""+userid));
		System.out.println("query issiihhkkh is  "+query);
		 DBCursor  cursor = collection.find(query);
		 User dbuser=null; 
	        while (cursor.hasNext()) { 
	           DBObject user = cursor.next();
	           ObjectId mobjid=(ObjectId)user.get("_id");
	           System.out.println("description "+user.get("description"));
	           dbuser=new User((String)mobjid.toString(),(String)user.get("FIRSTNAME"),(String)user.get("LASTNAME"),(String)user.get("MOBILE")
	        		   ,(String)user.get("EMAIL")
	        		   ,(String)user.get("AREA")
	        		    ,(String)user.get("CITY")
	        		   ,(String)user.get("STATE"));
	           
	        }
	        cursor.close();
	        return dbuser;

	}
	public User getUserWithGroups(String userid) {
		BasicDBObject query=new BasicDBObject("_id",new ObjectId(""+userid));
		System.out.println("query issiihhkkh is  "+query);
		 DBCursor  cursor = collection.find(query);
		 User dbuser=null; 
	        while (cursor.hasNext()) { 
	           DBObject user = cursor.next();
	           ObjectId mobjid=(ObjectId)user.get("_id");
	           System.out.println("description "+user.get("description"));
	           dbuser=new User((String)mobjid.toString(),(String)user.get("FIRSTNAME"),(String)user.get("LASTNAME"),(String)user.get("MOBILE")
	        		   ,(String)user.get("EMAIL")
	        		   ,(String)user.get("AREA")
	        		    ,(String)user.get("CITY")
	        		   ,(String)user.get("STATE"));
	           
	           List<GroupMember> memberGroups=getGroups(user);
	           if(memberGroups!=null && memberGroups.size()>0)
	           		dbuser.setGroups(memberGroups);
	        }
	        cursor.close();
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
}
