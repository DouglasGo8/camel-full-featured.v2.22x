package com.douglasdb.camel.feat.core.structuring.route.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * 
 * @author douglasdias
 *
 */
public class LongRunningProcessor implements Processor {
	
	public LongRunningProcessor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		
		Thread.sleep(3000);
		exchange
			.getIn().setBody("Long running process finished");
		
	}
	

}
