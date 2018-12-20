package com.douglasdb.camel.feat.core.domain;

/**
 * 
 * @author Administrator
 *
 */
public class MyBean {

	public MyBean() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param name
	 * @param hipster
	 * @return
	 */
	public String sayHello(String name, boolean hipster) {
		return (hipster) ? ("Yo ".concat(name)) : ("Hello ".concat(name));
	}

}
