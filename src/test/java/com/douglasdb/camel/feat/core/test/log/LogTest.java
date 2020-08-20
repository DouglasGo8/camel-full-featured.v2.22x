package com.douglasdb.camel.feat.core.test.log;

import com.douglasdb.camel.feat.core.log.LogRoute;
import lombok.SneakyThrows;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @author dodiaba
 */
public class LogTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new LogRoute();
    }

    @Test
    @SneakyThrows
    public void testLog() {
        super.getMockEndpoint("mock:result").expectedMessageCount(1);
        //
        super.template.sendBody("direct:start", "Hello Camel");
        assertMockEndpointsSatisfied();
    }

    @Test
    @SneakyThrows
    public void testLogAll() {
        super.getMockEndpoint("mock:result").expectedMessageCount(1);
        //
        super.template.sendBody("direct:startAll", "Hello Camel");
        assertMockEndpointsSatisfied();
    }
}
