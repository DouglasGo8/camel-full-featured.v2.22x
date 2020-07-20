package com.douglasdb.camel.feat.core.test.testing;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Test;

import java.util.Dictionary;

public class PropertyOverrideBlueprintTest extends CamelBlueprintTestSupport {


    @Produce(uri = "direct:in")
    private ProducerTemplate producerTemplate;

    @EndpointInject(uri = "mock:out")
    private MockEndpoint mockOut;

    @Override
    protected String getBlueprintDescriptor() {
        return "/OSGI-INF/blueprint/simpleTransform-context.xml," +
                "/OSGI-INF/blueprint/simpleTransform-properties-context.xml";
    }

    @Override
    protected String useOverridePropertiesWithConfigAdmin(Dictionary<String, String> props) throws Exception {
        props.put("transform.message", "Overridden");
        return "com.douglasdb.camel.feat.core.test.testing";
    }


    @Test
    public void testPayloadIsTransformed() throws InterruptedException {
        mockOut.setExpectedMessageCount(1);
        System.out.println(mockOut.message(0).body());
        //mockOut.message(0).body().isEqualTo("Overridden: Cheese");

        producerTemplate.sendBody("Cheese");

        assertMockEndpointsSatisfied();
    }

}
