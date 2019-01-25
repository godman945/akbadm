package com.pchome.akbadm.db.service.pfbx.bonus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.pfbx.bonus.IPfbxBonusApplyDAO;
import com.pchome.akbadm.db.pojo.PfbxBonusApply;
import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.pfbx.bonus.PfbxApplyBonusVo;
import com.pchome.akbadm.factory.pfbx.bonus.CheckStatusFactory;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyInvoiceStatus;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyStatus;

public class PfbxBonusApplyService extends BaseService<PfbxBonusApply, String> implements IPfbxBonusApplyService {
	private CheckStatusFactory checkStatusFactory;
	
	public PfbxCustomerInfo getCustomerInfoByApplyId(String applyId) {
		return ((IPfbxBonusApplyDAO) dao).get(applyId).getPfbxCustomerInfo();
	}

	public List<PfbxApplyBonusVo> getPfbxApplyBonusVos(String startDate, String endDate, String keyword, String category, String status) throws Exception
	{
		List<PfbxApplyBonusVo> vos = new ArrayList<PfbxApplyBonusVo>();
		PfbxApplyBonusVo vo = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date sDate = null;
		Date eDate = null;

		if (StringUtils.isNotEmpty(startDate)) {
			sDate = sdf.parse(startDate);
		}
		if (StringUtils.isNotEmpty(endDate)) {
			eDate = sdf.parse(endDate);
		}

		List<Object> applys = ((IPfbxBonusApplyDAO) dao).getListByKeyWords(sDate, eDate, keyword, category, status);
		
		for (Object object : applys){
			Object[] ob = (Object[])object;
			vo = new PfbxApplyBonusVo();
			vo.setPfbxApplyDate(sdf.parse(ob[0].toString()));
			vo.setPfbxApplyBonusId(ob[1].toString());
			vo.setPfbxCustomerInfoId(ob[2].toString());
			vo.setPfbxCustomerInfoName(ob[3].toString());
			vo.setPfbxCustomerCategory(ob[4].toString());
			vo.setPfbxCustomerCategoryName(checkStatusFactory.getEnumPfbxAccountCategoryName(ob[4].toString()));
			if(ob[5] != null){
				vo.setPfbxCustomerTaxId(ob[5].toString());
			}
			vo.setPfbxCustomerMemberId(ob[6].toString());
			vo.setPfbxApplyStatus(ob[7].toString());
			if(ob[8] != null){
				vo.setPfbxApplyNote(ob[8].toString());
			}
			vo.setApplyMoney(Float.parseFloat(ob[9].toString()));
			if(ob[10] != null){
				vo.setBankName(ob[10].toString());
			}
			if(ob[11] != null){
				vo.setBankCheckStatus(checkStatusFactory.getEnumPfbxCheckStatusName(ob[11].toString()));
			}
			if(ob[12] != null){
				vo.setPersonalName(ob[12].toString());
			}
			if(ob[13] != null){
				vo.setPersonalCheckStatus(checkStatusFactory.getEnumPfbxCheckStatusName(ob[13].toString()));		
			}
			if(ob[14] != null){
				vo.setPfbApplyInvoiceStatus(ob[14].toString());
			}
			if(ob[15] != null){
				vo.setPfbApplyInvoiceNote(ob[15].toString());
			}
			if(ob[16] != null){
				vo.setPfbApplyInvoiceCheckStatus(ob[16].toString());
			}

			vos.add(vo);
		}

		return vos;
	}
	
	@Override
	public void saveOrUpdate(PfbxBonusApply pfbxBonusApply){
		((IPfbxBonusApplyDAO) dao).saveOrUpdate(pfbxBonusApply);
	}

	@Override
	public List<PfbxBonusApply> findNotDoneFailOrder(String pfbId) {

		List<PfbxBonusApply> pfbxBonusApplyList = ((IPfbxBonusApplyDAO) dao).findNotDoneFailOrder(pfbId);

		if (pfbxBonusApplyList.isEmpty()) {
			return null;
		} else {
			return pfbxBonusApplyList;
		}
	}

	@Override
	public List<PfbxBonusApply> findAllDoneOrder(String pfbId) {

		List<PfbxBonusApply> pfbxBonusApplyList = ((IPfbxBonusApplyDAO) dao).findAllDoneOrder(pfbId);

		if (pfbxBonusApplyList.isEmpty()) {
			return null;
		} else {
			return pfbxBonusApplyList;
		}
	}

	@Override
	public PfbxBonusApply findLastDoneOrder(String pfbId) {

		List<PfbxBonusApply> pfbxBonusApplyList = ((IPfbxBonusApplyDAO) dao).findAllDoneOrder(pfbId);

		if (pfbxBonusApplyList.isEmpty()) {
			return null;
		} else {
			return pfbxBonusApplyList.get(0);
		}
	}

	@Override
	public PfbxBonusApply findByApplyId(String pfbId, String applyId) {
		PfbxBonusApply pfbxBonusApply = ((IPfbxBonusApplyDAO) dao).findByApplyId(pfbId, applyId);
		return pfbxBonusApply;
	}

	@Override
	public List<PfbxBonusApply> findApplyByInvoiceCheckStatus(String customerInfoId, String status,String orderApplyId) {
		List<PfbxBonusApply> resultList = ((IPfbxBonusApplyDAO)dao).findApplyByInvoiceCheckStatus(customerInfoId,status,orderApplyId);
		return resultList;
	}
	
	@Override
	public List<PfbxBonusApply> findOldDetalByInvoiceCheckStatus(String customerInfoId, String invoiceCheckStatus, Integer bankId, Integer personalId) {
		List<PfbxBonusApply> resultList = ((IPfbxBonusApplyDAO)dao).findOldDetalByInvoiceCheckStatus(customerInfoId, invoiceCheckStatus, bankId, personalId);
		return resultList;
	}

	@Override
	public List<PfbxBonusApply> findApplyOrderByFailStatus() {
		List<PfbxBonusApply> resultList = ((IPfbxBonusApplyDAO)dao).findApplyOrderByFailStatus();
		return resultList;
	}

	@Override
	public List<PfbxBonusApply> findApplyOrderByInvoiceStatus(EnumPfbApplyInvoiceStatus enumPfbApplyInvoiceStatus) {
		List<PfbxBonusApply> pfbxBonusApplyList = ((IPfbxBonusApplyDAO) dao).findApplyOrderByInvoiceStatus(enumPfbApplyInvoiceStatus);

		if (pfbxBonusApplyList.isEmpty()) {
			return null;
		} else {
			return pfbxBonusApplyList;
		}
	}

	@Override
	public List<PfbxBonusApply> findApplyOrderByApplyStatus(EnumPfbApplyStatus enumPfbApplyStatus) {
		List<PfbxBonusApply> pfbxBonusApplyList = ((IPfbxBonusApplyDAO) dao).findApplyOrderByApplyStatus(enumPfbApplyStatus);

		if (pfbxBonusApplyList.isEmpty()) {
			return null;
		} else {
			return pfbxBonusApplyList;
		}
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
		List<PfbxBonusApply> pfbxBonusApplyList = ((IPfbxBonusApplyDAO) dao).findApplyOrderByStatusAndUpdateDate(enumPfbApplyStatus, startDate, endDate, createDate);

		if (pfbxBonusApplyList.isEmpty()) {
			return null;
		} else {
			return pfbxBonusApplyList;
		}
	}
	
	public void setCheckStatusFactory(CheckStatusFactory checkStatusFactory) {
		this.checkStatusFactory = checkStatusFactory;
	}

}
