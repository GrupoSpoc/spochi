package com.spochi.dto;

public class PersonDTO {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public PersonDTO setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public PersonDTO setAge(int age) {
        this.age = age;
        return this;
    }
}
