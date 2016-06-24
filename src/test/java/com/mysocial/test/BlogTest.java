package com.mysocial.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mysocial.verticles.BlogVerticle;

import io.vertx.ext.web.Router;

public class BlogTest {
	
	BlogVerticle bv;
	Router r;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		bv = new BlogVerticle();
		r = Router.router(null);
	}

	@After
	public void tearDown() throws Exception {
		bv = null;
	}

	@Test
	public final void testStartFutureOfVoid() {
		System.out.println("Into -> testStartFutureOfVoid");
	}

	@Test
	public final void testStopFuture() {
		System.out.println("Into -> testStopFuture");
	}

	@Test
	public final void testDeploy() {
		System.out.println("Into -> testDeploy");
		assertNotNull(bv);
		assertNotNull(r);
	}

	@Test
	public final void testMain() {
		System.out.println("Into -> testMain");
		assertNotNull(bv);
		
	}

}
