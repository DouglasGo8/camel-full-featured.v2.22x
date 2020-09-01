package com.douglasdb.camel.feat.core.tracer;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.interceptor.Tracer;

public class TraceRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        super.getContext().setTracing(true);

        final Tracer tracer = new Tracer();
        tracer.setLogName("MyTracerLog");

        tracer.getDefaultTraceFormatter().setShowProperties(true);
        tracer.getDefaultTraceFormatter().setShowHeaders(false);

        getContext().addInterceptStrategy(tracer);

        from("direct:start")
                .setBody(simple("Tracing ${body}"))
                .to("mock:result");
    }
}
