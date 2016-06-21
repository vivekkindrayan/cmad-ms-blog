package com.mysocial.util;

public interface Constants {

	public static final String CONFIG_PROPS_FILE = "config.properties";
	
	public static final String PROP_KEY_MONGODB_HOST = "MONGODB_HOST";
	public static final String PROP_KEY_MONGODB_PORT = "MONGODB_PORT";
	public static final String PROP_KEY_HTTP_PORT = "HTTP_PORT";
	
	public static final String MONGODB_URL_PREFIX = "mongodb://";
	
	public static final String DB_NAME = "mysocialdb";
	public static final String DB_PACKAGE_NAME = "com.mysocial";
	
	public static final String COOKIE_HEADER = "Set-Cookie";
	public static final String SESSION_USER_KEY = "loggedInUser";
	
	public static final String COLLECTION_NAME_COMPANY = "company";
	public static final String COLLECTION_NAME_SITE = "site";
	public static final String COLLECTION_NAME_DEPARTMENT = "department";
	public static final String COLLECTION_NAME_USER = "user";
	public static final String COLLECTION_NAME_BLOG = "blog";
	public static final String COLLECTION_NAME_COMMENT = "comment";
	
	public static final String REST_URL_PREFIX = "/Services/rest/";
	public static final String REST_URL_GET_COMPANIES = COLLECTION_NAME_COMPANY;
	public static final String REST_URL_GET_COMPANY = COLLECTION_NAME_COMPANY + "/:companyId";
	public static final String REST_URL_GET_SITES = COLLECTION_NAME_SITE;
	public static final String REST_URL_GET_SITE = COLLECTION_NAME_SITE + "/:siteId";
	public static final String REST_URL_GET_DEPARTMENTS = COLLECTION_NAME_DEPARTMENT;
	public static final String REST_URL_GET_DEPARTMENT = COLLECTION_NAME_DEPARTMENT + "/:deptId";
	
	public static final String REST_URL_GET_SITES_FOR_COMPANY = REST_URL_GET_COMPANY + "/sites";
	public static final String REST_URL_GET_DEPTS_FOR_SITES = REST_URL_GET_SITES_FOR_COMPANY + "/:siteId/departments"; 
	
	public static final String REST_URL_REGISTER = "user/register";
	public static final String REST_URL_LOGIN = "user/auth";
	public static final String REST_URL_LOAD_SIGNED_IN_USER = "user";
	
	public static final String REST_URL_GET_BLOGS = "blogs";
	public static final String REST_URL_SUBMIT_BLOG = REST_URL_GET_BLOGS;
	public static final String REST_URL_SUBMIT_COMMENT = REST_URL_GET_BLOGS + "/:blogId/comments";
	public static final String QUERY_STRING_TAG = "tag=";
	
	
	public static final String RESPONSE_HEADER_CONTENT_TYPE = "content-type";
	public static final String RESPONSE_HEADER_JSON = "application/json";
	
	public static final int DEFAULT_WORKER_POOL_SIZE = 10;
}
