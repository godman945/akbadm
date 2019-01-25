package com.pchome.akbadm.struts2.action.pfd.bonus;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Map;

import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.rmi.bonus.IPfdBonusProvider;
import com.pchome.soft.depot.utils.PdfUtil;
import com.pchome.soft.util.DateValueUtil;

public class BonusBillAction extends BaseCookieAction{

	private static final String FILE_TYPE = ".pdf";
	private static final String PDF_FTL = "bonus.detail.pdf.ftl";	// PDF ftl 檔案名稱
	private IPfdBonusProvider pfdBonusProvider;
	private PdfUtil pdfUtil;
	
	private String downloadFileName;								// 下載檔案名稱
	private InputStream downloadFileStream;							// 下載檔案
	
	private String pfdId;				// pfd 帳戶編號
	private int year;					// 年
	private int month;					// 月
	private String payType;				// 付款方式
	
	private String ftlPath;				// 檔案範本路徑
	private String commandPath;			// wkhtmltopdf 路徑	
	private String tempPath;			// 暫存檔案路徑
	
	public String downloadBonusBillAction() throws Exception{
		
		log.info(" pfdId: "+pfdId);
		log.info(" year: "+year);
		log.info(" month: "+month);
		log.info(" payType: "+payType);
		log.info(" ftlPath: "+ftlPath);
		log.info(" commandPath: "+commandPath);
		log.info(" tempPath: "+tempPath);
		
		Map<String,Object> pdfMap = pfdBonusProvider.pfdBonusBillMap(pfdId, year, month);
		
		log.info("ADM PDF map: "+pdfMap);
		
		if(pdfMap != null){			
			downloadFileStream = pdfUtil.htmlConvertPdf(pdfMap, ftlPath, PDF_FTL, commandPath, tempPath);
		}
		
		StringBuffer fileName = new StringBuffer();
		
		fileName.append(year).append("年");
		fileName.append(month).append("月");
		fileName.append("請款單").append("_");
		fileName.append(DateValueUtil.getInstance().getDateValue(DateValueUtil.TODAY, DateValueUtil.DBPATH)).append(FILE_TYPE); 
		
		// 下載檔案編碼
		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			downloadFileName = new String(fileName.toString().getBytes("UTF-8"), "ISO8859-1");
		}else{
			downloadFileName = URLEncoder.encode(fileName.toString(), "UTF-8");
		}	
		
		return SUCCESS;
	}

	public void setPfdBonusProvider(IPfdBonusProvider pfdBonusProvider) {
		this.pfdBonusProvider = pfdBonusProvider;
	}

	public void setPdfUtil(PdfUtil pdfUtil) {
		this.pdfUtil = pdfUtil;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public InputStream getDownloadFileStream() {
		return downloadFileStream;
	}

	public void setPfdId(String pfdId) {
		this.pfdId = pfdId;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public void setFtlPath(String ftlPath) {
		this.ftlPath = ftlPath;
	}

	public void setCommandPath(String commandPath) {
		this.commandPath = commandPath;
	}

	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}
	
	
	
}
