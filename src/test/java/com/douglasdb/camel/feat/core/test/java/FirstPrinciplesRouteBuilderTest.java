package com.douglasdb.camel.feat.core.test.java;

import com.douglasdb.camel.feat.core.java.SimpleTransformRoute;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FirstPrinciplesRouteBuilderTest {

    private CamelContext context;


    @Before
    @SneakyThrows
    public void setUpContext() {
        this.context = new DefaultCamelContext();
        this.context.addRoutes(new SimpleTransformRoute());
        this.context.start();
    }

    @After
    public void cleanUpContext() throws Exception {
        this.context.stop();
    }

    @Test
    public void testPayloadIsTransformed() throws InterruptedException {
        final MockEndpoint out = this.context.getEndpoint("mock:out", MockEndpoint.class);

        out.setExpectedMessageCount(1);
        out.message(0).body().isEqualTo("Modified: Cheese");

        final ProducerTemplate producerTemplate = this.context.createProducerTemplate();
        producerTemplate.sendBody("direct:in", "Cheese");

        out.assertIsSatisfied();
    }
}
