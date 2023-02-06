package com.increff.pos.api;

import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.DailyReportDao;
import com.increff.pos.pojo.DailyReportPojo;
import com.increff.pos.util.PojoUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.List;

import static com.increff.pos.util.GetCurrentDateTime.getEndOfDay;
import static com.increff.pos.util.GetCurrentDateTime.getStartOfDay;
import static org.junit.Assert.assertEquals;

public class DailyReportApiTest extends AbstractUnitTest {
    @Autowired
    private DailyReportApi dailyReportApi;
    @Autowired
    private DailyReportDao dailyReportDao;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testAddDailyReport() throws ApiException {
        ZonedDateTime date= ZonedDateTime.now();
        Integer invoicedOrderCount = 2;
        Integer invoicedItemsCount=5;
        Double totalRevenue= 1425.93;
        DailyReportPojo reportPojo = PojoUtil.getDailyReportPojo(date,invoicedOrderCount,invoicedItemsCount,totalRevenue);
        dailyReportApi.addReport(reportPojo);

        DailyReportPojo dailyReportPojo =  dailyReportApi.getCheckReportByDate(date);
        assertEquals(dailyReportPojo.getInvoiceDate(),date);
        assertEquals(dailyReportPojo.getInvoicedOrderCount(),invoicedOrderCount);
        assertEquals(dailyReportPojo.getInvoicedItemsCount(),invoicedItemsCount);
        assertEquals(dailyReportPojo.getTotalRevenue(),totalRevenue);
    }

    @Test
    public void updateDailyReportPojo() throws ApiException {
        ZonedDateTime date= ZonedDateTime.now();
        Integer invoicedOrderCount = 2;
        Integer invoicedItemsCount=5;
        Double totalRevenue= 1425.93;
        DailyReportPojo reportPojo = PojoUtil.getDailyReportPojo(date,invoicedOrderCount,invoicedItemsCount,totalRevenue);
        dailyReportApi.addReport(reportPojo);

        Integer newInvoicedOrderCount = 3;
        Integer newInvoicedItemsCount=7;
        Double newTotalRevenue= 1565.84;
        DailyReportPojo updatedReportPojo = PojoUtil.getDailyReportPojo(date,newInvoicedOrderCount,newInvoicedItemsCount,newTotalRevenue);
        dailyReportApi.update(date, updatedReportPojo);

        DailyReportPojo resultReportPojo =  dailyReportApi.getCheckReportByDate(date);
        assertEquals(resultReportPojo.getInvoiceDate(),date);
        assertEquals(resultReportPojo.getTotalRevenue(),newTotalRevenue);
        assertEquals(resultReportPojo.getInvoicedOrderCount(),newInvoicedOrderCount);
        assertEquals(resultReportPojo.getInvoicedItemsCount(),newInvoicedItemsCount);
    }

    @Test
    public void testGetReportByDate() throws ApiException {
        ZonedDateTime date = getStartOfDay();
        Integer invoicedOrderCount = 2;
        Integer invoicedItemsCount=5;
        Double totalRevenue= 1425.93;
        DailyReportPojo reportPojo = PojoUtil.getDailyReportPojo(date,invoicedOrderCount,invoicedItemsCount,totalRevenue);
        dailyReportApi.addReport(reportPojo);
        DailyReportPojo reportPojo1 = dailyReportApi.getCheckReportByDate(date);
        DailyReportPojo reportPojo2 = dailyReportApi.getReportByDate(date);
        assertEquals(reportPojo1,reportPojo);
        assertEquals(reportPojo2,reportPojo);
    }



    @Test
    public void testSelectReportInDateRange() throws ApiException {
        ZonedDateTime date= ZonedDateTime.now();
        Integer invoicedOrderCount = 2;
        Integer invoicedItemsCount=5;
        Double totalRevenue= 1425.93;
        DailyReportPojo reportPojo = PojoUtil.getDailyReportPojo(date,invoicedOrderCount,invoicedItemsCount,totalRevenue);
        dailyReportApi.addReport(reportPojo);

        List<DailyReportPojo> reportPojoList = dailyReportApi.selectReportByDateFilter(date.minusDays(1),date.plusDays(1));
        assertEquals(reportPojoList.size(),1);
        assertEquals(reportPojoList.get(0),reportPojo);

        reportPojoList = dailyReportApi.selectReportByDateFilter(date.plusDays(2),date.plusDays(4));
        assertEquals(reportPojoList.size(),0);
    }


    @Test
    public void testReportExistenceByDate() throws ApiException {
        ZonedDateTime date= getStartOfDay();
        Integer invoicedOrderCount = 2;
        Integer invoicedItemsCount=5;
        Double totalRevenue= 1425.93;
        DailyReportPojo reportPojo = PojoUtil.getDailyReportPojo(date,invoicedOrderCount,invoicedItemsCount,totalRevenue);
        dailyReportApi.addReport(reportPojo);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("No such daily report for given date exists !");
        dailyReportApi.getCheckReportByDate(date.minusDays(2));

    }

    @Test
    public void testUpdateReportExistence() throws ApiException {
        ZonedDateTime date= getStartOfDay();
        Integer invoicedOrderCount = 2;
        Integer invoicedItemsCount=5;
        Double totalRevenue= 1425.93;
        DailyReportPojo reportPojo = PojoUtil.getDailyReportPojo(date,invoicedOrderCount,invoicedItemsCount,totalRevenue);
        dailyReportApi.addReport(reportPojo);

        Integer newInvoicedOrderCount = 3;
        Integer newInvoicedItemsCount=7;
        Double newTotalRevenue= 1545.93;
        DailyReportPojo updatedDailyReportPojo = PojoUtil.getDailyReportPojo(getEndOfDay(),newInvoicedOrderCount,newInvoicedItemsCount,newTotalRevenue);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("No such daily report for given date exists !");
        dailyReportApi.update(date.minusDays(2),updatedDailyReportPojo);
    }

}
