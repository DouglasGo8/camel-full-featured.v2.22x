package com.douglasdb.camel.feat.core.test.errorhandling.doTry;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 *
 */
public class EntryPoint extends CamelTestSupport {


    @Test
    public void testDoTryHappy() throws InterruptedException {

        final MockEndpoint mockBefore = super.getMockEndpoint("mock:before");
        mockBefore.expectedBodiesReceived("Foo");



        assertMockEndpointsSatisfied();


    }

}
