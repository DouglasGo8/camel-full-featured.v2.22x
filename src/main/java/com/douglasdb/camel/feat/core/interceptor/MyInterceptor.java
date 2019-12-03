package com.douglasdb.camel.feat.core.interceptor;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.processor.DelegateAsyncProcessor;
import org.apache.camel.spi.InterceptStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dbatista
 */
public class MyInterceptor implements InterceptStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(MyInterceptor.class);


    @Override
    public Processor wrapProcessorInInterceptors(CamelContext context,
                                                 ProcessorDefinition<?> definition,
                                                 Processor target,
                                                 Processor nextTarget) throws Exception {
        return new DelegateAsyncProcessor(exchange -> {
            LOG.info("Before the processor...");
            target.process(exchange);
            LOG.info("After the processor...");
        });
    }
}
