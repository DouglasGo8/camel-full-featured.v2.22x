package com.douglasdb.camel.feat.core.test.mockreply;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.SimpleBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.language.constant.ConstantLanguage;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class MockReplyRouteTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:replying")
    private MockEndpoint mockReplying;

    @EndpointInject(uri = "mock:out")
    private MockEndpoint mockOut;


    @Produce(uri = "direct:in")
    ProducerTemplate template;

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:in").inOut("mock:replying").to("mock:out");

            }
        };
    }


    @Test
    public void testReplyingFromMockByExpression() throws InterruptedException {

        this.mockReplying.returnReplyBody(SimpleBuilder.simple("Hello ${body}"));
        this.mockOut.expectedBodiesReceived("Hello Camel");
        //
        template.sendBody("Camel");
        //
        assertMockEndpointsSatisfied();
    }

    @Test
    public void testReplyingWithHeaderFromMockByExpression() throws InterruptedException {
        mockReplying.returnReplyHeader("responder", ConstantLanguage.constant("fakeService"));
        mockOut.message(0).header("responder").isEqualTo("fakeService");
        //
        template.sendBody("Camel");
        assertMockEndpointsSatisfied();
    }

    @Test
    public void testReplyingFromMockByProcessor() throws InterruptedException {
        mockReplying.whenAnyExchangeReceived(exchange -> {
            final Message in = exchange.getIn();
            in.setBody("Hey " + in.getBody());
        });

        // the 1st exchange will be handled by a different Processor
        mockReplying.whenExchangeReceived(1, exchange -> {
            final Message in = exchange.getIn();
            in.setBody("Czesc " + in.getBody());
        });
        //
        mockOut.expectedBodiesReceived("Czesc Camel", "Hey Camel", "Hey Camel");
        //
        this.template.sendBody("Camel");
        this.template.sendBody("Camel");
        this.template.sendBody("Camel");
        //
        assertMockEndpointsSatisfied();
    }

}
