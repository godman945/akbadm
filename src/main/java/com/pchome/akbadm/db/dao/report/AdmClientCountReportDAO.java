package com.pchome.akbadm.db.dao.report;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmClientCountReport;
import com.pchome.akbadm.db.vo.report.AdmClientCountForNext30DayReportVO;
import com.pchome.akbadm.db.vo.report.AdmClientCountReportVO;
import com.pchome.akbadm.db.vo.report.AdmCountReportVO;

public class AdmClientCountReportDAO extends BaseDAO<AdmClientCountReport, Integer> implements IAdmClientCountReportDAO {

	@Override
	public List<Object> prepareReportData(final String reportDate) throws Exception {
		List<Object> result = getHibernateTemplate().execute(
				new HibernateCallback<List<Object>>() {
					@Override
                    public List<Object> doInHibernate(Session session) throws HibernateException {

						StringBuffer sql = new StringBuffer();

						sql.append(" select ");
						sql.append(" ad_pvclk_date, ");
						sql.append(" count(distinct case when pfd_customer_info_id in ('PFDC20140520001','PFDC20141024003','PFDC20161012001') then customer_info_id else null end), ");		//直客客戶數
						sql.append(" count(distinct case when pfd_customer_info_id not in ('PFDC20140520001','PFDC20141024003','PFDC20150422001','PFDC20161012001') then customer_info_id else null end), ");	//經銷商客戶數
						sql.append(" count(distinct case when pfd_customer_info_id in ('PFDC20150422001') then customer_info_id else null end), ");											//業務客戶數
						
						
						sql.append(" ifnull(sum(case when pfd_customer_info_id in ('PFDC20140520001','PFDC20141024003','PFDC20161012001') then (ad_clk_price - ad_invalid_clk_price) else 0 end),0), ");	//直客廣告點擊數費用
						sql.append(" ifnull(sum(case when pfd_customer_info_id not in ('PFDC20140520001','PFDC20141024003','PFDC20150422001','PFDC20161012001') then (ad_clk_price - ad_invalid_clk_price) else 0 end),0), ");//經銷商廣告點擊數費用
						sql.append(" ifnull(sum(case when pfd_customer_info_id in ('PFDC20150422001') then (ad_clk_price - ad_invalid_clk_price) else 0 end),0), ");										//業務廣告點擊數費用

						//直客廣告每日預算
						sql.append(" (select ifnull(sum(a.max_price),0) from ( ");
						sql.append(" select distinct ad_action_seq, round(sum(ad_action_max_price)/count(ad_action_seq),0) as max_price from pfp_ad_pvclk ");
						sql.append(" where pfd_customer_info_id in ('PFDC20140520001','PFDC20141024003','PFDC20161012001') ");
						sql.append(" and ad_pvclk_date = '" + reportDate + "' ");
						sql.append(" group by ad_action_seq) a), ");

						//經銷商廣告每日預算
						sql.append(" (select ifnull(sum(b.max_price),0) from ( ");
						sql.append(" select distinct ad_action_seq, round(sum(ad_action_max_price)/count(ad_action_seq),0) as max_price from pfp_ad_pvclk ");
						sql.append(" where pfd_customer_info_id not in ('PFDC20140520001','PFDC20141024003','PFDC20150422001','PFDC20161012001') ");
						sql.append(" and ad_pvclk_date = '" + reportDate + "' ");
						sql.append(" group by ad_action_seq) b), ");
						
						//業務廣告每日預算
						sql.append(" (select ifnull(sum(c.max_price),0) from ( ");
						sql.append(" select distinct ad_action_seq, round(sum(ad_action_max_price)/count(ad_action_seq),0) as max_price from pfp_ad_pvclk ");
						sql.append(" where pfd_customer_info_id in ('PFDC20150422001') ");
						sql.append(" and ad_pvclk_date = '" + reportDate + "' ");
						sql.append(" group by ad_action_seq) c), ");
						
						sql.append(" count(distinct case when pfd_customer_info_id in ('PFDC20140520001','PFDC20141024003') then ad_seq else null end), ");		//直客廣告數
						sql.append(" count(distinct case when pfd_customer_info_id not in ('PFDC20140520001','PFDC20141024003') then ad_seq else null end), ");	//經銷商廣告數
						sql.append(" count(distinct case when pfd_customer_info_id in ('PFDC20150422001') then ad_seq else null end), ");						//業務廣告數
						
						sql.append(" ifnull(sum(ad_pv),0), ");									//曝光數
						sql.append(" ifnull(sum((case when ad_price_type = 'CPC' then ad_clk else ad_view end)),0) - ifnull(sum(ad_invalid_clk),0), ");	//互動數
						sql.append(" ifnull(sum(ad_invalid_clk),0), ");							//無效點擊數
						sql.append(" ifnull(sum(ad_clk_price - ad_invalid_clk_price),0), ");	//廣告點擊數總費用
						sql.append(" ifnull(sum(ad_invalid_clk_price),0), ");					//無效點擊數總費用
						
						sql.append(" ifnull((select sum(order_price) from pfp_order where DATE_FORMAT(update_date , '%Y-%m-%d') = ad_pvclk_date and status in ('B301','B302')),0), "); //儲值金額
						sql.append(" ifnull((select count(DATE_FORMAT(update_date , '%Y-%m-%d')) from pfp_order where DATE_FORMAT(update_date , '%Y-%m-%d') = ad_pvclk_date and status in ('B301','B302') group by DATE_FORMAT(update_date , '%Y-%m-%d')),0) "); //儲值總筆數 
						
						sql.append(" from pfp_ad_pvclk ");
						sql.append(" where ad_pvclk_date = '" + reportDate + "' ");
						sql.append(" group by ad_pvclk_date ");

						String sqlString = sql.toString();
//						log.info(">>> sql = " + sqlString);

						Query q = session.createSQLQuery(sqlString);

						List<Object> resultData = q.list();
//						log.info(">>> resultData.size() = " + resultData.size());

						return resultData;
					}
				});
		return result;
	}

