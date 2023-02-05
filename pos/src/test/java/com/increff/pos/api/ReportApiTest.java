//package com.increff.pos.api;
//
//import com.increff.pos.config.AbstractUnitTest;
//import com.increff.pos.dao.ReportDao;
//import com.increff.pos.pojo.DailyReportPojo;
//import com.increff.pos.util.PojoUtil;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.time.ZonedDateTime;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
//public class ReportApiTest extends AbstractUnitTest {
//    @Autowired
//    private ReportApi reportApi;
//    @Autowired
//    private ReportDao reportDao;
//    @Rule
//    public ExpectedException exceptionRule = ExpectedException.none();
//
//    @Test
//    public void testAddDailyReport() throws ApiException {
//        ZonedDateTime date= ZonedDateTime.now();
//        Integer invoicedOrderCount = 2;
//        Integer invoicedItemsCount=5;
//        Double totalRevenue= 1425.93;
//        DailyReportPojo reportPojo = PojoUtil.getDailyReportPojo(date,invoicedOrderCount,invoicedItemsCount,totalRevenue);
//        reportApi.addReport(reportPojo);
//
//        DailyReportPojo dailyReportPojo =  reportApi.getCheckReportByDate(date);
//        assertEquals(dailyReportPojo.getInvoiceDate(),date);
//        assertEquals(dailyReportPojo.getInvoicedOrderCount(),invoicedOrderCount);
//        assertEquals(dailyReportPojo.getInvoicedItemsCount(),invoicedItemsCount);
//        assertEquals(dailyReportPojo.getTotalRevenue(),totalRevenue);
//    }
//
//    @Test
//    public void updateDailyReportPojo() throws ApiException {
//        ZonedDateTime date= ZonedDateTime.now();
//        Integer invoicedOrderCount = 2;
//        Integer invoicedItemsCount=5;
//        Double totalRevenue= 1425.93;
//        DailyReportPojo reportPojo = PojoUtil.getDailyReportPojo(date,invoicedOrderCount,invoicedItemsCount,totalRevenue);
//        reportDao.insert(reportPojo);
//
//        Integer newInvoicedOrderCount = 3;
//        Integer newInvoicedItemsCount=7;
//        Double newTotalRevenue= 1565.84;
//        DailyReportPojo updatedReportPojo = PojoUtil.getDailyReportPojo(date,newInvoicedOrderCount,newInvoicedItemsCount,newTotalRevenue);
//        reportApi.update(date, updatedReportPojo);
//
//        DailyReportPojo resultReportPojo =  reportApi.getCheckReportByDate(date);
//        assertEquals(resultReportPojo.getInvoiceDate(),date);
//        assertEquals(resultReportPojo.getTotalRevenue(),newTotalRevenue);
//        assertEquals(resultReportPojo.getInvoicedOrderCount(),newInvoicedOrderCount);
//        assertEquals(resultReportPojo.getInvoicedItemsCount(),newInvoicedItemsCount);
//    }
//
//    @Test
//    public void testGetReportByDate() throws ApiException {
//        ZonedDateTime date= ZonedDateTime.now();
//        Integer invoicedOrderCount = 2;
//        Integer invoicedItemsCount=5;
//        Double totalRevenue= 1425.93;
//        DailyReportPojo reportPojo = PojoUtil.getDailyReportPojo(date,invoicedOrderCount,invoicedItemsCount,totalRevenue);
//        reportDao.insert(reportPojo);
//
//        DailyReportPojo reportPojo1 = reportApi.getCheckReportByDate(date);
//        assertEquals(reportPojo1,reportPojo);
//    }
//
//
//
//    @Test
//    public void testSelectReportInDateRange() throws ApiException {
//        ZonedDateTime date= ZonedDateTime.now();
//        Integer invoicedOrderCount = 2;
//        Integer invoicedItemsCount=5;
//        Double totalRevenue= 1425.93;
//        DailyReportPojo reportPojo = PojoUtil.getDailyReportPojo(date,invoicedOrderCount,invoicedItemsCount,totalRevenue);
//        reportDao.insert(reportPojo);
//
//        List<DailyReportPojo> reportPojoList = reportApi.selectReportByDateFilter(date.minusDays(1),date.plusDays(1));
//        assertEquals(reportPojoList.size(),1);
//        assertEquals(reportPojoList.get(0),reportPojo);
//
//        reportPojoList = reportApi.selectReportByDateFilter(date.plusDays(2),date.plusDays(4));
//        assertEquals(reportPojoList.size(),0);
//    }
//
//
//    @Test
//    public void testReportExistenceByDate() throws ApiException {
//        ZonedDateTime date= ZonedDateTime.now();
//        Integer invoicedOrderCount = 2;
//        Integer invoicedItemsCount=5;
//        Double totalRevenue= 1425.93;
//        DailyReportPojo reportPojo = PojoUtil.getDailyReportPojo(date,invoicedOrderCount,invoicedItemsCount,totalRevenue);
//        reportDao.insert(reportPojo);
//
//        exceptionRule.expect(ApiException.class);
//        exceptionRule.expectMessage("No such dailyReport with given date exists !");
//        reportApi.getCheckReportByDate(date.minusDays(2));
//
//    }
//
//    @Test
//    public void testUpdateReportExistence() throws ApiException {
//        ZonedDateTime date= ZonedDateTime.now();
//        Integer invoicedOrderCount = 2;
//        Integer invoicedItemsCount=5;
//        Double totalRevenue= 1425.93;
//        DailyReportPojo reportPojo = PojoUtil.getDailyReportPojo(date,invoicedOrderCount,invoicedItemsCount,totalRevenue);
//        reportDao.insert(reportPojo);
//
//        Integer newInvoicedOrderCount = 3;
//        Integer newInvoicedItemsCount=7;
//        Double newTotalRevenue= 1545.93;
//        DailyReportPojo updatedDailyReportPojo = PojoUtil.getDailyReportPojo(date,newInvoicedOrderCount,newInvoicedItemsCount,newTotalRevenue);
//        exceptionRule.expect(ApiException.class);
//        exceptionRule.expectMessage("No such dailyReport with given date exists !");
//        reportApi.update(date.minusDays(2),updatedDailyReportPojo);
//
//    }
//
//}
