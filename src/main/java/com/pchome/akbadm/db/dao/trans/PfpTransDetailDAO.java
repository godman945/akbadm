package com.pchome.akbadm.db.dao.trans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpTransDetail;
import com.pchome.enumerate.trans.EnumTransType;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

public class PfpTransDetailDAO extends BaseDAO <PfpTransDetail, String> implements IPfpTransDetailDAO{

    @Override
    @SuppressWarnings("unchecked")
	public List<PfpTransDetail> findTransDetail(String customerInfo, String startDate, String endDate,
			String pfpCustInfoId) throws Exception {

		StringBuffer hql = new StringBuffer();
		hql.append(" from PfpTransDetail ");
		hql.append(" where transDate >= '" + startDate + "' and transDate <= '" + endDate + "'");

		if(StringUtils.isNotEmpty(customerInfo)){
			hql.append(" and (pfpCustomerInfo.customerInfoId = '" + customerInfo + "' ");
			hql.append(" or pfpCustomerInfo.customerInfoTitle like '%" + customerInfo + "%' ) ");
		}
		if (StringUtils.isNotEmpty(pfpCustInfoId)) {
			hql.append(" and pfpCustomerInfo.customerInfoId in (" + pfpCustInfoId + ")");
		}
		hql.append(" order by transId");

		return super.getHibernateTemplate().find(hql.toString());
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<PfpTransDetail> findPfpTransDetail(String startDate, String endDate,
			String pfpCustInfoId) throws Exception {

		StringBuffer hql = new StringBuffer();
		hql.append(" from PfpTransDetail where 1=1");

		if (StringUtils.isNotEmpty(startDate)) {
			hql.append(" and transDate >= '" + startDate + "'");
		}

		if (StringUtils.isNotEmpty(endDate)) {
			hql.append(" and transDate <= '" + endDate + "'");
		}

		if (StringUtils.isNotEmpty(pfpCustInfoId)) {
			hql.append(" and pfpCustomerInfo.customerInfoId in (" + pfpCustInfoId + ")");
		}

		return super.getHibernateTemplate().find(hql.toString());
	}

	@Override
    @SuppressWarnings("unchecked")
	public TreeMap<String, List<PfpTransDetail>> getPfpTransDetailByCustomerInfoIdList(String startDate, String endDate, List<String> pfpAdCustomerInfoIds, List<String> transTypes) throws Exception {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		HashMap<String, Object> sqlParams = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from PfpTransDetail where 1=1");

		if (StringUtils.isNotEmpty(startDate)) {
			hql.append(" and transDate >= :startDate");
			sqlParams.put("startDate", sdf.parse(startDate));
		}

		if (StringUtils.isNotEmpty(endDate)) {
			hql.append(" and transDate <= :endDate");
			sqlParams.put("endDate", sdf.parse(endDate));
		}

		if (pfpAdCustomerInfoIds != null  && pfpAdCustomerInfoIds.size() > 0) {
			hql.append(" and pfpCustomerInfo.customerInfoId in (:pfpCustomerInfoIds)");
			sqlParams.put("pfpCustomerInfoIds", pfpAdCustomerInfoIds);
		}

		if(transTypes != null && transTypes.size() > 0) {
			hql.append(" and transType in (:transTypes) ");
			sqlParams.put("transTypes", transTypes);
		}
		hql.append(" order by pfpCustomerInfo.customerInfoId");

		//log.info("hql = " + hql.toString());
		//log.info("startDate = " + startDate);
		//log.info("endDate = " + endDate);
		//log.info("pfpAdCustomerInfoIds = " + pfpAdCustomerInfoIds);

		Query query = this.getSession().createQuery(hql.toString());
        for (String paramName:sqlParams.keySet()) {
        	if(paramName.equals("pfpCustomerInfoIds") || paramName.equals("transTypes")) {
        		query.setParameterList(paramName, (ArrayList<String>)sqlParams.get(paramName));
        	} else {
        		query.setParameter(paramName, sqlParams.get(paramName));
        	}
        }

		// 將得到的廣告成效結果，設定成 Map, 以方便用 adKeywordSeq 抓取資料
		TreeMap<String, List<PfpTransDetail>> pfpTransDetailMap = new TreeMap<String, List<PfpTransDetail>>();
		List<PfpTransDetail> pfpTransDetails = query.list();
		List<PfpTransDetail> TransDetails = new ArrayList<PfpTransDetail>();
		System.out.println("pfpTransDetails.size() = " + pfpTransDetails.size());
		String tmp_customerInfoId = "";
		for(PfpTransDetail pfpTransDetail:pfpTransDetails) {
			TransDetails.add(pfpTransDetail);
			if(StringUtils.isNotBlank(tmp_customerInfoId) & !tmp_customerInfoId.equals(pfpTransDetail.getPfpCustomerInfo().getCustomerInfoId())) {
				pfpTransDetailMap.put(tmp_customerInfoId, TransDetails);
			}
			tmp_customerInfoId = pfpTransDetail.getPfpCustomerInfo().getCustomerInfoId();
		}

		return pfpTransDetailMap;
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<PfpTransDetail> checkExistentTransDetail(String customerInfoId, Date date, String transType) throws Exception{

		StringBuffer hql = new StringBuffer();

		hql.append(" from PfpTransDetail ");
		hql.append(" where pfpCustomerInfo.customerInfoId = ?");
		hql.append(" and transDate = ? ");
		hql.append(" and transType = ? ");

		Object[] ob = new Object[]{customerInfoId,
									date,
									transType};

		return super.getHibernateTemplate().find(hql.toString(),ob);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<PfpTransDetail> sortTransDetailDesc(String customerInfoId){
		StringBuffer hql = new StringBuffer();
		hql.append("from PfpTransDetail ");
		hql.append("where pfpCustomerInfo.customerInfoId = ? ");
		hql.append("order by updateDate desc, transId desc ");

		return super.getHibernateTemplate().find(hql.toString(),customerInfoId);
	}

    @SuppressWarnings("unchecked")
    public List<PfpTransDetail> sortTransDetailDesc(String customerInfoId, int firstResult, int maxResult){
        StringBuffer hql = new StringBuffer();
        hql.append("from PfpTransDetail ");
        hql.append("where pfpCustomerInfo.customerInfoId = :customerInfoId ");
        hql.append("order by updateDate desc, transId desc ");

        Query query = this.getSession().createQuery(hql.toString());
        query.setString("customerInfoId", customerInfoId);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);

        return query.list();
    }

	@Override
    public Integer deleteRecordAfterDate(Date startDate) {

		StringBuffer hql = new StringBuffer();

		hql.append(" delete from PfpTransDetail ");
		hql.append(" where transDate >= ? ");

		return this.getHibernateTemplate().bulkUpdate(hql.toString(), startDate);
	}

	@Override
    public Integer deleteRecordAfterDateNotFeedback(Date startDate) {

		StringBuffer hql = new StringBuffer();

		hql.append(" delete from PfpTransDetail ");
		hql.append(" where trans_type != '6' ");
		hql.append(" and transDate >= ? ");

		return this.getHibernateTemplate().bulkUpdate(hql.toString(), startDate);
	}

	@Override
    public Integer deleteRecordAfterDateForFeedback(Date startDate) {

		StringBuffer hql = new StringBuffer();

		hql.append(" delete from PfpTransDetail ");
		hql.append(" where trans_type = '6' ");
		hql.append(" and transDate >= ? ");

		return this.getHibernateTemplate().bulkUpdate(hql.toString(), startDate);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<PfpTransDetail> findTransDetailSpendCost(String customerInfoId, Date date){

		StringBuffer hql = new StringBuffer();

		hql.append(" from PfpTransDetail ");
		hql.append(" where pfpCustomerInfo.customerInfoId = ? ");
		hql.append(" and transDate = ? ");
		hql.append(" and transType = ? ");

		return super.getHibernateTemplate().find(hql.toString(), customerInfoId, date, EnumTransType.SPEND_COST.getTypeId());
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<PfpTransDetail> findTransDetailInvalidCost(String customerInfoId, Date date){

		StringBuffer hql = new StringBuffer();

		hql.append(" from PfpTransDetail ");
		hql.append(" where pfpCustomerInfo.customerInfoId = ? ");
		hql.append(" and transDate = ? ");
		hql.append(" and transType = ? ");

		return super.getHibernateTemplate().find(hql.toString(), customerInfoId, date, EnumTransType.INVALID_COST.getTypeId());
	}

    @Override
    public float selectActivatePriceSumByActivateDate(EnumTransType enumTransType, String activateDate) {
        StringBuffer hql = new StringBuffer();
        hql.append("select sum(transPrice) ");
        hql.append("from PfpTransDetail ");
        hql.append("where pfpCustomerInfo.recognize = 'Y' ");
        hql.append("    and transType = :transType ");
        hql.append("    and transDate = :transDate ");
        hql.append("    and pfpCustomerInfo.recognize = 'Y' ");
        hql.append("    and pfpCustomerInfo.activateDate >= :startDate ");
        hql.append("    and pfpCustomerInfo.activateDate <= :endDate ");
        hql.append("order by transId desc ");

        Double result = (Double) this.getSession()
                        .createQuery(hql.toString())
                        .setString("transType", enumTransType.getTypeId())
                        .setString("transDate", activateDate)
                        .setString("startDate", activateDate + " 00:00:00")
                        .setString("endDate", activateDate + " 23:59:59")
                        .uniqueResult();
        return result != null ? result.floatValue() : 0f;
    }

    @Override
    public float selectActivatePriceSumByActivateDate(EnumPfdAccountPayType enumPfdAccountPayType, EnumTransType enumTransType, String activateDate) {
        StringBuffer hql = new StringBuffer();
        hql.append("select sum(ptd.transPrice) ");
        hql.append("from PfpTransDetail ptd, ");
        hql.append("     PfdUserAdAccountRef puaar ");
        hql.append("where ptd.pfpCustomerInfo.recognize = 'Y' ");
        hql.append("    and ptd.transType = :transType ");
        hql.append("    and ptd.transDate = :transDate ");
        hql.append("    and ptd.pfpCustomerInfo.activateDate >= :startDate ");
        hql.append("    and ptd.pfpCustomerInfo.activateDate <= :endDate ");
        hql.append("    and puaar.pfpPayType = :pfpPayType ");
        hql.append("    and ptd.pfpCustomerInfo.customerInfoId = puaar.pfpCustomerInfo.customerInfoId ");
        hql.append("order by ptd.transId desc ");

        Double result = (Double) this.getSession()
                        .createQuery(hql.toString())
                        .setString("transType", enumTransType.getTypeId())
                        .setString("transDate", activateDate)
                        .setString("startDate", activateDate + " 00:00:00")
                        .setString("endDate", activateDate + " 23:59:59")
                        .setString("pfpPayType", enumPfdAccountPayType.getPayType())
                        .uniqueResult();
        return result != null ? result.floatValue() : 0f;
    }

//    public float selectActivatePriceSumByActivateDate(EnumPfdAccountPayType enumPfdAccountPayType, EnumTransType enumTransType, String activateDate) {
//        StringBuffer hql = new StringBuffer();
//        hql.append("select sum(ptd.transPrice) ");
//        hql.append("from PfpTransDetail ptd, ");
//        hql.append("     PfdUserAdAccountRef puaar ");
//        hql.append("where ptd.pfpCustomerInfo.recognize = 'Y' ");
//        hql.append("    and ptd.transType = :transType ");
//        hql.append("    and ptd.transDate = :transDate ");
//        hql.append("    and ptd.pfpCustomerInfo.activateDate >= :startDate ");
//        hql.append("    and ptd.pfpCustomerInfo.activateDate <= :endDate ");
//        hql.append("    and puaar.pfpPayType = :pfpPayType ");
//        hql.append("    and ptd.pfpCustomerInfo.customerInfoId = puaar.pfpCustomerInfo.customerInfoId ");
//        hql.append("order by ptd.transId desc ");
//
//        Double result = (Double) this.getSession()
//                        .createQuery(hql.toString())
//                        .setString("transType", enumTransType.getTypeId())
//                        .setString("transDate", activateDate)
//                        .setString("startDate", activateDate + " 00:00:00")
//                        .setString("endDate", activateDate + " 23:59:59")
//                        .setString("pfpPayType", enumPfdAccountPayType.getPayType())
//                        .uniqueResult();
//        return result != null ? result.floatValue() : 0f;
//    }

    @Override
    public int selectCustomerInfoCountByTransDate(EnumTransType enumTransType, String transDate) {
        StringBuffer hql = new StringBuffer();
        hql.append("select count(distinct pfpCustomerInfo.customerInfoId) ");
        hql.append("from PfpTransDetail ");
        hql.append("where transType = :transType ");
        hql.append("    and transDate = :transDate ");
        hql.append("    and pfpCustomerInfo.recognize = 'Y' ");

        return ((Long) this.getSession()
                .createQuery(hql.toString())
                .setString("transType", enumTransType.getTypeId())
                .setString("transDate", transDate)
                .uniqueResult())
                .intValue();
    }


//    public int selectCustomerInfoCountByTransDate(EnumPfdAccountPayType enumPfdAccountPayType, EnumTransType enumTransType, String transDate) {
//        StringBuffer hql = new StringBuffer();
//        hql.append("select count(distinct ptd.pfpCustomerInfo.customerInfoId) ");
//        hql.append("from PfpTransDetail ptd, ");
//        hql.append("     PfdUserAdAccountRef puaar ");
//        hql.append("where ptd.pfpCustomerInfo.recognize = 'Y' ");
//        hql.append("    and ptd.transType = :transType ");
//        hql.append("    and ptd.transDate = :transDate ");
//        hql.append("    and puaar.pfpPayType = :pfpPayType ");
//        hql.append("    and ptd.pfpCustomerInfo.customerInfoId = puaar.pfpCustomerInfo.customerInfoId ");
//
//        return ((Long) this.getSession()
//                .createQuery(hql.toString())
//                .setString("transType", enumTransType.getTypeId())
//                .setString("transDate", transDate)
//                .setString("pfpPayType", enumPfdAccountPayType.getPayType())
//                .uniqueResult())
//                .intValue();
//    }

    @Override
    public int selectCustomerInfoCountByTransDate(EnumPfdAccountPayType enumPfdAccountPayType, EnumTransType enumTransType, String transDate) {
        StringBuffer hql = new StringBuffer();
        hql.append("select count(distinct ptd.pfpCustomerInfo.customerInfoId) ");
        hql.append("from PfpTransDetail ptd, ");
        hql.append("     PfdUserAdAccountRef puaar ");
        hql.append("where ptd.pfpCustomerInfo.recognize = 'Y' ");
        hql.append("    and ptd.transType = :transType ");
        hql.append("    and ptd.transDate = :transDate ");
        hql.append("    and puaar.pfpPayType = :pfpPayType ");
        hql.append("    and ptd.pfpCustomerInfo.customerInfoId = puaar.pfpCustomerInfo.customerInfoId ");

        return ((Long) this.getSession()
                .createQuery(hql.toString())
                .setString("transType", enumTransType.getTypeId())
                .setString("transDate", transDate)
                .setString("pfpPayType", enumPfdAccountPayType.getPayType())
                .uniqueResult())
                .intValue();
    }


    @Override
    public float selectTransPriceSumByTransDate(EnumTransType enumTransType, String transDate) {
        StringBuffer hql = new StringBuffer();
        hql.append("select sum(transPrice) ");
        hql.append("from PfpTransDetail ");
        hql.append("where transType = :transType ");
        hql.append("    and transDate = :transDate ");
        hql.append("    and pfpCustomerInfo.recognize = 'Y' ");

        Double result = (Double) this.getSession()
                .createQuery(hql.toString())
                .setString("transType", enumTransType.getTypeId())
                .setString("transDate", transDate)
                .uniqueResult();
        return result != null ? result.floatValue() : 0f;
    }

    @Override
    public float selectTransPriceSumByTransDate(EnumPfdAccountPayType enumPfdAccountPayType, EnumTransType enumTransType, String transDate) {
        StringBuffer hql = new StringBuffer();
        hql.append("select sum(ptd.transPrice) ");
        hql.append("from PfpTransDetail ptd, ");
        hql.append("     PfdUserAdAccountRef puaar ");
        hql.append("where ptd.pfpCustomerInfo.recognize = 'Y' ");
        hql.append("    and ptd.transType = :transType ");
        hql.append("    and ptd.transDate = :transDate ");
        hql.append("    and puaar.pfpPayType = :pfpPayType ");
        hql.append("    and ptd.pfpCustomerInfo.customerInfoId = puaar.pfpCustomerInfo.customerInfoId ");

        Double result = (Double) this.getSession()
                .createQuery(hql.toString())
                .setString("transType", enumTransType.getTypeId())
                .setString("transDate", transDate)
                .setString("pfpPayType", enumPfdAccountPayType.getPayType())
                .uniqueResult();
        return result != null ? result.floatValue() : 0f;
    }

//    public float selectTransPriceSumByTransDate(EnumPfdAccountPayType enumPfdAccountPayType, EnumTransType enumTransType, String transDate) {
//        StringBuffer hql = new StringBuffer();
//        hql.append("select sum(ptd.transPrice) ");
//        hql.append("from PfpTransDetail ptd, ");
//        hql.append("     PfdUserAdAccountRef puaar ");
//        hql.append("where ptd.pfpCustomerInfo.recognize = 'Y' ");
//        hql.append("    and ptd.transType = :transType ");
//        hql.append("    and ptd.transDate = :transDate ");
//        hql.append("    and puaar.pfpPayType = :pfpPayType ");
//        hql.append("    and ptd.pfpCustomerInfo.customerInfoId = puaar.pfpCustomerInfo.customerInfoId ");
//
//        Double result = (Double) this.getSession()
//                .createQuery(hql.toString())
//                .setString("transType", enumTransType.getTypeId())
//                .setString("transDate", transDate)
//                .setString("pfpPayType", enumPfdAccountPayType.getPayType())
//                .uniqueResult();
//        return result != null ? result.floatValue() : 0f;
//    }

    @Override
    public float selectRemainPriceSumByTransDate(String transDate) {
        StringBuffer hql = new StringBuffer();
        hql.append("select sum(transPrice) ");
        hql.append("from PfpTransDetail ");
        hql.append("where transId in ( ");
        hql.append("    select max(transId) ");
        hql.append("    from PfpTransDetail ");
        hql.append("    where transDate = :transDate ");
        hql.append("    group by pfpCustomerInfo.customerInfoId ");
        hql.append(") ");

        Query query = this.getSession().createQuery(hql.toString());
        query.setString("transDate", transDate);

        Object result = query.uniqueResult();
        if (result == null) {
            return 0f;
        }

        return ((Double) result).floatValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PfpTransDetail> findTransDetail(String customerInfoId, Date date, String typeId){

    	StringBuffer hql = new StringBuffer();
    	hql.append(" from PfpTransDetail ");
		hql.append(" where pfpCustomerInfo.customerInfoId = ? ");
		hql.append(" and transDate >= ? ");
		hql.append(" and transType >= ? ");

		return super.getHibernateTemplate().find(hql.toString(), customerInfoId, date, typeId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PfpTransDetail> findPfpLastTransDetails(Date date) {

    	StringBuffer hql = new StringBuffer();
    	hql.append(" from PfpTransDetail ");
		hql.append(" where transDate = ? ");
		hql.append(" order by transId ");

		return super.getHibernateTemplate().find(hql.toString(), date);
    }
}
