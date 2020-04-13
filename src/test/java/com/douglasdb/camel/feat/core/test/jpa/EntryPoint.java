package com.douglasdb.camel.feat.core.test.jpa;

import com.douglasdb.camel.feat.core.domain.jpa.PurchaseOrder;
import com.douglasdb.camel.feat.core.jpa.JpaRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.jpa.JpaEndpoint;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityManager;
import java.util.List;

import static java.lang.System.out;

/**
 * @author dbatista
 */
public class EntryPoint extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {

        // TODO Auto-generated method stub
        final String springPath = "META-INF/spring/jpa";
        return new ClassPathXmlApplicationContext(springPath.concat("/app-context.xml"));

    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new JpaRoute();
    }

    @Test
    public void testRouteJpa() throws InterruptedException {

        MockEndpoint mock = super.getMockEndpoint("mock:result");

        mock.setExpectedMessageCount(1);

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setName("motor");
        purchaseOrder.setAmount(1);
        purchaseOrder.setCustomer("honda");

        super.template.sendBody("seda:accounting", purchaseOrder);

        assertMockEndpointsSatisfied();
        assertEntityInDB();
    }


    private void assertEntityInDB() {
        JpaEndpoint jpaEndpoint = context.getEndpoint("jpa:com.douglasdb.camel.feat.core.domain.jpa.PurchaseOrder",
                JpaEndpoint.class);
        EntityManager em = jpaEndpoint.getEntityManagerFactory().createEntityManager();
        List<PurchaseOrder> list = em.createQuery("select x from PurchaseOrder x", PurchaseOrder.class)
                .getResultList();
        assertEquals(1, list.size());
        list.forEach(out::println);
        em.close();

    }


}
