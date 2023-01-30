package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="inventory",indexes = @Index(name = "index1", columnList = "Product_Barcode"),
        uniqueConstraints={@UniqueConstraint(columnNames={"Product_Barcode"})})

@Getter
@Setter

public class InventoryPojo {
    @Id
    @Column(name="Product_Id", nullable=false)
    private Integer productId;
    @Column(name="Product_Quantity", nullable=false)
    private Integer quantity;

    @Column(name="Product_Barcode", nullable=false)
    private String barcode;


}
