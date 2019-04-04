package com.douglasdb.camel.feat.core.test.aggregator;

import com.douglasdb.camel.feat.core.aggregator.AggregatorABC;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;


/**
 * @author Douglas Db
 */
public class EntryPoint extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new AggregatorABC();
    }


    @Test
    public void testABC() throws Exception {

        MockEndpoint mock = super.getMockEndpoint("mock:result");

        mock.expectedMessageCount(1);
        mock.expectedBodiesReceived("ABC");

        super.template.sendBodyAndHeader("direct:start", "A", "correlationId", 1);
        super.template.sendBodyAndHeader("direct:start", "B", "correlationId", 1);

        super.template.sendBodyAndHeader("direct:start", "F", "correlationId", 2);
        
        super.template.sendBodyAndHeader("direct:start", "C", "correlationId", 1);

        assertMockEndpointsSatisfied();



    }


    

}