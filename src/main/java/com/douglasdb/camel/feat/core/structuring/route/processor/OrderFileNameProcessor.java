package com.douglasdb.camel.feat.core.structuring.route.processor;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author douglasdias
 *
 */
public class OrderFileNameProcessor implements Processor {

	private String countryDateFormat;
	private final static String UNIVERSAL_DATE_FORMAT = "yyyy-MM-dd";

	public OrderFileNameProcessor() {
		// TODO Auto-generated constructor stub
	}

	public void setCountryDateFormat(String countryDateFormat) {
		this.countryDateFormat = countryDateFormat;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub

		final Message in = exchange.getIn();

		final String[] fields = in.getBody(String.class).split(",");
		final String countrySpecificDate = fields[0];

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.countryDateFormat);
		final Date date = simpleDateFormat.parse(countrySpecificDate);
		final SimpleDateFormat universalDateFormat = new SimpleDateFormat(UNIVERSAL_DATE_FORMAT);
		final String universalDate = universalDateFormat.format(date);
		//
		fields[0] = universalDate;

		in.setHeader(Exchange.FILE_NAME, universalDate + ".csv");
		in.setBody(StringUtils.join(fields, ","));

	}
}
