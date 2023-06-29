package com.example.vueback.entity;

import lombok.Getter;

import javax.persistence.*;

@Table(name = "member")
@Getter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto 증가
    private int id;
    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 100)
    private String password;

}
