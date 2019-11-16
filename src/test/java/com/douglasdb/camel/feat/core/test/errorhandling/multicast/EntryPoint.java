package com.douglasdb.camel.feat.core.test.errorhandling.multicast;

import com.douglasdb.camel.feat.core.errorhandling.multicast.MultiCastOriginalMessageRoute;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.io.IOException;

/**
 *
 */
public class EntryPoint extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new MultiCastOriginalMessageRoute();
    }

    @Test
    public void testMulticastWithHandleException() throws InterruptedException, IOException {


        final String json = "{ \"brand\" : \"Mercedes\", \"doors\" : 5, \"color\" : \"black\" }";
        final MockEndpoint result = super.getMockEndpoint("mock:result");

        final JsonNode mapper = new ObjectMapper().readTree(json);


        result.expectedMessageCount(0);
        // result.expectedBodiesReceived("*** {\"brand\":\"Volkswagen\",\"doors\":5} ***");
        super.template.sendBody("direct:start", mapper);

        assertMockEndpointsSatisfied();

    }

}
