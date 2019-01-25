package com.pchome.akbadm.report.pdf.trans;

import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.pchome.akbadm.db.pojo.PfpTransDetail;
import com.pchome.akbadm.db.service.trans.PfpTransDetailService;
import com.pchome.enumerate.trans.EnumTransType;
import com.pchome.soft.util.DateValueUtil;

public class TransQueryReport extends ATransQueryTemplate{

	private PfpTransDetailService pfpTransDetailService;
	private float totalAddMoney = 0;
	private float totalTax = 0;
	private float totalExpense = 0;
	private float totalIncome = 0;
	private float totalRefund = 0;
	@Override
	public void buildPdf(Document doc, List<String> queryCondition) throws Exception {
		// TODO Auto-generated method stub
		
		// 表格欄位寬度
		int[] colSize = {50, 70, 70, 50, 30, 50, 50, 50, 50};
		
		// 表格欄位名稱
		String[] colName = {"交易日期", "帳戶名稱", "帳戶編號", "儲值金額", "營業稅", "廣告費用", "調整(+)", "退款(-)", "帳戶餘額"};
				
		int tableWidth = 0;

		// 表格寬度
		for (int i=0; i<colSize.length; i++) {
			tableWidth += colSize[i];
		}
		
		String customerInfo = queryCondition.get(0);
		String startDate = queryCondition.get(1);
		String endDate = queryCondition.get(2);
		String field = queryCondition.get(3);
		String pfdCustomerInfoId = queryCondition.get(4);
		String payType = queryCondition.get(5);
		
		List<PfpTransDetail> transDetails = pfpTransDetailService.findTransDetail(customerInfo, startDate, endDate,
				pfdCustomerInfoId);
		
		//log.info(" size = "+vos.size());
		
		boolean showTitle = true;
		int i = 1;					
		int currentPageNum = 1;		// 目前頁數
		
		if(transDetails != null && transDetails.size() > 0){
			
			for(PfpTransDetail trans:transDetails){
				
				if(showTitle){
					
					// 標題
					this.addReportTitle(doc, "交易明細", tableWidth, String.valueOf(currentPageNum));
					// 副標題
					this.addReportQueryCondition(doc, queryCondition, tableWidth);
					// 欄位名稱
					this.addReportColName(doc, tableWidth, colSize, colName);
					
					showTitle = false;
				}
				
					
				
				// 報表內容
				PdfPTable contentTable = this.buildReportContentTable(trans, colSize, tableWidth);
				this.addReportContent(doc, contentTable);
				
				if (i%this.getPageMaxSize()==0) {
					
					// 小計
					
					// 換頁
					doc.newPage();
					showTitle = true;
					currentPageNum++;
				}
				
				i++;
			}
			
			// 報表小計
			PdfPTable subCountTable = this.buildReportSubCountTable(colSize, tableWidth, totalAddMoney, totalTax, totalExpense, totalIncome, totalRefund);
			this.addReportContent(doc, subCountTable);
			
		}else{
			
			// 標題
			this.addReportTitle(doc, "交易明細", tableWidth, String.valueOf(currentPageNum));
			// 副標題
			this.addReportQueryCondition(doc, queryCondition, tableWidth);
			// 欄位名稱
			this.addReportColName(doc, tableWidth, colSize, colName);
		}
	}
	
	@Override
	public void addReportQueryCondition(Document doc, List<String> queryCondition, int pageWidth) throws Exception{
		
		// 報表查詢條件
		PdfPTable table = new PdfPTable(1);
		table.setTotalWidth(pageWidth);

		int[] colSizeArray = {200};

		table.setWidths(colSizeArray);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("查詢日期： " + queryCondition.get(1) + " ~ " + queryCondition.get(2), this.getFont10()));
		cell.setMinimumHeight(this.getColTitleHeight());
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorderWidth(0);
		cell.setColspan(0);
		table.addCell(cell);

