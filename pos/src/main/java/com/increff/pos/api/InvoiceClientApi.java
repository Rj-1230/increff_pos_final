package com.increff.pos.api;
import com.increff.pos.client.InvoiceClient;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class InvoiceClientApi {
    @Autowired
    private InvoiceClient invoiceClient;
    @Transactional(rollbackOn = Exception.class)
    public void invoiceOrder(OrderPojo orderPojo, List<OrderItemData> orderItemsList) throws Exception {
        invoiceClient.downloadInvoice(orderPojo,orderItemsList);
    }
}
