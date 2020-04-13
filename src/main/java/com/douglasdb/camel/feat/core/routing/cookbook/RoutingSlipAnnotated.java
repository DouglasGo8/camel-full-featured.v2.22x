package com.douglasdb.camel.feat.core.routing.cookbook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.camel.Consume;
import org.apache.camel.Headers;
import org.apache.camel.RoutingSlip;

/**
 *
 */
public class RoutingSlipAnnotated {


    @Consume(uri="direct:start")
    @RoutingSlip(delimiter = ",")
    public List<String> routeMe(String body, @Headers Map<String, Object> headers) {

        ArrayList<String> results = new ArrayList<>();

        Object slip = headers.get("myRoutingSlipHeader");
        if (slip != null) {
            String[] uris = slip.toString().split(",");
            Collections.addAll(results, uris);
        }

        results.add("mock:oneMore");


        return results;

    }
}
