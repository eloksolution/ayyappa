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
import com.sun.xml.internal.txw2.Document;

@Repository("topicDAO")
public class TopicDAO {
	MongoClient mongoClient;
	DBCollection collection;
	
	@Autowired
	public void setMongoClient(MongoClient mongoClient){
		this.mongoClient=mongoClient;
		collection = MongoConfigaration.getDb().getCollection("topics");
	}
	
	public String addTopic(Topic topic){
		DBObject dbTopic = toDBObject(topic);
		System.out.println("Topics is "+dbTopic);
		collection.insert(dbTopic);
		ObjectId id = (ObjectId)dbTopic.get( "_id" );
		DBObject dbUserTopic= toDBUserTopic(topic,id.toString());
		BasicDBObject update = new BasicDBObject();
		update.put( "$push", new BasicDBObject( "TOPICS", dbUserTopic ) );
		BasicDBObject match = new BasicDBObject();
		match.put( "_id",new ObjectId(topic.getOwner()) );
		WriteResult rs=collection.update(match,update);
		System.out.println("result is "+rs.getError());
		//Groups
		BasicDBObject match1 = new BasicDBObject();
		match.put( "_id",new ObjectId(topic.getGroupId()) );
		WriteResult rs1=collection.update(match1,update);
		return id.toString();
	}
	private DBObject toDBUserTopic(Topic topic, String topicId) {
		 return new BasicDBObject("TOPICID", topicId)
			.append("TOPIC", topic.getTopic());
	}

	public void updateTopic(Topic topic){
		DBObject dbTopic=toDBObject(topic);
		collection.update(
		    new BasicDBObject("_id", new ObjectId(topic.getTopicId())),
		    dbTopic
		);
	}
	public void addDiscussion(String topicId,Discussion diss){
		System.out.println("Updating topic "+topicId+" discussion "+diss);
		DBObject dbDiscussions= toDBDissObject(diss);
		BasicDBObject update = new BasicDBObject();
		update.put( "$push", new BasicDBObject( "DISCUSSIONS", dbDiscussions ) );
		BasicDBObject match = new BasicDBObject();
		match.put( "_id",new ObjectId(topicId) );
		WriteResult rs=collection.update(match,update);
		System.out.println("Write result is "+rs.getLastError());
	}
	
	public static final DBObject toDBDissObject(Discussion topic) {
	    return new BasicDBObject("COMMENT", topic.getComment())
	    					.append("POSTDATE", new Date())
	    					.append("OWNERNAME", topic.getUserName())
	    					.append("OWNER", topic.getUserId());
	                     
	}
	
	public static final DBObject toDBObject(Topic topic) {
	    return new BasicDBObject("TOPIC", topic.getTopic())
	                     .append("OWNER", topic.getOwner())
	                     .append("GROUPID", topic.getGroupId())
						 .append("CREATEDATE", new Date())
	                     .append("DESCRIPTION", topic.getDescription());
	}
	
	

	public List<Topic> getTopics() {
		DBCursor  cursor = collection.find();
        List<Topic> topics=new ArrayList<>();
        while (cursor.hasNext()) { 
           DBObject topic = cursor.next();
           ObjectId objid=(ObjectId)topic.get("_id");
           Topic topicDB=new Topic((String)objid.toString(),(String)topic.get("TOPIC"),(String)topic.get("DESCRIPTION"),(String)topic.get("GROUPID"),(String)topic.get("OWNER"),objid.getTime());
		   System.out.println("DESCRIPTION "+topic.get("DESCRIPTION"));
		   List<Discussion> diss=getDiscussions(topic);
		   topicDB.setDiscussions(diss);
           topics.add(topicDB);
        }
        return topics;
	}

	private List<Discussion> getDiscussions(DBObject topic) {
		BasicDBList discussions = ( BasicDBList ) topic.get( "DISCUSSIONS" );
		if(discussions==null)return null;
		List<Discussion> discussDB=new ArrayList<>();
		for( Iterator< Object > it = discussions.iterator(); it.hasNext(); ){
			BasicDBObject dbo     = ( BasicDBObject ) it.next();
			Discussion  discussion = new Discussion();
			discussion.setComment(dbo.getString("COMMENT"));
			Date d=dbo.getDate("POSTDATE");
			if(d!=null){
				discussion.setPostDate(d);
				discussion.setsPostDate((String)(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(discussion.getPostDate())));
			}
			discussion.setUserId(dbo.getString("OWNERNAME"));
			discussion.setUserName(dbo.getString("OWNER"));
			discussDB.add(discussion);
		}
		return discussDB;
	}

	public Topic searchById(String topicId) {
		DBCursor cursor=getDBTopicCursor(topicId);
		DBObject topic = cursor.next();
	    ObjectId mobjid=(ObjectId)topic.get("_id");
	    Topic dbtop= new Topic((String)mobjid.toString(),(String) topic.get("TOPIC"), (String)topic.get("DESCRIPTION"), (String)topic.get("GROUPID"),
		    		   (String)topic.get("OWNER"),mobjid.getTime() );
	    List<Discussion> diss=getDiscussions(topic);
	    dbtop.setDiscussions(diss);
		return dbtop;
	}
	private DBCursor getDBTopicCursor(String topicId) {
		BasicDBObject query=new BasicDBObject("_id",new ObjectId(""+topicId));
		return collection.find(query);
	}

	public List<Topic> getGroupTopics(String groupId) {
		DBCursor  cursor = findObject(groupId);
        List<Topic> topics=new ArrayList<>();
        while (cursor.hasNext()) { 
           DBObject topic = cursor.next();
           ObjectId objid=(ObjectId)topic.get("_id");
           Topic topicDB=new Topic((String)objid.toString(),(String)topic.get("TOPIC"),(String)topic.get("DESCRIPTION"),(String)topic.get("GROUPID"),(String)topic.get("OWNER"),objid.getTime());
		   System.out.println("DESCRIPTION "+topic.get("DESCRIPTION"));
		   List<Discussion> diss=getDiscussions(topic);
		   topicDB.setDiscussions(diss);
           topics.add(topicDB);
        }
        return topics;
	}
	private DBCursor findObject(String groupId) {
		BasicDBObject query=new BasicDBObject("GROUPID",groupId);
		return collection.find(query);
	}

	
}
