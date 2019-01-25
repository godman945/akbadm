package com.pchome.akbadm.db.dao.report.quartzs;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfdAdVideoReport;

public class PfdAdVideoReportDAO extends BaseDAO<PfdAdVideoReport, Integer> implements IPfdAdVideoReportDAO {

	public List<Object> findVideoInfoByDate(final String reportDate) throws Exception {

		@SuppressWarnings("unchecked")
		List<Object> result = (List<Object>) getHibernateTemplate().execute(
				new HibernateCallback<List<Object>>() {
					public List<Object> doInHibernate(Session session) throws HibernateException, SQLException {
						StringBuffer sql = new StringBuffer();
						sql.append(" SELECT                                                                       ");
						sql.append(" 	ap.ad_pvclk_date,                                                         ");
						sql.append(" 	ap.ad_pvclk_time,                                                         ");
						sql.append(" 	ap.pfd_customer_info_id,                                                  ");
						sql.append(" 	ap.pfd_user_id,                                                           ");
						sql.append(" 	ap.customer_info_id,                                                      ");
						sql.append(" 	ap.ad_seq,                                                                ");
						sql.append(" 	ap.template_product_seq,                                                  ");
						sql.append(" 	ap.ad_price_type,                                                         ");
						sql.append(" 	ap.ad_pvclk_device,                                                       ");
						sql.append(" 	Sum(ap.ad_vpv),                                                           ");
						sql.append(" 	Sum(ap.ad_pv),                                                            ");
						sql.append(" 	Sum(ap.ad_clk),                                                           ");
						sql.append(" 	Sum(ap.ad_view),                                                          ");
						sql.append(" 	Sum(ap.ad_clk_price - ap.ad_invalid_clk_price),                           ");
						sql.append(" 	IFNULL(a.sum_ad_video_play,0) sum_ad_video_play,                          ");
						sql.append(" 	IFNULL(a.sum_ad_video_music,0) sum_ad_video_music,                        ");
						sql.append(" 	IFNULL(a.sum_ad_video_replay,0) sum_ad_video_replay,                      ");
						sql.append(" 	IFNULL(a.sum_video_process_25,0) sum_video_process_25,                    ");
						sql.append(" 	IFNULL(a.sum_video_process_50,0) sum_video_process_50,                    ");
						sql.append(" 	IFNULL(a.sum_video_process_75,0) sum_video_process_75,                    ");
						sql.append(" 	IFNULL(a.sum_video_process_100,0) sum_video_process_100,                  ");
						sql.append(" 	IFNULL(a.sum_idc,0) sum_idc                                               ");
						sql.append(" FROM (SELECT 	                                                              ");
						sql.append(" 			av.ad_seq,                                                        ");
						sql.append(" 			av.ad_price_type,                                                 ");
						sql.append(" 			av.ad_video_date,                                                 ");
						sql.append(" 			av.ad_video_time,                                                 ");
						sql.append(" 			av.ad_pvclk_device,                                               ");
						sql.append(" 			Sum(av.ad_video_play) sum_ad_video_play,                          ");
						sql.append(" 			Sum(av.ad_video_music) sum_ad_video_music,                        ");
						sql.append(" 			Sum(av.ad_video_replay) sum_ad_video_replay,                      ");
						sql.append(" 			Sum(CASE WHEN ad_video_process = 25 THEN ad_video_process_amount  ");
						sql.append(" 			ELSE 0 END) sum_video_process_25,                                 ");
						sql.append(" 			Sum(CASE WHEN ad_video_process = 50 THEN ad_video_process_amount  ");
						sql.append(" 			ELSE 0 END) sum_video_process_50,                                 ");
						sql.append(" 			Sum(CASE WHEN ad_video_process = 75 THEN ad_video_process_amount  ");
						sql.append(" 			ELSE 0 END) sum_video_process_75,                                 ");
						sql.append(" 			Sum(CASE WHEN ad_video_process = 100 THEN ad_video_process_amount "); 
						sql.append(" 			ELSE 0 END) sum_video_process_100,                                ");
						sql.append(" 			Sum(av.ad_video_idc) sum_idc                                      ");
						sql.append(" 		FROM pfp_ad_video av                                                  ");
						sql.append(" 		WHERE av.ad_video_date = :reportDate                                  ");
						sql.append(" 		GROUP BY av.ad_video_date,                                            ");
						sql.append(" 		av.ad_seq,                                                            ");
						sql.append(" 		av.ad_video_time,                                                     ");
						sql.append(" 		av.ad_pvclk_device,                                                   ");
						sql.append(" 		av.ad_price_type) a                                                   ");
						sql.append(" RIGHT JOIN pfp_ad_pvclk ap                                                   ");
						sql.append(" 	ON ap.ad_seq = a.ad_seq                                                   ");
						sql.append(" 	AND ap.ad_pvclk_date = a.ad_video_date                                    ");
						sql.append(" 	AND a.ad_video_time = ap.ad_pvclk_time                                    ");
						sql.append(" 	AND a.ad_price_type = ap.ad_price_type                                    ");
						sql.append(" 	AND a.ad_pvclk_device = ap.ad_pvclk_device                                ");
						sql.append(" WHERE                                                                        ");
						sql.append(" 	ap.ad_pvclk_date = :reportDate                                            ");
						sql.append(" 	AND ap.ad_price_type != 'CPC'                                             ");
						sql.append(" GROUP BY                                                                     ");
						sql.append(" 	ap.ad_pvclk_date,                                                         ");
						sql.append(" 	ap.ad_seq,                                                                ");
						sql.append(" 	ap.ad_pvclk_time,                                                         ");
						sql.append(" 	ap.ad_pvclk_device,                                                       ");
						sql.append(" 	ap.ad_price_type,                                                         ");
						sql.append(" 	ap.pfd_user_id                                                            ");
						Query query = session.createSQLQuery(sql.toString());
						query.setString("reportDate", reportDate);
						return query.list();
					}
				}
		);
		return result;
	}

