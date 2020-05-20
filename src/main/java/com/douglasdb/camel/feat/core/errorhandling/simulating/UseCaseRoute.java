package com.douglasdb.camel.feat.core.errorhandling.simulating;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;

import java.io.IOException;

public class UseCaseRoute extends RouteBuilder {

    @EndpointInject(ref = "fileEndpoint")
    private Endpoint file;

    @EndpointInject(ref = "httpEndpoint")
    private Endpoint http;

    @EndpointInject(ref = "ftpEndpoint")
    private Endpoint ftp;

    @Override
    public void configure() throws Exception {

        super.getContext().setTracing(true);

        errorHandler(defaultErrorHandler()
                .maximumRedeliveries(5)
                .redeliveryDelay(2000)
                .retryAttemptedLogLevel(LoggingLevel.WARN));

        // in case of a http exception then retry at most 3 times
        // and if exhausted then upload using ftp instead
        onException(IOException.class, HttpOperationFailedException.class)
                .maximumRedeliveries(3)
                .handled(true)
                .log("Sending to FTP")
                .to(ftp);

        from(file).to(http);
    }
}
