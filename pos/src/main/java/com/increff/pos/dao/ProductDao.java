package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao{
    private static String delete_product_pojo_by_id = "delete from ProductPojo p where productId=:id";
    private static String select_product_pojo_by_id = "select p from ProductPojo p where productId=:id";
    private static String select_product_pojo_by_barcode = "select p from ProductPojo p where barcode=:barcode";
    private static String select_all_product_pojo = "select p from ProductPojo p";

    @PersistenceContext
    EntityManager em;

    public Integer insert(ProductPojo p) {
        em.persist(p);
        em.flush();
        return p.getProductId();
    }
    public Integer delete(Integer id) {
        Query query = em.createQuery(delete_product_pojo_by_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public ProductPojo select(Integer id) {
        try {
            TypedQuery<ProductPojo> query = getQuery(select_product_pojo_by_id, ProductPojo.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        }
        catch(NoResultException e){
            return null;
        }
    }
    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = getQuery(select_all_product_pojo, ProductPojo.class);
        return query.getResultList();
    }
    
    public ProductPojo getProductPojoFromBarcode(String barcode) {
        try{
            TypedQuery<ProductPojo> query = getQuery(select_product_pojo_by_barcode, ProductPojo.class);
            query.setParameter("barcode", barcode);
            return query.getSingleResult();
        }
        catch(NoResultException e){
            return null;
        }
    }

}