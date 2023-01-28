package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="brand")

@Getter
@Setter
public class BrandPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="BrandID")
    private Integer id;
    @Column(name="Brand", nullable=false)
    private String brand;
    @Column(name="Category", nullable=false)
    private String category;

}
