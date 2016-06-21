package com.mysocial.db;

import static com.mysocial.util.Constants.*;
import static com.mysocial.util.MySocialUtil.handleException;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mysocial.beans.Comment;
import com.mysocial.util.MySocialUtil;


public class CommentPersistence {
	
	private static final MongoDatabase db = MySocialUtil.getMongoDB();
	
	private static final String KEY_ID = "_id";
	private static final String KEY_USERID = "userId";
	private static final String KEY_FIRST = "userFirst";
	private static final String KEY_LAST = "userLast";
	private static final String KEY_CONTENT = "content";
	private static final String KEY_DATE = "date";
	public static final String KEY_BLOG_ID = "blogId";
	
	public static void saveComment (Comment c) throws Exception
	{
		MySocialUtil.getCollectionForDB(COLLECTION_NAME_COMMENT).insertOne(serialize(c));
	}
	
	public static List<Comment> getCommentsForBlogId (String blogId)
	{
		List<Comment> comments = new ArrayList<Comment>();
		FindIterable<Document> iterable = db.getCollection(COLLECTION_NAME_COMMENT).find(new Document(KEY_BLOG_ID, new ObjectId(blogId)));
		iterable.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		    	try {
		    		comments.add(deSerialize(document));
		    	} catch (Exception ex) {
		    		handleException(ex);
		    	}
		    }
		});
		return comments;
	}
	
	static Document serialize(Comment c) throws Exception
	{
		return new Document()
			.append(KEY_USERID, c.getUserId())
			.append(KEY_FIRST, c.getUserFirst())
			.append(KEY_LAST, c.getUserLast())
			.append(KEY_CONTENT, c.getContent())
			.append(KEY_DATE, c.getDate())
			.append(KEY_BLOG_ID, c.getBlogId());
	}
	
	static Comment deSerialize(Document document) throws Exception
	{
		ObjectId id = (ObjectId) document.get(KEY_ID);
		ObjectId userId = (ObjectId) document.get(KEY_USERID);
        String first = (String) document.get(KEY_FIRST);
        String last = (String) document.get(KEY_LAST);
        String content = (String) document.get(KEY_CONTENT);
        String date = (String) document.get(KEY_DATE);
        ObjectId blogId = (ObjectId) document.get(KEY_BLOG_ID);
        
        Comment c = new Comment();
        c.setId(id.toHexString());
        c.setUserFirst(first);
        c.setUserLast(last);
        c.setContent(content);
        c.setDate(date);
        c.setBlogId(blogId);
        c.setUserId(userId);
        return c;
	}
	

}
