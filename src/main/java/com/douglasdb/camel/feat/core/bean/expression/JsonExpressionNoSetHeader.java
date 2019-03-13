

package com.douglasdb.camel.feat.core.bean.expression;

import org.apache.camel.builder.RouteBuilder;


/**
 * @author DouglasDb
 */
public class JsonExpressionNoSetHeader extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("file://src/main/resources/META-INF/order")
            .log("${body}")
            .recipientList(simple("mock:queue:${bean:com.douglasdb.camel.feat.core.expression.CustomerService?method=region}"))
        .end();
    }
    
}