
package com.douglasdb.camel.feat.core.splitter;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 */
public class WordTranslateBean {

    private Map<String, String> words = new HashMap<String, String>();

    public WordTranslateBean() {
        words.put("A", "Camel rocks");
        words.put("B", "Hi mom");
        words.put("C", "Yes it works");
    }

    /**
     * 
     * @param key
     * @return
     */
    public String translate(String key) {
        if (!words.containsKey(key)) {
            throw new IllegalArgumentException("Key not a known word " + key);
        }
        return words.get(key);
    }
}