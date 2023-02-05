package com.increff.pos.helper.flowHelper;

import com.increff.pos.model.data.InventoryData;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;

import static com.increff.pos.helper.dtoHelper.InventoryDtoHelper.convert;

public class InventoryFlowHelper {
    public static InventoryData convertInventoryPojoToInventoryData(InventoryPojo inventoryPojo, ProductPojo productPojo){
        InventoryData inventoryData = convert(inventoryPojo);
        inventoryData.setProductName(productPojo.getName());
        inventoryData.setBarcode(productPojo.getBarcode());
        inventoryData.setProductId(productPojo.getProductId());
        return inventoryData;
    }
}
