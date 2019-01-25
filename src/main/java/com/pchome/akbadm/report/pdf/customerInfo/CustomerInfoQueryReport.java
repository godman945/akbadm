package com.pchome.akbadm.report.pdf.customerInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.service.customerInfo.PfpCustomerInfoService;
import com.pchome.akbadm.db.service.pfd.account.IPfdAccountService;
import com.pchome.akbadm.db.vo.CustomerInfoReportVO;
import com.pchome.enumerate.recognize.EnumOrderType;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

public class CustomerInfoQueryReport extends ACustomerInfoQueryTemplate {

	private PfpCustomerInfoService pfpCustomerInfoService;
	private IPfdAccountService pfdAccountService;
	
	@Override
	public void buildPdf(Document doc, List<String> queryCondition) throws Exception {

		//每頁資料筆數
		int pageMaxSize = 40;
		
		// 表格欄位寬度
		int[] colSize = {50, 80, 60, 100, 40, 50, 100, 100, 50};
		
		// 表格欄位名稱
		String[] colName = {"開通日期", "會員帳號", "帳戶編號", "帳戶名稱", "帳戶類別", "儲值金額", "經銷歸屬", "業務歸屬", "付款方式"};
		
		int tableWidth = 0;

		// 表格寬度
		for (int i=0; i<colSize.length; i++) {
			tableWidth += colSize[i];
		}
		
		String startDate = queryCondition.get(0);
		String endDate = queryCondition.get(1);
		String pfdCustomerInfoId = queryCondition.get(2);
		String orderType = queryCondition.get(3);
		String payType = queryCondition.get(4);
		
		List<CustomerInfoReportVO> vos = pfpCustomerInfoService.findPfpCustomerInfosByDate(startDate, endDate,
				pfdCustomerInfoId, orderType, payType);

		log.info(">>> vos.size() = " + vos.size());

		boolean showTitle = true;
		int i = 1;					
		int currentPageNum = 1;		// 目前頁數
		
		if(vos != null && vos.size() > 0){
			
			for(CustomerInfoReportVO vo:vos){
				
				if(showTitle){
					
					// 標題
					this.addReportTitle(doc, "新帳戶開通數", tableWidth, String.valueOf(currentPageNum));

					// 查詢條件
					this.addReportQueryCondition(doc, queryCondition, tableWidth);

					// 欄位名稱
					this.addReportColName(doc, tableWidth, colSize, colName);
					
					showTitle = false;
				}

				// 報表內容
				PdfPTable contentTable = this.buildReportContentTable(vo, colSize, tableWidth);
				this.addReportContent(doc, contentTable);

				if (i%pageMaxSize==0) {
					
					// 小計
					
					// 換頁
					doc.newPage();
					showTitle = true;
					currentPageNum++;
				}
				
				i++;
			}
			
		}else{
			
			// 標題
			this.addReportTitle(doc, "新帳戶開通數", tableWidth, String.valueOf(currentPageNum));
			// 副標題
			this.addReportQueryCondition(doc, queryCondition, tableWidth);
			// 欄位名稱
			this.addReportColName(doc, tableWidth, colSize, colName);
		}
	}
	
	@Override
	public void addReportQueryCondition(Document doc, List<String> queryCondition, int pageWidth) throws Exception{

		Map<String, String> orderTypeMap = new HashMap<String, String>();
		for (EnumOrderType e : EnumOrderType.values()) {
			orderTypeMap.put(e.getTypeTag(), e.getTypeName());
		}

		Map<String, String> payTypeMap = new HashMap<String, String>();
		payTypeMap.put(EnumPfdAccountPayType.ADVANCE.getPayType(), EnumPfdAccountPayType.ADVANCE.getPayName());
		payTypeMap.put(EnumPfdAccountPayType.LATER.getPayType(), EnumPfdAccountPayType.LATER.getPayName());

		String startDate = queryCondition.get(0);
		String endDate = queryCondition.get(1);
		String pfdCustomerInfoId = queryCondition.get(2);
		String orderType = queryCondition.get(3);
		String payType = queryCondition.get(4);

		String pfdCustomerInfoTitle = "";
		String orderTypeDesc = "";
		String payTypeDesc = "";

		if (StringUtils.isNotBlank(pfdCustomerInfoId)) {

			Map<String, String> conditionMap = new HashMap<String, String>();
			conditionMap.put("customerInfoId", pfdCustomerInfoId);

			PfdCustomerInfo pfdCustomerInfo = pfdAccountService.getPfdCustomerInfoByCondition(conditionMap).get(0);

			pfdCustomerInfoTitle = pfdCustomerInfo.getCompanyName();
		} else {
			pfdCustomerInfoTitle = "全部";
		}
		if (StringUtils.isNotBlank(orderType)) {
			orderTypeDesc = orderTypeMap.get(orderType);
		} else {
			orderTypeDesc = "全部";
		}
		if (StringUtils.isNotBlank(payType)) {
			payTypeDesc = payTypeMap.get(payType);
		} else {
			payTypeDesc = "全部";
		}

		// 報表查詢條件
		PdfPTable table = new PdfPTable(2);
		table.setTotalWidth(pageWidth);

		int[] colSizeArray = {200, 100};

		table.setWidths(colSizeArray);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("查詢日期： " + startDate + " ~ " + endDate, this.getFont10()));
		cell.setMinimumHeight(this.getColTitleHeight());
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorderWidth(0);
		cell.setColspan(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("經銷歸屬： " + pfdCustomerInfoTitle, this.getFont10()));
		cell.setMinimumHeight(this.getColTitleHeight());
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorderWidth(0);
		cell.setColspan(0);
		table.addCell(cell);

