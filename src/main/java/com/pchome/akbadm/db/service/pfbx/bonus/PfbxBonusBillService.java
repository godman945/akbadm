package com.pchome.akbadm.db.service.pfbx.bonus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.pfbx.account.IPfbxCustomerInfoDAO;
import com.pchome.akbadm.db.dao.pfbx.bonus.IPfbxBonusBillDAO;
import com.pchome.akbadm.db.dao.pfbx.bonus.IPfbxBonusSetDAO;
import com.pchome.akbadm.db.pojo.PfbxBonusBill;
import com.pchome.akbadm.db.pojo.PfbxBonusSet;
import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.pojo.PfbxPersonal;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.enumerate.pfbx.account.EnumPfbxAccountCategory;
import com.pchome.enumerate.pfbx.bonus.EnumPfbxBonusSet;
import com.pchome.rmi.bonus.PfbxBonusVo;
import com.pchome.soft.util.DateValueUtil;

public class PfbxBonusBillService extends BaseService<PfbxBonusBill, Integer> implements IPfbxBonusBillService
{
	IPfbxBonusSetDAO PfbxBonusSetDAO;
	IPfbxCustomerInfoDAO pfbxCustomerInfoDAO;

	public List<PfbxBonusVo> getPfbxBonusVoList(String keyword, String category, String status)
	{
		log.info("PfbxBonusBillService - getPfbxBonusVoList");

		String today = DateValueUtil.getInstance().getTodayDateTime().substring(0, 8);
		List<PfbxBonusVo> list = new ArrayList<PfbxBonusVo>();

		List<PfbxBonusBill> billList = ((IPfbxBonusBillDAO) dao).listByKeyWord(keyword, category, status);
		List<PfbxBonusSet> setList = PfbxBonusSetDAO.getListOrderByStartDate();

		PfbxBonusVo vo = null;
		for (PfbxBonusBill bill : billList)
		{
			// 處理status 狀態說明
			String chStatus = "";
			for (EnumPfbxBonusSet eStatus : EnumPfbxBonusSet.values())
			{
				if (StringUtils.equals(eStatus.getStatus(), bill.getPfbBonusSetStatus()))
				{
					chStatus = eStatus.getChName().trim();
					break;
				}
			}

			// 處理帳號類型
			String chCategory = "";
			for (EnumPfbxAccountCategory eCategory : EnumPfbxAccountCategory.values())
			{
				PfbxCustomerInfo pfbxCustomerInfo = pfbxCustomerInfoDAO.get(bill.getPfbId());
				if (StringUtils.equals(eCategory.getCategory(), pfbxCustomerInfo.getCategory())){
					chCategory = eCategory.getChName().trim();
					break;
				}
			}

			// 擷取pfbxbonusSet中最新一筆(但在有效期內)%數
			float pchomePercent = 0;
			float customerPercent = 0;
			float totalPercent = 0;
			for (PfbxBonusSet set : setList)
			{
				if (StringUtils.equals(set.getPfbId(), bill.getPfbId()))
				{
					String setDate = set.getStartDate().toString().replaceAll("-", "");

					if (!(setDate.compareTo(today) > 0))
					{
						pchomePercent = set.getPchomeChargePercent();
						customerPercent = set.getPfbPercent();
						totalPercent = pchomePercent + customerPercent;
						break;
					}
				}
			}
			
//			20150617set by Nico
//			已付金額  total_apply_money
//			請款金額  apply_money
//			待結金額  cumulative_money
//			總金額 	  bill_remain

			vo = new PfbxBonusVo();
			vo.setPfbxCustomerInfoId(bill.getPfbId());	// 帳戶編號
			vo.setPfbxCustomerCategoryName(chCategory);									// 帳戶類型
			PfbxCustomerInfo pfbxCustomerInfo = pfbxCustomerInfoDAO.get(bill.getPfbId());
			if (StringUtils.equals(EnumPfbxAccountCategory.PERSONAL.getCategory(), pfbxCustomerInfo.getCategory())){
				vo.setPfbxCustomerInfoName(pfbxCustomerInfo.getContactName());
			} else {
				vo.setPfbxCustomerInfoName(pfbxCustomerInfo.getCompanyName());
			}
			
			String idCard = "";
			if(EnumPfbxAccountCategory.PERSONAL.getCategory().equals(pfbxCustomerInfo.getCategory())){
				Set<PfbxPersonal> pfbxPersonalList = pfbxCustomerInfo.getPfbxPersonals();
				for(PfbxPersonal pfbxPersonal:pfbxPersonalList){
					if("Y".equals(pfbxPersonal.getMainUse()) && "0".equals(pfbxPersonal.getDeleteFlag())){
						vo.setPfbxCustomerTaxId(pfbxPersonal.getIdCard());				// 身分證字號
						idCard = pfbxPersonal.getIdCard();
					}
				}
			} else {
				vo.setPfbxCustomerTaxId(pfbxCustomerInfo.getTaxId());			// 統一編號
			}
			vo.setPfbxCustomerMemberId(pfbxCustomerInfo.getMemberId());		// 會員編號
			vo.setStatusName(chStatus);													// 分潤狀態
			vo.setPfbxCustomerBonusPercent((int) customerPercent);						// pfb 分潤%數
			vo.setTotalPayBonus(bill.getTotalApplyMoney());								// pfb 累計已付金額
			vo.setApplyBonus(bill.getApplyMoney());										// pfb 請款金額
			vo.setWaitBonus(bill.getBillRemain());									// pfb 待結金額
			vo.setTotalBonus(bill.getBillRemain());										// pfb 總金額
			vo.setLastPayBonus(bill.getLastApplyMoney());														// 上次已付
			vo.setPfbxCustomerBonusPchomeChargePercent((int) pchomePercent); 			// pfb PChome分潤%數
			vo.setPfbxCustomerBonusTotalPercent((int) totalPercent); 					// pfb 分潤總%數
			
			if (StringUtils.isNotBlank(keyword)){	//關鍵字查詢(因身分證字號無法在hql做，所以用程式判斷)
				if(pfbxCustomerInfo.getCustomerInfoId().indexOf(keyword) > -1 ||
						//pfbxCustomerInfo.getTaxId().indexOf(keyword) > -1 ||
						pfbxCustomerInfo.getMemberId().indexOf(keyword) > -1 ||
						//idCard.indexOf(keyword)  > -1){
						vo.getPfbxCustomerInfoName().indexOf(keyword) > -1){ //更改為帳戶名稱
					list.add(vo);
				}
			} else {
				list.add(vo);
			}
			
		}

		return list;
	}

