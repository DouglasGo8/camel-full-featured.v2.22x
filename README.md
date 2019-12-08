To build this project use

    mvn install

To run this project from within Maven use

    mvn exec:java

For more help see the Apache Camel documentation

    http://camel.apache.org/


## links
https://github.com/CamelCookbook/camel-cookbook-examples

https://github.com/camelinaction/camelinaction2



## CMds

docker run -d --name=activemq -p 8161:8161 -p 61616:61616 webcenter/activemq

<!---
 **********************
 Insights to Enrichment
 **********************
1.Transaction 4522 S  XPTO (Json origin)
2.Transaction 2344 N  XXTS (Json origin)

@Component
public class EnrichRangeOne extends RouteBuilder {
		
    @Override
	public void configure() {

		from("WMQ01?concurrentConsumer=10")
		    .bean(ReadyToEnrichment.class) // check merge release 3.0.0 <-> 3.0.1
			.multicast()
				.timeout(2000)
				.stopOnException()
				.parallelProcessing(true)
				    .to("direct:BY4")
					.to("direct:CC")
			.end()
			.transform(simple("${exchangeProperty.payload}"))
			.to(MyCassandra.bean)
		}
	}

	@Component
	public class EnrichmentBY4 extends RouteBuilder {
		
		@Override
		public void configure() {

			from("direct:BY4")
				.setProperty("payload", "${body}")
				.multicast()
	            	.parallelProcessing()
	            	.choice()
	            		.when().jsonpath("$.data.evento.conta_origem[?(@.documento)]")
	            		.setProperty("document", "$.data.evento.conta_destino[0].documento")
	            		.to("direct:orig")
	            	.end()
					.choice()
	            		.when().jsonpath("$.data.evento.conta_destino[0][?(@.documento)]")
	            		.setProperty("document", "$.data.evento.conta_destino[0].documento")
	            		.to("direct:dest")
	            	.end()
	            .end();

	        from("direct:orig")
	        	.setHeader(CacheConstants.CACHE_OPERATION, simple(CacheConstants.CACHE_OPERATION_GET))
	            .setHeader(CacheConstants.CACHE_KEY, constant("key"))
	            .to("cache://...")
	            .setProperty("ofStsCacheKey", "${body}")
	            .setBody(exchangeProperty("payload"))            
	            .doTry()
	            	.setHeader("bank-key", "foo")
	            	.setHeader("Authorization", "Bearer ${exchangeProperty.ofStsCacheKey}")
	            	.toD("http://pep.endpoint/${exchangeProperty.document}")
	            .doCatch(HttpOperationFail.class)
					.bean(Foo.class, "method(${exchangeProperty.payload}, false)")
	            .finally()
	        		.bean(Foo.class, "method(${exchangeProperty.payload}, true)")
	        	.end()
	        .end();

	        from("direct:dest")
	            // same structure
	        	.end();            
		}

	}
-->