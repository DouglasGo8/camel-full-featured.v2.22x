package com.douglasdb.camel.feat.core.test.loadbalancer;

import com.douglasdb.camel.feat.core.loadbalancer.FailoverInheritErrorHandlerLoadBalancerRouter;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Administrator
 */
public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        
        return new FailoverInheritErrorHandlerLoadBalancerRouter();
        // LoadBalancer();
        // CircuitBreakerLoadBalancerRouter();
    }

    /**
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

    /**
     *
     */
    @Test
    @Ignore
    public void testLoadBalancer() throws InterruptedException {

        MockEndpoint a = super.getMockEndpoint("mock:a");

        a.expectedBodiesReceived("Camel rocks", "Cool");

        MockEndpoint b = super.getMockEndpoint("mock:b");

        b.expectedBodiesReceived("Hello", "Bye");

        super.template.sendBodyAndHeader("direct:start", "Hello", "type", "silver");
        super.template.sendBodyAndHeader("direct:start", "Camel rocks", "type", "gold");
        super.template.sendBodyAndHeader("direct:start", "Bye", "type", "bronze");
        super.template.sendBodyAndHeader("direct:start", "Cool", "type", "gold");

        assertMockEndpointsSatisfied();

    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Test
    public void testLoadBalancerWithFailoverErrorHandler() throws Exception {

        /*super.context.getRouteDefinition("start")
            .adviceWith(super.context, new RouteBuilder() {
            
                @Override
                public void configure() throws Exception {
                
                    super.interceptSendToEndpoint("direct:a")
                        .choice()
                            .when(body().contains("Kaboom"))
                                .throwException(new IllegalArgumentException("Damn"))
                            .end()
                        .end();
                }
        });

        super.context.start();*/

        //MockEndpoint a = super.getMockEndpoint("mock:a");

       // a.expectedBodiesReceived("Hello");

        //MockEndpoint b = super.getMockEndpoint("mock:b");

        //b.expectedBodiesReceived("Hello");



        super.template.sendBody("direct:start", "Hello");
        //super.template.sendBody("direct:start", "Kaboom");


        assertMockEndpointsSatisfied();

    }

    /**
     * @param endpoint
     * @param param
     */
    private void sendMessage(final String endpoint, Consumer<Exchange> param) {
        super.template.send(endpoint, (exchante) -> param.accept(exchante));
    }

}
