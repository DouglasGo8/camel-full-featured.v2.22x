package com.douglasdb.camel.feat.core.test.testing;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@MockEndpoints("activemq:out")
@RunWith(CamelSpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration("/META-INF/spring/testing/fixedEndpoints-context.xml")
public class AutoMockingFixedEndpointsEnhancedSpringTest {

    @Produce(uri = "activemq:in")
    private ProducerTemplate in;

    @EndpointInject(uri = "mock:activemq:out")
    private MockEndpoint mockOut;

    @Test
    @Ignore
    public void testTransformationThroughAutoMock() throws Exception {
        mockOut.expectedBodiesReceived("Modified: testMessage");
        in.sendBody("testMessage");
        mockOut.assertIsSatisfied();
    }
}
