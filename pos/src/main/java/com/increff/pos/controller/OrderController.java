package com.increff.pos.controller;

import com.increff.pos.api.ApiException;
import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.CustomerDetailsForm;
import com.increff.pos.model.form.OrderItemForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@Api
@Setter
public class OrderController {

    private static String PDF_PATH = "./src/main/resources/com/increff/pos/pdf/";
    @Autowired
    private OrderDto orderDto;

    @ApiOperation(value = "Creating a new order and pushing all cart items into an order")
    @RequestMapping(path = "/api/order", method = RequestMethod.POST)
    public void pushToNewOrder(@RequestBody CustomerDetailsForm customerDetailsForm) throws ApiException {
        orderDto.pushToNewOrder(customerDetailsForm);
    }

    @ApiOperation(value = "Select order by id")
    @RequestMapping(path = "/api/order/{orderId}", method = RequestMethod.GET)
    public OrderData getOrder(@PathVariable Integer orderId) throws ApiException {
        return orderDto.getOrderById(orderId);
    }

    @ApiOperation(value = "Updating details of a particular Order")
    @RequestMapping(path = "/api/order/{orderId}", method = RequestMethod.PUT)
    public void updateCustomerDetails(@PathVariable Integer orderId, @RequestBody CustomerDetailsForm customerDetailsForm) throws ApiException {
        orderDto.updateCustomerDetails(orderId, customerDetailsForm);
    }

    @ApiOperation(value = "Getting details of all the orders from a particular counter")
    @RequestMapping(path = "/api/order", method = RequestMethod.GET)
    public List<OrderData> getAllOrdersByCounterId() {
        return orderDto.getAllOrdersByCounterId();
    }

    @ApiOperation(value = "Getting details of all the orders")
    @RequestMapping(path = "/api/supervisor/order", method = RequestMethod.GET)
    public List<OrderData> getAllOrders() {
        return orderDto.getAll();
    }

    @ApiOperation(value = "Invoice an order")
    @RequestMapping(path = "/api/order/invoice/{orderId}", method = RequestMethod.PUT)
    public void markOrderInvoiced(@PathVariable Integer orderId) throws Exception {
        orderDto.invoiceOrder(orderId);
    }

    @ApiOperation(value = "Adding a orderItem")
    @RequestMapping(path = "/api/orderItem", method = RequestMethod.POST)
    public void addOrderItem(@RequestBody OrderItemForm orderItemForm) throws ApiException {
        orderDto.addOrderItem(orderItemForm);
    }


    @ApiOperation(value = "Deleting a orderItem")
    @RequestMapping(path = "/api/orderItem/{orderItemId}", method = RequestMethod.DELETE)
    public void deleteOrderItem(@PathVariable Integer orderItemId) throws ApiException {
        orderDto.deleteOrderItem(orderItemId);
    }

    @ApiOperation(value = "Getting details of a orderItem from order item ID")
    @RequestMapping(path = "/api/orderItem/{orderItemId}", method = RequestMethod.GET)
    public OrderItemData getOrderItem(@PathVariable Integer orderItemId) throws ApiException {
        return orderDto.getOrderItem(orderItemId);
    }

    @ApiOperation(value = "Updating details of a particular OrderItem")
    @RequestMapping(path = "/api/orderItem/{orderItemId}", method = RequestMethod.PUT)
    public void updateOrderItem(@PathVariable Integer orderItemId, @RequestBody OrderItemForm orderItemForm) throws ApiException {
        orderDto.updateOrderItem(orderItemId, orderItemForm);
    }

    @ApiOperation(value = "Getting details of all the order with given orderId")
    @RequestMapping(path = "/api/orderItems/{orderId}", method = RequestMethod.GET)
    public List<OrderItemData> getAllOrderItems(@PathVariable Integer orderId) throws ApiException {
        return orderDto.getAllOrderItems(orderId);
    }


    @ApiOperation(value = "Download Invoice")
    @RequestMapping(path = "/api/invoice/{orderCode}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getInvoicePDF(@PathVariable String orderCode) throws Exception {
        OrderData orderData = orderDto.getOrderByOrderCode(orderCode);
        return getInvoicePDF(orderData);
    }

    private ResponseEntity<byte[]> getInvoicePDF(OrderData   orderData) throws IOException {
        Path pdfPath = Paths.get(PDF_PATH + orderData.getOrderId() + "_invoice.pdf");
        byte[] contents = Files.readAllBytes(pdfPath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = orderData.getOrderId() + "_" + orderData.getCustomerName() + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }


}
