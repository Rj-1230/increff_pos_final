package com.increff.pos.helper.flowHelper;

import com.increff.pos.model.data.*;
import com.increff.pos.pojo.*;
import com.increff.pos.api.ApiException;

import java.util.List;
import java.util.Objects;

public class ReportFlowHelper {

    public static boolean filterByBrandCategory (String dataBrand,String dataCategory, String formBrand, String formCategory){
        return (((Objects.equals(dataBrand,formBrand)) && (Objects.equals(dataCategory,formCategory))) ||
                ((Objects.equals(formBrand,"All")) && (Objects.equals(dataCategory,formCategory))) ||
                ((Objects.equals(dataBrand,formBrand)) && (Objects.equals(formCategory,"All"))) ||
                ((Objects.equals(formBrand,"All")) && (Objects.equals(formCategory,"All"))));

    }

    public static ProductRevenueData convert(ProductPojo productPojo, BrandPojo brandPojo, List<OrderItemPojo>orderItemPojoList)
    {
        ProductRevenueData productRevenueData = new ProductRevenueData();
        productRevenueData.setProductId(productPojo.getProductId());
        productRevenueData.setProductName(productPojo.getName());
        productRevenueData.setBrand(brandPojo.getBrand());
        productRevenueData.setCategory(brandPojo.getCategory());
        orderItemPojoList.forEach(orderItemPojo ->
        {
            if(Objects.equals(productRevenueData.getProductId(),orderItemPojo.getProductId())){
                productRevenueData.setQuantity(orderItemPojo.getQuantity());
                productRevenueData.setTotal(orderItemPojo.getSellingPrice()*orderItemPojo.getQuantity());
            }
        });

        return productRevenueData;
    }

    public static InventoryReportData convert(InventoryPojo inventoryPojo, BrandPojo brandPojo, ProductPojo productPojo) throws ApiException
    {
        InventoryReportData inventoryReportData = new InventoryReportData();
        inventoryReportData.setBrand(brandPojo.getBrand());
        inventoryReportData.setCategory(brandPojo.getCategory());
        inventoryReportData.setQuantity(inventoryPojo.getQuantity());
        inventoryReportData.setProductName(productPojo.getName());
        return inventoryReportData;
    }

    public static BrandData convert(BrandPojo brandPojo) throws ApiException
    {
        BrandData brandData = new BrandData();
        brandData.setBrand(brandPojo.getBrand());
        brandData.setId(brandPojo.getBrandId());
        brandData.setCategory(brandPojo.getCategory());
        return brandData;
    }

    public static DailyReportData convert(DailyReportPojo dailyReportPojo) throws ApiException
    {
        DailyReportData dailyReportData = new DailyReportData();
        dailyReportData.setInvoiceDate(dailyReportPojo.getInvoiceDate());
        dailyReportData.setInvoicedOrderCount(dailyReportPojo.getInvoicedOrderCount());
        dailyReportData.setInvoicedItemsCount(dailyReportPojo.getInvoicedItemsCount());
        dailyReportData.setTotalRevenue(dailyReportPojo.getTotalRevenue());

        return dailyReportData;
    }
}
