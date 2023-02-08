package com.increff.pos.dao;

import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao {
    private static String select_order_pojo_by_order_code = "select p from OrderPojo p where orderCode=:orderCode";
    private static String select_order_pojo_by_order_id = "select p from OrderPojo p where orderId=:id";
    private static String select_all_order_pojo_between_start_date_and_end_date = "select p from OrderPojo p where orderInvoiceTime>=:start and orderInvoiceTime<=:end";
    private static String select_all_order_pojo_by_counter_id = "select p from OrderPojo p where counterId=:id order by orderCreateTime desc";
    private static String select_all_order_pojo = "select p from OrderPojo p order by orderCreateTime desc";
    @PersistenceContext
    EntityManager em;

    public Integer insertOrder(OrderPojo p) {
        em.persist(p);
        em.flush();
        return p.getOrderId();
    }

    public OrderPojo selectOrder(Integer id) {
        try {
            TypedQuery<OrderPojo> query = getQuery(select_order_pojo_by_order_id, OrderPojo.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<OrderPojo> selectAllOrdersByCounterId(Integer id) {
        TypedQuery<OrderPojo> query = getQuery(select_all_order_pojo_by_counter_id, OrderPojo.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    public List<OrderPojo> selectAllOrders() {
        TypedQuery<OrderPojo> query = getQuery(select_all_order_pojo, OrderPojo.class);
        return query.getResultList();
    }

    public List<OrderPojo> selectOrderBetweenDateRange(ZonedDateTime start, ZonedDateTime end) {
        try {
            TypedQuery<OrderPojo> query = getQuery(select_all_order_pojo_between_start_date_and_end_date, OrderPojo.class);
            query.setParameter("start", start);
            query.setParameter("end", end);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public OrderPojo selectOrderByOrderCode(String orderCode) {
        try {
            TypedQuery<OrderPojo> query = getQuery(select_order_pojo_by_order_code, OrderPojo.class);
            query.setParameter("orderCode", orderCode);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}