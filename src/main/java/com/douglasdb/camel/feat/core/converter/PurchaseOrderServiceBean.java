package com.douglasdb.camel.feat.core.converter;

import java.math.BigDecimal;

import org.apache.camel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglasdb.camel.feat.core.domain.PurchaseOrderDefault;


/**
 * 
 * @author Administrator
 *
 */
public class PurchaseOrderServiceBean {

	private static Logger LOG = LoggerFactory.getLogger(PurchaseOrderServiceBean.class);

	public PurchaseOrderDefault lookup(@Header("id") String id) {
		
		LOG.info("Finding purchase order for id " + id);
		// just return a fixed response
		
		PurchaseOrderDefault order = new PurchaseOrderDefault();
		
		order.setPrice(BigDecimal.valueOf(69.99));
		order.setAmount(1);
		order.setName("Camel in Action");
		
		
		return order;
	}
}
