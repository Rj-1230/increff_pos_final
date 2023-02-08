package com.increff.invoice_app.service;

import com.increff.invoice_app.model.InvoiceForm;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {
    public void generateInvoice(InvoiceForm form) {
        CreateXMLFileJava createXMLFileJava = new CreateXMLFileJava();
        createXMLFileJava.createXML(form);
        PDFFromFOP pdfFromFOP = new PDFFromFOP();
        pdfFromFOP.createPdf();
    }
}
