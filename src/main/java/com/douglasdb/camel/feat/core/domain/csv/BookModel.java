package com.douglasdb.camel.feat.core.domain.csv;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@CsvRecord(separator = ",", crlf = "UNIX")
public class BookModel {

    @DataField(pos = 1)
    private String category;

    @DataField(pos = 2)
    private String title;

    @DataField(pos = 3, defaultValue = "en")
    private String titleLanguage;

    @DataField(pos = 4)
    private String author1;

    @DataField(pos = 5)
    private String author2;

    @DataField(pos = 6, pattern = "MMM-yyyy")
    private Date publishDate;

    @DataField(pos = 7, precision = 2)
    private BigDecimal price;

    @Override
    public int hashCode() {
        int result = category != null ? category.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (titleLanguage != null ? titleLanguage.hashCode() : 0);
        result = 31 * result + (author1 != null ? author1.hashCode() : 0);
        result = 31 * result + (author2 != null ? author2.hashCode() : 0);
        result = 31 * result + (publishDate != null ? publishDate.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookModel bookModel = (BookModel) o;

        if (author1 != null ? !author1.equals(bookModel.author1) : bookModel.author1 != null) return false;
        if (author2 != null ? !author2.equals(bookModel.author2) : bookModel.author2 != null) return false;
        if (category != null ? !category.equals(bookModel.category) : bookModel.category != null) return false;
        if (price != null ? !price.equals(bookModel.price) : bookModel.price != null) return false;
        if (publishDate != null ? !publishDate.equals(bookModel.publishDate) : bookModel.publishDate != null)
            return false;
        if (title != null ? !title.equals(bookModel.title) : bookModel.title != null) return false;
        if (titleLanguage != null ? !titleLanguage.equals(bookModel.titleLanguage) : bookModel.titleLanguage != null)
            return false;

        return true;
    }


    /**
     * Invoke when unmarshal is fired
     * @return
     */
    @Override
    public String toString() {
        return "BookModel{" +
                "category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", titleLanguage='" + titleLanguage + '\'' +
                ", author1='" + author1 + '\'' +
                ", author2='" + author2 + '\'' +
                ", publishDate=" + publishDate +
                ", price=" + price +
                '}';
    }

}
