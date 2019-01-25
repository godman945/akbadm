package com.pchome.akbadm.db.dao.ad;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdPvclk;
import com.pchome.akbadm.utils.ConvertUtil;
import com.pchome.config.TestConfig;

public class PfpAdPvclkDAO extends BaseDAO<PfpAdPvclk, String> implements IPfpAdPvclkDAO
{
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getListObjPFBDetailByReportDate(Date reportDate) throws Exception
	{
		StringBuffer hql = new StringBuffer();

//		hql.append(" select ");
//		hql.append("pfbx_customer_info_id, ");
//		hql.append("SUM(ad_clk_price), ");
//		hql.append("SUM(ad_invalid_clk_price), ");
//		hql.append("(SUM(ad_clk_price) - SUM(ad_invalid_clk_price)), ");
//		hql.append("(select SUM(total_bonus) from pfbx_bonus_day_report a where a.pfb_id=b.pfbx_customer_info_id and report_date = :reportDate group by pfb_id) ");
//		hql.append("from pfp_ad_pvclk b ");
//		hql.append("where ad_pvclk_date = :reportDate group by pfbx_customer_info_id desc ");

		hql.append(" SELECT ");
		hql.append(" a.pfbx_customer_info_id, ");
		hql.append(" SUM(a.ad_clk_price) ad_clk_price, ");
		hql.append(" SUM(a.ad_invalid_clk_price) ad_invalid_clk_price, ");
		hql.append(" (SUM(a.ad_clk_price) - SUM(a.ad_invalid_clk_price)), ");
		hql.append(" (SELECT ");
		hql.append(" SUM(total_bonus) ");
		hql.append(" FROM pfbx_bonus_day_report r ");
		hql.append(" WHERE r.pfb_id = a.pfbx_customer_info_id ");
		hql.append(" AND r.report_date = a.ad_pvclk_date ");
		hql.append(" GROUP BY r.pfb_id) ");
		hql.append(" total_bonus, ");
		hql.append(" a.ad_pvclk_date ");
		hql.append(" FROM (SELECT ");
		hql.append(" FLOOR(SUM(ad_clk_price)) ad_clk_price, ");
		hql.append(" SUM(ad_invalid_clk_price) ad_invalid_clk_price, ");
		hql.append(" pfbx_customer_info_id, ");
		hql.append(" ad_pvclk_date ");
		hql.append(" FROM pfp_ad_pvclk ");
		hql.append(" WHERE 1 = 1 ");
		hql.append(" AND ad_pvclk_date = :reportDate ");
		hql.append(" GROUP BY customer_info_id, ");
		hql.append(" ad_pvclk_date, ");
		hql.append(" pfbx_customer_info_id) a ");
		hql.append(" GROUP BY a.ad_pvclk_date, a.pfbx_customer_info_id ");
		Query q = this.getSession().createSQLQuery(hql.toString());
		q.setDate("reportDate", reportDate);
		List<Object[]> list = q.list();

		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getListObjPFPDetailByReportDate(Date reportDate) throws Exception
	{
		StringBuffer hql = new StringBuffer();

		hql.append("select ");
		hql.append("customer_info_id, ");
		hql.append("FLOOR(SUM(ad_clk_price)), ");
		hql.append("SUM(ad_invalid_clk_price), ");
		hql.append("(select SUM(loss_cost) from adm_trans_loss a where a.customer_info_id=b.customer_info_id and trans_date = :reportDate), ");
		hql.append("(FLOOR(SUM(ad_clk_price)) - SUM(ad_invalid_clk_price)) ");
		hql.append("from pfp_ad_pvclk b where ad_pvclk_date = :reportDate group by customer_info_id desc");

		Query q = this.getSession().createSQLQuery(hql.toString());
		q.setDate("reportDate", reportDate);
		List<Object[]> list = q.list();

		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getListObjByDate1(Date startDate, Date endDate) throws Exception
	{
		StringBuffer hql = new StringBuffer();
		List<Object> pos = new ArrayList<Object>();

		hql.append("select adPvclkDate , (SUM(adClkPrice) - SUM(adInvalidClkPrice)) from PfpAdPvclk where adPvclkDate between ? and ? group by adPvclkDate order by adPvclkDate desc");
		pos.add(startDate);
		pos.add(endDate);

		List<Object[]> list = super.getHibernateTemplate().find(hql.toString(), pos.toArray());
		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getListObjByDate(Date startDate, Date endDate) throws Exception
	{
		StringBuffer hql = new StringBuffer();
		List<Object> pos = new ArrayList<Object>();

		hql.append("select adClkPrice , adInvalidClkPrice , adPvclkDate from PfpAdPvclk where adPvclkDate between ? and ? ");
		pos.add(startDate);
		pos.add(endDate);

		List<Object[]> list = super.getHibernateTemplate().find(hql.toString(), pos.toArray());
		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Map> getListMapByDate(Date startDate, Date endDate) throws Exception
	{
		List<Map> maps = new ArrayList<Map>();

		StringBuffer hql = new StringBuffer();
		hql.append("select ");
		hql.append("ad_clk_price , ");
		hql.append("ad_invalid_clk_price , ");
		hql.append("ad_pvclk_date ");
		hql.append("from pfp_ad_pvclk ");
		hql.append("where ad_pvclk_date ");
		hql.append("between :startDate and :endDate ");

		Query q = super.getSession().createSQLQuery(hql.toString());
		q.setDate("startDate", startDate).setDate("endDate", endDate);

		maps = q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

		return maps;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PfpAdPvclk> getListByDate(Date startDate, Date endDate) throws Exception
	{
		StringBuffer hql = new StringBuffer();
		List<Object> pos = new ArrayList<Object>();

		hql.append("from PfpAdPvclk where adPvclkDate between ? and ? ");

		pos.add(startDate);
		pos.add(endDate);

		List<PfpAdPvclk> list = super.getHibernateTemplate().find(hql.toString(), pos.toArray());

		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object> findAdPvclkTotalCost(String startDate, String endDate) throws Exception
	{

		StringBuffer hql = new StringBuffer();

		hql.append(" select sum(adPv), sum(adClk), sum(adClkPrice), sum(adClkInvalid), sum(adClkInvalidPrice), ");
		hql.append(" 		pfpAd.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoTitle, ");
		hql.append(" 		pfpAd.pfpAdGroup.pfpAdAction.adActionName, ");
		hql.append(" 		pfpAd.pfpAdGroup.pfpAdAction.adActionMax ");
		hql.append(" from PfpAdPvclk ");
		hql.append(" where DATE_FORMAT(adPvclkCreateTime,'%Y-%m-%d') >= '" + startDate + "' ");
		hql.append(" and DATE_FORMAT(adPvclkCreateTime,'%Y-%m-%d') <= '" + endDate + "' ");
		hql.append(" group by pfpAd.pfpAdGroup.pfpAdAction.adActionSeq ");

		// log.info(" hql = "+hql.toString());

		return super.getHibernateTemplate().find(hql.toString());
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object> findAdPvclkShortCost(String startDate, String endDate) throws Exception
	{

		StringBuffer hql = new StringBuffer();

		hql.append(" select sum(adPv), sum(adClk), sum(adClkPrice), sum(adClkInvalid), sum(adClkInvalidPrice), ");
		hql.append(" 		pfpAd.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoTitle, ");
		hql.append(" 		pfpAd.pfpAdGroup.pfpAdAction.adActionName, ");
		hql.append(" 		pfpAd.pfpAdGroup.pfpAdAction.adActionMax ");
		hql.append(" from PfpAdPvclk ");
		hql.append(" where DATE_FORMAT(adPvclkCreateTime,'%Y-%m-%d') >= '" + startDate + "' ");
		hql.append(" and DATE_FORMAT(adPvclkCreateTime,'%Y-%m-%d') <= '" + endDate + "' ");
		hql.append(" group by adPvclkCreateTime ");

		// log.info(" hql = "+hql.toString());

		return super.getHibernateTemplate().find(hql.toString());
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object> accountPvclkSum(String customerInfoId, Date date)
	{

		StringBuffer hql = new StringBuffer();

		hql.append(" select pfpAd.pfpAdGroup.pfpAdAction.adActionSeq, ");
		hql.append(" 		 pfpAd.pfpAdGroup.pfpAdAction.adActionMax, sum(adClkPrice) ");
		hql.append(" from PfpAdPvclk ");
		hql.append(" where pfpAd.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoId = ? ");
		hql.append(" and adPvclkDate = ? ");

		Object[] ob = new Object[]
		{ customerInfoId, date };

		return super.getHibernateTemplate().find(hql.toString(), ob);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object> adActionPvclkSum(String customerInfoId, Date date)
	{

		StringBuffer hql = new StringBuffer();

		hql.append(" select pfpAd.pfpAdGroup.pfpAdAction.adActionSeq, ");
		hql.append(" 		 pfpAd.pfpAdGroup.pfpAdAction.adActionMax, ");
		hql.append(" 		 COALESCE(sum(adClkPrice),0), ");
		hql.append(" 		 pfpAd.pfpAdGroup.pfpAdAction.adActionControlPrice, ");
		hql.append(" 		 pfpAd.pfpAdGroup.pfpAdAction.changeMax ");
		hql.append(" from PfpAdPvclk ");
		hql.append(" where adPvclkDate = ? ");
        hql.append(" and pfpAd.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoId = ? ");
		hql.append(" group by pfpAd.pfpAdGroup.pfpAdAction.adActionSeq ");

		Object[] ob = new Object[]
		{ date, customerInfoId };

		return super.getHibernateTemplate().find(hql.toString(), ob);
	}

    @Override
    public float customerCost(String pfpCustomerId, Date pvclkDate) {
        StringBuffer hql = new StringBuffer();
        hql.append("select sum(adClkPrice) ");
        hql.append("from PfpAdPvclk ");
        hql.append("where adPvclkDate = :pvclkDate ");
        hql.append("and customerInfoId = :pfpCustomerId ");

        Query query = this.getSession().createQuery(hql.toString());
        query.setDate("pvclkDate", pvclkDate);
        query.setString("pfpCustomerId", pfpCustomerId);

        Double result = (Double) query.uniqueResult();
        return result != null ? result.floatValue() : 0f;
    }

    @Override
	public float actionCost(String actionId, Date pvclkDate) {
        StringBuffer hql = new StringBuffer();
        hql.append("select sum(adClkPrice) ");
        hql.append("from PfpAdPvclk ");
        hql.append("where adPvclkDate = :pvclkDate ");
        hql.append("and adActionSeq = :actionId ");

        Query query = this.getSession().createQuery(hql.toString());
        query.setDate("pvclkDate", pvclkDate);
        query.setString("actionId", actionId);

        Double result = (Double) query.uniqueResult();
        return result != null ? result.floatValue() : 0f;
	}

	/**
	 * sum by date, id
	 *
	 * @return adPv, adClk, adClkPrice, adInvalidClk, adInvalidClkPrice
	 */
	@Override
	public Float[] selectAdPvclkSumByPvclkDate(String pvclkDate, String customerInfoId, String adActionSeq, String adGroupSeq, String adSeq)
	{
		List<String> list = new ArrayList<String>();

		StringBuffer hql = new StringBuffer();
		hql.append("select ");
		hql.append("    sum(adPv), ");
		hql.append("    sum(adClk), ");
		hql.append("    sum(adClkPrice), ");
		hql.append("    sum(adInvalidClk), ");
		hql.append("    sum(adInvalidClkPrice) ");
		hql.append("from PfpAdPvclk ");
		hql.append("where adPvclkDate = ? ");
		hql.append("    and pfpAd.pfpAdGroup.pfpAdAction.pfpCustomerInfo.recognize = 'Y' ");
		list.add(pvclkDate);

		if (StringUtils.isNotBlank(adSeq))
		{
			hql.append("and pfpAd.adSeq = ? ");
			list.add(adSeq);
		}
		if (StringUtils.isNotBlank(adGroupSeq))
		{
			hql.append("and pfpAd.pfpAdGroup.adGroupSeq = ? ");
			list.add(adGroupSeq);
		}
		if (StringUtils.isNotBlank(adActionSeq))
		{
			hql.append("and pfpAd.pfpAdGroup.pfpAdAction.adActionSeq = ? ");
			list.add(adActionSeq);
		}
		if (StringUtils.isNotBlank(customerInfoId))
		{
			hql.append("and pfpAd.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoId = ? ");
			list.add(customerInfoId);
		}

		Query query = this.getSession().createQuery(hql.toString());
		for (int i = 0; i < list.size(); i++)
		{
			query.setString(i, list.get(i));
		}
		return ConvertUtil.convertFloat((Object[]) query.uniqueResult());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> selectPfpAdPvclkSums(Date pvclkDate)
	{
		StringBuffer hql = new StringBuffer();
		hql.append("select ");
		hql.append("    pfpAd.adSeq, ");
		hql.append("    sum(adPv), ");
		hql.append("    sum(adClk), ");
        hql.append("    sum(adView) ");
		hql.append("from PfpAdPvclk ");
		hql.append("where adPvclkDate = ? ");
//		hql.append("    and pfpAd.pfpAdGroup.pfpAdAction.pfpCustomerInfo.recognize = 'Y' ");
		hql.append("group by pfpAd.adSeq");

		return this.getHibernateTemplate().find(hql.toString(), pvclkDate);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PfpAdPvclk> selectPfpAdPvclk(int firstResult, int maxResults)
	{
		String hql = "from PfpAdPvclk order by adPvclkDate desc, adPvclkTime desc";
		return super.getSession()
				.createQuery(hql)
				.setFirstResult(firstResult)
				.setMaxResults(maxResults)
				.list();
	}

	// @Override
	// public float selectAdClkPrice(String pvclkDate, String customerInfoId,
	// String actionId) {
	// StringBuffer hql = new StringBuffer();
	// hql.append("select sum(adClkPrice) ");
	// hql.append("from PfpAdPvclk ");
	// hql.append("where adPvclkDate = :pvclkDate ");
	// if (StringUtils.isNotBlank(actionId)) {
	// hql.append("and pfpAd.pfpAdGroup.pfpAdAction.adActionSeq = :actionId ");
	// }
	// if (StringUtils.isNotBlank(customerInfoId)) {
	// hql.append("and pfpAd.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoId = :customerInfoId ");
	// }
	//
	// Query query = this.getSession().createQuery(hql.toString());
	// query.setString("pvclkDate", pvclkDate);
	// if (StringUtils.isNotBlank(actionId)) {
	// query.setString("actionId", actionId);
	// }
	// if (StringUtils.isNotBlank(customerInfoId)) {
	// query.setString("customerInfoId", customerInfoId);
	// }
	// Double result = (Double) query.uniqueResult();
	//
	// return result != null ? result.floatValue() : 0f;
	// }

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> selectAdClkPriceByPfpCustomerInfoId(Date pvclkDate)
	{
		StringBuffer hql = new StringBuffer();
		hql.append("select customerInfoId, sum(adClkPrice) ");
		hql.append("from PfpAdPvclk ");
		hql.append("where adPvclkDate = ? ");
		hql.append("group by customerInfoId");

		return this.getHibernateTemplate().find(hql.toString(), pvclkDate);
	}

	@Override
	public Map<String, Float> selectAdClkPriceMapByPfpCustomerInfoId(Date pvclkDate)
	{
		Map<String, Float> map = new HashMap<String, Float>();

		List<Object[]> list = this.selectAdClkPriceByPfpCustomerInfoId(pvclkDate);
		for (Object[] obj : list)
		{
			map.put((String) obj[0], ConvertUtil.convertFloat(obj[1]));
		}

		return map;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> selectAdClkPriceByPfpAdActionId(Date pvclkDate)
	{
		StringBuffer hql = new StringBuffer();
		hql.append("select adActionSeq, sum(adClkPrice) ");
		hql.append("from PfpAdPvclk ");
		hql.append("where adPvclkDate = ? ");
		hql.append("group by adActionSeq");

		return this.getHibernateTemplate().find(hql.toString(), pvclkDate);
	}

	@Override
	public Map<String, Float> selectAdClkPriceMapByPfpAdActionId(Date pvclkDate)
	{
		Map<String, Float> map = new HashMap<String, Float>();

		List<Object[]> list = this.selectAdClkPriceByPfpAdActionId(pvclkDate);
		for (Object[] obj : list)
		{
			map.put((String) obj[0], ConvertUtil.convertFloat(obj[1]));
		}

		return map;
	}

	@Override
	public float totalSysAdPvclk(Date startDate, Date endDate)
	{

		StringBuffer hql = new StringBuffer();

		hql.append(" select sum(adClkPrice-adInvalidClkPrice) ");
		hql.append(" from PfpAdPvclk ");
		hql.append(" where 1 = 1 ");
		hql.append(" and adPvclkDate between :startDate and :endDate ");

		Query query = this.getSession().createQuery(hql.toString());

		query.setDate("startDate", startDate)
				.setDate("endDate", endDate);

		Double result = (Double) query.uniqueResult();

		return result != null ? result.floatValue() : 0f;
	}

	@Override
	public float totalPfbAdPvclk(String pfbId, Date startDate, Date endDate)
	{
		log.info("pfbId＞＞＞＞＞＞＞＞＞＞＞＞＞"+pfbId);
		log.info("startDate＞＞＞＞＞＞＞＞＞＞＞＞＞"+startDate);
		log.info("endDate＞＞＞＞＞＞＞＞＞＞＞＞＞"+endDate);
		StringBuffer hql = new StringBuffer();
		hql.append(" select sum(adClkPrice) ");
		hql.append(" from PfpAdPvclk ");
		hql.append(" where 1 = 1 ");
		if (StringUtils.isNotBlank(pfbId))
		{
			hql.append(" and pfbxCustomerInfoId = :pfbId ");
		}
		hql.append(" and adPvclkDate between :startDate and :endDate ");
		hql.append(" GROUP BY  customerInfoId  ");
		Query query = this.getSession().createQuery(hql.toString());

		if (StringUtils.isNotBlank(pfbId))
		{
			query.setString("pfbId", pfbId);
		}

		query.setDate("startDate", startDate);
		query.setDate("endDate", endDate);

//		if(StringUtils.isNotBlank(pfbId) && pfbId.equals("PFBC20150519001")){
//			System.out.println("SSSSSAAAAAAAAAAAAAAAAAAAAA");
//
//			StringBuffer hql2 = new StringBuffer();
//			hql2.append(" select sum(adClkPrice) ");
//			hql2.append(" from PfpAdPvclk ");
//			hql2.append(" where 1 = 1 ");
//			if (StringUtils.isNotBlank(pfbId))
//			{
//				hql2.append(" and pfbxCustomerInfoId = :pfbId ");
//			}
//			hql2.append(" and adPvclkDate between :startDate and :endDate ");
//			hql2.append(" GROUP BY  customerInfoId  ");
//			Query query2 = this.getSession().createQuery(hql2.toString());
//
//			if (StringUtils.isNotBlank(pfbId))
//			{
//				query2.setString("pfbId", pfbId);
//			}
//
//			query2.setDate("startDate", startDate);
//			query2.setDate("endDate", endDate);
//			List<Double> list =  query2.list();
//			System.out.println(list.size());
//			System.out.println(list);
//			double adClkPrice = 0;
//			for (Double a : list) {
//				adClkPrice = adClkPrice + Math.floor(a);
//			}
//			System.out.println(adClkPrice);
//			System.out.println("SSSSSAAAAAAAAAAAAAAAAAAAAA");
//////			System.out.println("ALEX@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+list.size());
//		}
		double adClkPrice = 0;
		List<Double> list =  query.list();
		for (Double cliclPriceBypfp : list) {
			adClkPrice = adClkPrice + Math.floor(cliclPriceBypfp);
		}
		return  Float.parseFloat(String.valueOf(adClkPrice));
//		Double result = (Double) query.uniqueResult();
//		return result != null ? result.floatValue() : 0f;
//		return  0f;
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<Object[]> findPfpAdPvclks(Date date)
	{

		StringBuffer hql = new StringBuffer();

		hql.append(" select customerInfoId, sum(adClkPrice), sum(adInvalidClkPrice) ");
		hql.append(" from PfpAdPvclk ");
		hql.append(" where adPvclkDate = ? ");
		hql.append(" group by customerInfoId ");

		return super.getHibernateTemplate().find(hql.toString(), date);
	}

	@Override
	public Integer updateEmptyPfbxCustomerPostion(Date date, String pfbxCustomerID, String pfbxPostionID)
	{

		StringBuffer hql = new StringBuffer();

		hql.append(" update PfpAdPvclk");
		hql.append(" set pfbxCustomerInfoId = :coustomerId , ");
		hql.append(" pfbxPositionId = :postionId ");
		hql.append(" where adPvclkDate = :processDate ");
		hql.append(" and (pfbxPositionId = '' or pfbxPositionId is null)");

		Query query = this.getSession().createQuery(hql.toString());

		query.setString("coustomerId", pfbxCustomerID).setString("postionId", pfbxPostionID).setDate("processDate", date);

		return query.executeUpdate();
	}

	@Override
	public Integer updateEmptyUrl(Date date)
	{
		StringBuffer hql = new StringBuffer();

		hql.append(" update PfpAdPvclk");
		hql.append(" set adUrl = 'none referer'");
		hql.append(" where adPvclkDate = :processDate ");
		hql.append(" and (adUrl = '' or adUrl is null)");

		Query query = this.getSession().createQuery(hql.toString());

		query.setDate("processDate", date);

		return query.executeUpdate();
	}

    @Override
    public List<PfpAdPvclk> findLastPfpAdPvclk() {
        String hql = "from PfpAdPvclk order by adPvclkDate desc, adPvclkTime desc";
        Query query = this.getSession().createQuery(hql)
                                        .setFirstResult(0)
                                        .setMaxResults(1);
        return query.list();
    }

    @Override
    public Map<String,Boolean> checkPvClk(String date){
    	Map<String,Boolean> checkMap = new HashMap<String,Boolean>();

    	//取得pfp_ad_pvclk的pv、clk總數
    	int pv = 0;
    	int clk = 0;
    	int invalidClk = 0;
    	StringBuffer sql1 = new StringBuffer();
    	sql1.append("select ifnull(sum(ad_pv),0), ifnull(sum(ad_clk),0), ifnull(sum(ad_invalid_clk),0) ");
    	sql1.append("from pfp_ad_pvclk ");
    	sql1.append("where 1=1 ");
    	if(StringUtils.isNotEmpty(date)){
    		sql1.append("and ad_pvclk_date = '" + date + "'");
    	}

    	Query query1 = this.getSession().createSQLQuery(sql1.toString());

    	List<Object> dataList1 = query1.list();
    	Object[] objArray1 = (Object[]) dataList1.get(0);
    	pv = Integer.parseInt(objArray1[0].toString());
    	clk = Integer.parseInt(objArray1[1].toString());
    	invalidClk = Integer.parseInt(objArray1[2].toString());
    	log.info(">>>>>>>>>>>>>>>>>>pv=" + pv);
    	log.info(">>>>>>>>>>>>>>>>>clk=" + clk);

    	//取得pfp_ad_action_report的pv、clk總數
    	int pfpPv = 0;
    	int pfpClk = 0;

    	StringBuffer sql2 = new StringBuffer();
    	sql2.append("select ifnull(sum(ad_pv),0), ifnull(sum(ad_clk),0) ");
    	sql2.append("from pfp_ad_action_report ");
    	sql2.append("where 1=1 ");
    	if(StringUtils.isNotEmpty(date)){
    		sql2.append("and ad_pvclk_date = '" + date + "'");
    	}

    	Query query2 = this.getSession().createSQLQuery(sql2.toString());

    	List<Object> dataList2 = query2.list();
    	Object[] objArray2 = (Object[]) dataList2.get(0);
    	pfpPv = Integer.parseInt(objArray2[0].toString());
    	pfpClk = Integer.parseInt(objArray2[1].toString());
    	log.info(">>>>>>>>>>>>>>>>>>pfpPv=" + pfpPv);
    	log.info(">>>>>>>>>>>>>>>>>>pfpClk=" + pfpClk);

    	if(pv == pfpPv){
    		checkMap.put("pfpPv", true);
    	} else {
    		checkMap.put("pfpPv", false);
    	}

    	if((clk - invalidClk) == pfpClk){
    		checkMap.put("pfpClk", true);
    	} else {
    		checkMap.put("pfpClk", false);
    	}

    	//取得pfbx_ad_time_report的pv、clk總數
    	int pfbPv = 0;
    	int pfbClk = 0;

    	StringBuffer sql3 = new StringBuffer();
    	sql3.append("select ifnull(sum(ad_pv),0), ifnull(sum(ad_clk),0) ");
    	sql3.append("from pfbx_ad_time_report ");
    	sql3.append("where 1=1 ");
    	if(StringUtils.isNotEmpty(date)){
    		sql3.append("and ad_pvclk_date = '" + date + "'");
    	}

    	Query query3 = this.getSession().createSQLQuery(sql3.toString());

    	List<Object> dataList3 = query3.list();
    	Object[] objArray3 = (Object[]) dataList3.get(0);
    	pfbPv = Integer.parseInt(objArray3[0].toString());
    	pfbClk = Integer.parseInt(objArray3[1].toString());
    	log.info(">>>>>>>>>>>>>>>>>>pfbPv=" + pfbPv);
    	log.info(">>>>>>>>>>>>>>>>>>pfbClk=" + pfbClk);

    	if(pv == pfbPv){
    		checkMap.put("pfbPv", true);
    	} else {
    		checkMap.put("pfbPv", false);
    	}

    	if(clk == pfbClk){
    		checkMap.put("pfbClk", true);
    	} else {
    		checkMap.put("pfbClk", false);
    	}

    	//取得pfd_ad_action_report的pv、clk總數
    	int pfdPv = 0;
    	int pfdClk = 0;

    	StringBuffer sql4 = new StringBuffer();
    	sql4.append("select ifnull(sum(ad_pv),0), ifnull(sum(ad_clk),0) ");
    	sql4.append("from pfd_ad_action_report ");
    	sql4.append("where 1=1 ");
    	if(StringUtils.isNotEmpty(date)){
    		sql4.append("and ad_pvclk_date = '" + date + "'");
    	}

    	Query query4 = this.getSession().createSQLQuery(sql4.toString());

    	List<Object> dataList4 = query4.list();
    	Object[] objArray4 = (Object[]) dataList4.get(0);
    	pfdPv = Integer.parseInt(objArray4[0].toString());
    	pfdClk = Integer.parseInt(objArray4[1].toString());
    	log.info(">>>>>>>>>>>>>>>>>>pfdPv=" + pfdPv);
    	log.info(">>>>>>>>>>>>>>>>>>pfdClk=" + pfdClk);

    	if(pv == pfdPv){
    		checkMap.put("pfdPv", true);
    	} else {
    		checkMap.put("pfdPv", false);
    	}

    	if((clk - invalidClk) == pfdClk){
    		checkMap.put("pfdClk", true);
    	} else {
    		checkMap.put("pfdClk", false);
    	}

    	return checkMap;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PfpAdPvclk selectOneBeforeDate(Date pvclkDate) {
        PfpAdPvclk pfpAdPvclk = null;

        StringBuffer sql = new StringBuffer();
        sql.append("select * ");
        sql.append("from pfp_ad_pvclk ");
        sql.append("where ad_pvclk_date < :pvclkDate ");
        sql.append("order by ad_pvclk_date ");
        sql.append("limit 1 ");

        SQLQuery query = this.getSession().createSQLQuery(sql.toString());
        query.addEntity(PfpAdPvclk.class);
        query.setDate("pvclkDate", pvclkDate);

        List<PfpAdPvclk> list = query.list();
        if (list.size() > 0) {
            pfpAdPvclk = list.get(0);
        }

        return pfpAdPvclk;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PfpAdPvclk selectOneByDate(Date pvclkDate) {
        PfpAdPvclk pfpAdPvclk = null;

        StringBuffer sql = new StringBuffer();
        sql.append("select * ");
        sql.append("from pfp_ad_pvclk ");
        sql.append("where ad_pvclk_date = :pvclkDate ");
        sql.append("order by ad_pvclk_date ");
        sql.append("limit 1 ");

        SQLQuery query = this.getSession().createSQLQuery(sql.toString());
        query.addEntity(PfpAdPvclk.class);
        query.setDate("pvclkDate", pvclkDate);

        List<PfpAdPvclk> list = query.list();
        if (list.size() > 0) {
            pfpAdPvclk = list.get(0);
        }

        return pfpAdPvclk;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PfpAdPvclk selectBackupByDate(Date pvclkDate) {
        PfpAdPvclk pfpAdPvclk = null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(pvclkDate);
        int year = calendar.get(Calendar.YEAR);

        StringBuffer sql = new StringBuffer();
        sql.append("select * ");
        sql.append("from pfp_ad_pvclk").append("_").append(year).append(" ");
        sql.append("where ad_pvclk_date = :pvclkDate ");
        sql.append("order by ad_pvclk_date ");
        sql.append("limit 1 ");

        SQLQuery query = this.getSession().createSQLQuery(sql.toString());
        query.addEntity(PfpAdPvclk.class);
        query.setDate("pvclkDate", pvclkDate);

        List<PfpAdPvclk> list = query.list();
        if (list.size() > 0) {
            pfpAdPvclk = list.get(0);
        }

        return pfpAdPvclk;
    }

    @Override
    public int selectCountByDate(Date pvclkDate) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(ad_pvclk_seq) ");
        sql.append("from pfp_ad_pvclk ");
        sql.append("where ad_pvclk_date = :pvclkDate ");

        Query query = this.getSession().createSQLQuery(sql.toString());
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
        sql.append("    pfp_ad_pvclk").append("_").append(year).append(" ");
        sql.append("select * ");
        sql.append("from pfp_ad_pvclk ");
        sql.append("where ad_pvclk_date = :pvclkDate ");

        Query query = this.getSession().createSQLQuery(sql.toString());
        query.setDate("pvclkDate", pvclkDate);
        return query.executeUpdate();
    }

    @Override
    public int deleteByDate(Date pvclkDate) {
        StringBuffer sql = new StringBuffer();
        sql.append("delete ");
        sql.append("from pfp_ad_pvclk ");
        sql.append("where ad_pvclk_date = :pvclkDate ");

        Query query = this.getSession().createSQLQuery(sql.toString());
        query.setDate("pvclkDate", pvclkDate);
        return query.executeUpdate();
    }

	public static void main(String[] args) throws Exception
	{
		ApplicationContext context = null;

		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));

		PfpAdPvclkDAO daoa = context.getBean(PfpAdPvclkDAO.class);

		try
		{
			List<Object[]> list = daoa.getListObjByDate1(sdf.parse("2015-06-01"), sdf.parse("2015-06-09"));

			System.err.println("...list size=" + list.size());
		}
		catch(Exception ex)
		{
			System.err.println("Error:" + ex);
		}
	}

    @Override
    public int deleteMalice(Date recordDate, int recordTime) {
        String hql = "delete from PfpAdPvclk where adPvclkDate = ? and adPvclkTime = ? and adInvalidClk > 0";
        return this.getHibernateTemplate().bulkUpdate(hql, recordDate, recordTime);
    }
}
