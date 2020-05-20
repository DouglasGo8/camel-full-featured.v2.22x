package com.douglasdb.camel.feat.core.errorhandling.multicast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author dbatista
 */
public class BeanBrand {


    public void handleBrand(JsonNode json) {

        if (json.get("brand").isArray()) // same to handle Specifics field
            ((ObjectNode) json).put("brand", "Volkswagen");

        throw new NullPointerException("Fail cause 'The Realization'!!!");
    }
}
