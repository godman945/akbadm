package com.pchome.akbadm.db.dao.ad;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdPvclkReferer;

public class PfpAdPvclkRefererDAO extends BaseDAO<PfpAdPvclkReferer, String> implements IPfpAdPvclkRefererDAO {
    @Override
    @SuppressWarnings("unchecked")
    public PfpAdPvclkReferer selectOneBeforeDate(Date pvclkDate) {
        PfpAdPvclkReferer pfpAdPvclkReferer = null;

        StringBuffer sql = new StringBuffer();
        sql.append("select * ");
        sql.append("from pfp_ad_pvclk_referer ");
        sql.append("where ad_pvclk_date < :pvclkDate ");
        sql.append("order by ad_pvclk_date ");
        sql.append("limit 1 ");

        SQLQuery query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
        query.addEntity(PfpAdPvclkReferer.class);
        query.setDate("pvclkDate", pvclkDate);

        List<PfpAdPvclkReferer> list = query.list();
        if (list.size() > 0) {
            pfpAdPvclkReferer = list.get(0);
        }

        return pfpAdPvclkReferer;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PfpAdPvclkReferer selectOneByDate(Date pvclkDate) {
        PfpAdPvclkReferer pfpAdPvclkReferer = null;

        StringBuffer sql = new StringBuffer();
        sql.append("select * ");
        sql.append("from pfp_ad_pvclk_referer ");
        sql.append("where ad_pvclk_date = :pvclkDate ");
        sql.append("order by ad_pvclk_date ");
        sql.append("limit 1 ");

        SQLQuery query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
        query.addEntity(PfpAdPvclkReferer.class);
        query.setDate("pvclkDate", pvclkDate);

        List<PfpAdPvclkReferer> list = query.list();
        if (list.size() > 0) {
            pfpAdPvclkReferer = list.get(0);
        }

        return pfpAdPvclkReferer;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PfpAdPvclkReferer selectBackupByDate(Date pvclkDate) {
        PfpAdPvclkReferer pfpAdPvclkReferer = null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(pvclkDate);
        int year = calendar.get(Calendar.YEAR);

        StringBuffer sql = new StringBuffer();
        sql.append("select * ");
        sql.append("from pfp_ad_pvclk_referer").append("_").append(year).append(" ");
        sql.append("where ad_pvclk_date = :pvclkDate ");
        sql.append("order by ad_pvclk_date ");
        sql.append("limit 1 ");

        SQLQuery query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
        query.addEntity(PfpAdPvclkReferer.class);
        query.setDate("pvclkDate", pvclkDate);

        List<PfpAdPvclkReferer> list = query.list();
        if (list.size() > 0) {
            pfpAdPvclkReferer = list.get(0);
        }

        return pfpAdPvclkReferer;
    }

    @Override
    public int selectCountByDate(Date pvclkDate) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(ad_pvclk_referer_id) ");
        sql.append("from pfp_ad_pvclk_referer ");
        sql.append("where ad_pvclk_date = :pvclkDate ");

        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
        query.setDate("pvclkDate", pvclkDate);
        BigInteger result = (BigInteger) query.uniqueResult();
        return result.intValue();
    }

    @Override
    public int replaceSelectByDate(Date pvclkDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(pvclkDate);
        int year = calendar.get(Calendar.YEAR);

        StringBuffer sql = new StringBuffer();
        sql.append("replace into ");
        sql.append("    pfp_ad_pvclk_referer").append("_").append(year).append(" ");
        sql.append("select * ");
        sql.append("from pfp_ad_pvclk_referer ");
        sql.append("where ad_pvclk_date = :pvclkDate ");

        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
        query.setDate("pvclkDate", pvclkDate);
        return query.executeUpdate();
    }

    @Override
    public int deleteByDate(Date pvclkDate) {
        StringBuffer sql = new StringBuffer();
        sql.append("delete ");
        sql.append("from pfp_ad_pvclk_referer ");
        sql.append("where ad_pvclk_date = :pvclkDate ");

        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
        query.setDate("pvclkDate", pvclkDate);
        return query.executeUpdate();
    }
}
