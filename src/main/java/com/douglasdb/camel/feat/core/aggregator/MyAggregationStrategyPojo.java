package com.douglasdb.camel.feat.core.aggregator;

public class MyAggregationStrategyPojo {


    public String concat(String oldBody, String newBody) {
        if (null != newBody)
            return oldBody.concat(newBody);
        return oldBody;
    }
}
