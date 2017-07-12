package in.eloksolutions.ayyappa.config;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "in.eloksolutions.ayyappa.config" })
@PropertySource(value = { "classpath:application.properties" })
public class MongoConfigaration {

    @Autowired
    private Environment environment;
    private static DB db;
    
    
    @Bean
    public MongoClient getMongoClient() {
    	try {
    		MongoClient mongoClient=new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
    		db = mongoClient.getDB("ayyappaDB");
			return mongoClient;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
     }


	public static DB getDb() {
		return db;
	}


	public void setDb(DB db) {
		this.db = db;
	}
	
}

