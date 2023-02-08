package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao {
    private static String select_brand_pojo_by_id = "select p from BrandPojo p where brandId=:id";
    private static String select_all_brand_pojo = "select p from BrandPojo p";
    private static String select_brand_pojo_by_brand_name_and_category_name = "select p from BrandPojo p where brand=:brand and category=:category";

    public BrandPojo select(Integer id) {
        try {
            TypedQuery<BrandPojo> query = getQuery(select_brand_pojo_by_id, BrandPojo.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public List<BrandPojo> selectAll() {
        TypedQuery<BrandPojo> query = getQuery(select_all_brand_pojo, BrandPojo.class);
        return query.getResultList();
    }

    public BrandPojo getBrandPojoFromBrandCategory(String brandName, String categoryName) {
        try {
            TypedQuery<BrandPojo> query = getQuery(select_brand_pojo_by_brand_name_and_category_name, BrandPojo.class);
            query.setParameter("brand", brandName);
            query.setParameter("category", categoryName);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}