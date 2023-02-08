package com.increff.pos.helper.dtoHelper;

import com.increff.pos.model.form.DateBrandCategoryFilterForm;

public class ReportDtoHelper {
    public static void normalize(DateBrandCategoryFilterForm f) {
        f.setBrand(f.getBrand().toLowerCase().trim());
        f.setCategory(f.getCategory().toLowerCase().trim());

        if (f.getBrand().length() > 20) {
            f.setBrand(f.getBrand().substring(0, 20));
        }
        if (f.getCategory().length() > 20) {
            f.setCategory(f.getCategory().substring(0, 20));
        }
    }
}
