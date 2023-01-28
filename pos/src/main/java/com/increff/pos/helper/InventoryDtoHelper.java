package com.increff.pos.helper;

import com.increff.pos.model.*;
import com.increff.pos.pojo.InventoryPojo;

import java.util.ArrayList;
import java.util.List;

public class InventoryDtoHelper {
    public static InventoryPojo convert(InventoryForm f){
        InventoryPojo p = new InventoryPojo();
        p.setBarcode(f.getBarcode());
        p.setQuantity(f.getQuantity());
        return p;
    }

    public static InventoryData convert(InventoryPojo p){
        InventoryData d = new InventoryData();
        d.setQuantity(p.getQuantity());
        d.setBarcode(p.getBarcode());
        return d;
    }

    public static void normalize(InventoryForm f) {
        f.setBarcode(f.getBarcode().toLowerCase().trim());
        if(f.getBarcode().length()>15) {
            f.setBarcode(f.getBarcode().substring(0, 15));
        }
    }


    public static List<InventoryData> getAllInventory(List<InventoryPojo> list){
        List<InventoryData> list2 = new ArrayList<InventoryData>();
        for(InventoryPojo p: list){
            list2.add(convert(p));
        }
        return list2;
    }
}
