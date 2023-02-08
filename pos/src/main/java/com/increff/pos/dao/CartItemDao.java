package com.increff.pos.dao;

import com.increff.pos.pojo.CartItemPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class CartItemDao extends AbstractDao {
    private static String delete_cart_item_pojo_by_id = "delete from CartItemPojo p where cartItemId=:id";
    private static String select_cart_item_pojo_by_id = "select p from CartItemPojo p where cartItemId=:id";
    private static String select_cart_item_pojo_by_product_id_and_counter_id = "select p from CartItemPojo p where productId=:productId and counterId=:counterId";
    private static String select_all_cart_item_pojo_by_counter_id = "select p from CartItemPojo p where counterId=:counterId";
    private static String select_all_cart_item_pojo = "select p from CartItemPojo p";

    @PersistenceContext
    EntityManager em;

    public Integer delete(Integer id) {
        Query query = em.createQuery(delete_cart_item_pojo_by_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public CartItemPojo select(Integer id) {
        try {
            TypedQuery<CartItemPojo> query = getQuery(select_cart_item_pojo_by_id, CartItemPojo.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public CartItemPojo getCartPojoFromProductIdAndCounterId(Integer productId, Integer counterId) {
        try {
            TypedQuery<CartItemPojo> query = getQuery(select_cart_item_pojo_by_product_id_and_counter_id, CartItemPojo.class);
            query.setParameter("productId", productId);
            query.setParameter("counterId", counterId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public List<CartItemPojo> selectAllCartPojoByCounterId(Integer counterId) {
        TypedQuery<CartItemPojo> query = getQuery(select_all_cart_item_pojo_by_counter_id, CartItemPojo.class);
        query.setParameter("counterId", counterId);
        return query.getResultList();
    }

    public List<CartItemPojo> selectAll() {
        TypedQuery<CartItemPojo> query = getQuery(select_all_cart_item_pojo, CartItemPojo.class);
        return query.getResultList();
    }

}