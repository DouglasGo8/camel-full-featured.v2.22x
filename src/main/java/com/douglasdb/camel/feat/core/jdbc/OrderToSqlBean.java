package com.douglasdb.camel.feat.core.jdbc;

import org.apache.camel.Headers;
import org.apache.camel.language.XPath;

import java.util.Map;

/**
 *
 */
public class OrderToSqlBean {

    public String toSql(@XPath("order/@name") String name,
                        @XPath("order/@amount") String amount,
                        @XPath("order/@customer") String customer,
                        @Headers Map<String, Object> outHeaders) {

        outHeaders.put("partName", name);
        outHeaders.put("quantity", Integer.parseInt(amount));
        outHeaders.put("customer", customer);

        return "insert into INCOMING_ORDERS (part_name, quantity, customer) values (:?partName, :?quantity, :?customer)";
    }
}
