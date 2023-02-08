package com.increff.pos.util;

import com.increff.pos.model.form.*;

public class DummyForm {
    public static OrderItemForm getDummyOrderItemForm() {
        String barcode = "barcode";
        Integer quantity = 5;
        Double sellingPrice = 1000.95;
        return FormUtil.getOrderItemForm(barcode, quantity, sellingPrice);
    }

    public static CustomerDetailsForm getDummyCustomerDetailsForm() {
        String customerName = "customer";
        String customerPhone = "9876543210";
        return FormUtil.getCustomerDetailsForm(customerName, customerPhone);
    }

    public static BrandForm getDummyBrandForm() {
        String brand = "brand";
        String category = "category";
        return FormUtil.getBrandForm(brand, category);
    }

    public static DateBrandCategoryFilterForm getDummyDateBrandCategoryForm() {
        String start = "2023-01-01";
        String end = "2023-01-30";
        String brand = "brand";
        String category = "category";
        return FormUtil.getDateBrandCategoryFilterForm(start, end, brand, category);
    }

    public static DateFilterForm getDummyDateForm() {
        String start = "2023-01-01";
        String end = "2023-01-30";
        return FormUtil.getDateFilterForm(start, end);
    }

    public static CartItemForm getDummyCartItemForm() {
        String barcode = "barcode";
        Integer quantity = 10;
        Double sellingPrice = 120.66;
        return FormUtil.getCartItemForm(barcode, quantity, sellingPrice);
    }

    public static InventoryForm getDummyInventoryForm() {
        String barcode = "barcode";
        Integer quantity = 10;
        return FormUtil.getInventoryForm(barcode, quantity);
    }

    public static ProductForm getDummyProductForm() {
        String barcode = "barcode";
        String brand = "brand";
        String category = "category";
        String productName = "prod1";
        Double mrp = 124.54;
        return FormUtil.getProductForm(barcode, brand, category, productName, mrp);
    }

    public static UserForm getDummyUserForm() {
        String email = "abcd@gmail.com";
        String password = "abcd@1234";
        return FormUtil.getUserForm(email, password);
    }

}
