package com.douglasdb.camel.feat.core.test.testing;

import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration({"/META-INF/spring/testing/test-properties-context.xml", "/META-INF/spring/testing/simpleTransform-context.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SimpleTransformEnhancedSpringTest {

    @Autowired
    private CamelContext context;
    @Produce(uri = "direct:in")
    private ProducerTemplate template;
    @EndpointInject(uri = "mock:out")
    private MockEndpoint mockOut;

    @Test
    @SneakyThrows
    public void testPayloadIsTransformed() {
        mockOut.setExpectedMessageCount(1);
        mockOut.message(0).body().isEqualTo("Modified: Cheese");
        template.sendBody("Cheese");
        //
        MockEndpoint.assertIsSatisfied(context);
    }

    @Test
    @SneakyThrows
    public void testPayloadIsTransformedAgainAndMocksWorkCorrectly() {
        mockOut.setExpectedMessageCount(1);
        mockOut.message(0).body().isEqualTo("Modified: Foo");
        template.sendBody("Foo");
        //
        MockEndpoint.assertIsSatisfied(context);
    }
}
