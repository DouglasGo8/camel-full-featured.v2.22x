package com.douglasdb.camel.feat.core.test.interceptor;

import com.douglasdb.camel.feat.core.interceptor.MyRouteBuilder;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @author dbatista
 */
public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new MyRouteBuilder();
    }

    @Test
    public void myTest() throws InterruptedException {
        super.getMockEndpoint("mock:result").expectedMessageCount(1);
        assertMockEndpointsSatisfied();
    }

}
