package com.increff.pos.api;

import com.increff.pos.dao.ReportDao;
import com.increff.pos.pojo.DailyReportPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ReportApi {
    @Autowired
    private ReportDao dao;

    @Transactional
    public void addReport(DailyReportPojo pojo){
            dao.insert(pojo);
    }

    @Transactional
    public DailyReportPojo getReportByDate(ZonedDateTime date){
        return dao.select(date);
    }

    @Transactional
    public DailyReportPojo getCheckReportByDate(ZonedDateTime date) throws ApiException {
        DailyReportPojo dailyReportPojo = dao.select(date);
        if(Objects.isNull(dailyReportPojo)){
            throw new ApiException("No such dailyReport with given date exists !");
        }
        return dailyReportPojo;
    }

    @Transactional
    public List<DailyReportPojo> selectReportByDateFilter(ZonedDateTime start, ZonedDateTime end){
    return dao.selectReportByDateFilter(start, end);
    }

    @Transactional
    public void update(ZonedDateTime date, DailyReportPojo newPojo) throws ApiException {
        DailyReportPojo pojo = getCheckReportByDate(date);
        pojo.setInvoicedOrderCount(newPojo.getInvoicedOrderCount());
        pojo.setTotalRevenue(newPojo.getTotalRevenue());
        pojo.setInvoicedItemsCount(newPojo.getInvoicedItemsCount());
    }
}