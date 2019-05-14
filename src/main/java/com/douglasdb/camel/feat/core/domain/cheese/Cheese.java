package com.douglasdb.camel.feat.core.domain.cheese;

import lombok.Data;

/**
 *
 */
@Data
public class Cheese implements Cloneable {

    private int age;


    @Override
    public Object clone() throws CloneNotSupportedException {
        Cheese cheese = new Cheese();
        cheese.setAge(this.getAge());
        return cheese;
    }

    @Override
    public int hashCode(){
        return this.age;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Cheese cheese = (Cheese) o;

        return this.age == cheese.age;
    }

    @Override
    public String toString() {
        return "Cheese{age=" + age + "}";
    }
}
