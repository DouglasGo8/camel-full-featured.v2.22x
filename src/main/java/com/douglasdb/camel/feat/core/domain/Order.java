package com.douglasdb.camel.feat.core.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Administrator
 *
 */
@Data
@RequiredArgsConstructor
public class Order {

	private String name;
	private int amount;

	/**
	 * 
	 * @param name
	 * @param amount
	 */
	public Order(String name, int amount) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Order[" + name + " , " + amount + "]";
	}

}
