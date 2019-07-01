


package com.douglasdb.camel.feat.core.routingsplip;

import org.apache.camel.RoutingSlip;

public class SlipBean {


    @RoutingSlip
    public String slip(String body) {

        String answer = "mock:a";

        if (body.contains("Cool")) {
            answer += ", mock:b";
        }

        answer += ", mock:c";

        return answer;
    }
}