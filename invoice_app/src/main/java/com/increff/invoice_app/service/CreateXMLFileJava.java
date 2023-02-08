package com.increff.invoice_app.service;

import com.increff.invoice_app.model.InvoiceForm;
import com.increff.invoice_app.model.OrderItemData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CreateXMLFileJava {

    public static final String xmlFilePath = "src/main/resources/xml/invoice.xml";


    public void createXML(InvoiceForm invoiceForm) {

        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            // root element
            Element root = document.createElement("invoice");
            document.appendChild(root);

            String s1 = invoiceForm.getCustomerName().substring(0, 1).toUpperCase();  // first letter = J
            String s2 = invoiceForm.getCustomerName().substring(1);     // after 1st letter = avatpoint
            String res = s1 + s2; // J + avatpoint

            Element customerName = document.createElement("customerName");
            customerName.appendChild(document.createTextNode(res));
            root.appendChild(customerName);

            Element customerPhone = document.createElement("customerPhone");
            customerPhone.appendChild(document.createTextNode(invoiceForm.getCustomerPhone()));
            root.appendChild(customerPhone);

            Element orderId = document.createElement("orderId");
            orderId.appendChild(document.createTextNode(invoiceForm.getOrderId().toString()));
            root.appendChild(orderId);

            Element companyName = document.createElement("companyName");
            companyName.appendChild(document.createTextNode("Increff"));
            root.appendChild(companyName);

            Element building = document.createElement("building");
            building.appendChild(document.createTextNode("Enzyme Tech Park"));
            root.appendChild(building);

            Element street = document.createElement("street");
            street.appendChild(document.createTextNode("18th Cross, 6th Main Rd"));
            root.appendChild(street);

            Element locality = document.createElement("locality");
            locality.appendChild(document.createTextNode("Sector 6, HSR Layout,"));
            root.appendChild(locality);

            Element city = document.createElement("city");
            city.appendChild(document.createTextNode("Bangaluru - 560102"));
            root.appendChild(city);

            Element state = document.createElement("state");
            state.appendChild(document.createTextNode("Karanataka"));
            root.appendChild(state);

            String formattedInvoiceDate = invoiceForm.getInvoiceTime().substring(0, 10);
            Element invoiceDate = document.createElement("invoiceDate");
            invoiceDate.appendChild(document.createTextNode(formattedInvoiceDate));
            root.appendChild(invoiceDate);

            Element invoiceTime = document.createElement("invoiceTime");
            invoiceTime.appendChild(document.createTextNode(invoiceForm.getInvoiceTime().substring(11, 19)));
            root.appendChild(invoiceTime);

            Element orderItems = document.createElement("orderItems");
            root.appendChild(orderItems);
            Double amount = 0.0;
            int index = 1;
            // order item element
            for (OrderItemData o : invoiceForm.getOrderItemList()) {
                Element orderItem = document.createElement("orderItem");

                orderItems.appendChild(orderItem);

//                serial number
                Element sn = document.createElement("sn");
                sn.appendChild(document.createTextNode(String.valueOf(index)));
                orderItem.appendChild(sn);

                // set an attribute to staff element
                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(o.getOrderItemId().toString()));
                orderItem.appendChild(id);

                // firstname element
                Element productName = document.createElement("productName");
                productName.appendChild(document.createTextNode(o.getProductName()));
                orderItem.appendChild(productName);

                // lastname element
                Element quantity = document.createElement("quantity");
                quantity.appendChild(document.createTextNode(o.getQuantity().toString()));
                orderItem.appendChild(quantity);

                Element sellingPrice = document.createElement("sellingPrice");
                sellingPrice.appendChild(document.createTextNode(String.format("%.2f", new BigDecimal(o.getSellingPrice()).setScale(2, RoundingMode.HALF_UP).doubleValue())));
                orderItem.appendChild(sellingPrice);

                Element total = document.createElement("total");
                total.appendChild(document.createTextNode(String.format("%.2f", new BigDecimal(o.getSellingPrice() * o.getQuantity()).setScale(2, RoundingMode.HALF_UP).doubleValue())));
                orderItem.appendChild(total);

                amount += o.getSellingPrice() * o.getQuantity();
                index++;
            }

            Element totalAmount = document.createElement("totalAmount");
            totalAmount.appendChild(document.createTextNode(String.format("%.2f", new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP).doubleValue())));
            root.appendChild(totalAmount);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));
            transformer.transform(domSource, streamResult);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}