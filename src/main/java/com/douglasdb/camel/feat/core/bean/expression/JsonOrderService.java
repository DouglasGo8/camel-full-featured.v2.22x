package com.douglasdb.camel.feat.core.bean.expression;

import org.apache.camel.jsonpath.JsonPath;
import org.apache.camel.language.Bean;

/**
 * @author DouglasDb
 */
public class JsonOrderService {

    /**
     * 
     */
    public String handleIncomingOrder(
        @JsonPath("$.order.customerId") int customerId,
        @JsonPath("$.order.item") String item, 
        @Bean(ref = "guid", method = "generate") int orderId) {

        return String.format("%d, %d. %s", orderId, customerId, item);

    }

}