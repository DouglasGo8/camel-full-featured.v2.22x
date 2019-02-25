package com.douglasdb.camel.feat.core.common;

import java.util.ArrayList;

import com.douglasdb.camel.feat.core.domain.Item;

/**
 * 
 * @author Administrator
 *
 */

public class ItemService {

	private ArrayList<Item> items = new ArrayList<>();

	/**
	 * 
	 */
	public ItemService() {
		// TODO Auto-generated constructor stub
	
		this.items.add(new Item("Thing0"));
		this.items.add(new Item("Thing1"));
		
	}

	
	public Item[] getItems() {
		Item[] out = new Item[items.size()];
		return items.toArray(out);
	}

	public Item getItem(int id) {
		return items.get(id);
	}

	public void setItem(int id, Item item) {
		items.set(id, item);
	}

	public void setItems(Item[] items) {
		this.items.clear();
		for (int i = 0; i < items.length; i++) {
			this.items.add(i, items[i]);
		}
	}

}
