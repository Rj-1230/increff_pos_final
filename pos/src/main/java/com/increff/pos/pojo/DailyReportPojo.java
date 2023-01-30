package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Table(name="daily_sales")

public class DailyReportPojo{
    @Id
    @Column(nullable = false, name = "Invoice_Date")
    private ZonedDateTime invoiceDate;
    @Column(nullable = false, name = "Invoiced_Orders_Count")
    private Integer invoicedOrderCount;
    @Column(nullable = false, name = "Invoiced_Items_Count")
    private Integer invoicedItemsCount;
    @Column(nullable = false, name = "Total_Revenue")
    private Double totalRevenue;
}