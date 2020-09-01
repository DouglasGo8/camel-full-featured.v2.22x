package com.douglasdb.camel.feat.core.monitoring;


import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jmx.JMXUriBuilder;
import org.apache.camel.processor.interceptor.Tracer;

import java.util.HashMap;
import java.util.Map;

public class MonitorRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        getContext().setTracing(true);

        Tracer tracer = new Tracer();
        tracer.setLogName("MyTracerLog");

        tracer.getDefaultTraceFormatter().setShowProperties(true);
        tracer.getDefaultTraceFormatter().setShowHeaders(false);

        getContext().addInterceptStrategy(tracer);
        from("direct:start").routeId("monitorRoute")
                .transform(simple("Monitoring ${body}"))
                .log("${body}")
                .to("mock:result");


        final Map<String, String> map = new HashMap<>();
        map.put("context", "localhost/" + getContext().getName());
        map.put("type", "routes");
        map.put("name", "\"monitorRoute\"");

        //

        JMXUriBuilder jmxUriBuilder = new JMXUriBuilder("platform")
                .withObjectDomain("org.apache.camel")
                .withObjectProperties(map)
                .withMonitorType("counter")
                .withObservedAttribute("ExchangesCompleted")
                .withInitThreshold(0)
                .withGranularityPeriod(500)
                .withOffset(1)
                .withDifferenceMode(false);


        log.info("jmxUri = {}", jmxUriBuilder.toString());

        //
        from(jmxUriBuilder.toString())
                .routeId("jmxMonitor")
                .log("${body}")
                .to("mock:monitor");

    }
}
