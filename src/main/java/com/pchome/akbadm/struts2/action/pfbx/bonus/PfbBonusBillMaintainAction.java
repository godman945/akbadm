package com.pchome.akbadm.struts2.action.pfbx.bonus;

import com.pchome.akbadm.db.vo.pfbx.bonus.PfbxBonusBillVo;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.rmi.bonus.IPfbBonusProvider;
import com.pchome.rmi.bonus.PfbxBonusVo;
import com.pchome.soft.depot.utils.PdfUtil;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class PfbBonusBillMaintainAction extends BaseCookieAction{


	private IPfbBonusProvider pfbBonusProvider;
	private PdfUtil pdfUtil;
	
	private String pfbId;				// pfb 帳戶編號
	private String strYear;				// 累計啟始年
	private String strMonth;			// 累計啟始月
	private String endYear;				// 累計結束年
	private String endMonth;			// 累計結束月
	private String payDate;				// 付款日期
	private PfbxBonusVo pfbxBonusVo;	// pfb 帳戶資料
	private List<PfbxBonusBillVo> pfbxBonusBillVos;		// 帳單明細
	
	private String ftlPath;								// ftl 路徑
	private String ftlName;								// ftl 名稱
	private String commandPath;							// wkhtmltopdf 路徑	
	private String tempPath;							// 暫存檔案路徑
	private String downloadFileName;					// 下載檔案名稱
	private InputStream downloadFileStream;				// 下載檔案
	
	public String pfbBonusBillAction() {
		
		log.info(">> pfbId: "+pfbId);
		log.info(">> strYear: "+strYear);
		log.info(">> strMonth: "+strMonth);
		log.info(">> endYear: "+endYear);
		log.info(">> endMonth: "+endMonth);
		
//		pfbxBonusVo = pfbxBonusService.findPfbxBonusVo(pfbId);	
//		
//		pfbxBonusBillVos = pfbxBonusTransService.findPfbxBonusBill(pfbId, strYear, strMonth, endYear, endMonth);
		
		return SUCCESS;
	}
	
	public String downloadPfbBonusBillAction() throws Exception{
	
		String fileName = pfbBonusProvider.downloadPfbBonusFileName(payDate, pfbId);
		
		// 下載檔案編碼
		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			downloadFileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
		}else{
			downloadFileName = URLEncoder.encode(fileName, "UTF-8");
		}	
		
		Map<String,Object> map = pfbBonusProvider.downloadPfbBonusMap(pfbId, strYear, strMonth, endYear, endMonth, payDate);
		
		downloadFileStream = pdfUtil.htmlConvertPdf(map, ftlPath, ftlName, commandPath, tempPath);
		
		return SUCCESS;
	}

	public void setPfbBonusProvider(IPfbBonusProvider pfbBonusProvider) {
		this.pfbBonusProvider = pfbBonusProvider;
	}

	public void setPdfUtil(PdfUtil pdfUtil) {
		this.pdfUtil = pdfUtil;
	}
	
	public void setPfbId(String pfbId) {
		this.pfbId = pfbId;
	}

	public String getStrYear() {
		return strYear;
	}

	public void setStrYear(String strYear) {
		this.strYear = strYear;
	}

	public String getStrMonth() {
		return strMonth;
	}

	public void setStrMonth(String strMonth) {
		this.strMonth = strMonth;
	}

	public String getEndYear() {
		return endYear;
	}

	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}

	public String getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(String endMonth) {
		this.endMonth = endMonth;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public PfbxBonusVo getPfbxBonusVo() {
		return pfbxBonusVo;
	}

	public List<PfbxBonusBillVo> getPfbxBonusBillVos() {
		return pfbxBonusBillVos;
	}

	public void setCommandPath(String commandPath) {
		this.commandPath = commandPath;
	}

	public void setFtlPath(String ftlPath) {
		this.ftlPath = ftlPath;
	}

	public void setFtlName(String ftlName) {
		this.ftlName = ftlName;
	}

	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public InputStream getDownloadFileStream() {
		return downloadFileStream;
	}
}
