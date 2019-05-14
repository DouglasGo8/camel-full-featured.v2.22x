package com.douglasdb.camel.feat.core.transform.enrich;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.commons.lang.Validate;

public class MergeInReplacementText implements AggregationStrategy {

    public static final String ENRICH_EXAMPLE_ORIGINAL_BODY = "EnrichExample.originalBody";
    public static final String ENRICH_EXAMPLE_REPLACEMENT_STRING = "EnrichExample.replacementString";


    /**
     * When using this AggregationStrategy, this method must be called <b>before</b> the enrich call as this
     * method sets up the message body, and adds some properties needed by the aggregate method.
     */
    public void setup(Exchange exchange) {

        // Stores original Payload in properties

        final String originalBody = exchange.getIn().getBody(String.class);

        exchange.setProperty(ENRICH_EXAMPLE_ORIGINAL_BODY, originalBody);

        final String enrichParameter = originalBody.substring(originalBody.lastIndexOf(" ") + 1);

        exchange.setProperty(ENRICH_EXAMPLE_REPLACEMENT_STRING, enrichParameter);


        //System.out.println(enrichParameter);

        exchange.getIn().setBody(enrichParameter); // original body modified
    }

    @Override
    public Exchange aggregate(Exchange original, Exchange enrichResponse) {


        //System.out.println(original.getIn().getBody(String.class));

        //System.out.println("--->" + original.getProperty(ENRICH_EXAMPLE_ORIGINAL_BODY, String.class));

        final String originalBody = original.getProperty(ENRICH_EXAMPLE_ORIGINAL_BODY, String.class);
        final String replacementString = original.getProperty(ENRICH_EXAMPLE_REPLACEMENT_STRING, String.class);


        // System.out.println(originalBody);
        // System.out.println(replacementString);

        Validate.notEmpty(originalBody, "The property '" + ENRICH_EXAMPLE_ORIGINAL_BODY + "' must be set with the original message body.");
        Validate.notEmpty(replacementString,
                "The property '" + ENRICH_EXAMPLE_REPLACEMENT_STRING + "' must be set with the value to be replaced.");

        final String replacementValue = enrichResponse.getIn().getBody(String.class);

        // System.out.println(replacementValue);

        final String mergeResult = originalBody.replaceAll(replacementString + "$", replacementValue);

        // System.out.println(mergeResult);

        original.getIn().setBody(mergeResult);

        return original;
    }
}
