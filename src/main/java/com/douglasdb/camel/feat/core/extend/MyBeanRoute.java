package com.douglasdb.camel.feat.core.extend;

import org.apache.camel.builder.RouteBuilder;

import com.douglasdb.camel.feat.core.domain.MyBean;


/**
 * 
 * @author Administrator
 *
 */
public class MyBeanRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		from("direct:normal")
			.bean(MyBean.class, "sayHello(${body}, false)");
		
		from("direct:hipster")
			.bean(MyBean.class, "sayHello(${body}, true)");
		
		from("direct:undecided")
			.bean(MyBean.class, "sayHello(${body}, ${header.hipster})");
	}

}
