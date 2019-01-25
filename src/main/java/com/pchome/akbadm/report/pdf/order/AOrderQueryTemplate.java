package com.pchome.akbadm.report.pdf.order;

import java.util.Date;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.pchome.akbadm.report.pdf.APDFReport;
import com.pchome.soft.util.DateValueUtil;

public abstract class AOrderQueryTemplate extends APDFReport{

	@Override
	public void addReportTitle(Document doc, String title, int tableWidth, String page) throws Exception {
		// TODO Auto-generated method stub
		
		// 報表抬頭		
		int[] colTitleSize = {tableWidth-120, 50, 70};
		
		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(tableWidth);
		table.setWidths(colTitleSize);
		
		PdfPCell cell = null;
		
		cell = new PdfPCell(new Paragraph(title, this.getFont20()));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorderWidth(0);
		cell.setRowspan(3);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("列印日期：", this.getFont10()));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(DateValueUtil.getInstance().dateToString(new Date()), this.getFont10()));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("頁次：", this.getFont10()));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(page, this.getFont10()));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		doc.add(table);
	}
	
	public abstract void addReportQueryCondition(Document doc, List<String> queryCondition, int pageWidth) throws Exception;
	
	@Override
	public void addReportColName(Document doc, int tableWidth, int[] colSize, String[] colName) throws Exception{
		
		// 報表欄位
		PdfPTable table = new PdfPTable(colSize.length);
		table.setTotalWidth(tableWidth);

		table.setWidths(colSize);

		PdfPCell cell = null;

		for (int i=0; i<colName.length; i++) {
			cell = new PdfPCell(new Paragraph(colName[i], this.getFont10()));
			cell.setMinimumHeight(this.getColTitleHeight());
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBorderWidth(1);
			table.addCell(cell);
		}

		doc.add(table);
	}
	
	@Override
	public void addReportContent(Document doc, PdfPTable table) throws Exception{
		doc.add(table);
	}
}
