package com.pchome.akbadm.struts2.ajax.pfbx.bonus;

import com.google.gson.Gson;
import com.pchome.akbadm.db.pojo.PfbxBonusSetSpecial;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusSetSpecialService;
import com.pchome.akbadm.db.service.pfbx.bonus.PfbxBonusBillService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.rmi.bonus.PfbxBonusVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PfbBonusSetMaintainAjax extends BaseCookieAction
{

	private Gson gson;

	private String keyword; // 關鍵字查詢
	private String category; // 帳戶類型
	private String status; // 分潤狀態
	private List<PfbxBonusVo> pfbxBonusVo;

	private PfbxBonusBillService pfbxBonusBillService;
	private IPfbxBonusSetSpecialService pfbxBonusSetSpecialService;
	
	public String searchPfbBonusSetAjax() throws ParseException
	{
		log.info("in PfbBonusSetMaintainAjax");
		
		pfbxBonusVo = pfbxBonusBillService.getPfbxBonusVoList(keyword, category, status);
		
		//取得優惠
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(new Date());
		Date nowDate = sdf.parse(today);
		Map<String, Object> pfbxBonusSetSpecialMap = pfbxBonusSetSpecialService.getPfbxBonusSetSpecialNow(nowDate);
		
		for(PfbxBonusVo data:pfbxBonusVo){
			if(pfbxBonusSetSpecialMap.get(data.getPfbxCustomerInfoId()) != null){
				PfbxBonusSetSpecial pfbxBonusSetSpecial =(PfbxBonusSetSpecial) pfbxBonusSetSpecialMap.get(data.getPfbxCustomerInfoId());
				float totalPercent = pfbxBonusSetSpecial.getPfbPercent() + pfbxBonusSetSpecial.getPchomeChargePercent();
				data.setPfbxCustomerBonusPercent((int) pfbxBonusSetSpecial.getPfbPercent());
				data.setPfbxCustomerBonusPchomeChargePercent((int) pfbxBonusSetSpecial.getPchomeChargePercent());
				data.setPfbxCustomerBonusTotalPercent((int) totalPercent);
			}
		}
		
		return SUCCESS;
	}

	public void setGson(Gson gson)
	{
		this.gson = gson;
	}

	public void setKeyword(String keyword)
	{
		this.keyword = keyword;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public List<PfbxBonusVo> getPfbxBonusVo()
	{
		return pfbxBonusVo;
	}

	public void setPfbxBonusBillService(PfbxBonusBillService pfbxBonusBillService)
	{
		this.pfbxBonusBillService = pfbxBonusBillService;
	}

	public void setPfbxBonusSetSpecialService(
			IPfbxBonusSetSpecialService pfbxBonusSetSpecialService) {
		this.pfbxBonusSetSpecialService = pfbxBonusSetSpecialService;
	}

}
