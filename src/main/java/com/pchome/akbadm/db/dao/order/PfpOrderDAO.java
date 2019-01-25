package com.pchome.akbadm.db.dao.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpOrder;
import com.pchome.enumerate.billing.EnumBillingStatus;
import com.pchome.soft.util.DateValueUtil;

public class PfpOrderDAO extends BaseDAO<PfpOrder,String> implements IPfpOrderDAO{

	@Override
    @SuppressWarnings("unchecked")
	public List<PfpOrder> findPfpOrder(String orderId, String account, Date sDate, Date eDate, String status) {

		StringBuffer hql = new StringBuffer();

		hql.append(" from PfpOrder ");
		hql.append(" where notifyDate <= ? ");
		hql.append(" and notifyDate >= ? ");

		if(StringUtils.isNotBlank(orderId)){
			hql.append(" and orderId = ? ");
		}
		else{
			hql.append(" and orderId != ? ");
		}

		if(StringUtils.isNotBlank(account)){
			hql.append(" and pfpCustomerInfo.customerInfoId = ? ");
		}
		else{
			hql.append(" and pfpCustomerInfo.customerInfoId != ? ");
		}

		if(StringUtils.isNotBlank(status)){
			hql.append(" and ");
			if(status.equals("B4")){
				hql.append(" (status like 'B5%' or status like ? ) ");
			}else{
				hql.append(" status like ? ");
			}

		}
		else{
			hql.append(" and orderId != ? ");
		}

//		hql.append("from PfpOrder as o where ");
//
//		hql.append("'"+eDate+"' >= o.createDate and  o.createDate >= '"+sDate+"' ");
//
//		if(StringUtils.isNotEmpty(orderId)){
//			hql.append("and o.orderId = '"+orderId+"' ");
//		}
//
//		if(StringUtils.isNotEmpty(account)){
//			hql.append("and (o.pfpCustomerInfo.customerInfoId = '"+account+"' or  o.pfpCustomerInfo.customerInfoTitle = '"+account+"') ");
//		}
//
//		if(StringUtils.isNotEmpty(status)){
//
//			hql.append("and ");
//
//			if(status.equals("B4")){
//				hql.append("(o.status like 'B5%' or o.status like '"+status+"%') ");
//			}else{
//				hql.append("o.status like '"+status+"%' ");
//			}
//
//		}
//
		hql.append(" order by orderId ");

		log.info(" status  = "+status);
		log.info(" hql  = "+hql.toString());

		Object[] ob = new Object[]{eDate, sDate, orderId, account, status+"%"};

		return super.getHibernateTemplate().find(hql.toString(),ob);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<PfpOrder> findActivateOrder(String customerInfoId,
			String startDate, String endDate) throws Exception {

		StringBuffer hql = new StringBuffer();

		hql.append(" from PfpOrder");
		hql.append(" where pfpCustomerInfo.customerInfoId = '" + customerInfoId + "'");
		hql.append(" and status in ('" + EnumBillingStatus.B301 + "','" + EnumBillingStatus.B302 + "')");
		hql.append(" and createDate >= '" + startDate + " 00:00:00'");
		hql.append(" and createDate <= '" + endDate + " 23:59:59'");

		return super.getHibernateTemplate().find(hql.toString());
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<PfpOrder> findPfpOrderByDate(String startDate, String endDate) throws Exception{
		StringBuffer hql = new StringBuffer();

		hql.append(" from PfpOrder ");
		hql.append(" where (status = '"+EnumBillingStatus.B301+"' or status = '"+EnumBillingStatus.B302+"') ");
		hql.append(" and createDate >= '"+startDate+" 00:00:00' and createDate <= '"+endDate+" 23:59:59'");
		hql.append(" order by updateDate desc ");

		return super.getHibernateTemplate().find(hql.toString());
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<PfpOrder> successOrder(String customerInfoId, Date startDate, Date endDate) {
		StringBuffer hql = new StringBuffer();

		hql.append(" from PfpOrder ");
		hql.append(" where (status = ? or status = ? ) ");
		hql.append(" and notifyDate between ? and ? ");
        hql.append(" and pfpCustomerInfo.customerInfoId = ? ");

		Object[] ob = new Object[]{
	        EnumBillingStatus.B301.toString(),
			EnumBillingStatus.B302.toString(),
			startDate,
			endDate,
			customerInfoId
		};

		return super.getHibernateTemplate().find(hql.toString(),ob);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<PfpOrder> successOrder(String customerInfoId, Date date, String OrderBy) {

		StringBuffer hql = new StringBuffer();

		hql.append(" from PfpOrder ");
		hql.append(" where pfpCustomerInfo.customerInfoId = ? ");
		hql.append(" and (status = ? or status = ? ) ");
		hql.append(" and notifyDate = ? ");
		hql.append(" order by ?");

		Object[] ob = new Object[]{customerInfoId,
									EnumBillingStatus.B301.toString(),
									EnumBillingStatus.B302.toString(),
									date,
									OrderBy};

		return super.getHibernateTemplate().find(hql.toString(),ob);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<PfpOrder> findSuccessOrder(String orderId){

		StringBuffer hql = new StringBuffer();

		hql.append(" from PfpOrder ");
		hql.append(" where orderId = ? ");
		hql.append(" and (status = ? or status = ? ) ");

		Object[] ob = new Object[]{orderId,
									EnumBillingStatus.B301.toString(),
									EnumBillingStatus.B302.toString()};

		return super.getHibernateTemplate().find(hql.toString(),ob);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<PfpOrder> findRefundOrder(String customerInfoId, Date date) {

		StringBuffer hql = new StringBuffer();
		hql.append(" from PfpOrder ");
		hql.append(" where pfpCustomerInfo.customerInfoId = ? ");
		hql.append(" and (status = ? or status = ? ) ");
		hql.append(" and notifyDate = ? ");

		List<Object> list = new ArrayList<Object>();
		list.add(customerInfoId);
		list.add(EnumBillingStatus.R301.toString());
		list.add(EnumBillingStatus.R302.toString());
		list.add(date);

		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<PfpOrder> findPfpOrder(String pfpCustomerInfoId) {

		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append(" from PfpOrder ");
		hql.append(" where pfpCustomerInfo.customerInfoId = ? ");
		hql.append(" order by orderId  ");

		list.add(pfpCustomerInfoId);


		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@Override
    public Integer deletePfpOrder(String orderId) {

		StringBuffer hql = new StringBuffer();

	    hql.append("delete from PfpOrder ");
	    hql.append("where orderId = ? ");

	    return this.getHibernateTemplate().bulkUpdate(hql.toString(), orderId);
	}
	
	@SuppressWarnings("unchecked")
	public List<PfpOrder> findOrder(String orderId) throws Exception {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from PfpOrder ");
		hql.append(" where orderId = ? ");
		
		return super.getHibernateTemplate().find(hql.toString(), orderId);
	}

	public static void main(String arg[]) throws Exception{

		System.out.println(DateValueUtil.getInstance().getDateDiffDay("2013-11-09", "2013-11-09"));

//		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.path);
//
//		PfpOrderDAO dao = (PfpOrderDAO) context.getBean("PfpOrderDAO");
//		String date = DateValueUtil.getInstance().getDateValue(DateValueUtil.TODAY, DateValueUtil.DBPATH);
//		List<PfpOrder> orderList = dao.successOrder("AC2013071600001", DateValueUtil.getInstance().stringToDate(date));
//		System.out.println(" size  = "+orderList.size());
		//PfpOrder pfpOrder = dao.findApplyPfpOrder("AC201301310000000039");
		//System.out.println("  "+;
		//String date = DateValueUtil.getInstance().getDateValue(DateValueUtil.YESTERDAY, DateValueUtil.DBPATH);
		//System.out.println(" size  = "+dao.findPfpOrder("AC201303060000000023"));
	}
}
