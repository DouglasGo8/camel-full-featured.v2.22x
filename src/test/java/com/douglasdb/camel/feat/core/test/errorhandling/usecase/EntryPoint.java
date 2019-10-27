package com.douglasdb.camel.feat.core.test.errorhandling.usecase;

import com.douglasdb.camel.feat.core.errorhandling.usecase.UseCaseRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new UseCaseRoute();
    }

    @Test
    public void setUpHttpServer() throws InterruptedException {

        System.out.println("Starting client... press ctrl + c to stop it");
        System.out.println("... started.");
        System.out.println("Drop files into the target/rider folder");
        System.out.println("for example copy src/main/resources/good.txt to target/rider");
        System.out.println("and copy src/main/resources/bad.txt to target/rider");

        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }
}
