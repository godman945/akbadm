package com.pchome.akbadm.report.pdf.costRank;

import java.text.DecimalFormat;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.pchome.akbadm.db.service.ad.PfpAdPvclkService;
import com.pchome.akbadm.db.vo.AdPvclkVO;

public class CostRankQueryReport extends ACostRankQueryTemplate{

	private PfpAdPvclkService pfpAdPvclkService;
	
	@Override
	public void buildPdf(Document doc, List<String> queryCondition) throws Exception {
		// TODO Auto-generated method stub
		
		// 表格欄位寬度
		int[] colSize = {30, 70, 70, 50, 50, 50, 50, 50, 50};
		
		// 表格欄位名稱
		String[] colName = {"排名", "帳戶名稱", "廣告活動", "每日花費上線", "曝光數", "點選率", "點選次數", "平均點選費用", "費用"};
				
		int tableWidth = 0;

		// 表格寬度
		for (int i=0; i<colSize.length; i++) {
			tableWidth += colSize[i];
		}
		
		String pageSize = queryCondition.get(0);
		String startDate = queryCondition.get(1);
		String endDate = queryCondition.get(2);
		
		List<AdPvclkVO> adPvclkVOs = pfpAdPvclkService.findAdPvclkCostRank(startDate, endDate, Integer.parseInt(pageSize));
		
		//log.info(" size = "+vos.size());
		
		boolean showTitle = true;
		int i = 1;					
		int currentPageNum = 1;		// 目前頁數
		
		if(adPvclkVOs != null && adPvclkVOs.size() > 0){
			
			for(AdPvclkVO vo:adPvclkVOs){
				
				if(showTitle){
					
					// 標題
					this.addReportTitle(doc, "花費成效排名", tableWidth, String.valueOf(currentPageNum));
					// 副標題
					this.addReportQueryCondition(doc, queryCondition, tableWidth);
					// 欄位名稱
					this.addReportColName(doc, tableWidth, colSize, colName);
					
					showTitle = false;
				}
				
					
				
				// 報表內容
				PdfPTable contentTable = this.buildReportContentTable(vo, colSize, tableWidth, i);
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
			
			
		}else{
			
			// 標題
			this.addReportTitle(doc, "花費成效排名", tableWidth, String.valueOf(currentPageNum));
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
	
	private PdfPTable buildReportContentTable(AdPvclkVO vo, int[] colSize, int tableWidth, int rank) throws Exception{
		// 報表內容
		PdfPTable table = new PdfPTable(colSize.length);
		table.setTotalWidth(tableWidth);
		table.setWidths(colSize);
		
		DecimalFormat df = new DecimalFormat("#.00");  
		
		PdfPCell cell = null;
		
		// 排名
		cell = this.buildReportContentCell(String.valueOf(rank), this.getFont8());
		table.addCell(cell);
		
		// 帳戶名稱
		cell = this.buildReportContentCell(vo.getPfpAdPvclk()[5].toString(), this.getFont8());
		table.addCell(cell);

		// 廣告活動
		cell = this.buildReportContentCell(vo.getPfpAdPvclk()[6].toString(), this.getFont8());
		table.addCell(cell);
		
		// 每日花費上線
		cell = this.buildReportContentCell(vo.getPfpAdPvclk()[7].toString(), this.getFont8());
		table.addCell(cell);
		
		// 曝光數
		cell = this.buildReportContentCell(vo.getPfpAdPvclk()[0].toString(), this.getFont8());
		table.addCell(cell);
		
		// 點選率
		cell = this.buildReportContentCell(df.format(vo.getAdClickRate())+"%", this.getFont8());
		table.addCell(cell);
		
		// 點選次數
		cell = this.buildReportContentCell(vo.getPfpAdPvclk()[1].toString(), this.getFont8());
		table.addCell(cell);
		
		// 平均點選費用
		cell = this.buildReportContentCell(df.format(vo.getAverageCost()), this.getFont8());
		table.addCell(cell);
		
		// 費用
		cell = this.buildReportContentCell(vo.getPfpAdPvclk()[2].toString(), this.getFont8());
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

	public void setPfpAdPvclkService(PfpAdPvclkService pfpAdPvclkService) {
		this.pfpAdPvclkService = pfpAdPvclkService;
	}


		
}
