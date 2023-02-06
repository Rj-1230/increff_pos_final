package com.increff.pos.dto;

import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.api.ApiException;
import com.increff.pos.api.BrandApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.increff.pos.helper.dtoHelper.BrandDtoHelper.*;
import static com.increff.pos.util.NullCheckHelper.*;

@Service

public class BrandDto {
    @Autowired
    private BrandApi brandApi;

    public void addBrand(BrandForm brandForm)throws  ApiException{
        checkNullable(brandForm);
        normalize(brandForm);
        brandApi.addBrand(convert(brandForm));
    }

    public BrandData getBrand(Integer id) throws ApiException {
        return convert(brandApi.getCheckBrand(id));
    }
    public void updateBrand(Integer id,BrandForm brandForm) throws ApiException {
        checkNullable(brandForm);
        normalize(brandForm);
        brandApi.updateBrand(id,convert(brandForm));
    }
    public List<BrandData> getAll(){
        return getAllBrands(brandApi.getAll());
    }

}
