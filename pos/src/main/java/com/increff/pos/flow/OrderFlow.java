package com.increff.pos.flow;

import com.increff.pos.api.*;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.pojo.*;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.increff.pos.helper.dtoHelper.OrderDtoHelper.convert;
import static com.increff.pos.helper.flowHelper.CartItemFlowHelper.convertCartPojoToOrderItemPojo;
import static com.increff.pos.helper.flowHelper.OrderFlowHelper.convertOrderItemPojoToOrderItemData;
import static com.increff.pos.util.SecurityUtil.getPrincipal;

@Service
@Setter
public class OrderFlow {
    private static String PDF_PATH = "./src/main/resources/com/increff/pos/pdf/";
    @Autowired
    private OrderApi orderApi;
    @Autowired
    private InventoryApi inventoryApi;
    @Autowired
    private InvoiceClientApi invoiceClientApi;
    @Autowired
    private ProductApi productApi;
    @Autowired
    private CartItemApi cartItemApi;

    @Transactional(rollbackOn = ApiException.class)
    public void addNewOrder(OrderPojo orderPojo) throws ApiException {
        List<CartItemPojo> cartItemPojoList = cartItemApi.getAllItemsInCart(getPrincipal().getId());
        if (cartItemPojoList.size() == 0) {
            throw new ApiException("The order can't be created as the cart is empty");
        }
        checkSufficientInventoryToCreateOrder(cartItemPojoList);
        Integer orderId = orderApi.addOrder(orderPojo);
        createNewOrderByAddingItemsToTheOrder(orderId, cartItemPojoList);
        cartItemApi.deleteAll(cartItemPojoList);
    }

    @Transactional(rollbackOn = Exception.class)
    public void invoiceOrder(Integer orderId) throws Exception {
        orderApi.invoiceOrder(orderId);
        createInvoiceForOrder(orderId);
    }


    @Transactional(rollbackOn = ApiException.class)
    public void addOrderItem(OrderItemPojo orderItemPojo, String barcode) throws ApiException {
        ProductPojo productPojo = productApi.getCheckProduct(barcode);
        orderItemPojo.setProductId(productPojo.getProductId());
        checkSufficientInventoryToAddOrderItem(orderItemPojo, productPojo.getMrp());
        orderApi.addOrderItem(orderItemPojo);
    }

    @Transactional(rollbackOn = ApiException.class)
    public void deleteOrderItem(Integer id) throws ApiException {
        OrderItemPojo ex = orderApi.getCheckOrderItem(id);
        InventoryPojo inventoryPojo = inventoryApi.getCheckByProductId(ex.getProductId());
        inventoryApi.updateInventory(inventoryPojo, inventoryPojo.getQuantity() + ex.getQuantity());
        orderApi.deleteOrderItem(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public OrderItemData getOrderItem(Integer id) throws ApiException {
        OrderItemPojo orderItemPojo = orderApi.getCheckOrderItem(id);
        String productName = productApi.getCheckProduct(orderItemPojo.getProductId()).getName();
        String barcode = productApi.getCheckProduct(orderItemPojo.getProductId()).getBarcode();
        return convertOrderItemPojoToOrderItemData(orderItemPojo, productName, barcode);
    }

    @Transactional(rollbackOn = ApiException.class)
    public void updateOrderItem(Integer id, OrderItemPojo orderItemPojo) throws ApiException {
        OrderItemPojo ex = orderApi.getCheckOrderItem(id);
        InventoryPojo inventoryPojo = inventoryApi.getCheckByProductId(ex.getProductId());
        ProductPojo productPojo = productApi.getCheckProduct(ex.getProductId());
        if (orderItemPojo.getSellingPrice() > productPojo.getMrp()) {
            throw new ApiException("Item can't be updated to order as selling price must be less than MRP. Product's MRP :" + productPojo.getMrp());
        }
        inventoryApi.updateInventory(inventoryPojo, inventoryPojo.getQuantity() - orderItemPojo.getQuantity() + ex.getQuantity());
        orderApi.updateOrderItem(ex, orderItemPojo);
    }


    @Transactional(rollbackOn = ApiException.class)
    public List<OrderItemData> getAllOrderItemsOfAnOrder(Integer orderId) throws ApiException {
        List<OrderItemPojo> orderItemPojoList = orderApi.getAllOrderItems(orderId);
        List<OrderItemData> list2 = new ArrayList<OrderItemData>();
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            String productName = productApi.getCheckProduct(orderItemPojo.getProductId()).getName();
            String barcode = productApi.getCheckProduct(orderItemPojo.getProductId()).getBarcode();
            list2.add(convertOrderItemPojoToOrderItemData(orderItemPojo, productName, barcode));
        }
        return list2;
    }

    private void createNewOrderByAddingItemsToTheOrder(Integer orderId, List<CartItemPojo> cartItemPojoList) throws ApiException {
        for (CartItemPojo cartItemPojo : cartItemPojoList) {
            OrderItemPojo orderItemPojo = convertCartPojoToOrderItemPojo(cartItemPojo, orderId);
            checkSufficientInventoryToAddOrderItem(orderItemPojo, orderItemPojo.getSellingPrice());
            orderApi.addOrderItem(orderItemPojo);
        }
    }

    private void checkSufficientInventoryToCreateOrder(List<CartItemPojo> cartItemPojoList) throws ApiException {
        for (CartItemPojo cartItemPojo : cartItemPojoList) {
            InventoryPojo inventoryPojo = inventoryApi.getCheckByProductId(cartItemPojo.getProductId());
            if (cartItemPojo.getQuantity() > inventoryPojo.getQuantity()) {
                throw new ApiException("The item " + productApi.getCheckProduct(cartItemPojo.getProductId()).getName() + " can't be added to order because sufficient amount not present in inventory. Inventory count = " + inventoryPojo.getQuantity() + "Cart count =" + cartItemPojo.getQuantity());
            }
        }
    }

    private void createInvoiceForOrder(Integer orderId) throws Exception {
        OrderData orderData = convert(orderApi.getCheckOrder(orderId));
        List<OrderItemData> orderItemsList = getAllOrderItemsOfAnOrder(orderId);
        byte[] contents = invoiceClientApi.invoiceOrder(orderData, orderItemsList);
        Path pdfPath = Paths.get(PDF_PATH + orderId + "_invoice.pdf");
        Files.write(pdfPath, contents);
    }

    private void checkSufficientInventoryToAddOrderItem(OrderItemPojo orderItemPojo, Double mrp) throws ApiException {
        InventoryPojo inventoryPojo = inventoryApi.getCheckByProductId(orderItemPojo.getProductId());
        if (orderItemPojo.getQuantity() > inventoryPojo.getQuantity()) {
            throw new ApiException("Item can't be added to order as it exceeds the inventory. Present inventory count : " + inventoryPojo.getQuantity());
        }
        if (orderItemPojo.getSellingPrice() > mrp) {
            throw new ApiException("Item can't be added to order as selling price must be less than MRP. Product's MRP :" + mrp);
        }
        inventoryApi.updateInventory(inventoryPojo, inventoryPojo.getQuantity() - orderItemPojo.getQuantity());
    }


}
