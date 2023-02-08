package com.increff.pos.api;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(rollbackOn = ApiException.class)
public class BrandApi {

    @Autowired
    private BrandDao brandDao;

    public void addBrand(BrandPojo brandPojo) throws ApiException {
        BrandPojo exBrandPojo = brandDao.getBrandPojoFromBrandCategory(brandPojo.getBrand(), brandPojo.getCategory());
        if (Objects.nonNull(exBrandPojo)) {
            throw new ApiException("The brand " + brandPojo.getBrand() + " with category " + brandPojo.getCategory() + " already exists");
        }
        brandDao.insert(brandPojo);
    }

    public BrandPojo getBrandPojo(Integer brandId) {
        return brandDao.select(brandId);
    }

    public List<BrandPojo> getAll() {
        return brandDao.selectAll();
    }

    public void updateBrand(Integer brandId, BrandPojo newBrandPojo) throws ApiException {
        BrandPojo brandPojo = brandDao.getBrandPojoFromBrandCategory(newBrandPojo.getBrand(), newBrandPojo.getCategory());
        if (Objects.nonNull(brandPojo)) {
            throw new ApiException("The brand " + brandPojo.getBrand() + " with category " + brandPojo.getCategory() + " already exists");
        }
        BrandPojo exBrandPojo = getCheckBrand(brandId);
        exBrandPojo.setBrand(newBrandPojo.getBrand());
        exBrandPojo.setCategory(newBrandPojo.getCategory());
    }

    public BrandPojo getCheckBrand(Integer brandId) throws ApiException {
        BrandPojo brandPojo = brandDao.select(brandId);
        if (Objects.isNull(brandPojo)) {
            throw new ApiException("No such brand-category with given id exists !");
        }
        return brandPojo;
    }

    public Integer getCheckBrand(String brandName, String categoryName) throws ApiException {
        BrandPojo brandPojo = brandDao.getBrandPojoFromBrandCategory(brandName, categoryName);
        if (Objects.isNull(brandPojo)) {
            throw new ApiException("There is no entry with brand " + brandName + " and category " + categoryName);
        }
        return brandPojo.getBrandId();
    }
}