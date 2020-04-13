package com.douglasdb.camel.feat.core.errorhandling.errorhandler;

import org.apache.camel.Exchange;
import org.w3c.dom.Document;

import java.io.InputStream;


/**
 *
 */
public class OrderService {

    public String validate(String body) throws OrderValidationException {

        if (!body.contains("amount")) {
            throw new OrderValidationException("Invalid order");
        }

        return body + ", id=123";
    }

    public String enrich(String body) throws OrderException {
        if (body.contains("ActiveMQ in Action")) {
            throw new OrderException("ActiveMQ in Action is out of stock");
        }
        return body + ", status=OK";
    }

    public String toCsv(String body) throws OrderException {
        if (body.contains("xml")) {
            throw new OrderException("xml files not allowed");
        }

        return body.replaceAll("#", ",");
    }

    public void toSoap(Exchange exchange) {

        String body = exchange.getIn().getBody(String.class);

        String file = "F:/.camel/data/inbox/message1.xml";

        if (body.contains("ActiveMQ in Action")) {
            // load the soapFault.xml into a DOM
            InputStream is = exchange.getContext().getClassResolver()
                    .loadResourceAsStream(file);
            Document dom = exchange.getContext().getTypeConverter().convertTo(Document.class, is);

            // set a fault to indicate a failure
            exchange.getOut().setFault(true);
            exchange.getOut().setBody(dom);
        } else {
            // load the soapOK.xml into a DOM
            InputStream is = exchange.getContext().getClassResolver()
                    .loadResourceAsStream(file);
            Document dom = exchange.getContext().getTypeConverter().convertTo(Document.class, is);

            // set a xml reply
            exchange.getOut().setBody(dom);
        }
    }
}
