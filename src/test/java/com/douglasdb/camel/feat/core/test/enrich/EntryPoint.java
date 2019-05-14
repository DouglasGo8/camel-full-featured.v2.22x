package com.douglasdb.camel.feat.core.test.enrich;

import com.douglasdb.camel.feat.core.enrich.AbbreviationExpander;
import com.douglasdb.camel.feat.core.enrich.EnrichRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 *
 */
public class EntryPoint extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return  new EnrichRoute();
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry jndi = super.createRegistry();
        jndi.bind("myExpander", new AbbreviationExpander());

        return jndi;
    }

    @Test
    public void testEnrich() {

        String response = super.template.requestBody("direct:start", "MA", String.class);


        assertEquals("Massachusetts", response);


        response = super.template.requestBody("direct:start", "CA", String.class);


        assertEquals("California", response);

    }
}
