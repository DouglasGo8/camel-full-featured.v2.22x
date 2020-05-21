package com.douglasdb.camel.feat.core.test.mock;

import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class EntryPointSpring extends CamelSpringTestSupport {
    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/mock/app-context.xml");
    }


    @Test
    public void testQuote() throws Exception {
        // get the mock endpoint
        final MockEndpoint quote = getMockEndpoint("mock:quote");
        // set expectations such as 1 message should arrive
        quote.expectedMessageCount(1);

        // fire in a message to Camel
        super.template.sendBody("stub:jms:topic:quote", "Camel rocks");

        // verify the result
        quote.assertIsSatisfied();
    }

    @Test
    public void testIsCamelMessage() throws Exception {

        final MockEndpoint mock = getMockEndpoint("mock:quote");
        mock.expectedMessageCount(2);

        super.template.sendBody("stub:jms:topic:quote", "Hello Camel");
        super.template.sendBody("stub:jms:topic:quote", "Camel rocks");

        assertMockEndpointsSatisfied();

        final List<Exchange> list = mock.getReceivedExchanges();

        final String body1 = list.get(0).getIn().getBody(String.class);
        final String body2 = list.get(1).getIn().getBody(String.class);

        assertTrue(body1.contains("Camel"));
        assertTrue(body2.contains("Camel"));

    }

    @Test
    public void testSameMessageArrived() throws Exception {
        // get the mock endpoint
        final MockEndpoint quote = getMockEndpoint("mock:quote");
        // set expectations that the same message arrived as we send
        quote.expectedBodiesReceived("Camel rocks");

        // fire in a message to Camel
        super.template.sendBody("stub:jms:topic:quote", "Camel rocks");

        // verify the result
        quote.assertIsSatisfied();
    }

    @Test
    public void testTwoMessages() throws Exception {
        // get the mock endpoint
        final MockEndpoint quote = getMockEndpoint("mock:quote");
        // set expectations the two messages arrives in any order
        quote.expectedBodiesReceivedInAnyOrder("Camel rocks", "Hello Camel");

        // fire in a messages to Camel
        super.template.sendBody("stub:jms:topic:quote", "Hello Camel");
        super.template.sendBody("stub:jms:topic:quote", "Camel rocks");

        // verify the result
        quote.assertIsSatisfied();
    }

    @Test
    public void testTwoMessagesOrdered() throws Exception {
        // get the mock endpoint
        final MockEndpoint quote = getMockEndpoint("mock:quote");
        // set expectations the two messages arrives in specified order
        quote.expectedBodiesReceived("Hello Camel", "Camel rocks");

        // fire in a messages to Camel
        super.template.sendBody("stub:jms:topic:quote", "Hello Camel");
        super.template.sendBody("stub:jms:topic:quote", "Camel rocks");

        // verify the result
        quote.assertIsSatisfied();
    }

    @Test
    public void testContains() throws Exception {
        // get the mock endpoint
        final MockEndpoint quote = getMockEndpoint("mock:quote");
        // set expectations the two messages arrives in specified order
        quote.expectedMessageCount(2);
        // all messages should contain the Camel word
        quote.allMessages().body().contains("Camel");

        // fire in a messages to Camel
        super.template.sendBody("stub:jms:topic:quote", "Hello Camel");
        super.template.sendBody("stub:jms:topic:quote", "Camel rocks");

        // verify the result
        quote.assertIsSatisfied();
    }
}
