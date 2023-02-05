package com.increff.pos.dao;

import com.increff.pos.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class InventoryDao extends AbstractDao{
    private static String delete_inventory_pojo_by_id = "delete from InventoryPojo p where inventoryId=:id";
    private static String select_inventory_pojo_by_product_id = "select p from InventoryPojo p where productId=:id";
    private static String select_all_inventory_pojo = "select p from InventoryPojo p";

    @PersistenceContext
    EntityManager em;

    public Integer delete(Integer id) {
        Query query = em.createQuery(delete_inventory_pojo_by_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public InventoryPojo select(Integer id) {
        try{
            TypedQuery<InventoryPojo> query = getQuery(select_inventory_pojo_by_product_id, InventoryPojo.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        }
        catch(NoResultException e){
            return null;
        }
    }

    public List<InventoryPojo> selectAll() {
        TypedQuery<InventoryPojo> query = getQuery(select_all_inventory_pojo, InventoryPojo.class);
        return query.getResultList();
    }

}