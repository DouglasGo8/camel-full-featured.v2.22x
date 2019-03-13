package com.douglasdb.camel.feat.core.bean.predicate;

import org.apache.camel.builder.RouteBuilder;

public class JsonPredicateRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("file://src/main/resources/META-INF/order")
            .choice()
                .when(method(CustomerPredicateService.class, "isGold"))
                    .to("mock:queue:gold")
                .when(method(CustomerPredicateService.class, "isSilver"))
                    .to("mock:queue:silver")
                .otherwise()
                    .to("mock:queue:regular")
            .end();
    }
    
}