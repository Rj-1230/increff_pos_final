package com.increff.pos.flow;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.increff.pos.helper.ReportFlowHelper.*;
import static com.increff.pos.util.GetCurrentDateTime.*;

//import static com.increff.pos.helper.ReportFlowHelper.filterByBrandCategory;
@Service
public class ReportFlow {

    private static final NumberFormat formatter = new DecimalFormat("#0.00");
    @Autowired
    ReportApi reportApi;
    @Autowired
    OrderApi orderApi;
    @Autowired
    ProductApi productApi;
    @Autowired
    BrandApi brandApi;
    @Autowired
    InventoryApi inventoryApi;

    public List<ProductRevenueData> getRevenueBrandCategoryWise(DateBrandCategoryFilterForm form) throws ApiException {
        List<ProductRevenueData> list1 = new ArrayList<ProductRevenueData>();
        HashMap<Integer, ProductRevenueData> map = new HashMap<>();
        ZonedDateTime startDate = convertStringToZonedDateTime(form.getStart() + " 00:00:00");
        ZonedDateTime endDate = convertStringToZonedDateTime(form.getEnd() + " 23:59:59");

        List<OrderPojo> orderPojoList = orderApi.selectOrderByDateFilter(startDate, endDate);
        for (OrderPojo e : orderPojoList) {
            Integer orderId = e.getOrderId();
            List<OrderItemPojo> orderItemPojoList = orderApi.getAllOrderItems(orderId);
            Set<Integer> productIds = orderItemPojoList.stream().map(OrderItemPojo::getProductId).collect(Collectors.toSet());
            Map<Integer,ProductPojo> productIdPojoMap = productIds.stream().collect(Collectors.toMap(value-> value,
                    value -> productApi.get(value)));
            Map<Integer,BrandPojo> productIdBrandPojoMap = productIds.stream().collect(Collectors.toMap(value-> value,
                    value -> brandApi.getBrandPojo(productApi.get(value).getBrandId())));
//            Set<ProductPojo> productPojos = productIds.stream().map(x->productService.get(x)).collect(Collectors.toSet());
//            Set<BrandPojo> brandPojos = productPojos.stream().map(x->brandService.getBrand(x.getBrandId())).collect(Collectors.toSet());

//            Map<Integer,ProductRevenueData> productRevenueDataMap = productIds.stream().collect(Collectors.toMap(value-> value,
//                    value -> convert(productIdPojoMap.get(value),productIdBrandPojoMap.get(value))));

            Map<Integer,ProductRevenueData> productRevenueDataMap = productIds.stream().collect(Collectors.toMap(value-> value,
                    value -> convert(productApi.get(value), brandApi.getBrandPojo(productApi.get(value).getBrandId()))));


            for(Map.Entry<Integer,ProductRevenueData> entry : productRevenueDataMap.entrySet()){
                System.out.println(entry.getKey() + " --------------"+ entry.getValue().getBrand() +entry.getValue().getCategory());
            }

//            Set<ProductRevenueData> productRevenueDataSet = productIds.stream().filter(productId -> Objects.equals(productId,productPojos.))
//            ProductRevenueData productRevenueData = convert(productPojo, brandPojo);
//                map.put(p.getProductId(), productRevenueData);
//
//                Integer productId = p.getProductId();
//                Integer quantity = map.get(productId).getQuantity();
//                double total = map.get(productId).getTotal();
//                map.get(productId).setQuantity(quantity + p.getQuantity());
//                map.get(productId).setTotal(total + p.getQuantity() * p.getSellingPrice());
        }


//        Converting map to list
        for (Map.Entry<Integer, ProductRevenueData> e : map.entrySet()) {
            e.getValue().setTotal(Double.parseDouble(formatter.format(e.getValue().getTotal())));
            if (e.getValue().getQuantity() == 0)
                continue;
            if (filterByBrandCategory(e.getValue().getBrand(), e.getValue().getCategory(), form.getBrand(), form.getCategory()))
                list1.add(e.getValue());
        }
        return list1;
    }


