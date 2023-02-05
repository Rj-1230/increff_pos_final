package com.increff.pos.helper.dtoHelper;

import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;

@Service
public class ProductDtoHelper {
    private static final NumberFormat formatter = new DecimalFormat("#0.00");

    public static void normalize(ProductForm f) {
        f.setBarcode(f.getBarcode().toLowerCase().trim());
        f.setMrp(Double.parseDouble(formatter.format(f.getMrp())));
        f.setName(f.getName().toLowerCase().trim());

        if(f.getBrandName().length()>15){
            f.setBrandName(f.getBrandName().substring(0,15));
        }
        if(f.getCategoryName().length()>15){
            f.setCategoryName(f.getCategoryName().substring(0,15));
        }
        if(f.getBarcode().length()>15){
            f.setBarcode(f.getBarcode().substring(0,15));
        }
        if(f.getName().length()>15){
            f.setName(f.getName().substring(0,15));
        }
    }

    public static ProductData convert(ProductPojo p){
        ProductData d = new ProductData();
        d.setProductId(p.getProductId());
        d.setBarcode(p.getBarcode());
        d.setName(p.getName());
        d.setMrp(p.getMrp());
        return d;
    }

    public static ProductPojo convert(ProductForm f){
        ProductPojo p = new ProductPojo();
        p.setName(f.getName());
        p.setBarcode(f.getBarcode());
        p.setMrp(f.getMrp());
        return p;
    }
}
