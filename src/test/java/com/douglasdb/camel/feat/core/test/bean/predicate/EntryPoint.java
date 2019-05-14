package com.douglasdb.camel.feat.core.test.bean.predicate;

import com.douglasdb.camel.feat.core.bean.predicate.JsonPredicateRoute;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.processor.validation.PredicateValidationException;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 */
public class EntryPoint extends CamelTestSupport {

    private static String endpoint = "file://src/main/resources/META-INF/order";

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new JsonPredicateRoute();
        // CompoundPredicateRoute();
    }

    @Override
    public void setUp() throws Exception {
        super.deleteDirectory(endpoint);
        super.setUp();
    }

    @Test
    @Ignore
    public void testCompoundPredicateValid() throws Exception {

        final String xml = "<book><title>Camel in Action</title><user>Donald Duck</user></book>";

        super.getMockEndpoint("mock:valid").expectedMessageCount(1);

        super.template.sendBodyAndHeader("direct:start", xml, "source", "batch");

        assertMockEndpointsSatisfied();

    }

    @Test(expected = PredicateValidationException.class)
    @Ignore
    public void testCompoundPredicateInvalid() throws Exception {

        try {
            String xml = "<book><title>Camel in Action</title><user>Claus</user></book>";
            super.template.sendBodyAndHeader("direct:start", xml, "source", "batch");
        } catch (CamelExecutionException e) {
            PredicateValidationException pve = assertIsInstanceOf(PredicateValidationException.class, e.getCause());
            throw pve;
        }
    }

    @Test
    @Ignore
    public void sendGoldOrder() throws Exception {
        super.getMockEndpoint("mock:queue:gold").expectedMessageCount(1);
        super.getMockEndpoint("mock:queue:silver").expectedMessageCount(0);
        super.getMockEndpoint("mock:queue:regular").expectedMessageCount(0);

        // prepare a JSon document from a String
        String json = "{ \"order\": { \"loyaltyCode\": 88, \"item\": \"ActiveMQ in Action\" } }";

        // store the order as a file which is picked up by the route
        template.sendBodyAndHeader(endpoint, json, Exchange.FILE_NAME, "order.json");

        assertMockEndpointsSatisfied();
    }


    @Test
    public void sendSilverOrder() throws Exception {
        super.getMockEndpoint("mock:queue:gold").expectedMessageCount(0);
        super.getMockEndpoint("mock:queue:silver").expectedMessageCount(1);
        super.getMockEndpoint("mock:queue:regular").expectedMessageCount(0);

        // prepare a JSon document from a String
        String json = "{ \"order\": { \"loyaltyCode\": 4444, \"item\": \"ActiveMQ in Action\" } }";

        // store the order as a file which is picked up by the route
        template.sendBodyAndHeader(endpoint, json, Exchange.FILE_NAME, "order.json");

        assertMockEndpointsSatisfied();
    }
}