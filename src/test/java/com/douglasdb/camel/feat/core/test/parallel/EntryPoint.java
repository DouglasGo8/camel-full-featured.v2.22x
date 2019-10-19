package com.douglasdb.camel.feat.core.test.parallel;

import com.douglasdb.camel.feat.core.paralell.endpointconsumers.EndpointConsumersSedaRoute;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spi.Synchronization;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

/**
 * @author dbatista
 */
public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new EndpointConsumersSedaRoute();
        // SlowProcessingRoute();
        // SometimesAsyncProcessorRoute();
        // AsyncProcessorRoute();
    }

    @Test
    @Ignore
    public void testAsyncProcessing() throws InterruptedException {

        MockEndpoint mock = super.getMockEndpoint("mock:out");

        mock.setExpectedCount(10);
        mock.setResultWaitTime(5000);

        IntStream.range(1, 11).forEach(n -> {
            super.template.sendBody("seda:in", ExchangePattern.InOnly, "Message[" + n + "]");
        });

        assertMockEndpointsSatisfied();

    }


    @Test
    @Ignore
    public void testSyncProcessing() throws InterruptedException {

        MockEndpoint mock = super.getMockEndpoint("mock:out");

        mock.setExpectedCount(10);
        mock.setResultWaitTime(5000);

        IntStream.range(1, 11).forEach(n -> {
            super.template.sendBody("direct:sync", ExchangePattern.InOnly, "Message[" + n + "]");
        });

        assertMockEndpointsSatisfied();

    }


    @Test
    @Ignore
    public void testAsyncProcessingSomething() throws InterruptedException {

        MockEndpoint mock = super.getMockEndpoint("mock:out");

        mock.setExpectedCount(10);
        mock.setResultWaitTime(5000);

        IntStream.range(1, 11).forEach(n -> {
            super.template.sendBodyAndHeader("seda:in", "Message[" + n + "]", "processAsync",
                    true);
        });

        assertMockEndpointsSatisfied();

        Message message = mock.getExchanges().get(0).getIn();

        // NOT Equals
        assertNotEquals(message.getHeader("initiatingThread"), message.getHeader("completingThread"));
    }


    @Test
    @Ignore
    public void testSyncProcessingSomething() throws InterruptedException {

        MockEndpoint mock = super.getMockEndpoint("mock:out");

        mock.setExpectedCount(10);
        mock.setResultWaitTime(5000);

        IntStream.range(1, 11).forEach(n -> {
            super.template.sendBodyAndHeader("seda:in", "Message[" + n + "]", "processAsync",
                    false);
        });

        assertMockEndpointsSatisfied();

        Message message = mock.getExchanges().get(0).getIn();

        assertEquals(message.getHeader("initiatingThread"), message.getHeader("completingThread"));
    }

    @Test
    @Ignore
    public void testAsyncRequest() throws Exception {
        CompletableFuture<Object> future = super.template.asyncRequestBody("direct:processInOut",
                "Some Payload");

        while (!future.isDone()) {
            super.log.info("Doing something else while processing...");
            Thread.sleep(200);
        }

        String response = (String) future.get();
        super.log.info("Received a response");
        assertEquals("Processed Some Payload", response);

    }

    @Test
    @Ignore
    public void testAsyncRequestWithCallback() throws InterruptedException, ExecutionException {

        CompletableFuture<Object> future = template.asyncCallbackRequestBody("direct:processInOut",
                "Another Payload",
                new Synchronization() {
                    @Override
                    public void onComplete(Exchange exchange) {
                        assertEquals("Processed Another Payload", exchange.getOut().getBody());
                    }

                    @Override
                    public void onFailure(Exchange exchange) {
                        fail();
                    }
                });

        while (!future.isDone()) {
            super.log.info("Doing something else while processing...");
            Thread.sleep(200);
        }

        super.log.info("Received a response");
    }


    @Test
    @Ignore
    public void testParallelConsumption() throws InterruptedException {

        MockEndpoint mock = super.getMockEndpoint("mock:out");

        mock.setExpectedCount(100);
        mock.setResultWaitTime(3000);

        IntStream.range(1, 101).forEach(n -> {
            super.template.sendBodyAndHeader("seda:in", "Message[" + n + "]", "processAsync",
                    false);
        });

        assertMockEndpointsSatisfied();
    }



}
