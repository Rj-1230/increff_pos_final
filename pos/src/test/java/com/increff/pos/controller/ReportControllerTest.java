package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.InvoiceClientApi;
import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.*;
import com.increff.pos.dto.OrderDto;
import com.increff.pos.dto.ReportDto;
import com.increff.pos.flow.OrderFlow;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.DailyReportData;
import com.increff.pos.model.data.InventoryReportData;
import com.increff.pos.model.data.ProductRevenueData;
import com.increff.pos.pojo.*;
import com.increff.pos.util.FormUtil;
import com.increff.pos.util.PojoUtil;
import com.increff.pos.util.UserPrincipal;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.ZonedDateTime;
import java.util.List;

import static com.increff.pos.util.DummyForm.*;
import static com.increff.pos.util.DummyUtil.putDummyOrderInvoiceTime;
import static org.junit.Assert.assertEquals;

public class ReportControllerTest extends AbstractUnitTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Autowired
    private ReportController reportController;
    @Autowired
    private OrderController orderController;
    @Autowired
    private OrderDto orderDto;
    @Autowired
    private OrderFlow orderFlow;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private CartItemDao cartItemDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private DailyReportDao dailyReportDao;
    @Autowired
    private ReportDto dailyReportDto;

    @Before
    public void setup() throws Exception {
        //Authentication mocking
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setRole("supervisor");
        userPrincipal.setId(1);
        userPrincipal.setEmail("supervisor@increff.com");
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userPrincipal);
        SecurityContextHolder.setContext(securityContext);

        //Adding brand
        String brand = "brand";
        String category = "category";
        BrandPojo brandPojo = PojoUtil.getBrandPojo(brand, category);
        brandDao.insert(brandPojo);

        //Adding a product
        Integer brandId = brandDao.selectAll().get(0).getBrandId();
        String barcode = "barcode";
        String productName = "prod1";
        Double mrp = 1240.54;
        ProductPojo productPojo = PojoUtil.getProductPojo(brandId, barcode, mrp, productName);
        productDao.insert(productPojo);

        ProductPojo productPojo1 = PojoUtil.getProductPojo(brandId, "barcode1", 1200.65, "prod2");
        productDao.insert(productPojo1);

        //Adding inventory corresponding to above product since it was added in flow layer
        Integer productId = productDao.selectAll().get(0).getProductId();
        Integer productId1 = productDao.selectAll().get(1).getProductId();
        Integer quantity = 100;
        InventoryPojo inventoryPojo = PojoUtil.getInventoryPojo(productId, quantity);
        InventoryPojo inventoryPojo1 = PojoUtil.getInventoryPojo(productId1, quantity);
        inventoryDao.insert(inventoryPojo);
        inventoryDao.insert(inventoryPojo1);

        //Add some items in cart
        Integer cartQuantity = 5;
        Integer counterId = 1;
        Double sellingPrice = 1000.95;
        CartItemPojo cartItemPojo = PojoUtil.getCartPojo(productId, counterId, sellingPrice, cartQuantity);
        CartItemPojo cartItemPojo1 = PojoUtil.getCartPojo(productId1, counterId, sellingPrice, cartQuantity);
        cartItemDao.insert(cartItemPojo);
        cartItemDao.insert(cartItemPojo1);

        InvoiceClientApi invoiceClientApi = Mockito.mock(InvoiceClientApi.class);
        Mockito.when(invoiceClientApi.invoiceOrder(Mockito.any(), Mockito.anyList())).thenReturn(new byte[12]);
        orderFlow.setInvoiceClientApi(invoiceClientApi);
        orderDto.setOrderFlow(orderFlow);
        orderController.setOrderDto(orderDto);

//       Create and invoice order
        orderController.pushToNewOrder(getDummyCustomerDetailsForm());
        orderController.markOrderInvoiced(orderDao.selectAllOrders().get(0).getOrderId());
        OrderPojo orderPojo = orderDao.selectAllOrders().get(0);
        orderPojo.setOrderInvoiceTime(putDummyOrderInvoiceTime());

