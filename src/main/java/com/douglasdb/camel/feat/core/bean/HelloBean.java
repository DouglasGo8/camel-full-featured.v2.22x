package com.douglasdb.camel.feat.core.bean;

import org.slf4j.LoggerFactory;



public class HelloBean {
	
	
	
	
	public String hello(String name) {
		LoggerFactory.getLogger(HelloBean.class)
			.info("Invoking HelloBean with {}", name);
		return "Hello " + name;
	}
}
