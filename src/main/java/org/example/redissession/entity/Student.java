package org.example.redissession.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Student implements Serializable {
    String name;
    Integer age;
}
