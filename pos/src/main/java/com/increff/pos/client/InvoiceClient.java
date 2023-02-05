package com.increff.pos.client;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.form.InvoiceForm;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.properties.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.transaction.Transactional;

import static com.increff.pos.util.FormUtil.convertToInvoiceForm;

@Component
public class InvoiceClient {
    @Autowired
    private Properties properties;
    @Transactional(rollbackOn = Exception.class)
    public byte[] downloadInvoice(OrderData orderData, List<OrderItemData>orderItemsList) throws Exception {
        InvoiceForm invoiceForm = convertToInvoiceForm(orderData,orderItemsList);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(properties.getInvoice_appUrl(), invoiceForm, byte[].class).getBody();
    }

}
