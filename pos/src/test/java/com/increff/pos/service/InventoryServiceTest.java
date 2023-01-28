package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.util.PojoUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class InventoryServiceTest extends AbstractUnitTest{
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private InventoryDao inventoryDao;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testAddInventory() throws ApiException {
        Integer productId = 1;
        String barcode = "abc";
        Integer quantity = 5;
        InventoryPojo inventoryPojo = PojoUtil.getInventoryPojo(productId,barcode,quantity);
        inventoryService.addNewItemToInventory(inventoryPojo);

        List<InventoryPojo> inventoryPojoList =  inventoryService.getAll();
        assertEquals(inventoryPojoList.size(),1);
        assertEquals(inventoryPojoList.get(0).getProductId(),productId);
        assertEquals(inventoryPojoList.get(0).getBarcode(),barcode);
        assertEquals(inventoryPojoList.get(0).getQuantity(),quantity);
    }
    @Test
    public void testSizeOfInventoryList() throws ApiException {
        Integer productId = 1;
        String barcode = "abc";
        Integer quantity = 5;
        for(int i=0;i<4;i++){
            InventoryPojo inventoryPojo = PojoUtil.getInventoryPojo(productId+i,barcode+i,quantity);
            inventoryDao.insert(inventoryPojo);
        }
        List<InventoryPojo> inventoryPojoList =  inventoryService.getAll();
        assertEquals(inventoryPojoList.size(),4);
    }

    @Test
    public void testDeleteInventory() throws ApiException {
        Integer productId = 1;
        String barcode = "abc";
        Integer quantity = 5;
        for(int i=0;i<4;i++){
            InventoryPojo inventoryPojo = PojoUtil.getInventoryPojo(productId+i,barcode+i,quantity);
            inventoryDao.insert(inventoryPojo);
        }
        List<InventoryPojo> inventoryPojoList =  inventoryService.getAll();
        assertEquals(inventoryPojoList.size(),4);
        inventoryService.delete(inventoryPojoList.get(0).getProductId());
        List<InventoryPojo> newInventoryPojoList =  inventoryService.getAll();
        assertEquals(newInventoryPojoList.size(),3);
    }

    @Test
    public void updateInventory() throws ApiException {
        Integer productId = 1;
        String barcode = "abc";
        Integer quantity = 5;
        InventoryPojo inventoryPojo = PojoUtil.getInventoryPojo(productId,barcode,quantity);
        inventoryDao.insert(inventoryPojo);

        Integer newQuantity = 10;
        InventoryPojo updatedInventoryPojo = PojoUtil.getInventoryPojo(productId,barcode,newQuantity);
        inventoryService.updateInventory(updatedInventoryPojo, newQuantity);
        List<InventoryPojo> inventoryPojoList =  inventoryService.getAll();
        assertEquals(inventoryPojoList.get(0).getProductId(),productId);
        assertEquals(inventoryPojoList.get(0).getBarcode(),barcode);
        assertEquals(inventoryPojoList.get(0).getQuantity(),newQuantity);
    }

    @Test
    public void testGetCheckInventory() throws ApiException {
        Integer productId = 1;
        String barcode = "abc";
        Integer quantity = 5;
        InventoryPojo inventoryPojo = PojoUtil.getInventoryPojo(productId,barcode,quantity);
        inventoryDao.insert(inventoryPojo);

        InventoryPojo inventoryPojo1 = inventoryService.getCheck(inventoryPojo.getProductId());
       assertEquals(inventoryPojo1,inventoryPojo);
    }

    @Test
    public void testPositiveQuantity() throws ApiException {
        Integer productId = 1;
        String barcode = "abc";
        Integer quantity = 5;
        InventoryPojo inventoryPojo = PojoUtil.getInventoryPojo(productId,barcode,quantity);
        inventoryDao.insert(inventoryPojo);

        Integer newQuantity=-2;
        InventoryPojo newInventoryPojo = PojoUtil.getInventoryPojo(productId,barcode,newQuantity);
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The inventory count must not be negative. Current Inventory count :" + quantity);
        inventoryService.updateInventory(newInventoryPojo,newQuantity);
    }

    @Test
    public void testProductExistenceInInventory() throws ApiException {
        Integer productId = 1;
        String barcode = "abc";
        Integer quantity = 5;
        InventoryPojo inventoryPojo = PojoUtil.getInventoryPojo(productId,barcode,quantity);
        inventoryDao.insert(inventoryPojo);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("No such inventory with given Product Id exists !");
        inventoryService.getCheck(inventoryPojo.getProductId()+1);
    }

}
