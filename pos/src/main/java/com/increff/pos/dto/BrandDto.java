package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.api.ApiException;
import com.increff.pos.api.BrandApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.increff.pos.helper.dtoHelper.BrandDtoHelper.*;
import static com.increff.pos.helper.NullCheckHelper.*;

@Service

public class BrandDto {
    @Autowired
    private BrandApi brandApi;

    public void addBrand(BrandForm f)throws  ApiException{
        checkNullable(f);
        normalize(f);
        brandApi.addBrand(convert(f));
    }
    public void deleteBrand(@PathVariable Integer id){
        brandApi.deleteBrand(id);
    }

    public BrandData getBrand(Integer id) throws ApiException {
        return convert(brandApi.getCheckBrand(id));
    }
    public void updateBrand(@PathVariable Integer id, @RequestBody BrandForm f) throws ApiException {
        checkNullable(f);
        normalize(f);
        brandApi.updateBrand(id,convert(f));
    }
    public List<BrandData> getAll(){
        return getAllBrands(brandApi.getAll());
    }

}
