package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;
//import java.time.String;

@Entity
@Table(name="orders",indexes = @Index(name = "multi_index2", columnList = "Order_Invoice_Time, Counter_ID, Order_Code"),
        uniqueConstraints={@UniqueConstraint(columnNames={"Order_Code"})})
@Getter
@Setter

//Order is a reserved keyword so use diff table name
public class OrderPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_generator")
    @SequenceGenerator(name="order_generator", sequenceName = "order_seq", allocationSize=1,initialValue = 101)
    @Column(name="Order_Id", nullable=false)
    private Integer orderId;
    @Column(name="Customer_Name", nullable=false)
    private String customerName;
    @Column(name="Order_Create_Time", nullable=false)
    private ZonedDateTime orderCreateTime;
    @Column(name="Order_Invoice_Time",nullable = false)
    private ZonedDateTime orderInvoiceTime;
    @Column(name="Order_Status",nullable = false)
    private String status;
    @Column(name="Counter_ID", nullable=false)
    private Integer counterId;
    @Column(name="Customer_Phone",nullable = false)
    private String customerPhone;
    @Column(nullable = false, name = "Order_Code", unique = true)
    private String orderCode;

}
