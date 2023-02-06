package com.increff.pos.api;

import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.util.PojoUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class InventoryApiTest extends AbstractUnitTest {
    @Autowired
    private InventoryApi inventoryApi;
    @Autowired
    private InventoryDao inventoryDao;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testAddInventory() throws ApiException {
        Integer productId = 1;
        Integer quantity = 5;
        InventoryPojo inventoryPojo = PojoUtil.getInventoryPojo(productId,quantity);
        inventoryApi.addNewItemToInventory(inventoryPojo);

        List<InventoryPojo> inventoryPojoList =  inventoryApi.getAll();
        assertEquals(inventoryPojoList.size(),1);
        assertEquals(inventoryPojoList.get(0).getProductId(),productId);
        assertEquals(inventoryPojoList.get(0).getQuantity(),quantity);
    }
    @Test
    public void testSizeOfInventoryList() throws ApiException {
        Integer productId = 1;
        Integer quantity = 5;
        for(int i=0;i<4;i++){
            InventoryPojo inventoryPojo = PojoUtil.getInventoryPojo(productId+i,quantity);
            inventoryDao.insert(inventoryPojo);
        }
        List<InventoryPojo> inventoryPojoList =  inventoryApi.getAll();
        assertEquals(inventoryPojoList.size(),4);
    }

    @Test
    public void updateInventory() throws ApiException {
        Integer productId = 1;
        Integer quantity = 5;
        InventoryPojo inventoryPojo = PojoUtil.getInventoryPojo(productId,quantity);
        inventoryDao.insert(inventoryPojo);

        Integer newQuantity = 10;
        InventoryPojo updatedInventoryPojo = PojoUtil.getInventoryPojo(productId,newQuantity);
        inventoryApi.updateInventory(updatedInventoryPojo, newQuantity);
        List<InventoryPojo> inventoryPojoList =  inventoryApi.getAll();
        assertEquals(inventoryPojoList.get(0).getProductId(),productId);
        assertEquals(inventoryPojoList.get(0).getQuantity(),newQuantity);
    }

    @Test
    public void testGetCheckInventory() throws ApiException {
        Integer productId = 1;
        Integer quantity = 5;
        InventoryPojo inventoryPojo = PojoUtil.getInventoryPojo(productId,quantity);
        inventoryDao.insert(inventoryPojo);

        InventoryPojo inventoryPojo1 = inventoryApi.getCheck(inventoryPojo.getInventoryId());
       assertEquals(inventoryPojo1,inventoryPojo);
    }

    @Test
    public void testPositiveQuantity() throws ApiException {
        Integer productId = 1;
        Integer quantity = 5;
        InventoryPojo inventoryPojo = PojoUtil.getInventoryPojo(productId,quantity);
        inventoryDao.insert(inventoryPojo);

        Integer newQuantity=-2;
        InventoryPojo newInventoryPojo = PojoUtil.getInventoryPojo(productId,newQuantity);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The inventory must be non-negative after update. Current Inventory count :"+quantity+" User wants to set quantity as : "+newQuantity);
        inventoryApi.updateInventory(newInventoryPojo,newQuantity);
    }

    @Test
    public void testProductExistenceInInventory() throws ApiException {
        Integer productId = 1;
        Integer quantity = 5;
        InventoryPojo inventoryPojo = PojoUtil.getInventoryPojo(productId,quantity);
        inventoryDao.insert(inventoryPojo);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("No such inventory with given inventory Id exists !");
        inventoryApi.getCheck(inventoryPojo.getInventoryId()+1);
    }

    @Test
    public void testProductExistenceInInventoryByProductId() throws ApiException {
        Integer productId = 1;
        Integer quantity = 5;
        InventoryPojo inventoryPojo = PojoUtil.getInventoryPojo(productId,quantity);
        inventoryDao.insert(inventoryPojo);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("No such inventory with given product Id exists !");
        inventoryApi.getCheckByProductId(inventoryPojo.getProductId()+1);
    }

}
