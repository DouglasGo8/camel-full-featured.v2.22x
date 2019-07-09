
package com.douglasdb.camel.feat.core.splitter;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 */
@Data
@AllArgsConstructor
public class Department implements Serializable {

    private final int id;
    private final String zip;
    private final String country;
    private final String address;

    @Override
    public String toString() {
        return "Department " + id + ", " + address + ", " + zip + ", " + country;
    }

}