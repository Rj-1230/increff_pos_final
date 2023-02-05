package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="order_items",indexes = @Index(name = "index2", columnList = "orderId,productId"),
        uniqueConstraints={@UniqueConstraint(columnNames={"orderId","productId"})})

@Getter
@Setter
public class OrderItemPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="orderItem_generator", sequenceName = "orderItem_seq", allocationSize=1,initialValue = 100001)
    private Integer orderItemId;
    @Column(nullable=false)
    private Integer orderId;
    @Column(nullable=false)
    private Integer productId;
    @Column(nullable=false)
    private Integer quantity;
    @Column(nullable=false)
    private Double sellingPrice ;


}
