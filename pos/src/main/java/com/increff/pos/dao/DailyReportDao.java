package com.increff.pos.dao;

import com.increff.pos.pojo.DailyReportPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class DailyReportDao extends AbstractDao{
    private static String select_daily_report_pojo_by_date = "select p from DailyReportPojo p where invoiceDate=:date";
    private static String select_all_daily_report_pojo_between_start_date_and_end_date = "select p from DailyReportPojo p where invoiceDate>=:start and invoiceDate<=:end";
    private static String select_all_daily_report_pojo = "select p from DailyReportPojo p";

    public DailyReportPojo select(ZonedDateTime date) {
        try {
        TypedQuery<DailyReportPojo> query = getQuery(select_daily_report_pojo_by_date, DailyReportPojo.class);
        query.setParameter("date", date);
        return query.getSingleResult();
    }
        catch(
    NoResultException e){
        return null;
    }
    }
    public List<DailyReportPojo> selectAll() {
        TypedQuery<DailyReportPojo> query = getQuery(select_all_daily_report_pojo, DailyReportPojo.class);
        return query.getResultList();
    }

    public List<DailyReportPojo> getReportBetweenDateRange(ZonedDateTime start, ZonedDateTime end)
    {
            TypedQuery<DailyReportPojo> query = getQuery(select_all_daily_report_pojo_between_start_date_and_end_date, DailyReportPojo.class);
            query.setParameter("start", start);
            query.setParameter("end", end);
            return query.getResultList();

    }
}