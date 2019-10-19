package com.douglasdb.camel.feat.core.test.parallel;

import java.util.stream.IntStream;

import com.douglasdb.camel.feat.core.paralell.asyncprocessor.AsyncProcessorRoute;

import org.apache.camel.ExchangePattern;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @author dbatista
 */
public class AsyncProcessorTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new AsyncProcessorRoute();
    }

    @Test
    public void testAsyncProcessing() throws InterruptedException {

        MockEndpoint mock = super.getMockEndpoint("mock:out");

        mock.setExpectedCount(10);

        mock.setResultWaitTime(5000);

        IntStream.range(1, 11).forEach(n -> {
            super.template.sendBody("seda:in", ExchangePattern.InOnly, "Message[" + n + "]");
        });

        assertMockEndpointsSatisfied();

    }

}
