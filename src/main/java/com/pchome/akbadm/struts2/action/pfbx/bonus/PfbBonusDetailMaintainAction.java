package com.pchome.akbadm.struts2.action.pfbx.bonus;

import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusBillService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusTransDetailService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.pfbx.account.EnumPfbxAccountCategory;
import com.pchome.enumerate.pfbx.bonus.EnumPfbxBonusSet;
import com.pchome.rmi.bonus.PfbxBonusTransVo;
import com.pchome.rmi.bonus.PfbxBonusVo;

import java.util.List;

public class PfbBonusDetailMaintainAction extends BaseCookieAction
{

	private EnumPfbxAccountCategory[] enumAccountCategory = null;
	private EnumPfbxBonusSet[] enumBonusSet = null;

	private String pfbId; // pfb 帳戶編號
	private PfbxBonusVo pfbxBonusVo; // pfb 帳戶資料
	private List<PfbxBonusTransVo> pfbxBonusTransVos; // pfb 交易資料

	private IPfbxBonusBillService pfbxBonusBillService;
	private IPfbxBonusTransDetailService pfbxBonusTransDetailService;

	public String execute()
	{
		// 下拉選單
		enumAccountCategory = EnumPfbxAccountCategory.values();
		enumBonusSet = EnumPfbxBonusSet.values();

		return SUCCESS;
	}

	public String pfbBonusTransDetailAction()
	{
		log.info(">> pfbId: " + pfbId);

		// 帳戶基本資料Vo
		pfbxBonusVo = pfbxBonusBillService.findPfbxBonusVo(pfbId);
		
		//帳戶交易明細
		pfbxBonusTransVos = pfbxBonusTransDetailService.getPfbxBonusTransVo_List(pfbId);

		return SUCCESS;
	}

	public EnumPfbxAccountCategory[] getEnumAccountCategory()
	{
		return enumAccountCategory;
	}

	public EnumPfbxBonusSet[] getEnumBonusSet()
	{
		return enumBonusSet;
	}

	public void setPfbId(String pfbId)
	{
		this.pfbId = pfbId;
	}

	public PfbxBonusVo getPfbxBonusVo()
	{
		return pfbxBonusVo;
	}

	public List<PfbxBonusTransVo> getPfbxBonusTransVos()
	{
		return pfbxBonusTransVos;
	}

	public void setPfbxBonusBillService(IPfbxBonusBillService pfbxBonusBillService)
	{
		this.pfbxBonusBillService = pfbxBonusBillService;
	}

	public void setPfbxBonusTransDetailService(IPfbxBonusTransDetailService pfbxBonusTransDetailService)
	{
		this.pfbxBonusTransDetailService = pfbxBonusTransDetailService;
	}

	
}
