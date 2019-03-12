package com.douglasdb.camel.feat.core.test.bean;

import com.douglasdb.camel.feat.core.bean.HelloBean;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.JndiRegistry;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * 
 */
public class EntryPointJndi extends TestCase {


    private CamelContext context;
    private ProducerTemplate template;

    @Override
    protected void setUp() throws Exception {

        JndiRegistry registry = new JndiRegistry(true);

        registry.bind("helloBean",  new HelloBean());

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
        this.template.stop();
        this.context.stop();
    }

    @Test
    public void testHello()  throws Exception {
        Object reply = template.requestBody("direct:hello", "World");
        assertEquals("Hello World", reply);
    }
}