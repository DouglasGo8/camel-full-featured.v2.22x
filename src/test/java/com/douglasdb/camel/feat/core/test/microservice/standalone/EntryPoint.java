package com.douglasdb.camel.feat.core.test.microservice.standalone;

import com.douglasdb.camel.feat.core.microservice.standalone.HelloRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new HelloRoute();
    }


    @Test
    public void testStandalone() throws InterruptedException {

        Object out = fluentTemplate.to("jetty:http://localhost:13166/hello").request(String.class);
        // assert that the reply message is what we expect
        assertEquals("Hello from Camel", out);

        TimeUnit.SECONDS.sleep(5);
    }

}
