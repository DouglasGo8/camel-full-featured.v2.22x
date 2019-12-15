To build this project use

    mvn install

To run this project from within Maven use

    mvn exec:java

For more help see the Apache Camel documentation

    http://camel.apache.org/


## links
<https://github.com/camelinaction/camelinaction2 />
<https://github.com/apache/camel/tree/master/examples />
<https://github.com/CamelCookbook/camel-cookbook-examples />

## CMds

docker run -d --name=activemq -p 8161:8161 -p 61616:61616 webcenter/activemq


 **********************
 Insights to Enrichment
 **********************
1.Transaction 4522 S  XPTO (Json origin)
2.Transaction 2344 N  XXTS (Json origin)

@Component
public class EnrichRangeOne extends RouteBuilder {
		
		
		
    @Override
	public void configure() {

        final String enrichOfAU(%{body}...);
        final String enrichOfBY4(%{body}...);
                
		from("WMQ01?concurrentConsumer=10")
		    .bean(ReadyToEnrichment.class) // check merge release 3.0.0 <-> 3.0.1
			.multicast()
				.timeout(1000)
				.stopOnException()
				.parallelProcessing(true)
				.multicast()
				    .bean(EnrichFromAU.class, enrichOfAU);
				    .bean(EnrichFromBY.class, enrichOfBY4);
				.end()
			.end()
			.to(MyCassandra.bean)
		}
	}
