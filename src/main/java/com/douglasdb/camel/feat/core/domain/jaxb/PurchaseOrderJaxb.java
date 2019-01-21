package com.douglasdb.camel.feat.core.domain.jaxb;



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
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PurchaseOrderJaxb {


    @XmlAttribute
    private String name;

    @XmlAttribute
    private double price;

    @XmlAttribute
    private double amount;

	
}
