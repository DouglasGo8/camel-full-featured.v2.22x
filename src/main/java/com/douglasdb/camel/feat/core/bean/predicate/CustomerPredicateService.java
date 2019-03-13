package com.douglasdb.camel.feat.core.bean.predicate;

import org.apache.camel.jsonpath.JsonPath;

/**
 * 
 */
public class CustomerPredicateService {

   
    public boolean isGold(@JsonPath("$.order.loyaltyCode") int customerId) {
        return customerId < 1000;
    }

    public boolean isSilver(@JsonPath("$.order.loyaltyCode") int customerId) {
        return customerId >= 1000 && customerId < 5000;
    }
}