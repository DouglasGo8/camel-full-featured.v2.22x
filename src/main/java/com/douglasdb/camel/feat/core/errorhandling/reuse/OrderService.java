package com.douglasdb.camel.feat.core.errorhandling.reuse;

/**
 * @author dbatista
 */
public class OrderService {

    public String validate(String body) throws OrderValidationException {

        if (!body.contains("amount")) {
            throw new OrderValidationException("Invalid order");
        }

        return body + ",id=123";
    }

    public String enrich(String body) throws OrderException {
        if (body.contains("ActiveMQ in Action")) {
            throw new OrderException("ActiveMQ in Action is out of stock");
        }

        return body + ",status=OK";

    }

    public String toCsv(String body) throws OrderException {
        if (body.contains("xml")) {
            throw new OrderException("xml files not allowed");
        }
        return body.replaceAll("#", ",");
    }
}
