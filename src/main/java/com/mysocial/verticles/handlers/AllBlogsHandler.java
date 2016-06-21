package com.mysocial.verticles.handlers;

import static com.mysocial.util.Constants.*;

import java.util.List;

import com.mysocial.beans.Blog;
import com.mysocial.db.BlogPersistence;
import com.mysocial.util.MySocialUtil;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class AllBlogsHandler implements Handler<RoutingContext> {
	
	Vertx vertx;
	
	public AllBlogsHandler(Vertx vertx) {
		this.vertx = vertx;
	}
	
	@SuppressWarnings("unchecked")
	public void handle(RoutingContext routingContext) {
		
		String query = routingContext.request().query();
		
		vertx.executeBlocking(future -> {
			String tag = null;
			if (query != null && !query.isEmpty() && query.contains(QUERY_STRING_TAG)) {
				String[] queryString = query.split("=");
				if (queryString.length == 2) {
					tag = queryString[1];
				}
			}
			List<Blog> blogs = null;
			if (tag != null && !tag.isEmpty()) {
				blogs = BlogPersistence.getBlogsForTag(tag);
			} else {
				blogs = BlogPersistence.getAllBlogs();
			}
			future.complete(blogs);
		}, resultHandler -> {
			HttpServerResponse response = routingContext.response();
			List<Blog> blogs = (List<Blog>) resultHandler.result();
			if (resultHandler.succeeded()) {
				response.putHeader(RESPONSE_HEADER_CONTENT_TYPE, RESPONSE_HEADER_JSON).end(Json.encodePrettily(blogs));
			} else {
				MySocialUtil.handleFailure(resultHandler, this.getClass());
			}
		});
	}

}
