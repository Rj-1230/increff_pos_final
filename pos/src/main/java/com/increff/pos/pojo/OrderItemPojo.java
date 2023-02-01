package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="order_items",indexes = @Index(name = "index2", columnList = "Order_Id,Product_Id"))

@Getter
@Setter
public class OrderItemPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="orderItem_generator", sequenceName = "orderItem_seq", allocationSize=1,initialValue = 100001)
    @Column(name="Order_Item_Id")
    private Integer orderItemId;
    @Column(name="Order_Id", nullable=false)
    private Integer orderId;
    @Column(name="Product_Id", nullable=false)
    private Integer productId;
    @Column(name="Product_Name", nullable=false)
    private String productName;
    @Column(name="Product_Quantity", nullable=false)
    private Integer quantity;
    @Column(name="Selling_Price", nullable=false)
    private Double sellingPrice ;


}
