


package com.douglasdb.camel.feat.core.splitter;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 */
public class SplitterBeanRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        from("direct:start")
            .split()
                .method(CustomerService.class, "splitDepartments")
                .to("log:split")
                .to("mock:split");
                
    }


}