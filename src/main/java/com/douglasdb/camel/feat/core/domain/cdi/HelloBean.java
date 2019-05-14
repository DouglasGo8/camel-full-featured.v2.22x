package com.douglasdb.camel.feat.core.domain.cdi;

import javax.inject.Named;
import javax.inject.Singleton;

import lombok.Data;

/**
 * @author DouglasDb
 */
@Data
@Singleton
@Named(value = "helloBean")
public class HelloBean {

    private int counter;

    @Override
    public String toString() {
        return String.format("Hello %d (n) times", ++counter);
    }
}