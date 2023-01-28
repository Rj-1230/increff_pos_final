package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="orderItems")
//@Table(name="orderItems", uniqueConstraints={@UniqueConstraint(columnNames={"ProductId"})})
@Getter
@Setter
//
//Hibernate properties table for null, uniqye
//xslt file
public class OrderItemPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="orderItem_generator", sequenceName = "orderItem_seq", allocationSize=1,initialValue = 100001)
    @Column(name="OrderItemID")
    private Integer orderItemId;
    @Column(name="OrderID", nullable=false)
    private Integer orderId;

    @Column(name="ProductID", nullable=false)
    private Integer productId;
    @Column(name="Product_Name", nullable=false)
    private String productName;
    @Column(name="ProductQuantity", nullable=false)
    private Integer quantity;
    @Column(name="SellingPrice", nullable=false)
    private Double sellingPrice ;


}
