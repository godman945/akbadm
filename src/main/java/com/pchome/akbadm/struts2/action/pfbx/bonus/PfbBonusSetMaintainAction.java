package com.pchome.akbadm.struts2.action.pfbx.bonus;

import com.pchome.akbadm.db.pojo.PfbxBonusBill;
import com.pchome.akbadm.db.pojo.PfbxBonusSet;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusBillService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusSetService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusSetSpecialService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.pfbx.account.EnumPfbxAccountCategory;
import com.pchome.enumerate.pfbx.bonus.EnumPfbxBonusSet;
import com.pchome.rmi.bonus.PfbxBonusSetSpecialVO;
import com.pchome.rmi.bonus.PfbxBonusVo;
import com.pchome.soft.util.DateValueUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public class PfbBonusSetMaintainAction extends BaseCookieAction
{

	private IPfbxBonusSetService pfbxBonusSetService;
	private IPfbxBonusBillService pfbxBonusBillService;
	private IPfbxBonusSetSpecialService pfbxBonusSetSpecialService;

	private EnumPfbxAccountCategory[] enumAccountCategory = null;
	private EnumPfbxBonusSet[] enumBonusSet = null;

	private PfbxBonusVo pfbxBonusVo; // pfb 帳戶資料
	private String pfbId; // pfb 帳戶編號
	private String status; // 分潤狀態
	private int percent; // pfb 收益百分比
	
	private List<PfbxBonusSetSpecialVO> pfbxBonusSetSpecialList;

	public String execute()
	{

		enumAccountCategory = EnumPfbxAccountCategory.values();
		enumBonusSet = EnumPfbxBonusSet.values();

		return SUCCESS;
	}

	public String editPfbBonusSetAction()
	{
		log.info(">> pfbId: " + pfbId);

		// 導入set 設定
		enumBonusSet = EnumPfbxBonusSet.values();

		// 取得畫面Vo
		pfbxBonusVo = pfbxBonusBillService.findPfbxBonusVo(pfbId);

		pfbxBonusSetSpecialList = pfbxBonusSetSpecialService.findPfbxBonusSetSpecialList(pfbId);
		
		return SUCCESS;
	}

	@Transactional
	public String updatePfbBonusSetAction()
	{
		log.info(">> pfbId: " + pfbId);
		log.info(">> status: " + status);
		log.info(">> percent: " + percent);
		log.info(">> session:   " + super.getSession().get("session_user_id").toString());

		if (StringUtils.isNotBlank(pfbId) && StringUtils.isNotBlank(status) && percent >= 0)
		{
			Date today = DateValueUtil.getInstance().getNowDateTime();
			
			//修改bill
			PfbxBonusBill bill = pfbxBonusBillService.getpfbxBonusBillByPfbId(pfbId);
			bill.setPfbBonusSetStatus(status);
			
			//修改set 
			PfbxBonusSet set = new PfbxBonusSet();
			set.setPfbId(pfbId);
			set.setPchomeChargePercent(100 - percent);
			set.setPfbPercent(percent);
			set.setStartDate(today);
			set.setCreateDate(today);
			
			pfbxBonusBillService.saveOrUpdate(bill);
			pfbxBonusSetService.saveOrUpdate(set);
			
			// PfbxBonus pfbxBonus = pfbxBonusService.findPfbxBonus(pfbId);
			//
			// if(pfbxBonus != null){
			//
			// Date today = new Date();
			// boolean isChangePercent = false;
			// boolean isChangeStatus = false;
			//
			// if(pfbxBonus.getPfbxBonusSet().getCustomerPercent() != percent){
			// // 建立分潤設定
			// PfbxBonusSet bonusSet = new PfbxBonusSet();
			//
			// bonusSet.setCustomerPercent(percent);
			// bonusSet.setAuthor(super.getSession().get("session_user_id").toString());
			// bonusSet.setIp(super.request.getRemoteAddr());
			// bonusSet.setCreateDate(today);
			//
			// pfbxBonusSetService.saveOrUpdate(bonusSet);
			//
			// pfbxBonus.setPfbxBonusSet(bonusSet);
			//
			// isChangePercent = true;
			// }
			//
			// if(!pfbxBonus.getPfbxBonusSetStatus().equals(status) ){
			//
			// // 更新分潤狀態
			// pfbxBonus.setPfbxBonusSetStatus(status);
			//
			// pfbxBonus.setUpdateDate(today);
			//
			// isChangeStatus = true;
			// }
			//
			// if(isChangePercent || isChangeStatus){
			// // 更新分潤設定
			// pfbxBonusService.saveOrUpdate(pfbxBonus);
			// }
			//
			// }

		}

		return SUCCESS;
	}

	public void setPfbxBonusBillService(IPfbxBonusBillService pfbxBonusBillService)
	{
		this.pfbxBonusBillService = pfbxBonusBillService;
	}

	public void setPfbxBonusSetService(IPfbxBonusSetService pfbxBonusSetService)
	{
		this.pfbxBonusSetService = pfbxBonusSetService;
	}

	public void setPfbxBonusSetSpecialService(IPfbxBonusSetSpecialService pfbxBonusSetSpecialService) {
		this.pfbxBonusSetSpecialService = pfbxBonusSetSpecialService;
	}

	public EnumPfbxAccountCategory[] getEnumAccountCategory()
	{
		return enumAccountCategory;
	}

	public EnumPfbxBonusSet[] getEnumBonusSet()
	{
		return enumBonusSet;
	}

	public PfbxBonusVo getPfbxBonusVo()
	{
		return pfbxBonusVo;
	}

	public void setPfbId(String pfbId)
	{
		this.pfbId = pfbId;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public void setPercent(int percent)
	{
		this.percent = percent;
	}

	public List<PfbxBonusSetSpecialVO> getPfbxBonusSetSpecialList() {
		return pfbxBonusSetSpecialList;
	}

}
