package com.douglasdb.camel.feat.core.converter;

import java.math.BigDecimal;

import com.douglasdb.camel.feat.core.domain.purchase.PurchaseOrderDefault;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConverter;



/**
 * 
 * @author Administrator
 *
 */
@Converter
public class PurchaseOrderConverter {

	
	public PurchaseOrderConverter() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @param data
	 * @param exchange
	 * @return
	 */
	@Converter
	public static PurchaseOrderDefault toPurcharseOrderDefault(byte[] data, Exchange exchange) {
		
		TypeConverter converter = exchange.getContext().getTypeConverter();

        String s = converter.convertTo(String.class, data);
        
        if (s == null || s.length() < 30) {
            throw new IllegalArgumentException("data is invalid");
        }

        s = s.replaceAll("##START##", "");
        s = s.replaceAll("##END##", "");

        String name = s.substring(0, 10).trim();
        String s2 = s.substring(10, 20).trim();
        String s3 = s.substring(20).trim();

        BigDecimal price = new BigDecimal(s2);
        price.setScale(2);

        Integer amount = converter.convertTo(Integer.class, s3);

        PurchaseOrderDefault order = new PurchaseOrderDefault(name, amount, price);
        
        return order;
	}
	
}
