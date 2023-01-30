package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.UserPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class UserDao{

    private static String delete_userpojo_by_id = "delete from UserPojo p where id=:id";
    private static String selct_userpojo_by_id = "select p from UserPojo p where id=:id";
    private static String selct_userpojo_by_email = "select p from UserPojo p where email=:email";
    private static String select_all_userpojo= "select p from UserPojo p";

    @PersistenceContext
    EntityManager em;
    @Transactional
    public void insert(UserPojo p) {
        em.persist(p);
    }

    public Integer delete(Integer id) {
        Query query = em.createQuery(delete_userpojo_by_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public UserPojo select(Integer id) {
        try {
            TypedQuery<UserPojo> query = getQuery(selct_userpojo_by_id);
            query.setParameter("id", id);
            return query.getSingleResult();
        }
         catch(NoResultException e){
                return null;
            }
    }

    public UserPojo select(String email) {
        try {
        TypedQuery<UserPojo> query = getQuery(selct_userpojo_by_email);
        query.setParameter("email", email);
        return query.getSingleResult();
    }
        catch(NoResultException e){
        return null;
    }
    }

    public List<UserPojo> selectAll() {
        TypedQuery<UserPojo> query = getQuery(select_all_userpojo);
        return query.getResultList();
    }

    TypedQuery<UserPojo> getQuery(String jpql) {
        return em.createQuery(jpql, UserPojo.class);
    }

}