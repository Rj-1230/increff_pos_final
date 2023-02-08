package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao {
    private static String delete_order_item_pojo_by_id = "delete from OrderItemPojo p where orderItemId=:id";
    private static String select_order_item_pojo_by_id = "select p from OrderItemPojo p where orderItemId=:id";
    private static String select_all_order_item_pojo_by_order_id = "select p from OrderItemPojo p where orderId=:id";
    private static String select_order_item_pojo_by_product_id_and_order_id = "select p from OrderItemPojo p where productId=:productId and orderId=:orderId";


    @PersistenceContext
    EntityManager em;

    public Integer deleteOrderItem(Integer id) {
        Query query = em.createQuery(delete_order_item_pojo_by_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public OrderItemPojo selectOrderItem(Integer id) {
        try {
            TypedQuery<OrderItemPojo> query = getQuery(select_order_item_pojo_by_id, OrderItemPojo.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    public List<OrderItemPojo> selectAllOrderItemsByOrderId(Integer id) {
        TypedQuery<OrderItemPojo> query = getQuery(select_all_order_item_pojo_by_order_id, OrderItemPojo.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    public OrderItemPojo getOrderItemPojoByProductIdAndOrderId(Integer productId, Integer orderId) {
        try {
            TypedQuery<OrderItemPojo> query = getQuery(select_order_item_pojo_by_product_id_and_order_id, OrderItemPojo.class);
            query.setParameter("productId", productId);
            query.setParameter("orderId", orderId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}