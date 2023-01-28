package com.increff.pos.util;

import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;

public class FormUtil {
    public static BrandForm getBrandForm(String brand, String category){
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        return brandForm;
    }
}
