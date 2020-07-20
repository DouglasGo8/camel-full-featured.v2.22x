package com.douglasdb.camel.feat.core.test.testing;

import lombok.SneakyThrows;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@UseAdviceWith(true)
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration({"/META-INF/spring/testing/fixedEndpoints-context.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FixedEndpointEnhancedSpringTest {
    @Autowired
    private ModelCamelContext context;

    @Test
    @SneakyThrows
    public void testOverriddenEndpoints() {

        context.getRouteDefinition("modifyPayloadBetweenQueues")
                .adviceWith(context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() {
                        //
                        replaceFromWith("direct:in");
                        //
                        interceptSendToEndpoint("activemq:out")
                                .skipSendToOriginalEndpoint()
                                .to("mock:out");
                    }
                });
        //
        context.start();
        //
        MockEndpoint out = context.getEndpoint("mock:out", MockEndpoint.class);
        out.setExpectedMessageCount(1);
        out.message(0).body().isEqualTo("Modified: Cheese");
        //
        final ProducerTemplate template = context.createProducerTemplate();
        template.sendBody("direct:in", "Cheese");
        //
        out.assertIsSatisfied();
    }
}
