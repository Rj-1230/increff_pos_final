package com.increff.pos.util;

import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.model.form.InvoiceForm;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.api.ApiException;

import java.util.List;

public class FormUtil {
    public static BrandForm getBrandForm(String brand, String category){
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        return brandForm;
    }

    public static ProductForm getProductForm(String barcode,String brandName,String categoryName,String name,Double mrp){
        ProductForm productForm = new ProductForm();
        productForm.setBarcode(barcode);
        productForm.setBrandName(brandName);
        productForm.setCategoryName(categoryName);
        productForm.setName(name);
        productForm.setMrp(mrp);
        return productForm;
    }


    public static InventoryForm getInventoryForm(String barcode,Integer quantity){
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode(barcode);
        inventoryForm.setQuantity(quantity);
        return inventoryForm;
    }


    public static InvoiceForm convertToInvoiceForm(OrderData orderData, List<OrderItemData> orderItemDataList) throws ApiException
    {
        InvoiceForm invoiceForm = new InvoiceForm();
        invoiceForm.setOrderId(orderData.getOrderId());
        invoiceForm.setCustomerName(orderData.getCustomerName());
        invoiceForm.setCustomerPhone(orderData.getCustomerPhone());
        invoiceForm.setInvoiceTime(orderData.getOrderInvoiceTime().toString());
        invoiceForm.setOrderItemList(orderItemDataList);
        return invoiceForm;
    }
}
