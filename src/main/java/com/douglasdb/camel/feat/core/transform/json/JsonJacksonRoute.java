package com.douglasdb.camel.feat.core.transform.json;

import com.douglasdb.camel.feat.core.domain.json.View;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;


/**
 *
 */
public class JsonJacksonRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        from("direct:marshal")
                .marshal().json(JsonLibrary.Jackson)
                .to("mock:marshalResult");

        from("direct:unmarshal")
                .unmarshal().json(JsonLibrary.Jackson, View.class)
                .to("mock:unmarshalResult");

        from("direct:marshal_xstream")
                .marshal().json(JsonLibrary.XStream)
                .to("mock:marshalXStreamResult");

        from("direct:unmarshal_xstream")
                .unmarshal().json(JsonLibrary.XStream, View.class)
                .to("mock:unmarshalXStreamResult");
    }
}
