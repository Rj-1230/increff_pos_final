package com.increff.pos.flow;

import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

import static com.increff.pos.util.generateRandomString.createRandomOrderCode;

@Service
public class OrderFlow {
    @Autowired
    private OrderService orderService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;

    @Transactional(rollbackOn  = ApiException.class)
    public void addNewOrder(OrderPojo orderPojo) throws ApiException {
        List<CartPojo> cartPojoList = cartService.getAll();
        if(cartPojoList.size()==0){
            throw new ApiException("The order can't be created as the cart is empty");
        }
        checkSufficientInventoryToCreateOrder(cartPojoList);
        orderService.addOrder(orderPojo,cartPojoList);
        cartService.deleteAll();
    }

    private void checkSufficientInventoryToCreateOrder(List<CartPojo> cartPojoList) throws ApiException {
        for(CartPojo d : cartPojoList){
            InventoryPojo inventoryPojo = inventoryService.getCheck(d.getProductId());
            if(d.getQuantity()>inventoryPojo.getQuantity()){
                throw new ApiException("The item "+d.getProductName()+" can't be added to order because sufficient amount not present in inventory. Inventory count = "+inventoryPojo.getQuantity()+"Cart count ="+d.getQuantity());
            }
        }
    }



    @Transactional(rollbackOn = ApiException.class)
    public void addOrderItem(OrderItemPojo orderItemPojo,String barcode) throws ApiException {
        ProductPojo productPojo= productService.getProductPojoFromBarcode(barcode);
        orderItemPojo.setProductId(productPojo.getProductId());
        orderItemPojo.setProductName(productPojo.getName());
        checkSufficientInventoryToAddOrderItem(orderItemPojo,productPojo.getMrp());
        orderService.addOrderItem(orderItemPojo);
    }

    @Transactional
    private void checkSufficientInventoryToAddOrderItem(OrderItemPojo orderItemPojo, Double mrp) throws ApiException {
    InventoryPojo inventoryPojo = inventoryService.getCheck(orderItemPojo.getProductId());
    if(orderItemPojo.getQuantity()>inventoryPojo.getQuantity()){
        throw new ApiException("Item can't be added to order as it exceeds the inventory. Present inventory count : "+inventoryPojo.getQuantity());
    }

    if(orderItemPojo.getSellingPrice()>mrp){
        throw new ApiException("Item can't be added to order as selling price must be less than MRP. Product's MRP :"+mrp);
    }
    inventoryService.updateInventory(inventoryPojo,inventoryPojo.getQuantity()-orderItemPojo.getQuantity());
    }


    @Transactional(rollbackOn = ApiException.class)
    public void deleteOrderItem(Integer id) throws ApiException {
        OrderItemPojo ex = orderService.getCheckOrderItem(id);
        InventoryPojo inventoryPojo = inventoryService.getCheck(ex.getProductId());
        inventoryService.updateInventory(inventoryPojo,inventoryPojo.getQuantity()+ ex.getQuantity());
        orderService.deleteOrderItem(id);
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void updateOrderItem(Integer id, OrderItemPojo orderItemPojo) throws ApiException {
        OrderItemPojo ex = orderService.getCheckOrderItem(id);
        InventoryPojo inventoryPojo = inventoryService.getCheck(ex.getProductId());
        ProductPojo productPojo= productService.getCheck(ex.getProductId());
        if(orderItemPojo.getSellingPrice()>productPojo.getMrp()){
            throw new ApiException("Item can't be updated to order as selling price must be less than MRP. Product's MRP :"+productPojo.getMrp());
        }
        inventoryService.updateInventory(inventoryPojo,inventoryPojo.getQuantity()-orderItemPojo.getQuantity()+ex.getQuantity());
        orderService.updateOrderItem(ex,orderItemPojo);
    }


}
