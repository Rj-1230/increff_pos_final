package com.increff.pos.api;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.CartPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import static com.increff.pos.helper.CartFlowHelper.convertCartPojoToOrderItemPojo;
import static com.increff.pos.util.SecurityUtil.getPrincipal;
import static com.increff.pos.util.generateRandomString.createRandomOrderCode;

@Service
public class OrderApi {

    @Autowired
    private OrderDao orderDao;

    @Transactional(rollbackOn = ApiException.class)
    public void addOrder(OrderPojo orderPojo,List<CartPojo>cartPojoList) throws ApiException {
        orderPojo.setOrderCode(createRandomOrderCode());
        Integer orderId = orderDao.insertOrder(orderPojo);
        for(CartPojo cartPojo : cartPojoList){
            OrderItemPojo orderItemPojo = convertCartPojoToOrderItemPojo(cartPojo,orderId);
            addOrderItem(orderItemPojo);
        }
    }

    public List<OrderPojo> getAllOrdersByCounterId() {
        return orderDao.selectAllOrders(getPrincipal().getId());
    }

    public List<OrderPojo> getAll() {
        return orderDao.selectAllOrders();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void updateCustomerDetails(Integer id, OrderPojo p) throws ApiException {
        OrderPojo ex = getCheckOrder(id);
        ex.setCustomerPhone(p.getCustomerPhone());
        ex.setCustomerName(p.getCustomerName());
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void invoiceOrder(Integer id) throws ApiException {
        OrderPojo ex = getCheckOrder(id);
        ex.setStatus("invoiced");
        ex.setOrderInvoiceTime(ZonedDateTime.now());
    }

    @Transactional(rollbackOn  = ApiException.class)
    public OrderPojo getCheckOrder(Integer id) throws ApiException {
        OrderPojo p = orderDao.selectOrder(id);
          if(Objects.isNull(p)){
              throw new ApiException("No order with given Order Id exists");
          }
          return p;
    }
    @Transactional(rollbackOn  = ApiException.class)
    public OrderPojo getCheckOrderByOrderCode(String orderCode) throws ApiException {
        OrderPojo orderPojo = orderDao.selectByOrderCode(orderCode);
        if(Objects.isNull(orderPojo)){
            throw new ApiException("No order with given order code exists");
        }
        return orderPojo;
    }

    public List<OrderPojo> selectOrderByDateFilter(ZonedDateTime start, ZonedDateTime end){
        return orderDao.selectDateFilter(start, end);
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
    public void deleteOrderItem(Integer id) throws ApiException {
        orderDao.deleteOrderItem(id);
    }

    @Transactional
    public List<OrderItemPojo> getAllOrderItems(Integer id) {
        return orderDao.selectAllOrderItems(id);
    }


    @Transactional(rollbackOn  = ApiException.class)
    public void updateOrderItem(OrderItemPojo ex, OrderItemPojo p) throws ApiException {

        ex.setQuantity(p.getQuantity());
        ex.setSellingPrice(p.getSellingPrice());
    }


    @Transactional
    public OrderItemPojo getCheckOrderItem(Integer id) throws ApiException {
        OrderItemPojo p = orderDao.selectOrderItem(id);
        if(!Objects.nonNull(p)){
            throw new ApiException("Order item with given id doesn't exist");
        }
        return p;
    }


}