package com.douglasdb.camel.feat.core.transform.xquery;

import org.apache.camel.builder.RouteBuilder;
import static org.apache.camel.component.xquery.XQueryBuilder.xquery;

/**
 *
 */
public class XqueryParamRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {


        from("direct:start")
                .transform(xquery("declare variable $in.headers.myParamValue as xs:string external; <books value='{$in.headers.myParamValue}'>{for $x in /bookstore/book where $x/price>($in.headers.myParamValue cast as xs:integer) order by $x/title return $x/title}</books>"))
                .log("${body}");


        from("direct:start2")
                .transform(xquery("<books>{for $x in /bookstore/book where $x/price>30 order by $x/title return $x/title}</books>"));
    }
}
