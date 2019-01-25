package com.pchome.akbadm.struts2.action.bonus;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.rmi.bonus.IBonusProvider;
import com.pchome.soft.depot.utils.PdfUtil;
import com.pchome.soft.util.DateValueUtil;

public class DownloadBonusBillAction extends BaseCookieAction{

	private static final String FILE_TYPE = ".pdf";
	private static final String PDF_FTL = "bonus.detail.pdf.ftl";				// PDF ftl 檔案名稱
	
	private IBonusProvider bonusProvider;
	private PdfUtil pdfUtil;
	private String ftlPath;									// 檔案範本路徑
	private String commandPath;								// wkhtmltopdf 路徑	
	private String tempPath;								// 暫存檔案路徑
	
	private String downloadPfdCustomerInfoId;
	private int downloadYear;												
	private int downloadMonth;
	private String downloadPayType;
	
	private String downloadFileName;								// 下載檔案名稱
	private InputStream downloadFileStream;							// 下載檔案
	
	public String downloadBonusBillAction() throws Exception{
		
		log.info(" downloadPfdCustomerInfoId: "+downloadPfdCustomerInfoId);
		log.info(" downloadYear: "+downloadYear);
		log.info(" downloadMonth: "+downloadMonth);
		log.info(" downloadPayType: "+downloadPayType);
		log.info(" ftlPath: "+ftlPath);
		log.info(" commandPath: "+commandPath);
		log.info(" tempPath: "+tempPath);
		
		if(StringUtils.isBlank(tempPath)){
			// 網頁基本路徑
			tempPath= request.getSession().getServletContext().getRealPath("/");
		}
		
		Map<String,Object> map = null;
		
		if(downloadPayType.equals(EnumPfdAccountPayType.ADVANCE.getPayType())){
			
			map = bonusProvider.bonusDetailBillMap(downloadPfdCustomerInfoId, EnumPfdAccountPayType.ADVANCE, downloadYear, downloadMonth);
			
		}
		else{
			map = bonusProvider.bonusDetailBillMap(downloadPfdCustomerInfoId, EnumPfdAccountPayType.LATER, downloadYear, downloadMonth);
			
		}
		
		log.info("downloadBonusBillAction map: "+map);
		
		downloadFileStream = pdfUtil.htmlConvertPdf(map, ftlPath, PDF_FTL, commandPath, tempPath);
		
		StringBuffer fileName = new StringBuffer();
		
		fileName.append(downloadYear).append("年");
		fileName.append(downloadMonth).append("月");
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
	

	public void setBonusProvider(IBonusProvider bonusProvider) {
		this.bonusProvider = bonusProvider;
	}

	public void setPdfUtil(PdfUtil pdfUtil) {
		this.pdfUtil = pdfUtil;
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

	public void setDownloadPfdCustomerInfoId(String downloadPfdCustomerInfoId) {
		this.downloadPfdCustomerInfoId = downloadPfdCustomerInfoId;
	}

	public void setDownloadYear(int downloadYear) {
		this.downloadYear = downloadYear;
	}

	public void setDownloadMonth(int downloadMonth) {
		this.downloadMonth = downloadMonth;
	}

	public void setDownloadPayType(String downloadPayType) {
		this.downloadPayType = downloadPayType;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public InputStream getDownloadFileStream() {
		return downloadFileStream;
	}
	
	
}
