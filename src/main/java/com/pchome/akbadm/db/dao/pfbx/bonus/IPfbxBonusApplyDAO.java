package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxBonusApply;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyInvoiceStatus;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyStatus;

public interface IPfbxBonusApplyDAO extends IBaseDAO<PfbxBonusApply, String>{
	
	public List<Object> getListByKeyWords(Date startDate, Date endDate,String keyword, String category, String status);

	public List<PfbxBonusApply> findNotDoneFailOrder(String pfbId);
	
	public List<PfbxBonusApply> findAllDoneOrder(String pfbId);
	
	public List<PfbxBonusApply> findApplyOrderByInvoiceStatus(EnumPfbApplyInvoiceStatus enumPfbApplyInvoiceStatus );
	
	public List<PfbxBonusApply> findApplyOrderByApplyStatus(EnumPfbApplyStatus enumPfbApplyStatus );
	
	public PfbxBonusApply findByApplyId(String pfbId , String applyId);

	List<PfbxBonusApply> findApplyByInvoiceCheckStatus(String customerInfoId, String status,String orderApplyId);

	List<PfbxBonusApply> findOldDetalByInvoiceCheckStatus(String customerInfoId, String invoiceCheckStatus, Integer bankId, Integer personalId);
	
	List<PfbxBonusApply> findApplyOrderByFailStatus();
	
	public void saveOrUpdate(PfbxBonusApply pfbxBonusApply);

	/**
	 * 依據狀態、更新時間及建立時間找請款單
	 * @param enumPfbApplyStatus
	 * @param startDate
	 * @param endDate
	 * @param createDate
	 * @return
	 */
	public List<PfbxBonusApply> findApplyOrderByStatusAndUpdateDate(EnumPfbApplyStatus enumPfbApplyStatus, String startDate, String endDate, String createDate);
}
