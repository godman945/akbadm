package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxBonusTransDetail;

public interface IPfbxBonusTransDetailDAO extends IBaseDAO<PfbxBonusTransDetail, Integer> {
	public List<PfbxBonusTransDetail> findByPfbId(String pfbId);
	
	public Integer deletePfbxBonusTransDetail(Date deleteDate);
	
	public List<PfbxBonusTransDetail> findLastPfbxBonusTransDetail(String pfbId);
		
	public List<PfbxBonusTransDetail> findApplyNullPfbxBonusTransDetail(String pfbId);

	public List<PfbxBonusTransDetail> findApplyNullPfbxBonusInvalidTransDetail(String pfbId);

	/**
	 * 刪除 pfbx_bonus_trans_detail 獎金交易明細 的紀錄
	 * @param customerInfoId 聯播網帳戶
	 * @param startDate 計算開始日期
	 * @param endDate 計算結算日期
	 * @param transItem 交易項目
	 * @param failApplyId 失敗請款編號
	 */
	public void deletePfbxBonusTransDetail(String customerInfoId, String startDate, String endDate, String transItem, String failApplyId);
}
