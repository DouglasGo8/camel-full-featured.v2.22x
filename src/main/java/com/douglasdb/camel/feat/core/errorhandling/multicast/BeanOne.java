package com.douglasdb.camel.feat.core.errorhandling.multicast;

/**
 * @author dbatista
 */
public class BeanOne {

    public String fromOutBeanOne(){

        System.out.println("Bean One!");

        throw new NullPointerException("Null pointer Exception here");

        //return "On Success";
    }
}
