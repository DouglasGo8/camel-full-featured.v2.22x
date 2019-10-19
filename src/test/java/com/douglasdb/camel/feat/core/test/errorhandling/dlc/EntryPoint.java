package com.douglasdb.camel.feat.core.test.errorhandling.dlc;

import com.douglasdb.camel.feat.core.errorhandling.dlc.DlcRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new DlcRoute();
    }

    @Test
    public void testDlq() throws InterruptedException {

        final MockEndpoint mock = super.getMockEndpoint("mock:result");
        mock.expectedMessageCount(1);

        super.template.sendBodyAndHeader("direct:start", "Foo", "myHeader", "original");

        assertMockEndpointsSatisfied();

    }
}
