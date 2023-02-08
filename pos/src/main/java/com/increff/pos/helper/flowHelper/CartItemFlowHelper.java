package com.increff.pos.helper.flowHelper;

import com.increff.pos.api.ApiException;
import com.increff.pos.model.data.CartItemData;
import com.increff.pos.pojo.CartItemPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductPojo;

import static com.increff.pos.helper.dtoHelper.CartItemDtoHelper.convert;

public class CartItemFlowHelper {
    public static OrderItemPojo convertCartPojoToOrderItemPojo(CartItemPojo cartItemPojo, Integer orderId) {
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setProductId(cartItemPojo.getProductId());
        orderItemPojo.setQuantity(cartItemPojo.getQuantity());
        orderItemPojo.setSellingPrice(cartItemPojo.getSellingPrice());
        return orderItemPojo;
    }

    public static CartItemData convertCartPojoToCartData(CartItemPojo cartItemPojo, String productName, String barcode) {
        CartItemData cartItemData = convert(cartItemPojo);
        cartItemData.setProductName(productName);
        cartItemData.setBarcode(barcode);
        return cartItemData;
    }

    public static Integer checkMrpAndInventoryForCartPojo(CartItemPojo cartItemPojo, ProductPojo productPojo, InventoryPojo inventoryPojo) throws ApiException {
        if (cartItemPojo.getSellingPrice() > productPojo.getMrp()) {
            throw new ApiException("Selling price must be less than MRP. Product's MRP :" + productPojo.getMrp());
        }
        if (cartItemPojo.getQuantity() > inventoryPojo.getQuantity()) {
            throw new ApiException("Not sufficient inventory. Present inventory count : " + inventoryPojo.getQuantity());
        }
        return inventoryPojo.getQuantity();
    }
}
