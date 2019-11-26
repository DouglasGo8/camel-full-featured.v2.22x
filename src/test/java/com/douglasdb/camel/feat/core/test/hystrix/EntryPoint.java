package com.douglasdb.camel.feat.core.test.hystrix;

import com.douglasdb.camel.feat.core.hystrix.CounterService;
import com.douglasdb.camel.feat.core.hystrix.HystrixWithFallbackRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author dbatista
 */
public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new HystrixWithFallbackRoute();
        // HystrixTimeoutRoute();
        // HystrixTimeoutAndFallbackRoute();
        // HystrixRoute();
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {

        final JndiRegistry jndi = super.createRegistry();
        jndi.bind("counter", new CounterService());

        return jndi;
    }

    @Test
    @Ignore
    public void testCamelHystrix() {

        Object out1 = super.template.requestBody("direct:start", "Hello");
        assertEquals("Count 1", out1);
        Object out2 = super.template.requestBody("direct:start", "Hello");
        assertEquals("Count 2", out2);
        Object out3 = super.template.requestBody("direct:start", "Hello");
        assertEquals("Count 3", out3);
        Object out4 = super.template.requestBody("direct:start", "Hello");
        assertEquals("Count 4", out4);

        try {
            super.template.requestBody("direct:start", "Hello");
            fail("Should have thrown exception");
        } catch (Exception e) {
            IOException cause = assertIsInstanceOf(IOException.class, e.getCause().getCause());
            assertEquals("Forced error", cause.getMessage());
        }
    }


    @Test
    @Ignore
    public void testFast() {
        Object out = template.requestBody("direct:start", "fast");
        assertEquals("Fast response", out);
    }

    @Test
    @Ignore
    public void testSlow() throws Exception {
        // this calls the slow route and therefore causes a timeout which triggers the fallback
        Object out = template.requestBody("direct:start", "slow");
        assertEquals("Fallback response", out);
    }

    @Test
    @Ignore
    public void testFastWithTimeout() {
        Object out = template.requestBody("direct:start", "fast");
        assertEquals("Fast response", out);
    }

    @Test
    @Ignore
    public void testSlowWithTimeout() {
        try {
            template.requestBody("direct:start", "slow");
            fail("Should fail due timeout");
        } catch (Exception e) {
            // expected a timeout
            assertIsInstanceOf(TimeoutException.class, e.getCause().getCause());
        }
    }

    @Test
    public void testCamelHystrixWithFallback() {

        Object out1 = template.requestBody("direct:start", "Hello");
        assertEquals("Count 1", out1);

        Object out2 = template.requestBody("direct:start", "Hello");
        assertEquals("Count 2", out2);

        Object out3 = template.requestBody("direct:start", "Hello");
        assertEquals("Count 3", out3);

        Object out4 = template.requestBody("direct:start", "Hello");
        assertEquals("Count 4", out4);

        // should not fail the 5th time
        Object out5 = template.requestBody("direct:start", "Hello");
        assertEquals("No Counter Ready", out5);
    }

}
