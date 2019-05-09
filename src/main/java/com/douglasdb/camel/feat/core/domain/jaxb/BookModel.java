
package com.douglasdb.camel.feat.core.domain.jaxb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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



    private String title;
    private String author1;
    private String author2;

    private String category;
    private String titleLanguage;


    private BigDecimal price;

    private LocalDate publishDate;


}