	@Override
	public float findLossCost(final String reportDate) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select sum(lossCost) ");
		hql.append(" from AdmTransLoss ");
    	hql.append(" where transDate = :transDate ");


    	Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
    	query.setString("transDate", reportDate);

    	 Double result = (Double) query.uniqueResult();

         return result != null ? result.floatValue() : 0f;
	}

	@Override
	public List<Object> findAdmBonusDetailReport(final String reportDate) throws Exception {
		List<Object> result = getHibernateTemplate().execute(
				new HibernateCallback<List<Object>>() {
					@Override
                    public List<Object> doInHibernate(Session session) throws HibernateException {

						StringBuffer sql = new StringBuffer();

						sql.append(" select ");

						//PChome 營運費用
						sql.append(" ifnull(sum(case when detail_item = '1' then save else 0 end),0), ");
						sql.append(" ifnull(sum(case when detail_item = '1' then free else 0 end),0), ");
						sql.append(" ifnull(sum(case when detail_item = '1' then postpaid else 0 end),0), ");
						//PFB 分潤
						sql.append(" ifnull(sum(case when detail_item = '4' then save else 0 end),0), ");
						sql.append(" ifnull(sum(case when detail_item = '4' then free else 0 end),0), ");
						sql.append(" ifnull(sum(case when detail_item = '4' then postpaid else 0 end),0), ");
						//PFD 佣金
						sql.append(" ifnull(sum(case when detail_item = '3' then save else 0 end),0), ");
						sql.append(" ifnull(sum(case when detail_item = '3' then free else 0 end),0), ");
						sql.append(" ifnull(sum(case when detail_item = '3' then postpaid else 0 end),0) ");

						sql.append(" from adm_bonus_detail_report ");
						sql.append(" where report_date = '" + reportDate + "' ");

						String sqlString = sql.toString();
//						log.info(">>> sql = " + sqlString);

						Query q = session.createSQLQuery(sqlString);

						List<Object> resultData = q.list();
//						log.info(">>> resultData.size() = " + resultData.size());

						return resultData;
					}
				});
		return result;
	}

	@Override
	public int deleteReportDataByReportDate(String reportDate){
		 String hql = "delete from AdmClientCountReport where countDate = '" + reportDate + "' ";
		 return this.getHibernateTemplate().bulkUpdate(hql);
	}

	@Override
	public List<AdmClientCountReportVO> findReportData(final String firstDay, final String lastDay) {
		List<AdmClientCountReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<AdmClientCountReportVO>>() {
				@Override
                public List<AdmClientCountReportVO> doInHibernate(Session session) throws HibernateException {

					List<AdmClientCountReportVO> resultData = new ArrayList<AdmClientCountReportVO>();
					DecimalFormat df1 = new DecimalFormat("###,###,###,###");
					DecimalFormat df2 = new DecimalFormat("###,###,###,##0.00");

					StringBuffer hql = new StringBuffer();
					hql.append("select ");
					hql.append("count_date, ");
					hql.append("pfp_client_count, ");
					hql.append("pfd_client_count, ");
					hql.append("pfp_ad_clk_price, ");
					hql.append("pfd_ad_clk_price, ");
					hql.append("pfp_ad_action_max_price, ");
					hql.append("pfd_ad_action_max_price, ");
					hql.append("pfp_ad_count, ");
					hql.append("pfd_ad_count, ");
					hql.append("ad_pv, ");
					hql.append("ad_clk, ");
					hql.append("ad_invalid_clk, ");
					hql.append("ad_clk_price, ");
					hql.append("ad_invalid_clk_price, ");
					hql.append("loss_cost, ");
					hql.append("pfp_save, ");
					hql.append("pfp_free, ");
					hql.append("pfp_postpaid, ");
					hql.append("pfb_save, ");
					hql.append("pfb_free, ");
					hql.append("pfb_postpaid, ");
					hql.append("pfd_save, ");
					hql.append("pfd_free, ");
					hql.append("pfd_postpaid, ");
					
					hql.append("sales_client_count, ");
					hql.append("sales_ad_clk_price, ");
					hql.append("sales_ad_action_max_price, ");
					hql.append("sales_ad_count, ");
					hql.append("total_save_price, ");
					hql.append("total_save_count ");
					
					
					
					hql.append(" from adm_client_count_report ");
					hql.append(" where 1=1 ");

					if(StringUtils.isNotEmpty(firstDay)){
						hql.append(" and count_date >= '" + firstDay + "' ");
					}

					if(StringUtils.isNotEmpty(lastDay)){
						hql.append(" and count_date <= '" + lastDay + "' ");
					}

					hql.append(" order by count_date desc ");

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
						AdmClientCountReportVO vo = new AdmClientCountReportVO();

						Object[] objArray = (Object[]) dataList.get(i);

						Long pfpAdClkPrice = (new Double((objArray[3]).toString())).longValue();
						Long pfdAdClkPrice = (new Double((objArray[4]).toString())).longValue();
						Long salesAdClkPrice = (new Double((objArray[25]).toString())).longValue();
						Long pfpAdActionMaxPrice = (new Double((objArray[5]).toString())).longValue();
						Long pfdAdActionMaxPrice = (new Double((objArray[6]).toString())).longValue();
						Long salesAdActionMaxPrice = (new Double((objArray[26]).toString())).longValue();
						
						double pfpSpendRate = 0;
						double pfdSpendRate = 0;
						double salesSpendRate = 0;
						
						//直客預算達成率
						if(pfpAdClkPrice != 0 && pfpAdActionMaxPrice != 0){
							pfpSpendRate = (pfpAdClkPrice.doubleValue() / pfpAdActionMaxPrice.doubleValue())*100;
						}

						//經銷商預算達成率
						if(pfdAdClkPrice != 0 && pfdAdActionMaxPrice != 0){
							pfdSpendRate = (pfdAdClkPrice.doubleValue() / pfdAdActionMaxPrice.doubleValue())*100;
						}
						
						//業務預算達成率
						if(salesAdClkPrice != 0 && salesAdActionMaxPrice != 0){
							salesSpendRate = (salesAdClkPrice.doubleValue() / salesAdActionMaxPrice.doubleValue())*100;
						}
						

						Long adPv = new Long((objArray[9]).toString());
						Long adClk = new Long((objArray[10]).toString());
						Long adClkPrice = (new Double((objArray[12]).toString())).longValue();
						
						Double totalClientCount = new Double(0);
						Double totalAdClkPriceCount = new Double(0);
						Double totalAdActionMaxPrice = new Double(0);
						Double totalSpendRate = new Double(0);
						
						double clkRate = 0;
						double adClkPriceAvg = 0;
						double adPvPrice = 0;

						//點擊率
						if(adPv != 0 && adClk != 0){
							clkRate = (adClk.doubleValue() / adPv.doubleValue())*100;
						}

						//單次點擊費用
						if(adClk != 0 && adClkPrice != 0){
							adClkPriceAvg = adClkPrice.doubleValue() / adClk.doubleValue();
						}

						//千次曝光費用
						if(adPv != 0 && adClkPrice != 0){
							adPvPrice = (adClkPrice.doubleValue() / adPv.doubleValue())*1000;
						}

						//廣告客戶數總計
						totalClientCount = new Double((objArray[1]).toString()) + new Double((objArray[2]).toString()) + new Double((objArray[24]).toString()); 
						//廣告互動費用總計
						totalAdClkPriceCount = new Double((objArray[3]).toString()) + new Double((objArray[4]).toString()) + new Double((objArray[25]).toString());
						//廣告每日預算總計
						totalAdActionMaxPrice = new Double((objArray[5]).toString()) + new Double((objArray[6]).toString()) + new Double((objArray[26]).toString());
						
						//總預算達成率
						if(totalAdClkPriceCount != 0 && totalAdActionMaxPrice !=0){
							totalSpendRate = (totalAdClkPriceCount.doubleValue() / totalAdActionMaxPrice.doubleValue())*100;
						}
						
						
						
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

						vo.setCountDate(objArray[0].toString());
						vo.setWeek(week);
						vo.setPfpClientCount(df1.format(new Double((objArray[1]).toString())));
						vo.setPfdClientCount(df1.format(new Double((objArray[2]).toString())));
						vo.setPfpAdClkPrice(df1.format(new Double((objArray[3]).toString())));
						vo.setPfdAdClkPrice(df1.format(new Double((objArray[4]).toString())));
						vo.setPfpAdActionMaxPrice(df1.format(new Double((objArray[5]).toString())));
						vo.setPfdAdActionMaxPrice(df1.format(new Double((objArray[6]).toString())));
						vo.setPfpSpendRate(df2.format(pfpSpendRate));
						vo.setPfdSpendRate(df2.format(pfdSpendRate));
						vo.setSalesSpendRate(df2.format(salesSpendRate));
						vo.setPfpAdCount(df1.format(new Double((objArray[7]).toString())));
						vo.setPfdAdCount(df1.format(new Double((objArray[8]).toString())));
						vo.setAdPv(df1.format(new Double((objArray[9]).toString())));
						vo.setAdClk(df1.format(new Double((objArray[10]).toString())));
						vo.setAdInvalidClk(df1.format(new Double((objArray[11]).toString())));
						vo.setAdClkPrice(df1.format(new Double((objArray[12]).toString())));
						vo.setAdInvalidClkPrice(df1.format(new Double((objArray[13]).toString())));
						vo.setClkRate(df2.format(clkRate));
						vo.setAdClkPriceAvg(df2.format(adClkPriceAvg));
						vo.setAdPvPrice(df2.format(adPvPrice));
						vo.setLossCost(df1.format(new Double((objArray[14]).toString())));
						vo.setPfpSave(df2.format(new Double((objArray[15]).toString())));
						vo.setPfpFree(df2.format(new Double((objArray[16]).toString())));
						vo.setPfpPostpaid(df2.format(new Double((objArray[17]).toString())));
						vo.setPfbSave(df2.format(new Double((objArray[18]).toString())));
						vo.setPfbFree(df2.format(new Double((objArray[19]).toString())));
						vo.setPfbPostpaid(df2.format(new Double((objArray[20]).toString())));
						vo.setPfdSave(df2.format(new Double((objArray[21]).toString())));
						vo.setPfdFree(df2.format(new Double((objArray[22]).toString())));
						vo.setPfdPostpaid(df2.format(new Double((objArray[23]).toString())));
						vo.setSalesClientCount(df1.format(new Double((objArray[24]).toString())));
						vo.setSalesAdClkPrice(df2.format(new Double((objArray[25]).toString())));
						vo.setSalesAdActionMaxPrice(df1.format(new Double((objArray[26]).toString())));
						vo.setSalesAdCount(df2.format(new Double((objArray[24]).toString())));
						vo.setTotalSavePrice(df1.format(new Double((objArray[28]).toString())));
						vo.setTotalSaveCount(df1.format(new Double((objArray[29]).toString())));
						vo.setTotalClientCount(df1.format(totalClientCount));
						vo.setTotalAdActionMaxPrice(df1.format(totalAdActionMaxPrice));
						vo.setTotalAdClkPriceCount(df1.format(totalAdClkPriceCount));
						vo.setTotalSpendRate(df2.format(totalSpendRate));
						resultData.add(vo);
					}

//					log.info("resultData: "+resultData.size());

					return resultData;
				}
		});
		return result;
	}

	@Override
	public List<AdmClientCountForNext30DayReportVO> findReportDataFpr30Day(final String searchDay) {
		List<AdmClientCountForNext30DayReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<AdmClientCountForNext30DayReportVO>>() {
				@Override
                public List<AdmClientCountForNext30DayReportVO> doInHibernate(Session session) throws HibernateException {

					List<AdmClientCountForNext30DayReportVO> resultData = new ArrayList<AdmClientCountForNext30DayReportVO>();
					DecimalFormat df1 = new DecimalFormat("###,###,###,###");

					StringBuffer hql = new StringBuffer();
					hql.append(" select ");
					hql.append(" ifnull(sum(ad_count),0), ");
					hql.append(" ifnull(sum(group_count),0), ");
					hql.append(" count(ad_action_seq), ");
					hql.append(" count(distinct customer_info_id), ");
					hql.append(" ifnull(sum(ad_action_max),0) ");
					hql.append(" from ( ");
					hql.append(" select ");
					hql.append(" count(ad.ad_seq) ad_count, ");
					hql.append(" count(distinct g.ad_group_seq) group_count, ");
					hql.append(" a.ad_action_seq, ");
					hql.append(" a.customer_info_id, ");
					hql.append(" ad_action_max ");
					hql.append(" from pfp_ad ad ");
					hql.append(" join pfp_ad_group g ");
					hql.append(" on ad.ad_group_seq = g.ad_group_seq ");
					hql.append(" join pfp_ad_action a ");
					hql.append(" on g.ad_action_seq = a.ad_action_seq ");
					hql.append(" join pfp_customer_info c ");
					hql.append(" on c.customer_info_id = a.customer_info_id ");
					hql.append(" where ad.ad_status = 4 ");
					hql.append(" and g.ad_group_status = 4 ");
					hql.append(" and a.ad_action_status = 4 ");
					hql.append(" and c.remain > 0 ");

					if(StringUtils.isNotEmpty(searchDay)){
						hql.append(" and '" + searchDay + "' between a.ad_action_start_date and a.ad_action_end_date ");
					}

					hql.append(" group by a.ad_action_seq, a.customer_info_id, ad_action_max ");
					hql.append(" ) as count_table ");

					String sql = hql.toString();
//					log.info(">>> sql = " + sql);
					List<Object> dataList = new ArrayList<Object>();
					try {
					Query query = session.createSQLQuery(sql);
					dataList = query.list();
					} catch (Exception e) {
						e.printStackTrace();
					}
//					log.info(">>> dataList.size() = " + dataList.size());

					for (int i=0; i<dataList.size(); i++) {
						AdmClientCountForNext30DayReportVO vo = new AdmClientCountForNext30DayReportVO();

						Object[] objArray = (Object[]) dataList.get(i);

						SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
						String week = "";
						try {
							 week = sdf.format(dateFormate.parse(searchDay));
							 week = getChiWeek(week);
							 week = week.substring(2);
						} catch (ParseException e) {
							log.error(">>>>>>>>>>>>>>>>>>>>>>>>日期換算星期錯誤!");
							e.printStackTrace();
						}

						vo.setCountDate(searchDay);
						vo.setWeek(week);
						vo.setPfpAdCount(df1.format(new Double((objArray[0]).toString())));
						vo.setPfpAdGroupCount(df1.format(new Double((objArray[1]).toString())));
						vo.setPfpAdActionCount(df1.format(new Double((objArray[2]).toString())));
						vo.setPfpCount(df1.format(new Double((objArray[3]).toString())));
						vo.setPfpAdActionMaxPrice(df1.format(new Double((objArray[4]).toString())));
						resultData.add(vo);
					}

//					log.info("resultData: "+resultData.size());

					return resultData;
				}
		});
		return result;
	}

	@Override
	public List<AdmCountReportVO> findCountReportData(final String firstDay, final String lastDay) {
		List<AdmCountReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<AdmCountReportVO>>() {
				@Override
                public List<AdmCountReportVO> doInHibernate(Session session) throws HibernateException {

					List<AdmCountReportVO> resultData = new ArrayList<AdmCountReportVO>();
					DecimalFormat df1 = new DecimalFormat("###,###,###,###");
					DecimalFormat df2 = new DecimalFormat("###,###,###,##0.00");

					StringBuffer hql = new StringBuffer();
					hql.append("select ");
					hql.append("a.count_date, ");
					hql.append("(a.pfp_ad_clk_price + a.pfd_ad_clk_price), ");
					hql.append("(a.pfp_client_count + a.pfd_client_count), ");
					hql.append("(select count(distinct pfd_customer_info_id) from pfp_ad_pvclk where ad_pvclk_date = a.count_date ), ");
					hql.append("(a.pfd_save + a.pfd_free + a.pfd_postpaid), ");
					hql.append("(a.pfb_save + a.pfb_free + a.pfb_postpaid), ");
					hql.append("(a.pfp_save + a.pfp_postpaid) ");
					hql.append(" from adm_client_count_report a ");
					hql.append(" where 1=1 ");

					if(StringUtils.isNotEmpty(firstDay)){
						hql.append(" and a.count_date >= '" + firstDay + "' ");
					}

					if(StringUtils.isNotEmpty(lastDay)){
						hql.append(" and a.count_date <= '" + lastDay + "' ");
					}

					hql.append(" order by a.count_date desc ");

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
						AdmCountReportVO vo = new AdmCountReportVO();

						Object[] objArray = (Object[]) dataList.get(i);				

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

						vo.setCountDate(objArray[0].toString());
						vo.setWeek(week);
						vo.setAdClkPrice(df1.format(new Double((objArray[1]).toString())));
						vo.setClientCount(df1.format(new Double((objArray[2]).toString())));
						vo.setPfdCount(df1.format(new Double((objArray[3]).toString())));
						vo.setPfdBonus(df2.format(new Double((objArray[4]).toString())));
						vo.setPfbBonus(df2.format(new Double((objArray[5]).toString())));
						vo.setPfpBonus(df2.format(new Double((objArray[6]).toString())));
						

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
