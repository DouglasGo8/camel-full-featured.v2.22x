package com.douglasdb.camel.feat.core.errorhandling.multicast;

/**
 * @author dbatista
 */
public class BeanOne {

    public String fromOutBeanOne(){

        System.out.println("Bean One!");

        return "On Success";
    }
}
