package com.increff.pos.client;

import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.InvoiceForm;
import com.increff.pos.spring.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;

import static com.increff.pos.util.FormUtil.convertToInvoiceForm;

@Component
public class InvoiceClient {
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private RestTemplate restTemplate;

    @Transactional(rollbackOn = Exception.class)
    public byte[] downloadInvoice(OrderData orderData, List<OrderItemData> orderItemsList) throws Exception {
        InvoiceForm invoiceForm = convertToInvoiceForm(orderData, orderItemsList);
        return restTemplate.postForEntity(applicationProperties.getInvoice_appUrl(), invoiceForm, byte[].class).getBody();
    }

}
