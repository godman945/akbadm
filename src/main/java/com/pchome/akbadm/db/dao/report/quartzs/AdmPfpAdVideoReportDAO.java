package com.pchome.akbadm.db.dao.report.quartzs;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdVideoReport;
import com.pchome.akbadm.db.vo.report.PfpAdVideoReportVO;

public class AdmPfpAdVideoReportDAO extends BaseDAO<PfpAdVideoReport, Integer> implements IAdmPfpAdVideoReportDAO {

	public List<Object> findVideoInfoByDate(final String reportDate) throws Exception {

		@SuppressWarnings("unchecked")
		List<Object> result = (List<Object>) getHibernateTemplate().execute(
				new HibernateCallback<List<Object>>() {
					public List<Object> doInHibernate(Session session) throws HibernateException, SQLException {
						StringBuffer sql = new StringBuffer();
						sql.append(" SELECT  ");
						sql.append(" k.customer_info_id, ");
						sql.append(" k.pfd_customer_info_id, ");
						sql.append(" k.template_product_seq, ");
						sql.append(" k.ad_seq, ");
						sql.append(" k.ad_pvclk_date,  ");
						sql.append(" k.ad_pvclk_time, ");
						sql.append(" k.ad_pvclk_device, ");
						sql.append(" k.ad_price_type, ");
						sql.append(" Sum(k.ad_pv), ");
						sql.append(" Sum(k.ad_vpv), ");
						sql.append(" Sum(k.ad_clk), ");
						sql.append(" Sum(k.ad_view), ");
						sql.append(" Sum(k.ad_clk_price - k.ad_invalid_clk_price), ");
						sql.append(" IFNULL(a.sum_ad_video_play,0)sum_ad_video_play, ");
						sql.append(" IFNULL(a.sum_ad_video_music,0)sum_ad_video_music, ");
						sql.append(" IFNULL(a.sum_ad_video_replay,0)sum_ad_video_replay, ");
						sql.append(" IFNULL(a.sum_video_process_25,0)sum_video_process_25, ");
						sql.append(" IFNULL(a.sum_video_process_50,0)sum_video_process_50, ");
						sql.append(" IFNULL(a.sum_video_process_75,0)sum_video_process_75, ");
						sql.append(" IFNULL(a.sum_video_process_100,0)sum_video_process_100, ");
						sql.append(" IFNULL(a.sum_idc,0)sum_idc ");
						sql.append(" FROM   (SELECT 	v.ad_seq, "); 
						sql.append(" v.ad_price_type, "); 
						sql.append(" v.ad_video_date, "); 
						sql.append(" v.ad_video_time, "); 
						sql.append(" v.ad_pvclk_device, "); 
						sql.append(" Sum(v.ad_video_play)  sum_ad_video_play, "); 
						sql.append(" Sum(v.ad_video_music) sum_ad_video_music, "); 
						sql.append(" Sum(v.ad_video_replay)sum_ad_video_replay, "); 
						sql.append(" Sum(CASE "); 
						sql.append(" WHEN ad_video_process = 25 THEN ad_video_process_amount "); 
						sql.append(" ELSE 0 "); 
						sql.append(" END)              sum_video_process_25, ");
						sql.append(" Sum(CASE "); 
						sql.append(" WHEN ad_video_process = 50 THEN ad_video_process_amount "); 
						sql.append(" ELSE 0 "); 
						sql.append(" END)              sum_video_process_50, "); 
						sql.append(" Sum(CASE "); 
						sql.append(" WHEN ad_video_process = 75 THEN ad_video_process_amount "); 
						sql.append(" ELSE 0 "); 
						sql.append(" END)              sum_video_process_75, "); 
						sql.append(" Sum(CASE "); 
						sql.append(" WHEN ad_video_process = 100 THEN ad_video_process_amount "); 
						sql.append(" ELSE 0 "); 
						sql.append(" END)              sum_video_process_100, ");
						sql.append(" Sum(v.ad_video_idc)   sum_idc "); 
						sql.append(" FROM   pfp_ad_video v "); 
						sql.append(" WHERE  v.ad_video_date = :reportDate ");
						sql.append(" GROUP  BY v.ad_video_date, "); 
						sql.append(" v.ad_seq, "); 
						sql.append(" v.ad_video_time, "); 
						sql.append(" v.ad_pvclk_device, "); 
						sql.append(" v.ad_price_type) a "); 
						sql.append(" RIGHT JOIN pfp_ad_pvclk k "); 
						sql.append(" ON k.ad_seq = a.ad_seq "); 
						sql.append(" AND k.ad_pvclk_date = a.ad_video_date "); 
						sql.append(" AND a.ad_video_time = k.ad_pvclk_time "); 
						sql.append(" AND a.ad_price_type = k.ad_price_type "); 
						sql.append(" AND a.ad_pvclk_device = k.ad_pvclk_device ");
						sql.append(" WHERE  k.ad_pvclk_date = :reportDate and k.ad_price_type !='CPC'"); 
						sql.append(" GROUP  BY k.ad_pvclk_date, "); 
						sql.append(" k.ad_seq, "); 
						sql.append(" k.ad_pvclk_time, "); 
						sql.append(" k.ad_pvclk_device, "); 
						sql.append(" k.ad_price_type ");
						Query query = session.createSQLQuery(sql.toString());
						query.setString("reportDate", reportDate);
						query.setString("reportDate", reportDate);
						return query.list();
					}
				}
		);
		return result;
	}
 
