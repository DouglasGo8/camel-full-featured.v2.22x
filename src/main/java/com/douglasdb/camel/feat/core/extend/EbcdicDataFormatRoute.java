package com.douglasdb.camel.feat.core.extend;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.util.ExchangeHelper;

/**
 * 
 * @author Administrator
 *
 */
public class EbcdicDataFormatRoute extends RouteBuilder {

	/**
	 * 
	 */
	public EbcdicDataFormatRoute() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		 
		final EbcdicDataFormat dataFormat = new EbcdicDataFormat("CP037");
		
		from("direct:marshal")
			.marshal(dataFormat);
		
		from("direct:unmarshal")
			.unmarshal(dataFormat);
	}

	/**
	 * 
	 * @author Administrator
	 *
	 */
	final class EbcdicDataFormat implements DataFormat {

		private String codepage = "CP037"; // US EBCDIC code page 000037
		
		/**
		 * 
		 */
		public EbcdicDataFormat() {
			// TODO Auto-generated constructor stub
		}

		
		/**
		 * 
		 * @param codepage
		 */
		public EbcdicDataFormat(String codepage) {
			this.codepage = codepage;
		}

		/**
		 * 
		 */
		@Override
		public void marshal(Exchange exchange, Object graph, OutputStream stream) throws Exception {
			// TODO Auto-generated method stub
			final String str = ExchangeHelper.convertToMandatoryType(exchange, String.class, graph);
			stream.write(str.getBytes(codepage));
		}

		/**
		 * 
		 */
		@Override
		public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
			// TODO Auto-generated method stub
			final byte[] bytes = ExchangeHelper.convertToMandatoryType(exchange, byte[].class, stream);
			return new String(bytes, codepage);
		}

	}

}
