package com.pchome.akbadm.db.dao.report;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdVideoReport;

public class PfpAdVideoReportDAO extends BaseDAO<PfpAdVideoReport, String> implements IPfpAdVideoReportDAO {
    @Override
    public int selectCountByCondition(final Map<String, Object> conditionMap) {
        List<Object[]> result = getHibernateTemplate().execute(
            new HibernateCallback<List<Object[]>>() {
                @Override
                @SuppressWarnings("unchecked")
                public List<Object[]> doInHibernate(Session session) {
                    StringBuffer sql = new StringBuffer();
                    sql.append("select ");
                    sql.append("    count(r.ad_video_report_seq) ");
                    sql.append("from pfp_ad_video_report r, ");
                    sql.append("    pfd_customer_info pfd, ");
                    sql.append("    pfp_customer_info pfp, ");
                    sql.append("    adm_template_product tpro ");
                    sql.append("where 1 = 1 ");
                    if (conditionMap.get("startDate") != null) {
                        sql.append(" and r.ad_video_date >= :startDate ");
                    }
                    if (conditionMap.get("endDate") != null) {
                        sql.append(" and r.ad_video_date <= :endDate ");
                    }
                    if (conditionMap.get("pfdCustomerInfoId") != null) {
                        sql.append(" and r.pfd_customer_info_id = :pfdCustomerInfoId ");
                    }
                    if (conditionMap.get("pfpCustomerInfoId") != null) {
                        sql.append(" and r.customer_info_id = :pfpCustomerInfoId ");
                    }
                    if (conditionMap.get("memberId") != null) {
                        sql.append(" and pfp.member_id = :memberId ");
                    }
                    if (conditionMap.get("adId") != null) {
                        sql.append(" and r.ad_seq = :adId ");
                    }
                    if (conditionMap.get("adPriceType") != null) {
                        sql.append(" and r.ad_price_type = :adPriceType ");
                    }
                    if (conditionMap.get("adDevice") != null) {
                        sql.append(" and r.ad_pvclk_device = :adDevice ");
                    }
                    if (conditionMap.get("tproWidth") != null) {
                        sql.append(" and tpro.template_product_width = :tproWidth ");
                    }
                    if (conditionMap.get("tproHeight") != null) {
                        sql.append(" and tpro.template_product_height = :tproHeight ");
                    }
                    sql.append("    and r.pfd_customer_info_id = pfd.customer_info_id ");
                    sql.append("    and r.customer_info_id = pfp.customer_info_id ");
                    sql.append("    and r.template_product_seq = tpro.template_product_seq ");
                    sql.append("group by r.ad_video_date, ");
                    sql.append("    r.pfd_customer_info_id, ");
                    sql.append("    r.customer_info_id, ");
                    sql.append("    r.ad_seq ");

                    Query query = session.createSQLQuery(sql.toString());
                    if (conditionMap.get("startDate") != null) {
                        query.setString("startDate", (String) conditionMap.get("startDate"));
                    }
                    if (conditionMap.get("endDate") != null) {
                        query.setString("endDate", (String) conditionMap.get("endDate"));
                    }
                    if (conditionMap.get("pfdCustomerInfoId") != null) {
                        query.setString("pfdCustomerInfoId", (String) conditionMap.get("pfdCustomerInfoId"));
                    }
                    if (conditionMap.get("pfpCustomerInfoId") != null) {
                        query.setString("pfpCustomerInfoId", (String) conditionMap.get("pfpCustomerInfoId"));
                    }
                    if (conditionMap.get("memberId") != null) {
                        query.setString("memberId", (String) conditionMap.get("memberId"));
                    }
                    if (conditionMap.get("adId") != null) {
                        query.setString("adId", (String) conditionMap.get("adId"));
                    }
                    if (conditionMap.get("adPriceType") != null) {
                        query.setString("adPriceType", (String) conditionMap.get("adPriceType"));
                    }
                    if (conditionMap.get("adDevice") != null) {
                        query.setString("adDevice", (String) conditionMap.get("adDevice"));
                    }
                    if (conditionMap.get("tproWidth") != null) {
                        query.setString("tproWidth", (String) conditionMap.get("tproWidth"));
                    }
                    if (conditionMap.get("tproHeight") != null) {
                        query.setString("tproHeight", (String) conditionMap.get("tproHeight"));
                    }

                    return query.list();
                }
            });

        return result.size();
    }

