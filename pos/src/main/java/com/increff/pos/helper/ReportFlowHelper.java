package com.increff.pos.helper;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.DailyReportData;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.ProductRevenueData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.DailyReportPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.api.ApiException;

import java.util.Objects;

public class ReportFlowHelper {

    public static boolean filterByBrandCategory (String dataBrand,String dataCategory, String formBrand, String formCategory){
        return (((Objects.equals(dataBrand,formBrand)) && (Objects.equals(dataCategory,formCategory))) ||
                ((Objects.equals(formBrand,"All")) && (Objects.equals(dataCategory,formCategory))) ||
                ((Objects.equals(dataBrand,formBrand)) && (Objects.equals(formCategory,"All"))) ||
                ((Objects.equals(formBrand,"All")) && (Objects.equals(formCategory,"All"))));

    }

    public static ProductRevenueData convert(ProductPojo p,BrandPojo brandPojo)
    {
        ProductRevenueData productRevenueData = new ProductRevenueData();
        productRevenueData.setProductId(p.getProductId());
        productRevenueData.setBarcode(p.getBarcode());
        productRevenueData.setMrp(p.getMrp());
        productRevenueData.setName(p.getName());
        productRevenueData.setQuantity(0);
        productRevenueData.setTotal(0.0);

        productRevenueData.setBrand(brandPojo.getBrand());
        productRevenueData.setCategory(brandPojo.getCategory());

        return productRevenueData;
    }

    public static InventoryReportData convert(InventoryPojo p,BrandPojo brandPojo) throws ApiException
    {
        InventoryReportData inventoryReportData = new InventoryReportData();
        inventoryReportData.setBrand(brandPojo.getBrand());
        inventoryReportData.setCategory(brandPojo.getCategory());
        inventoryReportData.setQuantity(p.getQuantity());

        return inventoryReportData;
    }

    public static BrandData convert(BrandPojo p) throws ApiException
    {
        BrandData brandData = new BrandData();
        brandData.setBrand(p.getBrand());
        brandData.setId(p.getId());
        brandData.setCategory(p.getCategory());
        return brandData;
    }

    public static DailyReportData convert(DailyReportPojo p) throws ApiException
    {
        DailyReportData dailyReportData = new DailyReportData();
        dailyReportData.setInvoiceDate(p.getInvoiceDate());
        dailyReportData.setInvoicedOrderCount(p.getInvoicedOrderCount());
        dailyReportData.setInvoicedItemsCount(p.getInvoicedItemsCount());
        dailyReportData.setTotalRevenue(p.getTotalRevenue());

        return dailyReportData;
    }
}
