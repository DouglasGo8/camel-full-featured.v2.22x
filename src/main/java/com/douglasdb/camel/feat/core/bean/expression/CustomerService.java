package com.douglasdb.camel.feat.core.bean.expression;

import org.apache.camel.jsonpath.JsonPath;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomerService {



    /**
     * 
     */
    public String region(@JsonPath("$.order.customerId") int customerId) {
        
        if (customerId < 1000) 
            return  "US";
        else if (customerId < 2000)
            return "EMEA";
        else
            return "OTHER";
    }
}