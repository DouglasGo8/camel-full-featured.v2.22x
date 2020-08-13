package com.douglasdb.camel.feat.core.domain.splitter;


import lombok.Data;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.dataformat.bindy.annotation.FixedLengthRecord;

import java.io.Serializable;

@Data
@FixedLengthRecord
public class Record implements Serializable {
    @DataField(pos = 1, length = 4)
    private String id;

    @DataField(pos = 2, length = 12)
    private String brand;

    @DataField(pos = 3, length = 20)
    private String model;
}
