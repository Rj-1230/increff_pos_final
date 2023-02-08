package com.increff.pos.api;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackOn = ApiException.class)
public class InventoryApi {

    @Autowired
    private InventoryDao inventoryDao;

    public void addNewItemToInventory(InventoryPojo newInventoryPojo) throws ApiException {
        inventoryDao.insert(newInventoryPojo);

    }

    public void updateInventory(InventoryPojo newInventoryPojo, Integer inventoryQuantity) throws ApiException {
        InventoryPojo exInventoryPojo = getCheckByProductId(newInventoryPojo.getProductId());
        if (inventoryQuantity < 0) {
            throw new ApiException("The inventory must be non-negative after update. Current Inventory count :" + exInventoryPojo.getQuantity() + " User wants to set quantity as : " + inventoryQuantity);
        }
        exInventoryPojo.setQuantity(inventoryQuantity);
    }

    public List<InventoryPojo> getAll() {
        return inventoryDao.selectAll();
    }

    public InventoryPojo getCheckByProductId(Integer productId) throws ApiException {
        InventoryPojo inventoryPojo = inventoryDao.selectByProductId(productId);
        if (Objects.isNull(inventoryPojo)) {
            throw new ApiException("No such inventory with given product Id exists !");
        }
        return inventoryPojo;
    }

    public InventoryPojo getCheck(Integer inventoryId) throws ApiException {
        InventoryPojo inventoryPojo = inventoryDao.select(inventoryId);
        if (Objects.isNull(inventoryPojo)) {
            throw new ApiException("No such inventory with given inventory Id exists !");
        }
        return inventoryPojo;
    }

}