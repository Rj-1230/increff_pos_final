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
    public void addBrand(BrandPojo p) throws ApiException{
        BrandPojo brandPojo = brandDao.getBrandPojoFromBrandCategory(p.getBrand(), p.getCategory());
        if(Objects.nonNull(brandPojo)){
            throw new ApiException("The given brand-category already exists");
        }
        brandDao.insert(p);
    }
    @Transactional
    public void deleteBrand(int id) {
        brandDao.delete(id);
    }

    @Transactional
    public List<BrandPojo> getAll() {
        return brandDao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void updateBrand(int id, BrandPojo newBrandPojo) throws ApiException {
        BrandPojo brandPojo = brandDao.getBrandPojoFromBrandCategory(newBrandPojo.getBrand(), newBrandPojo.getCategory());
        if(Objects.nonNull(brandPojo)){
            throw new ApiException("The given brand-category already exists");
        }
        BrandPojo exBrandPojo = getCheckBrand(id);
        exBrandPojo.setBrand(newBrandPojo.getBrand());
        exBrandPojo.setCategory(newBrandPojo.getCategory());
    }

    @Transactional(rollbackOn = ApiException.class)
    public  BrandPojo getCheckBrand(int id) throws ApiException {
        BrandPojo brandPojo = brandDao.select(id);
        if(!Objects.nonNull(brandPojo)){
            throw new ApiException("No such brand-category with given id exists !");
        }
        return brandPojo;
    }

    @Transactional(rollbackOn = ApiException.class)
    public int getBrandIdFromName(String brandName, String categoryName) throws ApiException {
        BrandPojo brandPojo = brandDao.getBrandPojoFromBrandCategory(brandName,categoryName);
        if(Objects.isNull(brandPojo)){
            throw new ApiException("The given brand-category does not exist");
        }
        return brandPojo.getId();
    }
}