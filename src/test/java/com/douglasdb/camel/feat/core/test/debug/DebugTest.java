package com.douglasdb.camel.feat.core.test.debug;

import com.douglasdb.camel.feat.core.debug.DebugRoute;
import lombok.SneakyThrows;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @author dbatista
 */
public class DebugTest extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
       return new DebugRoute();
    }

    @Override
    public boolean isUseDebugger() {
        return true;
    }

    @Override
    protected void debugBefore(Exchange exchange, Processor processor, ProcessorDefinition<?> definition, String id, String label) {
        log.info("Before {} with body {}", definition, exchange.getIn().getBody());
    }

    @Override
    protected void debugAfter(Exchange exchange, Processor processor, ProcessorDefinition<?> definition, String id, String label, long timeTaken) {
        log.info("After {} with body {}", definition, exchange.getIn().getBody());
    }

    @Test
    @SneakyThrows
    public void testDebug() {
        final String body = "Hello Camel's Paw";
        super.getMockEndpoint("mock:result").expectedBodiesReceived("Debug ".concat(body));
        //
        super.template.sendBody("direct:start", body);
        //
        assertMockEndpointsSatisfied();
    }


}
