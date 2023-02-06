package com.increff.pos.util;

import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.form.*;
import com.increff.pos.model.data.OrderItemData;
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

    public static CartItemForm getCartItemForm(String barcode,Integer quantity,Double sellingPrice){
        CartItemForm cartItemForm = new CartItemForm();
        cartItemForm.setBarcode(barcode);
        cartItemForm.setQuantity(quantity);
        cartItemForm.setSellingPrice(sellingPrice);
        return cartItemForm;
    }

    public static UserForm getUserForm(String email, String password){
        UserForm userForm = new UserForm();
        userForm.setEmail(email);
        userForm.setPassword(password);
        return  userForm;
    }

    public static CustomerDetailsForm getCustomerDetailsForm(String customerName, String customerPhone){
        CustomerDetailsForm customerDetailsForm = new CustomerDetailsForm();
        customerDetailsForm.setCustomerName(customerName);
        customerDetailsForm.setCustomerPhone(customerPhone);
        return customerDetailsForm;
    }

    public static OrderItemForm getOrderItemForm(String barcode, Integer quantity,Double sellingPrice){
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode(barcode);
        orderItemForm.setQuantity(quantity);
        orderItemForm.setSellingPrice(sellingPrice);
        return orderItemForm;
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
