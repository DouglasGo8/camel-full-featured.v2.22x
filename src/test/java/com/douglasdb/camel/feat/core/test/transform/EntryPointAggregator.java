package com.douglasdb.camel.feat.core.test.transform;


import com.douglasdb.camel.feat.core.transform.enrich.AbbreviationExpander;
import com.douglasdb.camel.feat.core.transform.enrich.EnrichWithAggregatorRoute;
import com.douglasdb.camel.feat.core.transform.enrich.MergeInReplacementText;
import org.apache.camel.CamelContext;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class EntryPointAggregator extends CamelTestSupport {


    @Override
    protected RouteBuilder[] createRouteBuilders() throws Exception {

        EnrichWithAggregatorRoute route = new EnrichWithAggregatorRoute();

        route.setMyMerger(super.context().getRegistry().lookupByNameAndType("myMerger",
                MergeInReplacementText.class));

        return new RouteBuilder[]{route, new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:expander")
                        .bean(AbbreviationExpander.class, "expand");
            }
        }};

    }


    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry jndiRegistry = super.createRegistry();

        // register beanId for use by EnrichRoute
        // you could also use Spring or Blueprint 'bean' to create and configure
        // these references where the '<bean id="<ref id>">'
        jndiRegistry.bind("myMerger", new MergeInReplacementText());

        return jndiRegistry;
    }


    @Test
    public void testEnrichWithAggregator() throws Exception {

        String response = template.requestBody("direct:start", "Hello MA", String.class);

        assertEquals("Hello Massachusetts", response);

        response = template.requestBody("direct:start", "I like CA", String.class);

        assertEquals("I like California", response);
    }

}
