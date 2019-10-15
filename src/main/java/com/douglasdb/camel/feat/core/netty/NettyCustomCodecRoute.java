package com.douglasdb.camel.feat.core.netty;

import org.apache.camel.builder.RouteBuilder;

/**
 *
 */
public class NettyCustomCodecRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("netty4:tcp://localhost:8998?encoder=#welderEncoder&decoder=#welderDecoder&sync=false")
                .to("jms:operations");

        from("jms:operations")
                .to("mock:end");

    }
}
