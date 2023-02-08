package com.increff.pos.util;

import com.increff.pos.pojo.*;

import java.time.ZonedDateTime;

public class PojoUtil {

    public static BrandPojo getBrandPojo(String brand, String category) {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brand);
        brandPojo.setCategory(category);
        return brandPojo;
    }

    public static ProductPojo getProductPojo(Integer brandId, String barcode, Double mrp, String productName) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBrandId(brandId);
        productPojo.setBarcode(barcode);
        productPojo.setMrp(mrp);
        productPojo.setName(productName);
        return productPojo;
    }

    public static InventoryPojo getInventoryPojo(Integer productId, Integer quantity) {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productId);
        inventoryPojo.setQuantity(quantity);
        return inventoryPojo;
    }

    public static CartItemPojo getCartPojo(Integer productId, Integer counterId, Double sellingPrice, Integer quantity) {
        CartItemPojo cartItemPojo = new CartItemPojo();
        cartItemPojo.setProductId(productId);
        cartItemPojo.setCounterId(counterId);
        cartItemPojo.setSellingPrice(sellingPrice);
        cartItemPojo.setQuantity(quantity);
        return cartItemPojo;
    }
}
