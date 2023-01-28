package com.increff.pos.dto;

import com.increff.pos.flow.OrderFlow;
import com.increff.pos.model.*;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.helper.GetCurrentTime.getCurrentDateTime;
import static com.increff.pos.helper.NullCheckHelper.*;
import static com.increff.pos.helper.OrderDtoHelper.*;
import static com.increff.pos.helper.OrderItemDtoHelper.*;
import static com.increff.pos.helper.OrderItemDtoHelper.getAllOrderItemsOfAgivenOrder;

@Service

public class OrderDto {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderFlow orderFlow;

    public OrderData getOrderByOrderCode(String orderCode) throws ApiException {
        OrderPojo p = orderService.getOrderByOrderCode(orderCode);
        return convert(p);
    }

    public OrderData getOrderDetails(int id) throws ApiException {
        return convert(orderService.getCheckOrder(id));
    }

    public void updateOrder(@PathVariable int id, @RequestBody OrderForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        orderService.updateCustomerDetails(id,convert(f));
    }

    public OrderData getOrderById(int id) throws ApiException
    {
        return convert(orderService.getCheckOrder(id));
    }

    public List<OrderData> getAllOrdersByCounterId(){
        return getAllOrders(orderService.getAllOrdersByCounterId());
    }

    public List<OrderData> getAll(){
        return getAllOrders(orderService.getAll());
    }


    public void placeOrder(int id) throws ApiException
    {
        orderService.placeOrder(id);
    }

    public void addOrderItem(OrderItemForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        orderFlow.addOrderItem(convert(f),f.getBarcode());
    }

    public void deleteOrderItem(@PathVariable int id) throws ApiException {
        orderFlow.deleteOrderItem(id);
    }

    public OrderItemData getOrderItem(int id) throws ApiException {
        return convert(orderService.getCheckOrderItem(id));
    }

    public void updateOrderItem(@PathVariable int id, @RequestBody OrderItemForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        orderFlow.updateOrderItem(id,convert(f));
    }

    public List<OrderItemData> getAllOrderItems(int orderId){
        return getAllOrderItemsOfAgivenOrder(orderService.getAllOrderItems(orderId));
    }



}
