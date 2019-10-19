package com.douglasdb.camel.feat.core.test.errorhandling.dlc;

import com.douglasdb.camel.feat.core.errorhandling.dlc.DlcRoute;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new DlcRoute();
    }

    @Test
    @Ignore
    public void testDlq() throws InterruptedException {

        final MockEndpoint mockSuccess = super.getMockEndpoint("mock:result");
        mockSuccess.expectedMessageCount(1);
        mockSuccess.expectedBodiesReceived("Foo");
        mockSuccess.message(0).exchangeProperty(Exchange.EXCEPTION_CAUGHT).isNull();
        mockSuccess.message(0).header("myHeader").isEqualTo("changed");

        final MockEndpoint mockError = super.getMockEndpoint("mock:error");

        mockError.expectedMessageCount(1);
        mockError.expectedBodiesReceived("KaBoom");
        mockError.message(0).exchangeProperty(Exchange.EXCEPTION_CAUGHT).isNotNull();
        mockError.message(0).exchangeProperty(Exchange.FAILURE_ROUTE_ID).isEqualTo("myDlcRoute");
        mockError.message(0).header("myHeader").isEqualTo("changed");

        super.template.sendBodyAndHeader("direct:start", "Foo", "myHeader", "original");

        super.template.sendBodyAndHeader("direct:start", "KaBoom", "myHeader", "original");

        assertMockEndpointsSatisfied();

    }


    @Test
    public void testDlqMultistep() throws InterruptedException {

        final MockEndpoint mockResult = getMockEndpoint("mock:result");
        mockResult.expectedMessageCount(1);
        mockResult.expectedBodiesReceived("Foo");
        mockResult.message(0).exchangeProperty(Exchange.EXCEPTION_CAUGHT).isNull();
        mockResult.message(0).header("myHeader").isEqualTo("changed");

        final MockEndpoint mockError = getMockEndpoint("mock:error");
        mockError.expectedMessageCount(1);
        mockError.expectedBodiesReceived("KaBoom");
        mockError.message(0).exchangeProperty(Exchange.EXCEPTION_CAUGHT).isNotNull();
        mockError.message(0).exchangeProperty(Exchange.FAILURE_ROUTE_ID).isEqualTo("myFlakyRoute");
        mockError.message(0).header("myHeader").isEqualTo("flaky");

        super.template.sendBodyAndHeader("direct:multiroute", "Foo", "myHeader", "original");
        super.template.sendBodyAndHeader("direct:multiroute", "KaBoom", "myHeader", "original");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testDlqMultistepOriginal() throws InterruptedException {

        final MockEndpoint mockResult = getMockEndpoint("mock:result");
        mockResult.expectedMessageCount(1);
        mockResult.expectedBodiesReceived("Foo");
        mockResult.message(0).exchangeProperty(Exchange.EXCEPTION_CAUGHT).isNull();
        mockResult.message(0).header("myHeader").isEqualTo("changed");

        final MockEndpoint mockError = getMockEndpoint("mock:error");
        mockError.expectedMessageCount(1);
        mockError.expectedBodiesReceived("KaBoom");
        mockError.message(0).exchangeProperty(Exchange.EXCEPTION_CAUGHT).isNotNull();
        mockError.message(0).exchangeProperty(Exchange.FAILURE_ROUTE_ID).isEqualTo("myFlakyRouteOriginal");
        mockError.message(0).header("myHeader").isEqualTo("multistep"); // keep original values when using useOriginalMessage() option

        super.template.sendBodyAndHeader("direct:multirouteOriginal", "Foo", "myHeader", "original");
        super.template.sendBodyAndHeader("direct:multirouteOriginal", "KaBoom", "myHeader", "original");

        assertMockEndpointsSatisfied();

    }

    @Test
    public void testDlqUseOriginal() throws InterruptedException {
        final MockEndpoint mockResult = getMockEndpoint("mock:result");
        mockResult.expectedMessageCount(1);
        mockResult.expectedBodiesReceived("Foo");
        mockResult.message(0).exchangeProperty(Exchange.EXCEPTION_CAUGHT).isNull();
        mockResult.message(0).header("myHeader").isEqualTo("changed");

        final MockEndpoint mockError = getMockEndpoint("mock:error");
        mockError.expectedMessageCount(1);
        mockError.expectedBodiesReceived("KaBoom");
        mockError.message(0).exchangeProperty(Exchange.EXCEPTION_CAUGHT).isNotNull();
        mockError.message(0).exchangeProperty(Exchange.FAILURE_ROUTE_ID).isEqualTo("myDlcOriginalRoute");
        mockError.message(0).header("myHeader").isEqualTo("original");

        super.template.sendBodyAndHeader("direct:useOriginal", "Foo", "myHeader", "original");
        super.template.sendBodyAndHeader("direct:useOriginal", "KaBoom", "myHeader", "original");

        assertMockEndpointsSatisfied();
    }


    @Test
    public void testDlqUseRouteSpecific() throws InterruptedException {

        final MockEndpoint mockResult = getMockEndpoint("mock:result");
        mockResult.expectedMessageCount(1);
        mockResult.expectedBodiesReceived("Foo");
        mockResult.message(0).exchangeProperty(Exchange.EXCEPTION_CAUGHT).isNull();
       //mockResult.message(0).header("myHeader").isEqualTo("changed");

        final MockEndpoint mockError = getMockEndpoint("mock:error");
        mockError.expectedMessageCount(1);
        mockError.expectedBodiesReceived("KaBoom");
        mockError.message(0).exchangeProperty(Exchange.EXCEPTION_CAUGHT).isNotNull();
        mockError.message(0).exchangeProperty(Exchange.FAILURE_ROUTE_ID).isEqualTo("myDlcSpecificRoute");
        //mockError.message(0).header("myHeader").isEqualTo("original");

        super.template.sendBodyAndHeader("direct:routeSpecific", "Foo", "myHeader", "original");
        super.template.sendBodyAndHeader("direct:routeSpecific", "KaBoom", "myHeader", "original");

        assertMockEndpointsSatisfied();
    }


}
