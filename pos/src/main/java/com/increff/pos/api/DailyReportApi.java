package com.increff.pos.api;

import com.increff.pos.dao.DailyReportDao;
import com.increff.pos.pojo.DailyReportPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackOn = ApiException.class)
public class DailyReportApi {
    @Autowired
    private DailyReportDao dailyReportDao;

    public void addReport(DailyReportPojo dailyReportPojo) {
        dailyReportDao.insert(dailyReportPojo);
    }

    public DailyReportPojo getReportByDate(ZonedDateTime date) {
        return dailyReportDao.select(date);
    }


    public DailyReportPojo getCheckReportByDate(ZonedDateTime invoiceDate) throws ApiException {
        DailyReportPojo dailyReportPojo = dailyReportDao.select(invoiceDate);
        if (Objects.isNull(dailyReportPojo)) {
            throw new ApiException("No such daily report for given date exists !");
        }
        return dailyReportPojo;
    }


    public List<DailyReportPojo> selectReportByDateFilter(ZonedDateTime startDate, ZonedDateTime endDate) {
        return dailyReportDao.getReportBetweenDateRange(startDate, endDate);
    }


    public void updateDailyReport(ZonedDateTime date, DailyReportPojo newReportPojo) throws ApiException {
        DailyReportPojo dailyReportPojo = getCheckReportByDate(date);
        dailyReportPojo.setInvoicedOrderCount(newReportPojo.getInvoicedOrderCount());
        dailyReportPojo.setTotalRevenue(newReportPojo.getTotalRevenue());
        dailyReportPojo.setInvoicedItemsCount(newReportPojo.getInvoicedItemsCount());
    }
}