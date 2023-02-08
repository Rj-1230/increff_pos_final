package com.increff.pos.api;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.CartItemPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import static com.increff.pos.util.generateRandomString.createRandomOrderCode;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderApi {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;

    public int addOrder(OrderPojo orderPojo) throws ApiException {
        orderPojo.setOrderCode(createRandomOrderCode());
        return orderDao.insertOrder(orderPojo);

    }

    public List<OrderPojo> getAllOrdersByCounterId(Integer id) {
        return orderDao.selectAllOrdersByCounterId(id);
    }

    public List<OrderPojo> getAll() {
        return orderDao.selectAllOrders();
    }

    public void updateCustomerDetails(Integer id, OrderPojo orderPojo) throws ApiException {
        String status = getCheckOrder(id).getStatus();
        if (Objects.equals(status, "invoiced")) {
            throw new ApiException("Invoiced order can't be edited");
        }
        OrderPojo ex = getCheckOrder(id);
        ex.setCustomerPhone(orderPojo.getCustomerPhone());
        ex.setCustomerName(orderPojo.getCustomerName());
    }

    public void invoiceOrder(Integer id) throws ApiException {
        String status = getCheckOrder(id).getStatus();
        if (Objects.equals(status, "invoiced")) {
            throw new ApiException("Order is already invoiced");
        }
        OrderPojo ex = getCheckOrder(id);
        ex.setStatus("invoiced");
        ex.setOrderInvoiceTime(ZonedDateTime.now());
    }

    public OrderPojo getCheckOrder(Integer orderId) throws ApiException {
        OrderPojo orderPojo = orderDao.selectOrder(orderId);
        if (Objects.isNull(orderPojo)) {
            throw new ApiException("No order with given Order Id exists");
        }
        return orderPojo;
    }

    public OrderPojo getCheckOrder(String orderCode) throws ApiException {
        OrderPojo orderPojo = orderDao.selectOrderByOrderCode(orderCode);
        if (Objects.isNull(orderPojo)) {
            throw new ApiException("No order with given Order code exists");
        }
        return orderPojo;
    }

    public List<OrderPojo> selectOrderByDateFilter(ZonedDateTime start, ZonedDateTime end) {
        return orderDao.selectOrderBetweenDateRange(start, end);
    }


    public void addOrderItem(OrderItemPojo orderItemPojo) throws ApiException {
        String status = getCheckOrder(orderItemPojo.getOrderId()).getStatus();
        if (Objects.equals(status, "invoiced")) {
            throw new ApiException("Invoiced order can't be edited");
        }
        OrderItemPojo b = orderItemDao.getOrderItemPojoByProductIdAndOrderId(orderItemPojo.getProductId(), orderItemPojo.getOrderId());
        if (Objects.nonNull(b)) {
            b.setQuantity(b.getQuantity() + orderItemPojo.getQuantity());
            b.setSellingPrice(orderItemPojo.getSellingPrice());
        } else {
            orderItemDao.insert(orderItemPojo);
        }
    }

    public void deleteOrderItem(Integer id) throws ApiException {
        getCheckOrderItem(id);
        String status = getCheckOrder(getCheckOrderItem(id).getOrderId()).getStatus();
        if (Objects.equals(status, "invoiced")) {
            throw new ApiException("Invoiced order can't be deleted");
        }
        orderItemDao.deleteOrderItem(id);
    }


    public List<OrderItemPojo> getAllOrderItems(Integer id) {
        return orderItemDao.selectAllOrderItemsByOrderId(id);
    }


    public void updateOrderItem(OrderItemPojo ex, OrderItemPojo orderItemPojo) throws ApiException {
        String status = getCheckOrder(ex.getOrderId()).getStatus();
        if (Objects.equals(status, "invoiced")) {
            throw new ApiException("Invoiced order can't be edited");
        }
        ex.setQuantity(orderItemPojo.getQuantity());
        ex.setSellingPrice(orderItemPojo.getSellingPrice());
    }

    public OrderItemPojo getCheckOrderItem(Integer id) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemDao.selectOrderItem(id);
        if (Objects.isNull(orderItemPojo)) {
            throw new ApiException("Order item with given order Item Id doesn't exist");
        }
        return orderItemPojo;
    }


}