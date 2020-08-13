package com.douglasdb.camel.feat.core.splitter;

import com.douglasdb.camel.feat.core.domain.splitter.Record;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.fixed.BindyFixedLengthDataFormat;

/**
 * @author dbatista
 */
public class SplitAggregatePartitionsRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("direct:bindyRecord")
                .split().tokenize("\n").streaming().aggregationStrategy(new ArrayListAggregationStrategy())
                    .unmarshal(new BindyFixedLengthDataFormat(Record.class))
                .end()
                .split(method(PartitionBean.class)).streaming()
                    //.log("${body}")
                    .to("mock:out")
                .end();
    }
}
