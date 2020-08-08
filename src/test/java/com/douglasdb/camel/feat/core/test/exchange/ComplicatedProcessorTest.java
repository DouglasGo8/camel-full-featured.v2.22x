package com.douglasdb.camel.feat.core.test.exchange;

import com.douglasdb.camel.feat.core.exchange.ComplicatedProcessor;
import lombok.SneakyThrows;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ComplicatedProcessorTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:in").process(new ComplicatedProcessor()).to("mock:out");
            }
        };
    }

    @Test
    @SneakyThrows
    public void testPrepend() {
        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.message(0).body().isEqualTo("SOMETHING text");
        mockOut.message(0).header("actionTaken").isEqualTo(true);

        final Map<String, Object> headers = new HashMap<>();
        headers.put("action", "prepend");

        template.sendBodyAndHeaders("direct:in", "text", headers);

        assertMockEndpointsSatisfied();
    }
}
