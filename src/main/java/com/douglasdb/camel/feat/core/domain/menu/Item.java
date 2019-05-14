package com.douglasdb.camel.feat.core.domain.menu;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * 
 * @author Administrator
 *
 */
@Data
@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class Item implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7128462351355453316L;


	public Item() {
		// TODO Auto-generated constructor stub
	}

	public Item(String name) {
		this.name = name;
	}

	@XmlAttribute
	private String name;

	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Item item = (Item) o;

		return name != null ? name.equals(item.name) : item.name == null;
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}
}
