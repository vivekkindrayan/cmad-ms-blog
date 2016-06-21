package com.mysocial.db;

import static com.mysocial.util.Constants.*;
import static com.mysocial.util.MySocialUtil.*;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mysocial.beans.User;
import com.mysocial.util.MySocialUtil;

public class UserPersistence {

	private static final MongoDatabase db = MySocialUtil.getMongoDB();
	
	private static final String KEY_ID = "_id";
	private static final String KEY_USERNAME = "userName";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_FIRST = "first";
	private static final String KEY_LAST = "last";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_COMPANYID = "companyId";
	private static final String KEY_SITEID = "siteId";
	private static final String KEY_DEPTID = "deptId";
	
	
	public static List<User> getAllUsers()
	{
		List<User> users = new ArrayList<User>();
		FindIterable<Document> iterable = db.getCollection(COLLECTION_NAME_USER).find();
		iterable.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		    	try {
		    		users.add(deSerialize(document));
		    	} catch (Exception ex) {
		    		handleException(ex);
		    	}
		    }
		});
		return users;
	}
	
	public static User getUserById(String userId)
	{
		List<User> users = new ArrayList<User>();
		FindIterable<Document> iterable = db.getCollection(COLLECTION_NAME_USER).find(new Document(KEY_ID, new ObjectId(userId)));
		iterable.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		    	try {
		    		users.add(deSerialize(document));
		    	} catch (Exception ex) {
		    		handleException(ex);
		    	}
		    }
		});
		if (users != null && users.size() > 0) {
			return users.get(0);
		} else {
			return null;
		}
	}
	
	public static User getUserByUsername(String userName)
	{
		List<User> users = new ArrayList<User>();
		FindIterable<Document> iterable = db.getCollection(COLLECTION_NAME_USER).find(new Document(KEY_USERNAME, userName));
		iterable.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		    	try {
		    		users.add(deSerialize(document));
		    	} catch (Exception ex) {
		    		handleException(ex);
		    	}
		    }
		});
		if (users.size() > 0) {
			return users.get(0);
		} else {
			return null;
		}
	}
	
	public static boolean authenticateUser(String userName, String password)
	{
		List<User> users = new ArrayList<User>();
		FindIterable<Document> iterable = db.getCollection(COLLECTION_NAME_USER).find(new Document(KEY_USERNAME, userName));
		iterable.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		    	try {
		    		users.add(deSerialize(document));
		    	} catch (Exception ex) {
		    		handleException(ex);
		    	}
		    }
		});
		if (users.size() == 1) {
			try {
				return MySocialUtil.comparePassword(password, users.get(0));
			} catch (Exception ex) {
				handleException(ex);
	    		return false;
			}
		} else {
			return false;
		}
	}
	
	public static void saveUser (User u) throws Exception
	{
		MySocialUtil.getCollectionForDB(COLLECTION_NAME_USER).insertOne(serialize(u));
	}
	
	private static Document serialize(User u) throws Exception
	{
		return new Document()
				.append(KEY_USERNAME, u.getUserName())
				.append(KEY_PASSWORD, MySocialUtil.encrypt(u.getPassword()))
				.append(KEY_FIRST, u.getFirst())
				.append(KEY_LAST, u.getLast())
				.append(KEY_EMAIL, u.getEmail())
				.append(KEY_COMPANYID, u.getCompanyId())
				.append(KEY_SITEID, u.getSiteId())
				.append(KEY_DEPTID, u.getDeptId());
	}
	
	private static User deSerialize(Document document) throws Exception
	{
		ObjectId id = (ObjectId) document.get(KEY_ID);
        String userName = (String) document.get(KEY_USERNAME);
        String password = (String) document.get(KEY_PASSWORD);
        String first = (String) document.get(KEY_FIRST);
        String last = (String) document.get(KEY_LAST);
        String email = (String) document.get(KEY_EMAIL);
        String companyId = ((ObjectId) document.get(KEY_COMPANYID)).toHexString();
        String siteId = ((ObjectId) document.get(KEY_SITEID)).toHexString();
        String deptId = ((ObjectId) document.get(KEY_DEPTID)).toHexString();
        
        User u = new User();
        u.setId(id);
        u.setUserName(userName);
        u.setPassword(password);
        u.setFirst(first);
        u.setLast(last);
        u.setEmail(email);
        u.setCompanyId(new ObjectId(companyId));
        u.setSiteId(new ObjectId(siteId));
        u.setDeptId(new ObjectId(deptId));
        return u;
	}
}
