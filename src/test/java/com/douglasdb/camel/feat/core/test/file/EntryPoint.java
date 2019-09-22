package com.douglasdb.camel.feat.core.test.file;

import com.douglasdb.camel.feat.core.file.FilePrinterRouter;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @author dbatista
 */
public class EntryPoint extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new FilePrinterRouter();
    }


    @Test
    public void testPrintFile() throws InterruptedException {

        super.getMockEndpoint("mock:end").expectedMessageCount(1);

        assertMockEndpointsSatisfied();

    }

}
