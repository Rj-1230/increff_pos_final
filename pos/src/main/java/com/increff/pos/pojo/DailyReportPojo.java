package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "pos_day_sales")
public class DailyReportPojo{
    @Id
    @Column(nullable = false, name = "date")
    private LocalDate date;
    @Column(nullable = false, name = "invoiced_orders_count")
    private Integer invoicedOrderCount;
    @Column(nullable = false, name = "invoiced_items_count")
    private Integer invoicedItemsCount;
    @Column(nullable = false, name = "total_revenue")
    private Double totalRevenue;
}