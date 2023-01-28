package com.increff.invoice_app.service;

import com.increff.invoice_app.model.InvoiceForm;
import com.increff.invoice_app.model.OrderItemData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {
    public void generateInvoice(InvoiceForm form)
    {
        print(form);
        CreateXMLFileJava createXMLFileJava = new CreateXMLFileJava();

        createXMLFileJava.createXML(form);

        PDFFromFOP pdfFromFOP = new PDFFromFOP();

        pdfFromFOP.createPdf();
    }

    private void print(InvoiceForm form)
    {
        System.out.println(form.getOrderId());
        System.out.println(form.getCustomerName());
        System.out.println(form.getPlaceDate());

        List<OrderItemData> orderItemList = form.getOrderItemList();

        System.out.println(orderItemList.size());

        for(OrderItemData o :orderItemList)
        {
            System.out.println(o.getProductName());
            System.out.println(o.getOrderItemId());
            System.out.println(o.getQuantity());
            System.out.println(o.getSellingPrice());
        }
    }
}
