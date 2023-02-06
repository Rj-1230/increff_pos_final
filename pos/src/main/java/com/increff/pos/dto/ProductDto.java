package com.increff.pos.dto;

import com.increff.pos.flow.ProductFlow;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.api.ApiException;
import com.increff.pos.api.ProductApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.increff.pos.util.NullCheckHelper.*;
import static com.increff.pos.helper.dtoHelper.ProductDtoHelper.*;

import java.util.List;

@Service

public class ProductDto {
    @Autowired
    private ProductApi productApi;
    @Autowired
    private ProductFlow productFlow;


    public void addProduct(ProductForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        productFlow.add(convert(f),f.getBrandName(),f.getCategoryName());
    }

    public ProductData getProduct(Integer id) throws ApiException {
        return productFlow.get(id);
    }

    public void updateProduct(Integer id,ProductForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        productApi.update(id,convert(f));
    }

    public List<ProductData> getAllProducts()throws ApiException{
        return productFlow.getAll();
    }
}