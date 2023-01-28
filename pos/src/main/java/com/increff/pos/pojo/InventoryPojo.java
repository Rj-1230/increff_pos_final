package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="inventory")
//        , uniqueConstraints={@UniqueConstraint(columnNames={"ProductId"})})

@Getter
@Setter

public class InventoryPojo {
    @Column(name="ProductQuantity", nullable=false)
    private Integer quantity;

    @Column(name="Product_Barcode", nullable=false)
    private String barcode;
    @Id
    @Column(name="ProductId", nullable=false)
    private Integer productId;

}
