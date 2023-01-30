package com.increff.pos.dao;

import com.increff.pos.pojo.CartPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class CartDao {
    private static String delete_cartPojo_by_id = "delete from CartPojo p where cartItemId=:id";
    private static String select_cartPojo_by_id = "select p from CartPojo p where cartItemId=:id";
    private static String select_cartPojo_by_ProductId_and_counterId = "select p from CartPojo p where productId=:productId and counterId=:counterId";
    private static String select_all_cartPojo_by_counterId = "select p from CartPojo p where counterId=:counterId";
    private static String select_all_cartPojo = "select p from CartPojo p";

    @PersistenceContext
    EntityManager em;

    public void insert(CartPojo p) {
        em.persist(p);
    }

    public Integer delete(Integer id) {
        Query query = em.createQuery(delete_cartPojo_by_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public CartPojo select(Integer id) {
        try{
            TypedQuery<CartPojo> query = getQuery(select_cartPojo_by_id);
            query.setParameter("id", id);
            return query.getSingleResult();
        }

         catch (NoResultException e){
            return null;
        }
    }

    public CartPojo getCartPojoFromProductIdAndCounterId(Integer productId,Integer counterId) {
        try{
            TypedQuery<CartPojo> query = getQuery(select_cartPojo_by_ProductId_and_counterId);
            query.setParameter("productId", productId);
            query.setParameter("counterId", counterId);
            return query.getSingleResult();
        }
        catch (NoResultException e){
            return null;
        }

    }

    public List<CartPojo> selectAll(Integer counterId) {
        TypedQuery<CartPojo> query = getQuery(select_all_cartPojo_by_counterId);
        query.setParameter("counterId", counterId);
        return query.getResultList();
    }

    public List<CartPojo> selectAll() {
        TypedQuery<CartPojo> query = getQuery(select_all_cartPojo);
//        query.setParameter("counterId", counterId);
        return query.getResultList();
    }

    TypedQuery<CartPojo> getQuery(String jpql) {
        return em.createQuery(jpql, CartPojo.class);
    }

}