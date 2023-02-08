package com.increff.pos.helper.dtoHelper;

import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;

public class InventoryDtoHelper {
    public static InventoryPojo convert(InventoryForm f) {
        InventoryPojo p = new InventoryPojo();
        p.setQuantity(f.getQuantity());
        return p;
    }

    public static InventoryData convert(InventoryPojo p) {
        InventoryData d = new InventoryData();
        d.setQuantity(p.getQuantity());
        return d;
    }

    public static void normalize(InventoryForm f) {
        f.setBarcode(f.getBarcode().toLowerCase().trim());
        if (f.getBarcode().length() > 20) {
            f.setBarcode(f.getBarcode().substring(0, 15));
        }
    }

}
