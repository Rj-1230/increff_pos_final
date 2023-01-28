package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
//import java.time.String;

@Entity
@Table(name="orders")

@Getter
@Setter

//Order is a reserved keyword so use diff table name
public class OrderPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_generator")
    @SequenceGenerator(name="order_generator", sequenceName = "order_seq", allocationSize=1,initialValue = 101)
    @Column(name="MyOrderID", nullable=false)
    private Integer orderId;
    @Column(name="CustomerName", nullable=false)
    private String customerName;
    @Column(name="OrderCreateTime", nullable=false)
    private String orderCreateTime;
    @Column(name="OrderPlaceTime",nullable = false)
    private String orderPlaceTime;
    @Column(name="OrderStatus",nullable = false)
    private String status;
    @Column(name="CounterID", nullable=false)
    private Integer counterId;
    @Column(name="CustomerPhone",nullable = false)
    private String customerPhone;
    @Column(nullable = false, name = "OrderCode", unique = true)
    private String orderCode;

}
