package com.douglasdb.camel.feat.core.test.notify;

import com.douglasdb.camel.feat.core.notify.NotifyRouter;
import lombok.SneakyThrows;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.seda.SedaEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class NotifyTest extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new NotifyRouter();
    }

    @Test
    @SneakyThrows
    public void testNotifyFrom() {

        final NotifyBuilder notify = new NotifyBuilder(context).from("seda:order").whenDone(1).create();

        super.template.sendBody("seda:quote", "Camel rocks");
        super.template.sendBody("seda:order", "123,2017-04-20'T'15:47:59,4444,5555");

        final boolean matches = notify.matches(5, TimeUnit.SECONDS);
        assertTrue(matches);

        final SedaEndpoint confirm = context.getEndpoint("seda:confirm", SedaEndpoint.class);
        assertEquals(1, confirm.getExchanges().size());
        assertEquals("OK,123,2017-04-20'T'15:47:59,4444,5555", confirm.getExchanges().get(0).getIn().getBody());
    }

    @Test
    @SneakyThrows
    public void testNotifyWhenAnyDoneMatches() {
        // use a predicate to indicate when a certain message is done
        final NotifyBuilder notify = new NotifyBuilder(super.context)
                .from("seda:order")
                .whenAnyDoneMatches(body().isEqualTo("OK,123,2017-04-20'T'15:48:00,2222,3333")).create();

        // send in 2 messages. Its the 2nd message we want to test
        template.sendBody("seda:order", "123,2017-04-20'T'15:47:59,4444,5555");
        template.sendBody("seda:order", "123,2017-04-20'T'15:48:00,2222,3333");

        final boolean matches = notify.matches(5, TimeUnit.SECONDS);
        assertTrue(matches);

        final SedaEndpoint confirm = context.getEndpoint("seda:confirm", SedaEndpoint.class);
        // there should be 2 messages on the confirm queue
        assertEquals(2, confirm.getExchanges().size());
        // and the 2nd message should be the message we wanted to test for
        assertEquals("OK,123,2017-04-20'T'15:48:00,2222,3333", confirm.getExchanges().get(1).getIn().getBody());
    }

    @Test
    @SneakyThrows
    public void testNotifyOr() {
        // shows how to stack multiple expressions using binary operations (or)
        final NotifyBuilder notify = new NotifyBuilder(super.context)
                .from("seda:quote").whenReceived(1).or().whenFailed(1).create();

        template.sendBody("seda:quote", "Camel rocks");
        template.sendBody("seda:order", "123,2017-04-20'T'15:48:00,2222,3333");

        final boolean matches = notify.matches(5, TimeUnit.SECONDS);
        assertTrue(matches);
    }

}
