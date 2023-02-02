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
    public void updateCustomerDetails(Integer id, OrderPojo orderPojo) throws ApiException {
        String status =getCheckOrderByOrderId(id).getStatus();
        if(Objects.equals(status,"invoiced")){
            throw new ApiException("Invoiced order can't be edited");
        }
        OrderPojo ex = getCheckOrderByOrderId(id);
        ex.setCustomerPhone(orderPojo.getCustomerPhone());
        ex.setCustomerName(orderPojo.getCustomerName());
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void invoiceOrder(Integer id) throws ApiException {
        String status =getCheckOrderByOrderId(id).getStatus();
        if(Objects.equals(status,"invoiced")){
            throw new ApiException("Order is already invoiced");
        }
        OrderPojo ex = getCheckOrderByOrderId(id);
        ex.setStatus("invoiced");
        ex.setOrderInvoiceTime(ZonedDateTime.now());
    }

    @Transactional(rollbackOn  = ApiException.class)
    public OrderPojo getCheckOrderByOrderId(Integer orderId) throws ApiException {
        OrderPojo orderPojo = orderDao.selectOrder(orderId);
          if(Objects.isNull(orderPojo)){
              throw new ApiException("No order with given Order Id exists");
          }
          return orderPojo;
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
    public void addOrderItem(OrderItemPojo orderItemPojo) throws ApiException {
        String status =getCheckOrderByOrderId(orderItemPojo.getOrderId()).getStatus();
        if(Objects.equals(status,"invoiced")){
            throw new ApiException("Invoiced order can't be edited");
        }
        OrderItemPojo b = orderDao.getOrderItemPojoFromProductId(orderItemPojo.getProductId(),orderItemPojo.getOrderId());
        if(Objects.nonNull(b)){
            b.setQuantity(b.getQuantity()+orderItemPojo.getQuantity());
            b.setSellingPrice(orderItemPojo.getSellingPrice());
        }
        else{
            orderDao.insertOrderItem(orderItemPojo);
        }

    }

    @Transactional(rollbackOn = ApiException.class)
    public void deleteOrderItem(Integer id) throws ApiException {
        String status =getCheckOrderByOrderId(getCheckOrderItem(id).getOrderId()).getStatus();
        if(Objects.equals(status,"invoiced")){
            throw new ApiException("Invoiced order can't be deleted");
        }
        orderDao.deleteOrderItem(id);
    }

    @Transactional
    public List<OrderItemPojo> getAllOrderItems(Integer id) {
        return orderDao.selectAllOrderItems(id);
    }


    @Transactional
    public void updateOrderItem(OrderItemPojo ex, OrderItemPojo orderItemPojo) throws ApiException {
        String status =getCheckOrderByOrderId(ex.getOrderId()).getStatus();
        if(Objects.equals(status,"invoiced")){
            throw new ApiException("Invoiced order can't be edited");
        }
        ex.setQuantity(orderItemPojo.getQuantity());
        ex.setSellingPrice(orderItemPojo.getSellingPrice());
    }


    @Transactional(rollbackOn  = ApiException.class)
    public OrderItemPojo getCheckOrderItem(Integer id) throws ApiException {
        OrderItemPojo orderItemPojo = orderDao.selectOrderItem(id);
        if(Objects.isNull(orderItemPojo)){
            throw new ApiException("Order item with given order Item Id doesn't exist");
        }
        return orderItemPojo;
    }


}