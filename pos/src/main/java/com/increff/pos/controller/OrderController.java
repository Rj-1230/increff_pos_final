package com.increff.pos.controller;

import com.increff.pos.invoice.InvoiceGenerator;
import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.*;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@Api

public class OrderController {

    @Autowired
    private OrderDto orderDto;
    @Autowired
    private InvoiceGenerator invoiceGenerator;

    @ApiOperation(value = "Select order by id")
    @RequestMapping(path = "/api/order/{orderId}", method = RequestMethod.GET)
    public OrderData getOrder(@PathVariable Integer orderId) throws ApiException {
        return orderDto.getOrderById(orderId);
    }
    @ApiOperation(value = "Updating details of a particular Order")
    @RequestMapping(path = "/api/order/{id}", method = RequestMethod.PUT)
    public void updateOrder(@PathVariable int id, @RequestBody OrderForm f) throws ApiException {
        orderDto.updateOrder(id, f);
    }

    @ApiOperation(value = "Getting details of all the orders")
    @RequestMapping(path = "/api/order", method = RequestMethod.GET)
    public List<OrderData> getAllOrdersByCounterId() {
        return orderDto.getAllOrdersByCounterId();
    }
    @ApiOperation(value = "Getting details of all the orders")
    @RequestMapping(path = "/api/supervisor/order", method = RequestMethod.GET)
    public List<OrderData> getAllOrders() {
        return orderDto.getAll();
    }

    @ApiOperation(value = "Mark order placed")
    @RequestMapping(path = "api/order/place/{orderId}", method = RequestMethod.PUT)
    public void markOrderPlaced(@PathVariable int orderId) throws ApiException, IOException {
        orderDto.placeOrder(orderId);

        OrderData orderData = orderDto.getOrderDetails(orderId);
        InvoiceForm invoiceForm = invoiceGenerator.generateInvoiceForOrder(orderData.getOrderCode());
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/invoice_app/api/invoice";
        byte[] contents = restTemplate.postForEntity(url, invoiceForm, byte[].class).getBody();
        Path pdfPath = Paths.get("./src/main/resources/pdf/" + orderId + "_invoice.pdf");
        Files.write(pdfPath, contents);
    }

    @ApiOperation(value="Adding a orderItem")
    @RequestMapping(path="/api/orderItem", method = RequestMethod.POST)
    public void add(@RequestBody OrderItemForm f)throws ApiException{
        orderDto.addOrderItem(f);
    }


    @ApiOperation(value="Deleting a orderItem")
    @RequestMapping(path="/api/orderItem/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException {
        orderDto.deleteOrderItem(id);
    }

    @ApiOperation(value="Getting details of a orderItem from order item ID")
    @RequestMapping(path="/api/orderItem/{id}", method = RequestMethod.GET)
    public OrderItemData get(@PathVariable int id) throws ApiException {
        return orderDto.getOrderItem(id);
    }

    @ApiOperation(value="Updating details of a particular OrderItem")
    @RequestMapping(path="/api/orderItem/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody OrderItemForm f) throws ApiException {
        orderDto.updateOrderItem(id,f);
    }

    @ApiOperation(value="Getting details of all the order with given order id")
    @RequestMapping(path="/api/orderItems/{id}", method = RequestMethod.GET)
    public List<OrderItemData> getAll(@PathVariable int id){
        return orderDto.getAllOrderItems(id);
    }


    @ApiOperation(value = "Download Invoice")
    @RequestMapping(path = "/api/invoice/{orderCode}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getPDF(@PathVariable String orderCode) throws Exception {

        OrderData orderData = orderDto.getOrderByOrderCode(orderCode);
        System.out.println("helloo");

        Path pdfPath = Paths.get("./src/main/resources/pdf/" + orderData.getOrderId() + "_invoice.pdf");

        byte[] contents = Files.readAllBytes(pdfPath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String filename = orderData.getOrderId()+"_"+orderData.getCustomerName() + ".pdf";
        headers.setContentDispositionFormData(filename, filename);

        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }


}
