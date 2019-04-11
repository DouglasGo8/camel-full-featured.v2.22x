

package com.douglasdb.camel.feat.core.aggregator;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.leveldb.LevelDBAggregationRepository;


/**
 * 
 */
public class AggregateABCRecoveryRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {


        final LevelDBAggregationRepository levelDb = new 
            LevelDBAggregationRepository("myrepo", "data/myrepo.dat");

        levelDb.setUseRecovery(true);
        levelDb.setMaximumRedeliveries(4);
        levelDb.setDeadLetterUri("mock:dead");
        levelDb.setRecoveryInterval(3000); // try eacg 3 seconds

        from("direct:start")
            .log("Sending ${body} with correlation key ${header.myId}")
            .aggregate(header("myId"), new MyAggregationStrategy())
                .aggregationRepository(levelDb)
                .completionSize(3)
            .log("Sending out ${body}")
            .to("mock:aggregate")
            .throwException(new IllegalArgumentException("Damn does not work"))
            .to("mock:result");



    }
    
}