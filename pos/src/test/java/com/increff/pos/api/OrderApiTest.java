package com.increff.pos.api;

import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.CartItemPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.PojoUtil;

import io.swagger.models.auth.In;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.generateRandomString.createRandomOrderCode;
import static org.junit.Assert.assertEquals;

public class OrderApiTest extends AbstractUnitTest {
    @Autowired
    private OrderApi orderApi;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private List<CartItemPojo> createTestCartList(){
        List<CartItemPojo> cartItemPojoList = new ArrayList<>();
        Integer productId=1;
        Integer counterId = 1;
        Double sellingPrice = 120.56;
        Integer quantity =2;
        for(int i=0;i<4;i++){
            CartItemPojo cartItemPojo = PojoUtil.getCartPojo(productId+i,counterId,sellingPrice,quantity);
            cartItemPojoList.add(cartItemPojo);
        }
        return cartItemPojoList;
    }

    private OrderPojo getTestOrderPojo(){
        String customerName="john";
        String customerPhone="9988776655";
        ZonedDateTime orderCreateTime= ZonedDateTime.now();
        ZonedDateTime orderInvoiceTime = ZonedDateTime.now().minusYears(100);
        String status="created";
        Integer counterId=1;
        return PojoUtil.getOrderPojo(customerName,customerPhone,orderCreateTime,orderInvoiceTime,status,counterId);
    }

    private OrderItemPojo getTestOrderItemPojo(){
        Integer orderId = 1;
        Integer productId = 2;
        Integer quantity =10;
        Double sellingPrice = 25.74;
        return PojoUtil.getOrderItemPojo(orderId,productId,quantity,sellingPrice);
    }
    @Test
    public void testAddOrder() throws ApiException {
        OrderPojo orderPojo = getTestOrderPojo();
        orderApi.addOrder(orderPojo,createTestCartList());

        List<OrderPojo> orderPojoList =  orderApi.getAll();
        assertEquals(orderPojoList.size(),1);
        assertEquals(orderPojoList.get(0),orderPojo);

    }


    @Test
    public void testSizeOfOrderList(){

        for(int i=0;i<4;i++){
            OrderPojo orderPojo = getTestOrderPojo();
            orderPojo.setOrderCode(createRandomOrderCode());
            orderDao.insertOrder(orderPojo);
        }
        List<OrderPojo> orderPojoList =  orderApi.getAll();
        assertEquals(orderPojoList.size(),4);
    }

    @Test
    public void testUpdateCustomerDetails() throws ApiException {
        OrderPojo orderPojo = getTestOrderPojo();
        orderPojo.setOrderCode(createRandomOrderCode());
        orderDao.insertOrder(orderPojo);

        String updatedCustomerName = "customer";
        String updatedCustomerPhone = "8976549241";
        OrderPojo updatedOrderPojo = PojoUtil.getOrderPojo(updatedCustomerName,updatedCustomerPhone,orderPojo.getOrderCreateTime(),orderPojo.getOrderInvoiceTime(),orderPojo.getStatus(), orderPojo.getCounterId());
        orderApi.updateCustomerDetails(orderPojo.getOrderId(), updatedOrderPojo);

        List<OrderPojo> orderPojoList =  orderApi.getAll();
        assertEquals(orderPojoList.get(0).getCustomerName(),updatedCustomerName);
        assertEquals(orderPojoList.get(0).getCustomerPhone(),updatedCustomerPhone);
    }

    @Test
    public void testInvoiceOrder() throws ApiException {
        OrderPojo orderPojo = getTestOrderPojo();
        orderPojo.setOrderCode(createRandomOrderCode());
        orderDao.insertOrder(orderPojo);

        orderApi.invoiceOrder(orderPojo.getOrderId());
        List<OrderPojo> orderPojoList =  orderApi.getAll();
        assertEquals(orderPojoList.get(0).getStatus(),"invoiced");
        assertEquals(orderPojoList.get(0).getOrderInvoiceTime().toLocalDate(),ZonedDateTime.now().toLocalDate());

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Invoiced order can't be edited");
        orderApi.updateCustomerDetails(orderPojo.getOrderId(),orderPojo);
    }

    @Test
    public void testInvoiceOrderUniqueness() throws ApiException {
        OrderPojo orderPojo = getTestOrderPojo();
        orderPojo.setOrderCode(createRandomOrderCode());
        orderDao.insertOrder(orderPojo);

        orderApi.invoiceOrder(orderPojo.getOrderId());
        List<OrderPojo> orderPojoList =  orderApi.getAll();
        assertEquals(orderPojoList.get(0).getStatus(),"invoiced");
        assertEquals(orderPojoList.get(0).getOrderInvoiceTime().toLocalDate(),ZonedDateTime.now().toLocalDate());

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("Order is already invoiced");
        orderApi.invoiceOrder(orderPojo.getOrderId());
    }

    @Test
    public void testGetCheckOrder() throws ApiException {
        OrderPojo orderPojo = getTestOrderPojo();
        orderPojo.setOrderCode(createRandomOrderCode());
        orderDao.insertOrder(orderPojo);

        OrderPojo orderPojo1 = orderApi.getCheckOrder(orderPojo.getOrderId());
        assertEquals(orderPojo1,orderPojo);
    }

