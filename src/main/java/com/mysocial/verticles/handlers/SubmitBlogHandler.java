package com.mysocial.verticles.handlers;

import static com.mysocial.util.Constants.*;

import java.util.ArrayList;
import java.util.Date;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysocial.beans.Blog;
import com.mysocial.beans.Comment;
import com.mysocial.beans.User;
import com.mysocial.db.BlogPersistence;
import com.mysocial.util.MySocialUtil;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.RoutingContext;

public class SubmitBlogHandler implements Handler<RoutingContext> {

	Vertx vertx;
	
	public SubmitBlogHandler(Vertx vertx) {
		this.vertx = vertx;
	}
	
	public void handle(RoutingContext routingContext) {
		
		HttpServerResponse response = routingContext.response();
		response.putHeader(RESPONSE_HEADER_CONTENT_TYPE, RESPONSE_HEADER_JSON);
		
		try {
			
			String blogJsonStr = routingContext.getBodyAsString();
			ObjectMapper blogMapper = new ObjectMapper();
			blogMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Blog b = blogMapper.readValue(blogJsonStr, Blog.class);
			b.setId(new ObjectId().toHexString());
			b.setDate(Long.toString(new Date().getTime()));
			b.setComments(new ArrayList<Comment>());
			System.out.println("Got blog from request");
			
			vertx.executeBlocking(future -> {
				User u = MySocialUtil.getSignedInUser(routingContext);
				if (u != null){
					b.setUserId(u.getId());
					b.setUserFirst(u.getFirst());
					b.setUserLast(u.getLast());
				}
				try {
					BlogPersistence.saveBlog(b);
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
					ex.printStackTrace();
					future.complete(null);
				}
				future.complete(b);
			}, resultHandler -> {
				if (resultHandler.succeeded()) {
					Blog savedBlog = (Blog) resultHandler.result();
					if (savedBlog != null && savedBlog.getUserId() != null) {
						response.setStatusCode(HttpResponseStatus.OK.code());
						routingContext.removeCookie(COOKIE_HEADER);
						routingContext.addCookie(Cookie.cookie(COOKIE_HEADER, b.getUserId().toHexString()));
						response.end();
					} else {
						System.err.println("Failed to retrieve saved blog object OR did not find a signed in user");
						response.setStatusCode(HttpResponseStatus.BAD_REQUEST.code());
						routingContext.removeCookie(COOKIE_HEADER);
						response.end();
					}

				} else {
					response.setStatusCode(HttpResponseStatus.BAD_REQUEST.code());
					routingContext.removeCookie(COOKIE_HEADER);
					response.end(resultHandler.cause().getMessage());
					MySocialUtil.handleFailure(resultHandler, this.getClass());
				}
			});
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace();
			response.setStatusCode(HttpResponseStatus.BAD_REQUEST.code());
			routingContext.removeCookie(COOKIE_HEADER);
			response.end();
		}
	}
	
}
