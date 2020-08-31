package com.douglasdb.camel.feat.core.test.managed;

import com.douglasdb.camel.feat.core.managed.ManagedRoute;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.management.DefaultManagementNamingStrategy;
import org.apache.camel.spi.ManagementAgent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

public class ManagedTest extends CamelTestSupport {

    private static Logger LOG = LoggerFactory.getLogger(ManagedTest.class);

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new ManagedRoute();
    }


    @Override
    protected CamelContext createCamelContext() throws Exception {
        super.enableJMX();
        final CamelContext context = super.createCamelContext();
        // Force hostname to be "localhost" for testing purposes
        final DefaultManagementNamingStrategy naming = (DefaultManagementNamingStrategy) context.getManagementStrategy()
                .getManagementNamingStrategy();
        naming.setHostName("localhost");
        naming.setDomainName("org.apache.camel");

        context.getManagementStrategy().getManagementAgent().setIncludeHostName(true);

        return context;
    }

    @Test
    @SneakyThrows
    public void testManagedResource() {
        final ManagementAgent managementAgent = super.context.getManagementStrategy().getManagementAgent();
        assertNotNull(managementAgent);
        //
        final MBeanServer mBeanServer = managementAgent.getMBeanServer();
        assertNotNull(mBeanServer);
        LOG.info(mBeanServer.toString()); // stuff such JmxMBeanServer@xxxx
        //
        final String mBeanServerDefaultDomain = managementAgent.getMBeanServerDefaultDomain();
        assertEquals("org.apache.camel", mBeanServerDefaultDomain);
        //
        final String managementName = context.getManagementName();
        assertNotNull("CamelContext should have a management name if JMX is enabled", managementName);
        LOG.info("managementName = {}", managementName);
        //
        // Get the Camel Context MBean
        ObjectName onContext = ObjectName.getInstance(mBeanServerDefaultDomain + ":context=localhost/" + managementName + ",type=context,name=\"" + context.getName() + "\"");
        assertTrue("Should be registered", mBeanServer.isRegistered(onContext));

        // Get myManagedBean
        ObjectName onManagedBean = ObjectName.getInstance(mBeanServerDefaultDomain + ":context=localhost/" + managementName + ",type=processors,name=\"myManagedBean\"");
        LOG.info("Canonical Name = {}", onManagedBean.getCanonicalName());
        assertTrue("Should be registered", mBeanServer.isRegistered(onManagedBean));

        // Send a couple of messages to get some route statistics
        super.template.sendBody("direct:start", "Hello Camel");
        super.template.sendBody("direct:start", "Camel Rocks!");


        // Get MBean attribute
        int camelsSeenCount = (Integer) mBeanServer.getAttribute(onManagedBean, "CamelsSeenCount");
        assertEquals(2, camelsSeenCount);

        // Stop the route via JMX
        mBeanServer.invoke(onManagedBean, "resetCamelsSeenCount", null, null);

        camelsSeenCount = (Integer) mBeanServer.getAttribute(onManagedBean, "CamelsSeenCount");
        assertEquals(0, camelsSeenCount);

    }
}
