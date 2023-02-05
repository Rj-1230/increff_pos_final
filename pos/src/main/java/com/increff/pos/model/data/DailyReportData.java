package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Getter
@Setter
public class DailyReportData {
        private Integer dailyReportId;
        private ZonedDateTime invoiceDate;
        private Integer invoicedOrderCount;
        private Integer invoicedItemsCount;
        private Double totalRevenue;
    }

