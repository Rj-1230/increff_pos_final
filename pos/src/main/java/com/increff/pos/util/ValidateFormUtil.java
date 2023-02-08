package com.increff.pos.util;

import com.increff.pos.api.ApiException;
import com.increff.pos.model.form.*;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.regex.*;
import static com.increff.pos.util.GetCurrentDateTime.convertStringToZonedDateTime;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;

public class ValidateFormUtil {
    public static void validateForm(BrandForm brandForm) throws ApiException {
        if (Objects.isNull(brandForm.getBrand()) || Objects.isNull(brandForm.getCategory())) {
            throw new ApiException("The brand name or category name field can't be null");
        }
        if (Objects.equals(brandForm.getBrand().trim(), "")) {
            throw new ApiException("The brand name field can't be empty");
        }
        if (Objects.equals(brandForm.getCategory().trim(), "")) {
            throw new ApiException("The category name field can't be empty");
        }
    }

    public static void validateForm(ProductForm productForm) throws ApiException {
        if (Objects.isNull(productForm.getBarcode())) {
            throw new ApiException("The barcode field can't be null");
        }
        if (Objects.isNull(productForm.getName())) {
            throw new ApiException("The product name field can't be null");
        }
        if (Objects.isNull(productForm.getMrp())) {
            throw new ApiException("The MRP of product can't be null");
        }
        if (Objects.isNull(productForm.getBrandName()) || Objects.isNull(productForm.getCategoryName())) {
            throw new ApiException("The brand name or category name field can't be null");
        }
        if (Objects.equals(productForm.getBarcode().trim(), "")) {
            throw new ApiException("The barcode can't be empty");
        }
        if (Objects.equals(productForm.getName().trim(), "")) {
            throw new ApiException("The product name field can't be empty");
        }

        if (productForm.getMrp() <= 0.0d) {
            throw new ApiException("The MRP of product can't be 0 or negative");
        }
        if (Double.compare(productForm.getMrp() , new Double(999999))>0){
            throw new ApiException("The MRP of product must be less than 999999");
        }

    }


    public static void validateForm(InventoryForm inventoryForm) throws ApiException {
        if (Objects.isNull(inventoryForm.getBarcode())) {
            throw new ApiException("The barcode field can't be null");
        }
        if (Objects.isNull(inventoryForm.getQuantity())) {
            throw new ApiException("The quantity field can't be null");
        }
        if (Objects.equals(inventoryForm.getBarcode().trim(), "")) {
            throw new ApiException("The barcode can't be empty");
        }
        if (inventoryForm.getQuantity() <= 0) {
            throw new ApiException("The item quantity in the inventory must be positive");
        }
        if (inventoryForm.getQuantity() > 999) {
            throw new ApiException("Maximum 999 items can be added to inventory at once");
        }
    }

    public static void validateForm(CartItemForm cartItemForm) throws ApiException {
        if (Objects.isNull(cartItemForm.getBarcode())) {
            throw new ApiException("The barcode field can't be null");
        }
        if (Objects.isNull(cartItemForm.getQuantity())) {
            throw new ApiException("The quantity field can't be null");
        }
        if (Objects.isNull(cartItemForm.getSellingPrice())) {
            throw new ApiException("The selling price field can't be null");
        }
        if (Objects.equals(cartItemForm.getBarcode().trim(), "")) {
            throw new ApiException("The barcode can't be empty");
        }
        if (cartItemForm.getQuantity() <= 0) {
            throw new ApiException("The item quantity to be added in the cart must be positive");
        }
        if (cartItemForm.getQuantity() > 99) {
            throw new ApiException("Maximum 99 items can be added to cart at once");
        }
        if (cartItemForm.getSellingPrice() <= 0.0d) {
            throw new ApiException("The selling price of item must be positive");
        }
        if (Double.compare(cartItemForm.getSellingPrice() , new Double(999999))>0){
            throw new ApiException("The selling price of item must be less than 999999");
        }
    }

    public static void validateForm(CustomerDetailsForm customerDetailsForm) throws ApiException {
        if (Objects.isNull(customerDetailsForm.getCustomerName())) {
            throw new ApiException("The customer name field can't be null");
        }
        if (Objects.isNull(customerDetailsForm.getCustomerPhone())) {
            throw new ApiException("The customer phone field can't be null");
        }
        if (Objects.equals(customerDetailsForm.getCustomerName().trim(), "")) {
            throw new ApiException("The customer name can't be empty");
        }
        if (Objects.equals(customerDetailsForm.getCustomerPhone().trim(), "")) {
            throw new ApiException("The customer phone number must contain 10 digits");
        }
        if (!onlyDigits(customerDetailsForm.getCustomerPhone(), customerDetailsForm.getCustomerPhone().length())) {
            throw new ApiException("The customer phone must be a valid 10 digit number");
        }
    }

