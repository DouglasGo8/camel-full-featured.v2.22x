package com.douglasdb.camel.feat.core.netty;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class NettyTcpRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("netty4:tcp://localhost:8999?textline=true&sync=false")
                .log("${body}")
                .to("jms:operations");

        from("jms:operations")
                .to("mock:end");
    }
}
