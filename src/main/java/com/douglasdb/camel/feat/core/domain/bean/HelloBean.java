package com.douglasdb.camel.feat.core.domain.bean;

import org.slf4j.LoggerFactory;

import lombok.NoArgsConstructor;


/**
 * @author DouglasDb
 */
@NoArgsConstructor
public class HelloBean {
	
	public String hello(String name) {
		LoggerFactory.getLogger(HelloBean.class)
			.info("Invoking HelloBean with {}", name);
		return "Hello " + name;
	}
}
