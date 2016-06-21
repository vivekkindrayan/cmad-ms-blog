package com.mysocial.beans;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

@Entity("user")
@Indexes(
    @Index(value = "userName", fields = @Field("userName"))
)
public class User {

	@Id
    private ObjectId id;
	private String userName;
	private String password;
	private String email;
	private String first;
	private String last;
	private ObjectId companyId;
	private ObjectId siteId;
	private ObjectId deptId;
	
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) throws Exception {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirst() {
		return first;
	}
	public void setFirst(String first) {
		this.first = first;
	}
	public String getLast() {
		return last;
	}
	public void setLast(String last) {
		this.last = last;
	}
	public ObjectId getCompanyId() {
		return companyId;
	}
	public void setCompanyId(ObjectId companyId) {
		this.companyId = companyId;
	}
	public ObjectId getSiteId() {
		return siteId;
	}
	public void setSiteId(ObjectId siteId) {
		this.siteId = siteId;
	}
	public ObjectId getDeptId() {
		return deptId;
	}
	public void setDeptId(ObjectId deptId) {
		this.deptId = deptId;
	}
	
}
