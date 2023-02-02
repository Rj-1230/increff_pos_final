package com.increff.pos.flow;

import com.increff.pos.pojo.*;
import com.increff.pos.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.increff.pos.helper.dtoHelper.OrderItemDtoHelper.getAllOrderItemsOfAgivenOrder;
import static com.increff.pos.util.SecurityUtil.getPrincipal;
import static com.increff.pos.helper.dtoHelper.OrderItemDtoHelper.convert;

@Service
public class OrderFlow {
    @Autowired
    private OrderApi orderApi;
    @Autowired
    private InventoryApi inventoryApi;
    @Autowired
    private InvoiceClientApi invoiceClientApi;
    @Autowired
    private ProductApi productApi;
    @Autowired
    private CartApi cartApi;

    @Transactional(rollbackOn  = ApiException.class)
    public void addNewOrder(OrderPojo orderPojo) throws ApiException {
        List<CartPojo> cartPojoList = cartApi.getAll(getPrincipal().getId());
        if(cartPojoList.size()==0){
            throw new ApiException("The order can't be created as the cart is empty");
        }
        checkSufficientInventoryToCreateOrder(cartPojoList);
        orderApi.addOrder(orderPojo,cartPojoList);
        cartApi.deleteAll(cartPojoList);
    }

    private void checkSufficientInventoryToCreateOrder(List<CartPojo> cartPojoList) throws ApiException {
        for(CartPojo d : cartPojoList){
            InventoryPojo inventoryPojo = inventoryApi.getCheck(d.getProductId());
            if(d.getQuantity()>inventoryPojo.getQuantity()){
                throw new ApiException("The item "+d.getProductName()+" can't be added to order because sufficient amount not present in inventory. Inventory count = "+inventoryPojo.getQuantity()+"Cart count ="+d.getQuantity());
            }
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void invoiceOrder(Integer id) throws Exception {
        orderApi.invoiceOrder(id);
        OrderPojo orderPojo = orderApi.getCheckOrderByOrderId(id);
        List<OrderItemPojo>orderItemsList = orderApi.getAllOrderItems(id);
        invoiceClientApi.invoiceOrder(orderPojo,getAllOrderItemsOfAgivenOrder(orderItemsList));
    }

    @Transactional(rollbackOn = ApiException.class)
    public void addOrderItem(OrderItemPojo orderItemPojo,String barcode) throws ApiException {
        ProductPojo productPojo= productApi.getCheckProductPojoFromBarcode(barcode);
        orderItemPojo.setProductId(productPojo.getProductId());
        orderItemPojo.setProductName(productPojo.getName());
        checkSufficientInventoryToAddOrderItem(orderItemPojo,productPojo.getMrp());
        orderApi.addOrderItem(orderItemPojo);
    }

    @Transactional
    private void checkSufficientInventoryToAddOrderItem(OrderItemPojo orderItemPojo, Double mrp) throws ApiException {
    InventoryPojo inventoryPojo = inventoryApi.getCheck(orderItemPojo.getProductId());
    if(orderItemPojo.getQuantity()>inventoryPojo.getQuantity()){
        throw new ApiException("Item can't be added to order as it exceeds the inventory. Present inventory count : "+inventoryPojo.getQuantity());
    }

    if(orderItemPojo.getSellingPrice()>mrp){
        throw new ApiException("Item can't be added to order as selling price must be less than MRP. Product's MRP :"+mrp);
    }
    inventoryApi.updateInventory(inventoryPojo,inventoryPojo.getQuantity()-orderItemPojo.getQuantity());
    }


    @Transactional(rollbackOn = ApiException.class)
    public void deleteOrderItem(Integer id) throws ApiException {
        OrderItemPojo ex = orderApi.getCheckOrderItem(id);
        InventoryPojo inventoryPojo = inventoryApi.getCheck(ex.getProductId());
        inventoryApi.updateInventory(inventoryPojo,inventoryPojo.getQuantity()+ ex.getQuantity());
        orderApi.deleteOrderItem(id);
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void updateOrderItem(Integer id, OrderItemPojo orderItemPojo) throws ApiException {
        OrderItemPojo ex = orderApi.getCheckOrderItem(id);
        InventoryPojo inventoryPojo = inventoryApi.getCheck(ex.getProductId());
        ProductPojo productPojo= productApi.getCheckProduct(ex.getProductId());
        if(orderItemPojo.getSellingPrice()>productPojo.getMrp()){
            throw new ApiException("Item can't be updated to order as selling price must be less than MRP. Product's MRP :"+productPojo.getMrp());
        }
        inventoryApi.updateInventory(inventoryPojo,inventoryPojo.getQuantity()-orderItemPojo.getQuantity()+ex.getQuantity());
        orderApi.updateOrderItem(ex,orderItemPojo);
    }


}
