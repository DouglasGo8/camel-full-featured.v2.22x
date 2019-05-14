package com.douglasdb.camel.feat.core.domain.purchase;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @author Administrator
 *
 */
@Data
@AllArgsConstructor
public class PurchaseOrderDefault implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private int amount;
	private BigDecimal price;

	public PurchaseOrderDefault() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Ordering " + amount + " of " + name + " at total " + price;
	}

}
