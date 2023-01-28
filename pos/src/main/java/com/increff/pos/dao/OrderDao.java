package com.increff.pos.dao;

import com.increff.pos.pojo.CartPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class OrderDao {

    private static String select_orderPojo_by_orderCode = "select p from OrderPojo p where orderCode=:orderCode";
    private static String select_orderPojo_by_orderId = "select p from OrderPojo p where orderId=:id";
    private static String select_all_orderPojo_between_startDate_and_endDate = "select p from OrderPojo p where orderPlaceTime>=:start and orderPlaceTime<=:end";
    private static String select_all_orderPojo_by_counterId = "select p from OrderPojo p where counterId=:id order by orderCreateTime desc";
    private static String select_all_orderPojo = "select p from OrderPojo p order by orderCreateTime desc";
    private static String delete_orderItemPojo_by_id = "delete from OrderItemPojo p where orderItemId=:id";
    private static String select_orderItemPojo_by_id = "select p from OrderItemPojo p where orderItemId=:id";
    private static String select_all_orderItemPojos_by_orderID = "select p from OrderItemPojo p where orderId=:id";
    private static String select_OrderItemPojo_by_ProductId_and_orderId = "select p from OrderItemPojo p where productId=:productId and orderId=:orderId";


    @PersistenceContext
    EntityManager em;

    public int insertOrder(OrderPojo p) {
        em.persist(p);
        em.flush();
        return p.getOrderId();
    }

    public OrderPojo selectOrder(int id) {
        try{
        TypedQuery<OrderPojo> query = getQuery(select_orderPojo_by_orderId);
        query.setParameter("id", id);
        return query.getSingleResult();
    }
        catch(NoResultException e){
        return null;
    }
    }

    public List<OrderPojo> selectAllOrders(int id) {
        TypedQuery<OrderPojo> query = getQuery(select_all_orderPojo_by_counterId);
        query.setParameter("id", id);
        return query.getResultList();
    }
    public List<OrderPojo> selectAllOrders() {
        TypedQuery<OrderPojo> query = getQuery(select_all_orderPojo);
        return query.getResultList();
    }

    public List<OrderPojo> selectDateFilter(String start, String end)
    {
        try{
            TypedQuery<OrderPojo> query = getQuery(select_all_orderPojo_between_startDate_and_endDate);
            query.setParameter("start", start);
            query.setParameter("end", end);
            return query.getResultList();
        }
        catch(Exception e)
        {
            return null;
        }
    }


    public void insertOrderItem(OrderItemPojo p) {
        em.persist(p);
    }

    public int deleteOrderItem(int id) {
        Query query = em.createQuery(delete_orderItemPojo_by_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public OrderItemPojo selectOrderItem(int id) {
        try {
            TypedQuery<OrderItemPojo> query = getItemQuery(select_orderItemPojo_by_id);
            query.setParameter("id", id);
            return query.getSingleResult();
        }
        catch (NoResultException e){
            return null;
        }
    }


    public List<OrderItemPojo> selectAllOrderItems(int id) {
        TypedQuery<OrderItemPojo> query = getItemQuery(select_all_orderItemPojos_by_orderID);
        query.setParameter("id", id);
        return query.getResultList();
    }

    public OrderItemPojo getOrderItemPojoFromProductId(int productId,int orderId) {
        try{
            TypedQuery<OrderItemPojo> query = getItemQuery(select_OrderItemPojo_by_ProductId_and_orderId);
            query.setParameter("productId", productId);
            query.setParameter("orderId", orderId);
            return query.getSingleResult();
        }
        catch (NoResultException e){
            return null;
        }

    }

    public OrderPojo selectByOrderCode(String orderCode) {
        try{
        TypedQuery<OrderPojo> query = getQuery(select_orderPojo_by_orderCode);
        query.setParameter("orderCode", orderCode);
        return query.getSingleResult();
    }
        catch (NoResultException e){
        return null;
    }
    }

    TypedQuery<OrderItemPojo> getItemQuery(String jpql) {
        return em.createQuery(jpql, OrderItemPojo.class);
    }

    TypedQuery<OrderPojo> getQuery(String jpql) {
        return em.createQuery(jpql, OrderPojo.class);
    }

}