package com.increff.pos.pojo;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="product", indexes = @Index(columnList = "barcode") , uniqueConstraints={@UniqueConstraint(columnNames={"barcode", "brandId"})})

@Getter
@Setter
public class ProductPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable=false)
    private Integer productId;
    @Column(nullable=false)
    private String barcode;
    @Column(nullable=false)
    private Integer brandId;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false)
    private Double mrp;


}
