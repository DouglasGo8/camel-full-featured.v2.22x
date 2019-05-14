package com.douglasdb.camel.feat.core.domain.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Administrador
 *
 */
@Data
@XmlRootElement
@NoArgsConstructor
@JsonRootName(value = "bookstore")
@JsonDeserialize(as = ArrayList.class, contentAs = Bookstore.class)
public class Bookstore {

	/**
	 * 
	 */
	
	private List<Book> book = new ArrayList<Book>();

}
