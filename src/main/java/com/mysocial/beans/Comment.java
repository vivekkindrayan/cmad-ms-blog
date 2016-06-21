package com.mysocial.beans;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

public class Comment {

	@Id
    private String id;
	private String content;
	private String date;
    private ObjectId blogId;
    private ObjectId userId;
    private String userLast;
    private String userFirst;
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ObjectId getUserId() {
		return userId;
	}

	public void setUserId(ObjectId userId) {
		this.userId = userId;
	}

	public ObjectId getBlogId() {
		return blogId;
	}

	public void setBlogId(ObjectId blogId) {
		this.blogId = blogId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUserLast() {
		return userLast;
	}

	public void setUserLast(String userLast) {
		this.userLast = userLast;
	}

	public String getUserFirst() {
		return userFirst;
	}

	public void setUserFirst(String userFirst) {
		this.userFirst = userFirst;
	}
    
    
}
