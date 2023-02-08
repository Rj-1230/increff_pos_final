package com.increff.pos.helper.flowHelper;

import com.increff.pos.helper.dtoHelper.ProductDtoHelper;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;

public class ProductFlowHelper {
    public static ProductData convertProductPojoToProductData(ProductPojo productPojo, BrandPojo brandPojo) {
        ProductData productData = ProductDtoHelper.convert(productPojo);
        productData.setBrand(brandPojo.getBrand());
        productData.setCategory(brandPojo.getCategory());
        return productData;
    }
}
