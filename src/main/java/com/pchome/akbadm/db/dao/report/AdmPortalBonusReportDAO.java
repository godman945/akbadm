package com.pchome.akbadm.db.dao.report;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmPortalBonusReport;
import com.pchome.akbadm.db.vo.report.AdmClientCountReportVO;
import com.pchome.akbadm.db.vo.report.AdmPortalBonusReportVO;

public class AdmPortalBonusReportDAO extends BaseDAO<AdmPortalBonusReport, Integer> implements IAdmPortalBonusReportDAO {

	@Override
	public Map<String,Object> getPfdBonusDayReport(final String reportDate) throws Exception {
		Map<String,Object> result = getHibernateTemplate().execute(
				new HibernateCallback<Map<String,Object>>() {
					@Override
                    public Map<String,Object> doInHibernate(Session session) throws HibernateException {

						Map<String,Object> resultMap = new HashMap<String,Object>();
						
						StringBuffer sql = new StringBuffer();

						sql.append(" select ");
						sql.append(" report_date, ");
						sql.append(" pfd_id, ");
						sql.append(" ifnull(sum(save_clk_price + free_clk_price + postpaid_clk_price),0), ");		//廣告花費
						sql.append(" ifnull(sum(pfd_bonus_money),0) ");												//經銷商獎金
						sql.append(" from pfd_bonus_day_report ");
						sql.append(" where pfd_id in ('PFDC20140520001','PFDC20141024003','PFDC20150422001') ");
						sql.append(" and report_date = '" + reportDate + "' ");
						sql.append(" group by report_date, pfd_id ");

						String sqlString = sql.toString();
//						log.info(">>> sql = " + sqlString);

						Query q = session.createSQLQuery(sqlString);

						List<Object> dataList = new ArrayList<Object>();
						dataList = q.list();
//						log.info(">>> resultData.size() = " + resultData.size());
						
						for (int i=0; i<dataList.size(); i++) {
							
							Object[] objArray = (Object[]) dataList.get(i);
							String pfdId = (objArray[1]).toString();
							
							resultMap.put(pfdId, objArray);
						}

						return resultMap;
					}
				});
		return result;
	}
	
	@Override
	public Map<String,Integer> getPfpClientCountMap(final String reportDate) throws Exception {
		Map<String,Integer> result = getHibernateTemplate().execute(
				new HibernateCallback<Map<String,Integer>>() {
					@Override
                    public Map<String,Integer> doInHibernate(Session session) throws HibernateException {

						Map<String,Integer> resultMap = new HashMap<String,Integer>();
						
						StringBuffer sql = new StringBuffer();

						sql.append(" select ");
						sql.append(" ad_pvclk_date, ");
						sql.append(" pfd_customer_info_id, ");
						sql.append(" count(distinct customer_info_id) ");		
						sql.append(" from pfp_ad_pvclk ");
						sql.append(" where pfd_customer_info_id in ('PFDC20140520001','PFDC20141024003','PFDC20150422001') ");
						sql.append(" and ad_pvclk_date = '" + reportDate + "' ");
						sql.append(" group by ad_pvclk_date, pfd_customer_info_id ");

						String sqlString = sql.toString();
//						log.info(">>> sql = " + sqlString);

						Query q = session.createSQLQuery(sqlString);

						List<Object> dataList = new ArrayList<Object>();
						dataList = q.list();
//						log.info(">>> resultData.size() = " + resultData.size());
						
						for (int i=0; i<dataList.size(); i++) {
							
							Object[] objArray = (Object[]) dataList.get(i);
							String pfdId = (objArray[1]).toString();
							Long pfpAdClkPrice = (new Double((objArray[2]).toString())).longValue();
							
							resultMap.put(pfdId, pfpAdClkPrice.intValue());
						}

						return resultMap;
					}
				});
		return result;
	}
	
	@Override
	public float findPortalPfbBonus(final String reportDate) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select sum(totalBonus) ");
		hql.append(" from PfbxBonusDayReport ");
		hql.append(" where pfbxCustomerInfo.customerInfoId in ('PFBC20190424002','PFBC20160804001') ");
    	hql.append(" and reportDate = :reportDate ");


    	Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
    	query.setString("reportDate", reportDate);

    	Double result = (Double) query.uniqueResult();

