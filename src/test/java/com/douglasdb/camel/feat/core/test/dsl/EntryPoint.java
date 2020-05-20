package com.douglasdb.camel.feat.core.test.dsl;

import org.apache.camel.test.junit4.CamelTestSupport;

public class EntryPoint extends CamelTestSupport {

    public void setUp() throws Exception {
        // delete directories so we have a clean start
        deleteDirectory("target/inbox");
        deleteDirectory("target/outbox");
        super.setUp();
    }
}
