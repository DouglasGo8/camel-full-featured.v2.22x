package com.douglasdb.camel.feat.core.test.loadbalancer;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import com.douglasdb.camel.feat.core.loadbalancer.CircuitBreakerLoadBalancerRouter;

/**
 * 
 * @author Administrator
 *
 */
public class EntryPoint extends CamelTestSupport {

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		// TODO Auto-generated method stub
		return new CircuitBreakerLoadBalancerRouter();
	}

	/**
	 * 
	 * @throws InterruptedException
	 */
	@Test
	@Ignore
	public void testLoadBalancerWithCircuitBreaker() throws InterruptedException {

		final MockEndpoint mock = super.getMockEndpoint("mock:a");

		// mock.setExpectedMessageCount(1);

		mock.expectedBodiesReceived("Got through!");

		{
			final String body1 = "Kaboom";

			/**
			 * Fails One
			 */
			sendMessage("direct:start", ar -> {
				ar.getIn().setBody(body1);
			});

			/**
			 * Fails Two
			 */
			sendMessage("direct:start", ar -> {
				ar.getIn().setBody(body1);
			});
		}

		{
			final String body2 = "Blocked";

			sendMessage("direct:start", ar -> {
				ar.getIn().setBody(body2);
			});
		}

		TimeUnit.SECONDS.sleep(5);

		{
			final String body3 = "Got through!";

			sendMessage("direct:start", ar -> {
				ar.getIn().setBody(body3);
			});

		}

		assertMockEndpointsSatisfied();

	}

	@Test
	public void testLoadBalancer() {
		
	}

	/**
	 * 
	 * @param endpoint
	 * @param param
	 */
	private void sendMessage(final String endpoint, Consumer<Exchange> param) {
		super.template.send(endpoint, (exchante) -> param.accept(exchante));
	}

}
