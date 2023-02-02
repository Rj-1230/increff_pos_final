package com.increff.pos.dto;

import com.increff.pos.flow.ProductFlow;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.api.ApiException;
import com.increff.pos.api.ProductApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import static com.increff.pos.helper.NullCheckHelper.*;
import static com.increff.pos.helper.dtoHelper.ProductDtoHelper.*;

import java.util.List;

@Service

public class ProductDto {
    @Autowired
    private ProductApi productApi;
    @Autowired
    private ProductFlow productFlow;


    public void add(ProductForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        productFlow.add(convert(f),f.getBrandName(),f.getCategoryName());
    }

    public void delete(@PathVariable Integer id){
        productApi.delete(id);
    }

    public ProductData get(Integer id) throws ApiException {
        return productFlow.get(id);
    }

    public void update(@PathVariable Integer id, @RequestBody ProductForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        productApi.update(id,convert(f));
    }

    public List<ProductData> getAll()throws ApiException{
        return productFlow.getAll();
    }
}