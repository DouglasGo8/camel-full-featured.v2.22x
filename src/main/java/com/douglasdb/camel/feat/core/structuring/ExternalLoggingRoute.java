package com.douglasdb.camel.feat.core.structuring;

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang3.Validate;

/**
 * 
 * @author douglasdias
 *
 */
public class ExternalLoggingRoute extends RouteBuilder {

	public static final String LOGGING_THREAD_NAME = "logging.threadName";
	public static final String LOG_MESSAGE_TO_BACKEND_SYSTEM = "logMessageToBackendSystem";
    
	private final String logMessageSourceUri;
	/**
	 * 
	 * @param endpointScheme
	 */
	public ExternalLoggingRoute(String endpointScheme) {
		// TODO Auto-generated constructor stub
		Validate.notEmpty(endpointScheme, "endpointScheme is null or empty");
		this.logMessageSourceUri = endpointScheme + ":" + LOG_MESSAGE_TO_BACKEND_SYSTEM;
	}
	/**
	 * 
	 */
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		from(logMessageSourceUri)
			.setHeader(LOGGING_THREAD_NAME, simple("${threadName}"))
			.log("##externalLog - Thread Name.: ${threadName}")
			.delay(1000)
			.log("Logged message to backend system ${body} by thread[${threadName}]")
			.setBody(simple("logging: ${body}"))
			.to("mock:out");
	}
	
	

}
