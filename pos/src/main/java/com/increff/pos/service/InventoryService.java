package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class InventoryService {

    @Autowired
    private InventoryDao inventoryDao;

    @Transactional(rollbackOn = ApiException.class)
    public void addNewItemToInventory(InventoryPojo newInventoryPojo) throws ApiException {
        inventoryDao.insert(newInventoryPojo);

    }
    @Transactional(rollbackOn = ApiException.class)
    public void updateInventory(InventoryPojo newInventoryPojo, int quantity) throws ApiException {
        InventoryPojo exInventoryPojo = getCheck(newInventoryPojo.getProductId());
        if (quantity <0) {
            throw new ApiException("The inventory count must not be negative. Current Inventory count :" + exInventoryPojo.getQuantity());
        }
        exInventoryPojo.setQuantity(quantity);
    }


    @Transactional
    public void delete(int id) {
        inventoryDao.delete(id);
    }

    @Transactional
    public List<InventoryPojo> getAll() {
        return inventoryDao.selectAll();
    }

    @Transactional(rollbackOn = ApiException.class)
    public InventoryPojo getCheck(int id) throws ApiException {
        InventoryPojo inventoryPojo = inventoryDao.select(id);
        if (Objects.isNull(inventoryPojo)) {
            throw new ApiException("No such inventory with given Product Id exists !");
        }
        return inventoryPojo;
    }

}