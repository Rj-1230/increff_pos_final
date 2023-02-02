package com.increff.pos.api;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.CartPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.PojoUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.util.generateRandomString.createRandomOrderCode;
import static org.junit.Assert.assertEquals;

public class OrderApiTest extends AbstractUnitTest{
    @Autowired
    private OrderApi orderApi;
    @Autowired
    private OrderDao orderDao;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private List<CartPojo> createTestCartList(){
        List<CartPojo> cartPojoList = new ArrayList<>();
        Integer productId=1;
        Integer counterId = 1;
        String productName = "prod1";
        Double sellingPrice = 120.56;
        Integer quantity =2;
        for(int i=0;i<4;i++){
            CartPojo cartPojo = PojoUtil.getCartPojo(productId+i,counterId,productName,sellingPrice,quantity);
            cartPojoList.add(cartPojo);
        }
        return  cartPojoList;
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
    }


    @Test
    public void testGetCheckOrder() throws ApiException {
        OrderPojo orderPojo = getTestOrderPojo();
        orderPojo.setOrderCode(createRandomOrderCode());
        orderDao.insertOrder(orderPojo);

        OrderPojo orderPojo1 = orderApi.getCheckOrderByOrderId(orderPojo.getOrderId());
        assertEquals(orderPojo1,orderPojo);
    }

    @Test
    public void testGetCheckOrderByOrderCode() throws ApiException {
        OrderPojo orderPojo = getTestOrderPojo();
        orderPojo.setOrderCode(createRandomOrderCode());
        orderDao.insertOrder(orderPojo);

        OrderPojo orderPojo1 = orderApi.getCheckOrderByOrderCode(orderPojo.getOrderCode());
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
        orderApi.getCheckOrderByOrderId(orderPojo.getOrderId()+1);
    }

    @Test
    public void testOrderExistenceByOrderCode() throws ApiException {
        OrderPojo orderPojo = getTestOrderPojo();
        orderPojo.setOrderCode(createRandomOrderCode());
        orderDao.insertOrder(orderPojo);

        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("No order with given order code exists");
        orderApi.getCheckOrderByOrderCode(orderPojo.getOrderCode()+"abc");
    }

}
