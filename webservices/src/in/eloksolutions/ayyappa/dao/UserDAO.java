package in.eloksolutions.ayyappa.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import in.eloksolutions.ayyappa.config.MongoConfigaration;
import in.eloksolutions.ayyappa.model.User;

@Repository("userDAO")
public class UserDAO {
	
	
	MongoClient mongoClient;
	DBCollection collection;
	
	@Autowired
	public void setMongoClient(MongoClient mongoClient){
		this.mongoClient=mongoClient;
		collection = MongoConfigaration.getDb().getCollection("users");
	}
	
	public void adduser(User user){
		DBObject dbuser = toDBObject(user);
		collection.save(dbuser);
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
}
