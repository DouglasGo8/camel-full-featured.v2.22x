package com.douglasdb.camel.feat.core.test.structuring;

import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author douglasdias
 */
public class EntryPointSpring extends CamelSpringTestSupport {

    @Produce(uri = "direct:in")
    private ProducerTemplate in;

    /**
     *
     */
    public EntryPointSpring() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean isUseAdviceWith() {
        // TODO Auto-generated method stub
        return true;
    }

    /**
     *
     */
    @Override
    protected AbstractApplicationContext createApplicationContext() {
        // TODO Auto-generated method stub
        final String springPath = "META-INF/spring/structuring/";
        //
        final String subPath = "templating";
        // "simple/dsl";
        // "simple/xml";
        //"seda";
        //"routecontrol";
        //"propertyplaceholder/nonroutebuilder";
        //"routes";
        //"routebuilder";

        return new ClassPathXmlApplicationContext(springPath.concat(subPath.concat("/app-context.xml")));
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
        final String response = (String) in.requestBody(originalMsg);

        assertEquals(changedMsg, response);

        assertMockEndpointsSatisfied();
    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testInOnlyMessage() throws InterruptedException {

        final String message = "Camel Rocks!!!";
        MockEndpoint mock1 = super.getMockEndpoint("mock:a");
        MockEndpoint mock2 = super.getMockEndpoint("mock:b");

        mock1.setExpectedMessageCount(1);
        mock2.setExpectedMessageCount(1);

        in.sendBody(message);

        assertMockEndpointsSatisfied();

    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testPropertiesLoad() throws InterruptedException {

        final String message = "Camel Rocks!!!";
        // final ExchangePattern mep = ExchangePattern.InOnly;

        MockEndpoint out = super.getMockEndpoint("mock:out");
        out.setExpectedMessageCount(1);
        out.message(0).body().isEqualTo("I hear you: Camel Rocks!!!");

        super.template.sendBody("direct:a", message);


        assertMockEndpointsSatisfied();
    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testPropertiesLoadInSpringRoute() throws InterruptedException {

        final String message = "Camel Rocks!!!";

        MockEndpoint out = super.getMockEndpoint("mock:out");
        out.setExpectedMessageCount(1);
        out.message(0).body().isEqualTo("I hear you: Camel Rocks!!!");

        super.template.sendBody("direct:a", message);


        assertMockEndpointsSatisfied();
    }

    /**
     *
     */
    @Test
    @Ignore
    public void testStartupRoute() {

        final String message = "Test Message";

        String out =
                super.fluentTemplate()
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
    public void testSimpleSpring() throws Exception {

        super.startCamelContext();

        Thread.sleep(5000);


        super.stopCamelContext();
    }


    /**
     * @throws Exception
     */
    @Test
    public void testRoutingLogic() throws Exception {

        super.context

                .getRouteDefinition("ukOrders")

                .adviceWith(super.context(), new AdviceWithRouteBuilder() {
                            /**
                             *
                             */
                            @Override
                            public void configure() throws Exception {
                                // TODO Auto-generated method stub

                                replaceFromWith("direct:in");
                                //
                                interceptSendToEndpoint("file:///Users/douglasdias/.camel/output")
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
