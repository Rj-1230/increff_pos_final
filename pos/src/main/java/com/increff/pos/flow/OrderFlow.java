package com.increff.pos.flow;

import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

import static com.increff.pos.util.generateRandomString.createRandomOrderCode;

@Service
public class OrderFlow {
    @Autowired
    private OrderService orderService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;


    @Transactional(rollbackOn = ApiException.class)
    public int addOrder(OrderPojo orderPojo) throws ApiException {
        String orderCode = createRandomOrderCode();
        OrderPojo x = orderService.getOrderByOrderCode(orderCode);
        while (x != null) {
            orderCode = createRandomOrderCode();
            x = orderService.getOrderByOrderCode(orderCode);
        }
        orderPojo.setOrderCode(orderCode);
        return orderService.addOrder(orderPojo);
    }


    @Transactional(rollbackOn = ApiException.class)
    public void addOrderItem(OrderItemPojo orderItemPojo,String barcode) throws ApiException {
        ProductPojo productPojo= productService.getProductPojoFromBarcode(barcode);
        orderItemPojo.setProductId(productPojo.getProductId());
        orderItemPojo.setProductName(productPojo.getName());

        InventoryPojo a = inventoryService.getCheck(orderItemPojo.getProductId());
        if(orderItemPojo.getQuantity()>a.getQuantity()){
            throw new ApiException("Item can't be added to order as it exceeds the inventory. Present inventory count : "+a.getQuantity());
        }

        if(orderItemPojo.getSellingPrice()>productPojo.getMrp()){
            throw new ApiException("Item can't be added to order as selling price must be less than MRP. Product's MRP :"+productPojo.getMrp());

        }

        inventoryService.updateInventory(a,a.getQuantity()-orderItemPojo.getQuantity());
        orderService.addOrderItem(orderItemPojo);
    }


    @Transactional(rollbackOn = ApiException.class)
    public void deleteOrderItem(int id) throws ApiException {
        OrderItemPojo ex = orderService.getCheckOrderItem(id);
        InventoryPojo a = inventoryService.getCheck(ex.getProductId());
        inventoryService.updateInventory(a,a.getQuantity()+ ex.getQuantity());
        orderService.deleteOrderItem(id);
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void updateOrderItem(int id, OrderItemPojo orderItemPojo) throws ApiException {
        OrderItemPojo ex = orderService.getCheckOrderItem(id);
        InventoryPojo a = inventoryService.getCheck(ex.getProductId());
        ProductPojo productPojo= productService.getCheck(ex.getProductId());
        if(orderItemPojo.getSellingPrice()>productPojo.getMrp()){
            throw new ApiException("Item can't be updated to order as selling price must be less than MRP. Product's MRP :"+productPojo.getMrp());

        }
        inventoryService.updateInventory(a,a.getQuantity()-orderItemPojo.getQuantity()+ex.getQuantity());
        orderService.updateOrderItem(ex,orderItemPojo);
    }


}
