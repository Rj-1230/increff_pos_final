package com.increff.pos.flow;

import com.increff.pos.model.data.*;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.DateBrandCategoryFilterForm;
import com.increff.pos.model.form.DateFilterForm;
import com.increff.pos.pojo.*;
import com.increff.pos.api.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.increff.pos.helper.flowHelper.ReportFlowHelper.*;
import static com.increff.pos.util.GetCurrentDateTime.*;

@Service
public class ReportFlow {
    private static final NumberFormat formatter = new DecimalFormat("#0.00");
    @Autowired
    DailyReportApi dailyReportApi;
    @Autowired
    OrderApi orderApi;
    @Autowired
    ProductApi productApi;
    @Autowired
    BrandApi brandApi;
    @Autowired
    InventoryApi inventoryApi;

    @Transactional(rollbackOn = ApiException.class)
    public List<ProductRevenueData> getRevenueBrandCategoryWise(DateBrandCategoryFilterForm form) throws ApiException {
        HashMap<Integer, ProductRevenueData> finalProductRevenueDataMap = new HashMap<>();
        List<OrderPojo> orderPojoList = getOrderListInDateRange(form);
        getProductRevenueDataForEveryOrderInDateRange(finalProductRevenueDataMap, orderPojoList);
        return convertMapToList(finalProductRevenueDataMap, form);
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<InventoryReportData> getInventoryBrandCategoryWise(BrandForm form) throws ApiException {
        HashMap<Integer, InventoryReportData> inventoryReportDataMap = new HashMap<>();
        List<InventoryPojo> inventoryPojoList = inventoryApi.getAll();
        getInventoryReportDataMap(inventoryReportDataMap, inventoryPojoList);
        return convertMapToList(inventoryReportDataMap, form);
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<BrandData> getBrandReport(BrandForm form) throws ApiException {
        HashMap<Integer, BrandData> brandReportDataMap = new HashMap<>();
        List<BrandPojo> brandPojoList = brandApi.getAll();
        getBrandReportDataMap(brandReportDataMap, brandPojoList);
        return convertMapToList(brandReportDataMap, form);
    }


    @Transactional(rollbackOn = ApiException.class)
    public List<DailyReportData> getDailySalesFilteredReport(DateFilterForm form) throws ApiException {
        HashMap<ZonedDateTime, DailyReportData> dailyReportDataMap = new HashMap<>();
        List<DailyReportPojo> dailyReportPojoList = getInvoicedOrdersInDateRange(form);
        getDailyReportDataMap(dailyReportDataMap, dailyReportPojoList);
        return convertMapToList(dailyReportDataMap);
    }


    @Transactional(rollbackOn = ApiException.class)
    public void createDailyReport() throws ApiException {
        List<OrderPojo> orderPojoList = getInvoicedOrdersInDateRange();
        Integer totalItems = 0;
        Double totalRevenue = 0.0;
        Integer totalOrders = orderPojoList.size();
        updateTotalCountAndRevenue(orderPojoList, totalItems, totalRevenue);
        DailyReportPojo reportPojo = convertToReportPojo(totalItems, totalRevenue, totalOrders);
        updateDailyReportPojo(reportPojo);
    }

    private void updateDailyReportPojo(DailyReportPojo reportPojo) throws ApiException {
        DailyReportPojo existingPojo = dailyReportApi.getReportByDate(getStartOfDay());
        if (existingPojo == null) {
            dailyReportApi.addReport(reportPojo);
        } else {
            dailyReportApi.update(getStartOfDay(), reportPojo);
        }
    }

    private void updateTotalCountAndRevenue(List<OrderPojo> orderPojoList, Integer totalItems, Double totalRevenue) {
        for (OrderPojo orderPojo : orderPojoList) {
            List<OrderItemPojo> orderItemPojoList = orderApi.getAllOrderItems(orderPojo.getOrderId());
            for (OrderItemPojo i : orderItemPojoList) {
                totalItems += i.getQuantity();
                totalRevenue += i.getQuantity() * i.getSellingPrice();
            }
        }
    }

    private DailyReportPojo convertToReportPojo(Integer totalItems, Double totalRevenue, Integer totalOrders) {
        DailyReportPojo reportPojo = new DailyReportPojo();
        totalRevenue = Double.parseDouble(formatter.format(totalRevenue));
        reportPojo.setInvoiceDate(getStartOfDay());
        reportPojo.setTotalRevenue(totalRevenue);
        reportPojo.setInvoicedItemsCount(totalItems);
        reportPojo.setInvoicedOrderCount(totalOrders);
        return reportPojo;
    }

    private List<OrderPojo> getInvoicedOrdersInDateRange() {
        ZonedDateTime start = getStartOfDay();
        ZonedDateTime end = getEndOfDay();
        return orderApi.selectOrderByDateFilter(start, end);
    }

    private void updateOrAddProductRevenueData(Map<Integer, ProductRevenueData> finalProductRevenueDataMap, Set<Integer> productIds, Map<Integer, ProductRevenueData> productRevenueDataMap) {
        productIds.forEach(productId -> {
            if (finalProductRevenueDataMap.containsKey(productId)) {
                finalProductRevenueDataMap.get(productId).setQuantity(finalProductRevenueDataMap.get(productId).getQuantity() + productRevenueDataMap.get(productId).getQuantity());
                finalProductRevenueDataMap.get(productId).setTotal(finalProductRevenueDataMap.get(productId).getTotal() + productRevenueDataMap.get(productId).getTotal());
            } else {
                finalProductRevenueDataMap.put(productId, productRevenueDataMap.get(productId));
            }
        });
    }

    private void getProductRevenueDataForEveryOrderInDateRange(Map<Integer, ProductRevenueData> finalProductRevenueDataMap, List<OrderPojo> orderPojoList) {
        for (OrderPojo e : orderPojoList) {
            Integer orderId = e.getOrderId();
            List<OrderItemPojo> orderItemPojoList = orderApi.getAllOrderItems(orderId);
            Set<Integer> productIds = orderItemPojoList.stream().map(OrderItemPojo::getProductId).collect(Collectors.toSet());
            Map<Integer, ProductRevenueData> productRevenueDataMap = productIds.stream().collect(Collectors.toMap(value -> value,
                    value -> convert(productApi.get(value), brandApi.getBrandPojo(productApi.get(value).getBrandId()), orderItemPojoList)));
            updateOrAddProductRevenueData(finalProductRevenueDataMap, productIds, productRevenueDataMap);
        }
    }

    private List<OrderPojo> getOrderListInDateRange(DateBrandCategoryFilterForm form) {
        ZonedDateTime startDate = convertStringToZonedDateTime(form.getStart() + " 00:00:00");
        ZonedDateTime endDate = convertStringToZonedDateTime(form.getEnd() + " 23:59:59");
        return orderApi.selectOrderByDateFilter(startDate, endDate);
    }

    private List<ProductRevenueData> convertMapToList(Map<Integer, ProductRevenueData> finalProductRevenueDataMap, DateBrandCategoryFilterForm form) {
        List<ProductRevenueData> list1 = new ArrayList<>();
        for (Map.Entry<Integer, ProductRevenueData> e : finalProductRevenueDataMap.entrySet()) {
            e.getValue().setTotal(Double.parseDouble(formatter.format(e.getValue().getTotal())));
            if (e.getValue().getQuantity() == 0)
                continue;
            if (filterByBrandCategory(e.getValue().getBrand(), e.getValue().getCategory(), form.getBrand(), form.getCategory())) {
                list1.add(e.getValue());
            }
        }
        return list1;
    }

    private void getInventoryReportDataMap(HashMap<Integer, InventoryReportData> inventoryReportDataMap, List<InventoryPojo> inventoryPojoList) throws ApiException {
        for (InventoryPojo p : inventoryPojoList) {
            ProductPojo productPojo = productApi.getCheckProduct(p.getProductId());
            BrandPojo brandPojo = brandApi.getCheckBrand(productPojo.getBrandId());
            InventoryReportData inventoryReportData = convert(p, brandPojo, productPojo);
            inventoryReportDataMap.put(p.getProductId(), inventoryReportData);
        }
    }

    private List<InventoryReportData> convertMapToList(Map<Integer, InventoryReportData> inventoryReportDataMap, BrandForm form) {
        List<InventoryReportData> list1 = new ArrayList<>();
        for (Map.Entry<Integer, InventoryReportData> e : inventoryReportDataMap.entrySet()) {
            if (filterByBrandCategory(e.getValue().getBrand(), e.getValue().getCategory(), form.getBrand(), form.getCategory()))
                list1.add(e.getValue());
        }
        return list1;
    }


    private void getBrandReportDataMap(HashMap<Integer, BrandData> brandReportDataMap, List<BrandPojo> brandPojoList) throws ApiException {
        for (BrandPojo p : brandPojoList) {
            BrandData brandData = convert(p);
            brandReportDataMap.put(p.getBrandId(), brandData);
        }
    }

    private List<BrandData> convertMapToList(HashMap<Integer, BrandData> brandReportDataMap, BrandForm form) {
        List<BrandData> list1 = new ArrayList<>();
        for (Map.Entry<Integer, BrandData> e : brandReportDataMap.entrySet()) {
            if (filterByBrandCategory(e.getValue().getBrand(), e.getValue().getCategory(), form.getBrand(), form.getCategory()))
                list1.add(e.getValue());
        }
        return list1;
    }

    private List<DailyReportPojo> getInvoicedOrdersInDateRange(DateFilterForm form) {
        ZonedDateTime startDate = convertStringToZonedDateTime(form.getStart() + " 00:00:00");
        ZonedDateTime endDate = convertStringToZonedDateTime(form.getEnd() + " 23:59:59");
        return dailyReportApi.selectReportByDateFilter(startDate, endDate);
    }

    private void getDailyReportDataMap(HashMap<ZonedDateTime, DailyReportData> dailyReportDataMap, List<DailyReportPojo> dailyReportPojoList) throws ApiException {
        for (DailyReportPojo p : dailyReportPojoList) {
            DailyReportData dailyReportData = convert(p);
            dailyReportDataMap.put(p.getInvoiceDate(), dailyReportData);
        }
    }

    private List<DailyReportData> convertMapToList(HashMap<ZonedDateTime, DailyReportData> dailyReportDataMap) {
        List<DailyReportData> list1 = new ArrayList<>();
        for (Map.Entry<ZonedDateTime, DailyReportData> e : dailyReportDataMap.entrySet()) {
            e.getValue().setTotalRevenue(Double.parseDouble(formatter.format(e.getValue().getTotalRevenue())));
            list1.add(e.getValue());
        }
        return list1;
    }

}

