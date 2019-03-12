package com.douglasdb.camel.feat.core.test.bean;

import com.douglasdb.camel.feat.core.bean.HelloBean;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.junit.Test;

import junit.framework.TestCase;

public class EntryPointSimple extends TestCase {

    private CamelContext context;
    private ProducerTemplate template;

    @Override
    protected void setUp() throws Exception {

        SimpleRegistry registry = new SimpleRegistry();

        registry.put("helloBean", new HelloBean());
        context = new DefaultCamelContext(registry);
        template = context.createProducerTemplate();

        context.addRoutes(new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:hello")
                    .bean("helloBean", "hello");
            }
        });

        context.start();

    }

    @Override
    protected void tearDown() throws Exception {
        // cleanup resources after test
        template.stop();
        context.stop();
    }


    @Test
    public void testHelloSimpleRegistry() {
        
        Object reply = this.template.requestBody("direct:hello", "World", String.class);
        assertEquals("Hello World", reply);

    }

}