	public int deleteVideoReportDataBytDate(String date) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("delete from PfdAdVideoReport where adVideoDate = :adVideoDate ");
		Session session = getSession();
		int stasus = session.createQuery(sql.toString()).setString("adVideoDate", date).executeUpdate();
		session.flush();
		return stasus;
	}

	public int addVideoReportDataBytDate(List<PfdAdVideoReport> list) throws Exception {
		Date date = new Date();
		for (PfdAdVideoReport pfdAdVideoReportVO : list) {
			PfdAdVideoReport pfdAdVideoReport = new PfdAdVideoReport();
			pfdAdVideoReport.setAdClk(pfdAdVideoReportVO.getAdClk());
			pfdAdVideoReport.setAdPrice(pfdAdVideoReportVO.getAdPrice());
			pfdAdVideoReport.setAdPriceType(pfdAdVideoReportVO.getAdPriceType());
			pfdAdVideoReport.setAdPv(pfdAdVideoReportVO.getAdPv());
			pfdAdVideoReport.setAdPvclkDevice(pfdAdVideoReportVO.getAdPvclkDevice());
			pfdAdVideoReport.setAdSeq(pfdAdVideoReportVO.getAdSeq());
			pfdAdVideoReport.setAdVideoDate(pfdAdVideoReportVO.getAdVideoDate());
			pfdAdVideoReport.setAdVideoMusic(pfdAdVideoReportVO.getAdVideoMusic());
			pfdAdVideoReport.setAdVideoPlay(pfdAdVideoReportVO.getAdVideoPlay());
			pfdAdVideoReport.setAdVideoProcess100(pfdAdVideoReportVO.getAdVideoProcess100());
			pfdAdVideoReport.setAdVideoProcess75(pfdAdVideoReportVO.getAdVideoProcess75());
			pfdAdVideoReport.setAdVideoProcess50(pfdAdVideoReportVO.getAdVideoProcess50());
			pfdAdVideoReport.setAdVideoProcess25(pfdAdVideoReportVO.getAdVideoProcess25());
			pfdAdVideoReport.setAdVideoReplay(pfdAdVideoReportVO.getAdVideoReplay());
			pfdAdVideoReport.setAdVideoTime(pfdAdVideoReportVO.getAdVideoTime());
			pfdAdVideoReport.setAdVideoUniq(0);
			pfdAdVideoReport.setAdView(pfdAdVideoReportVO.getAdView());
			pfdAdVideoReport.setAdVpv(pfdAdVideoReportVO.getAdVpv());
			pfdAdVideoReport.setCreateDate(date);
			pfdAdVideoReport.setUpdateDate(date);
			pfdAdVideoReport.setPfpCustomerInfoId(pfdAdVideoReportVO.getPfpCustomerInfoId());
			pfdAdVideoReport.setPfdCustomerInfoId(pfdAdVideoReportVO.getPfdCustomerInfoId());
			pfdAdVideoReport.setPfdUserId(pfdAdVideoReportVO.getPfdUserId());
			pfdAdVideoReport.setTemplateProductSeq(pfdAdVideoReportVO.getTemplateProductSeq());
			super.save(pfdAdVideoReport);
		}
		return 0;
	}
}
