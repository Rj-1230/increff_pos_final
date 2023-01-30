package com.increff.pos.helper;

import com.increff.pos.model.CartData;
import com.increff.pos.pojo.CartPojo;
import com.increff.pos.pojo.OrderItemPojo;

public class CartFlowHelper {
    public static OrderItemPojo convertCartPojoToOrderItemPojo(CartPojo cartPojo,Integer orderId){
        OrderItemPojo orderItemPojo =new OrderItemPojo();
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setProductId(cartPojo.getProductId());
        orderItemPojo.setProductName(cartPojo.getProductName());
        orderItemPojo.setQuantity(cartPojo.getQuantity());
        orderItemPojo.setSellingPrice(cartPojo.getSellingPrice());
        return orderItemPojo;
    }
}
