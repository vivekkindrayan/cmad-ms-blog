package com.mysocial.db;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mysocial.util.Constants.*;
import static com.mysocial.util.MySocialUtil.*;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mysocial.beans.Blog;
import com.mysocial.beans.Comment;
import com.mysocial.util.MySocialUtil;

public class BlogPersistence {

	private static final MongoDatabase db = MySocialUtil.getMongoDB();
	
	private static final String KEY_ID = "_id";
	private static final String KEY_USERID = "userId";
	private static final String KEY_FIRST = "userFirst";
	private static final String KEY_LAST = "userLast";
	private static final String KEY_TITLE = "title";
	private static final String KEY_CONTENT = "content";
	private static final String KEY_TAGS = "tags";
	private static final String KEY_DATE = "date";
	private static final String KEY_COMMENTS = "comments";
	
	public static final String KEY_TAGID = "tagId";
	
	public static List<Blog> getAllBlogs()
	{
		List<Blog> blogs = new ArrayList<Blog>();
		FindIterable<Document> iterable = db.getCollection(COLLECTION_NAME_BLOG).find();
		iterable.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		    	try {
		    		blogs.add(populateCommentsForBlog(deSerialize(document)));
		    	} catch (Exception ex) {
		    		handleException(ex);
		    	}
		    }
		});
		return blogs;
	}
	
	public static List<Blog> getBlogsForTag (String tag)
	{
		List<Blog> blogs = new ArrayList<Blog>();
		BasicDBObject query = new BasicDBObject();
		Pattern regex = Pattern.compile(tag.toLowerCase()); 
		query.put(KEY_TAGS, regex);
		FindIterable<Document> iterable = db.getCollection(COLLECTION_NAME_BLOG).find(query);
		iterable.forEach(new Block<Document>() {	
		    @Override
		    public void apply(final Document document) {
		    	try {
		    		blogs.add(populateCommentsForBlog(deSerialize(document)));
		    	} catch (Exception ex) {
		    		handleException(ex);
		    	}
		    }
		});
		return blogs;
	}
	
	public static List<Blog> getBlogsForUserId (String userId)
	{
		List<Blog> blogs = new ArrayList<Blog>();
		FindIterable<Document> iterable = db.getCollection(COLLECTION_NAME_BLOG).find(new Document(KEY_USERID, new ObjectId(userId)));
		iterable.forEach(new Block<Document>() {	
		    @Override
		    public void apply(final Document document) {
		    	try {
		    		blogs.add(populateCommentsForBlog(deSerialize(document)));
		    	} catch (Exception ex) {
		    		handleException(ex);
		    	}
		    }
		});
		return blogs;
	}
	
	public static Blog getBlogForId (String blogId)
	{
		List<Blog> blogs = new ArrayList<Blog>();
		FindIterable<Document> iterable = db.getCollection(COLLECTION_NAME_BLOG).find(new Document(KEY_ID, new ObjectId(blogId)));
		iterable.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		    	try {
		    		blogs.add(populateCommentsForBlog(deSerialize(document)));
		    	} catch (Exception ex) {
		    		handleException(ex);
		    	}
		    }
		});
		if (blogs.size() > 0) {
			return blogs.get(0);
		} else {
			return null;
		}
	}
	
	public static Blog populateCommentsForBlog (Blog b)
	{
		List<Comment> commentsList = CommentPersistence.getCommentsForBlogId(b.getId());
		if (commentsList == null || commentsList.size() == 0) {
			b.setComments(new ArrayList<Comment>());
		} else {
			b.setComments(commentsList);
		}
		return b;
	}
	
	public static void saveBlog(Blog b) throws Exception
	{
		MySocialUtil.getCollectionForDB(COLLECTION_NAME_BLOG).insertOne(serialize(b));
	}
	
	private static Document serialize(Blog b) throws Exception
	{
		Document blog = new Document()
				.append(KEY_USERID, b.getUserId())
				.append(KEY_FIRST, b.getUserFirst())
				.append(KEY_LAST, b.getUserLast())
				.append(KEY_TITLE, b.getTitle())
				.append(KEY_CONTENT, b.getContent())
				.append(KEY_TAGS, b.getTags().toLowerCase())
				.append(KEY_DATE, b.getDate());
		
		if (b.getComments() != null && b.getComments().size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for (Comment c : b.getComments()) {
				Document comment = CommentPersistence.serialize(c);
				sb.append(comment.toJson());
			}
			sb.append("]");
			String commentJson = sb.toString();
			blog.append(KEY_COMMENTS, commentJson);
			System.out.println("Adding comments " + commentJson + " to blog");
		}
		
		System.out.println("Saving blog " + blog.toJson());
		return blog;
	}
	
	private static Blog deSerialize(Document document) throws Exception
	{
		ObjectId id = (ObjectId) document.get(KEY_ID);
		String userId = ((ObjectId) document.get(KEY_USERID)).toHexString();
        String first = (String) document.get(KEY_FIRST);
        String last = (String) document.get(KEY_LAST);
        String title = (String) document.get(KEY_TITLE);
        String content = (String) document.get(KEY_CONTENT);
        String tags = (String) document.get(KEY_TAGS);
        String date = (String) document.get(KEY_DATE);
        
        Blog b = new Blog();
        b.setId(id.toHexString());
        b.setTitle(title);
        b.setContent(content);
        b.setTags(tags);
        b.setDate(date);
        b.setUserFirst(first);
        b.setUserLast(last);
        b.setUserId(new ObjectId(userId));
        
        return b;
	}
	
}
