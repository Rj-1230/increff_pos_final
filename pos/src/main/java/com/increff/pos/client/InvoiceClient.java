package com.increff.pos.client;

import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.form.InvoiceForm;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import javax.transaction.Transactional;

import static com.increff.pos.util.FormUtil.convertToInvoiceForm;

@Component
public class InvoiceClient {
    @Autowired
    private Properties properties;

    @Autowired
    private RestTemplate restTemplate;
    @Transactional(rollbackOn = Exception.class)
    public byte[] downloadInvoice(OrderData orderData, List<OrderItemData>orderItemsList) throws Exception {
        InvoiceForm invoiceForm = convertToInvoiceForm(orderData,orderItemsList);
        return restTemplate.postForEntity(properties.getInvoice_appUrl(), invoiceForm, byte[].class).getBody();
    }

}