		doc.add(table);
	}
	
	private PdfPTable buildReportContentTable(PfpTransDetail trans, int[] colSize, int tableWidth) throws Exception{
		// 報表內容
		PdfPTable table = new PdfPTable(colSize.length);
		table.setTotalWidth(tableWidth);
		table.setWidths(colSize);
		
		PdfPCell cell = null;
		
		// 交易日期
		cell = this.buildReportContentCell(DateValueUtil.getInstance().dateToString(trans.getTransDate()), this.getFont8());
		table.addCell(cell);
		
		// 帳戶名稱
		cell = this.buildReportContentCell(trans.getPfpCustomerInfo().getCustomerInfoTitle(), this.getFont8());
		table.addCell(cell);

		// 帳戶編號
		cell = this.buildReportContentCell(trans.getPfpCustomerInfo().getCustomerInfoId(), this.getFont8());
		table.addCell(cell);

		// 儲值金額
		float addMoney = 0;
		if(trans.getTransType().equals(EnumTransType.ADD_MONEY.getTypeId())){
			addMoney = trans.getTransPrice();			
			totalAddMoney += addMoney;
		}
		
		cell = this.buildReportContentCell(String.valueOf((int)addMoney), this.getFont8());
		table.addCell(cell);
		
		// 營業稅	
		totalAddMoney += trans.getTax();
		
		cell = this.buildReportContentCell(String.valueOf((int)trans.getTax()), this.getFont8());
		table.addCell(cell);
		
		// 廣告費用
		float expense = 0;
		if(trans.getTransType().equals(EnumTransType.SPEND_COST.getTypeId())){
			expense = trans.getTransPrice();
			totalExpense += expense;
		}
		
		cell = this.buildReportContentCell(String.valueOf((int)expense), this.getFont8());
		table.addCell(cell);
		
		// 調整(+)
		float income = 0;
		if(trans.getTransType().equals(EnumTransType.INVALID_COST.getTypeId())){
			income = trans.getTransPrice();
			totalIncome += income;
		}
		
		cell = this.buildReportContentCell(String.valueOf((int)income), this.getFont8());
		table.addCell(cell);
		
		// 退款(-)
		float refund = 0;
		if(trans.getTransType().equals(EnumTransType.REFUND.getTypeId())){
			refund = trans.getTransPrice();
			totalRefund += refund;
		}
		
		cell = this.buildReportContentCell(String.valueOf((int)refund), this.getFont8());
		table.addCell(cell);
		
		// 帳戶餘額		
		cell = this.buildReportContentCell(String.valueOf((int)trans.getRemain()), this.getFont8());
		table.addCell(cell);
		
		return table;
	}
	
	private PdfPTable buildReportSubCountTable(int[] colSize, int tableWidth, float totalAddMoney, float totalTax, float totalExpense, float totalIncome, float totalRefund) throws Exception{
		
		// 報表總計
		PdfPTable table = new PdfPTable(colSize.length);
		table.setTotalWidth(tableWidth);
		table.setWidths(colSize);
		
		PdfPCell cell = null;
		
		// 空欄位
		cell = this.buildReportContentCell("", this.getFont8());
		table.addCell(cell);
		
		// 空欄位
		cell = this.buildReportContentCell("", this.getFont8());
		table.addCell(cell);
		
		// 空欄位
		cell = this.buildReportContentCell("小計：", this.getFont8());
		table.addCell(cell);
		
		// 儲值金額 
		cell = this.buildReportContentCell(String.valueOf((int)totalAddMoney), this.getFont8());
		table.addCell(cell);
		
		// 營業稅
		cell = this.buildReportContentCell(String.valueOf((int)totalTax), this.getFont8());
		table.addCell(cell);
		
		// 廣告費用
		cell = this.buildReportContentCell(String.valueOf((int)totalExpense), this.getFont8());
		table.addCell(cell);
		
		// 調整(+)
		cell = this.buildReportContentCell(String.valueOf((int)totalIncome), this.getFont8());
		table.addCell(cell);
		
		// 退款(-)
		cell = this.buildReportContentCell(String.valueOf((int)totalRefund), this.getFont8());
		table.addCell(cell);
		
		// 空欄位
		cell = this.buildReportContentCell("", this.getFont8());
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

	public void setPfpTransDetailService(PfpTransDetailService pfpTransDetailService) {
		this.pfpTransDetailService = pfpTransDetailService;
	}



		
}
