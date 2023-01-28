package com.increff.pos.dao;

import com.increff.pos.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class InventoryDao {
    private static String delete_inventoryPojo_by_id = "delete from InventoryPojo p where productId=:id";
    private static String select_inventoryPojo_by_id = "select p from InventoryPojo p where productId=:id";
    private static String select_all_inventoryPojo = "select p from InventoryPojo p";

    @PersistenceContext
    EntityManager em;

    public void insert(InventoryPojo p) {
        em.persist(p);
    }

    public int delete(int id) {
        Query query = em.createQuery(delete_inventoryPojo_by_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public InventoryPojo select(int id) {
        try{
            TypedQuery<InventoryPojo> query = getQuery(select_inventoryPojo_by_id);
            query.setParameter("id", id);
            return query.getSingleResult();
        }
        catch(NoResultException e){
            return null;
        }
    }

    public List<InventoryPojo> selectAll() {
        TypedQuery<InventoryPojo> query = getQuery(select_all_inventoryPojo);
        return query.getResultList();
    }

    TypedQuery<InventoryPojo> getQuery(String jpql) {
        return em.createQuery(jpql, InventoryPojo.class);
    }

}