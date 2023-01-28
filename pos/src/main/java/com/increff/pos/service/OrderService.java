package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

import static com.increff.pos.helper.GetCurrentTime.getCurrentDateTime;
import static com.increff.pos.util.SecurityUtil.getPrincipal;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Transactional(rollbackOn = ApiException.class)
    public int addOrder(OrderPojo p) throws ApiException {
        return orderDao.insertOrder(p);
    }

    @Transactional
    public List<OrderPojo> getAllOrdersByCounterId() {
        return orderDao.selectAllOrders(getPrincipal().getId());
    }

    @Transactional
    public List<OrderPojo> getAll() {
        return orderDao.selectAllOrders();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void updateCustomerDetails(int id, OrderPojo p) throws ApiException {
        OrderPojo ex = getCheckOrder(id);
        ex.setCustomerPhone(p.getCustomerPhone());
        ex.setCustomerName(p.getCustomerName());
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void placeOrder(int id) throws ApiException {
        OrderPojo ex = getCheckOrder(id);
        ex.setStatus("invoiced");
        ex.setOrderPlaceTime(getCurrentDateTime());
    }

    @Transactional
    public OrderPojo getCheckOrder(int id) throws ApiException {
        OrderPojo p = orderDao.selectOrder(id);
          if(Objects.isNull(p)){
              throw new ApiException("No order with given id exists");
          }
          return p;
    }
    @Transactional
    public OrderPojo getOrderByOrderCode(String orderCode) throws ApiException {
        return orderDao.selectByOrderCode(orderCode);
    }

    @Transactional
    public List<OrderPojo> selectOrderByDateFilter(String start, String endDate) throws ApiException {
        List<OrderPojo> orderPojoList = orderDao.selectDateFilter(start, endDate);
        if(Objects.isNull(orderPojoList)){
            throw new ApiException("No orders exists between the given dates");
        }
        return orderPojoList;
    }


    @Transactional(rollbackOn = ApiException.class)
    public void addOrderItem(OrderItemPojo p) throws ApiException {
        OrderItemPojo b = orderDao.getOrderItemPojoFromProductId(p.getProductId(),p.getOrderId());
        if(Objects.nonNull(b)){
            b.setQuantity(b.getQuantity()+p.getQuantity());
            b.setSellingPrice(p.getSellingPrice());
        }
        else{
            orderDao.insertOrderItem(p);
        }

    }

    @Transactional(rollbackOn = ApiException.class)
    public void deleteOrderItem(int id) throws ApiException {
        orderDao.deleteOrderItem(id);
    }

    @Transactional
    public List<OrderItemPojo> getAllOrderItems(int id) {
        return orderDao.selectAllOrderItems(id);
    }


    @Transactional(rollbackOn  = ApiException.class)
    public void updateOrderItem(OrderItemPojo ex, OrderItemPojo p) throws ApiException {
        ex.setQuantity(p.getQuantity());
        ex.setSellingPrice(p.getSellingPrice());
    }


    @Transactional
    public OrderItemPojo getCheckOrderItem(int id) throws ApiException {
        OrderItemPojo p = orderDao.selectOrderItem(id);
        if(!Objects.nonNull(p)){
            throw new ApiException("Order item with given id doesn't exist");
        }
        return p;
    }


}