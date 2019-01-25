package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxBonusTransDetail;
import com.pchome.enumerate.pfbx.bonus.EnumPfbxBonusTrans;

public class PfbxBonusTransDetailDAO extends BaseDAO<PfbxBonusTransDetail, Integer> implements IPfbxBonusTransDetailDAO
{
	@SuppressWarnings("unchecked")
	public List<PfbxBonusTransDetail> findByPfbId(String pfbId) {
		log.info("PfbxBonusTransDetailDAO-findByPfbId");

		StringBuffer hql = new StringBuffer();
		List<Object> pos = new ArrayList<Object>();

		hql.append("from PfbxBonusTransDetail where pfbId = ? ");
		pos.add(pfbId);
		hql.append("order by id desc");

		return super.getHibernateTemplate().find(hql.toString(), pos.toArray());
	}

	public Integer deletePfbxBonusTransDetail(Date deleteDate) {
		StringBuffer hql = new StringBuffer();
		hql.append(" delete from PfbxBonusTransDetail ");
		hql.append(" where endDate >= ? ");
		hql.append(" and transItem = ? ");

		return super.getHibernateTemplate().bulkUpdate(hql.toString(), deleteDate, EnumPfbxBonusTrans.ADD_EXPENSE.getType());
	}

	@SuppressWarnings("unchecked")
	public List<PfbxBonusTransDetail> findLastPfbxBonusTransDetail(String pfbId) {
		StringBuffer hql = new StringBuffer();
		hql.append(" from PfbxBonusTransDetail ");
		hql.append(" where pfbId = ? ");
		hql.append(" order by id desc");
		
		return super.getHibernateTemplate().find(hql.toString(), pfbId); 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PfbxBonusTransDetail> findApplyNullPfbxBonusTransDetail(String pfbId) {
		
		StringBuffer hql = new StringBuffer();
		hql.append(" from PfbxBonusTransDetail ");
		hql.append(" where pfbId = ? ");
		
		hql.append(" and applyId is null ");
		hql.append(" and transIn > 0 ");
		
		return super.getHibernateTemplate().find(hql.toString(), pfbId); 
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PfbxBonusTransDetail> findApplyNullPfbxBonusInvalidTransDetail(String pfbId) {
		
		StringBuffer hql = new StringBuffer();

		hql.append(" from PfbxBonusTransDetail ");
		hql.append(" where pfbId = ? ");

		hql.append(" and applyId is null ");
		hql.append(" and transOut > 0 ");
		
		return super.getHibernateTemplate().find(hql.toString(), pfbId); 
	}

	/**
	 * 刪除 pfbx_bonus_trans_detail 獎金交易明細 的紀錄
	 * @param customerInfoId 聯播網帳戶
	 * @param startDate 計算開始日期
	 * @param endDate 計算結算日期
	 * @param transItem 交易項目
	 * @param failApplyId 失敗請款編號
	 */
	@Override
	public void deletePfbxBonusTransDetail(String customerInfoId, String startDate, String endDate, String transItem, String failApplyId) {
		String hql = "DELETE FROM PfbxBonusTransDetail ";
		hql += " WHERE pfb_id = '" + customerInfoId + "'";
		hql += " AND start_date = '" + startDate + "'";
		hql += " AND end_date = '" + endDate + "'";
		hql += " AND trans_item = '" + transItem + "'";
		hql += " AND fail_apply_id = '" + failApplyId + "'";
		Session session = getSession();
		session.createQuery(hql).executeUpdate();
		session.flush();
	}
}
