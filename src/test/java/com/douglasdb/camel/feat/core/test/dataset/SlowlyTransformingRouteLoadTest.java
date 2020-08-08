package com.douglasdb.camel.feat.core.test.dataset;

import com.douglasdb.camel.feat.core.dataset.ExpectedOutputDataSet;
import com.douglasdb.camel.feat.core.dataset.InputDataSet;
import com.douglasdb.camel.feat.core.dataset.SlowlyTransformingRoute;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;


public class SlowlyTransformingRouteLoadTest extends CamelTestSupport {

    @Override
    protected CamelContext createCamelContext() throws Exception {

        final int testBatchSize = 1000; // sent until 1000 messages
        InputDataSet inputDataSet = new InputDataSet();
        inputDataSet.setSize(testBatchSize);
        //
        ExpectedOutputDataSet outputDataSet = new ExpectedOutputDataSet();
        outputDataSet.setSize(testBatchSize);
        final SimpleRegistry registry = new SimpleRegistry();
        //
        registry.put("input", inputDataSet);
        registry.put("expectedOutput", outputDataSet);

        return new DefaultCamelContext(registry);
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        SlowlyTransformingRoute routeBuilder = new SlowlyTransformingRoute();
        routeBuilder.setSourceUri("dataset:input?produceDelay=-1");
        routeBuilder.setTargetUri("dataset:expectedOutput");
        return routeBuilder;
    }


    @Test
    @SneakyThrows
    public void testPayloadsTransformedInExpectedTime() {
        MockEndpoint expectedOutput = getMockEndpoint("dataset:expectedOutput");
        expectedOutput.setResultWaitTime(10000);
        expectedOutput.assertIsSatisfied();
    }
}
