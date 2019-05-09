package com.douglasdb.camel.feat.core.domain.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

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
public class Bookstore {
	
	
	/**
	 * 
	 */
	private List<Book> book = new ArrayList<Book>();
	
}