	public int deleteVideoReportDataBytDate(String date) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("delete from PfpAdVideoReport where adVideoDate = :adVideoDate ");
		System.out.println(sql.toString());
		Session session = getSession();
		int stasus = session.createQuery(sql.toString()).setString("adVideoDate", date).executeUpdate();
		session.flush();
		return stasus;
	}

	public int addVideoReportDataBytDate(List<PfpAdVideoReportVO> list) throws Exception {
		Date date = new Date();
		for (PfpAdVideoReportVO pfpAdVideoReportVO : list) {
			PfpAdVideoReport pfpAdVideoReport = new PfpAdVideoReport();
			pfpAdVideoReport.setAdClk(pfpAdVideoReportVO.getAdClk());
			pfpAdVideoReport.setAdPrice(pfpAdVideoReportVO.getAdPrice());
			pfpAdVideoReport.setAdPriceType(pfpAdVideoReportVO.getAdPriceType());
			pfpAdVideoReport.setAdPv(pfpAdVideoReportVO.getAdPv());
			pfpAdVideoReport.setAdPvclkDevice(pfpAdVideoReportVO.getAdPvclkDevice());
			pfpAdVideoReport.setAdSeq(pfpAdVideoReportVO.getAdSeq());
			pfpAdVideoReport.setAdVideoDate(pfpAdVideoReportVO.getAdVideoDate());
			pfpAdVideoReport.setAdVideoMusic(pfpAdVideoReportVO.getAdVideoMusic());
			pfpAdVideoReport.setAdVideoPlay(pfpAdVideoReportVO.getAdVideoPlay());
			pfpAdVideoReport.setAdVideoProcess100(pfpAdVideoReportVO.getAdVideoProcess100());
			pfpAdVideoReport.setAdVideoProcess75(pfpAdVideoReportVO.getAdVideoProcess75());
			pfpAdVideoReport.setAdVideoProcess50(pfpAdVideoReportVO.getAdVideoProcess50());
			pfpAdVideoReport.setAdVideoProcess25(pfpAdVideoReportVO.getAdVideoProcess25());
			pfpAdVideoReport.setAdVideoReplay(pfpAdVideoReportVO.getAdVideoReplay());
			pfpAdVideoReport.setAdVideoTime(pfpAdVideoReportVO.getAdVideoTime());
			pfpAdVideoReport.setAdVideoUniq(pfpAdVideoReport.getAdVideoIdc());
			pfpAdVideoReport.setAdView(pfpAdVideoReportVO.getAdView());
			pfpAdVideoReport.setAdVpv(pfpAdVideoReportVO.getAdVpv());
			pfpAdVideoReport.setCreateDate(date);
			pfpAdVideoReport.setUpdateDate(date);
			pfpAdVideoReport.setCustomerInfoId(pfpAdVideoReportVO.getCustomerInfoId());
			pfpAdVideoReport.setPfdCustomerInfoId(pfpAdVideoReportVO.getPfdCustomerInfoId());
			pfpAdVideoReport.setTemplateProductSeq(pfpAdVideoReportVO.getTemplateProductSeq());
			save(pfpAdVideoReport);
			if(list.indexOf(pfpAdVideoReportVO) % 2 == 0 ){
				getSession().flush();
				getSession().clear();
			}
			
		}
		return 0;
	}
}
