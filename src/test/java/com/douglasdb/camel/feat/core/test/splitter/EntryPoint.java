
package com.douglasdb.camel.feat.core.test.splitter;

import com.douglasdb.camel.feat.core.domain.splitter.ListWrapper;
import com.douglasdb.camel.feat.core.splitter.Customer;
import com.douglasdb.camel.feat.core.splitter.CustomerService;
import com.douglasdb.camel.feat.core.splitter.SplitXmlNamespaceRoute;
import org.apache.camel.*;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.*;

/**
 *
 */
public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new SplitXmlNamespaceRoute();
        // SplitXmlNamespacesRoute();
        // SplitXmlRoute();
        // SplitReaggregateRoute();
        // SplitParallelProcessingTimeoutRoute();
        // SplitParallelProcessingRoute();
        // SplitParallelProcessingExceptionHandlingRoute();
        // SplitExecutorServiceRoute();
        // SplitAggregateExceptionHandlingRoute();
        // SplitAggregateRoute();
        // SplitExceptionHandlingStopOnExceptionRoute();
        // SplitExceptionHandlingRoute();
        // SplitMultiLineRoute();
        // SplitNaturalRoute();
        // SplitSimpleExpressionRoute();
        // SplitStopOnExceptionRoute();
        // SplitterBeanRoute();
        // SplitAggregateExceptionABCRoute();
        // SplitterAggregateABCRoute();
        // SplitterABCRoute();
    }

    @Test
    @Ignore
    public void testSplitABC() throws Exception {

        MockEndpoint mock = super.getMockEndpoint("mock:split");

        mock.expectedBodiesReceived("A", "B", "C");

        super.template.sendBody("direct:start", Arrays.asList("A", "B", "C"));

        assertMockEndpointsSatisfied();
    }

    @Test
    @Ignore
    public void testSplitAggregateABC() throws Exception {

        MockEndpoint split = super.getMockEndpoint("mock:split");
        split.expectedBodiesReceived("Camel rocks", "Hi mom", "Yes it works");

        MockEndpoint result = super.getMockEndpoint("mock:result");
        result.expectedBodiesReceived("Camel rocksHi momYes it works");

        super.template.sendBody("direct:start", "A,B,C");

        assertMockEndpointsSatisfied();

    }

    @Test
    @Ignore
    public void testSplitAggregateExceptionABC() throws Exception {

        MockEndpoint split = getMockEndpoint("mock:split");
        // we expect 2 messages successfully to be split and translated into a quote
        split.expectedBodiesReceived("Camel rocks", "Yes it works");

        MockEndpoint result = getMockEndpoint("mock:result");
        // and one combined aggregated message as output with two two quotes together
        result.expectedBodiesReceived("Camel rocks+Yes it works");

        super.template.sendBody("direct:start", "A,F,C");

        assertMockEndpointsSatisfied();
    }

    @Test
    @Ignore
    public void testSplitBean() throws Exception {

        MockEndpoint mock = super.getMockEndpoint("mock:split");

        mock.expectedMessageCount(3);

        Customer customer = CustomerService.createCustomer();

        super.template.sendBody("direct:start", customer);

        assertMockEndpointsSatisfied();

    }

    @Test
    @Ignore
    public void testSplitStopOnException() throws Exception {

        MockEndpoint split = super.getMockEndpoint("mock:split");

        split.expectedBodiesReceived("Camel rocks");

        MockEndpoint result = super.getMockEndpoint("mock:result");
        result.expectedMessageCount(0);


        try {

            super.template.sendBody("direct:start", "A,F,C");
            fail("Should have thrown an exception");


        } catch (CamelExecutionException e) {
            CamelExchangeException cause = assertIsInstanceOf(CamelExchangeException.class, e.getCause());
            IllegalArgumentException iae = assertIsInstanceOf(IllegalArgumentException.class, cause.getCause());
            assertEquals("Key not a known word F", iae.getMessage());

        }


        assertMockEndpointsSatisfied();
    }

    @Test
    @Ignore
    public void testSplitSimpleExpression() throws InterruptedException {

        MockEndpoint mock = super.getMockEndpoint("mock:out");
        mock.setExpectedMessageCount(3);
        mock.expectedBodiesReceived("one", "two", "three");

        ListWrapper wrapper = new ListWrapper();
        wrapper.setWrapped(Arrays.asList("one", "two", "three"));
        super.template.sendBody("direct:in", wrapper);

        assertMockEndpointsSatisfied();

    }

    @Test
    @Ignore
    public void testSplitNaturalArray() throws InterruptedException {
        String[] array = new String[]{"one", "two", "three"};
        MockEndpoint mockSplit = super.getMockEndpoint("mock:split");

        mockSplit.expectedMessageCount(3);
        mockSplit.expectedBodiesReceived("one", "two", "three");

        MockEndpoint mockOut = super.getMockEndpoint("mock:out");
        mockOut.expectedMessageCount(1);
        mockOut.message(0).body().isEqualTo(array);

        super.template.sendBody("direct:in", array);

        assertMockEndpointsSatisfied();
    }

    @Test
    @Ignore
    public void testSplitNaturalList() throws InterruptedException {
        List<String> list = Arrays.asList("one", "two", "three");
        MockEndpoint mockSplit = super.getMockEndpoint("mock:split");

        mockSplit.expectedMessageCount(3);
        mockSplit.expectedBodiesReceived("one", "two", "three");

        MockEndpoint mockOut = super.getMockEndpoint("mock:out");
        mockOut.expectedMessageCount(1);
        mockOut.message(0).body().isEqualTo(list);

        super.template.sendBody("direct:in", list);

        assertMockEndpointsSatisfied();
    }

    @Test
    @Ignore
    public void testSplitNaturalIterable() throws InterruptedException {

        Set<String> set = new TreeSet<>();
        set.add("one");
        set.add("two");
        set.add("three");
        Iterator<String> iterator = set.iterator();
        MockEndpoint mockSplit = super.getMockEndpoint("mock:split");

        mockSplit.expectedMessageCount(3);
        mockSplit.expectedBodiesReceivedInAnyOrder("one", "two", "three");

        MockEndpoint mockOut = super.getMockEndpoint("mock:out");
        mockOut.expectedMessageCount(1);
        mockOut.message(0).body().isEqualTo(iterator);

        super.template.sendBody("direct:in", iterator);

        assertMockEndpointsSatisfied();
    }

    @Test
    @Ignore
    public void testSplitMultiLineString() throws InterruptedException {
        MockEndpoint mock = super.getMockEndpoint("mock:out");

        mock.expectedBodiesReceived("this is a", "multi-line", "piece of text");

        final String multiLineString = "this is a\nmulti-line\npiece of text";

        super.template.sendBody("direct:in", multiLineString);

        assertMockEndpointsSatisfied();
    }

    @Test
    @Ignore
    public void testRemainderElementsProcessedOnException() throws InterruptedException {
        String[] array = new String[]{"one", "two", "three"};

        MockEndpoint mockSplit = super.getMockEndpoint("mock:split");
        mockSplit.expectedMessageCount(2);
        mockSplit.expectedBodiesReceived("two", "three");

        MockEndpoint mockOut = super.getMockEndpoint("mock:out");
        mockOut.expectedMessageCount(0);

        try {
            super.template.sendBody("direct:in", array);
            fail("Exception not thrown");
        } catch (CamelExecutionException ex) {
            assertTrue(ex.getCause() instanceof IllegalStateException);
            assertMockEndpointsSatisfied();
        }
    }

    @Test
    @Ignore
    public void testNoElementsProcessedAfterException() throws InterruptedException {

        final String[] array = new String[]{"one", "two", "three"};

        MockEndpoint mockSplit = super.getMockEndpoint("mock:split");
        mockSplit.expectedMessageCount(1);
        mockSplit.expectedBodiesReceived("one");

        MockEndpoint mockOut = super.getMockEndpoint("mock:out");
        mockOut.expectedMessageCount(0);

        try {
            super.template.sendBody("direct:in", array);
            fail("Exception not thrown");
        } catch (CamelExecutionException ex) {
            assertTrue(ex.getCause() instanceof CamelExchangeException);
            log.info(ex.getMessage());
            assertMockEndpointsSatisfied();
        }
    }

    @Test
    @Ignore
    public void testSplitAggregatesResponses() throws InterruptedException {

        String[] array = new String[]{"one", "two", "three"};
        MockEndpoint mock = super.getMockEndpoint("mock:out");
        mock.expectedMessageCount(1);

        super.template.sendBody("direct:in", array);

        assertMockEndpointsSatisfied();

        Exchange exchange = mock.getReceivedExchanges().get(0);
        @SuppressWarnings("unchecked")
        Set<String> backendResponses = Collections.checkedSet(exchange.getIn().getBody(Set.class), String.class);
        assertTrue(backendResponses.containsAll(Arrays.asList("Processed: one", "Processed: two", "Processed: three")));
    }

    @Test
    @Ignore
    public void testHandlesException() throws InterruptedException {
        String[] array = new String[]{"one", "two", "three"};

        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.expectedMessageCount(1);

        template.sendBody("direct:in", array);

        assertMockEndpointsSatisfied();
        Exchange exchange = mockOut.getReceivedExchanges().get(0);
        @SuppressWarnings("unchecked")
        Set<String> backendResponses = Collections.checkedSet(exchange.getIn().getBody(Set.class), String.class);
        assertTrue(backendResponses.containsAll(Arrays.asList("Processed: one", "Failed: two", "Processed: three")));
    }

    @Test
    @Ignore
    public void testSplittingInParallel() throws InterruptedException {

        final List<String> messageFragments = new ArrayList<>();
        final int fragmentCount = 50;

        for (int i = 0; i < 50; i++)
            messageFragments.add("fragment" + i);

        System.out.println(messageFragments);

        MockEndpoint mockSplit = getMockEndpoint("mock:split");
        mockSplit.setExpectedMessageCount(fragmentCount);
        mockSplit.expectedBodiesReceivedInAnyOrder(messageFragments);

        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(1);
        mockOut.message(0).body().isEqualTo(messageFragments);

        template.sendBody("direct:in", messageFragments);

        assertMockEndpointsSatisfied();

    }

    @Test
    @Ignore
    public void testSplittingInParallelWithException() throws InterruptedException {

        final List<String> messageFragments = new ArrayList<>();
        final int fragmentCount = 50;

        for (int i = 0; i < 50; i++)
            messageFragments.add("fragment" + i);

        //System.out.println(messageFragments);

        int indexOnWhichExceptionThrown = 20;
        MockEndpoint mockSplit = getMockEndpoint("mock:split");
        mockSplit.setMinimumExpectedMessageCount(indexOnWhichExceptionThrown);

        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(0);

        try {
            template.sendBody("direct:in", messageFragments);
            fail();
        } catch (Exception e) {
            assertMockEndpointsSatisfied();
        }
    }

    @Test
    @Ignore
    public void testSplittingInParallelBasic() throws InterruptedException {

        List<String> messageFragments = new ArrayList<String>();
        int fragmentCount = 50;

        for (int i = 0; i < fragmentCount; i++) {
            messageFragments.add("fragment" + i);
        }
        MockEndpoint mockSplit = getMockEndpoint("mock:split");
        mockSplit.setExpectedMessageCount(fragmentCount);
        mockSplit.expectedBodiesReceivedInAnyOrder(messageFragments);

        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(1);
        mockOut.message(0).body().isEqualTo(messageFragments);

        template.sendBody("direct:in", messageFragments);

        assertMockEndpointsSatisfied();

    }

    @Test
    @Ignore
    public void testSplittingInParallelWithTimeout() throws InterruptedException {

        final int fragmentCount = 50;
        final List<String> messageFragments = new ArrayList<String>();

        for (int i = 0; i < fragmentCount; i++) {
            messageFragments.add("fragment" + i);
        }

        MockEndpoint mockSplit = getMockEndpoint("mock:split");
        mockSplit.setExpectedMessageCount(fragmentCount - 1); // 49

        ArrayList<String> expectedFragments = new ArrayList<String>(messageFragments);
        int indexDelayed = 20;

        expectedFragments.remove(indexDelayed); // removes 20th element
        mockSplit.expectedBodiesReceivedInAnyOrder(expectedFragments);

        // System.out.println(expectedFragments);

        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(1);

        template.sendBody("direct:in", messageFragments);
        assertMockEndpointsSatisfied();
    }

    @Test
    @Ignore
    public void testSplitAggregatesResponsesWithXml() throws Exception {

        MockEndpoint mockOut = super.getMockEndpoint("mock:out");

        mockOut.expectedMessageCount(2);

        final String fileName = "./src/main/resources/META-INF/bookstore/books.xml";

        assertFileExists(fileName);

        super.template.sendBody("direct:in",
                new FileInputStream(fileName));


        assertMockEndpointsSatisfied();
        List<Exchange> receivedExchanges = mockOut.getReceivedExchanges();
        assertBooksByCategory(receivedExchanges.get(0));
        assertBooksByCategory(receivedExchanges.get(1));
    }

    @Test
    @Ignore
    public void testSplitAggregatesResponsesCombined() throws Exception {
        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.expectedMessageCount(2);

        final String fileName = "./src/main/resources/META-INF/bookstore/books.xml";

        assertFileExists(fileName);

        super.template.sendBody("direct:in",
                new FileInputStream(fileName));

        assertMockEndpointsSatisfied();

        List<Exchange> receivedExchanges = mockOut.getReceivedExchanges();
        assertBooksByCategory(receivedExchanges.get(0));
        assertBooksByCategory(receivedExchanges.get(1));
    }

    @Test
    @Ignore
    public void splitXmlTest() throws Exception {

        MockEndpoint mockOut = super.getMockEndpoint("mock:out");

        mockOut.expectedMessageCount(2);
        mockOut.expectedBodiesReceived("Scott Cranton", "Jakub Korab");

        final String fileName = "./src/main/resources/META-INF/bookstore/books.xml";

        assertFileExists(fileName);
        super.template.sendBody("direct:in",
                new FileInputStream(fileName));

        assertMockEndpointsSatisfied();


    }

    @Test
    @Ignore
    public void splitXmlWithNamespacesTest() throws Exception {

        MockEndpoint mockOut = super.getMockEndpoint("mock:out");

        mockOut.expectedMessageCount(2);
        mockOut.expectedBodiesReceived("Scott Cranton", "Jakub Korab");

        final String fileName = "./src/main/resources/META-INF/bookstore/books-ns.xml";

        assertFileExists(fileName);

        super.template.sendBody("direct:in",
                new FileInputStream(fileName));

        assertMockEndpointsSatisfied();


    }

    @Test
    public void splitXmlWithNamespaceTest() throws Exception {

        MockEndpoint mockOut = super.getMockEndpoint("mock:out");

        mockOut.expectedMessageCount(2);
        mockOut.expectedBodiesReceived("Scott Cranton", "Jakub Korab");

        final String fileName = "./src/main/resources/META-INF/bookstore/books-ns.xml";

        assertFileExists(fileName);

        super.template.sendBody("direct:in",
                new FileInputStream(fileName));

        assertMockEndpointsSatisfied();


    }


    private void assertBooksByCategory(Exchange exchange) {
        Message in = exchange.getIn();
        @SuppressWarnings("unchecked")
        Set<String> books = Collections.checkedSet(in.getBody(Set.class), String.class);
        String category = in.getHeader("category", String.class);

        switch (category) {
            case "Tech":
                assertTrue(books.containsAll(Collections.singletonList("Apache Camel Developer's Cookbook")));
                break;
            case "Cooking":
                assertTrue(books.containsAll(Arrays.asList("Camel Cookbook",
                        "Double decadence with extra cream", "Cooking with Butter")));
                break;
            default:
                fail();
                break;
        }
    }
}