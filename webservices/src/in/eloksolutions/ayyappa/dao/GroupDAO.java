package in.eloksolutions.ayyappa.dao;

import in.eloksolutions.ayyappa.config.MongoConfigaration;
import in.eloksolutions.ayyappa.model.Group;
import in.eloksolutions.ayyappa.model.User;
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
	
	public void addGroup(Group group){
		DBObject dbgroup = toDBObject(group);
		collection.save(dbgroup);
	}
	public static final DBObject toDBObject(Group group) {
	    return new BasicDBObject("name", group.getName())
	                     .append("description", group.getDescription())
	                     .append("owner", group.getOwner())
	                     .append("createDate", group.getCreateDate())
	                     .append("numberOfMembers", group.getNumberOfMembers())
	                     .append("type", group.getType())
	                     .append("imagePath", group.getImagePath())
	                     .append("catagory", group.getCatgory());
	   
	                    
	}
	public List<Group> getGroup(){
        DBCursor  cursor = collection.find();
        List<Group> groups=new ArrayList<>();
        while (cursor.hasNext()) { 
           DBObject group = cursor.next();
        	ObjectId mobjid=(ObjectId)group.get("_id");
			System.out.println("id is  "+mobjid);
			Group dbgroup=new Group((String)mobjid.toString(),(String)group.get("name"),(String)group.get("description"),(String)group.get("owner"));
			List<User> groupMember=getGroupMembers(group);
			dbgroup.setGroupMembers(groupMember);
           groups.add(dbgroup);
        }
        cursor.close();
        return groups;
	}

	public Group searchById(String groupid) {
		BasicDBObject query=new BasicDBObject("_id",new ObjectId(""+groupid));
		System.out.println("query issiihhkkh is  "+query);
		 DBCursor  cursor = collection.find(query);
		Group dbgroup=null;
	        while (cursor.hasNext()) { 
	           DBObject group = cursor.next();
	           ObjectId mobjid=(ObjectId)group.get("_id");
	           System.out.println("description "+group.get("description"));
	           dbgroup=new Group((String)mobjid.toString(),(String)group.get("name"),(String)group.get("description"),(String)group.get("owner"));
	           List<User> groupMembers=getGroupMembers(group);
	           dbgroup.setGroupMembers(groupMembers);
	        }
	        cursor.close();
	        return dbgroup;

	}
	
	private List<User> getGroupMembers(DBObject group) {
		BasicDBList dbMembers = ( BasicDBList ) group.get( "members" );
		if(dbMembers==null)return null;
		List<User> groupMembers=new ArrayList<>();
		for( Iterator< Object > it = dbMembers.iterator(); it.hasNext(); ){
			BasicDBObject dbo     = ( BasicDBObject ) it.next();
			User  user = new User();
			user.setUserId(dbo.getString("userId"));
			user.setFirstName(dbo.getString("firstName"));
			user.setLastName(dbo.getString("lastName"));
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
		DBObject groupUser= toDBDissObject(groupMem.getUserId(),groupMem.getFirstName(),groupMem.getLastName());
		BasicDBObject update = new BasicDBObject();
		update.put( "$push", new BasicDBObject( "members", groupUser ) );
		BasicDBObject match = new BasicDBObject();
		match.put( "_id",new ObjectId(groupMem.getGroupId()) );
		WriteResult rs=collection.update(match,update);
		System.out.println("Write result is "+rs.getLastError());
		return rs.getError();
	}

	private DBObject toDBDissObject(String userId, String firstName, String lastName) {
		 return new BasicDBObject("userId", userId)
         .append("firstName", firstName)
         .append("lastName", lastName)
         .append("joinDate", new Date());
	}
}
