package com.pchome.akbadm.db.dao.ad;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdExcludeKeyword;

public class PfpAdExcludeKeywordDAO extends BaseDAO<PfpAdExcludeKeyword,String> implements IPfpAdExcludeKeywordDAO {
	@Override
    @SuppressWarnings("unchecked")
	public List<PfpAdExcludeKeyword> getPfpAdExcludeKeywords(String adExcludeKeywordSeq, String adGroupSeq, String adExcludeKeyword) throws Exception{
		StringBuffer sql = new StringBuffer("from PfpAdExcludeKeyword where 1=1");
		if (StringUtils.isNotEmpty(adExcludeKeywordSeq)) {
			sql.append(" and adExcludeKeywordSeq like '%" + adExcludeKeywordSeq.trim() + "%'");
		}

		if (StringUtils.isNotEmpty(adGroupSeq)) {
			sql.append(" and adGroupSeq like '%" + adGroupSeq.trim() + "%'");
		}

		if (StringUtils.isNotEmpty(adExcludeKeyword)) {
			sql.append(" and adExcludeKeyword like '%" + adExcludeKeyword.trim() + "%'");
		}

		return (List<PfpAdExcludeKeyword>) super.getHibernateTemplate().find(sql.toString());
	}

	@Override
    @SuppressWarnings("unchecked")
	public PfpAdExcludeKeyword getPfpAdExcludeKeywordBySeq(String adExcludeKeywordSeq) throws Exception {
		List<PfpAdExcludeKeyword> list = (List<PfpAdExcludeKeyword>) super.getHibernateTemplate().find("from PfpAdExcludeKeyword where adExcludeKeywordSeq = '" + adExcludeKeywordSeq + "'");
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
    public void saveOrUpdatePfpAdExcludeKeyword(PfpAdExcludeKeyword pfpAdExcludeKeyword) throws Exception{
		super.getHibernateTemplate().saveOrUpdate(pfpAdExcludeKeyword);
	}

	@Override
    public void insertPfpAdExcludeKeyword(PfpAdExcludeKeyword pfpAdExcludeKeyword) throws Exception {
		this.saveOrUpdate(pfpAdExcludeKeyword);
	}

	@Override
    public void updatePfpAdExcludeKeyword(PfpAdExcludeKeyword pfpAdExcludeKeyword) throws Exception {
		this.update(pfpAdExcludeKeyword);
	}

	@Override
    public void deletePfpAdExcludeKeyword(String adExcludeKeywordSeq) throws Exception{
		String userSql = "delete from PfpAdExcludeKeyword where adExcludeKeywordSeq = '" + adExcludeKeywordSeq + "'";
		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
		session.createQuery(userSql).executeUpdate();
		session.flush();
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<PfpAdExcludeKeyword> getPfpAdExcludeKeywords(String adGroupSeq, String customerInfoId) throws Exception{
		StringBuffer hql = new StringBuffer();

		hql.append(" from PfpAdExcludeKeyword  ");
		hql.append(" where pfpAdGroup.adGroupSeq = '" + adGroupSeq + "' ");
		hql.append(" and pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoId = '"+customerInfoId+"' ");
		//hql.append(" and adExcludeKeywordStatus != "+EnumExcludeKeywordStatus.CLOSE.getStatusId()+"  ");

		return (List<PfpAdExcludeKeyword>) super.getHibernateTemplate().find(hql.toString());
	}

    @Override
    @SuppressWarnings("unchecked")
    public List<PfpAdExcludeKeyword> selectPfpAdExcludeKeywords(String adGroupSeq, int status) {
        String hql = "from PfpAdExcludeKeyword where pfpAdGroup.adGroupSeq = ? and adExcludeKeywordStatus = ?";
        return (List<PfpAdExcludeKeyword>) super.getHibernateTemplate().find(hql, adGroupSeq, status);
    }
}
