package com.increff.pos.pojo;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="product", indexes = @Index(columnList = "Barcode") , uniqueConstraints={@UniqueConstraint(columnNames={"Barcode", "Brand_Id"})})

@Getter
@Setter
public class ProductPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Product_Id", nullable=false)
    private Integer productId;
    @Column(name="Barcode", nullable=false)
    private String barcode;
    @Column(name="Brand_Id", nullable=false)
    private Integer brandId;
    @Column(name="Product_Name")
    @NotNull
    private String name;
    @Column(name="Mrp", nullable=false)
    private Double mrp;


}
