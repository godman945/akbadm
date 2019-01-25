package com.pchome.akbadm.struts2.action.pfbx.bonus;

import com.pchome.akbadm.db.pojo.PfbxBonusSetSpecial;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusSetSpecialService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.rmi.bonus.PfbxBonusSetSpecialVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class pfbxBonusSetSpecialAction  extends BaseCookieAction{
	
	private IPfbxBonusSetSpecialService pfbxBonusSetSpecialService;
	private PfbxBonusSetSpecialVO pfbxBonusSetSpecialVO;
	private String message = "";
	private String id = "";
	private String specialName;
	private String pfbId;
	private String startDate;
	private String endDate;
	private String pfbPercent;
	private String deleteFlag;
	private String createDate;
	private String result;
	
	
	public String execute(){
		return SUCCESS;
	}
	
	public String addSpecial(){
		if(startDate.isEmpty()){
			message = "請輸入開始日期！";
			return INPUT;
		}
		
		if(endDate.isEmpty()){
			message = "請輸入結束日期！";
			return INPUT;
		}
		
		if(specialName.isEmpty()){
			message = "請輸入活動名稱！";
			return INPUT;
		}
		
		if(pfbPercent.isEmpty()){
			message = "請輸入客戶分潤！";
			return INPUT;
		}
		
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(pfbPercent);
		if(!isNum.matches()){
			message = "客戶分潤只能為數字！";
			return INPUT;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			PfbxBonusSetSpecial pfbxBonusSetSpecial = new PfbxBonusSetSpecial();
			pfbxBonusSetSpecial.setPfbId(pfbId);
			pfbxBonusSetSpecial.setSpecialName(specialName);
			pfbxBonusSetSpecial.setStartDate(sdf.parse(startDate));
			pfbxBonusSetSpecial.setEndDate(sdf.parse(endDate));
			pfbxBonusSetSpecial.setPfbPercent(Float.parseFloat(pfbPercent));
			pfbxBonusSetSpecial.setPchomeChargePercent(new Float(100) - Float.parseFloat(pfbPercent));
			pfbxBonusSetSpecial.setDeleteFlag(0);
			pfbxBonusSetSpecial.setUpdateDate(new Date());
			pfbxBonusSetSpecial.setCreateDate(new Date());
			
			pfbxBonusSetSpecialService.saveOrUpdate(pfbxBonusSetSpecial);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	public String updSpecialView(){
		PfbxBonusSetSpecial pfbxBonusSetSpecial = pfbxBonusSetSpecialService.getPfbxBonusSetSpecial(Integer.parseInt(id));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 24H:mm:ss");
		pfbxBonusSetSpecialVO = new PfbxBonusSetSpecialVO();
		pfbxBonusSetSpecialVO.setId(pfbxBonusSetSpecial.getId());
		pfbxBonusSetSpecialVO.setPfbId(pfbxBonusSetSpecial.getPfbId());
		pfbxBonusSetSpecialVO.setSpecialName(pfbxBonusSetSpecial.getSpecialName());
		pfbxBonusSetSpecialVO.setStartDate(sdf.format(pfbxBonusSetSpecial.getStartDate()));
		pfbxBonusSetSpecialVO.setEndDate(sdf.format(pfbxBonusSetSpecial.getEndDate()));
		pfbxBonusSetSpecialVO.setPfbPercent(pfbxBonusSetSpecial.getPfbPercent());
		pfbxBonusSetSpecialVO.setPchomeChargePercent(pfbxBonusSetSpecial.getPchomeChargePercent());
		pfbxBonusSetSpecialVO.setDeleteFlag(pfbxBonusSetSpecial.getDeleteFlag());
		pfbxBonusSetSpecialVO.setUpdateDate(sdf2.format(pfbxBonusSetSpecial.getUpdateDate()));
		pfbxBonusSetSpecialVO.setCreateDate(sdf2.format(pfbxBonusSetSpecial.getCreateDate()));
		
		return SUCCESS;
	}
	
	public String updSpecial(){
		if(startDate.isEmpty()){
			message = "請輸入開始日期！";
			return INPUT;
		}
		
		if(specialName.isEmpty()){
			message = "請輸入活動名稱！";
			return INPUT;
		}
		
		if(endDate.isEmpty()){
			message = "請輸入結束日期！";
			return INPUT;
		}
		
		if(pfbPercent.isEmpty()){
			message = "請輸入客戶分潤！";
			return INPUT;
		}
		
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(pfbPercent);
		if(!isNum.matches()){
			message = "客戶分潤只能為數字！";
			return INPUT;
		}
		
		PfbxBonusSetSpecial pfbxBonusSetSpecial = new PfbxBonusSetSpecial();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 24H:mm:ss");
		
		try {
			pfbxBonusSetSpecial.setId(Integer.parseInt(id));
			pfbxBonusSetSpecial.setPfbId(pfbId);
			pfbxBonusSetSpecial.setSpecialName(specialName);
			pfbxBonusSetSpecial.setStartDate(sdf.parse(startDate));
			pfbxBonusSetSpecial.setEndDate(sdf.parse(endDate));
			pfbxBonusSetSpecial.setPfbPercent(Float.parseFloat(pfbPercent));
			pfbxBonusSetSpecial.setPchomeChargePercent(new Float(100) - Float.parseFloat(pfbPercent));
			pfbxBonusSetSpecial.setDeleteFlag(Integer.parseInt(deleteFlag));
			pfbxBonusSetSpecial.setUpdateDate(new Date());
			pfbxBonusSetSpecial.setCreateDate(sdf2.parse((createDate)));
			
			pfbxBonusSetSpecialService.saveOrUpdate(pfbxBonusSetSpecial);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	public String delSpecial(){
		PfbxBonusSetSpecial pfbxBonusSetSpecial = pfbxBonusSetSpecialService.getPfbxBonusSetSpecial(Integer.parseInt(id));
		pfbId = pfbxBonusSetSpecial.getPfbId();
		pfbxBonusSetSpecial.setDeleteFlag(1);
		pfbxBonusSetSpecial.setUpdateDate(new Date());
		pfbxBonusSetSpecialService.saveOrUpdate(pfbxBonusSetSpecial);
		
		return SUCCESS;
	}
	
	//確認優惠日期區間有無重疊其他優惠
	public String chkSpecialDate() throws ParseException{
		result= "";
		log.info("------------------pfbId=" + pfbId);
		log.info("------------------startDate=" + startDate);
		log.info("------------------endDate=" + endDate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<PfbxBonusSetSpecial> list = pfbxBonusSetSpecialService.findPfbxBonusSetSpecialbyDate(pfbId, sdf.parse(startDate), sdf.parse(endDate));
		
		if(!list.isEmpty()){
			result = "與現有優惠的日期區間重疊，請更改日期區間!!";
		}
		log.info("---------------------result=" + result);
		return SUCCESS;
	}
	
	public void setPfbxBonusSetSpecialService(
			IPfbxBonusSetSpecialService pfbxBonusSetSpecialService) {
		this.pfbxBonusSetSpecialService = pfbxBonusSetSpecialService;
	}

	public PfbxBonusSetSpecialVO getPfbxBonusSetSpecialVO() {
		return pfbxBonusSetSpecialVO;
	}

	public void setPfbxBonusSetSpecialVO(PfbxBonusSetSpecialVO pfbxBonusSetSpecialVO) {
		this.pfbxBonusSetSpecialVO = pfbxBonusSetSpecialVO;
	}

	public String getPfbId() {
		return pfbId;
	}

	public void setPfbId(String pfbId) {
		this.pfbId = pfbId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getPfbPercent() {
		return pfbPercent;
	}

	public void setPfbPercent(String pfbPercent) {
		this.pfbPercent = pfbPercent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSpecialName() {
		return specialName;
	}

	public void setSpecialName(String specialName) {
		this.specialName = specialName;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public IPfbxBonusSetSpecialService getPfbxBonusSetSpecialService() {
		return pfbxBonusSetSpecialService;
	}
	
}
