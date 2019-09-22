package com.douglasdb.camel.feat.core.file;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class FilePrinterRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("file://D:/.camel/data/inbox?noop=true")
                .to("stream:out")
                .to("mock:end");
    }
}
