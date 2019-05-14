package com.douglasdb.camel.feat.core.test.bean.expression;

import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author DouglasDb
 */
public class EntryPointJsonSpring extends CamelSpringTestSupport {


    final String defPath = "META-INF/spring/bean/expression/";
    final String defSubPath = "json/";
    final String defFile = "app-context.xml";

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext(defPath.concat(defSubPath.concat(defFile)));
    }


    @Override
    public void setUp() throws Exception {
        super.deleteDirectory("file://src/main/resources/META-INF/order");
        super.setUp();
    }

    @Test
    public void sendIncomingOrder() throws Exception {
        
        MockEndpoint mock = super.getMockEndpoint("mock:queue:order");
        mock.expectedMessageCount(1);

        final String json = "{ \"order\": { \"customerId\": 4444, \"item\": \"Camel in Action\" } }";

        super.template.sendBodyAndHeader("file://src/main/resources/META-INF/order",
             json, Exchange.FILE_NAME, "order.json");

        assertMockEndpointsSatisfied();
    }



}