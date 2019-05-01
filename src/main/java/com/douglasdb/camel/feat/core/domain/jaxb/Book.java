
package com.douglasdb.camel.feat.core.domain.jaxb;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 */
@Data
public class Book {
    private final int year;
    private final String title;
    private final List<String> author;
    


    @Data
    @AllArgsConstructor
    class Title {
        private final String title;
    }
}