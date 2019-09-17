package com.douglasdb.camel.feat.core.splitter;

import com.douglasdb.camel.feat.core.aggregator.SetAggregationStrategy;
import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class SplitReaggregateRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:in")
                .split(xpath("/books/book"))
                .setHeader("category", xpath("/book/@category").stringResult())
                .transform(xpath("/book/@title").stringResult())
                    .to("direct:groupByCategory")
                .end();

        from("direct:groupByCategory")
                .aggregate(header("category"), new SetAggregationStrategy())
                    .completionTimeout(500)
                //log("${body}")
                .to("mock:out")
                .end();

        from("direct:combined")
                .split(xpath("/books/book"))
                .setHeader("category", xpath("/book/@category").stringResult())
                .transform(xpath("/book/@title").stringResult())
                .aggregate(header("category"), new SetAggregationStrategy())
                    .completionTimeout(500)
                        .to("mock:out")
                    .endParent()
                .end();

    }
}