    @Test
    public void testGetCheckOrderByOrderCode() throws ApiException {
        OrderPojo orderPojo = getTestOrderPojo();
        orderPojo.setOrderCode(createRandomOrderCode());
        orderDao.insertOrder(orderPojo);

        OrderPojo orderPojo1 = orderApi.getCheckOrder(orderPojo.getOrderCode());
        assertEquals(orderPojo1,orderPojo);
    }


    @Test
    public void testSelectOrderByDateFilter() throws ApiException {
        OrderPojo orderPojo = getTestOrderPojo();
        orderPojo.setOrderCode(createRandomOrderCode());
        orderDao.insertOrder(orderPojo);
        orderApi.invoiceOrder(orderPojo.getOrderId());

        OrderPojo orderPojo1 = getTestOrderPojo();
        orderPojo1.setOrderCode(createRandomOrderCode());
        orderDao.insertOrder(orderPojo1);
        orderApi.invoiceOrder(orderPojo1.getOrderId());

        List<OrderPojo> orderPojoList = orderApi.selectOrderByDateFilter(ZonedDateTime.now().minusDays(2),ZonedDateTime.now().plusDays(1));
        assertEquals(orderPojoList.size(),2);
    }


    @Test
    public void testOrderExistenceById() throws ApiException {
        OrderPojo orderPojo = getTestOrderPojo();
        orderPojo.setOrderCode(createRandomOrderCode());
        orderDao.insertOrder(orderPojo);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("No order with given Order Id exists");
        orderApi.getCheckOrder(orderPojo.getOrderId()+1);
    }

    @Test
    public void testOrderExistenceByOrderCode() throws ApiException {
        OrderPojo orderPojo = getTestOrderPojo();
        orderPojo.setOrderCode(createRandomOrderCode());
        orderDao.insertOrder(orderPojo);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("No order with given Order code exists");
        orderApi.getCheckOrder(orderPojo.getOrderCode()+"abc");
    }

    @Test
    public void testGetOrdersByCounterId() throws ApiException {
        OrderPojo orderPojo = getTestOrderPojo();
        orderPojo.setOrderCode(createRandomOrderCode());
        orderApi.addOrder(orderPojo,createTestCartList());

        List<OrderPojo> orderPojoList = orderApi.getAllOrdersByCounterId(1);
        assertEquals(1,orderPojoList.size());
    }

    @Test
    public void testAddOrderItem() throws ApiException {
        orderApi.addOrder(getTestOrderPojo(),createTestCartList());
        Integer orderId  = orderApi.getAll().get(0).getOrderId();

        OrderItemPojo orderItemPojo = getTestOrderItemPojo();
        orderItemPojo.setOrderId(orderId);
        orderApi.addOrderItem(orderItemPojo);

        List<OrderItemPojo> orderItemPojoList = orderApi.getAllOrderItems(orderId);
        assertEquals(1,orderItemPojoList.size());
    }

    @Test
    public void testUpdateOrderItem() throws ApiException {
        orderApi.addOrder(getTestOrderPojo(),createTestCartList());
        Integer orderId  = orderApi.getAll().get(0).getOrderId();

        OrderItemPojo orderItemPojo = getTestOrderItemPojo();
        orderItemPojo.setOrderId(orderId);
        orderApi.addOrderItem(orderItemPojo);

        Integer updatedQuantity = 20;
        Double updatedSellingPrice = 28.99;
        OrderItemPojo updatedOrderItemPojo = PojoUtil.getOrderItemPojo(orderId,2,updatedQuantity,updatedSellingPrice);
        orderApi.updateOrderItem(orderItemPojo,updatedOrderItemPojo);

        List<OrderItemPojo> orderItemPojoList = orderApi.getAllOrderItems(orderId);
        assertEquals(1,orderItemPojoList.size());
        assertEquals(updatedQuantity,orderItemPojoList.get(0).getQuantity());
        assertEquals(updatedSellingPrice,orderItemPojoList.get(0).getSellingPrice());
    }

    @Test
    public void testGetCheckOrderItem() throws ApiException {
        orderApi.addOrder(getTestOrderPojo(),createTestCartList());
        Integer orderId  = orderApi.getAll().get(0).getOrderId();

        OrderItemPojo orderItemPojo = getTestOrderItemPojo();
        orderItemPojo.setOrderId(orderId);
        orderApi.addOrderItem(orderItemPojo);

        OrderItemPojo orderItemPojo1 = orderApi.getCheckOrderItem(orderItemPojo.getOrderItemId());
        assertEquals(orderItemPojo1,orderItemPojo);
    }

    @Test
    public void testDeleteOrderItem() throws ApiException {
        orderApi.addOrder(getTestOrderPojo(),createTestCartList());
        Integer orderId  = orderApi.getAll().get(0).getOrderId();

        OrderItemPojo orderItemPojo = getTestOrderItemPojo();
        orderItemPojo.setOrderId(orderId);
        orderApi.addOrderItem(orderItemPojo);

        orderApi.deleteOrderItem(orderItemPojo.getOrderItemId());
        assertEquals(0,orderApi.getAllOrderItems(orderId).size());
    }

}
