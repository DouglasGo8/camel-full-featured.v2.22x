package com.douglasdb.camel.feat.core.domain.purchase;

import java.math.BigDecimal;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import lombok.Data;
/**
 * 
 * @author Administrator
 *
 */
@Data
@CsvRecord(separator = ",", crlf = "UNIX")
public class PurchaseOrderCsv {

	@DataField(pos = 1)
	private String name;

	@DataField(pos = 2, precision = 2)
	private BigDecimal price;

	@DataField(pos = 3)
	private int amount;

}
