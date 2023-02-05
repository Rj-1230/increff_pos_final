package com.increff.pos.api;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandDao;

@Service
@Transactional(rollbackOn  = ApiException.class)
public class BrandApi {

    @Autowired
    private BrandDao brandDao;

    public void addBrand(BrandPojo brandPojo) throws ApiException{
        BrandPojo exBrandPojo = brandDao.getBrandPojoFromBrandCategory(brandPojo.getBrand(), brandPojo.getCategory());
        if(Objects.nonNull(exBrandPojo)){
            throw new ApiException("The brand "+brandPojo.getBrand()+" with category "+brandPojo.getCategory()+" already exists");
        }
        brandDao.insert(brandPojo);
    }
    public void deleteBrand(Integer id) throws ApiException {
        getCheckBrand(id);
        brandDao.delete(id);
    }

    public BrandPojo getBrandPojo(Integer id) {
        return brandDao.select(id);
    }
    public List<BrandPojo> getAll() {
        return brandDao.selectAll();
    }

    public void updateBrand(Integer id, BrandPojo newBrandPojo) throws ApiException {
        BrandPojo brandPojo = brandDao.getBrandPojoFromBrandCategory(newBrandPojo.getBrand(), newBrandPojo.getCategory());
        if(Objects.nonNull(brandPojo)){
            throw new ApiException("The brand "+brandPojo.getBrand()+" with category "+brandPojo.getCategory()+" already exists");
        }
        BrandPojo exBrandPojo = getCheckBrand(id);
        exBrandPojo.setBrand(newBrandPojo.getBrand());
        exBrandPojo.setCategory(newBrandPojo.getCategory());
    }

    public  BrandPojo getCheckBrand(Integer id) throws ApiException {
        BrandPojo brandPojo = brandDao.select(id);
        if(Objects.isNull(brandPojo)){
            throw new ApiException("No such brand-category with given id exists !");
        }
        return brandPojo;
    }

    public Integer getCheckBrand(String brandName, String categoryName) throws ApiException {
        BrandPojo brandPojo = brandDao.getBrandPojoFromBrandCategory(brandName,categoryName);
        if(Objects.isNull(brandPojo)){
            throw new ApiException("There is no entry with brand "+brandName+" and category "+categoryName);
        }
        return brandPojo.getBrandId();
    }
}