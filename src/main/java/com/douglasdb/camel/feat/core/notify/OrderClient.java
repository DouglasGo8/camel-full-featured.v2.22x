package com.douglasdb.camel.feat.core.notify;

import lombok.SneakyThrows;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

public class OrderClient {

    private final ActiveMQConnectionFactory factory;

    public OrderClient(String url) {
        this.factory = new ActiveMQConnectionFactory(url);
    }

    @SneakyThrows
    public void sendOrder(int customerId, LocalDateTime date, String... itemIds) {
        //
        final StringBuilder body = new StringBuilder(customerId + "," + date);
        //
        for (String id: itemIds)
            body.append(",").append(id);
        //
        try (final Connection con = factory.createConnection()) {
            try (final Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
                con.start();
                final Destination destination = session.createQueue("order");
                try (final MessageProducer producer = session.createProducer(destination)) {
                    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                    Message msg = session.createTextMessage(body.toString());
                    producer.send(msg);
                }
            }
        }
    }
}
