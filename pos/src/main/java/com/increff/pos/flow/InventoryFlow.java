package com.increff.pos.flow;

import com.increff.pos.helper.ProductDtoHelper;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.helper.InventoryDtoHelper.*;

@Service
public class InventoryFlow {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;

    @Transactional(rollbackOn = ApiException.class)
    public void update(InventoryPojo newInventoryPojo) throws ApiException {
        ProductPojo productPojo = productService.getProductPojoFromBarcode(newInventoryPojo.getBarcode());
        newInventoryPojo.setProductId(productPojo.getProductId());
        inventoryService.updateInventory(newInventoryPojo, newInventoryPojo.getQuantity());
    }

    @Transactional(rollbackOn = ApiException.class)
    public InventoryData get(Integer id) throws ApiException {
        InventoryPojo inventoryPojo = inventoryService.getCheck(id);
        ProductPojo productPojo = productService.getCheck(id);
        InventoryData inventoryData = convert(inventoryPojo);
        inventoryData.setProductName(productPojo.getName());
        inventoryData.setProductId(productPojo.getProductId());
        return inventoryData;
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<InventoryData> getAll() throws ApiException {
        List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
        List<InventoryData> list2 = new ArrayList<InventoryData>();
        for(InventoryPojo inventoryPojo: inventoryPojoList){
            ProductPojo productPojo = productService.getCheck(inventoryPojo.getProductId());
            InventoryData inventoryData = convert(inventoryPojo);
            inventoryData.setProductName(productPojo.getName());
            inventoryData.setProductId(productPojo.getProductId());
            list2.add(inventoryData);
        }
        return list2;
    }
}
