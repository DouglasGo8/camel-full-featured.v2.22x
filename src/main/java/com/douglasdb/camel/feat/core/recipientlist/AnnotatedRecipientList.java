package com.douglasdb.camel.feat.core.recipientlist;

import java.util.function.Predicate;

import org.apache.camel.RecipientList;
import org.apache.camel.language.XPath;

/**
 * 
 * @author douglasdias
 *
 */
public class AnnotatedRecipientList {

	/**
	 * 
	 * @param customer
	 * @return
	 */
	@RecipientList
	public String[] route(@XPath("/order/@customer") String customer) {

		final Predicate<String> p = (c) -> customer.equals(c);

		
		System.out.println(customer);
		
		return p.test("honda") ? 
				new String[] { "acmq:queue:accounting", "acmq:queue:production" }
				: new String[] { "acmq:queue:accounting" };

	}

}
