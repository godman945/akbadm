package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxBonusApply;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyInvoiceStatus;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyStatus;

public class PfbxBonusApplyDAO extends BaseDAO<PfbxBonusApply, String> implements IPfbxBonusApplyDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getListByKeyWords(Date startDate, Date endDate, String keyword, String category, String status) {
		super.getSession().beginTransaction().commit();

		StringBuffer sql = new StringBuffer();

		sql.append(" select apply.apply_date, apply.apply_id, apply.pfb_id, ");
		sql.append(" case when cus.category = '2' then cus.company_name else cus.contact_name end, cus.category, ");
		sql.append(" case when cus.category = '2' then cus.tax_id else personal.id_card end, ");
		sql.append(" cus.member_id, apply.apply_status, apply.apply_note, apply.apply_money, ");
		sql.append(" bank.bank_name, bank.check_status, personal.name, personal.check_status, ");
		sql.append(" apply.invoice_status, apply.invoice_note, apply.invoice_check_status ");
		sql.append(" from pfbx_bonus_apply apply ");
		sql.append(" join pfbx_customer_info cus on apply.pfb_id = cus.customer_info_id ");
		sql.append(" left join pfbx_bank bank on apply.pfbx_bank = bank.id ");
		sql.append(" left join pfbx_personal personal on apply.pfbx_personal = personal.id ");
		sql.append(" where 1=1 ");
		
		if (startDate != null) {
			sql.append(" and apply.apply_date >=:startDate ");
		}
		if (endDate != null) {
			sql.append(" and apply.apply_date <=:endDate ");
		}
		
		if (StringUtils.isNotBlank(keyword)) {
			sql.append(" and (cus.customer_info_id like '%" + keyword + "%' or cus.tax_id like '%" + keyword + "%' or cus.member_id like '%" + keyword + "%'"
					+ " or apply.apply_id like '%" + keyword + "%' "
					+ " or personal.id_card like '%" + keyword + "%') ");
		}
		
		if (StringUtils.isNotBlank(category)) {
			sql.append("and cus.category = '" + category + "'");
		}

		if (StringUtils.isNotBlank(status)) {
			sql.append("and apply.apply_status = '" + status + "'");
		}

		sql.append(" order by apply.apply_date desc, cus.customer_info_id desc ");
		
		Query q = super.getSession().createSQLQuery(sql.toString());
		
		if (startDate != null) {
			q.setDate("startDate", startDate);
		}
		