		doc.add(table);

		//換行用另一個 table
		PdfPTable table2 = new PdfPTable(2);
		table2.setTotalWidth(pageWidth);

		int[] colSizeArray2 = {200, 100};

		table2.setWidths(colSizeArray2);

		PdfPCell cell2 = null;

		cell2 = new PdfPCell(new Paragraph("儲值類型： " + orderTypeDesc, this.getFont10()));
		cell2.setMinimumHeight(this.getColTitleHeight());
		cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell2.setVerticalAlignment(Element.ALIGN_TOP);
		cell2.setBorderWidth(0);
		cell2.setColspan(0);
		table2.addCell(cell2);

		cell2 = new PdfPCell(new Paragraph("付款方式： " + payTypeDesc, this.getFont10()));
		cell2.setMinimumHeight(this.getColTitleHeight());
		cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell2.setVerticalAlignment(Element.ALIGN_TOP);
		cell2.setBorderWidth(0);
		cell2.setColspan(0);
		table2.addCell(cell2);

		doc.add(table2);
	}
	
	private PdfPTable buildReportContentTable(CustomerInfoReportVO vo, int[] colSize, int tableWidth) throws Exception{
		// 報表內容
		PdfPTable table = new PdfPTable(colSize.length);
		table.setTotalWidth(tableWidth);
		table.setWidths(colSize);
		
		PdfPCell cell = null;
		
		// 開通日期 
		cell = this.buildReportContentCell(vo.getActivateDate(), this.getFont8());
		table.addCell(cell);
		
		// 會員帳號
		cell = this.buildReportContentCell(vo.getMemberId(), this.getFont8());
		table.addCell(cell);

		// 帳戶編號
		cell = this.buildReportContentCell(vo.getCustomerInfoId(), this.getFont8());
		table.addCell(cell);

		// 帳戶名稱
		cell = this.buildReportContentCell(vo.getCustomerInfoName(), this.getFont8());
		table.addCell(cell);

		// 帳戶類別
		cell = this.buildReportContentCell(vo.getCustomerInfoType(), this.getFont8());
		table.addCell(cell);

		// 儲值金額
		cell = this.buildReportContentCell(String.valueOf(vo.getAddMoney()), this.getFont8());
		table.addCell(cell);	

		// 經銷歸屬
		cell = this.buildReportContentCell(vo.getPfdCustInfoName(), this.getFont8());
		table.addCell(cell);	

		// 業務歸屬
		cell = this.buildReportContentCell(vo.getPfdUserName(), this.getFont8());
		table.addCell(cell);	

		// 付款方式
		cell = this.buildReportContentCell(vo.getPayType(), this.getFont8());
		table.addCell(cell);	

		return table;
	}
	
	private PdfPCell buildReportContentCell(String cellName, Font size) throws Exception{
		
		PdfPCell cell = new PdfPCell(new Paragraph(cellName, size));
		cell.setMinimumHeight(this.getColDataHeight());
		cell.setBorderWidth(this.getColDataBorder());
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		return cell;
	}

	public void setPfdAccountService(IPfdAccountService pfdAccountService) {
		this.pfdAccountService = pfdAccountService;
	}

	public void setPfpCustomerInfoService(PfpCustomerInfoService pfpCustomerInfoService) {
		this.pfpCustomerInfoService = pfpCustomerInfoService;
	}
}
