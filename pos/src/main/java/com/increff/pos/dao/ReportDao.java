package com.increff.pos.dao;

import com.increff.pos.pojo.DailyReportPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.pojo.UserPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

@Repository
public class ReportDao{
    private static String select_dailyReportPojo_by_date = "select p from DailyReportPojo p where date=:date";
    private static String select_all_dailyReportPojo_between_startDate_and_endDate = "select p from DailyReportPojo p where date>=:start and date<=:end";

    private static String select_all_dailyReportPojo = "select p from DailyReportPojo p";
    @PersistenceContext
    EntityManager em;

    public void insert(DailyReportPojo p) {
        em.persist(p);
    }
    public DailyReportPojo select(LocalDate date) {
        try {
        TypedQuery<DailyReportPojo> query = getQuery(select_dailyReportPojo_by_date);
        query.setParameter("date", date);
        return query.getSingleResult();
    }
        catch(
    NoResultException e){
        return null;
    }
    }
    public List<DailyReportPojo> selectAll() {
        TypedQuery<DailyReportPojo> query = getQuery(select_all_dailyReportPojo);
        return query.getResultList();
    }

    public List<DailyReportPojo> selectReportByDateFilter(LocalDate start, LocalDate end)
    {
        try{
            TypedQuery<DailyReportPojo> query = getQuery(select_all_dailyReportPojo_between_startDate_and_endDate);
            query.setParameter("start", start);
            query.setParameter("end", end);
            return query.getResultList();
        }
        catch(Exception e)
        {
            return null;
        }
    }

    TypedQuery<DailyReportPojo> getQuery(String jpql) {
        return em.createQuery(jpql, DailyReportPojo.class);
    }
}