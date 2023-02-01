package com.increff.pos.dto;

import com.increff.pos.flow.OrderFlow;
import com.increff.pos.model.*;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.increff.pos.helper.NullCheckHelper.*;
import static com.increff.pos.helper.OrderDtoHelper.*;
import static com.increff.pos.helper.OrderItemDtoHelper.*;
import static com.increff.pos.helper.OrderItemDtoHelper.getAllOrderItemsOfAgivenOrder;

@Service

public class OrderDto {
    @Autowired
    private OrderApi orderApi;
    @Autowired
    private OrderFlow orderFlow;
    public void pushToNewOrder(OrderForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        orderFlow.addNewOrder(convert(f));
    }

    public OrderData getOrderByOrderCode(String orderCode) throws ApiException {
        OrderPojo p = orderApi.getCheckOrderByOrderCode(orderCode);
        return convert(p);
    }

    public OrderData getOrderDetails(Integer id) throws ApiException {
        return convert(orderApi.getCheckOrder(id));
    }

    public void updateCustomerDetails(@PathVariable Integer id, @RequestBody OrderForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        orderApi.updateCustomerDetails(id,convert(f));
    }

    public OrderData getOrderById(Integer id) throws ApiException
    {
        return convert(orderApi.getCheckOrder(id));
    }

    public List<OrderData> getAllOrdersByCounterId(){
        return getAllOrders(orderApi.getAllOrdersByCounterId());
    }

    public List<OrderData> getAll(){
        return getAllOrders(orderApi.getAll());
    }


    public void invoiceOrder(Integer id) throws Exception
    {
        orderFlow.invoiceOrder(id);
    }

    public void addOrderItem(OrderItemForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        orderFlow.addOrderItem(convert(f),f.getBarcode());
    }

    public void deleteOrderItem(@PathVariable Integer id) throws ApiException {
        orderFlow.deleteOrderItem(id);
    }

    public OrderItemData getOrderItem(Integer id) throws ApiException {
        return convert(orderApi.getCheckOrderItem(id));
    }

    public void updateOrderItem(@PathVariable Integer id, @RequestBody OrderItemForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        orderFlow.updateOrderItem(id,convert(f));
    }

    public List<OrderItemData> getAllOrderItems(Integer orderId){
        return getAllOrderItemsOfAgivenOrder(orderApi.getAllOrderItems(orderId));
    }


}
