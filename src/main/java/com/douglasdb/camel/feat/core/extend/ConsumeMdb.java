package com.douglasdb.camel.feat.core.extend;

import org.apache.camel.Consume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Administrator
 *
 */
public class ConsumeMdb {

	private static final Logger LOG = LoggerFactory.getLogger(ConsumeMdb.class);

	/**
	 * 
	 * @param message
	 * @return
	 */
	@Consume(uri = "activemq:queue:sayhello")
	public String onMyMessage(String message) {
		LOG.info("Message = {}", message);
		return "Hello " + message;
	}
}
