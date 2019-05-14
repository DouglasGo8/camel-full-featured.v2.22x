package com.douglasdb.camel.feat.core.transform.csv;

import com.douglasdb.camel.feat.core.domain.csv.BookModel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;

/**
 *
 */
public class CsvRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        final DataFormat bindy = new BindyCsvDataFormat(BookModel.class);

        from("direct:unmarshal").unmarshal(bindy);
        from("direct:marshal").marshal(bindy);

    }

}
