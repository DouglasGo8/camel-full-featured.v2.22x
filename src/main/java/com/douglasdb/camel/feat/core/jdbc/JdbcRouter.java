package com.douglasdb.camel.feat.core.jdbc;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.JndiRegistry;

/**
 * @author dbatista
 */
public class JdbcRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

            from("jms:accounting")
                    .to("bean:orderToSql")
                    .log("${body}")
                    .to("jdbc:dataSource?useHeadersAsParameters=true")
                    .to("mock:result");



    }
}
