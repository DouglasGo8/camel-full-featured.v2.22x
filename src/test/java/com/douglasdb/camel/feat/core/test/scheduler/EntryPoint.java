package com.douglasdb.camel.feat.core.test.scheduler;

import com.douglasdb.camel.feat.core.scheduler.SchedulerRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 *
 */
public class EntryPoint extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new SchedulerRoute();
    }

    @Test
    public void testScheduler() throws InterruptedException {
        getMockEndpoint("mock:end").expectedMessageCount(1);

        assertMockEndpointsSatisfied();
    }


}
