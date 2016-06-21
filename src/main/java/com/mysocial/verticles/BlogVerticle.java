package com.mysocial.verticles;

import static com.mysocial.util.Constants.*;

import com.mysocial.verticles.handlers.AllBlogsHandler;
import com.mysocial.verticles.handlers.SubmitBlogHandler;
import com.mysocial.verticles.handlers.SubmitCommentHandler;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.ErrorHandler;

public class BlogVerticle extends AbstractVerticle {

	public static final String VERTICLE_NAME = BlogVerticle.class.getName();
	public static final int HTTP_PORT = 9092;
	
	@Override
	public void start(Future<Void> startFuture)
	{
		System.out.println(VERTICLE_NAME + " started");
		startFuture.complete();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void stop(Future stopFuture) throws Exception
	{
		System.out.println(VERTICLE_NAME + " stopped");
		stopFuture.complete();
	}
	
	public void deploy(Vertx vertx, Router router) throws Exception
	{
		vertx.deployVerticle(VERTICLE_NAME, new Handler<AsyncResult<String>>() {
			public void handle(AsyncResult<String> event) {
				
				router.post(REST_URL_PREFIX + REST_URL_SUBMIT_BLOG).handler(new SubmitBlogHandler(vertx));
				router.get(REST_URL_PREFIX + REST_URL_GET_BLOGS).handler(new AllBlogsHandler(vertx));
				router.post(REST_URL_PREFIX + REST_URL_SUBMIT_COMMENT).handler(new SubmitCommentHandler(vertx));
				
				System.out.println(VERTICLE_NAME + " deployment complete");
			}
		});
	}
	
	public static void main(String[] args) throws Exception 
	{
		VertxOptions options = new VertxOptions().setWorkerPoolSize(DEFAULT_WORKER_POOL_SIZE);
		Vertx vertx = Vertx.vertx(options);
		HttpServer server = vertx.createHttpServer();
		Router router = Router.router(vertx);
		
		router.route().handler(CookieHandler.create());
		router.route().handler(BodyHandler.create());
		router.route().failureHandler(ErrorHandler.create());
		
		BlogVerticle bv = new BlogVerticle();
		bv.deploy(vertx, router);
		server.requestHandler(router::accept).listen(HTTP_PORT);
	}
}
