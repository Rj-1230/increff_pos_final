package com.increff.pos.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public abstract class AbstractDao {

    @PersistenceContext
    private EntityManager em;

    public void insert(Object obj) {
        em.persist(obj);
    }

    protected <T> TypedQuery<T> getQuery(String jpql, Class<T> clazz) {
        return em.createQuery(jpql, clazz);
    }


}
