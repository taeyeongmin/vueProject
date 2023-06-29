package com.example.vueback.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "orders")
@Getter
@Setter
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto 증가
    private int id;
    @Column
    private int memberId;
    @Column(length = 50, nullable = false)
    private String name;
    @Column(length = 50, nullable = false)
    private String address;
    @Column(length = 10, nullable = false)
    private String payment;
    @Column(length = 16, nullable = false)
    private String cardNumber;
    @Column(length = 500, nullable = false)
    private String items;


}
