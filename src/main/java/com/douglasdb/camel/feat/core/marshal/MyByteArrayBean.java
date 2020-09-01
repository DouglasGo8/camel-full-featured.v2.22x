package com.douglasdb.camel.feat.core.marshal;

import org.apache.camel.Handler;

public class MyByteArrayBean {


    @Handler
    public void message(byte[] data) {

        System.out.println(data);
    }
}
