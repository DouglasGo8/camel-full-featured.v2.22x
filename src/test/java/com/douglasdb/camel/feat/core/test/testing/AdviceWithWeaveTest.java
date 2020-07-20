package com.douglasdb.camel.feat.core.test.testing;

import lombok.SneakyThrows;
import org.apache.camel.Message;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class AdviceWithWeaveTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:in").id("slowRoute")
                        .process(exchange -> {
                            Thread.sleep(10000);
                            final Message in = exchange.getIn();
                            in.setBody("Slow reply to: " + in.getBody());
                        }).id("reallySlowProcessor")
                        .to("mock:out");
            }
        };
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Test
    @SneakyThrows
    public void testSubstitutionOfSlowProcessor() {
        super.context.getRouteDefinition("slowRoute")
                .adviceWith(super.context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() {
                        weaveById("reallySlowProcessor")
                                .replace()
                                .transform().simple("Fast reply to: ${body}");
                    }
                });
        //
        super.context.start();
        //
        final MockEndpoint mockOut = super.getMockEndpoint("mock:out");
        mockOut.message(0).body().isEqualTo("Fast reply to: request");
        //
        super.sendBody("direct:in", "request");
        assertMockEndpointsSatisfied();
    }
}