    public List<InventoryReportData> getInventoryBrandCategoryWise(BrandForm form) throws ApiException {
        List<InventoryReportData> list1 = new ArrayList<InventoryReportData>();

        List<InventoryPojo> inventoryPojoList = inventoryApi.getAll();
        HashMap<Integer, InventoryReportData> map = new HashMap<>();

//        getting the list of all available products in map
        for(InventoryPojo p: inventoryPojoList) {
            ProductPojo productPojo = productApi.getCheck(p.getProductId());
            BrandPojo brandPojo = brandApi.getCheckBrand(productPojo.getBrandId());
            InventoryReportData inventoryReportData = convert(p,brandPojo);
            map.put(p.getProductId(), inventoryReportData);
        }
//        Converting map to list
        for (Map.Entry<Integer, InventoryReportData> e: map.entrySet())
        {
            if(filterByBrandCategory(e.getValue().getBrand(),e.getValue().getCategory(),form.getBrand(),form.getCategory()))
                list1.add(e.getValue());
        }

        return list1;
    }


    public List<BrandData> getBrandReport(BrandForm form) throws ApiException {
        List<BrandData> list1 = new ArrayList<BrandData>();

        List<BrandPojo> brandPojoList = brandApi.getAll();
        HashMap<Integer, BrandData> map = new HashMap<>();
        for(BrandPojo p: brandPojoList) {
            BrandData brandData = convert(p);
            map.put(p.getId(), brandData);
        }
        for (Map.Entry<Integer,BrandData> e: map.entrySet())
        {
            if(filterByBrandCategory(e.getValue().getBrand(),e.getValue().getCategory(),form.getBrand(),form.getCategory()))
                list1.add(e.getValue());
        }
        return list1;
    }



    public List<DailyReportData> getDailySalesFilteredReport(DateFilterForm form) throws ApiException
    {
        List<DailyReportData> list1 = new ArrayList<DailyReportData>();
        HashMap<ZonedDateTime, DailyReportData> map = new HashMap<>();

        ZonedDateTime startDate = convertStringToZonedDateTime(form.getStart()+" 00:00:00");
        ZonedDateTime endDate = convertStringToZonedDateTime(form.getEnd()+" 23:59:59");

        List<DailyReportPojo> dailyReportPojoList = reportApi.selectReportByDateFilter(startDate,endDate);
        for(DailyReportPojo p: dailyReportPojoList) {
            DailyReportData dailyReportData = convert(p);
            map.put(p.getInvoiceDate(),dailyReportData);
        }

        for (Map.Entry<ZonedDateTime, DailyReportData> e: map.entrySet())
        {
            e.getValue().setTotalRevenue(Double.parseDouble(formatter.format(e.getValue().getTotalRevenue())));
            list1.add(e.getValue());

        }

        return list1;
    }


    public void createDailyReport() throws ApiException {
        DailyReportPojo reportPojo = new DailyReportPojo();
//        ZonedDateTime start = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime start = getStartOfDay();
        ZonedDateTime end = getEndOfDay();
//        ZonedDateTime end = ZonedDateTime.now().withHour(23).withMinute(59).withSecond(59);
        Integer totalItems = 0;
        Double totalRevenue = 0.0;
        List<OrderPojo> orderPojoList = orderApi.selectOrderByDateFilter(start, end);

        Integer totalOrders = orderPojoList.size();
        for (OrderPojo o : orderPojoList) {
            Integer id = o.getOrderId();
            List<OrderItemPojo> orderItemPojoList = orderApi.getAllOrderItems(id);
            for (OrderItemPojo i : orderItemPojoList) {
                totalItems += i.getQuantity();
                totalRevenue += i.getQuantity() * i.getSellingPrice();
            }
        }
        totalRevenue = Double.parseDouble(formatter.format(totalRevenue));

        reportPojo.setInvoiceDate(start);
        reportPojo.setTotalRevenue(totalRevenue);
        reportPojo.setInvoicedItemsCount(totalItems);
        reportPojo.setInvoicedOrderCount(totalOrders);
        DailyReportPojo pojo = reportApi.getReportByDate(start);
        if (pojo == null) {
            reportApi.addReport(reportPojo);
        } else {
            reportApi.update(start, reportPojo);
        }
    }

}
