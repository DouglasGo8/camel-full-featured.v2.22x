package com.douglasdb.camel.feat.core.wiretrap;

import com.douglasdb.camel.feat.core.domain.cheese.Cheese;

/**
 *
 */
public class CheeseRipener {


    /**
     *
     * @param cheese
     */
    public static void ripen(Cheese cheese) {
        cheese.setAge(cheese.getAge() + 1);
    }
}
