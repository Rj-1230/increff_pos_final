package com.increff.pos.util;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;

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
}
