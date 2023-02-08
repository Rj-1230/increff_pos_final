package com.increff.pos.dto;

import com.increff.pos.api.ApiException;
import com.increff.pos.api.ProductApi;
import com.increff.pos.flow.ProductFlow;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.increff.pos.helper.dtoHelper.ProductDtoHelper.convert;
import static com.increff.pos.helper.dtoHelper.ProductDtoHelper.normalize;
import static com.increff.pos.util.ValidateFormUtil.validateForm;

@Service

public class ProductDto {
    @Autowired
    private ProductApi productApi;
    @Autowired
    private ProductFlow productFlow;


    public void addProduct(ProductForm f) throws ApiException {
        validateForm(f);
        normalize(f);
        productFlow.add(convert(f), f.getBrandName(), f.getCategoryName());
    }

    public ProductData getProduct(Integer id) throws ApiException {
        return productFlow.get(id);
    }

    public void updateProduct(Integer id, ProductForm f) throws ApiException {
        validateForm(f);
        normalize(f);
        productApi.update(id, convert(f));
    }

    public List<ProductData> getAllProducts() throws ApiException {
        return productFlow.getAll();
    }
}