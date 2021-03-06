package in.eloksolutions.ayyappa.dao;

import in.eloksolutions.ayyappa.config.MongoConfigaration;
import in.eloksolutions.ayyappa.model.Padipooja;
import in.eloksolutions.ayyappa.model.User;
import in.eloksolutions.ayyappa.vo.PadiMember;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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

	public String addPadipooja(Padipooja padipooja, User user) {
		DBObject dbpadi = padiDBObject(padipooja);
		collection.insert(dbpadi);
		ObjectId id = (ObjectId) dbpadi.get("_id");
		join(new PadiMember(id.toString(),user.getUserId(),user.getFirstName(),user.getLastName(),user.getImgPath()));
		DBObject dbUserPadi= toDBUserPadi(padipooja,id.toString());
		BasicDBObject update = new BasicDBObject();
		update.put( "$push", new BasicDBObject( "PADIS", dbUserPadi ) );
		BasicDBObject match = new BasicDBObject();
		match.put( "_id",new ObjectId(padipooja.getMemId()));
		WriteResult rs=collection.update(match,update);
		System.out.println("result is "+rs.getUpsertedId());
		return id.toString();
	}
	private DBObject toDBUserPadi(Padipooja padipooja, String padiID) {
		 return new BasicDBObject("PADIID", padiID)
		 .append("IMGPATH", padipooja.getImgPath())
			.append("PADI", padipooja.getName());
	}

	public static final DBObject padiDBObject(Padipooja padipooja) {
		Date createDate=null;
		try {
			createDate=new SimpleDateFormat("dd-MM-yyyy").parse(padipooja.getDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    return new BasicDBObject("eventName", padipooja.getEventName())
	                     .append("description", padipooja.getDescription())
	                     .append("location", padipooja.getLocation())
	                     .append("date", padipooja.getDate())
	                     .append("time", padipooja.getTime())
	                     .append("eventDate", createDate)
	                     .append("memId", padipooja.getMemId())
	                     .append("imgPath", padipooja.getImgPath())
	                     .append("name", padipooja.getName());
	}
	
	public static final DBObject padiUpdateDBObject(Padipooja padipooja) {
		Date createDate=null;
		try {
			createDate=new SimpleDateFormat("dd-MM-yyyy").parse(padipooja.getDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    return new BasicDBObject("eventName", padipooja.getEventName())
	                     .append("description", padipooja.getDescription())
	                     .append("location", padipooja.getLocation())
	                     .append("date", padipooja.getDate())
	                     .append("time", padipooja.getTime())
	                     .append("eventDate", createDate)
	                     .append("imgPath", padipooja.getImgPath());
	}

	public List<Padipooja> getPadiPoojas(String userId) {
		DBCursor cursor = collection.find().sort(new  BasicDBObject("createDate", -1));
		List<Padipooja> padipooja = getPadiPoojasDB(cursor,userId);
		return padipooja;
	}
	
	public List<Padipooja> getUserPadiPoojas(String userId) {
		BasicDBObject query = new BasicDBObject("members.userId", userId);
		DBCursor cursor = collection.find(query).sort(new  BasicDBObject("createDate", -1));
		List<Padipooja> padipooja = getPadiPoojasDB(cursor,userId);
		return padipooja;
	}
	
	public List<Padipooja> getTOPPadipooja(String userId) {
		DBCursor cursor = collection.find().sort(new  BasicDBObject("createDate", -1)).limit(5);
		List<Padipooja> padipooja = getPadiPoojasDB(cursor,userId);
		return padipooja;
	}

	private List<Padipooja> getPadiPoojasDB(DBCursor cursor, String userId) {
		List<Padipooja> padipooja = new ArrayList<>();
		GregorianCalendar cal=new GregorianCalendar();
		while (cursor.hasNext()) {
			DBObject padi = cursor.next();
			ObjectId mobjid = (ObjectId) padi.get("_id");
			System.out.println("name " + padi.get("eventName"));
			
			Padipooja pooja=new Padipooja( mobjid.toString(), (String) padi
					.get("eventName"), (String) padi.get("location"),
					(String) padi.get("description"),
					(String) padi.get("date"), (String) padi.get("time"),
					(String) padi.get("memId"), (String) padi.get("name"), (String) padi.get("imgPath"));
			Date date=(Date)padi.get("eventDate");
			if(date!=null){
				cal.setTime(date);
				pooja.setDay(cal.get(Calendar.DAY_OF_MONTH)+"");
				pooja.setMonth(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH ));
				pooja.setYear(cal.get(Calendar.YEAR)+"");
				pooja.setWeek(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH ));
			}
			pooja.setIsMember((isMember(padi,userId))?"Y":"N");
			pooja.setNoOfMembers(noOfMembers(padi)+"");
			padipooja.add(pooja);
		}
		return padipooja;
	}
	private int noOfMembers(DBObject padi) {
		BasicDBList dbMembers = (BasicDBList) padi.get("members");
		if (dbMembers == null)
			return 0;
		return dbMembers.size();
	}
	
	private boolean isMember(DBObject padi, String userId) {
		BasicDBList dbMembers = (BasicDBList) padi.get("members");
		if (dbMembers == null)
			return false;
		for (Iterator<Object> it = dbMembers.iterator(); it.hasNext();) {
			BasicDBObject dbo = (BasicDBObject) it.next();
			if (userId.equalsIgnoreCase(dbo.getString("userId")))
				return true;
		}
		return false;
	}
	private boolean isMember(List<User> members, String userId) {
		for(User u:members){
			if(u.getUserId().equals(userId))return true;
		}
		return false;
	}
	public Padipooja searchById(String padipoojaid, String userId) {
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
					(String) padiPooja.get("memId"),
					(String) padiPooja.get("name"),
					(String) padiPooja.get("imgPath")));
	           List<User> padiMembers=getPadiMembers(padiPooja,userId,dbPadi);
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
	private List<User> getPadiMembers(DBObject group, String userId, Padipooja dbPadi) {
		BasicDBList dbMembers = ( BasicDBList ) group.get( "members" );
		if(dbMembers==null)return null;
		List<User> padiMembers=new ArrayList<>();
		for( Iterator< Object > it = dbMembers.iterator(); it.hasNext(); ){
			BasicDBObject dbo     = ( BasicDBObject ) it.next();
			User  user = new User();
			user.setUserId(dbo.getString("userId"));
			if(userId.equalsIgnoreCase(user.getUserId()))dbPadi.setIsMember("Y");
			user.setFirstName(dbo.getString("firstName"));
			user.setLastName(dbo.getString("lastName"));
			user.setImgPath(dbo.getString("imgPath"));
			Date joinDate=dbo.getDate("joinDate");
			if(joinDate!=null){
				user.setCreateDate((String)(new SimpleDateFormat("dd/MM/yyyy").format(joinDate)));
			}
			padiMembers.add(user);
		}
		return padiMembers;
	}
	public String update(Padipooja uPadi) {
		DBObject dbPadi=padiUpdateDBObject(uPadi);
		BasicDBObject newDocument = new BasicDBObject();
		newDocument.append("$set", dbPadi);
		WriteResult wr=collection.update(
		    new BasicDBObject("_id", new ObjectId(uPadi.getPadipoojaId())),
		    newDocument
		);
		return wr.getUpsertedId().toString();
	}
	public String join(PadiMember padiMember ){
		System.out.println("Updating padiMember "+padiMember);
		DBObject dbPadiUser= toDBDissObject(padiMember.getUserId(),padiMember.getFirstName(),padiMember.getLastName(),padiMember.getImgPath());
		BasicDBObject update = new BasicDBObject();
		update.put( "$push", new BasicDBObject( "members", dbPadiUser ) );
		BasicDBObject match = new BasicDBObject();
		match.put( "_id",new ObjectId(padiMember.getPadiId()) );
		WriteResult rs=collection.update(match,update);
		System.out.println("Write result is "+rs.getUpsertedId());
		return rs.getUpsertedId().toString();
	}
	
	public String leave(PadiMember padiMember ){
		System.out.println("Updating padiMember "+padiMember);
		DBObject dbPadiUser=new BasicDBObject("userId", padiMember.getUserId());
		BasicDBObject update = new BasicDBObject();
		update.put( "$pull", new BasicDBObject( "members", dbPadiUser ) );
		BasicDBObject match = new BasicDBObject();
		match.put( "_id",new ObjectId(padiMember.getPadiId()) );
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

	
}
	

