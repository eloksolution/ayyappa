package in.eloksolutions.ayyappa.dao;

import in.eloksolutions.ayyappa.config.MongoConfigaration;
import in.eloksolutions.ayyappa.model.Feedback;
import in.eloksolutions.ayyappa.model.User;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

@Repository("feedbackDAO")
public class FeedbackDAO {
	MongoClient mongoClient;
	DBCollection collection;
	
	@Autowired
	public void setMongoClient(MongoClient mongoClient){
		this.mongoClient=mongoClient;
		collection = MongoConfigaration.getDb().getCollection("feedbacks");
	}

	public String userFeedback(Feedback feed) {
		
			DBObject dbuser = toDBObject(feed);
			collection.insert(dbuser);
			ObjectId id = (ObjectId)dbuser.get( "_id" );
			return id.toString();
		}
		public static final DBObject toDBObject(Feedback feed) {
		    return new BasicDBObject("name", feed.getName())
		                     .append("mobile", feed.getMobile())
		                     .append("email", feed.getEmail())
		                     .append("comment", feed.getFeedback());
		                    
		   
		                    
		}
		
	}
	