		if (startDate != null) {
			q.setDate("endDate", endDate);
		}

		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PfbxBonusApply> findNotDoneFailOrder(String pfbId) {
		StringBuffer hql = new StringBuffer();
		hql.append(" from PfbxBonusApply ");
		hql.append(" where 1 = 1 ");
		hql.append(" and  pfbxCustomerInfo.customerInfoId = ? ");
		hql.append(" and ( applyStatus = ?  or  applyStatus = ? )");

		return super.getHibernateTemplate().find(hql.toString(), pfbId, EnumPfbApplyStatus.APPLY.getStatus(), EnumPfbApplyStatus.WAIT_PAY.getStatus());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void saveOrUpdate(PfbxBonusApply pfbxBonusApply) {
		super.getHibernateTemplate().saveOrUpdate(pfbxBonusApply);
		super.getHibernateTemplate().flush();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PfbxBonusApply> findAllDoneOrder(String pfbId) {
		StringBuffer hql = new StringBuffer();
		hql.append(" from PfbxBonusApply ");
		hql.append(" where 1 = 1 ");
		hql.append(" and  pfbxCustomerInfo.customerInfoId = ? ");
		hql.append(" and applyStatus = ?");
		hql.append(" order by applyDate desc");

		return super.getHibernateTemplate().find(hql.toString(), pfbId, EnumPfbApplyStatus.SUCCESS.getStatus());
	}

	@Override
	public PfbxBonusApply findByApplyId(String pfbId, String applyId) {
		StringBuffer hql = new StringBuffer();
		hql.append(" from PfbxBonusApply ");
		hql.append(" where 1 = 1 ");
		hql.append(" and  pfbxCustomerInfo.customerInfoId = ? ");
		hql.append(" and  applyId = ? ");
		@SuppressWarnings("unchecked")
		List<PfbxBonusApply> list = super.getHibernateTemplate().find(hql.toString(), pfbId, applyId);

		PfbxBonusApply pfbxBonusApply = null;

		if (!list.isEmpty()) {
			pfbxBonusApply = list.get(0);
		}

		return pfbxBonusApply;
	}

	@Override
	public List<PfbxBonusApply> findApplyByInvoiceCheckStatus(String customerInfoId, String status,String orderApplyId) {
		Criteria criteria = super.getSession().createCriteria(PfbxBonusApply.class);
		if(StringUtils.isNotBlank(customerInfoId)) {
			criteria.add(Restrictions.eq("pfbxCustomerInfo.customerInfoId", customerInfoId));
		}
		if(StringUtils.isNotBlank(orderApplyId)) {
			criteria.add(Restrictions.eq("applyId", orderApplyId));
		}
		criteria.add(Restrictions.eq("invoiceCheckStatus", status));
		return criteria.list();
	}

	@Override
    public List<PfbxBonusApply> findOldDetalByInvoiceCheckStatus(String customerInfoId, String invoiceCheckStatus, Integer bankId, Integer personalId) {
        Criteria criteria = super.getSession().createCriteria(PfbxBonusApply.class);
        criteria.add(Restrictions.eq("pfbxCustomerInfo.customerInfoId", customerInfoId));
        criteria.add(Restrictions.eq("invoiceCheckStatus", invoiceCheckStatus));
        criteria.add(Restrictions.eq("pfbxBank.id", bankId));
        criteria.add(Restrictions.eq("pfbxPersonal.id", personalId));
        criteria.list();
        return criteria.list();
    }
	
	@Override
	public List<PfbxBonusApply> findApplyOrderByFailStatus() {
		Criteria criteria = super.getSession().createCriteria(PfbxBonusApply.class);
		Disjunction orCriteria = Restrictions.disjunction();
		/*orCriteria.add(Restrictions.or(Restrictions.eq("invoiceStatus", EnumPfbApplyInvoiceStatus.FAIL.getStatus()),
				Restrictions.eq("invoiceStatus", EnumPfbApplyInvoiceStatus.VERIFY.getStatus())));
		criteria.add(orCriteria);*/
		criteria.add(Restrictions.eq("applyStatus", EnumPfbApplyStatus.APPLY.getStatus()));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PfbxBonusApply> findApplyOrderByInvoiceStatus(EnumPfbApplyInvoiceStatus enumPfbApplyInvoiceStatus) {
		StringBuffer hql = new StringBuffer();
		hql.append(" from PfbxBonusApply ");
		hql.append(" where 1 = 1 ");
		hql.append(" and  invoiceStatus = ? ");
		hql.append(" and  applyStatus = ? ");

		return super.getHibernateTemplate().find(hql.toString(), enumPfbApplyInvoiceStatus.getStatus(), EnumPfbApplyStatus.APPLY.getStatus());
	}
	
    @SuppressWarnings("unchecked")
	@Override
	public List<PfbxBonusApply> findApplyOrderByApplyStatus(EnumPfbApplyStatus enumPfbApplyStatus) {
		StringBuffer hql = new StringBuffer();
		hql.append(" from PfbxBonusApply ");
		hql.append(" where 1 = 1 ");
		hql.append(" and  applyStatus = ? ");

		return super.getHibernateTemplate().find(hql.toString(), enumPfbApplyStatus.getStatus());
	}

    /**
	 * 依據狀態、更新時間及建立時間找請款單
	 * @param enumPfbApplyStatus
	 * @param startDate
	 * @param endDate
	 * @param createDate
	 * @return
	 */
	@Override
	public List<PfbxBonusApply> findApplyOrderByStatusAndUpdateDate(EnumPfbApplyStatus enumPfbApplyStatus, String startDate, String endDate, String createDate) {
		StringBuffer hql = new StringBuffer();
		hql.append(" from PfbxBonusApply ");
		hql.append(" where 1 = 1 ");
		hql.append(" and applyStatus = ? ");
		hql.append(" and update_date >= ? ");
		hql.append(" and update_date <= ? ");
		hql.append(" and create_date >= ? ");

		return super.getHibernateTemplate().find(hql.toString(), enumPfbApplyStatus.getStatus(), startDate, endDate, createDate);
	}
}
