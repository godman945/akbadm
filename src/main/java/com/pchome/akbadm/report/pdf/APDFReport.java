package com.pchome.akbadm.report.pdf;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public abstract class APDFReport {

	protected Logger log = LogManager.getRootLogger();

	private String fontPath; //字型檔路徑
	private BaseFont baseFont;

	/**
	 * 從 server.properties 注入 
	 */
	public void setFontPath(String fontPath) {
		this.fontPath = fontPath;
		try {
			baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public Font getFont8() throws Exception{
		return new Font(this.baseFont, 8 ,Font.ITALIC);
	}
	
	public Font getFont10() throws Exception{
		return new Font(this.baseFont, 10 ,Font.ITALIC);
	}
	
	public Font getFont20() throws Exception{
		return new Font(this.baseFont, 20 ,Font.BOLDITALIC);
	}
	
	public int getColTitleHeight() throws Exception{
		// 欄位高度
		return 20;
	}
	
	public int getColDataHeight() throws Exception{
		// 欄位高度
		return 15;
	}
	
	public int getColDataBorder() throws Exception{
		// 欄位框線樣式
		return 1;
	}
	
	public int getPageMaxSize() throws Exception{
		// 每頁最多幾筆資料
		return 20;
	}

	public byte[] getPdfStream(List<String> queryCondition) throws Exception{
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		// pageSize, marginLeft, marginRight, marginTop, marginBottom
		Document doc = new Document(PageSize.A4.rotate(), -40, -40, 20, 20); //橫印

		PdfWriter.getInstance(doc, out);

		doc.open();
		
		this.buildPdf(doc, queryCondition);
		
		doc.close();

		return out.toByteArray();
	}

	public abstract void buildPdf(Document doc, List<String> queryCondition) throws Exception;
	
	public abstract void addReportTitle(Document doc, String title, int tableWidth, String page) throws Exception;
	
	public abstract void addReportColName(Document doc, int tableWidth, int[] colSize, String[] colName) throws Exception;
	
	public abstract void addReportContent(Document doc, PdfPTable table) throws Exception;
}
