package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class ProductDao {
//This is JPQL : Java Persistence Query language
    private static String delete_productPojo_by_id = "delete from ProductPojo p where productId=:id";
    private static String select_productPojo_by_id = "select p from ProductPojo p where productId=:id";
    private static String select_productPojo_by_barcode = "select p from ProductPojo p where barcode=:barcode";
    private static String select_all_productPojo = "select p from ProductPojo p";

    @PersistenceContext
    EntityManager em;

    public int insert(ProductPojo p) {
        em.persist(p);
        em.flush();
        return p.getProductId();
    }

    public ProductPojo getProductPojoFromBarcode(String barcode) {
        try{
            TypedQuery<ProductPojo> query = getQuery(select_productPojo_by_barcode);
            query.setParameter("barcode", barcode);
            return query.getSingleResult();
        }
        catch(NoResultException e){
            return null;
        }
    }

    public int delete(int id) {
        Query query = em.createQuery(delete_productPojo_by_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public ProductPojo select(int id) {
        try {
            TypedQuery<ProductPojo> query = getQuery(select_productPojo_by_id);
            query.setParameter("id", id);
            return query.getSingleResult();
        }
        catch(NoResultException e){
            return null;
        }
    }

    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = getQuery(select_all_productPojo);
        return query.getResultList();
    }


    TypedQuery<ProductPojo> getQuery(String jpql) {
        return em.createQuery(jpql, ProductPojo.class);
    }

}