	public PfbxBonusVo findPfbxBonusVo(String pfbId)
	{
		log.info("PfbxBonusBillService - findPfbxBonusVo");

		String today = DateValueUtil.getInstance().getTodayDateTime().substring(0, 8);
		List<PfbxBonusVo> list = new ArrayList<PfbxBonusVo>();

		// 取得set 設定 & bill by pfbId
		List<PfbxBonusSet> setList = PfbxBonusSetDAO.getListByPfbIdOrderSDate(pfbId);
		List<PfbxBonusBill> billList = ((IPfbxBonusBillDAO) dao).listByPfbId(pfbId);
		PfbxBonusBill bill = new PfbxBonusBill();
		if (billList.size() > 0)
		{
			bill = billList.get(0);
		}
		PfbxCustomerInfo pfbxCustomerInfo = pfbxCustomerInfoDAO.get(bill.getPfbId());
		// 處理帳號類型
		String chCategory = "";
		for (EnumPfbxAccountCategory eCategory : EnumPfbxAccountCategory.values())
		{
			
			if (StringUtils.equals(eCategory.getCategory(), pfbxCustomerInfo.getCategory()))
			{
				chCategory = eCategory.getChName().trim();
				break;
			}
		}

		// 擷取pfbxbonusSet中最新一筆(但在有效期內)%數
		float pchomePercent = 0;
		float customerPercent = 0;
		float totalPercent = 0;
		for (PfbxBonusSet set : setList)
		{
			String setDate = set.getStartDate().toString().replaceAll("-", "");

			if (!(setDate.compareTo(today) > 0))
			{
				pchomePercent = set.getPchomeChargePercent();
				customerPercent = set.getPfbPercent();
				totalPercent = pchomePercent + customerPercent;
				break;
			}
		}

		// 組裝Vo
		PfbxBonusVo vo = new PfbxBonusVo();
		
		vo.setPfbxCustomerInfoId(pfbxCustomerInfo.getCustomerInfoId());
		if (StringUtils.equals(EnumPfbxAccountCategory.PERSONAL.getCategory(), pfbxCustomerInfo.getCategory())){
			vo.setPfbxCustomerInfoName(pfbxCustomerInfo.getContactName());
		} else {
			vo.setPfbxCustomerInfoName(pfbxCustomerInfo.getCompanyName());
		}
		vo.setPfbxCustomerInfoContactName(pfbxCustomerInfo.getContactName());
		vo.setPfbxCustomerCategoryName(chCategory);
		if(EnumPfbxAccountCategory.PERSONAL.getCategory().equals(pfbxCustomerInfo.getCategory())){
			Set<PfbxPersonal> pfbxPersonalList = pfbxCustomerInfo.getPfbxPersonals();
			for(PfbxPersonal pfbxPersonal:pfbxPersonalList){
				if("Y".equals(pfbxPersonal.getMainUse()) && "0".equals(pfbxPersonal.getDeleteFlag())){
					vo.setPfbxCustomerTaxId(pfbxPersonal.getIdCard());				// 身分證字號
				}
			}
		} else {
			vo.setPfbxCustomerTaxId(pfbxCustomerInfo.getTaxId());			// 統一編號
		}
		vo.setPfbxCustomerMemberId(pfbxCustomerInfo.getMemberId());
		vo.setStatusId(bill.getPfbBonusSetStatus());
		vo.setPfbxCustomerBonusPercent((int)customerPercent);
		vo.setTotalPayBonus(bill.getTotalApplyMoney());								// pfb 累計已付金額
		vo.setApplyBonus(bill.getApplyMoney());										// pfb 請款金額
		vo.setWaitBonus(bill.getBillRemain());									// pfb 待結金額
		vo.setTotalBonus(bill.getBillRemain());										// pfb 總金額
		vo.setLastPayBonus(bill.getLastApplyMoney());														// 上次已付
		vo.setPfbxCustomerBonusPchomeChargePercent((int) pchomePercent); 			// pfb PChome分潤%數
		vo.setPfbxCustomerBonusTotalPercent((int) totalPercent); 					// pfb 分潤總%數
		vo.setApplyMinLimit(bill.getMinLimit());									// pfb 付款門檻

		return vo;
	}
	
	public PfbxBonusBill getpfbxBonusBillByPfbId(String pfbId)
	{
		PfbxBonusBill bill = new PfbxBonusBill();
		List<PfbxBonusBill> billList = ((IPfbxBonusBillDAO) dao).listByPfbId(pfbId);
		if(billList.size() > 0)
		{
			bill = billList.get(0);
		}
		
		return bill;
	}
	
	
	public void setPfbxCustomerInfoDAO(IPfbxCustomerInfoDAO pfbxCustomerInfoDAO)
	{
		this.pfbxCustomerInfoDAO = pfbxCustomerInfoDAO;
	}

	public void setPfbxBonusSetDAO(IPfbxBonusSetDAO pfbxBonusSetDAO)
	{
		PfbxBonusSetDAO = pfbxBonusSetDAO;
	}

	public PfbxBonusBill findPfbxBonusBill(String pfbId) {
		
		List<PfbxBonusBill> pfbxs = ((IPfbxBonusBillDAO)dao).findPfbxBonusBills(pfbId);
		
		if(pfbxs.isEmpty()){
			return null;
		}else{
			return pfbxs.get(0);
		}
			
	}
}
