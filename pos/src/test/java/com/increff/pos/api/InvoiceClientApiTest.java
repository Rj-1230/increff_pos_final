//package com.increff.pos.api;
//
//import com.increff.pos.config.AbstractUnitTest;
//import com.increff.pos.dao.BrandDao;
//import com.increff.pos.model.data.OrderData;
//import com.increff.pos.model.data.OrderItemData;
//import com.increff.pos.pojo.BrandPojo;
//import com.increff.pos.util.PojoUtil;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
//public class InvoiceClientApiTest extends AbstractUnitTest {
//    @Autowired
//    private InvoiceClientApi invoiceClientApi;
//    @Rule
//    public ExpectedException exceptionRule = ExpectedException.none();
//
//    @Test
//    public void testInvoiceOrder() throws Exception {
//        OrderData orderData = createTestOrderData();
//        List<OrderItemData> orderItemDataList = createOrderitemsList();
//        invoiceClientApi.invoiceOrder(orderData,orderItemDataList);
//    }
//
//    private void createTestOrderData(){
//        OrderData orderData = new OrderData();
//        orderData.setOrderCreateTime(Zo);
//    }
//}
