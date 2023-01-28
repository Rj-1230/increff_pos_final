package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="cartItems")

@Getter
@Setter

public class CartPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CartItemID", nullable=false)
    private Integer cartItemId;
    @Column(name="ProductName", nullable=false)
    private String productName;
    @Column(name="ProductQuantity", nullable=false)
    private Integer quantity;
    @Column(name="ProductId", nullable=false)
    private Integer productId;
    @Column(name="SellingPrice", nullable=false)
    private Double sellingPrice ;
    @Column(name="CounterID", nullable=false)
    private Integer counterId;


}
