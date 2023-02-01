package com.increff.invoice_app.service;

import com.increff.invoice_app.model.InvoiceForm;
import com.increff.invoice_app.model.OrderItemData;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {
    public void generateInvoice(InvoiceForm form)
    {
        CreateXMLFileJava createXMLFileJava = new CreateXMLFileJava();
        createXMLFileJava.createXML(form);
        PDFFromFOP pdfFromFOP = new PDFFromFOP();
        pdfFromFOP.createPdf();
    }
}
