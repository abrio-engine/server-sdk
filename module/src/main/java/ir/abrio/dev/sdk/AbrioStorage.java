package ir.abrio.dev.sdk;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Abrio Component
 */
public class AbrioStorage {
    private MongoClient mongoClient;
    private Map<String,DBCollection> mongoNameToCollection = new HashMap<String, DBCollection>();

    AbrioStorage(ComponentConfigures componentConfigures) throws UnknownHostException {
        mongoClient = new MongoClient();
        DB projectDb = mongoClient.getDB(componentConfigures.projectId); //will create if doesn't exist
        for(String collectionName: componentConfigures.collections){
            if(projectDb.collectionExists(collectionName)){
                DBCollection namedCollection = projectDb.getCollection(collectionName);
                mongoNameToCollection.put(collectionName, namedCollection);
            }else{
                DBCollection createdCollection = projectDb.createCollection(collectionName,null); //TODO search if options can be exploited
                mongoNameToCollection.put(collectionName, createdCollection);
            }
        }
    }

    public DBCollection get(String collectionName) {
        return mongoNameToCollection.get(collectionName);
    }

    public void close() {
        mongoClient.close();
    }
}
