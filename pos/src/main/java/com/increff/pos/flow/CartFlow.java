package com.increff.pos.flow;

import com.increff.pos.pojo.*;
import com.increff.pos.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CartFlow {
    @Autowired
    private InventoryApi inventoryApi;
    @Autowired
    private CartApi cartApi;

    @Autowired
    private OrderApi orderApi;
    @Autowired
    private ProductApi productApi;

    @Transactional(rollbackOn = ApiException.class)
    public void add(CartPojo cartPojo,String barcode) throws ApiException {
        ProductPojo productPojo= productApi.getCheckProductPojoFromBarcode(barcode);
        cartPojo.setProductId(productPojo.getProductId());
        cartPojo.setProductName(productPojo.getName());
        Integer inventoryQuantity = checkMrpAndInventoryForCartPojo(cartPojo,productPojo);
        cartApi.add(cartPojo,inventoryQuantity);
    }


    @Transactional(rollbackOn  = ApiException.class)
    public void update(Integer id, CartPojo newCartPojo) throws ApiException {
        CartPojo exCartPojo = cartApi.getCheck(id);
        System.out.println(newCartPojo.getProductId());
        ProductPojo productPojo = productApi.getCheckProduct(exCartPojo.getProductId());
        if(newCartPojo.getSellingPrice()>productPojo.getMrp()){
            throw new ApiException("Item can't be added to cart as selling price must be less than MRP. Product's MRP :"+productPojo.getMrp());

        }
        InventoryPojo inventoryPojo = inventoryApi.getCheck(exCartPojo.getProductId());
        if(newCartPojo.getQuantity()>inventoryPojo.getQuantity()){
            throw new ApiException("Item can't be updated as it exceeds the inventory. Present inventory count : "+inventoryPojo.getQuantity());
        }

        cartApi.update(exCartPojo,newCartPojo);
    }



    @Transactional(rollbackOn = ApiException.class)
    private Integer checkMrpAndInventoryForCartPojo(CartPojo cartPojo, ProductPojo productPojo) throws ApiException {
        if(cartPojo.getSellingPrice()>productPojo.getMrp()){
            throw new ApiException("Item can't be added to cart as selling price must be less than MRP. Product's MRP :"+productPojo.getMrp());
        }
        InventoryPojo inventoryPojo = inventoryApi.getCheck(cartPojo.getProductId());
        if (cartPojo.getQuantity() > inventoryPojo.getQuantity()) {
            throw new ApiException("Item can't be added to cart as it exceeds the inventory. Present inventory count : " + inventoryPojo.getQuantity());
        }
        return inventoryPojo.getQuantity();
    }
}
