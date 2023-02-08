package com.increff.pos.api;

import com.increff.pos.client.InvoiceClient;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceClientApi {
    @Autowired
    private InvoiceClient invoiceClient;

    public byte[] invoiceOrder(OrderData orderData, List<OrderItemData> orderItemsList) throws Exception {
        return invoiceClient.downloadInvoice(orderData, orderItemsList);
    }
}
