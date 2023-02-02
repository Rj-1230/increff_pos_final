package com.increff.pos.helper.dtoHelper;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandDtoHelper {
    public static BrandPojo convert(BrandForm f){
        BrandPojo p = new BrandPojo();
        p.setBrand(f.getBrand());
        p.setCategory(f.getCategory());
        return p;
    }

    public static BrandData convert(BrandPojo p){
        BrandData d = new BrandData();
        d.setBrand(p.getBrand());
        d.setCategory(p.getCategory());
        d.setId(p.getId());
        return d;
    }

    public static void normalize(BrandForm f) {
        f.setBrand(f.getBrand().toLowerCase().trim());
        f.setCategory(f.getCategory().toLowerCase().trim());

        if(f.getBrand().length()>15){
            f.setBrand(f.getBrand().substring(0,15));
        }
        if(f.getCategory().length()>15){
            f.setCategory(f.getCategory().substring(0,15));
        }
    }

    public static List<BrandData> getAllBrands(List<BrandPojo> list){
        List<BrandData> list2 = new ArrayList<BrandData>();
        for(BrandPojo p: list){
            list2.add(convert(p));
        }
        return list2;
    }
}
