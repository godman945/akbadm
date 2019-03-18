package com.pchome.akbadm.db.dao.pfbx.report;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.vo.pfbx.report.PfbPositionPriceReportVO;

public class PfbPositionPriceReportDAO extends BaseDAO<PfbPositionPriceReportVO, String> implements IPfbPositionPriceReportDAO {

	/**
	 * 取得版位定價查詢資料
	 * @param customerInfoId PFB帳號
	 * @param pid            PFB 版位編號
	 * @param pprice         版位價格
	 * @return 
	 * @throws Exception
	 */
	@Override
	public List<PfbPositionPriceReportVO> getPositionPriceDataList(final String customerInfoId, final String pid, final int pprice)
			throws Exception {
		
		List<PfbPositionPriceReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<PfbPositionPriceReportVO>>() {
					@Override
                    public List<PfbPositionPriceReportVO> doInHibernate(Session session) throws HibernateException {

						StringBuffer sb = new StringBuffer();
						sb.append(" select");
						sb.append("  pfbxCustomerInfo.customerInfoId,"); //廣告帳號
						sb.append("  PId,");                             //版位id
						sb.append("  PName,");                           //版位名稱
						sb.append("  PType,");                           //版位類型 N:正常 S:出價 P:私人
						sb.append("  PPrice,");                          //版位出價
						sb.append("  updateDate");                       //更新時間
						sb.append(" from PfbxPosition");
						sb.append(" where deleteFlag = 0");
						if (StringUtils.isNotBlank(customerInfoId)) {
							sb.append(" and pfbxCustomerInfo.customerInfoId = :customerInfoId");
						}
						if (StringUtils.isNotBlank(pid)) {
							sb.append(" and PId = :pid");
						}
						if (pprice > 0) {
							sb.append(" and PPrice > :pprice");
						}
						
 						String hql = sb.toString();
//						log.info(">>> hql = " + hql);

						Query query = session.createQuery(hql);
						if (StringUtils.isNotBlank(customerInfoId)) {
							query.setString("customerInfoId", customerInfoId);
						}
						if (StringUtils.isNotBlank(pid)) {
							query.setString("pid", pid);
						}
						if (pprice > 0) {
							query.setInteger("pprice", pprice);
						}
						
						List<Object> dataList = query.list();
//						log.info(">>> dataList.size() = " + dataList.size());

						List<PfbPositionPriceReportVO> resultData = new ArrayList<PfbPositionPriceReportVO>();

						for (int i=0; i<dataList.size(); i++) {
							Object[] objArray = (Object[]) dataList.get(i);
							String customerInfoId = (String) objArray[0];
							String pId = (String) objArray[1];
							String pName = (String) objArray[2];
							String pType = (String) objArray[3];
							int pPrice = (int) objArray[4];
							Date updateDate = (Date) objArray[5];

							PfbPositionPriceReportVO pfbPositionPriceReportVO = new PfbPositionPriceReportVO();
							pfbPositionPriceReportVO.setCustomerInfoId(customerInfoId);
							pfbPositionPriceReportVO.setpId(pId);
							pfbPositionPriceReportVO.setpName(pName);
							if("N".equals(pType)){
								pfbPositionPriceReportVO.setpType("正常");
							}else if("S".equals(pType)){
								pfbPositionPriceReportVO.setpType("出價");
							}else if("P".equals(pType)){
								pfbPositionPriceReportVO.setpType("私人");
							}else{
								pfbPositionPriceReportVO.setpType("");
							}
							pfbPositionPriceReportVO.setpPrice(pPrice);
							pfbPositionPriceReportVO.setUpdateDate(updateDate);

							resultData.add(pfbPositionPriceReportVO);
						}

						return resultData;
					}
				}
		);
		return result;
	}

	/**
	 * 更新版位定價資料
	 * @param pid        版位編號
	 * @param pprice     出價
	 * @throws Exception
	 */
	@Override
	public void updatePositionPrice(String pid, int pprice) throws Exception{
		String hql = "update PfbxPosition ";
		       hql +="set PPrice =:pprice, updateDate = CURRENT_TIMESTAMP() ";
		       hql +="where PId = :pid ";
		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("pid", pid);
        query.setInteger("pprice", pprice);
        query.executeUpdate();
        session.flush();
	}
}