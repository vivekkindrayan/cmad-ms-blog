package com.mysocial.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Base64;

import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import io.vertx.core.AsyncResult;
import io.vertx.ext.web.RoutingContext;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mysocial.beans.Auth;
import com.mysocial.beans.User;
import com.mysocial.db.UserPersistence;

import static com.mysocial.util.Constants.*;

public class MySocialUtil {
	
	private static MongoClient mongoClient = null;
	private static ThreadLocal<MongoDatabase> mongoDb = new ThreadLocal<MongoDatabase>();
	private static ThreadLocal<Datastore> mongoTL = new ThreadLocal<Datastore>();
	
	public static final int MONGODB_PORT = 27027;
	public static final String MONGODB_HOST = "vm-amitaga-001";
	
	public static String MONGODB_URL = MONGODB_URL_PREFIX + MONGODB_HOST + ":" + Integer.toString(MONGODB_PORT);
	
	/**
	 * Method to retrieve a mongo database client from the thread local storage
	 * @return
	 */
	public static MongoDatabase getMongoDB(){
		if(mongoDb.get()==null){
			MongoClientURI connectionString = new MongoClientURI(MONGODB_URL);
			mongoClient = new MongoClient(connectionString);	
			MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
			mongoDb.set(mongoDatabase);
			return mongoDatabase;
		}
		return mongoDb.get();
	}
	
	public static Datastore getMongoDataStore(){
		Morphia morphia = new Morphia();
		morphia.mapPackage(DB_PACKAGE_NAME);
		Datastore datastore = morphia.createDatastore(mongoClient, DB_NAME);
		if(mongoTL.get()==null){
			datastore.ensureIndexes();
			mongoTL.set(datastore);
		}
		return mongoTL.get();
	}

	public static MongoCollection<Document> getCollectionForDB (String dbName)
	{
		MongoDatabase db = getMongoDB();
		MongoIterable<String> collections = db.listCollectionNames();
		boolean collectionExists = false;
		for (String collection : collections) {
			if (collection.equals(dbName)) {
				collectionExists = true;
				break;
			}
		}
		
		if (!collectionExists) {
			db.createCollection(dbName);
		}
		
		return db.getCollection(dbName);
	}
	
	@SuppressWarnings("rawtypes")
	public static void handleFailure(AsyncResult<Object> resultHandler, Class clazz) {
		Throwable t = resultHandler.cause();
		System.err.println("Failed in " + clazz.getName() + ", result = " + resultHandler.result() + ", message = " + t.getMessage() + ", stacktrace = "); 
		t.printStackTrace() ;
	}
	
	public static synchronized String encrypt(String plaintext) throws Exception
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(plaintext.getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();
	}
	
	public static boolean comparePassword(String inputPassword, User u) throws Exception
	{
		return u.getPassword().equals(encrypt(inputPassword));
	}

	public static void handleException (Exception ex)
	{
		System.err.println(ex.getMessage());
		ex.printStackTrace();
	}
	
	public static User getSignedInUser (RoutingContext routingContext)
	{
		String signedInUserId = null;
		String authHeader = routingContext.request().getHeader(AUTH_HEADER);
		
		User u = null;
		if (authHeader != null) {
			u = getAuthFromHeader(authHeader);
			if (u != null) {
				System.out.println("User data for " + signedInUserId + " fetched successfully");
			} else {
				System.err.println("Failed to fetch data for signed in user");
			}
		}
		return u;
	}
	
	protected void finalize() {
		mongoClient.close();
	}
	
	public static User getAuthFromHeader (String header) {
		Auth auth = new Auth();
		if (header != null && !header.isEmpty() && header.startsWith(AUTH_HEADER)) {
			
	        String credentials = new String(Base64.getDecoder().decode(header), Charset.forName("UTF-8"));
	        final String[] values = credentials.split(":",2);
	        auth.setUserName(values[0]);
	        auth.setPassword(values[1]);
		}
		
		if (UserPersistence.authenticateUser(auth.getUserName(), auth.getPassword())) {
			return UserPersistence.getUserByUsername(auth.getUserName());
		}
		else {
			return null;
		}
	}
	

}