package com.increff.pos.service;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandDao;

@Service
public class BrandService {

    @Autowired
    private BrandDao brandDao;

    @Transactional(rollbackOn  = ApiException.class)
    public void addBrand(BrandPojo brandPojo) throws ApiException{
        BrandPojo exBrandPojo = brandDao.getBrandPojoFromBrandCategory(brandPojo.getBrand(), brandPojo.getCategory());
        if(Objects.nonNull(exBrandPojo)){
            throw new ApiException("The given brand-category already exists");
        }
        brandDao.insert(brandPojo);
    }
    @Transactional
    public void deleteBrand(Integer id) {
        brandDao.delete(id);
    }

    @Transactional
    public BrandPojo getBrandPojo(Integer id) {
        return brandDao.select(id);
    }

    @Transactional
    public List<BrandPojo> getAll() {
        return brandDao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void updateBrand(Integer id, BrandPojo newBrandPojo) throws ApiException {
        BrandPojo brandPojo = brandDao.getBrandPojoFromBrandCategory(newBrandPojo.getBrand(), newBrandPojo.getCategory());
        if(Objects.nonNull(brandPojo)){
            throw new ApiException("The given brand-category already exists");
        }
        BrandPojo exBrandPojo = getCheckBrand(id);
        exBrandPojo.setBrand(newBrandPojo.getBrand());
        exBrandPojo.setCategory(newBrandPojo.getCategory());
    }

    @Transactional(rollbackOn = ApiException.class)
    public  BrandPojo getCheckBrand(Integer id) throws ApiException {
        BrandPojo brandPojo = brandDao.select(id);
        if(!Objects.nonNull(brandPojo)){
            throw new ApiException("No such brand-category with given id exists !");
        }
        return brandPojo;
    }

    @Transactional(rollbackOn = ApiException.class)
    public Integer getBrandIdFromName(String brandName, String categoryName) throws ApiException {
        BrandPojo brandPojo = brandDao.getBrandPojoFromBrandCategory(brandName,categoryName);
        if(Objects.isNull(brandPojo)){
            throw new ApiException("The given brand-category does not exist");
        }
        return brandPojo.getId();
    }
}