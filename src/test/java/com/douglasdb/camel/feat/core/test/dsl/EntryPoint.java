package com.douglasdb.camel.feat.core.test.dsl;

import com.douglasdb.camel.feat.core.dsl.FileMoveRoute;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.io.File;

public class EntryPoint extends CamelTestSupport {

    private final String dir = "F:/.camel/data";

    public void setUp() throws Exception {
        // delete directories so we have a clean start

        //
        deleteDirectory(dir.concat("/inbox"));
        deleteDirectory(dir.concat("/outbox"));
        //
        super.setUp();
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new FileMoveRoute();
    }

    @Test
    public void testMoveFile() {

        final NotifyBuilder notify = new NotifyBuilder(super.context).whenDone(1).create();

        super.template.sendBodyAndHeader("file://".concat(dir.concat("/inbox")),
                "Hello World", Exchange.FILE_NAME, "hello.txt");

        assertTrue(notify.matchesMockWaitTime());

        // test the file was moved
        File target = new File(dir.concat("/outbox/hello.txt"));
        assertTrue("File should have been moved", target.exists());

        // test that its content is correct as well
        String content = context.getTypeConverter().convertTo(String.class, target);
        assertEquals("Hello World", content);
    }
}
