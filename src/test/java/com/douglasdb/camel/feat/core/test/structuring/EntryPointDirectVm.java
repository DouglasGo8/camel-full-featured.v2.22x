package com.douglasdb.camel.feat.core.test.structuring;

import com.douglasdb.camel.feat.core.structuring.ExternalLoggingRoute;
import org.apache.camel.CamelContext;
import org.apache.camel.Message;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;


/**
 * @author douglasdias
 */
public class EntryPointDirectVm {

    private CamelContext testHarnessCtx;
    private CamelContext externalLogCtx;

    public EntryPointDirectVm() {
        // TODO Auto-generated constructor stub
    }

    /**
     *
     */
    @Before
    public void setupContexts() {

        try {

            this.testHarnessCtx = new DefaultCamelContext();

            this.testHarnessCtx.addRoutes(new RouteBuilder() {

                @Override
                public void configure() throws Exception {
                    // TODO Auto-generated method stub

                    from("direct:in")
                            .setHeader("harness.threadName", simple("${threadName}"))
                            .log("##testHarness - Thread Name.: ${threadName}")
                            //.to("direct-vm:logMessageToBackendSystem")
                            .to("vm:logMessageToBackendSystem")
                            .log("Completed logging");
                }

            });

            this.testHarnessCtx.start();
            //

            this.externalLogCtx = new DefaultCamelContext();
            //this.externalLogCtx.addRoutes(new ExternalLoggingRoute("direct-vm"));
            this.externalLogCtx.addRoutes(new ExternalLoggingRoute("vm"));
            this.externalLogCtx.start();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @throws Exception
     */
    @After
    public void shutdownContexts() throws Exception {
        this.testHarnessCtx.stop();
        this.externalLogCtx.stop();
    }

    /**
     * @throws InterruptedException
     */
    @Test
    public void testMessagePassing() throws InterruptedException {

        ProducerTemplate producerTemplate = testHarnessCtx.createProducerTemplate();

        MockEndpoint out = externalLogCtx.getEndpoint("mock:out", MockEndpoint.class);
        out.setExpectedMessageCount(1);
        out.message(0).body().isEqualTo("logging: Some Payload");

        producerTemplate.sendBody("direct:in", "Some Payload");
        out.assertIsSatisfied(1000);
        Message message = out.getExchanges().get(0).getIn();

        // to direct-vm (Equals Threads Name)
        //assertTrue(message.getHeader("harness.threadName")
        //	.equals(message.getHeader(ExternalLoggingRoute.LOGGING_THREAD_NAME)));

        // to vm (Non Equals Threads Name)
        assertFalse(message.getHeader("harness.threadName")
                .equals(message.getHeader(ExternalLoggingRoute.LOGGING_THREAD_NAME)));

    }
}