        return result != null ? result.floatValue() : 0f;
	}
	
	@Override
	public float findPortalOperatingIncome(final String reportDate) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select sum(ifnull(save,0) + ifnull(postpaid,0)) ");
		hql.append(" from AdmBonusDetailReport ");
		hql.append(" where detailItem = '1' ");
    	hql.append(" and reportDate = :reportDate ");


    	Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
    	query.setString("reportDate", reportDate);

    	Double result = (Double) query.uniqueResult();

        return result != null ? result.floatValue() : 0f;
	}
	
	@Override
	public int deleteReportDataByReportDate(String reportDate) {
		 String hql = "delete from AdmPortalBonusReport where adPvclkDate = '" + reportDate + "' ";
		 return this.getHibernateTemplate().bulkUpdate(hql);
	}
	
	@Override
	public List<AdmPortalBonusReportVO> findReportData(final String firstDay, final String lastDay) {
		List<AdmPortalBonusReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<AdmPortalBonusReportVO>>() {
				@Override
                public List<AdmPortalBonusReportVO> doInHibernate(Session session) throws HibernateException {

					List<AdmPortalBonusReportVO> resultData = new ArrayList<AdmPortalBonusReportVO>();
					DecimalFormat df1 = new DecimalFormat("###,###,###,###");
					DecimalFormat df2 = new DecimalFormat("###,###,###,##0.00");

					StringBuffer hql = new StringBuffer();
					hql.append("select ");
					hql.append("ad_pvclk_date, ");
					hql.append("sum(case when pfd_customer_info_id in ('PFDC20140520001') then ad_valid_price else 0.00 end), ");
					hql.append("sum(case when pfd_customer_info_id in ('PFDC20140520001') then pfp_client_count else 0 end), ");
					hql.append("sum(case when pfd_customer_info_id in ('PFDC20140520001') then pfd_bonus else 0.00 end), ");
					hql.append("sum(case when pfd_customer_info_id in ('PFDC20141024003') then ad_valid_price else 0.00 end), ");
					hql.append("sum(case when pfd_customer_info_id in ('PFDC20141024003') then pfp_client_count else 0 end), ");
					hql.append("sum(case when pfd_customer_info_id in ('PFDC20141024003') then pfd_bonus else 0.00 end), ");
					hql.append("sum(case when pfd_customer_info_id in ('PFDC20150422001') then ad_valid_price else 0.00 end), ");
					hql.append("sum(case when pfd_customer_info_id in ('PFDC20150422001') then pfp_client_count else 0 end), ");
					hql.append("sum(case when pfd_customer_info_id in ('PFDC20150422001') then pfd_bonus else 0.00 end), ");
					hql.append("sum(portal_pfb_bonus), ");
					hql.append("sum(portal_operating_income) ");
					hql.append(" from adm_portal_bonus_report ");
					hql.append(" where 1=1 ");

					if(StringUtils.isNotEmpty(firstDay)){
						hql.append(" and ad_pvclk_date >= '" + firstDay + "' ");
					}

					if(StringUtils.isNotEmpty(lastDay)){
						hql.append(" and ad_pvclk_date <= '" + lastDay + "' ");
					}

					hql.append(" group by ad_pvclk_date ");
					hql.append(" order by ad_pvclk_date desc ");

					String sql = hql.toString();
//					log.info(">>> sql = " + sql);
					List<Object> dataList = new ArrayList<Object>();
					try {
					Query query = session.createSQLQuery(sql);
					dataList = query.list();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					log.info(">>> dataList.size() = " + dataList.size());

					for (int i=0; i<dataList.size(); i++) {
						AdmPortalBonusReportVO vo = new AdmPortalBonusReportVO();

						Object[] objArray = (Object[]) dataList.get(i);

						double portalValidPrice = new Double((objArray[3]).toString());
						double ddTestValidPrice = new Double((objArray[6]).toString());
						double salesPfdBonus = new Double((objArray[9]).toString());
						double portalPfbBonus  = new Double((objArray[10]).toString());
						double portalOperatingIncome  = new Double((objArray[11]).toString());
						double incomeSum = portalValidPrice + ddTestValidPrice + salesPfdBonus + portalPfbBonus + portalOperatingIncome;
						
						SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
						String week = "";
						try {
							 week = sdf.format(dateFormate.parse(objArray[0].toString()));
							 week = getChiWeek(week);
							 week = week.substring(2);
						} catch (ParseException e) {
							log.error(">>>>>>>>>>>>>>>>>>>>>>>>日期換算星期錯誤!");
							e.printStackTrace();
						}

						vo.setAdPvclkDate(objArray[0].toString());
						vo.setWeek(week);
						vo.setPortalValidPrice(df2.format(new Double((objArray[1]).toString())));
						vo.setPortalPfpClientCount(df1.format(new Double((objArray[2]).toString())));
						vo.setPortalPfdBonus(df2.format(portalValidPrice));
						vo.setDdTestValidPrice(df2.format(new Double((objArray[4]).toString())));
						vo.setDdTestPfpClientCount(df1.format(new Double((objArray[5]).toString())));
						vo.setDdTestPfdBonus(df2.format(ddTestValidPrice));
						vo.setSalesValidPrice(df2.format(new Double((objArray[7]).toString())));
						vo.setSalesPfpClientCount(df1.format(new Double((objArray[8]).toString())));
						vo.setSalesPfdBonus(df2.format(salesPfdBonus));
						vo.setPortalPfbBonus(df2.format(portalPfbBonus));
						vo.setPortalOperatingIncome(df2.format(portalOperatingIncome));
						vo.setIncomeSum(df2.format(incomeSum));

						resultData.add(vo);
					}

//					log.info("resultData: "+resultData.size());

					return resultData;
				}
		});
		return result;
	}
	
	private String getChiWeek(String week){

		String chiWeek = week;

		if(StringUtils.equals("Monday", week)){
			chiWeek = "星期一";
			return chiWeek;
		}
		if(StringUtils.equals("Tuesday", week)){
			chiWeek = "星期二";
			return chiWeek;
		}
		if(StringUtils.equals("Wednesday", week)){
			chiWeek = "星期三";
			return chiWeek;
		}
		if(StringUtils.equals("Thursday", week)){
			chiWeek = "星期四";
			return chiWeek;
		}
		if(StringUtils.equals("Friday", week)){
			chiWeek = "星期五";
			return chiWeek;
		}
		if(StringUtils.equals("Saturday", week)){
			chiWeek = "星期六";
			return chiWeek;
		}
		if(StringUtils.equals("Sunday", week)){
			chiWeek = "星期日";
			return chiWeek;
		}

		return chiWeek;
	}
}
