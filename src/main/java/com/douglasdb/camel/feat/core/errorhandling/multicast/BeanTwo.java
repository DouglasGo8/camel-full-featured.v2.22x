package com.douglasdb.camel.feat.core.errorhandling.multicast;

/**
 * @author dbatista
 */
public class BeanTwo {

    public String fromOutBeanOne() throws MulticastBusinessException {

        System.out.println("Bean Two!");

        throw new MulticastBusinessException("I'm fail!");
    }
}
