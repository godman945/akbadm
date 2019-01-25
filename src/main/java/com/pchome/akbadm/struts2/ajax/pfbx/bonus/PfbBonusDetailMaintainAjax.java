package com.pchome.akbadm.struts2.ajax.pfbx.bonus;

import com.pchome.akbadm.db.service.pfbx.bonus.PfbxBonusBillService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.rmi.bonus.PfbxBonusVo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class PfbBonusDetailMaintainAjax extends BaseCookieAction
{
	private String keyword; // 關鍵字查詢
	private String category; // 帳戶類型
	private String status; // 分潤狀態
	
	private List<PfbxBonusVo> pfbxBonusVo;
	private PfbxBonusVo totalVO;		//總計

	private PfbxBonusBillService pfbxBonusBillService;

	private String downloadFlag = "";//download report 旗標
	private InputStream downloadFileStream;//下載報表的 input stream
	private String downloadFileName;//下載顯示名
	
	public String searchPfbBonusDetailAjax() throws Exception 
	{
		log.info("in PfbBonusSetMaintainAjax");

		pfbxBonusVo = pfbxBonusBillService.getPfbxBonusVoList(keyword, category, status);
	
		totalVO = new PfbxBonusVo();
		int totalSize = 0;
		float totalPayBonusSum = 0;
		float applyBonus = 0;
		float waitBonus = 0;
		
		if(!pfbxBonusVo.isEmpty()){
			totalSize = pfbxBonusVo.size();
			for(PfbxBonusVo data:pfbxBonusVo){
				totalPayBonusSum += data.getTotalPayBonus();
				applyBonus += data.getApplyBonus();
				waitBonus += data.getWaitBonus();
			}
		}
		
		totalVO.setTotalSize(totalSize);
		totalVO.setTotalPayBonus(totalPayBonusSum);
		totalVO.setApplyBonus(applyBonus);
		totalVO.setWaitBonus(waitBonus);
		
		if(downloadFlag.trim().equals("yes")){
			log.info("makeDownloadReportData");
			makeDownloadReportData();
		}
		
		return SUCCESS;
	}
	
	//下載報表
	private void makeDownloadReportData() throws Exception {
		SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMddhhmmss");
    	String filename="聯播網累計收益報表_" + dformat.format(new Date()) + ".csv";
    	DecimalFormat df = new DecimalFormat("###,###,###,###");
    	String[] tableHeadArray = {"帳戶編號","帳戶名稱","帳戶類型","統一編號","會員帳號","分潤狀態","已付金額","請款金額","代結金額"};
		
    	StringBuffer content=new StringBuffer();
    	
    	content.append("報表名稱:,累計收益");
		content.append("\n\n");
		
		if(StringUtils.isNotBlank(keyword)){
			content.append("帳戶編號/帳戶名稱/會員帳號:," + keyword);
			content.append("\n\n");
		}
		
		content.append("帳戶類型:,");
		if(StringUtils.isNotBlank(category)){
			if("1".equals(category)){
				content.append("個人戶");
			} else {
				content.append("公司戶");
			}
		} else {
			content.append("所有帳戶類型");
		}
		content.append("\n\n");
		
		content.append("分潤狀態:,");
		if(StringUtils.isNotBlank(status)){
			if("1".equals(status)){
				content.append("開啟");
			} else {
				content.append("關閉");
			}
		} else {
			content.append("所有分潤狀態");
		}
		content.append("\n\n");
		
		for(String s:tableHeadArray){
			content.append("\"" + s + "\"");
			content.append(",");
		}
		content.append("\n");
		
		for (int i=0; i<pfbxBonusVo.size(); i++) {
			PfbxBonusVo data = pfbxBonusVo.get(i);

			content.append("\"" + data.getPfbxCustomerInfoId() + "\",");
			content.append("\"" + data.getPfbxCustomerInfoName() + "\",");
			content.append("\"" + data.getPfbxCustomerCategoryName() + "\",");
			if(data.getPfbxCustomerTaxId() != null){
				content.append("\"" + data.getPfbxCustomerTaxId() + "\",");
			} else {
				content.append(",");
			}
			content.append("\"" + data.getPfbxCustomerMemberId() + "\",");
			content.append("\"" + data.getStatusName() + "\",");
			content.append("\"" + df.format(data.getTotalPayBonus()) + "\",");
			content.append("\"" + df.format(data.getApplyBonus()) + "\",");
			content.append("\"" + df.format(data.getWaitBonus()) + "\"");
			content.append("\n");
		}
		content.append("\n");
		//總計
		content.append("總計,");
		content.append("\"筆數：" + totalVO.getTotalSize() + "\",");
		content.append(",");
		content.append(",");
		content.append(",");
		content.append(",");
		content.append("\"" + df.format(totalVO.getTotalPayBonus()) + "\",");
		content.append("\"" + df.format(totalVO.getApplyBonus()) + "\",");
		content.append("\"" + df.format(totalVO.getWaitBonus()) + "\"");
		content.append("\n");
		
		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			downloadFileName = new String(filename.getBytes("UTF-8"), "ISO8859-1");
		} else {
			downloadFileName = URLEncoder.encode(filename, "UTF-8");			
		}

		downloadFileStream = new ByteArrayInputStream(content.toString().getBytes("big5"));
	}
	
	

	public void setPfbxBonusBillService(PfbxBonusBillService pfbxBonusBillService)
	{
		this.pfbxBonusBillService = pfbxBonusBillService;
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

	public void setDownloadFlag(String downloadFlag) {
		this.downloadFlag = downloadFlag;
	}

	public InputStream getDownloadFileStream() {
		return downloadFileStream;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public PfbxBonusVo getTotalVO() {
		return totalVO;
	}
	
}
