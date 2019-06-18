package com.douglasdb.camel.feat.core.loadbalancer;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.processor.loadbalancer.SimpleLoadBalancerSupport;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 */
public class MyCustomLoadBalancer extends SimpleLoadBalancerSupport {

    @Override
    public void process(Exchange exchange) throws Exception {

        final Function<Exchange, Processor> target = e ->
                "gold".equals(e.getIn().getHeader("type", String.class)) ?
                  super.getProcessors().get(0):
                  super.getProcessors().get(1);

        target.apply(exchange).process(exchange);

    }
}
