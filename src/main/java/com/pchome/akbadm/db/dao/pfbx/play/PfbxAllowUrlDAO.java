package com.pchome.akbadm.db.dao.pfbx.play;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxAllowUrl;
import com.pchome.enumerate.pfbx.account.EnumPfbxAllowUrlStatus;


public class PfbxAllowUrlDAO extends BaseDAO<PfbxAllowUrl, String> implements IPfbxAllowUrlDAO {
    @Override
    public List<PfbxAllowUrl> selectPfbxAllowUrl(String deleteFlag, String urlStatus) {
        StringBuffer hql = new StringBuffer();
        hql.append("from PfbxAllowUrl ");
        hql.append("where deleteFlag = ? ");
        hql.append("    and urlStatus = ? ");
        return (List<PfbxAllowUrl>) this.getHibernateTemplate().find(hql.toString(), deleteFlag, urlStatus);
    }

    @Override
    public List<PfbxAllowUrl> selectPfbxAllowUrl(String deleteFlag, String urlStatus, String playType) {
        StringBuffer hql = new StringBuffer();
        hql.append("from PfbxAllowUrl ");
        hql.append("where deleteFlag = ? ");
        hql.append("    and urlStatus = ? ");
        hql.append("    and pfbxCustomerInfo.playType = ? ");
        return (List<PfbxAllowUrl>) this.getHibernateTemplate().find(hql.toString(), deleteFlag, urlStatus, playType);
    }

    @Override
	@SuppressWarnings("unchecked")
	public List<PfbxAllowUrl> findPfbxAllowUrlByCondition(String pfbxCustomerInfoId) throws Exception {

		StringBuffer sb = new StringBuffer();
		sb.append("from PfbxAllowUrl where deleteFlag = '0'");

		if (StringUtils.isNotEmpty(pfbxCustomerInfoId)) {
			sb.append(" and pfbxCustomerInfo.customerInfoId in ('" + pfbxCustomerInfoId + "')");
		}


		sb.append(" order by createDate desc");

		String hql = sb.toString();
		log.info(">>> hql = " + hql);

		return (List<PfbxAllowUrl>) super.getHibernateTemplate().find(hql);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PfbxAllowUrl> findDefaultUrl(String pfbxCustomerInfoId) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("from PfbxAllowUrl where defaultType = 'Y'");

		if (StringUtils.isNotEmpty(pfbxCustomerInfoId)) {
			sb.append(" and pfbxCustomerInfo.customerInfoId in ('" + pfbxCustomerInfoId + "')");
		}

		String hql = sb.toString();
		log.info(">>> hql = " + hql);

		return (List<PfbxAllowUrl>) super.getHibernateTemplate().find(hql);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PfbxAllowUrl> getPfbxAllowUrlById(String id) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("from PfbxAllowUrl where id =" + id);

		String hql = sb.toString();
		log.info(">>> hql = " + hql);

		return (List<PfbxAllowUrl>) super.getHibernateTemplate().find(hql);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PfbxAllowUrl> findPfbxAllowUrlByRootDomain(String pfbxCustomerInfoId, String rootDomain, String id) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("from PfbxAllowUrl where deleteFlag = '0' ");
		sb.append(" and pfbxCustomerInfo.customerInfoId = '" + pfbxCustomerInfoId + "' ");
		sb.append(" and urlStatus in ('" + EnumPfbxAllowUrlStatus.APPLY.getCode() + "','" + EnumPfbxAllowUrlStatus.START.getCode() + "') ");
		sb.append(" and (rootDomain like CONCAT('%.','" + rootDomain + "','%') or rootDomain like CONCAT('" + rootDomain + "','%')  ");
		sb.append(" or '" + rootDomain + "' like CONCAT('%.',rootDomain,'%') or '" + rootDomain + "' like CONCAT('rootDomain','%') ) ");
		sb.append(" and id not in ('" + id + "')");

		String hql = sb.toString();
		log.info(">>> hql = " + hql);

		return (List<PfbxAllowUrl>) super.getHibernateTemplate().find(hql);
	}
}
