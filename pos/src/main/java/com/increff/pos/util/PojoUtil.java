package com.increff.pos.util;

import com.increff.pos.pojo.*;

import java.time.ZonedDateTime;

public class PojoUtil {

    public static BrandPojo getBrandPojo(String brand, String category){
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brand);
        brandPojo.setCategory(category);
        return brandPojo;
    }

    public static ProductPojo getProductPojo(Integer brandId, String barcode,Double mrp, String productName){
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBrandId(brandId);
        productPojo.setBarcode(barcode);
        productPojo.setMrp(mrp);
        productPojo.setName(productName);
        return  productPojo;
    }

    public static InventoryPojo getInventoryPojo(Integer productId, String barcode, Integer quantity){
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productId);
        inventoryPojo.setBarcode(barcode);
        inventoryPojo.setQuantity(quantity);
        return  inventoryPojo;
    }
    public static CartPojo getCartPojo(Integer productId,Integer counterId, String productName, Double sellingPrice, Integer quantity){
        CartPojo cartPojo = new CartPojo();
        cartPojo.setProductId(productId);
        cartPojo.setCounterId(counterId);
        cartPojo.setProductName(productName);
        cartPojo.setSellingPrice(sellingPrice);
        cartPojo.setQuantity(quantity);
        return  cartPojo;
    }

    public static UserPojo getUserPojo(String email, String password){
        UserPojo userPojo = new UserPojo();
        userPojo.setEmail(email);
        userPojo.setPassword(password);
        return userPojo;
    }

    public static DailyReportPojo getDailyReportPojo(ZonedDateTime date, Integer invoicedOrderCount, Integer invoicedItemsCount, Double totalRevenue){
        DailyReportPojo dailyReportPojo = new DailyReportPojo();
        dailyReportPojo.setInvoiceDate(date);
        dailyReportPojo.setInvoicedOrderCount(invoicedOrderCount);
        dailyReportPojo.setInvoicedItemsCount(invoicedItemsCount);
        dailyReportPojo.setTotalRevenue(totalRevenue);
        return  dailyReportPojo;
    }
}
