package com.pchome.akbadm.db.dao.ad;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdInvalid;

public class PfpAdInvalidDAO extends BaseDAO<PfpAdInvalid, String> implements IPfpAdInvalidDAO{

	@Override
    @SuppressWarnings("unchecked")
	public List<Object> accountInvalidSum(String customerInfoId, Date date) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select sum(adInvalidClkPrice) ");
		hql.append(" from PfpAdInvalid ");
		hql.append(" where customerInfoId = ? ");
		hql.append(" and adInvalidDate = ? ");

		Object[] ob = new Object[]{customerInfoId,date};

		return super.getHibernateTemplate().find(hql.toString(),ob);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<Object> adActionInvalidSum(String adActionSeq, Date date) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select sum(adInvalidClkPrice) ");
		hql.append(" from PfpAdInvalid ");
		hql.append(" where adInvalidDate = ? ");
        hql.append(" and pfpAd.pfpAdGroup.pfpAdAction.adActionSeq = ? ");

		Object[] ob = new Object[]{date, adActionSeq};

		return super.getHibernateTemplate().find(hql.toString(),ob);
	}

    @Override
    public int deleteMalice(Date recordDate, int recordTime) {
        String hql = "delete from PfpAdInvalid where adInvalidDate = ? and adInvalidTime = ? and adInvalidClk > 0";
        return this.getHibernateTemplate().bulkUpdate(hql, recordDate, recordTime);
    }
}
