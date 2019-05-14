package com.douglasdb.camel.feat.core.test.bean.expression;

import com.douglasdb.camel.feat.core.bean.expression.JsonDynamicExpressionRoute;


import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author DouglasDb
 */
public class EntryPoint extends CamelTestSupport {


    private final String endpoint = "file://src/main/resources/META-INF/order";
  
    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new JsonDynamicExpressionRoute();
                  // JsonExpressionNoSetHeader();
    }


    @Override
    public void setUp() throws Exception {
        super.deleteDirectory(endpoint);
        super.setUp();
    }

    @Test
    @Ignore    
    public void testsendUSOrder() throws Exception {

        final String json = "{ \"order\": { \"customerId\": 88, \"item\": \"ActiveMQ in Action\" } }";


        super.getMockEndpoint("mock:queue:US").expectedMessageCount(1);
        super.getMockEndpoint("mock:queue:EMEA").expectedMessageCount(0);
        super.getMockEndpoint("mock:queue:OTHER").expectedMessageCount(0);

        /**
         * 
         */
        super.template()
            .sendBodyAndHeader(endpoint, json, Exchange.FILE_NAME, "order.json");

        

        assertMockEndpointsSatisfied();
    }



    @Test
    @Ignore
    public void testsendEMEAOrder() throws Exception {

        final String json = "{ \"order\": { \"customerId\": 1234, \"item\": \"ActiveMQ in Action\" } }";


        super.getMockEndpoint("mock:queue:US").expectedMessageCount(0);
        super.getMockEndpoint("mock:queue:EMEA").expectedMessageCount(1);
        super.getMockEndpoint("mock:queue:OTHER").expectedMessageCount(0);

        /**
         * 
         */
        super.template()
            .sendBodyAndHeader(endpoint, json, Exchange.FILE_NAME, "order.json");

        

        assertMockEndpointsSatisfied();
    }



}