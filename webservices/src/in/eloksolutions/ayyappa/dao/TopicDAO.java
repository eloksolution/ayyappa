package in.eloksolutions.ayyappa.dao;

import in.eloksolutions.ayyappa.config.MongoConfigaration;
import in.eloksolutions.ayyappa.model.Discussion;
import in.eloksolutions.ayyappa.model.Topic;

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

@Repository("topicDAO")
public class TopicDAO {
	MongoClient mongoClient;
	DBCollection collection;
	
	@Autowired
	public void setMongoClient(MongoClient mongoClient){
		this.mongoClient=mongoClient;
		collection = MongoConfigaration.getDb().getCollection("topics");
	}
	
	public void addTopic(Topic topic){
		DBObject dbTopic = toDBObject(topic);
		System.out.println("Topics is "+dbTopic);
		collection.save(dbTopic);
	}
	public void updateTopic(Topic topic){
		collection.update(
		    new BasicDBObject("_id", new ObjectId(topic.getTopicId())),
		    new BasicDBObject("$set", new BasicDBObject("topic", topic.getTopic()))
		);
	}
	public void addDiscussion(String topicId,Discussion diss){
		System.out.println("Updating topic "+topicId+" discussion "+diss);
		DBObject dbDiscussions= toDBDissObject(diss);
		BasicDBObject update = new BasicDBObject();
		update.put( "$push", new BasicDBObject( "discussions", dbDiscussions ) );
		BasicDBObject match = new BasicDBObject();
		match.put( "_id",new ObjectId(topicId) );
		WriteResult rs=collection.update(match,update);
		System.out.println("Write result is "+rs.getLastError());
	}
	
	public static final DBObject toDBDissObject(Discussion topic) {
	    return new BasicDBObject("comment", topic.getComment())
	    					.append("postDate", new Date())
	    					.append("owner", topic.getUserId());
	                     
	}
	
	public static final DBObject toDBObject(Topic topic) {
	    return new BasicDBObject("topic", topic.getTopic())
	                     .append("owner", topic.getOwner())
	                     .append("groupId", topic.getGroupId())
	                     .append("description", topic.getDescription());
	}

	public List<Topic> getTopics() {
		DBCursor  cursor = collection.find();
        List<Topic> topics=new ArrayList<>();
        while (cursor.hasNext()) { 
           DBObject topic = cursor.next();
           ObjectId objid=(ObjectId)topic.get("_id");
           Topic topicDB=new Topic((String)objid.toString(),(String)topic.get("name"),(String)topic.get("description"),(String)topic.get("groupId"),(String)topic.get("owner"),objid.getTime());
		   System.out.println("description "+topic.get("description"));
		   List<Discussion> diss=getDiscussions(topic);
		   topicDB.setDiscussions(diss);
           topics.add(topicDB);
        }
        return topics;
	}

	private List<Discussion> getDiscussions(DBObject topic) {
		BasicDBList discussions = ( BasicDBList ) topic.get( "discussions" );
		if(discussions==null)return null;
		List<Discussion> discussDB=new ArrayList<>();
		for( Iterator< Object > it = discussions.iterator(); it.hasNext(); ){
			BasicDBObject dbo     = ( BasicDBObject ) it.next();
			Discussion  discussion = new Discussion();
			discussion.setComment(dbo.getString("comment"));
			Date d=dbo.getDate("postDate");
			if(d!=null){
				discussion.setPostDate(d);
				discussion.setsPostDate((String)(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(discussion.getPostDate())));
			}
			discussion.setUserId(dbo.getString("userId"));
			discussDB.add(discussion);
		}
		return discussDB;
	}

	public Topic searchById(String topicId) {
		BasicDBObject query = new BasicDBObject("_id", topicId);
		DBCursor cursor = collection.find(query);

		try {
		   while(cursor.hasNext()) {
			   DBObject topic = cursor.next();
	           ObjectId mobjid=(ObjectId)topic.get("_id");
		       System.out.println("mobjid" +mobjid);
		       return new Topic(mobjid.toString(),(String) topic.get("topic"), (String)topic.get("description"), (String)topic.get("groupId"),
		    		   (String)topic.get("owner"),mobjid.getTime() );
		   }
		} finally {
		   cursor.close();
		}
		return null;
	}
}
