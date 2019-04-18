package com.pchome.akbadm.db.dao.ad;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdKeywordPvclk;
import com.pchome.akbadm.utils.ConvertUtil;

public class PfpAdKeywordPvclkDAO extends BaseDAO<PfpAdKeywordPvclk, String> implements IPfpAdKeywordPvclkDAO {
    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> selectPfpAdKeywordPvclkSums(Date pvclkDate) {
        final StringBuffer hql = new StringBuffer();
        hql.append("select ");
        hql.append("    pfpAdKeyword.adKeywordSeq, ");
        hql.append("    sum(adKeywordPv), ");
        hql.append("    sum(adKeywordClk) ");
        hql.append("from PfpAdKeywordPvclk ");
        hql.append("where adKeywordPvclkDate = ? ");
        hql.append("group by pfpAdKeyword.adKeywordSeq");

        return (List<Object[]>) this.getHibernateTemplate().find(hql.toString(), pvclkDate);
    }

    @Override
    public int[] selectPfpAdKeywordPvclkSum(final String seq, final Date pvclkDate) {
        final StringBuffer sql = new StringBuffer();
        sql.append("select ");
        sql.append("    sum(ad_keyword_pv), ");
        sql.append("    sum(ad_keyword_clk) ");
        sql.append("from pfp_ad_keyword_pvclk ");
        sql.append("where ad_keyword_seq = :seq ");
        sql.append("    and ad_keyword_pvclk_date = :pvclkDate ");

        Object[] results = getHibernateTemplate().execute(
            new HibernateCallback<Object[]>() {
                @Override
                public Object[] doInHibernate(Session session) throws HibernateException {
                    return (Object[]) session.createSQLQuery(sql.toString())
                        .setString("seq", seq)
                        .setDate("pvclkDate", pvclkDate)
                        .uniqueResult();
                }
            });

        int[] r = new int[results.length];
        BigDecimal bigDecimal = null;
        for (int i = 0; i < results.length; i++) {
            bigDecimal = (BigDecimal) results[i];
            if (bigDecimal != null) {
                r[i] = bigDecimal.intValue();
            }
        }

        return r;
    }

    @Override
    public float selectAdKeywordClkPrice(String pvclkDate, String customerInfoId, String actionId) {
        StringBuffer hql = new StringBuffer();
        hql.append("select sum(adKeywordClkPrice) ");
        hql.append("from PfpAdKeywordPvclk ");
        hql.append("where adKeywordPvclkDate = :pvclkDate ");
        if (StringUtils.isNotBlank(actionId)) {
            hql.append("    and pfpAdKeyword.pfpAdGroup.pfpAdAction.adActionSeq = :actionId ");
        }
        if (StringUtils.isNotBlank(customerInfoId)) {
            hql.append("    and pfpAdKeyword.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoId = :customerInfoId ");
        }

        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
        query.setString("pvclkDate", pvclkDate);
        if (StringUtils.isNotBlank(actionId)) {
            query.setString("actionId", actionId);
        }
        if (StringUtils.isNotBlank(customerInfoId)) {
            query.setString("customerInfoId", customerInfoId);
        }
        Double result = (Double) query.uniqueResult();

        return result != null ? result.floatValue() : 0f;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> selectAdKeywordClkPriceByPfpCustomerInfoId(Date pvclkDate) {
        StringBuffer hql = new StringBuffer();
        hql.append("select customerInfoId, sum(adKeywordClkPrice) ");
        hql.append("from PfpAdKeywordPvclk ");
        hql.append("where adKeywordPvclkDate = ? ");
        hql.append("group by pfpAdKeyword.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoId");

        return (List<Object[]>) this.getHibernateTemplate().find(hql.toString(), pvclkDate);
    }

    @Override
    public Map<String, Float> selectAdKeywordClkPriceMapByPfpCustomerInfoId(Date pvclkDate) {
        Map<String, Float> map = new HashMap<String, Float>();

        List<Object[]> list = this.selectAdKeywordClkPriceByPfpCustomerInfoId(pvclkDate);
        for (Object[] obj: list) {
            map.put((String) obj[0], ConvertUtil.convertFloat(obj[1]));
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> selectAdKeywordClkPriceByPfpAdActionId(Date pvclkDate) {
        StringBuffer hql = new StringBuffer();
        hql.append("select adActionSeq, sum(adKeywordClkPrice) ");
        hql.append("from PfpAdKeywordPvclk ");
        hql.append("where adKeywordPvclkDate = ? ");
        hql.append("group by pfpAdKeyword.pfpAdGroup.pfpAdAction.adActionSeq");

        return (List<Object[]>) this.getHibernateTemplate().find(hql.toString(), pvclkDate);
    }

    @Override
    public Map<String, Float> selectAdKeywordClkPriceMapByPfpAdActionId(Date pvclkDate) {
        Map<String, Float> map = new HashMap<String, Float>();

        List<Object[]> list = this.selectAdKeywordClkPriceByPfpAdActionId(pvclkDate);
        for (Object[] obj: list) {
            map.put((String) obj[0], ConvertUtil.convertFloat(obj[1]));
        }

        return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PfpAdKeywordPvclk> selectPfpAdKeywordPvclk(int firstResult, int maxResults) {
        String hql = "from PfpAdKeywordPvclk order by adKeywordPvclkDate desc, adKeywordPvclkTime desc";
        return this.getHibernateTemplate().getSessionFactory().getCurrentSession()
                    .createQuery(hql)
                    .setFirstResult(firstResult)
                    .setMaxResults(maxResults)
                    .list();
    }

    @Override
    public int deleteMalice(Date recordDate, int recordTime) {
        String hql = "delete from PfpAdKeywordPvclk where adKeywordPvclkDate = ? and adKeywordPvclkTime = ? and adKeywordInvalidClk > 0";
        return this.getHibernateTemplate().bulkUpdate(hql, recordDate, recordTime);
    }
}