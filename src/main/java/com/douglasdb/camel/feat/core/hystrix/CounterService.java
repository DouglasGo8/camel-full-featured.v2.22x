package com.douglasdb.camel.feat.core.hystrix;

import java.io.IOException;

public class CounterService {

    private int counter;

    public String count() throws IOException {
        counter++;
        if (counter % 5 == 0) {
            throw new IOException("Forced error");
        }
        return "Count " + counter;
    }
}
