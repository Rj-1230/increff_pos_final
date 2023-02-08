package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user", indexes = @Index(columnList = "email"), uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class UserPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer userId;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

}