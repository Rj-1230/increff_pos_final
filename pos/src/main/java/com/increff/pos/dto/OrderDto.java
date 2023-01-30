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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public OrderData getOrderDetails(Integer id) throws ApiException {
        return convert(orderService.getCheckOrder(id));
    }

    public void updateCustomerDetails(@PathVariable Integer id, @RequestBody OrderForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        orderService.updateCustomerDetails(id,convert(f));
    }

    public OrderData getOrderById(Integer id) throws ApiException
    {
        return convert(orderService.getCheckOrder(id));
    }

    public List<OrderData> getAllOrdersByCounterId(){
        return getAllOrders(orderService.getAllOrdersByCounterId());
    }

    public List<OrderData> getAll(){
        return getAllOrders(orderService.getAll());
    }


    public void invoiceOrder(Integer id) throws ApiException
    {
        orderService.invoiceOrder(id);
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
        return convert(orderService.getCheckOrderItem(id));
    }

    public void updateOrderItem(@PathVariable Integer id, @RequestBody OrderItemForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        orderFlow.updateOrderItem(id,convert(f));
    }

    public List<OrderItemData> getAllOrderItems(Integer orderId){
        return getAllOrderItemsOfAgivenOrder(orderService.getAllOrderItems(orderId));
    }

    public List<OrderPojo> selectOrderByDateFilter(String start, String end) throws ApiException {
        DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        start += " 00:00:00";
        end += " 23:59:59";
        ZoneId timeZone = ZoneId.systemDefault();
        ZonedDateTime startDate = LocalDateTime.parse(start, formatter).atZone(timeZone);
        ZonedDateTime endDate = LocalDateTime.parse(end, formatter).atZone(timeZone);
        return orderService.selectOrderByDateFilter(startDate,endDate);
    }

    public void pushToNewOrder(OrderForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        orderFlow.addNewOrder(convert(f));
    }


}
