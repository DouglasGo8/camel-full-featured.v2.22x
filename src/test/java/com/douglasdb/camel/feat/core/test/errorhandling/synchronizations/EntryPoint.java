package com.douglasdb.camel.feat.core.test.errorhandling.synchronizations;

import com.douglasdb.camel.feat.core.errorhandling.synchronizations.DynamicOnCompletionRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new DynamicOnCompletionRoute();
    }

    @Test
    public void testOnCompletionCompleted() throws InterruptedException {

        final String COMPLETING_BODY = "this message should complete";
        final MockEndpoint mockStart = super.getMockEndpoint("mock:start");
        final MockEndpoint mockCancel = super.getMockEndpoint("mock:cancel");
        final MockEndpoint mockConfirm = super.getMockEndpoint("mock:confirm");

        mockStart.setExpectedMessageCount(1);
        mockStart.message(0).body().isEqualTo(COMPLETING_BODY);

        mockCancel.setExpectedMessageCount(0);

        mockConfirm.setExpectedMessageCount(1);
        mockConfirm.message(0).body().isEqualTo(COMPLETING_BODY);

        super.template.asyncSendBody("direct:in", COMPLETING_BODY);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testOnCompletionFailed() throws InterruptedException {

        final String FAILING_BODY = "this message should explode";

        final MockEndpoint mockStart = getMockEndpoint("mock:start");
        final MockEndpoint mockCancel = getMockEndpoint("mock:cancel");
        final MockEndpoint mockConfirm = getMockEndpoint("mock:confirm");

        mockStart.setExpectedMessageCount(1);
        mockStart.message(0).body().isEqualTo(FAILING_BODY);

        mockCancel.setExpectedMessageCount(1);
        mockCancel.message(0).body().isEqualTo(FAILING_BODY);

        mockConfirm.setExpectedMessageCount(0);

        template.asyncSendBody("direct:in", FAILING_BODY);

        assertMockEndpointsSatisfied();

    }

}
