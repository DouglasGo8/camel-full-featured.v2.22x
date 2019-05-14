package com.douglasdb.camel.feat.core.bean.bean;

import org.apache.camel.builder.RouteBuilder;
import com.douglasdb.camel.feat.core.domain.bean.HelloBean;
/**
 * 
 * @author Administrador
 *
 */
public class InvokeWithBeanRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
	
		from("direct:hello")
			.log("${body}")
			.bean(HelloBean.class, "hello");
	}
	
	
	
	
	

}
