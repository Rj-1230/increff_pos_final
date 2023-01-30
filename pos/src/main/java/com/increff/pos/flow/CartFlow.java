package com.increff.pos.flow;

import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.increff.pos.helper.CartFlowHelper.convertCartPojoToOrderItemPojo;
@Service
public class CartFlow {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;

    @Transactional(rollbackOn = ApiException.class)
    public void add(CartPojo cartPojo,String barcode) throws ApiException {
        ProductPojo productPojo= productService.getProductPojoFromBarcode(barcode);
        cartPojo.setProductId(productPojo.getProductId());
        cartPojo.setProductName(productPojo.getName());
        Integer inventoryQuantity = checkMrpAndInventoryForCartPojo(cartPojo,productPojo);
        cartService.add(cartPojo,inventoryQuantity);
    }


    @Transactional(rollbackOn  = ApiException.class)
    public void update(Integer id, CartPojo newCartPojo) throws ApiException {
        CartPojo exCartPojo = cartService.getCheck(id);
        ProductPojo productPojo = productService.getCheck(newCartPojo.getProductId());
        if(newCartPojo.getSellingPrice()>productPojo.getMrp()){
            throw new ApiException("Item can't be added to cart as selling price must be less than MRP. Product's MRP :"+productPojo.getMrp());

        }
        InventoryPojo inventoryPojo = inventoryService.getCheck(exCartPojo.getProductId());
        if(newCartPojo.getQuantity()>inventoryPojo.getQuantity()){
            throw new ApiException("Item can't be updated as it exceeds the inventory. Present inventory count : "+inventoryPojo.getQuantity());
        }

        cartService.update(exCartPojo,newCartPojo);
    }



    @Transactional(rollbackOn = ApiException.class)
    private Integer checkMrpAndInventoryForCartPojo(CartPojo cartPojo, ProductPojo productPojo) throws ApiException {
        if(cartPojo.getSellingPrice()>productPojo.getMrp()){
            throw new ApiException("Item can't be added to cart as selling price must be less than MRP. Product's MRP :"+productPojo.getMrp());
        }
        InventoryPojo inventoryPojo = inventoryService.getCheck(cartPojo.getProductId());
        if (cartPojo.getQuantity() > inventoryPojo.getQuantity()) {
            throw new ApiException("Item can't be added to cart as it exceeds the inventory. Present inventory count : " + inventoryPojo.getQuantity());
        }
        return inventoryPojo.getQuantity();
    }
}
