package com.douglasdb.camel.feat.core.test.notify;

import com.douglasdb.camel.feat.core.notify.OrderClient;
import lombok.SneakyThrows;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.spi.BrowsableEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.concurrent.TimeUnit;

/**
 * @author dbatista
 */
public class OrderTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/notify/app-context.xml");
    }

    @Test
    @SneakyThrows
    public void testOrderClientValid() {
        final NotifyBuilder notify = new NotifyBuilder(super.context).whenDone(1).create();
        final LocalDateTime datetime = LocalDateTime.of(2017, Month.APRIL, 20, 7, 47, 58);
        //
        final OrderClient client = new OrderClient("tcp://localhost:61616");
        //
        client.sendOrder(123, datetime, "4444", "5555");

        boolean matches = notify.matches(5, TimeUnit.SECONDS);

        assertTrue(matches);

        final BrowsableEndpoint be = super.context.getEndpoint("activemq:queue:confirm", BrowsableEndpoint.class);
        assertEquals(1, be.getExchanges().size());
        assertEquals("OK,123,2017-04-20T07:47:58,4444,5555",
                be.getExchanges().get(0).getIn().getBody(String.class));


    }

    @Test
    @SneakyThrows
    public void testOrderClientInvalid() {
        final NotifyBuilder notify = new NotifyBuilder(super.context).whenDone(1).create();
        final LocalDateTime datetime = LocalDateTime.of(2017, Month.APRIL, 20, 7, 47, 58);
        //
        final OrderClient client = new OrderClient("tcp://localhost:61616");
        //
        client.sendOrder(999, datetime, "5555", "2222");

        boolean matches = notify.matches(5, TimeUnit.SECONDS);

        assertTrue(matches);

        final BrowsableEndpoint be = super.context.getEndpoint("activemq:queue:invalid", BrowsableEndpoint.class);
        assertEquals(1, be.getExchanges().size());
        assertEquals("999,2017-04-20T07:47:58,5555,2222", be.getExchanges().get(0).getIn().getBody(String.class));


    }

    @Test
    @SneakyThrows
    public void testOrderClientFailure() {
        final NotifyBuilder notify = new NotifyBuilder(super.context).whenFailed(1).create();
        final LocalDateTime datetime = LocalDateTime.of(2017, Month.APRIL, 20, 7, 47, 58);
        //
        final OrderClient client = new OrderClient("tcp://localhost:61616");
        //
        client.sendOrder(123, datetime, "4444", "9999");

        final boolean matches = notify.matches(5, TimeUnit.SECONDS);
        assertTrue(matches);
        // final BrowsableEndpoint be = super.context.getEndpoint("activemq:queue:invalid", BrowsableEndpoint.class);
        // assertEquals(1, be.getExchanges().size());

    }
}
