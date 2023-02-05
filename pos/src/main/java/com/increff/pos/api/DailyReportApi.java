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
    private DailyReportDao dao;

    public void addReport(DailyReportPojo pojo){
            dao.insert(pojo);
    }

    public DailyReportPojo getReportByDate(ZonedDateTime date){
        return dao.select(date);
    }


    public DailyReportPojo getCheckReportByDate(ZonedDateTime invoiceDate) throws ApiException {
        DailyReportPojo dailyReportPojo = dao.select(invoiceDate);
        if(Objects.isNull(dailyReportPojo)){
            throw new ApiException("No such daily report for given date exists !");
        }
        return dailyReportPojo;
    }


    public List<DailyReportPojo> selectReportByDateFilter(ZonedDateTime start, ZonedDateTime end){
    return dao.getReportBetweenDateRange(start, end);
    }


    public void update(ZonedDateTime date, DailyReportPojo newPojo) throws ApiException {
        DailyReportPojo pojo = getCheckReportByDate(date);
        pojo.setInvoicedOrderCount(newPojo.getInvoicedOrderCount());
        pojo.setTotalRevenue(newPojo.getTotalRevenue());
        pojo.setInvoicedItemsCount(newPojo.getInvoicedItemsCount());
    }
}