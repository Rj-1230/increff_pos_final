package com.increff.pos.service;

import com.increff.pos.dao.ReportDao;
import com.increff.pos.pojo.DailyReportPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class ReportService {
    @Autowired
    private ReportDao dao;

    @Transactional
    public void addReport(DailyReportPojo pojo) throws ApiException {
        try {
            dao.insert(pojo);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    @Transactional
    public DailyReportPojo getReportByDate(LocalDate date) throws ApiException {
        return dao.select(date);
    }

    @Transactional
    public List<DailyReportPojo> selectReportByDateFilter(LocalDate start, LocalDate endDate) throws ApiException {
        List<DailyReportPojo> dailyReportPojoList = dao.selectReportByDateFilter(start, endDate);
        if(Objects.isNull(dailyReportPojoList)){
            throw new ApiException("No orders exists between the given dates");
        }
        return dailyReportPojoList;
    }

    @Transactional
    public void update(LocalDate date, DailyReportPojo newPojo)
    {
        DailyReportPojo pojo = dao.select(date);
        pojo.setInvoicedOrderCount(newPojo.getInvoicedOrderCount());
        pojo.setTotalRevenue(newPojo.getTotalRevenue());
        pojo.setInvoicedItemsCount(newPojo.getInvoicedItemsCount());
    }
}