package com.douglasdb.camel.feat.core.test.monitoring;

import com.douglasdb.camel.feat.core.monitoring.MonitorRoute;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.management.DefaultManagementNamingStrategy;
import org.apache.camel.spi.ManagementAgent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import javax.management.MBeanServer;

public class MonitorTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new MonitorRoute();
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        super.enableJMX();
        final CamelContext context = super.createCamelContext();

        DefaultManagementNamingStrategy naming = (DefaultManagementNamingStrategy) context
                .getManagementStrategy().getManagementNamingStrategy();
        //
        naming.setHostName("localhost");
        naming.setDomainName("org.apache.camel");

        // setup the ManagementAgent to include the hostname
        context.getManagementStrategy().getManagementAgent().setIncludeHostName(true);

        return context;
    }

    @Test
    public void testMonitor() throws Exception {
        final ManagementAgent managementAgent = context.getManagementStrategy().getManagementAgent();
        assertNotNull(managementAgent);

        final MBeanServer mBeanServer = managementAgent.getMBeanServer();
        assertNotNull(mBeanServer);

        final String mBeanServerDefaultDomain = managementAgent.getMBeanServerDefaultDomain();
        assertEquals("org.apache.camel", mBeanServerDefaultDomain);

        final String managementName = context.getManagementName();
        log.info("managementName = {}", managementName);

        getMockEndpoint("mock:result").expectedMessageCount(3);
        getMockEndpoint("mock:monitor").expectedMessageCount(4);

        // Send a couple of messages to get some route statistics
        template.sendBody("direct:start", "Hello Camel");
        Thread.sleep(1000);
        template.sendBody("direct:start", "Camel Rocks!");
        Thread.sleep(1000);
        template.sendBody("direct:start", "Camel Rocks!");
        Thread.sleep(1000);
    }
}