    public static boolean onlyDigits(String str, int n) {
        for (int i = 0; i < n; i++) {
            if (str.charAt(i) < '0' || str.charAt(i) > '9') {
                return false;
            }
        }
        return true;
    }

    public static void validateForm(OrderItemForm orderItemForm) throws ApiException {
        if (Objects.isNull(orderItemForm.getBarcode())) {
            throw new ApiException("The barcode field can't be null");
        }
        if (Objects.isNull(orderItemForm.getQuantity())) {
            throw new ApiException("The quantity field can't be null");
        }
        if (Objects.isNull(orderItemForm.getSellingPrice())) {
            throw new ApiException("The selling price field can't be null");
        }
        if (Objects.equals(orderItemForm.getBarcode().trim(), "")) {
            throw new ApiException("The barcode can't be empty");
        }
        if (orderItemForm.getQuantity() <= 0) {
            throw new ApiException("The items to be added to order must be positive");
        }
        if (orderItemForm.getSellingPrice() <= 0.0d) {
            throw new ApiException("The selling price of item must be positive");
        }
        if (orderItemForm.getQuantity() > 99) {
            throw new ApiException("Maximum 99 items can be added to order at once");
        }
        if (orderItemForm.getSellingPrice() > 999999.0d) {
            throw new ApiException("The selling price of order item must be less than 999999");
        }
    }


    public static void validateForm(UserForm userForm) throws ApiException {
        if (Objects.isNull(userForm.getEmail())) {
            throw new ApiException("The email field can't be null");
        }
        if (Objects.isNull(userForm.getPassword())) {
            throw new ApiException("The password field can't be null");
        }
        if (Objects.equals(userForm.getEmail().trim(), "")) {
            throw new ApiException("Email can't be empty");
        }
        if (Objects.equals(userForm.getPassword().trim(), "")) {
            throw new ApiException("The password can't be empty");
        }
        if (userForm.getPassword().trim().length() < 8) {
            throw new ApiException("The password must be at least 8 characters long");
        }
        if (userForm.getPassword().trim().length() > 15) {
            throw new ApiException("The password can be max 15 characters long");
        }

        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if(!Pattern.matches(regexPattern,userForm.getEmail())){
            throw new ApiException("Enter valid email address");
        }

    }

    public static void validateForm(DateBrandCategoryFilterForm dateBrandCategoryFilterForm) throws ApiException {
        if (Objects.isNull(dateBrandCategoryFilterForm.getStart()) || Objects.isNull(dateBrandCategoryFilterForm.getEnd())) {
            throw new ApiException("The start date or end date field can't be null");
        }
        if (Objects.isNull(dateBrandCategoryFilterForm.getBrand()) || Objects.isNull(dateBrandCategoryFilterForm.getCategory())) {
            throw new ApiException("The brand or category field can't be null");
        }

        if (Objects.equals(dateBrandCategoryFilterForm.getStart().trim(), "")) {
            throw new ApiException("Start date can't be empty");
        }
        if (Objects.equals(dateBrandCategoryFilterForm.getEnd().trim(), "")) {
            throw new ApiException("End date can't be empty");
        }
        if (Objects.equals(dateBrandCategoryFilterForm.getBrand().trim(), "")) {
            throw new ApiException("The brand can't be empty");
        }
        if (Objects.equals(dateBrandCategoryFilterForm.getCategory().trim(), "")) {
            throw new ApiException("The category can't be empty");
        }
    }

    public static void validateForm(DateFilterForm dateFilterForm) throws ApiException {
        if (Objects.isNull(dateFilterForm.getStart()) || Objects.isNull(dateFilterForm.getEnd())) {
            throw new ApiException("The start date or end date field can't be null");
        }
        if (Objects.equals(dateFilterForm.getStart().trim(), "")) {
            throw new ApiException("Start Date can't be empty");
        }
        if (Objects.equals(dateFilterForm.getEnd().trim(), "")) {
            throw new ApiException("End date can't be empty");
        }
    }


    public static void checkDate(String startDate, String endDate) throws ApiException {
        ZonedDateTime start = convertStringToZonedDateTime(startDate + " 00:00:00");
        ZonedDateTime end = convertStringToZonedDateTime(endDate + " 23:59:59");
        if (end.isBefore(start)) {
            throw new ApiException("The end date must be ahead of start date");
        }
        ZonedDateTime temp = start.plusDays(31);
        if (end.isAfter(temp)) {
            throw new ApiException("The end date must be only a month ahead of start date");
        }

    }


}
