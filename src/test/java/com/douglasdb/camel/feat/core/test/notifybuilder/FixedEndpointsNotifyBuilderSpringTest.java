package com.douglasdb.camel.feat.core.test.notifybuilder;


import org.apache.camel.CamelContext;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration({"/META-INF/spring/testing/fixedEndpoints-context.xml", "/META-INF/spring/notifybuilder/test-jms-context.xml"})
public class FixedEndpointsNotifyBuilderSpringTest {

    @Autowired
    private CamelContext context;
    @Autowired
    private JmsTemplate jmsTemplate;

    @Test
    public void testSingleMessageDone() {
        final String message = "testMessage";

        NotifyBuilder notify = new NotifyBuilder(context).from("activemq:in").whenDone(1).create();

        sendMessageBody(message);
        assertTrue(notify.matches(10, TimeUnit.SECONDS));
    }

    @Test
    public void testSingleMessageDoneSentToOutAndMatched() throws InterruptedException {
        final String messageText = "testMessage";

        MockEndpoint mock = context.getEndpoint("mock:nameDoesNotMatter", MockEndpoint.class);
        mock.message(0).inMessage().contains(messageText);
        mock.message(0).header("count").isNull();

        NotifyBuilder notify = new NotifyBuilder(context)
                .from("activemq:in")
                .whenDone(1).wereSentTo("activemq:out")
                .whenDoneSatisfied(mock)
                .create();

        sendMessageBody(messageText);
        assertTrue(notify.matches(10, TimeUnit.SECONDS));
    }

    @Test
    public void testMessageFiltering() throws InterruptedException {
        NotifyBuilder notify = new NotifyBuilder(context)
                .from("activemq:in")
                .whenExactlyDone(1).filter().simple("${body} contains 'test'")
                .create();

        sendMessageBody("testMessage");
        sendMessageBody("realMessage");

        assertTrue(notify.matches(10, TimeUnit.SECONDS));
    }

    private void sendMessageBody(final String messageText) {
        jmsTemplate.send("in", session -> session.createTextMessage(messageText));
    }
}
