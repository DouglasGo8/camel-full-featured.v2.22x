package com.douglasdb.camel.feat.core.test.errorhandling.oncompletion;

import com.douglasdb.camel.feat.core.errorhandling.oncompletion.OnCompletionOtherRoute;
import com.douglasdb.camel.feat.core.errorhandling.oncompletion.OnCompletionRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @author dbatista
 */
public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder[] createRouteBuilders() throws Exception {
        return new RoutesBuilder[]{
                new OnCompletionRoute(),
                new OnCompletionOtherRoute()
        };
    }


    @Test
    public void testOnCompletionDefinedAtRouteLevel() throws InterruptedException {
        final MockEndpoint mockCompleted = super.getMockEndpoint("mock:completed");
        mockCompleted.setExpectedMessageCount(1);
        mockCompleted.message(0).body().isEqualTo("this message should be fine"); // original body

        super.template.asyncSendBody("direct:onCompletion", "this message should be fine");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testOnCompletionGlobal() throws InterruptedException {

        final MockEndpoint mockGlobal = super.getMockEndpoint("mock:global");
        mockGlobal.setExpectedMessageCount(1);
        mockGlobal.message(0).body().isEqualTo("this message should explode"); // original body

        template.asyncSendBody("direct:noOnCompletion", "this message should explode");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testOnCompletionFailureAtRouteLevel() throws InterruptedException {
        final MockEndpoint mockFailed = super.getMockEndpoint("mock:failed");
        mockFailed.setExpectedMessageCount(1);
        mockFailed.message(0).body().isEqualTo("this message should explode"); // original body

        template.asyncSendBody("direct:onCompletionFailure", "this message should explode");

        assertMockEndpointsSatisfied();
    }


    @Test
    public void testOnCompletionFailureConditional() throws InterruptedException {
        final MockEndpoint mockFailed = super.getMockEndpoint("mock:failed");
        mockFailed.setExpectedMessageCount(1);
        mockFailed.message(0).body().isEqualTo("this message should explode"); // original body

        super.template.asyncSendBody("direct:onCompletionFailureConditional", "this message should explode");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testOnCompletionChained() throws InterruptedException {

        final MockEndpoint mockFailed = super.getMockEndpoint("mock:failed");
        mockFailed.setExpectedMessageCount(1);
        mockFailed.message(0).body().isEqualTo("this message should explode");

        MockEndpoint mockCompleted = getMockEndpoint("mock:completed");
        mockCompleted.setExpectedMessageCount(1);
        mockCompleted.message(0).body().isEqualTo("this message should complete");

        super.template.asyncSendBody("direct:onChained", "this message should explode");
        super.template.asyncSendBody("direct:onChained", "this message should complete");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testOnCompletionChoice() throws InterruptedException {
        final MockEndpoint mockFailed = getMockEndpoint("mock:failed");
        mockFailed.setExpectedMessageCount(1);
        mockFailed.message(0).body().isEqualTo("this message should explode");

        final MockEndpoint mockCompleted = getMockEndpoint("mock:completed");
        mockCompleted.setExpectedMessageCount(1);
        mockCompleted.message(0).body().isEqualTo("this message should complete");

        // here we have 2 onCompletions set - one on a top-level route, and another on a sub-route
        // both should be triggered depending on success or failure
        super.template.asyncSendBody("direct:onCompletionChoice", "this message should explode");
        super.template.asyncSendBody("direct:onCompletionChoice", "this message should complete");

        assertMockEndpointsSatisfied();
    }


}
