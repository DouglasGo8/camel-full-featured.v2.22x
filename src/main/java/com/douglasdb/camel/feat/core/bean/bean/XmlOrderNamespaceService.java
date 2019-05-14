package com.douglasdb.camel.feat.core.bean.bean;

import org.apache.camel.Body;
import org.apache.camel.language.Bean;
import org.apache.camel.language.NamespacePrefix;
import org.apache.camel.language.XPath;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * 
 * @author DouglasDb
 *
 */
public class XmlOrderNamespaceService {

	/**
	 * 
	 * @param xml
	 * @param customerId
	 * @param orderId
	 * @return
	 */
	public Document handleIncomingOrder (
			@Body Document xml, 
			@XPath(value="/c:order/@customerId", 
			       namespaces=@NamespacePrefix(prefix="c", uri="http://camelinaction.com/order")) int customerId, 
			@Bean(ref="guid", method="generate") int orderId) {

		Attr attr = xml.createAttribute("orderId");
		attr.setValue("" + orderId);
		Node node = xml.getElementsByTagName("order").item(0);
		node.getAttributes().setNamedItem(attr);
		
		//LoggerFactory.getLogger(XmlOrderNamespaceService.class)
			//.info("getting Body -- {} with attr {}", xml.toString(), customerId);
		
		return xml;
	}
}