//        Manually create a daily report
        DailyReportPojo dailyReportPojo = new DailyReportPojo();
        dailyReportPojo.setInvoiceDate(putDummyOrderInvoiceTime());
        dailyReportPojo.setInvoicedItemsCount(3);
        dailyReportPojo.setInvoicedOrderCount(1);
        dailyReportPojo.setTotalRevenue(2067.88);
        dailyReportDao.insert(dailyReportPojo);

    }


    @Test
    public void testBrandReport() throws ApiException {
        List<BrandData> brandDataList = reportController.getBrandReport(getDummyBrandForm());
        assertEquals(1, brandDataList.size());
        BrandData brandData = brandDataList.get(0);
        assertEquals("brand", brandData.getBrand());
        assertEquals("category", brandData.getCategory());
    }

    @Test
    public void testBrandReportFilter() throws ApiException {
        List<BrandData> brandDataList = reportController.getBrandReport(FormUtil.getBrandForm("abcd", "category"));
        assertEquals(0, brandDataList.size());
        brandDataList = reportController.getBrandReport(FormUtil.getBrandForm("abcd", "abcd"));
        assertEquals(0, brandDataList.size());
        brandDataList = reportController.getBrandReport(FormUtil.getBrandForm("brand", "abcd"));
        assertEquals(0, brandDataList.size());
        brandDataList = reportController.getBrandReport(FormUtil.getBrandForm("brand", "all"));
        assertEquals(1, brandDataList.size());
        brandDataList = reportController.getBrandReport(FormUtil.getBrandForm("all", "category"));
        assertEquals(1, brandDataList.size());
        brandDataList = reportController.getBrandReport(FormUtil.getBrandForm("all", "all"));
        assertEquals(1, brandDataList.size());
    }

    @Test
    public void testGetInventoryReport() throws ApiException {
        List<InventoryReportData> inventoryReportDataList = reportController.getInventoryReport(getDummyBrandForm());
        assertEquals(2, inventoryReportDataList.size());
        InventoryReportData inventoryReportData = inventoryReportDataList.get(0);
        assertEquals("brand", inventoryReportData.getBrand());
        assertEquals("category", inventoryReportData.getCategory());
        assertEquals("prod1", inventoryReportData.getProductName());
        assertEquals(new Integer(95), inventoryReportData.getQuantity());
    }

    @Test
    public void testInventoryReportFilter() throws ApiException {
        List<InventoryReportData> inventoryReportDataList = reportController.getInventoryReport(FormUtil.getBrandForm("abcd", "category"));
        assertEquals(0, inventoryReportDataList.size());
        inventoryReportDataList = reportController.getInventoryReport(FormUtil.getBrandForm("abcd", "abcd"));
        assertEquals(0, inventoryReportDataList.size());
        inventoryReportDataList = reportController.getInventoryReport(FormUtil.getBrandForm("brand", "abcd"));
        assertEquals(0, inventoryReportDataList.size());
        inventoryReportDataList = reportController.getInventoryReport(FormUtil.getBrandForm("brand", "all"));
        assertEquals(2, inventoryReportDataList.size());
        inventoryReportDataList = reportController.getInventoryReport(FormUtil.getBrandForm("all", "category"));
        assertEquals(2, inventoryReportDataList.size());
        inventoryReportDataList = reportController.getInventoryReport(FormUtil.getBrandForm("all", "all"));
        assertEquals(2, inventoryReportDataList.size());
    }

    @Test
    public void testGetRevenueReport() throws ApiException {
        List<ProductRevenueData> productRevenueDataList = reportController.getRevenueReport(getDummyDateBrandCategoryForm());
        assertEquals(2, productRevenueDataList.size());
        ProductRevenueData productRevenueData = productRevenueDataList.get(0);
        assertEquals("brand", productRevenueData.getBrand());
        assertEquals("category", productRevenueData.getCategory());
        assertEquals("prod1", productRevenueData.getProductName());
        assertEquals(new Integer(5), productRevenueData.getQuantity());
        assertEquals(new Double(5004.75), productRevenueData.getTotal());

    }

    @Test
    public void testRevenueReportBrandFilter() throws ApiException {
        List<ProductRevenueData> productRevenueDataList = reportController.getRevenueReport(FormUtil.getDateBrandCategoryFilterForm("2023-01-01", "2023-01-30", "abcd", "category"));
        assertEquals(0, productRevenueDataList.size());
        productRevenueDataList = reportController.getRevenueReport(FormUtil.getDateBrandCategoryFilterForm("2023-01-01", "2023-01-30", "abcd", "abcd"));
        assertEquals(0, productRevenueDataList.size());
        productRevenueDataList = reportController.getRevenueReport(FormUtil.getDateBrandCategoryFilterForm("2023-01-01", "2023-01-30", "brand", "abcd"));
        assertEquals(0, productRevenueDataList.size());
        productRevenueDataList = reportController.getRevenueReport(FormUtil.getDateBrandCategoryFilterForm("2023-01-01", "2023-01-30", "brand", "all"));
        assertEquals(2, productRevenueDataList.size());
        productRevenueDataList = reportController.getRevenueReport(FormUtil.getDateBrandCategoryFilterForm("2023-01-01", "2023-01-30", "all", "category"));
        assertEquals(2, productRevenueDataList.size());
        productRevenueDataList = reportController.getRevenueReport(FormUtil.getDateBrandCategoryFilterForm("2023-01-01", "2023-01-30", "all", "all"));
        assertEquals(2, productRevenueDataList.size());
    }

    @Test
    public void testRevenueReportDateFilter() throws ApiException {
        List<ProductRevenueData> productRevenueDataList = reportController.getRevenueReport(FormUtil.getDateBrandCategoryFilterForm("2023-01-06", "2023-01-30", "all", "all"));
        assertEquals(0, productRevenueDataList.size());

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The end date must be ahead of start date");
        reportController.getRevenueReport(FormUtil.getDateBrandCategoryFilterForm("2023-01-06", "2023-01-01", "all", "all"));
    }

    @Test
    public void testRevenueReportDateDurationFilter() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The end date must be only a month ahead of start date");
        reportController.getRevenueReport(FormUtil.getDateBrandCategoryFilterForm("2023-01-06", "2023-03-01", "all", "all"));
    }

    @Test
    public void testGetDailyReport() throws ApiException {
        List<DailyReportData> dailyReportDataList = reportController.getDailySalesReport(getDummyDateForm());
        assertEquals(1, dailyReportDataList.size());
        DailyReportData dailyReportData = dailyReportDataList.get(0);
        assertEquals(new Integer(1), dailyReportData.getInvoicedOrderCount());
        assertEquals(new Integer(3), dailyReportData.getInvoicedItemsCount());
        assertEquals(new Double(2067.88), dailyReportData.getTotalRevenue());
    }

    @Test
    public void testDailyReportDateDurationFilter() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The end date must be only a month ahead of start date");
        reportController.getDailySalesReport(FormUtil.getDateFilterForm("2023-01-06", "2023-03-01"));
    }

    @Test
    public void testDailyReportDateFilter() throws ApiException {
        List<DailyReportData> dailyReportDataList = reportController.getDailySalesReport(FormUtil.getDateFilterForm("2023-01-06", "2023-01-30"));
        assertEquals(0, dailyReportDataList.size());

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The end date must be ahead of start date");
        reportController.getDailySalesReport(FormUtil.getDateFilterForm("2023-01-06", "2023-01-01"));
    }


    @Test
    public void testCreateDailyReport() throws ApiException {
        orderDao.selectAllOrders().get(0).setOrderInvoiceTime(ZonedDateTime.now());
       dailyReportDto.createDailyReport();

       assertEquals(dailyReportDao.selectAll().size(),2);
       DailyReportPojo dailyReportPojo = dailyReportDao.selectAll().get(0);
       assertEquals(new Integer(3),dailyReportPojo.getInvoicedItemsCount());
       assertEquals(new Integer(1),dailyReportPojo.getInvoicedOrderCount());
       assertEquals(new Double(2067.88),dailyReportPojo.getTotalRevenue());
    }

    @Test
    public void testDateNonNullable() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The start date or end date field can't be null");
        reportController.getRevenueReport(FormUtil.getDateBrandCategoryFilterForm(null,"2023-01-01","brand","category"));
    }
    @Test
    public void testStartDateNonEmpty() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Start date can't be empty");
        reportController.getRevenueReport(FormUtil.getDateBrandCategoryFilterForm("    ","2023-01-01","brand","category"));
    }
    @Test
    public void testEndDateNonEmpty() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("End date can't be empty");
        reportController.getRevenueReport(FormUtil.getDateBrandCategoryFilterForm("2023-01-01","    ","brand","category"));
    }

    @Test
    public void testBrandNonEmpty() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The brand can't be empty");
        reportController.getRevenueReport(FormUtil.getDateBrandCategoryFilterForm("2023-01-01","2023-01-05","    ","category"));
    }
    @Test
    public void testCategoryNonEmpty() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The category can't be empty");
        reportController.getRevenueReport(FormUtil.getDateBrandCategoryFilterForm("2023-01-01","2023-01-05","brand","   "));
    }


}
