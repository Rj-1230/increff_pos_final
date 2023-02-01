package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="cart_items", indexes = @Index(name = "multiIndex", columnList = "Product_Id, Counter_Id"))

@Getter
@Setter

public class CartPojo extends AbstractPojo{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Cart_Item_Id", nullable=false)
    private Integer cartItemId;
    @Column(name="Product_Name", nullable=false)
    private String productName;
    @Column(name="Product_Quantity", nullable=false)
    private Integer quantity;
    @Column(name="Product_Id", nullable=false)
    private Integer productId;
    @Column(name="Selling_Price", nullable=false)
    private Double sellingPrice ;
    @Column(name="Counter_Id", nullable=false)
    private Integer counterId;


}
