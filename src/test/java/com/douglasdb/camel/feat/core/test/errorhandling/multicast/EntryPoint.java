package com.douglasdb.camel.feat.core.test.errorhandling.multicast;

import com.douglasdb.camel.feat.core.errorhandling.multicast.MultiCastOriginalMessageRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 *
 */
public class EntryPoint extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new MultiCastOriginalMessageRoute();
    }

    @Test
    public void testMulticast() throws InterruptedException {


        MockEndpoint result = super.getMockEndpoint("mock:result");

        result.expectedMessageCount(0);
        //result.expectedBodiesReceived("Message changed!!");

        super.template.sendBody("direct:start", "Camel Rocks");


        assertMockEndpointsSatisfied();

    }

}
