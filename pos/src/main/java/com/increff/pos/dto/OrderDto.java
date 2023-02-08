package com.increff.pos.dto;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.OrderApi;
import com.increff.pos.flow.OrderFlow;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.CustomerDetailsForm;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pojo.OrderPojo;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.increff.pos.helper.dtoHelper.OrderDtoHelper.convert;
import static com.increff.pos.helper.dtoHelper.OrderDtoHelper.getAllOrders;
import static com.increff.pos.helper.dtoHelper.OrderDtoHelper.normalize;
import static com.increff.pos.helper.dtoHelper.OrderItemDtoHelper.convert;
import static com.increff.pos.helper.dtoHelper.OrderItemDtoHelper.normalize;
import static com.increff.pos.util.SecurityUtil.getPrincipal;
import static com.increff.pos.util.ValidateFormUtil.validateForm;

@Service
@Setter
public class OrderDto {
    @Autowired
    private OrderApi orderApi;
    @Autowired
    private OrderFlow orderFlow;

    public void pushToNewOrder(CustomerDetailsForm f) throws ApiException {
        validateForm(f);
        normalize(f);
        orderFlow.addNewOrder(convert(f));
    }

    public OrderData getOrderByOrderCode(String orderCode) throws ApiException {
        OrderPojo p = orderApi.getCheckOrder(orderCode);
        return convert(p);
    }

    public void updateCustomerDetails(@PathVariable Integer id, @RequestBody CustomerDetailsForm f) throws ApiException {
        validateForm(f);
        normalize(f);
        orderApi.updateCustomerDetails(id, convert(f));
    }

    public OrderData getOrderById(Integer id) throws ApiException {
        return convert(orderApi.getCheckOrder(id));
    }

    public List<OrderData> getAllOrdersByCounterId() {
        return getAllOrders(orderApi.getAllOrdersByCounterId(getPrincipal().getId()));
    }

    public List<OrderData> getAll() {
        return getAllOrders(orderApi.getAll());
    }


    public void invoiceOrder(Integer id) throws Exception {
        orderFlow.invoiceOrder(id);
    }

    public void addOrderItem(OrderItemForm f) throws ApiException {
        validateForm(f);
        normalize(f);
        orderFlow.addOrderItem(convert(f), f.getBarcode());
    }

    public void deleteOrderItem(Integer id) throws ApiException {
        orderFlow.deleteOrderItem(id);
    }

    public OrderItemData getOrderItem(Integer id) throws ApiException {
        return orderFlow.getOrderItem(id);
    }

    public void updateOrderItem(Integer id, OrderItemForm f) throws ApiException {
        validateForm(f);
        normalize(f);
        orderFlow.updateOrderItem(id, convert(f));
    }

    public List<OrderItemData> getAllOrderItems(Integer orderId) throws ApiException {
        return orderFlow.getAllOrderItemsOfAnOrder(orderId);
    }


}
