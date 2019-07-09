
package com.douglasdb.camel.feat.core.splitter;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 */
@Data
@AllArgsConstructor
public class Customer implements Serializable {

    private final int id;
    private final String name;
    private final List<Department> departments;

    @Override
    public String toString() {
        return "Customer " + id + ", name";
    }

}