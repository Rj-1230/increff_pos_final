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
    public void updateInventory(InventoryPojo newInventoryPojo, Integer quantity) throws ApiException {
        InventoryPojo exInventoryPojo = getCheck(newInventoryPojo.getProductId());
        if (quantity <0) {
            throw new ApiException("The inventory must be non-negative after update. Current Inventory count :" + exInventoryPojo.getQuantity()+" User wants to set quantity as : "+quantity);
        }
        exInventoryPojo.setQuantity(quantity);
    }


    public void delete(Integer id) throws ApiException {
        getCheck(id);
        inventoryDao.delete(id);
    }

    public List<InventoryPojo> getAll() {
        return inventoryDao.selectAll();
    }

    public InventoryPojo getCheck(Integer productId) throws ApiException {
        InventoryPojo inventoryPojo = inventoryDao.select(productId);
        if (Objects.isNull(inventoryPojo)) {
            throw new ApiException("No such inventory with given Product Id exists !");
        }
        return inventoryPojo;
    }

}