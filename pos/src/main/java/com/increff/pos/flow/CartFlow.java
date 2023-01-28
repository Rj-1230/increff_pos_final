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
    private OrderFlow orderFlow;
    @Autowired
    private ProductService productService;

    @Transactional(rollbackOn = ApiException.class)
    public void add(CartPojo cartPojo,String barcode) throws ApiException {
        ProductPojo productPojo= productService.getProductPojoFromBarcode(barcode);
        cartPojo.setProductId(productPojo.getProductId());
        cartPojo.setProductName(productPojo.getName());

        if(cartPojo.getSellingPrice()>productPojo.getMrp()){
            throw new ApiException("Item can't be added to cart as selling price must be less than MRP. Product's MRP :"+productPojo.getMrp());

        }
        InventoryPojo inventoryPojo = inventoryService.getCheck(cartPojo.getProductId());
        if (cartPojo.getQuantity() > inventoryPojo.getQuantity()) {
            throw new ApiException("Item can't be added to cart as it exceeds the inventory. Present inventory count : " + inventoryPojo.getQuantity());
        }
        cartService.add(cartPojo,inventoryPojo);
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, CartPojo newCartPojo) throws ApiException {
        CartPojo exCartPojo = cartService.getCheck(id);
        InventoryPojo inventoryPojo = inventoryService.getCheck(exCartPojo.getProductId());
        if(newCartPojo.getQuantity()>inventoryPojo.getQuantity()){
            throw new ApiException("Item can't be updated as it exceeds the inventory. Present inventory count : "+inventoryPojo.getQuantity());
        }
        ProductPojo productPojo = productService.getCheck(newCartPojo.getProductId());
        if(newCartPojo.getSellingPrice()>productPojo.getMrp()){
            throw new ApiException("Item can't be added to cart as selling price must be less than MRP. Product's MRP :"+productPojo.getMrp());

        }
        cartService.update(exCartPojo,newCartPojo);
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void pushToNewOrder(OrderPojo orderPojo) throws ApiException {
        List<CartPojo> cartPojoList = cartService.getAll();
        if(cartPojoList.size()==0){
            throw new ApiException("The order can't be created as the cart is empty");
        }
        for(CartPojo d : cartPojoList){
            InventoryPojo a = inventoryService.getCheck(d.getProductId());
            if(d.getQuantity()>a.getQuantity()){
                throw new ApiException("The item "+d.getProductName()+" can't be added to order because sufficient amount not present in inventory. Inventory count = "+a.getQuantity()+"Cart count ="+d.getQuantity());
            }
        }
        int orderId = orderFlow.addOrder(orderPojo);
        for(CartPojo cartPojo : cartPojoList){
            OrderItemPojo orderItemPojo = convertCartPojoToOrderItemPojo(cartPojo,orderId);
            orderService.addOrderItem(orderItemPojo);
        }

        cartService.deleteAll();
    }

}
