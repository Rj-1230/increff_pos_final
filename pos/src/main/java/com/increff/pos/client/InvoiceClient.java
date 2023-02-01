package com.increff.pos.client;

import com.increff.pos.model.InvoiceForm;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.transaction.Transactional;

import static com.increff.pos.util.FormUtil.convertToInvoiceForm;

@Service
public class InvoiceClient {
    @Value("${invoice_app.url}")
    private String invoice_appUrl;

    @Transactional(rollbackOn = Exception.class)
    public void downloadInvoice(OrderPojo orderPojo,List<OrderItemData>orderItemsList) throws Exception {
        InvoiceForm invoiceForm = convertToInvoiceForm(orderPojo,orderItemsList);
        RestTemplate restTemplate = new RestTemplate();
        byte[] contents = restTemplate.postForEntity(invoice_appUrl, invoiceForm, byte[].class).getBody();
        Path pdfPath = Paths.get("./src/main/resources/pdf/" + orderPojo.getOrderId() + "_invoice.pdf");
        Files.write(pdfPath, contents);
    }

}
