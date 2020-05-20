package com.douglasdb.camel.feat.core.test.errorhandling.multicast;

import com.douglasdb.camel.feat.core.errorhandling.multicast.MultiCastOriginalMessageRoute;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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


        final String json1 = "{ \"brand\" : \"Mercedes\", \"doors\" : 5, \"color\" : \"black\" }";
        final String json2 = "{ \"brand\" : \"Volkswagen\", \"doors\" : 4, \"color\" : \"red\" }";
        final MockEndpoint result = super.getMockEndpoint("mock:result");

        final JsonNode mapper1 = new ObjectMapper().readTree(json1);
        final JsonNode mapper2 = new ObjectMapper().readTree(json2);

        // result.expectedMessageCount(1);
        // result.expectedBodiesReceived("*** {\"brand\":\"Volkswagen\",\"doors\":5} ***");
        super.template.sendBody("direct:start", mapper1);
        super.template.sendBody("direct:start", mapper2);

        assertMockEndpointsSatisfied();

    }

}
