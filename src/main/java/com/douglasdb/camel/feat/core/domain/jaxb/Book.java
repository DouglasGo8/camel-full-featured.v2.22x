package com.douglasdb.camel.feat.core.domain.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Administrador
 *
 */
@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class Book implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4969479925583084606L;

	private int year;
	private double price;

	private Title title;

	@XmlAttribute
	private String category;

	private List<String> author = new ArrayList<String>();

	/**
	 * 
	 * @author Administrador
	 *
	 */
	@Data
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Title {

		@XmlAttribute
		private String lang;
		
		
		@XmlValue
		private String value;
	}
}
