package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="user", indexes = @Index(columnList = "User_Email") , uniqueConstraints={@UniqueConstraint(columnNames={"User_Email"})})
public class UserPojo{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name="User_Id", nullable=false)
        private Integer id;
        @Column(name="User_Email", nullable=false)
        private String email;
        @Column(name="User_Password", nullable=false)
        private String password;

}