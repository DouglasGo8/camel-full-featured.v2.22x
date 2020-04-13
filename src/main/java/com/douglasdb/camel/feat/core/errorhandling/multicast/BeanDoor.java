package com.douglasdb.camel.feat.core.errorhandling.multicast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author dbatista
 */
public class BeanDoor {

    public void handleDoor(JsonNode json) throws MulticastBusinessException {

        ((ObjectNode) json).put("doors", 4);

        //throw new MulticastBusinessException("Fail cause 'Autumn into Winter'!!!");



    }
}
