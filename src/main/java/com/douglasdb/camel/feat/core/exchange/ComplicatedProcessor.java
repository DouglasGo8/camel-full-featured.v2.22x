package com.douglasdb.camel.feat.core.exchange;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class ComplicatedProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        final String something = "SOMETHING";
        final Message in = exchange.getIn();
        final String action = in.getHeader("action", String.class);
        //
        if ((action == null) || (action.isEmpty())) {
            in.setHeader("actionTaken", false);
        } else {
            in.setHeader("actionTaken", true);
            String body = in.getBody(String.class);
            if (action.equals("append")) {
                in.setBody(body + " " + something);
            } else if (action.equals("prepend")) {
                in.setBody(something + " " + body);
            } else {
                throw new IllegalArgumentException(
                        "Unrecognized action requested: [" + action + "]");
            }
        }
    }
}
