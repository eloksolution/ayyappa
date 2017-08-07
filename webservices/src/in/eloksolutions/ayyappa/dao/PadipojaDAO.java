package in.eloksolutions.ayyappa.dao;

import in.eloksolutions.ayyappa.config.MongoConfigaration;
import in.eloksolutions.ayyappa.model.Padipooja;
import in.eloksolutions.ayyappa.model.User;
import in.eloksolutions.ayyappa.vo.PadiMember;

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

@Repository("padipoojaDao")
public class PadipojaDAO {
	MongoClient mongoClient;
	DBCollection collection;
	
  @Autowired
  public void setMongoClient(MongoClient mongoClient){
		this.mongoClient=mongoClient;
		collection = MongoConfigaration.getDb().getCollection("padipooja");
	}

	public String addPadipooja(Padipooja padipooja) {
		DBObject dbpadi = padiDBObject(padipooja);
		collection.insert(dbpadi);
		ObjectId id = (ObjectId) dbpadi.get("_id");
		
		DBObject dbUserPadi= toDBUserPadi(padipooja,id.toString());
		BasicDBObject update = new BasicDBObject();
		update.put( "$push", new BasicDBObject( "PADIS", dbUserPadi ) );
		BasicDBObject match = new BasicDBObject();
		match.put( "_id",new ObjectId(padipooja.getMemId()));
		WriteResult rs=collection.update(match,update);
		System.out.println("result is "+rs.getError());
		return id.toString();
	}
	private DBObject toDBUserPadi(Padipooja padipooja, String padiID) {
		 return new BasicDBObject("PADIID", padiID)
			.append("PADI", padipooja.getName());
	}

	public static final DBObject padiDBObject(Padipooja padipooja) {
	    return new BasicDBObject("eventName", padipooja.getEventName())
	                     .append("description", padipooja.getDescription())
	                     .append("location", padipooja.getLocation())
	                     .append("date", padipooja.getDate())
	                     .append("time", padipooja.getTime())
	                     .append("memId", padipooja.getMemId())
	                     .append("name", padipooja.getName());
	}
	
	public List<Padipooja> getPadipooja() {
		DBCursor cursor = collection.find();
		List<Padipooja> padipooja = new ArrayList<>();
		while (cursor.hasNext()) {
			DBObject padi = cursor.next();
			ObjectId mobjid = (ObjectId) padi.get("_id");
			System.out.println("name " + padi.get("eventName"));
			padipooja.add(new Padipooja( mobjid.toString(), (String) padi
					.get("eventName"), (String) padi.get("location"),
					(String) padi.get("description"),
					(String) padi.get("date"), (String) padi.get("time"),
					(String) padi.get("memid"), (String) padi.get("name")));
		}
		return padipooja;
	}

	public Padipooja searchById(String padipoojaid,String userId) {
		Padipooja padipooja= searchById(padipoojaid);
		List<User> members=padipooja.getPadiMembers();
		if(members==null){
			return padipooja;
		}
		boolean flag=isMember(members,userId);
		if(flag){
			padipooja.setIsMember("Y");
		}
		return padipooja;
	}
	
	private boolean isMember(List<User> members, String userId) {
		for(User u:members){
			if(u.getUserId().equals(userId))return true;
		}
		return false;
	}
	public Padipooja searchById(String padipoojaid) {
		DBCursor cursor = getPadiCursor(padipoojaid);
		Padipooja dbPadi = null;
		while (cursor.hasNext()) {
			DBObject padiPooja = cursor.next();
			ObjectId mobjid = (ObjectId) padiPooja.get("_id");
			System.out.println("description " + padiPooja.get("description"));
			dbPadi = (new Padipooja((String) mobjid.toString(),
					(String) padiPooja.get("eventName"),
					(String) padiPooja.get("location"),
					(String) padiPooja.get("description"),
					(String) padiPooja.get("date"),
					(String) padiPooja.get("time"),
					(String) padiPooja.get("memid"),
					(String) padiPooja.get("name")));
	           List<User> padiMembers=getPadiMembers(padiPooja);
	           dbPadi.setPadiMembers(padiMembers);
		}
		cursor.close();
		return dbPadi;

	}
	private DBCursor getPadiCursor(String padipoojaid) {
		BasicDBObject query = new BasicDBObject("_id", new ObjectId(padipoojaid));
		DBCursor cursor = collection.find(query);
		return cursor;
	}
	private List<User> getPadiMembers(DBObject group) {
		BasicDBList dbMembers = ( BasicDBList ) group.get( "members" );
		if(dbMembers==null)return null;
		List<User> padiMembers=new ArrayList<>();
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
			padiMembers.add(user);
		}
		return padiMembers;
	}
	public String update(Padipooja uPadi) {
		DBObject dbPadi=padiDBObject(uPadi);
		WriteResult wr=collection.update(
		    new BasicDBObject("_id", new ObjectId(uPadi.getPadipoojaId())),
		    dbPadi
		);
		return wr.getError();
	}
	public String join(PadiMember padiMember ){
		System.out.println("Updating padiMember "+padiMember);
		DBObject dbPadiUser= toDBDissObject(padiMember.getUserId(),padiMember.getFirstName(),padiMember.getLastName());
		BasicDBObject update = new BasicDBObject();
		update.put( "$push", new BasicDBObject( "members", dbPadiUser ) );
		BasicDBObject match = new BasicDBObject();
		match.put( "_id",new ObjectId(padiMember.getPadiId()) );
		WriteResult rs=collection.update(match,update);
		System.out.println("Write result is "+rs.getLastError());
		return rs.getError();
	}
	
	public String leave(PadiMember padiMember ){
		System.out.println("Updating padiMember "+padiMember);
		DBObject dbPadiUser=new BasicDBObject("userId", padiMember.getUserId());
		BasicDBObject update = new BasicDBObject();
		update.put( "$pull", new BasicDBObject( "members", dbPadiUser ) );
		BasicDBObject match = new BasicDBObject();
		match.put( "_id",new ObjectId(padiMember.getPadiId()) );
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
	

