package com.douglasdb.camel.feat.core.test.testing;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Properties;

public class SimpleTransformPropertiesInjectingSpringTest extends CamelSpringTestSupport {
    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/META-INF/spring/testing/test-properties-context.xml",
                "/META-INF/spring/testing/simpleTransform-context.xml");
    }

    @Override
    protected Boolean ignoreMissingLocationWithPropertiesComponent() {
        return true;
    }

    @Override
    protected Properties useOverridePropertiesWithPropertiesComponent() {
        final Properties properties = new Properties();
        properties.setProperty("start.endpoint", "direct:in");
        properties.setProperty("end.endpoint", "mock:out");
        return properties;
    }
}
