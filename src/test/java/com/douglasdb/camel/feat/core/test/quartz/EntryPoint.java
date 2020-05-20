package com.douglasdb.camel.feat.core.test.quartz;

import com.douglasdb.camel.feat.core.quartz.QuartzCronRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 *
 */
public class EntryPoint extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new QuartzCronRoute();
        //QuartzRoute();
    }


    @Test
    public void testPrintFile() throws InterruptedException {

        super.getMockEndpoint("mock:end").expectedMessageCount(1);

        assertMockEndpointsSatisfied();
    }
}
