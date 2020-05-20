package com.douglasdb.camel.feat.core.test.jdbc;

import com.douglasdb.camel.feat.core.jdbc.JdbcRouter;
import com.douglasdb.camel.feat.core.jdbc.OrderToSqlBean;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;
import static org.apache.camel.component.jms.JmsComponent.jmsComponentClientAcknowledge;
/**
 *
 */
public class EntryPoint extends CamelTestSupport {

    private JdbcTemplate jdbc;

    @Before
    public void setupDatabase() {

        final DataSource ds = super.context.getRegistry().lookupByNameAndType("dataSource", DataSource.class);

        this.jdbc = new JdbcTemplate(ds);

        jdbc.execute("create table INCOMING_ORDERS (part_name varchar(20), quantity int, customer varchar(20))");
    }

    @After
    public void dropDatabase() throws Exception {
        jdbc.execute("drop table incoming_orders");
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new JdbcRouter();
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        // TODO Auto-generated method stub

       final CamelContext camelContext = super.createCamelContext();

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        camelContext.addComponent("jms", jmsComponentClientAcknowledge(connectionFactory));


        return camelContext;
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {

        JndiRegistry jndi = super.createRegistry();
        jndi.bind("orderToSql", new OrderToSqlBean());

        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
        ds.setUrl("jdbc:derby:memory:order;create=true");
        //ds.setUsername("");
        //ds.setPassword("");
        jndi.bind("dataSource", ds);

        return jndi;
    }

    @Test
    public void testJdbcInsert() throws InterruptedException {

        MockEndpoint mock = super.getMockEndpoint("mock:result");

        int rows = jdbc.queryForObject("select count(*) from incoming_orders", Integer.class);

        assertEquals(0, rows);

        template.sendBody("jms:accounting", "<order name=\"motor\" amount=\"1\" customer=\"honda\"/>");

        mock.assertIsSatisfied();

        rows = jdbc.queryForObject("select count(*) from incoming_orders", Integer.class);

        assertEquals(1, rows);
    }


}
