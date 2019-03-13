package com.douglasdb.camel.feat.core.bean.predicate;

import org.apache.camel.Predicate;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.builder.RouteBuilder;
import static org.apache.camel.builder.PredicateBuilder.not;
/**
 * 
 */
public class CompoundPredicateRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        

        

        from("direct:start")
            .validate(this.valid())
            .log("${body}")
            .to("mock:valid");
    }


    private Predicate valid() {
        return PredicateBuilder
            .and(
                xpath("/book/title = 'Camel in Action'"),
                // this simple must return true
                simple("${header.source} == 'batch'"),
                // this method call predicate must return false (as we use not)
                not(method(CompoundValidate.class, "isAuthor")));                            
    }

}