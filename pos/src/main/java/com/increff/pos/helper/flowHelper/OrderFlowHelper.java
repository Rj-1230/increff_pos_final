package com.increff.pos.helper.flowHelper;

import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.pojo.OrderItemPojo;

import static com.increff.pos.helper.dtoHelper.OrderItemDtoHelper.convert;

public class OrderFlowHelper {
    public static OrderItemData convertOrderItemPojoToOrderItemData(OrderItemPojo orderItemPojo, String productName, String barcode) {
        OrderItemData orderItemData = convert(orderItemPojo);
        orderItemData.setProductName(productName);
        orderItemData.setBarcode(barcode);
        return orderItemData;
    }

}


