package com.douglasdb.camel.feat.core.managed;

import org.apache.camel.Handler;
import org.apache.camel.api.management.ManagedAttribute;
import org.apache.camel.api.management.ManagedOperation;
import org.apache.camel.api.management.ManagedResource;

@ManagedResource(description = "My Managed Bean within Camel")
public class MyManagedBean {
    private int camelsSeenCount;

    @Handler
    public String doSomething(String body) {
        if (body.contains("Camel")) camelsSeenCount++;
        return "Managed " + body;
    }

    @ManagedAttribute(description = "How many Camels Have been Seen")
    public int getCamelsSeenCount() {
        return this.camelsSeenCount;
    }

    @ManagedOperation(description = "Set Camels Seen Count to Zero")
    public void resetCamelsSeenCount() {
        this.camelsSeenCount = 0;
    }
}
