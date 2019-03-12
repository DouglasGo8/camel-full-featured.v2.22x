package com.douglasdb.camel.feat.core.test.structuring;

import com.douglasdb.camel.feat.core.structuring.OrderProcessingRoute;
import com.douglasdb.camel.feat.core.structuring.route.processor.OrderFileNameProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author douglasdias
 */
public class EntryPoint extends CamelTestSupport {

    final String inDir = "/Users/douglasdias/.camel/input";
    final String outDir = "/Users/douglasdias/.camel/output";

    @Produce(uri = "direct:in")
    private ProducerTemplate in;

    /**
     *
     */
    public EntryPoint() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean isUseAdviceWith() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        // TODO Auto-generated method stub

        /**
         * Custom Router and Processor Miss self part and Spring part of exercise
         */

        OrderFileNameProcessor processor = new OrderFileNameProcessor();

        processor.setCountryDateFormat("dd-MM-yyyy");

        final OrderProcessingRoute route = new OrderProcessingRoute();

        route.setId("ordersId");
        route.setInputDirectory(inDir);
        route.setOutputDirectory(outDir);
        route.setOrderFileNameProcessor(processor);

        return route;

        /**
         *
         */

        // super.context.addComponent("myLogger", new LogComponent());

        // return new LogMessageOnTimerEventRoute();
        // SedaTimerRoute();
        // StoppingControlBusRouteWithProcessor();
        // StoppingControlBusRoute
        // RouteStartupRoute();
        // DirectRoute();
        // DirectLoopRoute();
    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testInOutMessageWithLoop() throws InterruptedException {

        final String helloFromCamel = "Camel Rocks!!!";

        MockEndpoint out = super.getMockEndpoint("mock:out");

        out.setExpectedMessageCount(1);
        out.message(0).header("loopCount").isEqualTo(10);

        in.sendBody(helloFromCamel);

        assertMockEndpointsSatisfied();

    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testInOutMessage() throws InterruptedException {

        final String originalMsg = "Camel Rocks!!!";
        final String changedMsg = "A2[ B[ A1[ Camel Rocks!!! ] ] ]";

        MockEndpoint mock1 = super.getMockEndpoint("mock:a");
        MockEndpoint mock2 = super.getMockEndpoint("mock:b");

        mock1.setExpectedMessageCount(1);
        mock2.setExpectedMessageCount(1);

        mock1.message(0).body().isEqualTo(changedMsg);
        String response = (String) in.requestBody(originalMsg);

        assertEquals(changedMsg, response);

        assertMockEndpointsSatisfied();

    }

    /**
     *
     */
    @Test
    @Ignore
    public void testStartupRoute() {

        final String message = "Test Message";

        String out = super.fluentTemplate()
            .withBody(message)
            .to("direct:receiveOrders")
            .request(String.class);

        assertEquals("Processed..." + message, out);

    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testRouteShutdown() throws InterruptedException {

        MockEndpoint mockOut = super.getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(1);
        MockEndpoint mockStopped = super.getMockEndpoint("mock:stopped");
        mockStopped.setExpectedMessageCount(1);

        super.template.sendBody("direct:in", "mainRoute");

        assertMockEndpointsSatisfied();

    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testRouteShuthdownWithProcessor() throws InterruptedException {

        MockEndpoint mockOut = super.getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(1);
        MockEndpoint mockStopped = super.getMockEndpoint("mock:stopped");
        mockStopped.setExpectedMessageCount(1);

        super.template.sendBody("direct:in", "mainRoute");

        assertMockEndpointsSatisfied();

    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testLoadBalancing() throws InterruptedException {

        final int pingCount = 10;

        MockEndpoint out = super.getMockEndpoint("mock:out");

        out.setMinimumExpectedMessageCount(pingCount);
        Thread.sleep((pingCount * 200) + 3000);
        out.assertIsSatisfied();

    }

    /**
     * @throws Exception
     */
    @Test
    @Ignore
    public void testLogMessageTimer() throws Exception {

        // MockEndpoint out = super.getMockEndpoint("mock:out");

        super.context.start();
        // assertMockEndpointsSatisfied();

        Thread.sleep(5000);

        super.context.stop();
    }

    /**
     * @throws Exception
     */
    @Test
    @Ignore
    public void testRoutingLogic() throws Exception {

        super.context
                .getRouteDefinition("ordersId")
                .adviceWith(super.context(), new AdviceWithRouteBuilder() {
                            /**
                             *
                             */
                            @Override
                            public void configure() throws Exception {
                                // TODO Auto-generated method stub

                                replaceFromWith("direct:in");
                                //
                                interceptSendToEndpoint(String.format("file://%s", outDir))
                                        // going to ignore the route .toF file://
                                        .skipSendToOriginalEndpoint()
                                        .to("mock:out");
                            }
                        }
                );

        super.context().start();

        final MockEndpoint out = super.getMockEndpoint("mock:out");

        out.setExpectedMessageCount(1);
        out.message(0).body().startsWith("2013-11-23");
        out.message(0).header(Exchange.FILE_NAME).isEqualTo("2013-11-23.csv");

        fluentTemplate()
                .to("direct:in")
                .withBody("23-11-2013,1,Geology rocks t-shirt")
                .send();
        //
        assertMockEndpointsSatisfied();
    }
}
