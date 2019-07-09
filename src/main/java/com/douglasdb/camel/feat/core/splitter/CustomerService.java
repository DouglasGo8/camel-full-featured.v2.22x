
package com.douglasdb.camel.feat.core.splitter;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 */
public class CustomerService {

    public List<Department> splitDepartments(Customer customer) {
        // this is a very simple logic, but your use cases
        // may very well require more complex logic
        return customer.getDepartments();
    }

    public static Customer createCustomer() {
        
        final List<Department> departments = new ArrayList<Department>();
      
        departments.add(new Department(222, "Oceanview 66", "89210", "USA"));
        departments.add(new Department(333, "Lakeside 41", "22020", "USA"));
        departments.add(new Department(444, "Highstreet 341", "11030", "USA"));

        Customer customer = new Customer(123, "Honda", departments);

        return customer;
    }
}