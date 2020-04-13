package com.douglasdb.camel.feat.core.errorhandling.multicast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author dbatista
 */
public class BeanColor {

    public void handleColor(JsonNode json, String color) {
        ((ObjectNode) json).put("color", color);
    }
}
