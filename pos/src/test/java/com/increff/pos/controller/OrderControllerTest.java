package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.InvoiceClientApi;
import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.dao.*;
import com.increff.pos.dto.OrderDto;
import com.increff.pos.flow.OrderFlow;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.CustomerDetailsForm;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.CartItemPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.FormUtil;
import com.increff.pos.util.PojoUtil;
import com.increff.pos.util.UserPrincipal;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static com.increff.pos.util.DummyForm.getDummyCustomerDetailsForm;
import static com.increff.pos.util.DummyForm.getDummyOrderItemForm;
import static org.junit.Assert.*;

public class OrderControllerTest extends AbstractUnitTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Autowired
    private OrderController orderController;
    @Autowired
    private OrderDto orderDto;
    @Autowired
    private OrderFlow orderFlow;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private CartItemDao cartItemDao;
    @Autowired
    private OrderDao orderDao;

    @Before
    public void setup() throws Exception {
        //Authentication mocking
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setRole("supervisor");
        userPrincipal.setId(1);
        userPrincipal.setEmail("supervisor@increff.com");
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userPrincipal);
        SecurityContextHolder.setContext(securityContext);

        //Adding brand
        String brand = "brand";
        String category = "category";
        BrandPojo brandPojo = PojoUtil.getBrandPojo(brand, category);
        brandDao.insert(brandPojo);

        //Adding a product
        Integer brandId = brandDao.selectAll().get(0).getBrandId();
        String barcode = "barcode";
        String productName = "prod1";
        Double mrp = 1240.54;
        ProductPojo productPojo = PojoUtil.getProductPojo(brandId, barcode, mrp, productName);
        productDao.insert(productPojo);

        ProductPojo productPojo1 = PojoUtil.getProductPojo(brandId, "barcode1", 1200.65, "prod2");
        productDao.insert(productPojo1);

        //Adding inventory corresponding to above product since it was added in flow layer
        Integer productId = productDao.selectAll().get(0).getProductId();
        Integer productId1 = productDao.selectAll().get(1).getProductId();
        Integer quantity = 100;
        InventoryPojo inventoryPojo = PojoUtil.getInventoryPojo(productId, quantity);
        InventoryPojo inventoryPojo1 = PojoUtil.getInventoryPojo(productId1, quantity);
        inventoryDao.insert(inventoryPojo);
        inventoryDao.insert(inventoryPojo1);

        //Add some items in cart
        Integer cartQuantity = 5;
        Integer counterId = 1;
        Double sellingPrice = 1000.95;
        CartItemPojo cartItemPojo = PojoUtil.getCartPojo(productId, counterId, sellingPrice, cartQuantity);
        CartItemPojo cartItemPojo1 = PojoUtil.getCartPojo(productId1, counterId, sellingPrice, cartQuantity);
        cartItemDao.insert(cartItemPojo);
        cartItemDao.insert(cartItemPojo1);

        InvoiceClientApi invoiceClientApi = Mockito.mock(InvoiceClientApi.class);
        Mockito.when(invoiceClientApi.invoiceOrder(Mockito.any(), Mockito.anyList())).thenReturn(new byte[12]);
        orderFlow.setInvoiceClientApi(invoiceClientApi);
        orderDto.setOrderFlow(orderFlow);
        orderController.setOrderDto(orderDto);
    }

    @Test
    public void testAddOrder() throws ApiException {
        orderController.pushToNewOrder(getDummyCustomerDetailsForm());
        List<OrderData> orderDataList = orderController.getAllOrders();

        assertEquals(1, orderDataList.size());
        OrderData orderData = orderDataList.get(0);
        assertNull(orderData.getOrderInvoiceTime());
        assertNotNull(orderData.getOrderCreateTime());
        assertEquals("created", orderData.getStatus());
        assertEquals(new Integer(1), orderData.getCounterId());
        assertEquals(36, orderData.getOrderCode().length());
        assertEquals("customer", orderData.getCustomerName());
        assertEquals("9876543210", orderData.getCustomerPhone());
    }

    @Test
    public void testGetOrder() throws ApiException {
        orderController.pushToNewOrder(getDummyCustomerDetailsForm());
        OrderData orderData = orderController.getAllOrders().get(0);
        OrderData orderData1 = orderController.getOrder(orderDao.selectAllOrders().get(0).getOrderId());
        assertEquals(orderData1.getOrderCode(), orderData.getOrderCode());
        assertEquals(orderData1.getOrderId(), orderData.getOrderId());
        assertEquals(orderData1.getStatus(), orderData.getStatus());
        assertEquals(orderData1.getOrderCreateTime(), orderData.getOrderCreateTime());
        assertEquals(orderData1.getOrderInvoiceTime(), orderData.getOrderInvoiceTime());
        assertEquals(orderData1.getCustomerName(), orderData.getCustomerName());
        assertEquals(orderData1.getCustomerPhone(), orderData.getCustomerPhone());
    }

    @Test
    public void testUpdateCustomerDetails() throws ApiException {
        orderController.pushToNewOrder(getDummyCustomerDetailsForm());
        CustomerDetailsForm updatedCustomerDetailsForm = FormUtil.getCustomerDetailsForm("test_customer", "1234567899");

        orderController.updateCustomerDetails(orderController.getAllOrders().get(0).getOrderId(), updatedCustomerDetailsForm);
        OrderData orderData = orderController.getAllOrders().get(0);
        assertEquals("test_customer", orderData.getCustomerName());
        assertEquals("1234567899", orderData.getCustomerPhone());
    }

    @Test
    public void testGetAllOrdersByCounterId() throws ApiException {
        orderController.pushToNewOrder(getDummyCustomerDetailsForm());
        List<OrderData> orderDataList = orderController.getAllOrdersByCounterId();
        assertEquals(1, orderDataList.size());
    }


    @Test
    public void testInvoiceOrder() throws Exception {
        orderController.pushToNewOrder(getDummyCustomerDetailsForm());
        orderController.markOrderInvoiced(orderController.getAllOrders().get(0).getOrderId());
        List<OrderData> orderDataList = orderController.getAllOrders();
        assertEquals(1, orderDataList.size());
        assertEquals("invoiced", orderDataList.get(0).getStatus());
    }


    @Test
    public void testAddOrderItem() throws ApiException {
        orderController.pushToNewOrder(getDummyCustomerDetailsForm());

        Integer orderId = orderController.getAllOrders().get(0).getOrderId();
        OrderItemForm orderItemForm = getDummyOrderItemForm();
        orderItemForm.setOrderId(orderId);
        orderController.addOrderItem(orderItemForm);

        List<OrderItemData> orderItemDataList = orderController.getAllOrderItems(orderId);
        assertEquals(2, orderItemDataList.size());
        assertEquals(new Integer(10), orderItemDataList.get(0).getQuantity());
        assertEquals(new Integer(5), orderItemDataList.get(1).getQuantity());
    }


    @Test
    public void testDeleteOrderItem() throws ApiException {
        orderController.pushToNewOrder(getDummyCustomerDetailsForm());
        List<OrderItemData> orderItemDataList = orderController.getAllOrderItems(orderController.getAllOrders().get(0).getOrderId());
        orderController.deleteOrderItem(orderItemDataList.get(0).getOrderItemId());
        assertEquals(2, orderItemDataList.size());
        orderItemDataList = orderController.getAllOrderItems(orderController.getAllOrders().get(0).getOrderId());
        assertEquals(1, orderItemDataList.size());
    }

    @Test
    public void testGetOrderItem() throws ApiException {
        orderController.pushToNewOrder(getDummyCustomerDetailsForm());
        OrderItemData orderItemData = orderController.getAllOrderItems(orderController.getAllOrders().get(0).getOrderId()).get(0);
        OrderItemData orderItemData1 = orderController.getOrderItem(orderItemData.getOrderItemId());
        assertEquals(orderItemData1.getOrderId(), orderItemData.getOrderId());
        assertEquals(orderItemData1.getSellingPrice(), orderItemData.getSellingPrice());
        assertEquals(orderItemData1.getProductName(), orderItemData.getProductName());
        assertEquals(orderItemData1.getQuantity(), orderItemData.getQuantity());
        assertEquals(orderItemData1.getProductId(), orderItemData.getProductId());
        assertEquals(orderItemData1.getOrderItemId(), orderItemData.getOrderItemId());
    }


    @Test
    public void testUpdateOrderItem() throws ApiException {
        orderController.pushToNewOrder(getDummyCustomerDetailsForm());

        OrderItemForm updatedOrderItemForm = FormUtil.getOrderItemForm("barcode", 2, 1000.77);
        orderController.updateOrderItem(orderController.getAllOrderItems(orderController.getAllOrders().get(0).getOrderId()).get(0).getOrderItemId(), updatedOrderItemForm);

        OrderItemData orderItemData = orderController.getAllOrderItems(orderController.getAllOrders().get(0).getOrderId()).get(0);
        assertEquals(new Integer(2), orderItemData.getQuantity());
        assertEquals(new Double(1000.77), orderItemData.getSellingPrice());
    }

    @Test
    public void testCustomerNameNonNullable() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The customer name field can't be null");
        orderController.pushToNewOrder(FormUtil.getCustomerDetailsForm(null,"1234567899"));
    }
    @Test
    public void testCustomerPhoneNonNullable() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The customer phone field can't be null");
        orderController.pushToNewOrder(FormUtil.getCustomerDetailsForm("customer",null));
    }

    @Test
    public void testCustomerNameNonEmpty() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The customer name can't be empty");
        orderController.pushToNewOrder(FormUtil.getCustomerDetailsForm("    ","1234567899"));
    }
    @Test
    public void testCustomerPhoneNonEmpty() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The customer phone number must contain 10 digits");
        orderController.pushToNewOrder(FormUtil.getCustomerDetailsForm("customer","    "));
    }
    @Test
    public void testCustomerPhoneValid() throws ApiException {
        exceptionRule.expect(ApiException.class);
        exceptionRule.expectMessage("The customer phone must be a valid 10 digit number");
        orderController.pushToNewOrder(FormUtil.getCustomerDetailsForm("customer","123456789q"));
    }
}
