package in.eloksolutions.ayyappa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration

@ComponentScan({ "in.eloksolutions.ayyappa.config" })
@PropertySource(value = { "classpath:application.properties" })
public class MongoConfigaration {

    @Autowired
    private Environment environment;
    private static DB db;
    
    @Bean
    public MongoClient getMongoClient() {
    		MongoClient mongoClient=new MongoClient(new MongoClientURI("mongodb://root:ankO960yHLxt@52.15.94.159:27017"));
    		//MongoClient mongoClient=new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    		//db.authenticate("root", "ankO960yHLxt".toCharArray());
    		db = mongoClient.getDB("ayyappaDB");
			return mongoClient;
     }
	public static DB getDb() {
		return db;
	}

	public void setDb(DB db) {
		this.db = db;
	}
	
}