    @Override
    public List<Object[]> selectByCondition(final Map<String, Object> conditionMap, final int firstResult, final int maxResults) {
        return getHibernateTemplate().execute(
            new HibernateCallback<List<Object[]>>() {
                @Override
                @SuppressWarnings("unchecked")
                public List<Object[]> doInHibernate(Session session) {
                    StringBuffer sql = new StringBuffer();
                    sql.append("select ");
                    sql.append("    r.ad_video_date, ");
                    sql.append("    r.pfd_customer_info_id, ");
                    sql.append("    pfd.company_name, ");
                    sql.append("    r.customer_info_id, ");
                    sql.append("    pfp.customer_info_title, ");
                    sql.append("    r.ad_seq, ");
                    sql.append("    r.template_product_seq, ");
                    sql.append("    tpro.template_product_width, ");
                    sql.append("    tpro.template_product_height, ");
                    sql.append("    r.ad_price_type, ");
                    sql.append("    r.ad_pvclk_device, ");
                    sql.append("    sum(r.ad_pv), ");
                    sql.append("    sum(r.ad_clk), ");
                    sql.append("    sum(r.ad_vpv), ");
                    sql.append("    sum(r.ad_view), ");
                    sql.append("    sum(r.ad_price), ");
                    sql.append("    sum(r.ad_video_play), ");
                    sql.append("    sum(r.ad_video_music), ");
                    sql.append("    sum(r.ad_video_replay), ");
                    sql.append("    sum(r.ad_video_process_25), ");
                    sql.append("    sum(r.ad_video_process_50), ");
                    sql.append("    sum(r.ad_video_process_75), ");
                    sql.append("    sum(r.ad_video_process_100), ");
                    sql.append("    sum(r.ad_video_uniq), ");
                    sql.append("    sum(r.ad_video_idc) ");
                    sql.append("from pfp_ad_video_report r, ");
                    sql.append("    pfd_customer_info pfd, ");
                    sql.append("    pfp_customer_info pfp, ");
                    sql.append("    adm_template_product tpro ");
                    sql.append("where 1 = 1 ");
                    if (conditionMap.get("startDate") != null) {
                        sql.append(" and r.ad_video_date >= :startDate ");
                    }
                    if (conditionMap.get("endDate") != null) {
                        sql.append(" and r.ad_video_date <= :endDate ");
                    }
                    if (conditionMap.get("adVideoDate") != null) {
                        sql.append(" and r.ad_video_date = :adVideoDate ");
                    }
                    if (conditionMap.get("pfdCustomerInfoId") != null) {
                        sql.append(" and r.pfd_customer_info_id = :pfdCustomerInfoId ");
                    }
                    if (conditionMap.get("pfpCustomerInfoId") != null) {
                        sql.append(" and r.customer_info_id = :pfpCustomerInfoId ");
                    }
                    if (conditionMap.get("memberId") != null) {
                        sql.append(" and pfp.member_id = :memberId ");
                    }
                    if (conditionMap.get("adId") != null) {
                        sql.append(" and r.ad_seq = :adId ");
                    }
                    if (conditionMap.get("adPriceType") != null) {
                        sql.append(" and r.ad_price_type = :adPriceType ");
                    }
                    if (conditionMap.get("adDevice") != null) {
                        sql.append(" and r.ad_pvclk_device = :adDevice ");
                    }
                    if (conditionMap.get("tproWidth") != null) {
                        sql.append(" and tpro.template_product_width = :tproWidth ");
                    }
                    if (conditionMap.get("tproHeight") != null) {
                        sql.append(" and tpro.template_product_height = :tproHeight ");
                    }
                    sql.append("    and r.pfd_customer_info_id = pfd.customer_info_id ");
                    sql.append("    and r.customer_info_id = pfp.customer_info_id ");
                    sql.append("    and r.template_product_seq = tpro.template_product_seq ");
                    sql.append("group by r.ad_video_date, ");
                    sql.append("    r.pfd_customer_info_id, ");
                    sql.append("    r.customer_info_id, ");
                    sql.append("    r.ad_seq ");
                    sql.append("order by r.ad_video_date, ");
                    sql.append("    r.pfd_customer_info_id, ");
                    sql.append("    r.customer_info_id, ");
                    sql.append("    r.ad_seq ");

                    Query query = session.createSQLQuery(sql.toString());
                    if (conditionMap.get("startDate") != null) {
                        query.setString("startDate", (String) conditionMap.get("startDate"));
                    }
                    if (conditionMap.get("endDate") != null) {
                        query.setString("endDate", (String) conditionMap.get("endDate"));
                    }
                    if (conditionMap.get("adVideoDate") != null) {
                        query.setString("adVideoDate", (String) conditionMap.get("adVideoDate"));
                    }
                    if (conditionMap.get("pfdCustomerInfoId") != null) {
                        query.setString("pfdCustomerInfoId", (String) conditionMap.get("pfdCustomerInfoId"));
                    }
                    if (conditionMap.get("pfpCustomerInfoId") != null) {
                        query.setString("pfpCustomerInfoId", (String) conditionMap.get("pfpCustomerInfoId"));
                    }
                    if (conditionMap.get("memberId") != null) {
                        query.setString("memberId", (String) conditionMap.get("memberId"));
                    }
                    if (conditionMap.get("adId") != null) {
                        query.setString("adId", (String) conditionMap.get("adId"));
                    }
                    if (conditionMap.get("adPriceType") != null) {
                        query.setString("adPriceType", (String) conditionMap.get("adPriceType"));
                    }
                    if (conditionMap.get("adDevice") != null) {
                        query.setString("adDevice", (String) conditionMap.get("adDevice"));
                    }
                    if (conditionMap.get("tproWidth") != null) {
                        query.setString("tproWidth", (String) conditionMap.get("tproWidth"));
                    }
                    if (conditionMap.get("tproHeight") != null) {
                        query.setString("tproHeight", (String) conditionMap.get("tproHeight"));
                    }
                    query.setFirstResult(firstResult);
                    query.setMaxResults(maxResults);

                    return query.list();
                }
            });
    }
}
