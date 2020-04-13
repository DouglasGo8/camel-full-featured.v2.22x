package com.douglasdb.camel.feat.core.test.loadbalancer;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;
import com.douglasdb.camel.feat.core.loadbalancer.TopicLoadBalancerRouter;

/**
 * @author Administrator
 */
public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {

        return new TopicLoadBalancerRouter();
        // StickyLoadBalancertRouter();
        // LoadBalancerRandomStrategyRouter();
        // LoadBalancerRoundRobin();
        // FailoverRoundRobinLoadBalancerRouter();
        // FailoverLoadBalancer();
        // LoadBalancerCustomStrategyRouter();
        // FailoverInheritErrorHandlerLoadBalancerRouter();
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

    /**
     * 
     */
    @Override
    public boolean isUseAdviceWith() {
        return false; // true;
    }

    @Test
    @Ignore
    public void testLoadBalancerWithFailoverErrorHandler() throws Exception {

        super.context.getRouteDefinition("start").adviceWith(super.context, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                super.interceptSendToEndpoint("direct:a")
                        // .log("Intercepting body with ${body}")
                        .choice().when(body().contains("Kaboom")).throwException(new IllegalArgumentException("Damn"))
                        .end().end();
            }
        });

        super.context.start();

        MockEndpoint a = super.getMockEndpoint("mock:a");

        // a.setExpectedMessageCount(1);
        a.expectedBodiesReceived("Hello");

        MockEndpoint b = super.getMockEndpoint("mock:b");

        b.expectedBodiesReceived("Kaboom");

        super.template.sendBody("direct:start", "Hello");
        super.template.sendBody("direct:start", "Kaboom");

        assertMockEndpointsSatisfied();

    }

    /**
     * 
     */
    @Test
    @Ignore
    public void testLoadBalancerWithFailover() throws InterruptedException {

        final MockEndpoint mock_a = super.getMockEndpoint("mock:a");
        final MockEndpoint mock_b = super.getMockEndpoint("mock:b");

        mock_a.setExpectedMessageCount(1);
        mock_a.expectedBodiesReceived("Hello", "Cool", "Bye");

        mock_b.expectedBodiesReceived("Kaboom");

        super.template.sendBody("direct:start", "Hello");
        super.template.sendBody("direct:start", "Kaboom");
        super.template.sendBody("direct:start", "Cool");
        super.template.sendBody("direct:start", "Bye");

        assertMockEndpointsSatisfied();
    }

    /**
     * 
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testLoadBalancerRoundRobinWithFailover() throws InterruptedException {

        final MockEndpoint mock_a = super.getMockEndpoint("mock:a");
        final MockEndpoint mock_b = super.getMockEndpoint("mock:b");

        mock_a.setExpectedMessageCount(1);
        mock_a.expectedBodiesReceived("Hello", "Boom");

        mock_b.expectedBodiesReceived("Bye", "Kaboom");

        super.template.sendBody("direct:start", "Hello");
        super.template.sendBody("direct:start", "Boom");
        super.template.sendBody("direct:start", "Bye");
        super.template.sendBody("direct:start", "Kaboom");

        assertMockEndpointsSatisfied();
    }

    /**
     * 
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testLoadBalancerRoundRobin() throws InterruptedException {

        final MockEndpoint mock_a = super.getMockEndpoint("mock:a");
        final MockEndpoint mock_b = super.getMockEndpoint("mock:b");

        mock_a.setExpectedMessageCount(1);
        mock_a.expectedBodiesReceived("Hello", "Cool");

        mock_b.expectedBodiesReceived("Camel Rocks", "Bye");

        super.template.sendBody("direct:start", "Hello");
        super.template.sendBody("direct:start", "Camel Rocks");
        super.template.sendBody("direct:start", "Cool");
        super.template.sendBody("direct:start", "Bye");

        assertMockEndpointsSatisfied();
    }

    /**
     * 
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testLoadBalancerRandomStrategy() throws InterruptedException {

        super.template.sendBody("direct:start", "Hello");
        super.template.sendBody("direct:start", "Camel Rocks");
        super.template.sendBody("direct:start", "Cool");
        super.template.sendBody("direct:start", "Bye");

    }

    /**
     * 
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testLoadBalancerWithSticky() throws InterruptedException {

        final MockEndpoint mock_a = super.getMockEndpoint("mock:a");
        final MockEndpoint mock_b = super.getMockEndpoint("mock:b");

        mock_a.setExpectedMessageCount(1);
        mock_a.expectedBodiesReceived("Hello", "Bye");

        mock_b.expectedBodiesReceived("Camel Rocks", "Cool");

        super.template.sendBodyAndHeader("direct:start", "Hello", "type", "gold");
        super.template.sendBodyAndHeader("direct:start", "Camel Rocks", "type", "silver");
        super.template.sendBodyAndHeader("direct:start", "Cool", "type", "silver");
        super.template.sendBodyAndHeader("direct:start", "Bye", "type", "gold");

        assertMockEndpointsSatisfied();
    }

    /**
     * Works a Broadcast message
     * @throws InterruptedException
     */
    @Test
    public void testLoadBalancerWithTopicStrategy() throws InterruptedException {

        final MockEndpoint mock_a = super.getMockEndpoint("mock:a");
        final MockEndpoint mock_b = super.getMockEndpoint("mock:b");

        mock_a.setExpectedMessageCount(4);
        mock_b.setExpectedMessageCount(4);

        super.template.sendBodyAndHeader("direct:start", "Hello", "type", "gold");
        super.template.sendBodyAndHeader("direct:start", "Camel Rocks", "type", "silver");
        super.template.sendBodyAndHeader("direct:start", "Cool", "type", "silver");
        super.template.sendBodyAndHeader("direct:start", "Bye", "type", "gold");

        assertMockEndpointsSatisfied();
    }

    /**
     * @param endpoint
     * @param param
     */
    private void sendMessage(final String endpoint, Consumer<Exchange> param) {
        super.template.send(endpoint, (exchange) -> param.accept(exchange));
    }

}
