package com.douglasdb.camel.feat.core.test.logthroughput;

import com.douglasdb.camel.feat.core.logthroughput.LogThroughputRoute;
import lombok.SneakyThrows;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.stream.IntStream;

public class LogthroughputTest extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new LogThroughputRoute();
    }

    @Test
    public void testLogThroughput() throws InterruptedException {
        final int messageCount = 20;
        super.getMockEndpoint("mock:result").expectedMessageCount(messageCount);
        //
        IntStream.rangeClosed(1, messageCount).asLongStream()
                .forEach(i -> {
                    template.sendBody("direct:start", "Hello Camel " + i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testLogThroughputInterval() throws InterruptedException {
        final int messageCount = 50;
        super.getMockEndpoint("mock:result").expectedMessageCount(messageCount);
        //
        IntStream.rangeClosed(1, messageCount).asLongStream()
                .forEach(i -> {
                    template.sendBody("direct:startInterval", "Hello Camel " + i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

        assertMockEndpointsSatisfied();
    }
}
