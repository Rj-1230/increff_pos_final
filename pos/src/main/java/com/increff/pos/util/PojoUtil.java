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

    public static InventoryPojo getInventoryPojo(Integer productId, Integer quantity){
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productId);
        inventoryPojo.setQuantity(quantity);
        return  inventoryPojo;
    }
    public static CartItemPojo getCartPojo(Integer productId, Integer counterId, Double sellingPrice, Integer quantity){
        CartItemPojo cartItemPojo = new CartItemPojo();
        cartItemPojo.setProductId(productId);
        cartItemPojo.setCounterId(counterId);
        cartItemPojo.setSellingPrice(sellingPrice);
        cartItemPojo.setQuantity(quantity);
        return cartItemPojo;
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


    public static OrderPojo getOrderPojo(String customerName, String customerPhone, ZonedDateTime orderCreateTime, ZonedDateTime orderInvoiceTime,
                                         String status,Integer counterId){
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setCustomerName(customerName);
        orderPojo.setCustomerPhone(customerPhone);
        orderPojo.setOrderCreateTime(orderCreateTime);
        orderPojo.setOrderInvoiceTime(orderInvoiceTime);
        orderPojo.setStatus(status);
        orderPojo.setCounterId(counterId);
        return  orderPojo;
    }

    public static OrderItemPojo getOrderItemPojo(Integer orderId,Integer productId,Integer quantity, Double sellingPrice){
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setProductId(productId);
        orderItemPojo.setQuantity(quantity);
        orderItemPojo.setSellingPrice(sellingPrice);
        return orderItemPojo;
    }
}
