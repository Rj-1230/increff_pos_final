//package com.increff.pos.invoice;
//
//import com.increff.pos.dto.OrderDto;
//import com.increff.pos.model.InvoiceForm;
//import com.increff.pos.model.OrderItemData;
//import com.increff.pos.pojo.OrderItemPojo;
//import com.increff.pos.pojo.OrderPojo;
//import com.increff.pos.service.ApiException;
//import com.increff.pos.service.OrderService;
//import com.increff.pos.service.ProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import java.util.List;
//public class InvoiceGenerator {
//
//    @Autowired
//    OrderService orderService;
//    @Autowired
//    OrderDto orderDto;
//
//    public InvoiceForm generateInvoiceForOrder(OrderPojo orderPojo,List<OrderItemData> orderItemDataList) throws ApiException
//    {
//        InvoiceForm invoiceForm = new InvoiceForm();
//        invoiceForm.setOrderId(orderPojo.getOrderId());
//        invoiceForm.setCustomerName(orderPojo.getCustomerName());
//        invoiceForm.setCustomerPhone(orderPojo.getCustomerPhone());
//        invoiceForm.setInvoiceDate(orderPojo.getOrderInvoiceTime());
//        invoiceForm.setOrderItemList(orderItemDataList);
//        return invoiceForm;
//    }
//}