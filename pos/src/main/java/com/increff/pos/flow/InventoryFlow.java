package com.increff.pos.flow;

import com.increff.pos.model.InventoryData;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.api.ApiException;
import com.increff.pos.api.InventoryApi;
import com.increff.pos.api.ProductApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.helper.InventoryDtoHelper.*;

@Service
public class InventoryFlow {
    @Autowired
    private InventoryApi inventoryApi;
    @Autowired
    private ProductApi productApi;

    @Transactional(rollbackOn = ApiException.class)
    public void update(InventoryPojo newInventoryPojo) throws ApiException {
        ProductPojo productPojo = productApi.getProductPojoFromBarcode(newInventoryPojo.getBarcode());
        newInventoryPojo.setProductId(productPojo.getProductId());
        inventoryApi.updateInventory(newInventoryPojo, newInventoryPojo.getQuantity());
    }

    @Transactional(rollbackOn = ApiException.class)
    public InventoryData get(Integer id) throws ApiException {
        InventoryPojo inventoryPojo = inventoryApi.getCheck(id);
        ProductPojo productPojo = productApi.getCheck(id);
        InventoryData inventoryData = convert(inventoryPojo);
        inventoryData.setProductName(productPojo.getName());
        inventoryData.setProductId(productPojo.getProductId());
        return inventoryData;
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<InventoryData> getAll() throws ApiException {
        List<InventoryPojo> inventoryPojoList = inventoryApi.getAll();
        List<InventoryData> list2 = new ArrayList<InventoryData>();
        for(InventoryPojo inventoryPojo: inventoryPojoList){
            ProductPojo productPojo = productApi.getCheck(inventoryPojo.getProductId());
            InventoryData inventoryData = convert(inventoryPojo);
            inventoryData.setProductName(productPojo.getName());
            inventoryData.setProductId(productPojo.getProductId());
            list2.add(inventoryData);
        }
        return list2;
    }
}
