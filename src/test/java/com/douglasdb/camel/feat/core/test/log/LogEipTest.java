package com.douglasdb.camel.feat.core.test.log;

import com.douglasdb.camel.feat.core.log.LogEipRoute;
import lombok.SneakyThrows;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @author dbatista
 */
public class LogEipTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new LogEipRoute();
    }

    @Test
    @SneakyThrows
    public void testLogEip() {
        super.getMockEndpoint("mock:result").expectedMessageCount(1);
        super.template.sendBody("direct:start", "Hello Camel");
        assertMockEndpointsSatisfied();
    }

    @Test
    @SneakyThrows
    public void testLogEipLevel() {
        super.getMockEndpoint("mock:result").expectedMessageCount(1);
        super.template.sendBody("direct:startLevel", "Hello Camel");
        assertMockEndpointsSatisfied();
    }

    @Test
    @SneakyThrows
    public void testLogEipName () {
        super.getMockEndpoint("mock:result").expectedMessageCount(1);
        super.template.sendBody("direct:startName", "Hello Camel");
        assertMockEndpointsSatisfied();
    }
}
