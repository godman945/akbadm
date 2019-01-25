package com.pchome.akbadm.db.service.pfbx.bonus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.pfbx.bonus.IPfbxBonusTransDetailDAO;
import com.pchome.akbadm.db.pojo.PfbxBonusTransDetail;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.rmi.bonus.PfbxBonusTransVo;

public class PfbxBonusTransDetailService extends BaseService<PfbxBonusTransDetail, Integer> implements IPfbxBonusTransDetailService
{
	public List<PfbxBonusTransVo> getPfbxBonusTransVo_List(String pfbId)
	{
		List<PfbxBonusTransVo> voList = new ArrayList<PfbxBonusTransVo>();
		
		List<PfbxBonusTransDetail> detailList = ((IPfbxBonusTransDetailDAO)dao).findByPfbId(pfbId);
		
		PfbxBonusTransVo vo = null;
		for(PfbxBonusTransDetail detail : detailList)
		{
			vo = new PfbxBonusTransVo();
			if(StringUtils.equals(detail.getTransItem(), "2")){
				vo.setTransDate(detail.getStartDate() + " - " + detail.getEndDate());
			} else {
				vo.setTransDate(detail.getStartDate().toString());
			}
			vo.setTransDesc(detail.getTransDesc());
			vo.setWaitBonus(detail.getTransIn());
			vo.setPayBonus(detail.getTransOut());
			vo.setRemain(detail.getTransRemain());
			
			voList.add(vo);
			
		}
		
		return voList;
	}

	public Integer deletePfbxBonusTransDetail(Date deleteDate) {
		return ((IPfbxBonusTransDetailDAO) dao).deletePfbxBonusTransDetail(deleteDate);
	}
	
	public PfbxBonusTransDetail findLastPfbxBonusTransDetail(String pfbId) {
		List<PfbxBonusTransDetail> list = ((IPfbxBonusTransDetailDAO) dao).findLastPfbxBonusTransDetail(pfbId);
		
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}

	@Override
	public List<PfbxBonusTransDetail> findApplyNullPfbxBonusTransDetail(String pfbId) {
         
		List<PfbxBonusTransDetail> list = ((IPfbxBonusTransDetailDAO) dao).findApplyNullPfbxBonusTransDetail(pfbId);
		
		if(list.isEmpty()){
			return null;
		}else{
			return list;
		}
	}

	@Override
	public List<PfbxBonusTransDetail> findListPfbxBonusTransDetail(String pfbId) {
		List<PfbxBonusTransDetail> list = ((IPfbxBonusTransDetailDAO) dao).findLastPfbxBonusTransDetail(pfbId);
		
		if(list.isEmpty()){
			return null;
		}else{
			return list;
		}
	}
	
	@Override
	public List<PfbxBonusTransDetail> findApplyNullPfbxBonusInvalidTransDetail(String pfbId) {
         
		List<PfbxBonusTransDetail> list = ((IPfbxBonusTransDetailDAO) dao).findApplyNullPfbxBonusInvalidTransDetail(pfbId);
		
		if(list.isEmpty()){
			return null;
		}else{
			return list;
		}
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
	public void deleteItemTransDetail(String customerInfoId, String startDate, String endDate, String transItem, String failApplyId) {
		((IPfbxBonusTransDetailDAO) dao).deletePfbxBonusTransDetail(customerInfoId, startDate, endDate, transItem, failApplyId);
	}

}
