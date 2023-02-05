package com.increff.pos.helper.dtoHelper;

import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.DateBrandCategoryFilterForm;
import com.increff.pos.model.form.DateFilterForm;

public class ReportDtoHelper {
    public static void normalize(DateBrandCategoryFilterForm f) {
        f.setBrand(f.getBrand().toLowerCase().trim());
        f.setCategory(f.getCategory().toLowerCase().trim());

        if(f.getBrand().length()>15){
            f.setBrand(f.getBrand().substring(0,15));
        }
        if(f.getCategory().length()>15){
            f.setCategory(f.getCategory().substring(0,15));
        }
    }
}
