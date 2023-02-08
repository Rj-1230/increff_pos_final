package com.increff.pos.dao;

import com.increff.pos.pojo.UserPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDao extends AbstractDao {
    private static String select_user_pojo_by_id = "select p from UserPojo p where userId=:id";
    private static String select_user_pojo_by_email = "select p from UserPojo p where email=:email";
    private static String select_all_user_pojo = "select p from UserPojo p";

    public UserPojo selectUserById(Integer id) {
        try {
            TypedQuery<UserPojo> query = getQuery(select_user_pojo_by_id, UserPojo.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public UserPojo selectUserByEmail(String email) {
        try {
            TypedQuery<UserPojo> query = getQuery(select_user_pojo_by_email, UserPojo.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<UserPojo> selectAll() {
        TypedQuery<UserPojo> query = getQuery(select_all_user_pojo, UserPojo.class);
        return query.getResultList();
    }

}