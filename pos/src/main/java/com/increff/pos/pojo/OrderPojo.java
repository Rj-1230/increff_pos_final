package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;
//import java.time.String;

@Entity
@Table(name="orders",indexes = @Index(name = "multiIndex3", columnList = "orderInvoiceTime, counterID, orderCode"),
        uniqueConstraints={@UniqueConstraint(columnNames={"orderCode"})})
@Getter
@Setter

//Order is a reserved keyword so use diff table name
public class OrderPojo extends AbstractPojo{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_generator")
    @SequenceGenerator(name="order_generator", sequenceName = "order_seq", allocationSize=1,initialValue = 101)
    private Integer orderId;
    @Column(nullable=false)
    private String customerName;
    @Column(nullable = false)
    private String customerPhone;
    @Column(nullable=false)
    private ZonedDateTime orderCreateTime;
    private ZonedDateTime orderInvoiceTime;
    @Column(nullable = false)
    private String status;
    @Column(nullable=false)
    private Integer counterId;
    @Column(nullable = false)
    private String orderCode;

}
