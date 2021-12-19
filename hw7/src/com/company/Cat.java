package com.company;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class Cat implements Serializable {
    private String name;
    private int age;

    public Cat(String name) {
        this.name = name;
        this.age = ThreadLocalRandom.current().nextInt(1,5);
    }

    public String getName() {
        return name;
    }
}
