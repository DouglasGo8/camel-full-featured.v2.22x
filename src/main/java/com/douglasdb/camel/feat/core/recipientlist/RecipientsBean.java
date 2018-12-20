package com.douglasdb.camel.feat.core.recipientlist;

import java.util.function.Predicate;

import org.apache.camel.Exchange;
import org.apache.camel.language.XPath;

/**
 * 
 * @author douglasdias
 *
 */
public class RecipientsBean {

	public RecipientsBean() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param customer
	 * @return
	 */
	public String[] recipients(@XPath("/order/@customer") String customer) {

		 //final Predicate<String> p = (c) ->  customer.equals(c);
		
		// System.out.println(customer);

		return isCustomerFromBrand("honda", customer::equals)
				? new String[] { "acmq:queue:accounting", "acmq:queue:production" } : 
					new String[] { "acmq:queue:accounting" };

		/*
		 * return p.test("honda") ? new String[] { "acmq:queue:accounting",
		 * "acmq:queue:production" } : new String[] { "acmq:queue:accounting" };
		 */

	}

	/**
	 *
	 * @param exchange
	 * @return
	 */
	public String getEndpointsToRouteMessageTo(Exchange exchange) {

		String orderType = exchange.getIn().getHeader("orderType", String.class);

		if (orderType == null) {
            return "direct:unrecognized";
		} else if (orderType.equals("priority")) {
			return "direct:order.priority,direct:billing";
		} else {
			return "direct:order.normal,direct:billing";
		}
	}

	/**
	 * 
	 * @param brand
	 * @param args
	 * @return
	 */
	private boolean isCustomerFromBrand(final String brand, final Predicate<String> args) {
		
		return args.test(brand);

	}
}
