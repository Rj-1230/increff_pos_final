package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class DailyReportData {
    private ZonedDateTime invoiceDate;
    private Integer invoicedOrderCount;
    private Integer invoicedItemsCount;
    private Double totalRevenue;
}

