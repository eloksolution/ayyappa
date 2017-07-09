package in.eloksolutions.ayyappa.dao;

import in.eloksolutions.ayyappa.config.MongoConfigaration;
import in.eloksolutions.ayyappa.model.Padipooja;

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

@Repository("padipoojaDao")
public class PadipojaDAO {
	MongoClient mongoClient;
	DBCollection collection;
	
  @Autowired
  public void setMongoClient(MongoClient mongoClient){
		this.mongoClient=mongoClient;
		collection = MongoConfigaration.getDb().getCollection("padipooja");
	}
  public void addPadipooja(Padipooja padipooja) {
		DBObject dbpadi = padiDBObject(padipooja);
		collection.save(dbpadi);	
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
		
		DBCursor  cursor = collection.find();
        List<Padipooja> padipooja=new ArrayList<>();
        while (cursor.hasNext()) { 
           DBObject padi = cursor.next();
           Object objid=padi.get("_id");
			ObjectId mobjid=null;
			String sObjid="";
			if(objid instanceof ObjectId){
				mobjid=(ObjectId)padi.get("_id");
				sObjid=mobjid.toString();
			}
			if(objid instanceof String)
				sObjid=(String)padi.get("_id");
			
			System.out.println("id is  "+sObjid);
           System.out.println("name "+padi.get("eventName"));
           System.out.println("description "+padi.get("description"));
           padipooja.add(new Padipooja((String)sObjid, (String)padi.get("eventName"), (String)padi.get("location"),(String)padi.get("description") , (String)padi.get("date")
        		   , (String)padi.get("time"), (String)padi.get("memid"), (String)padi.get("name")));
                 
        }
        return padipooja;
	}
}
