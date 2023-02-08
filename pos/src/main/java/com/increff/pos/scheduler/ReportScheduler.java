package com.increff.pos.scheduler;

import com.increff.pos.dto.ReportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;

@EnableAsync
public class ReportScheduler {
    @Autowired
    ReportDto reportDto;

    @Async
    @Scheduled(cron = "${cron.expression}")
    public void createDailyReport() {
        try {
            reportDto.createDailyReport();
        } catch (Exception e) {
            return;
        }
    }
}