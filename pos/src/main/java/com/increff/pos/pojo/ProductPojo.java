package com.increff.pos.pojo;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="product")
//        uniqueConstraints={@UniqueConstraint(columnNames={"Barcode", "BrandID","ProductName"})})

@Getter
@Setter
public class ProductPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MyProductId", nullable=false)
    private Integer productId;
    @Column(name="Barcode", nullable=false)
    private String barcode;
    @Column(name="BrandId", nullable=false)
    private Integer brandId;
    @Column(name="ProductName")
    @NotNull
    private String name;
    @Column(name="Mrp", nullable=false)
    private Double mrp;


}
