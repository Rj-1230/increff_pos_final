package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Table(name = "daily_sales", indexes = @Index(name = "multiIndex1", columnList = "invoiceDate"),
        uniqueConstraints = {@UniqueConstraint(columnNames = {"invoiceDate"})})

public class DailyReportPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dailyReportId;
    @Column(nullable = false)
    private ZonedDateTime invoiceDate;
    @Column(nullable = false)
    private Integer invoicedOrderCount;
    @Column(nullable = false)
    private Integer invoicedItemsCount;
    @Column(nullable = false)
    private Double totalRevenue;
}