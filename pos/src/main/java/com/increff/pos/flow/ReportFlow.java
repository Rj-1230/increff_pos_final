package com.increff.pos.flow;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.increff.pos.helper.ReportFlowHelper.*;
import static com.increff.pos.util.GetCurrentDateTime.getLocalDate;
//import static com.increff.pos.helper.ReportFlowHelper.filterByBrandCategory;
@Service
public class ReportFlow {

    private static final NumberFormat formatter = new DecimalFormat("#0.00");
    @Autowired
    ReportService reportService;
    @Autowired
    OrderService orderService;
    @Autowired
    ProductService productService;
    @Autowired
    BrandService brandService;
    @Autowired
    InventoryService inventoryService;

    public List<ProductRevenueData> getRevenueBrandCategoryWise(DateBrandCategoryFilterForm form) throws ApiException {
        List<ProductRevenueData> list1 = new ArrayList<ProductRevenueData>();
//        HashMap<Integer, ProductRevenueData> map = new HashMap<>();
//        String startDate = form.getStart() + " 00:00:00";
//        String endDate = form.getEnd() + " 23:59:59";
//
//        List<OrderPojo> orderPojoList = orderService.selectOrderByDateFilter(startDate, endDate);
//        for (OrderPojo e : orderPojoList) {
//            int orderId = e.getOrderId();
//            List<OrderItemPojo> orderItemPojoList = orderService.getAllOrderItems(orderId);
//            Set<Integer> productIds = orderItemPojoList.stream().map(OrderItemPojo::getProductId).collect(Collectors.toSet());
//
//                ProductPojo productPojo = productService.getCheck(p.getProductId());
//                BrandPojo brandPojo = brandService.getCheckBrand(productPojo.getBrandId());
//                ProductRevenueData productRevenueData = convert(productPojo, brandPojo);
//                map.put(p.getProductId(), productRevenueData);
//
//                int productId = p.getProductId();
//                int quantity = map.get(productId).getQuantity();
//                double total = map.get(productId).getTotal();
//                map.get(productId).setQuantity(quantity + p.getQuantity());
//                map.get(productId).setTotal(total + p.getQuantity() * p.getSellingPrice());
//
//            }
//        }
//
//
////        Converting map to list
//        for (Map.Entry<Integer, ProductRevenueData> e : map.entrySet()) {
//            e.getValue().setTotal(Double.parseDouble(formatter.format(e.getValue().getTotal())));
//            if (e.getValue().getQuantity() == 0)
//                continue;
//            if (filterByBrandCategory(e.getValue().getBrand(), e.getValue().getCategory(), form.getBrand(), form.getCategory()))
//                list1.add(e.getValue());
//        }
//        return list1;
//    }
//
//
//    public List<InventoryReportData> getInventoryBrandCategoryWise(BrandForm form) throws ApiException {
//        List<InventoryReportData> list1 = new ArrayList<InventoryReportData>();
//
//        List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
//        HashMap<Integer, InventoryReportData> map = new HashMap<>();
//
////        getting the list of all available products in map
//        for(InventoryPojo p: inventoryPojoList) {
//            ProductPojo productPojo = productService.getCheck(p.getProductId());
//            BrandPojo brandPojo = brandService.getCheckBrand(productPojo.getBrandId());
//            InventoryReportData inventoryReportData = convert(p,brandPojo);
//            map.put(p.getProductId(), inventoryReportData);
//        }
////        Converting map to list
//        for (Map.Entry<Integer, InventoryReportData> e: map.entrySet())
//        {
//            if(filterByBrandCategory(e.getValue().getBrand(),e.getValue().getCategory(),form.getBrand(),form.getCategory()))
//                list1.add(e.getValue());
//        }

        return list1;
    }


    public List<BrandData> getBrandReport(BrandForm form) throws ApiException {
        List<BrandData> list1 = new ArrayList<BrandData>();

        List<BrandPojo> brandPojoList = brandService.getAll();
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
        HashMap<LocalDate, DailyReportData> map = new HashMap<>();
        LocalDate startDate = LocalDate.parse(form.getStart());
        LocalDate endDate = LocalDate.parse(form.getEnd());
        List<DailyReportPojo> dailyReportPojoList = reportService.selectReportByDateFilter(startDate,endDate);
        for(DailyReportPojo p: dailyReportPojoList) {
            DailyReportData dailyReportData = convert(p);
            map.put(p.getDate(),dailyReportData);
        }

        for (Map.Entry<LocalDate, DailyReportData> e: map.entrySet())
        {
            e.getValue().setTotalRevenue(Double.parseDouble(formatter.format(e.getValue().getTotalRevenue())));
            list1.add(e.getValue());

        }

        return list1;
    }




    public void createDailyReport() throws ApiException {
        DailyReportPojo reportPojo = new DailyReportPojo();
        LocalDate date = getLocalDate();

        Integer totalItems = 0;
        Double totalRevenue = 0.0;
        String startDate = date + " 00:00:00";
        String endDate = date + " 23:59:59";
        List<OrderPojo> orderPojoList = orderService.selectOrderByDateFilter(startDate, endDate);
        Integer totalOrders = orderPojoList.size();
        for (OrderPojo o : orderPojoList) {
            Integer id = o.getOrderId();
            List<OrderItemPojo> orderItemPojoList = orderService.getAllOrderItems(id);
            for (OrderItemPojo i : orderItemPojoList) {
                totalItems += i.getQuantity();
                totalRevenue += i.getQuantity() * i.getSellingPrice();
            }
        }
        totalRevenue = Double.parseDouble(formatter.format(totalRevenue));

        reportPojo.setDate(date);
        reportPojo.setTotalRevenue(totalRevenue);
        reportPojo.setInvoicedItemsCount(totalItems);
        reportPojo.setInvoicedOrderCount(totalOrders);

        DailyReportPojo pojo = reportService.getReportByDate(date);
        if (pojo == null) {
            reportService.addReport(reportPojo);
        } else {
            reportService.update(date, reportPojo);
        }
    }

}
