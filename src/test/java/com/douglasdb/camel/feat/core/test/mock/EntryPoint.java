package com.douglasdb.camel.feat.core.test.mock;

import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.List;
import static org.apache.camel.builder.Builder.simple;

public class EntryPoint extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("stub:jms:topic:quote").to("mock:quote");
            }
        };
    }

    @Test
    public void testQuote() throws InterruptedException {

        final MockEndpoint quote = super.getMockEndpoint("mock:quote");
        quote.expectedMessageCount(1);
        //
        super.template.sendBody("stub:jms:topic:quote", "Camel rocks");
        quote.assertIsSatisfied();
    }

    @Test
    public void testGapDetected() throws InterruptedException {

        final MockEndpoint mock = super.getMockEndpoint("mock:quote");
        mock.expectedMessageCount(3);
        // Java 8
        mock.expects(() -> {
            mock.getExchanges().stream()
                    .reduce(0, (last, exchange) -> {
                        //
                        final int current = exchange.getIn().getHeader("Counter", Integer.class);
                        //
                        if (current <= last) {
                            fail("Counter is not greater than last counter");
                        } else {
                            fail("Gap detected: last: " + last + " current: " + current);
                        }
                        return current;

                    }, (prev, current) -> current);
        });

        // Before Java 8

        /*mock.expects(new Runnable() {
            public void run() {
                // loop the received exchanges and detect gaps
                int last = 0;
                for (final Exchange exchange : mock.getExchanges()) {
                    // get the current index
                    int current = exchange.getIn().getHeader("Counter", Integer.class);
                    // must be greater than the last number
                    if (current <= last) {
                        fail("Counter is not greater than last counter");
                        // and the gap between must exactly be 1
                    } else if (current - last != 1) {
                        fail("Gap detected: last: " + last + " current: " + current);
                    }
                    // remember as new last
                    last = current;
                }
            }
        });*/

        super.template.sendBodyAndHeader("stub:jms:topic:quote", "A", "Counter", 1);
        super.template.sendBodyAndHeader("stub:jms:topic:quote", "B", "Counter", 2);
        super.template.sendBodyAndHeader("stub:jms:topic:quote", "C", "Counter", 4);


        mock.assertIsSatisfied();

    }

    @Test
    public void testGapDetectedExpected() throws Exception {
        final MockEndpoint mock = getMockEndpoint("mock:quote");
        mock.expectedMessageCount(3);
        mock.expects(new Runnable() {
            public void run() {
                int last = 0;
                for (Exchange exchange : mock.getExchanges()) {
                    int current = exchange.getIn().getHeader("Counter", Integer.class);
                    if (current <= last) {
                        fail("Counter is not greater than last counter");
                    } else if (current - last != 1) {
                        fail("Gap detected: last: " + last + " current: " + current);
                    }
                    last = current;
                }
            }
        });

        super.template.sendBodyAndHeader("stub:jms:topic:quote", "A", "Counter", 1);
        super.template.sendBodyAndHeader("stub:jms:topic:quote", "B", "Counter", 2);
        super.template.sendBodyAndHeader("stub:jms:topic:quote", "C", "Counter", 4);

        // assert that we fail using Is_Not_Satisfied
        mock.assertIsNotSatisfied();
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
        // set expectations the two messages arrives
        quote.expectedMessageCount(2);
        // all messages should contain the Camel word
        quote.allMessages().body().contains("Camel");

        // fire in a messages to Camel
        super.template.sendBody("stub:jms:topic:quote", "Hello Camel");
        super.template.sendBody("stub:jms:topic:quote", "Camel rocks");

        // verify the result
        quote.assertIsSatisfied();
    }

    @Test
    public void testMatches() throws Exception {
        // get the mock endpoint
        final MockEndpoint quote = getMockEndpoint("mock:quote");
        // set expectations the two messages arrives
        quote.expectedMessageCount(2);
        // all messages should have a body that has more than 6 chars
        quote.allMessages().body().matches(simple("${body.length} > 6"));

        // fire in a messages to Camel
        template.sendBody("stub:jms:topic:quote", "Hello Camel");
        template.sendBody("stub:jms:topic:quote", "Camel rocks");

        // verify the result
        quote.assertIsSatisfied();
    }
}
