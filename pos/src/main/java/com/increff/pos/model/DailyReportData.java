package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
@Getter
@Setter
public class DailyReportData {
        private String date;
        private Integer invoicedOrderCount;
        private Integer invoicedItemsCount;
        private Double totalRevenue;
    }

