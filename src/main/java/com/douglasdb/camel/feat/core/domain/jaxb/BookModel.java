

package com.douglasdb.camel.feat.core.domain.jaxb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookModel implements Serializable {


    /**
     *
     */    
    private static final long serialVersionUID = 1L;

    private String title;
    private String author1;
    private String author2;

    private String category;
    private String titleLanguage;


    private BigDecimal price;

    private LocalDate publishDate;

}