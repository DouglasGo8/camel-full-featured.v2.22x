package com.douglasdb.camel.feat.core.aggregator;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.leveldb.LevelDBAggregationRepository;

/**
 *
 */
public class AggregatorABCLevelDBRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {


        LevelDBAggregationRepository levelDB =
                new LevelDBAggregationRepository("myrepo", "data/myrepo.dat");

        from("file:///D:/.camel/data/inbox/minimal/?noop=true")
                .log("Consuming file ${file:name}")
                .convertBodyTo(String.class)
                .aggregate(constant(true), new MyAggregationStrategy())
                .aggregationRepository(levelDB)
                    .completionSize(3)
                    .log("Sending out ${body}")
                .to("mock:result");

    }
}
