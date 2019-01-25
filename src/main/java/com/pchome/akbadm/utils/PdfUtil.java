package com.pchome.akbadm.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import com.pchome.akbadm.db.vo.AdActionReportVO;
import com.pchome.akbadm.db.vo.AdReportVO;
import com.pchome.akbadm.db.vo.AdTemplateReportVO;
import com.pchome.akbadm.db.vo.KeywordReportVO;
import com.pchome.akbadm.db.vo.PfpTransDetailReportVO;
import com.pchome.akbadm.struts2.ajax.report.PfpTransDetailVO;

public class PdfUtil {

	private static final Log logger = LogFactory.getLog(PdfUtil.class);

	//字型檔位置
//	private String fontPath = "C:\\Windows\\Fonts\\kaiu.ttf";
//	private String fontPath = "/usr/share/fonts/wqy-zenhei/kaiu.ttf";
//	private String fontPath = "/home/webuser/akb/git.project/akbadm.master/WebContent/WEB-INF/src/fonts/kaiu.ttf";	// 正式機位置
//	private String fontPath = "/export/home/webuser/akb/webapps/admDevelop/WEB-INF/src/fonts/kaiu.ttf";	// 測試機位置
	private String fontPath = "D:\\My Documents\\workspace_java_kepler\\akbadm_develop2\\WebContent\\WEB-INF\\src\\fonts\\kaiu.ttf";	// 本機位置
 
	private NumberFormat numFormat = new DecimalFormat("###,###,###,###");
	private DecimalFormat df = new DecimalFormat("###,##0.00");

	public PdfUtil(String fontPath) {
		this.fontPath = fontPath;
	}

	/**
	 * 關鍵字成效報表
	 */
	public void prepareKeywordReportPdf(Document doc, List<KeywordReportVO> dataList,
			String startDate, String endDate, String keywordType, String sortMode, String displayCount) {

		try {

			java.io.File fontFile = new java.io.File(fontPath);
			logger.info(">>> " + fontFile.getAbsolutePath() + ", " + fontFile.exists());
			BaseFont bfChinese = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED); 

			Font font10 = new Font(bfChinese, 10 ,Font.ITALIC);
		    Font font8 = new Font(bfChinese, 8 ,Font.ITALIC);
		    Font fontB20 = new Font(bfChinese, 20 ,Font.BOLDITALIC);

			PdfPCell cell = null;

			int colDataHeight = 15;
			int colDataBorder = 1;

			int currentPageNum = 1; //目前頁數

			int pageMaxSize = 20; //每頁最多幾筆資料

			int totalDataCount = dataList.size(); //資料總筆數

			int totalPage = 0; //總頁數
			if (totalDataCount%pageMaxSize==0) {
				totalPage = totalDataCount/pageMaxSize;
			} else {
				totalPage = totalDataCount/pageMaxSize + 1;
			}

			int[] colSizeArray = {20, 80, 40, 40, 40, 50, 40};
			String[] colNameArray = {"排名", "關鍵字", "曝光數", "點選次數", "點選率", "平均點選出價", "費用"};

			int pageWidth = 0; //表格總寬度
			for (int i=0; i<colSizeArray.length; i++) {
				pageWidth += colSizeArray[i];
			}

			double pvSum_all = 0; //PV總和加總
			double clkSum_all = 0; //Click總和加總
			double priceSum_all = 0; //價格總和加總

			for (int i=0; i<totalPage; i++) {

				int currentDataAmount = 0; //目前頁數的資料筆數
				if (currentPageNum!=totalPage) { //不是最後一頁
					currentDataAmount = pageMaxSize;
				} else { //最後一頁
					currentDataAmount = totalDataCount - pageMaxSize*(totalPage-1);
				}

				//title
				this.writeKeywordReportTitle(doc, font10, fontB20,
						Integer.toString(currentPageNum), pageWidth, colSizeArray, colNameArray);

				//query condition
				if (i==0) {
					this.writeKeywordReportQueryCondition(doc, font10, pageWidth,
							startDate, endDate, keywordType, sortMode, displayCount);
				}

				//column name
				this.writeReportColName(doc, font10, pageWidth, colSizeArray, colNameArray);

				//content
				PdfPTable table = new PdfPTable(colSizeArray.length);
				table.setTotalWidth(pageWidth);
				table.setWidths(colSizeArray);

				for (int k=0; k<currentDataAmount; k++) {

					int dataIndex = (currentPageNum-1)*pageMaxSize + k;
					KeywordReportVO vo = dataList.get(dataIndex);

					//排名
					cell = new PdfPCell(new Paragraph(Integer.toString(dataIndex + 1), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//關鍵字
					cell = new PdfPCell(new Paragraph(vo.getKeyword(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//曝光數
					cell = new PdfPCell(new Paragraph(vo.getKwPvSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					pvSum_all += numFormat.parse(vo.getKwPvSum()).doubleValue();

					//點選次數
					cell = new PdfPCell(new Paragraph(vo.getKwClkSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					clkSum_all += numFormat.parse(vo.getKwClkSum()).doubleValue();

					//點選率
					cell = new PdfPCell(new Paragraph(vo.getKwClkRate(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//平均點選出價
					cell = new PdfPCell(new Paragraph(vo.getClkPriceAvg(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//費用
					cell = new PdfPCell(new Paragraph(vo.getKwPriceSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					priceSum_all += numFormat.parse(vo.getKwPriceSum()).doubleValue();
				}

				//footer
				if (currentPageNum==totalPage) { //最後一頁才會出現統計數據

					//總計
					cell = new PdfPCell(new Paragraph("總計", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//填空白
					cell = new PdfPCell(new Paragraph("", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//曝光數總和
					cell = new PdfPCell(new Paragraph(numFormat.format(pvSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//點選次數總和
					cell = new PdfPCell(new Paragraph(numFormat.format(clkSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//點選率
					String strClkRate = df.format((clkSum_all / pvSum_all)*100) + "%";
					cell = new PdfPCell(new Paragraph(strClkRate, font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//平均點選出價
					String strClkPriceAvg = df.format(priceSum_all / clkSum_all);
					cell = new PdfPCell(new Paragraph(strClkPriceAvg, font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//費用總和
					cell = new PdfPCell(new Paragraph(numFormat.format(priceSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);
				}

				doc.add(table);

				//換頁
				doc.newPage();

				currentPageNum++;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void writeKeywordReportTitle(Document doc, Font font10, Font fontB20,
			String pageNum,	int pageWidth, int[] colSizeArray, String[] colNameArray) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		int[] colSizeTitle = {pageWidth-120, 50, 70};

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);
		table.setWidths(colSizeTitle);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("關鍵字成效報表", fontB20));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorderWidth(0);
		cell.setRowspan(3);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("列印日期：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(sdf.format(new Date()), font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("頁次：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(pageNum, font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		doc.add(table);
	}

	private void writeKeywordReportQueryCondition(Document doc, Font font10, int pageWidth,
			String startDate, String endDate, String keywordType,
			String sortMode, String displayCount) throws Exception {

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);

		int colHeight = 20;

		int[] colSizeArray = {200, 200, 200};

		table.setWidths(colSizeArray);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("廣告形式： " + keywordType, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("排序方式： " + sortMode, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("顯示前： " + displayCount + " 名", font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("查詢日期： " + startDate + " ~ " + endDate, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorderWidth(0);
		cell.setColspan(3);
		table.addCell(cell);

		doc.add(table);
	}

	private void writeReportColName(Document doc, Font font10, int pageWidth,
			int[] colSizeArray, String[] colNameArray) throws Exception {

		PdfPTable table = new PdfPTable(colSizeArray.length);
		table.setTotalWidth(pageWidth);

		int colHeight = 20;

		table.setWidths(colSizeArray);

		PdfPCell cell = null;

		for (int i=0; i<colNameArray.length; i++) {
			cell = new PdfPCell(new Paragraph(colNameArray[i], font10));
			cell.setMinimumHeight(colHeight);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBorderWidth(1);
			table.addCell(cell);
		}

		doc.add(table);
	}

	/**
	 * 關鍵字明細報表
	 */
	public void prepareKeywordDetailPdf(Document doc, List<KeywordReportVO> dataList,
			String startDate, String endDate, String keywordType, String sortMode, String displayCount) {

		try {

			java.io.File fontFile = new java.io.File(fontPath);
			logger.info(">>> " + fontFile.getAbsolutePath() + ", " + fontFile.exists());
			BaseFont bfChinese = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED); 

			Font font10 = new Font(bfChinese, 10 ,Font.ITALIC);
		    Font font8 = new Font(bfChinese, 8 ,Font.ITALIC);
		    Font fontB20 = new Font(bfChinese, 20 ,Font.BOLDITALIC);

			PdfPCell cell = null;

			int colDataHeight = 15;
			int colDataBorder = 1;

			int currentPageNum = 1; //目前頁數

			int pageMaxSize = 15; //每頁最多幾筆資料

			int totalDataCount = dataList.size(); //資料總筆數

			int totalPage = 0; //總頁數
			if (totalDataCount%pageMaxSize==0) {
				totalPage = totalDataCount/pageMaxSize;
			} else {
				totalPage = totalDataCount/pageMaxSize + 1;
			}

			int[] colSizeArray = {20, 100, 50, 50, 50, 50, 40, 40, 40, 50, 40};
			String[] colNameArray = {"排名", "會員帳號", "帳戶名稱", "廣告活動", "廣告群組", "關鍵字", "曝光數", "點選次數", "點選率", "平均點選出價", "費用"};

			int pageWidth = 0; //表格總寬度
			for (int i=0; i<colSizeArray.length; i++) {
				pageWidth += colSizeArray[i];
			}

			double pvSum_all = 0; //PV總和加總
			double clkSum_all = 0; //Click總和加總
			double priceSum_all = 0; //價格總和加總

			for (int i=0; i<totalPage; i++) {

				int currentDataAmount = 0; //目前頁數的資料筆數
				if (currentPageNum!=totalPage) { //不是最後一頁
					currentDataAmount = pageMaxSize;
				} else { //最後一頁
					currentDataAmount = totalDataCount - pageMaxSize*(totalPage-1);
				}

				//title
				this.writeKeywordDetailTitle(doc, font10, fontB20,
						Integer.toString(currentPageNum), pageWidth, colSizeArray, colNameArray);

				//query condition
				if (i==0) {
					this.writeKeywordDetailQueryCondition(doc, font10, pageWidth,
							startDate, endDate, keywordType, sortMode, displayCount);
				}

				//column name
				this.writeKeywordDetailColName(doc, font10, pageWidth, colSizeArray, colNameArray);

				//content
				PdfPTable table = new PdfPTable(colSizeArray.length);
				table.setTotalWidth(pageWidth);
				table.setWidths(colSizeArray);

				for (int k=0; k<currentDataAmount; k++) {

					int dataIndex = (currentPageNum-1)*pageMaxSize + k;
					KeywordReportVO vo = dataList.get(dataIndex);

					//排名
					cell = new PdfPCell(new Paragraph(Integer.toString(dataIndex + 1), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//會員帳號
					cell = new PdfPCell(new Paragraph(vo.getCustomerId(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//帳戶名稱
					cell = new PdfPCell(new Paragraph(vo.getCustomerName(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//廣告活動
					cell = new PdfPCell(new Paragraph(vo.getAdAction(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//廣告群組
					cell = new PdfPCell(new Paragraph(vo.getAdGroup(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//關鍵字
					cell = new PdfPCell(new Paragraph(vo.getKeyword(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//曝光數
					cell = new PdfPCell(new Paragraph(vo.getKwPvSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					pvSum_all += numFormat.parse(vo.getKwPvSum()).doubleValue();

					//點選次數
					cell = new PdfPCell(new Paragraph(vo.getKwClkSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					clkSum_all += numFormat.parse(vo.getKwClkSum()).doubleValue();

					//點選率
					cell = new PdfPCell(new Paragraph(vo.getKwClkRate(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//平均點選出價
					cell = new PdfPCell(new Paragraph(vo.getClkPriceAvg(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//費用
					cell = new PdfPCell(new Paragraph(vo.getKwPriceSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					priceSum_all += numFormat.parse(vo.getKwPriceSum()).doubleValue();
				}

				//footer
				if (currentPageNum==totalPage) { //最後一頁才會出現統計數據

					//總計
					cell = new PdfPCell(new Paragraph("總計", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//填空白
					for (int e=0; e<5; e++) {
						cell = new PdfPCell(new Paragraph("", font8));
						cell.setMinimumHeight(colDataHeight);
						cell.setBorderWidth(0);
						table.addCell(cell);
					}

					//曝光數總和
					cell = new PdfPCell(new Paragraph(numFormat.format(pvSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//點選次數總和
					cell = new PdfPCell(new Paragraph(numFormat.format(clkSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//點選率
					String strClkRate = df.format((clkSum_all / pvSum_all)*100) + "%";
					cell = new PdfPCell(new Paragraph(strClkRate, font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//平均點選出價
					String strClkPriceAvg = df.format(priceSum_all / clkSum_all);
					cell = new PdfPCell(new Paragraph(strClkPriceAvg, font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//費用總和
					cell = new PdfPCell(new Paragraph(numFormat.format(priceSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);
				}

				doc.add(table);

				//換頁
				doc.newPage();

				currentPageNum++;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void writeKeywordDetailTitle(Document doc, Font font10, Font fontB20,
			String pageNum,	int pageWidth, int[] colSizeArray, String[] colNameArray) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		int[] colSizeTitle = {pageWidth-190, 100, 90};

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);
		table.setWidths(colSizeTitle);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("關鍵字明細報表", fontB20));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorderWidth(0);
		cell.setRowspan(3);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("列印日期：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(sdf.format(new Date()), font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("頁次：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(pageNum, font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		doc.add(table);
	}

	private void writeKeywordDetailQueryCondition(Document doc, Font font10, int pageWidth,
			String startDate, String endDate, String keywordType,
			String sortMode, String displayCount) throws Exception {

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);

		int colHeight = 20;

		int[] colSizeArray = {200, 200, 200};

		table.setWidths(colSizeArray);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("廣告形式： " + keywordType, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("排序方式： " + sortMode, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("顯示前： " + displayCount + " 名", font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("查詢日期： " + startDate + " ~ " + endDate, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorderWidth(0);
		cell.setColspan(3);
		table.addCell(cell);

		doc.add(table);
	}

	private void writeKeywordDetailColName(Document doc, Font font10, int pageWidth,
			int[] colSizeArray, String[] colNameArray) throws Exception {

		PdfPTable table = new PdfPTable(colSizeArray.length);
		table.setTotalWidth(pageWidth);

		int colHeight = 20;

		table.setWidths(colSizeArray);

		PdfPCell cell = null;

		for (int i=0; i<colNameArray.length; i++) {
			cell = new PdfPCell(new Paragraph(colNameArray[i], font10));
			cell.setMinimumHeight(colHeight);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setBorderWidth(1);
			table.addCell(cell);
		}

		doc.add(table);
	}

	/**
	 * 廣告成效報表
	 */
	public void prepareAdReportPdf(Document doc, List<AdReportVO> dataList,
			String startDate, String endDate, String keywordType, String sortMode, String displayCount) {

		try {

			java.io.File fontFile = new java.io.File(fontPath);
			logger.info(">>> " + fontFile.getAbsolutePath() + ", " + fontFile.exists());
			BaseFont bfChinese = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED); 

			Font font10 = new Font(bfChinese, 10 );
		    Font font8 = new Font(bfChinese, 8 );
		    Font fontB20 = new Font(bfChinese, 20 ,Font.BOLD);

			PdfPCell cell = null;

			int colDataHeight = 15;
			int colDataBorder = 1;

			int currentPageNum = 1; //目前頁數

			int pageMaxSize = 20; //每頁最多幾筆資料

			int totalDataCount = dataList.size(); //資料總筆數

			int totalPage = 0; //總頁數
			if (totalDataCount%pageMaxSize==0) {
				totalPage = totalDataCount/pageMaxSize;
			} else {
				totalPage = totalDataCount/pageMaxSize + 1;
			}

			int[] colSizeArray = {20, 80, 40, 40, 40, 40, 40, 40, 50, 40};
			String[] colNameArray = {"排名", "帳戶名稱", "廣告title", "廣告活動", "廣告群組", "曝光數", "點選次數", "點選率", "平均點選出價", "費用"};

			int pageWidth = 0; //表格總寬度
			for (int i=0; i<colSizeArray.length; i++) {
				pageWidth += colSizeArray[i];
			}

			double pvSum_all = 0; //PV總和加總
			double clkSum_all = 0; //Click總和加總
			double priceSum_all = 0; //價格總和加總

			for (int i=0; i<totalPage; i++) {

				int currentDataAmount = 0; //目前頁數的資料筆數
				if (currentPageNum!=totalPage) { //不是最後一頁
					currentDataAmount = pageMaxSize;
				} else { //最後一頁
					currentDataAmount = totalDataCount - pageMaxSize*(totalPage-1);
				}

				//title
				this.writeAdReportTitle(doc, font10, fontB20,
						Integer.toString(currentPageNum), pageWidth, colSizeArray, colNameArray);

				//query condition
				if (i==0) {
					this.writeAdReportQueryCondition(doc, font10, pageWidth,
							startDate, endDate, keywordType, sortMode, displayCount);
				}

				//column name
				this.writeReportColName(doc, font10, pageWidth, colSizeArray, colNameArray);

				//content
				PdfPTable table = new PdfPTable(colSizeArray.length);
				table.setTotalWidth(pageWidth);
				table.setWidths(colSizeArray);

				for (int k=0; k<currentDataAmount; k++) {

					int dataIndex = (currentPageNum-1)*pageMaxSize + k;
					AdReportVO vo = dataList.get(dataIndex);

					//排名
					cell = new PdfPCell(new Paragraph(Integer.toString(dataIndex + 1), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//帳戶名稱
					cell = new PdfPCell(new Paragraph(vo.getCustomerName() + "(" + vo.getCustomerId() + ")", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//廣告title
					cell = new PdfPCell(new Paragraph(vo.getAdTitle(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//廣告活動
					cell = new PdfPCell(new Paragraph(vo.getAdAction(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//廣告群組
					cell = new PdfPCell(new Paragraph(vo.getAdGroup(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//曝光數
					cell = new PdfPCell(new Paragraph(vo.getKwPvSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					pvSum_all += numFormat.parse(vo.getKwPvSum()).doubleValue();

					//點選次數
					cell = new PdfPCell(new Paragraph(vo.getKwClkSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					clkSum_all += numFormat.parse(vo.getKwClkSum()).doubleValue();

					//點選率
					cell = new PdfPCell(new Paragraph(vo.getKwClkRate(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//平均點選出價
					cell = new PdfPCell(new Paragraph("$ " + vo.getClkPriceAvg(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//費用
					cell = new PdfPCell(new Paragraph("$ " + vo.getKwPriceSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					priceSum_all += numFormat.parse(vo.getKwPriceSum()).doubleValue();
				}

				//footer
				if (currentPageNum==totalPage) { //最後一頁才會出現統計數據

					//總計
					cell = new PdfPCell(new Paragraph("總計", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//填空白
					for (int e=0; e<4; e++) {
						cell = new PdfPCell(new Paragraph("", font8));
						cell.setMinimumHeight(colDataHeight);
						cell.setBorderWidth(0);
						table.addCell(cell);
					}

					//曝光數總和
					cell = new PdfPCell(new Paragraph(numFormat.format(pvSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//點選次數總和
					cell = new PdfPCell(new Paragraph(numFormat.format(clkSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//點選率
					String strClkRate = df.format((clkSum_all / pvSum_all)*100) + "%";
					cell = new PdfPCell(new Paragraph(strClkRate, font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//平均點選出價
					String strClkPriceAvg = df.format(priceSum_all / clkSum_all);
					cell = new PdfPCell(new Paragraph("$ " + strClkPriceAvg, font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//費用總和
					cell = new PdfPCell(new Paragraph("$ " + numFormat.format(priceSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);
				}

				doc.add(table);

				//換頁
				doc.newPage();

				currentPageNum++;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void writeAdReportTitle(Document doc, Font font10, Font fontB20,
			String pageNum,	int pageWidth, int[] colSizeArray, String[] colNameArray) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		int[] colSizeTitle = {pageWidth-190, 100, 90};

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);
		table.setWidths(colSizeTitle);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("廣告成效報表", fontB20));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorderWidth(0);
		cell.setRowspan(3);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("列印日期：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(sdf.format(new Date()), font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("頁次：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(pageNum, font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		doc.add(table);
	}

	private void writeAdReportQueryCondition(Document doc, Font font10, int pageWidth,
			String startDate, String endDate, String keywordType,
			String sortMode, String displayCount) throws Exception {

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);

		int colHeight = 20;

		int[] colSizeArray = {200, 200, 200};

		table.setWidths(colSizeArray);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("廣告形式： " + keywordType, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("排序方式： " + sortMode, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("顯示前： " + displayCount + " 名", font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("查詢日期： " + startDate + " ~ " + endDate, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorderWidth(0);
		cell.setColspan(3);
		table.addCell(cell);

		doc.add(table);
	}

	/**
	 * 總廣告成效
	 */
	public void prepareAdActionReportPdf(Document doc, List<AdActionReportVO> dataList,
			String startDate, String endDate, String adType, String customerInfoId,
			String pfdCustomerInfoIdText, String payTypeText) {

		try {

			java.io.File fontFile = new java.io.File(fontPath);
			logger.info(">>> " + fontFile.getAbsolutePath() + ", " + fontFile.exists());
			BaseFont bfChinese = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED); 

			Font font10 = new Font(bfChinese, 10 ,Font.ITALIC);
		    Font font8 = new Font(bfChinese, 8 ,Font.ITALIC);
		    Font fontB20 = new Font(bfChinese, 20 ,Font.BOLDITALIC);

			PdfPCell cell = null;

			int colDataHeight = 15;
			int colDataBorder = 1;

			int currentPageNum = 1; //目前頁數

			int pageMaxSize = 30; //每頁最多幾筆資料

			int totalDataCount = dataList.size(); //資料總筆數

			int totalPage = 0; //總頁數
			if (totalDataCount%pageMaxSize==0) {
				totalPage = totalDataCount/pageMaxSize;
			} else {
				totalPage = totalDataCount/pageMaxSize + 1;
			}

			int[] colSizeArray = {50, 50, 50, 50, 50, 50, 50};
			String[] colNameArray = {"日期", "曝光數", "點選次數", "點選率", "平均點選出價", "費用", "超播金額"};

			int pageWidth = 0; //表格總寬度
			for (int i=0; i<colSizeArray.length; i++) {
				pageWidth += colSizeArray[i];
			}

			double pvSum_all = 0; //PV總和加總
			double clkSum_all = 0; //Click總和加總
			double priceSum_all = 0; //價格總和加總
			double overPriceSum_all = 0; //超播金額

			for (int i=0; i<totalPage; i++) {

				int currentDataAmount = 0; //目前頁數的資料筆數
				if (currentPageNum!=totalPage) { //不是最後一頁
					currentDataAmount = pageMaxSize;
				} else { //最後一頁
					currentDataAmount = totalDataCount - pageMaxSize*(totalPage-1);
				}

				//title
				this.writeAdActionReportTitle(doc, font10, fontB20,
						Integer.toString(currentPageNum), pageWidth, colSizeArray, colNameArray);

				//query condition
				if (i==0) {
					this.writeAdActionReportQueryCondition(doc, font10, pageWidth,
							startDate, endDate, adType, customerInfoId,
							pfdCustomerInfoIdText, payTypeText);
				}

				//column name
				this.writeReportColName(doc, font10, pageWidth, colSizeArray, colNameArray);

				//content
				PdfPTable table = new PdfPTable(colSizeArray.length);
				table.setTotalWidth(pageWidth);
				table.setWidths(colSizeArray);

				for (int k=0; k<currentDataAmount; k++) {

					int dataIndex = (currentPageNum-1)*pageMaxSize + k;
					AdActionReportVO vo = dataList.get(dataIndex);

					//日期
					cell = new PdfPCell(new Paragraph(vo.getReportDate(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//曝光數
					cell = new PdfPCell(new Paragraph(vo.getPvSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					pvSum_all += numFormat.parse(vo.getPvSum()).doubleValue();

					//點選次數
					cell = new PdfPCell(new Paragraph(vo.getClkSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					clkSum_all += numFormat.parse(vo.getClkSum()).doubleValue();

					//點選率
					cell = new PdfPCell(new Paragraph(vo.getClkRate(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//平均點選出價
					cell = new PdfPCell(new Paragraph(vo.getClkPriceAvg(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//費用
					cell = new PdfPCell(new Paragraph(vo.getPriceSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					priceSum_all += numFormat.parse(vo.getPriceSum()).doubleValue();

					//超播金額
					cell = new PdfPCell(new Paragraph(vo.getOverPriceSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					overPriceSum_all += numFormat.parse(vo.getOverPriceSum()).doubleValue();
				}

				//footer
				if (currentPageNum==totalPage) { //最後一頁才會出現統計數據

					//總計
					cell = new PdfPCell(new Paragraph("總計", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//曝光數總和
					cell = new PdfPCell(new Paragraph(numFormat.format(pvSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//點選次數總和
					cell = new PdfPCell(new Paragraph(numFormat.format(clkSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//點選率
					String strClkRate = df.format((clkSum_all / pvSum_all)*100) + "%";
					cell = new PdfPCell(new Paragraph(strClkRate, font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//平均點選出價
					String strClkPriceAvg = df.format(priceSum_all / clkSum_all);
					cell = new PdfPCell(new Paragraph(strClkPriceAvg, font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//費用總和
					cell = new PdfPCell(new Paragraph(numFormat.format(priceSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//超播金額總和
					cell = new PdfPCell(new Paragraph(numFormat.format(overPriceSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);
				}

				doc.add(table);

				//換頁
				doc.newPage();

				currentPageNum++;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void writeAdActionReportTitle(Document doc, Font font10, Font fontB20,
			String pageNum,	int pageWidth, int[] colSizeArray, String[] colNameArray) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		int[] colSizeTitle = {pageWidth-130, 50, 80};

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);
		table.setWidths(colSizeTitle);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("總廣告成效", fontB20));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorderWidth(0);
		cell.setRowspan(3);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("列印日期：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(sdf.format(new Date()), font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("頁次：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(pageNum, font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		doc.add(table);
	}

	private void writeAdActionReportQueryCondition(Document doc, Font font10, int pageWidth,
			String startDate, String endDate, String adType, String customerInfoId,
			String pfdCustomerInfoIdText, String payTypeText) throws Exception {

		int colHeight = 20;

		int[] colSizeArray = {200, 150, 150};

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);
		table.setWidths(colSizeArray);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("廣告形式： " + adType, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("帳戶序號： " + customerInfoId, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("經銷歸屬： " + pfdCustomerInfoIdText, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		doc.add(table);

		PdfPTable table2 = new PdfPTable(3);
		table2.setTotalWidth(pageWidth);
		table2.setWidths(colSizeArray);

		PdfPCell cell2 = null;

		cell2 = new PdfPCell(new Paragraph("查詢日期： " + startDate + " ~ " + endDate, font10));
		cell2.setMinimumHeight(colHeight);
		cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell2.setVerticalAlignment(Element.ALIGN_TOP);
		cell2.setColspan(2);
		cell2.setBorderWidth(0);
		table2.addCell(cell2);

		cell2 = new PdfPCell(new Paragraph("付款方式： " + payTypeText, font10));
		cell2.setMinimumHeight(colHeight);
		cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell2.setVerticalAlignment(Element.ALIGN_TOP);
		cell2.setBorderWidth(0);
		table2.addCell(cell2);

		doc.add(table2);
	}

	/**
	 * 總廣告成效明細
	 */
	public void prepareAdActionDetailPdf(Document doc, List<AdActionReportVO> dataList,
			String reportDate, String adTypeText, String pfpCustomerInfoIdText,
			String pfdCustomerInfoIdText, String payTypeText) {

		try {

			java.io.File fontFile = new java.io.File(fontPath);
			logger.info(">>> " + fontFile.getAbsolutePath() + ", " + fontFile.exists());
			BaseFont bfChinese = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED); 

			Font font10 = new Font(bfChinese, 10 ,Font.ITALIC);
		    Font font8 = new Font(bfChinese, 8 ,Font.ITALIC);
		    Font fontB20 = new Font(bfChinese, 20 ,Font.BOLDITALIC);

			PdfPCell cell = null;

			int colDataHeight = 15;
			int colDataBorder = 1;

			int currentPageNum = 1; //目前頁數

			int pageMaxSize = 20; //每頁最多幾筆資料

			int totalDataCount = dataList.size(); //資料總筆數

			int totalPage = 0; //總頁數
			if (totalDataCount%pageMaxSize==0) {
				totalPage = totalDataCount/pageMaxSize;
			} else {
				totalPage = totalDataCount/pageMaxSize + 1;
			}

			int[] colSizeArray = {40, 80, 80, 80, 80, 30, 30, 30, 30, 30, 30};
			String[] colNameArray = {"日期", "經銷商", "業務", "會員帳戶", "廣告活動", "曝光數", "點選次數", "點選率", "平均點選出價", "費用", "超播金額"};

			int pageWidth = 0; //表格總寬度
			for (int i=0; i<colSizeArray.length; i++) {
				pageWidth += colSizeArray[i];
			}

			double pvSum_all = 0; //PV總和加總
			double clkSum_all = 0; //Click總和加總
			double priceSum_all = 0; //價格總和加總
			double overPriceSum_all = 0; //超播金額

			for (int i=0; i<totalPage; i++) {

				int currentDataAmount = 0; //目前頁數的資料筆數
				if (currentPageNum!=totalPage) { //不是最後一頁
					currentDataAmount = pageMaxSize;
				} else { //最後一頁
					currentDataAmount = totalDataCount - pageMaxSize*(totalPage-1);
				}

				//title
				this.writeAdActionDetailTitle(doc, font10, fontB20,
						Integer.toString(currentPageNum), pageWidth, colSizeArray, colNameArray);

				//query condition
//				if (i==0) {
//					this.writeAdActionDetailQueryCondition(doc, font10, pageWidth, reportDate,
//							adTypeText, pfpCustomerInfoIdText, pfdCustomerInfoIdText, payTypeText);
//				}

				//column name
				this.writeReportColName(doc, font10, pageWidth, colSizeArray, colNameArray);

				//content
				PdfPTable table = new PdfPTable(colSizeArray.length);
				table.setTotalWidth(pageWidth);
				table.setWidths(colSizeArray);

				for (int k=0; k<currentDataAmount; k++) {

					int dataIndex = (currentPageNum-1)*pageMaxSize + k;
					AdActionReportVO vo = dataList.get(dataIndex);

					//日期
					cell = new PdfPCell(new Paragraph(vo.getReportDate(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//經銷商
					cell = new PdfPCell(new Paragraph(vo.getPfdCustomerInfoTitle() + " (" + vo.getPfdCustomerInfoId() + ")", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//業務
					cell = new PdfPCell(new Paragraph(vo.getPfdUserName() + " (" + vo.getPfdUserId() + ")", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//會員帳戶
					cell = new PdfPCell(new Paragraph(vo.getCustomerInfoName() + " (" + vo.getCustomerInfoId() + ")", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//廣告活動
					cell = new PdfPCell(new Paragraph(vo.getAdActionName(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//曝光數
					cell = new PdfPCell(new Paragraph(vo.getPvSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					pvSum_all += numFormat.parse(vo.getPvSum()).doubleValue();

					//點選次數
					cell = new PdfPCell(new Paragraph(vo.getClkSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					clkSum_all += numFormat.parse(vo.getClkSum()).doubleValue();

					//點選率
					cell = new PdfPCell(new Paragraph(vo.getClkRate(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//平均點選出價
					cell = new PdfPCell(new Paragraph(vo.getClkPriceAvg(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//費用
					cell = new PdfPCell(new Paragraph(vo.getPriceSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					priceSum_all += numFormat.parse(vo.getPriceSum()).doubleValue();

					//超播金額
					cell = new PdfPCell(new Paragraph(vo.getOverPriceSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					overPriceSum_all += numFormat.parse(vo.getOverPriceSum()).doubleValue();
				}

				//footer
				if (currentPageNum==totalPage) { //最後一頁才會出現統計數據

					//總計
					cell = new PdfPCell(new Paragraph("總計", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//填空白
					for (int e=0; e<3; e++) {
						cell = new PdfPCell(new Paragraph("", font8));
						cell.setMinimumHeight(colDataHeight);
						cell.setBorderWidth(0);
						table.addCell(cell);
					}

					//曝光數總和
					cell = new PdfPCell(new Paragraph(numFormat.format(pvSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//點選次數總和
					cell = new PdfPCell(new Paragraph(numFormat.format(clkSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//點選率
					String strClkRate = df.format((clkSum_all / pvSum_all)*100) + "%";
					cell = new PdfPCell(new Paragraph(strClkRate, font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//平均點選出價
					String strClkPriceAvg = "0";
					if (priceSum_all!=0 && clkSum_all!=0) {
						strClkPriceAvg = df.format(priceSum_all / clkSum_all);
					}
					cell = new PdfPCell(new Paragraph(strClkPriceAvg, font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//費用總和
					cell = new PdfPCell(new Paragraph(numFormat.format(priceSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//超播金額
					cell = new PdfPCell(new Paragraph(numFormat.format(overPriceSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);
				}

				doc.add(table);

				//換頁
				doc.newPage();

				currentPageNum++;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void writeAdActionDetailTitle(Document doc, Font font10, Font fontB20,
			String pageNum,	int pageWidth, int[] colSizeArray, String[] colNameArray) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		int[] colSizeTitle = {pageWidth-190, 100, 90};

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);
		table.setWidths(colSizeTitle);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("總廣告成效明細", fontB20));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorderWidth(0);
		cell.setRowspan(3);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("列印日期：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(sdf.format(new Date()), font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("頁次：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(pageNum, font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		doc.add(table);
	}

	private void writeAdActionDetailQueryCondition(Document doc, Font font10, int pageWidth,
			String reportDate, String adTypeText, String pfpCustomerInfoId,
			String pfdCustomerInfoIdText, String payTypeText) throws Exception {

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);

		int colHeight = 20;

		int[] colSizeArray = {200, 200, 200};

		table.setWidths(colSizeArray);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("廣告型式： " + adTypeText, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("帳戶序號： " + pfpCustomerInfoId, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("查詢日期： " + reportDate, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorderWidth(0);
		cell.setColspan(3);
		table.addCell(cell);

		doc.add(table);
	}

	/**
	 * 總使用明細
	 */
	public void prepareTotalTransReportPdf(Document doc, List<PfpTransDetailReportVO> dataList,
			String startDate, String endDate) {

		try {

			java.io.File fontFile = new java.io.File(fontPath);
			logger.info(">>> " + fontFile.getAbsolutePath() + ", " + fontFile.exists());
			BaseFont bfChinese = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED); 

			Font font10 = new Font(bfChinese, 10 ,Font.ITALIC);
		    Font font8 = new Font(bfChinese, 8 ,Font.ITALIC);
		    Font fontB20 = new Font(bfChinese, 20 ,Font.BOLDITALIC);

			PdfPCell cell = null;

			int colDataHeight = 15;
			int colDataBorder = 1;

			int currentPageNum = 1; //目前頁數

			int pageMaxSize = 20; //每頁最多幾筆資料

			int totalDataCount = dataList.size(); //資料總筆數

			int totalPage = 0; //總頁數
			if (totalDataCount%pageMaxSize==0) {
				totalPage = totalDataCount/pageMaxSize;
			} else {
				totalPage = totalDataCount/pageMaxSize + 1;
			}

			int[] colSizeArray = {50, 50, 50, 50, 50, 50, 50};
			String[] colNameArray = {"日期", "帳戶儲值", "營業稅", "廣告活動花費", "惡意點擊費用", "退款", "免費贈送"};

			int pageWidth = 0; //表格總寬度
			for (int i=0; i<colSizeArray.length; i++) {
				pageWidth += colSizeArray[i];
			}

			//加總
			double add_sum = 0; //加值
			double tax_sum = 0; //稅
			double spend_sum = 0; //花費
			double invalid_sum = 0; //惡意點擊
			double refund_sum = 0; //惡意點擊
			double free_sum = 0; //免費贈送

			for (int i=0; i<totalPage; i++) {

				int currentDataAmount = 0; //目前頁數的資料筆數
				if (currentPageNum!=totalPage) { //不是最後一頁
					currentDataAmount = pageMaxSize;
				} else { //最後一頁
					currentDataAmount = totalDataCount - pageMaxSize*(totalPage-1);
				}

				//title
				this.writeTotalTransReportTitle(doc, font10, fontB20,
						Integer.toString(currentPageNum), pageWidth, colSizeArray, colNameArray);

				//query condition
				if (i==0) {
					this.writeTotalTransReportQueryCondition(doc, font10, pageWidth,
							startDate, endDate);
				}

				//column name
				this.writeReportColName(doc, font10, pageWidth, colSizeArray, colNameArray);

				//content
				PdfPTable table = new PdfPTable(colSizeArray.length);
				table.setTotalWidth(pageWidth);
				table.setWidths(colSizeArray);

				for (int k=0; k<currentDataAmount; k++) {

					int dataIndex = (currentPageNum-1)*pageMaxSize + k;
					PfpTransDetailReportVO vo = dataList.get(dataIndex);

					//日期
					cell = new PdfPCell(new Paragraph(vo.getReportDate(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//帳戶儲值
					cell = new PdfPCell(new Paragraph(numFormat.format(vo.getAdd()), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					add_sum += vo.getAdd();

					//營業稅
					cell = new PdfPCell(new Paragraph(numFormat.format(vo.getTax()), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					tax_sum += vo.getTax();

					//廣告活動花費
					cell = new PdfPCell(new Paragraph(numFormat.format(vo.getSpend()), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					spend_sum += vo.getSpend();

					//惡意點擊費用
					cell = new PdfPCell(new Paragraph(numFormat.format(vo.getInvalid()), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					invalid_sum += vo.getInvalid();

					//退款
					cell = new PdfPCell(new Paragraph(numFormat.format(vo.getRefund()), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					refund_sum += vo.getRefund();

					//免費贈送
					cell = new PdfPCell(new Paragraph(numFormat.format(vo.getFree()), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					free_sum += vo.getFree();
				}

				//footer
				if (currentPageNum==totalPage) { //最後一頁才會出現統計數據

					//總計
					cell = new PdfPCell(new Paragraph("總計", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//帳戶儲值
					cell = new PdfPCell(new Paragraph(numFormat.format(add_sum), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//營業稅總和
					cell = new PdfPCell(new Paragraph(numFormat.format(tax_sum), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//廣告活動花費
					cell = new PdfPCell(new Paragraph(numFormat.format(spend_sum), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//惡意點擊費用
					cell = new PdfPCell(new Paragraph(numFormat.format(invalid_sum), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//退款
					cell = new PdfPCell(new Paragraph(numFormat.format(refund_sum), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//免費贈送
					cell = new PdfPCell(new Paragraph(numFormat.format(free_sum), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);
				}

				doc.add(table);

				//換頁
				doc.newPage();

				currentPageNum++;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void writeTotalTransReportTitle(Document doc, Font font10, Font fontB20,
			String pageNum,	int pageWidth, int[] colSizeArray, String[] colNameArray) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		int[] colSizeTitle = {pageWidth-190, 100, 90};

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);
		table.setWidths(colSizeTitle);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("總使用明細", fontB20));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorderWidth(0);
		cell.setRowspan(3);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("列印日期：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(sdf.format(new Date()), font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("頁次：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(pageNum, font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		doc.add(table);
	}

	private void writeTotalTransReportQueryCondition(Document doc, Font font10, int pageWidth,
			String startDate, String endDate) throws Exception {

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);

		int colHeight = 20;

		int[] colSizeArray = {200, 200, 200};

		table.setWidths(colSizeArray);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("查詢日期： " + startDate + " ~ " + endDate, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorderWidth(0);
		cell.setColspan(3);
		table.addCell(cell);

		doc.add(table);
	}

	/**
	 * 總使用明細
	 */
	public void prepareTotalTransDetailPdf(Document doc, List<PfpTransDetailReportVO> dataList,
			String reportDate) {

		try {

			java.io.File fontFile = new java.io.File(fontPath);
			logger.info(">>> " + fontFile.getAbsolutePath() + ", " + fontFile.exists());
			BaseFont bfChinese = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED); 

			Font font10 = new Font(bfChinese, 10 ,Font.ITALIC);
		    Font font8 = new Font(bfChinese, 8 ,Font.ITALIC);
		    Font fontB20 = new Font(bfChinese, 20 ,Font.BOLDITALIC);

			PdfPCell cell = null;

			int colDataHeight = 15;
			int colDataBorder = 1;

			int currentPageNum = 1; //目前頁數

			int pageMaxSize = 20; //每頁最多幾筆資料

			int totalDataCount = dataList.size(); //資料總筆數

			int totalPage = 0; //總頁數
			if (totalDataCount%pageMaxSize==0) {
				totalPage = totalDataCount/pageMaxSize;
			} else {
				totalPage = totalDataCount/pageMaxSize + 1;
			}

			int[] colSizeArray = {50, 50, 50, 50, 50, 50, 50, 50, 50};
			String[] colNameArray = {"日期", "會員帳號", "帳戶名稱", "帳戶儲值", "營業稅", "廣告活動花費", "惡意點擊費用", "退款", "免費贈送"};

			int pageWidth = 0; //表格總寬度
			for (int i=0; i<colSizeArray.length; i++) {
				pageWidth += colSizeArray[i];
			}

			//加總
			double add_sum = 0; //加值
			double tax_sum = 0; //稅
			double spend_sum = 0; //花費
			double invalid_sum = 0; //惡意點擊
			double refund_sum = 0; //惡意點擊
			double free_sum = 0; //免費贈送

			for (int i=0; i<totalPage; i++) {

				int currentDataAmount = 0; //目前頁數的資料筆數
				if (currentPageNum!=totalPage) { //不是最後一頁
					currentDataAmount = pageMaxSize;
				} else { //最後一頁
					currentDataAmount = totalDataCount - pageMaxSize*(totalPage-1);
				}

				//title
				this.writeTotalTransDetailTitle(doc, font10, fontB20,
						Integer.toString(currentPageNum), pageWidth, colSizeArray, colNameArray);

				//query condition
				if (i==0) {
					this.writeTotalTransDetailQueryCondition(doc, font10, pageWidth, reportDate);
				}

				//column name
				this.writeReportColName(doc, font10, pageWidth, colSizeArray, colNameArray);

				//content
				PdfPTable table = new PdfPTable(colSizeArray.length);
				table.setTotalWidth(pageWidth);
				table.setWidths(colSizeArray);

				for (int k=0; k<currentDataAmount; k++) {

					int dataIndex = (currentPageNum-1)*pageMaxSize + k;
					PfpTransDetailReportVO vo = dataList.get(dataIndex);

					//日期
					cell = new PdfPCell(new Paragraph(vo.getReportDate(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//會員帳號
					cell = new PdfPCell(new Paragraph(vo.getCustomerInfoId(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//帳戶名稱
					cell = new PdfPCell(new Paragraph(vo.getCustomerInfoName(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//帳戶儲值
					cell = new PdfPCell(new Paragraph(numFormat.format(vo.getAdd()), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					add_sum += vo.getAdd();

					//營業稅
					cell = new PdfPCell(new Paragraph(numFormat.format(vo.getTax()), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					tax_sum += vo.getTax();

					//廣告活動花費
					cell = new PdfPCell(new Paragraph(numFormat.format(vo.getSpend()), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					spend_sum += vo.getSpend();

					//惡意點擊費用
					cell = new PdfPCell(new Paragraph(numFormat.format(vo.getInvalid()), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					invalid_sum += vo.getInvalid();

					//退款
					cell = new PdfPCell(new Paragraph(numFormat.format(vo.getRefund()), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					refund_sum += vo.getRefund();

					//免費贈送
					cell = new PdfPCell(new Paragraph(numFormat.format(vo.getFree()), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					free_sum += vo.getFree();
				}

				//footer
				if (currentPageNum==totalPage) { //最後一頁才會出現統計數據

					//總計
					cell = new PdfPCell(new Paragraph("總計", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					cell.setColspan(3);
					table.addCell(cell);

					//帳戶儲值
					cell = new PdfPCell(new Paragraph(numFormat.format(add_sum), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//營業稅總和
					cell = new PdfPCell(new Paragraph(numFormat.format(tax_sum), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//廣告活動花費
					cell = new PdfPCell(new Paragraph(numFormat.format(spend_sum), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//惡意點擊費用
					cell = new PdfPCell(new Paragraph(numFormat.format(invalid_sum), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//退款
					cell = new PdfPCell(new Paragraph(numFormat.format(refund_sum), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//免費贈送
					cell = new PdfPCell(new Paragraph(numFormat.format(free_sum), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);
				}

				doc.add(table);

				//換頁
				doc.newPage();

				currentPageNum++;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void writeTotalTransDetailTitle(Document doc, Font font10, Font fontB20,
			String pageNum,	int pageWidth, int[] colSizeArray, String[] colNameArray) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		int[] colSizeTitle = {pageWidth-190, 100, 90};

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);
		table.setWidths(colSizeTitle);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("單日使用明細", fontB20));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorderWidth(0);
		cell.setRowspan(3);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("列印日期：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(sdf.format(new Date()), font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("頁次：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(pageNum, font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		doc.add(table);
	}

	private void writeTotalTransDetailQueryCondition(Document doc, Font font10, int pageWidth,
			String reportDate) throws Exception {

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);

		int colHeight = 20;

		int[] colSizeArray = {200, 200, 200};

		table.setWidths(colSizeArray);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("查詢日期： " + reportDate, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorderWidth(0);
		cell.setColspan(3);
		table.addCell(cell);

		doc.add(table);
	}

	/**
	 * 廣告樣式成效表
	 */
	public void prepareAdTemplateReportPdf(Document doc, List<AdTemplateReportVO> dataList,
			String startDate, String endDate) {

		try {

			java.io.File fontFile = new java.io.File(fontPath);
			logger.info(">>> " + fontFile.getAbsolutePath() + ", " + fontFile.exists());
			BaseFont bfChinese = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED); 

			Font font10 = new Font(bfChinese, 10 ,Font.ITALIC);
		    Font font8 = new Font(bfChinese, 8 ,Font.ITALIC);
		    Font fontB20 = new Font(bfChinese, 20 ,Font.BOLDITALIC);

			PdfPCell cell = null;

			int colDataHeight = 15;
			int colDataBorder = 1;

			int currentPageNum = 1; //目前頁數

			int pageMaxSize = 20; //每頁最多幾筆資料

			int totalDataCount = dataList.size(); //資料總筆數

			int totalPage = 0; //總頁數
			if (totalDataCount%pageMaxSize==0) {
				totalPage = totalDataCount/pageMaxSize;
			} else {
				totalPage = totalDataCount/pageMaxSize + 1;
			}

			int[] colSizeArray = {50, 50, 50, 50, 50, 50};
			String[] colNameArray = {"廣告樣式", "曝光數", "點選次數", "點選率", "平均點選出價", "費用"};

			int pageWidth = 0; //表格總寬度
			for (int i=0; i<colSizeArray.length; i++) {
				pageWidth += colSizeArray[i];
			}

			double pvSum_all = 0; //PV總和加總
			double clkSum_all = 0; //Click總和加總
			double priceSum_all = 0; //價格總和加總

			for (int i=0; i<totalPage; i++) {

				int currentDataAmount = 0; //目前頁數的資料筆數
				if (currentPageNum!=totalPage) { //不是最後一頁
					currentDataAmount = pageMaxSize;
				} else { //最後一頁
					currentDataAmount = totalDataCount - pageMaxSize*(totalPage-1);
				}

				//title
				this.writeAdTemplateTitle(doc, font10, fontB20,
						Integer.toString(currentPageNum), pageWidth, colSizeArray, colNameArray);

				//query condition
				if (i==0) {
					this.writeAdTemplateQueryCondition(doc, font10, pageWidth, startDate, endDate);
				}

				//column name
				this.writeReportColName(doc, font10, pageWidth, colSizeArray, colNameArray);

				//content
				PdfPTable table = new PdfPTable(colSizeArray.length);
				table.setTotalWidth(pageWidth);
				table.setWidths(colSizeArray);

				for (int k=0; k<currentDataAmount; k++) {

					int dataIndex = (currentPageNum-1)*pageMaxSize + k;
					AdTemplateReportVO vo = dataList.get(dataIndex);

					//廣告樣式
					cell = new PdfPCell(new Paragraph(vo.getTemplateProdName(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//曝光數
					cell = new PdfPCell(new Paragraph(vo.getPvSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					pvSum_all += numFormat.parse(vo.getPvSum()).doubleValue();

					//點選次數
					cell = new PdfPCell(new Paragraph(vo.getClkSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					clkSum_all += numFormat.parse(vo.getClkSum()).doubleValue();

					//點選率
					cell = new PdfPCell(new Paragraph(vo.getClkRate(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//平均點選出價
					cell = new PdfPCell(new Paragraph(vo.getClkPriceAvg(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//費用
					cell = new PdfPCell(new Paragraph(vo.getPriceSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					priceSum_all += numFormat.parse(vo.getPriceSum()).doubleValue();
				}

				//footer
				if (currentPageNum==totalPage) { //最後一頁才會出現統計數據

					//總計
					cell = new PdfPCell(new Paragraph("總計", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//曝光數總和
					cell = new PdfPCell(new Paragraph(numFormat.format(pvSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//點選次數總和
					cell = new PdfPCell(new Paragraph(numFormat.format(clkSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//點選率
					String strClkRate = df.format((clkSum_all / pvSum_all)*100) + "%";
					cell = new PdfPCell(new Paragraph(strClkRate, font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//平均點選出價
					String strClkPriceAvg = "0";
					if (priceSum_all!=0 && clkSum_all!=0) {
						strClkPriceAvg = df.format(priceSum_all / clkSum_all);
					}
					cell = new PdfPCell(new Paragraph(strClkPriceAvg, font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//費用總和
					cell = new PdfPCell(new Paragraph(numFormat.format(priceSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);
				}

				doc.add(table);

				//換頁
				doc.newPage();

				currentPageNum++;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void writeAdTemplateTitle(Document doc, Font font10, Font fontB20,
			String pageNum,	int pageWidth, int[] colSizeArray, String[] colNameArray) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		int[] colSizeTitle = {pageWidth-190, 100, 90};

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);
		table.setWidths(colSizeTitle);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("廣告樣式成效表", fontB20));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorderWidth(0);
		cell.setRowspan(3);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("列印日期：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(sdf.format(new Date()), font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("頁次：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(pageNum, font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		doc.add(table);
	}

	private void writeAdTemplateQueryCondition(Document doc, Font font10, int pageWidth,
			String startDate, String endDate) throws Exception {

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);

		int colHeight = 20;

		int[] colSizeArray = {200, 200, 200};

		table.setWidths(colSizeArray);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("查詢日期： " + startDate + " ~ " + endDate, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorderWidth(0);
		cell.setColspan(3);
		table.addCell(cell);

		doc.add(table);
	}

	/**
	 * 廣告十日內即將下檔報表
	 */
	public void prepareAdOfflineReportPdf(Document doc, List<AdActionReportVO> dataList) {

		try {

			java.io.File fontFile = new java.io.File(fontPath);
			logger.info(">>> " + fontFile.getAbsolutePath() + ", " + fontFile.exists());
			BaseFont bfChinese = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED); 

			Font font10 = new Font(bfChinese, 10 ,Font.ITALIC);
		    Font font8 = new Font(bfChinese, 8 ,Font.ITALIC);
		    Font fontB20 = new Font(bfChinese, 20 ,Font.BOLDITALIC);

			PdfPCell cell = null;

			int colDataHeight = 15;
			int colDataBorder = 1;

			int currentPageNum = 1; //目前頁數

			int pageMaxSize = 20; //每頁最多幾筆資料

			int totalDataCount = dataList.size(); //資料總筆數

			int totalPage = 0; //總頁數
			if (totalDataCount%pageMaxSize==0) {
				totalPage = totalDataCount/pageMaxSize;
			} else {
				totalPage = totalDataCount/pageMaxSize + 1;
			}

			int[] colSizeArray = {40, 40, 40, 80, 80, 80, 60, 60, 40, 40, 40, 60, 40};
			String[] colNameArray = {"廣告開始", "廣告結束", "統一編號", "經銷歸屬", "帳戶名稱", "廣告活動", "每日花費上限", "每日花費達成", "曝光數", "點選次數", "點選率", "平均點選出價", "費用"};

			int pageWidth = 0; //表格總寬度
			for (int i=0; i<colSizeArray.length; i++) {
				pageWidth += colSizeArray[i];
			}

			for (int i=0; i<totalPage; i++) {

				int currentDataAmount = 0; //目前頁數的資料筆數
				if (currentPageNum!=totalPage) { //不是最後一頁
					currentDataAmount = pageMaxSize;
				} else { //最後一頁
					currentDataAmount = totalDataCount - pageMaxSize*(totalPage-1);
				}

				//title
				this.writeAdOfflineTitle(doc, font10, fontB20,
						Integer.toString(currentPageNum), pageWidth, colSizeArray, colNameArray);

				//column name
				this.writeReportColName(doc, font10, pageWidth, colSizeArray, colNameArray);

				//content
				PdfPTable table = new PdfPTable(colSizeArray.length);
				table.setTotalWidth(pageWidth);
				table.setWidths(colSizeArray);

				for (int k=0; k<currentDataAmount; k++) {

					int dataIndex = (currentPageNum-1)*pageMaxSize + k;
					AdActionReportVO vo = dataList.get(dataIndex);

					//廣告開始
					cell = new PdfPCell(new Paragraph(vo.getStartDate(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//廣告結束
					cell = new PdfPCell(new Paragraph(vo.getEndDate(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//統一編號
					cell = new PdfPCell(new Paragraph(vo.getTaxId(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//經銷歸屬
					cell = new PdfPCell(new Paragraph(vo.getDealer(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//帳戶名稱
					cell = new PdfPCell(new Paragraph(vo.getCustomerInfoName(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//廣告活動
					cell = new PdfPCell(new Paragraph(vo.getAdActionName(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//每日花費上限
					cell = new PdfPCell(new Paragraph(vo.getMaxPrice(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//每日花費達成
					cell = new PdfPCell(new Paragraph(vo.getArrivalRate(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//曝光數
					cell = new PdfPCell(new Paragraph(vo.getPvSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//點選次數
					cell = new PdfPCell(new Paragraph(vo.getClkSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//點選率
					cell = new PdfPCell(new Paragraph(vo.getClkRate(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//平均點選出價
					cell = new PdfPCell(new Paragraph(vo.getClkPriceAvg(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//費用
					cell = new PdfPCell(new Paragraph(vo.getPriceSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
				}

				doc.add(table);

				//換頁
				doc.newPage();

				currentPageNum++;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void writeAdOfflineTitle(Document doc, Font font10, Font fontB20,
			String pageNum,	int pageWidth, int[] colSizeArray, String[] colNameArray) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		int[] colSizeTitle = {pageWidth-190, 100, 90};

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);
		table.setWidths(colSizeTitle);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("廣告十日內即將下檔報表", fontB20));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorderWidth(0);
		cell.setRowspan(3);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("列印日期：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(sdf.format(new Date()), font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("頁次：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(pageNum, font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		doc.add(table);
	}

	/**
	 * 花費成效排名
	 */
	public void prepareAdSpendReportPdf(Document doc, List<AdActionReportVO> dataList,
			String startDate, String endDate) {

		try {

			java.io.File fontFile = new java.io.File(fontPath);
			logger.info(">>> " + fontFile.getAbsolutePath() + ", " + fontFile.exists());
			BaseFont bfChinese = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED); 

			Font font10 = new Font(bfChinese, 10 ,Font.ITALIC);
		    Font font8 = new Font(bfChinese, 8 ,Font.ITALIC);
		    Font fontB20 = new Font(bfChinese, 20 ,Font.BOLDITALIC);

			PdfPCell cell = null;

			int colDataHeight = 15;
			int colDataBorder = 1;

			int currentPageNum = 1; //目前頁數	

			int pageMaxSize = 20; //每頁最多幾筆資料

			int totalDataCount = dataList.size(); //資料總筆數

			int totalPage = 0; //總頁數
			if (totalDataCount%pageMaxSize==0) {
				totalPage = totalDataCount/pageMaxSize;
			} else {
				totalPage = totalDataCount/pageMaxSize + 1;
			}

			int[] colSizeArray = {30, 70, 50, 60, 60, 50, 40, 40, 40, 40, 50, 40};
			String[] colNameArray = {"排名", "經銷歸屬" ,"會員帳號", "帳戶名稱", "廣告活動", "每日花費上限", "調控金額", "曝光數", "點選次數", "點選率", "平均點選出價", "費用"};

			int pageWidth = 0; //表格總寬度
			for (int i=0; i<colSizeArray.length; i++) {
				pageWidth += colSizeArray[i];
			}
			System.out.println("pageWidth = " + pageWidth);

			for (int i=0; i<totalPage; i++) {

				int currentDataAmount = 0; //目前頁數的資料筆數
				if (currentPageNum!=totalPage) { //不是最後一頁
					currentDataAmount = pageMaxSize;
				} else { //最後一頁
					currentDataAmount = totalDataCount - pageMaxSize*(totalPage-1);
				}

				//title
				this.writeAdSpendReportTitle(doc, font10, fontB20,
						Integer.toString(currentPageNum), pageWidth, colSizeArray, colNameArray);

				//query condition
				if (i==0) {
					this.writeAdSpendReportQueryCondition(doc, font10, pageWidth,
							startDate, endDate);
				}

				//column name
				this.writeReportColName(doc, font10, pageWidth, colSizeArray, colNameArray);

				System.out.println("pageWidth = " + pageWidth);
				//content
				PdfPTable table = new PdfPTable(colSizeArray.length);
				table.setTotalWidth(pageWidth);
				table.setWidths(colSizeArray);

				for (int k=0; k<currentDataAmount; k++) {

					int dataIndex = (currentPageNum-1)*pageMaxSize + k;
					AdActionReportVO vo = dataList.get(dataIndex);

					//排名
					cell = new PdfPCell(new Paragraph(Integer.toString(dataIndex + 1), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//經銷歸屬
					cell = new PdfPCell(new Paragraph(vo.getDealer(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//會員帳號
					cell = new PdfPCell(new Paragraph(vo.getCustomerInfoId(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//帳戶名稱
					cell = new PdfPCell(new Paragraph(vo.getCustomerInfoName(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//廣告活動
					cell = new PdfPCell(new Paragraph(vo.getAdActionName(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//每日花費上限
					cell = new PdfPCell(new Paragraph(vo.getMaxPrice(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					//調控金額
					cell = new PdfPCell(new Paragraph(vo.getAdActionControlPrice(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//曝光數
					cell = new PdfPCell(new Paragraph(vo.getPvSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//點選次數
					cell = new PdfPCell(new Paragraph(vo.getClkSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//點選率
					cell = new PdfPCell(new Paragraph(vo.getClkRate(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//平均點選出價
					cell = new PdfPCell(new Paragraph(vo.getClkPriceAvg(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//費用
					cell = new PdfPCell(new Paragraph(vo.getPriceSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
				}

				doc.add(table);

				//換頁
				doc.newPage();

				currentPageNum++;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void writeAdSpendReportTitle(Document doc, Font font10, Font fontB20,
			String pageNum,	int pageWidth, int[] colSizeArray, String[] colNameArray) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		int[] colSizeTitle = {pageWidth-190, 100, 90};

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);
		table.setWidths(colSizeTitle);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("花費成效排名", fontB20));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorderWidth(0);
		cell.setRowspan(3);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("列印日期：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(sdf.format(new Date()), font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("頁次：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(pageNum, font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		doc.add(table);
	}

	private void writeAdSpendReportQueryCondition(Document doc, Font font10, int pageWidth,
			String startDate, String endDate) throws Exception {

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);

		int colHeight = 20;

		int[] colSizeArray = {200, 200, 200};

		table.setWidths(colSizeArray);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("查詢日期： " + startDate + " ~ " + endDate, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorderWidth(0);
		cell.setColspan(3);
		table.addCell(cell);

		doc.add(table);
	}

	/**
	 * 關鍵字出價報表
	 */
	public void prepareKeywordOfferPriceReportPdf(Document doc, List<KeywordReportVO> dataList,
			String startDate, String endDate, String keywordType, String searchText, String displayCount) {

		try {

			java.io.File fontFile = new java.io.File(fontPath);
			logger.info(">>> " + fontFile.getAbsolutePath() + ", " + fontFile.exists());
			BaseFont bfChinese = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED); 

			Font font10 = new Font(bfChinese, 10 ,Font.ITALIC);
		    Font font8 = new Font(bfChinese, 8 ,Font.ITALIC);
		    Font fontB20 = new Font(bfChinese, 20 ,Font.BOLDITALIC);

			PdfPCell cell = null;

			int colDataHeight = 15;
			int colDataBorder = 1;

			int currentPageNum = 1; //目前頁數

			int pageMaxSize = 20; //每頁最多幾筆資料

			int totalDataCount = dataList.size(); //資料總筆數

			int totalPage = 0; //總頁數
			if (totalDataCount%pageMaxSize==0) {
				totalPage = totalDataCount/pageMaxSize;
			} else {
				totalPage = totalDataCount/pageMaxSize + 1;
			}

			int[] colSizeArray = {20, 20, 80, 80, 80, 40, 40, 40, 50, 40};
			String[] colNameArray = {"排名", "出價",  "關鍵字", "分類", "廣告", "曝光數", "點選次數", "點選率", "平均點選出價", "費用"};

			int pageWidth = 0; //表格總寬度
			for (int i=0; i<colSizeArray.length; i++) {
				pageWidth += colSizeArray[i];
			}

			double pvSum_all = 0; //PV總和加總
			double clkSum_all = 0; //Click總和加總
			double priceSum_all = 0; //價格總和加總

			for (int i=0; i<totalPage; i++) {

				int currentDataAmount = 0; //目前頁數的資料筆數
				if (currentPageNum!=totalPage) { //不是最後一頁
					currentDataAmount = pageMaxSize;
				} else { //最後一頁
					currentDataAmount = totalDataCount - pageMaxSize*(totalPage-1);
				}

				//title
				this.writeKeywordOfferPriceReportTitle(doc, font10, fontB20,
						Integer.toString(currentPageNum), pageWidth, colSizeArray, colNameArray);

				//query condition
				if (i==0) {
					this.writeKeywordOfferPriceReportQueryCondition(doc, font10, pageWidth,
							startDate, endDate, keywordType, searchText, displayCount);
				}

				//column name
				this.writeReportColName(doc, font10, pageWidth, colSizeArray, colNameArray);

				//content
				PdfPTable table = new PdfPTable(colSizeArray.length);
				table.setTotalWidth(pageWidth);
				table.setWidths(colSizeArray);

				for (int k=0; k<currentDataAmount; k++) {

					int dataIndex = (currentPageNum-1)*pageMaxSize + k;
					KeywordReportVO vo = dataList.get(dataIndex);

					//排名
					cell = new PdfPCell(new Paragraph(Integer.toString(dataIndex + 1), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//出價
					cell = new PdfPCell(new Paragraph(vo.getOfferPrice(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//關鍵字
					cell = new PdfPCell(new Paragraph(vo.getKeyword(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//活動
					cell = new PdfPCell(new Paragraph(vo.getAdGroup(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//廣告
					cell = new PdfPCell(new Paragraph(vo.getAdAction(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//曝光數
					cell = new PdfPCell(new Paragraph(vo.getKwPvSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					pvSum_all += numFormat.parse(vo.getKwPvSum()).doubleValue();

					//點選次數
					cell = new PdfPCell(new Paragraph(vo.getKwClkSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					clkSum_all += numFormat.parse(vo.getKwClkSum()).doubleValue();

					//點選率
					cell = new PdfPCell(new Paragraph(vo.getKwClkRate(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//平均點選出價
					cell = new PdfPCell(new Paragraph(vo.getClkPriceAvg(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//費用
					cell = new PdfPCell(new Paragraph(vo.getKwPriceSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					priceSum_all += numFormat.parse(vo.getKwPriceSum()).doubleValue();
				}

				//footer
				if (currentPageNum==totalPage) { //最後一頁才會出現統計數據

					//總計
					cell = new PdfPCell(new Paragraph("總計", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//填空白
					for (int x=0; x<4; x++) {
						cell = new PdfPCell(new Paragraph("", font8));
						cell.setMinimumHeight(colDataHeight);
						cell.setBorderWidth(0);
						table.addCell(cell);
					}

					//曝光數總和
					cell = new PdfPCell(new Paragraph(numFormat.format(pvSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//點選次數總和
					cell = new PdfPCell(new Paragraph(numFormat.format(clkSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//點選率
					String strClkRate = df.format((clkSum_all / pvSum_all)*100) + "%";
					cell = new PdfPCell(new Paragraph(strClkRate, font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//平均點選出價
					String strClkPriceAvg = df.format(priceSum_all / clkSum_all);
					cell = new PdfPCell(new Paragraph(strClkPriceAvg, font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//費用總和
					cell = new PdfPCell(new Paragraph(numFormat.format(priceSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);
				}

				doc.add(table);

				//換頁
				doc.newPage();

				currentPageNum++;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void writeKeywordOfferPriceReportTitle(Document doc, Font font10, Font fontB20,
			String pageNum,	int pageWidth, int[] colSizeArray, String[] colNameArray) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		int[] colSizeTitle = {pageWidth-120, 50, 70};

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);
		table.setWidths(colSizeTitle);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("關鍵字出價報表", fontB20));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorderWidth(0);
		cell.setRowspan(3);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("列印日期：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(sdf.format(new Date()), font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("頁次：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(pageNum, font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		doc.add(table);
	}

	private void writeKeywordOfferPriceReportQueryCondition(Document doc, Font font10, int pageWidth,
			String startDate, String endDate, String keywordType,
			String searchText, String displayCount) throws Exception {

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);

		int colHeight = 20;

		int[] colSizeArray = {200, 200, 200};

		table.setWidths(colSizeArray);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("廣告形式： " + keywordType, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("搜尋關鍵字： " + searchText, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("顯示前： " + displayCount + " 名", font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("查詢日期： " + startDate + " ~ " + endDate, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorderWidth(0);
		cell.setColspan(3);
		table.addCell(cell);

		doc.add(table);
	}

	/**
	 * 帳戶交易明細報表
	 */
	public void prepareTransDetailReportPdf(Document doc, List<PfpTransDetailVO> dataList,
			String startDate, String endDate, String customerInfo, String pfdCustomerInfoIdText,
			String payTypeText) {

		try {

			java.io.File fontFile = new java.io.File(fontPath);
			logger.info(">>> " + fontFile.getAbsolutePath() + ", " + fontFile.exists());
			BaseFont bfChinese = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED); 

			Font font10 = new Font(bfChinese, 10 ,Font.ITALIC);
		    Font font8 = new Font(bfChinese, 8 ,Font.ITALIC);
		    Font fontB20 = new Font(bfChinese, 20 ,Font.BOLDITALIC);

			PdfPCell cell = null;

			int colDataHeight = 15;
			int colDataBorder = 1;

			int currentPageNum = 1; //目前頁數

			int pageMaxSize = 20; //每頁最多幾筆資料

			int totalDataCount = dataList.size(); //資料總筆數

			int totalPage = 0; //總頁數
			if (totalDataCount%pageMaxSize==0) {
				totalPage = totalDataCount/pageMaxSize;
			} else {
				totalPage = totalDataCount/pageMaxSize + 1;
			}

			int[] colSizeArray = {80, 80, 80, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60};
			String[] colNameArray = {"交易日期", "帳戶名稱", "帳戶編號", "儲值金額", "營業稅", "廣告花費",
					"惡意點擊費用", "退款", "禮金贈送", "帳戶餘額", "經銷歸屬", "業務歸屬", "付款方式"};

			int pageWidth = 0; //表格總寬度
			for (int i=0; i<colSizeArray.length; i++) {
				pageWidth += colSizeArray[i];
			}

			int totalAddMoney = 0; //帳戶儲值加總
			int totalTax = 0; //稅額加總
			int totalSpend = 0; //廣告花費加總
			int totalInvalidCost = 0; //惡意點擊費用加總
			int totalRefund = 0; //退款加總
			int totalGift = 0; //禮金加總

			for (int i=0; i<totalPage; i++) {

				int currentDataAmount = 0; //目前頁數的資料筆數
				if (currentPageNum!=totalPage) { //不是最後一頁
					currentDataAmount = pageMaxSize;
				} else { //最後一頁
					currentDataAmount = totalDataCount - pageMaxSize*(totalPage-1);
				}

				//title
				this.writeTransDetailReportTitle(doc, font10, fontB20,
						Integer.toString(currentPageNum), pageWidth, colSizeArray, colNameArray);

				//query condition
				if (i==0) {
					this.writeTransDetailReportQueryCondition(doc, font10, pageWidth,
							startDate, endDate, customerInfo, pfdCustomerInfoIdText, payTypeText);
				}

				//column name
				this.writeReportColName(doc, font10, pageWidth, colSizeArray, colNameArray);

				//content
				PdfPTable table = new PdfPTable(colSizeArray.length);
				table.setTotalWidth(pageWidth);
				table.setWidths(colSizeArray);

				for (int k=0; k<currentDataAmount; k++) {

					int dataIndex = (currentPageNum-1)*pageMaxSize + k;
					PfpTransDetailVO vo = dataList.get(dataIndex);

					//交易日期
					cell = new PdfPCell(new Paragraph(vo.getTransDate(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//帳戶名稱
					cell = new PdfPCell(new Paragraph(vo.getCustName(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//帳戶編號
					cell = new PdfPCell(new Paragraph(vo.getCustId(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//儲值金額
					cell = new PdfPCell(new Paragraph(Integer.toString(vo.getAdd()), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					totalAddMoney += vo.getAdd();

					//營業稅
					cell = new PdfPCell(new Paragraph(Integer.toString(vo.getTax()), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					totalTax += vo.getTax();

					//廣告花費
					cell = new PdfPCell(new Paragraph(Integer.toString(vo.getSpend()), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					totalSpend += vo.getSpend();

					//惡意點擊費用
					cell = new PdfPCell(new Paragraph(Integer.toString(vo.getInvalid()), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					totalInvalidCost += vo.getInvalid();

					//退款
					cell = new PdfPCell(new Paragraph(Integer.toString(vo.getRefund()), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					totalRefund += vo.getRefund();

					//禮金贈送
					cell = new PdfPCell(new Paragraph(Integer.toString(vo.getGift()), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					totalGift += vo.getGift();

					//帳戶餘額
					cell = new PdfPCell(new Paragraph(Integer.toString(vo.getRemain()), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//經銷歸屬
					cell = new PdfPCell(new Paragraph(vo.getPfdCustInfoName(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//業務歸屬
					cell = new PdfPCell(new Paragraph(vo.getPfdUserName(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					//付款方式
					cell = new PdfPCell(new Paragraph(vo.getPayType(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
				}

				//footer
				if (currentPageNum==totalPage) { //最後一頁才會出現統計數據

					//總計
					cell = new PdfPCell(new Paragraph("總計", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//填空白
					for (int x=0; x<2; x++) {
						cell = new PdfPCell(new Paragraph("", font8));
						cell.setMinimumHeight(colDataHeight);
						cell.setBorderWidth(0);
						table.addCell(cell);
					}

					//帳戶儲值加總
					cell = new PdfPCell(new Paragraph(numFormat.format(totalAddMoney), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//稅額加總
					cell = new PdfPCell(new Paragraph(numFormat.format(totalTax), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//廣告花費加總
					cell = new PdfPCell(new Paragraph(numFormat.format(totalSpend), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//惡意點擊費用加總
					cell = new PdfPCell(new Paragraph(numFormat.format(totalInvalidCost), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//退款加總
					cell = new PdfPCell(new Paragraph(numFormat.format(totalRefund), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//禮金加總
					cell = new PdfPCell(new Paragraph(numFormat.format(totalGift), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//填空白
					cell = new PdfPCell(new Paragraph("", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//填空白
					cell = new PdfPCell(new Paragraph("", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//填空白
					cell = new PdfPCell(new Paragraph("", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//填空白
					cell = new PdfPCell(new Paragraph("", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(0);
					table.addCell(cell);

				}

				doc.add(table);

				//換頁
				doc.newPage();

				currentPageNum++;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void writeTransDetailReportTitle(Document doc, Font font10, Font fontB20,
			String pageNum,	int pageWidth, int[] colSizeArray, String[] colNameArray) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		int[] colSizeTitle = {pageWidth-200, 80, 120};

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);
		table.setWidths(colSizeTitle);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("帳戶交易明細報表", fontB20));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorderWidth(0);
		cell.setRowspan(3);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("列印日期：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(sdf.format(new Date()), font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("頁次：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(pageNum, font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		doc.add(table);
	}

	private void writeTransDetailReportQueryCondition(Document doc, Font font10, int pageWidth,
			String startDate, String endDate, String customerInfo,
			String pfdCustomerInfoIdText, String payTypeText) throws Exception {

		int colHeight = 20;

		int[] colSizeArray = {200, 200};

		PdfPTable table = new PdfPTable(2);
		table.setTotalWidth(pageWidth);
		table.setWidths(colSizeArray);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("查詢日期： " + startDate + " ~ " + endDate, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("使用者帳戶： " + customerInfo, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorderWidth(0);
		table.addCell(cell);

		doc.add(table);

		PdfPTable table2 = new PdfPTable(2);
		table2.setTotalWidth(pageWidth);
		table2.setWidths(colSizeArray);

		PdfPCell cell2 = null;

		cell2 = new PdfPCell(new Paragraph("經銷歸屬： " + pfdCustomerInfoIdText, font10));
		cell2.setMinimumHeight(colHeight);
		cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell2.setVerticalAlignment(Element.ALIGN_TOP);
		cell2.setBorderWidth(0);
		table2.addCell(cell2);

		cell2 = new PdfPCell(new Paragraph("付款方式： " + payTypeText, font10));
		cell2.setMinimumHeight(colHeight);
		cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell2.setVerticalAlignment(Element.ALIGN_TOP);
		cell2.setBorderWidth(0);
		table2.addCell(cell2);

		doc.add(table2);
	}

	/**
	 * 未達每日花費上限
	 */
	public void prepareUnReachBudgetReportPdf(Document doc, List<AdActionReportVO> dataList,
			String startDate, String endDate) {

		try {

			java.io.File fontFile = new java.io.File(fontPath);
			logger.info(">>> " + fontFile.getAbsolutePath() + ", " + fontFile.exists());
			BaseFont bfChinese = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED); 

			Font font10 = new Font(bfChinese, 10 ,Font.ITALIC);
		    Font font8 = new Font(bfChinese, 8 ,Font.ITALIC);
		    Font fontB20 = new Font(bfChinese, 20 ,Font.BOLDITALIC);

			PdfPCell cell = null;

			int colDataHeight = 15;
			int colDataBorder = 1;

			int currentPageNum = 1; //目前頁數

			int pageMaxSize = 20; //每頁最多幾筆資料

			int totalDataCount = dataList.size(); //資料總筆數

			int totalPage = 0; //總頁數
			if (totalDataCount%pageMaxSize==0) {
				totalPage = totalDataCount/pageMaxSize;
			} else {
				totalPage = totalDataCount/pageMaxSize + 1;
			}

			int[] colSizeArray = {30, 60, 60, 70, 60, 40, 40, 40, 40, 55, 40, 40};
			String[] colNameArray = {"排名", "會員帳號", "帳戶名稱", "廣告活動", "每日花費上限", "調控金額", "曝光數", "點選次數", "點選率", "平均點選出價", "費用", "未達費用"};

			int pageWidth = 0; //表格總寬度
			for (int i=0; i<colSizeArray.length; i++) {
				pageWidth += colSizeArray[i];
			}

			double pvSum_all = 0; //PV總和加總
			double clkSum_all = 0; //Click總和加總
			double priceSum_all = 0; //價格總和加總
			double unReachPriceSum_all = 0; //未達費用加總

			for (int i=0; i<totalPage; i++) {

				int currentDataAmount = 0; //目前頁數的資料筆數
				if (currentPageNum!=totalPage) { //不是最後一頁
					currentDataAmount = pageMaxSize;
				} else { //最後一頁
					currentDataAmount = totalDataCount - pageMaxSize*(totalPage-1);
				}

				//title
				this.writeUnReachBudgetReportTitle(doc, font10, fontB20,
						Integer.toString(currentPageNum), pageWidth, colSizeArray, colNameArray);

				//query condition
				if (i==0) {
					this.writeUnReachBudgetReportQueryCondition(doc, font10, pageWidth,	startDate, endDate);
				}

				//column name
				this.writeReportColName(doc, font10, pageWidth, colSizeArray, colNameArray);

				//content
				PdfPTable table = new PdfPTable(colSizeArray.length);
				table.setTotalWidth(pageWidth);
				table.setWidths(colSizeArray);

				for (int k=0; k<currentDataAmount; k++) {

					int dataIndex = (currentPageNum-1)*pageMaxSize + k;
					AdActionReportVO vo = dataList.get(dataIndex);

					//排名
					cell = new PdfPCell(new Paragraph(Integer.toString(dataIndex + 1), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//會員帳號
					cell = new PdfPCell(new Paragraph(vo.getCustomerInfoId(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//帳戶名稱
					cell = new PdfPCell(new Paragraph(vo.getCustomerInfoName(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//廣告活動
					cell = new PdfPCell(new Paragraph(vo.getAdActionName(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//每日花費上限
					cell = new PdfPCell(new Paragraph(vo.getMaxPrice(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					//調控金額
					cell = new PdfPCell(new Paragraph(vo.getAdActionControlPrice(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//曝光數
					cell = new PdfPCell(new Paragraph(vo.getPvSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					pvSum_all += numFormat.parse(vo.getPvSum()).doubleValue();

					//點選次數
					cell = new PdfPCell(new Paragraph(vo.getClkSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					clkSum_all += numFormat.parse(vo.getClkSum()).doubleValue();

					//點選率
					cell = new PdfPCell(new Paragraph(vo.getClkRate(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//平均點選出價
					cell = new PdfPCell(new Paragraph(vo.getClkPriceAvg(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);

					//費用
					cell = new PdfPCell(new Paragraph(vo.getPriceSum(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					priceSum_all += numFormat.parse(vo.getPriceSum()).doubleValue();

					//未達費用
					cell = new PdfPCell(new Paragraph(vo.getUnReachPrice(), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					unReachPriceSum_all += numFormat.parse(vo.getUnReachPrice()).doubleValue();
				}

				//footer
				if (currentPageNum==totalPage) { //最後一頁才會出現統計數據

					//總計
					cell = new PdfPCell(new Paragraph("總計", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//填空白
					for (int x=0; x<5; x++) {
						cell = new PdfPCell(new Paragraph("", font8));
						cell.setMinimumHeight(colDataHeight);
						cell.setBorderWidth(0);
						table.addCell(cell);
					}

					//曝光數總和
					cell = new PdfPCell(new Paragraph(numFormat.format(pvSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//點選次數總和
					cell = new PdfPCell(new Paragraph(numFormat.format(clkSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//點選率
					String strClkRate = df.format((clkSum_all / pvSum_all)*100) + "%";
					cell = new PdfPCell(new Paragraph(strClkRate, font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//平均點選出價
					String strClkPriceAvg = df.format(priceSum_all / clkSum_all);
					cell = new PdfPCell(new Paragraph(strClkPriceAvg, font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//費用總和
					cell = new PdfPCell(new Paragraph(numFormat.format(priceSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);

					//未達費用總和
					cell = new PdfPCell(new Paragraph(numFormat.format(unReachPriceSum_all), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setBorderWidth(0);
					table.addCell(cell);
				}

				doc.add(table);

				//換頁
				doc.newPage();

				currentPageNum++;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void writeUnReachBudgetReportTitle(Document doc, Font font10, Font fontB20,
			String pageNum,	int pageWidth, int[] colSizeArray, String[] colNameArray) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		int[] colSizeTitle = {pageWidth-190, 100, 90};

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);
		table.setWidths(colSizeTitle);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("未達每日花費上限報表", fontB20));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorderWidth(0);
		cell.setRowspan(3);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("列印日期：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(sdf.format(new Date()), font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("頁次：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(pageNum, font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		doc.add(table);
	}

	private void writeUnReachBudgetReportQueryCondition(Document doc, Font font10, int pageWidth,
			String startDate, String endDate) throws Exception {

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);

		int colHeight = 20;

		int[] colSizeArray = {200, 200, 200};

		table.setWidths(colSizeArray);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("查詢日期： " + startDate + " ~ " + endDate, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorderWidth(0);
		cell.setColspan(3);
		table.addCell(cell);

		doc.add(table);
	}

	/**
	 * 行動裝置成效排名
	 */
	public void prepareAdMobileOsReportPdf(Document doc, List<AdActionReportVO> dataList,
			String startDate, String endDate, String adMobileOS, String customerInfoId) {

		try {

			java.io.File fontFile = new java.io.File(fontPath);
			logger.info(">>> " + fontFile.getAbsolutePath() + ", " + fontFile.exists());
			BaseFont bfChinese = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED); 

			Font font10 = new Font(bfChinese, 10 ,Font.ITALIC);
		    Font font8 = new Font(bfChinese, 8 ,Font.ITALIC);
		    Font fontB20 = new Font(bfChinese, 20 ,Font.BOLDITALIC);

			PdfPCell cell = null;

			int colDataHeight = 15;
			int colDataBorder = 1;

			int[] colSizeArray = {30, 140, 50, 50, 40, 40, 50, 50, 40};
			String[] colNameArray = {"排名", "帳戶名稱" ,"裝置系統", "曝光數", "點選次數", "點選率", "無效點選次數", "平均點選出價", "費用"};
			
			int pageMaxSize = 40; //每頁最多幾筆資料

			int pageWidth = 0; //表格總寬度
			for (int i=0; i<colSizeArray.length; i++) {
				pageWidth += colSizeArray[i];
			}
			System.out.println("pageWidth = " + pageWidth);

			int rank = 0;
			String tmp_customerInfoId = "";
			
			int pageNo = 1;			// 頁次
			int pageAmount = 0;		// 頁面筆數
			int voNo = 0;			// 紀錄目前資料編號

			// pdf 內容
			PdfPTable table = new PdfPTable(colSizeArray.length);
			table.setTotalWidth(pageWidth);
			table.setWidths(colSizeArray);

			// 讀取資料，寫入 pdf
			for(AdActionReportVO vo:dataList) {
				voNo ++;	// 紀錄目前筆數
				int rowspan = vo.getRowspan();	// 每筆customerId 橫跨的列數				
				// 特別處理第一頁的 pdf 標題
				if(pageNo == 1 && pageAmount == 0) {
					//title
					this.writeAdMobileOsReportTitle(doc, font10, fontB20,
							Integer.toString(pageNo), pageWidth, colSizeArray, colNameArray);

					String modifyAdMobileOS = adMobileOS;
					if(adMobileOS != null && adMobileOS.equals("all")) {
						modifyAdMobileOS = "全部";
					} else if(adMobileOS != null && adMobileOS.equals("")){
						modifyAdMobileOS = "其他";
					}
					//query condition
					this.writeAdMobileOsReportQueryCondition(doc, font10, pageWidth,
							startDate, endDate, modifyAdMobileOS, customerInfoId);

					//column name
					this.writeReportColName(doc, font10, pageWidth, colSizeArray, colNameArray);
				}

				// 本頁面筆數如果加了 customerInfoId 橫跨的列數後，超過一頁最大筆數，則將資料新增至下一頁
				if((pageAmount + rowspan) > pageMaxSize) {
					// 將 pdf table 寫入 doc 文件中
					doc.add(table);
					//新增下一頁
					doc.newPage();
					// 頁數 + 1
					pageNo ++;
					//頁面筆數重新計算
					pageAmount = 0;

					//title
					this.writeAdMobileOsReportTitle(doc, font10, fontB20,
							Integer.toString(pageNo), pageWidth, colSizeArray, colNameArray);

					//column name
					this.writeReportColName(doc, font10, pageWidth, colSizeArray, colNameArray);

					// 重新設定 pdf table
					table = new PdfPTable(colSizeArray.length);
					table.setTotalWidth(pageWidth);
					table.setWidths(colSizeArray);
					
					//System.out.println("pageNo = " + pageNo);
					//System.out.println();
				}
				pageAmount++;

				//System.out.println("排名" + rank + " => " + tmp_customerInfoId + "; " + vo.getCustomerInfoId() + "; " + vo.getCustomerInfoName());
				// 相同 customerInfoId 合併為一格，用 rowspan 處理
				if(!tmp_customerInfoId.equals(vo.getCustomerInfoId())) {
					tmp_customerInfoId = vo.getCustomerInfoId();
					rank ++;
					//排名
					cell = new PdfPCell(new Paragraph(Integer.toString(rank), font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setRowspan(vo.getRowspan());
					table.addCell(cell);

					//帳戶名稱
					cell = new PdfPCell(new Paragraph(vo.getCustomerInfoName()+"("+vo.getCustomerInfoId()+")", font8));
					cell.setMinimumHeight(colDataHeight);
					cell.setBorderWidth(colDataBorder);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setRowspan(vo.getRowspan());
					table.addCell(cell);
				}

				//裝置系統
				String adOs = vo.getAdOs();
				if(vo.getAdOs().equals(""))		adOs = "其他";
				cell = new PdfPCell(new Paragraph(adOs, font8));
				cell.setMinimumHeight(colDataHeight);
				cell.setBorderWidth(colDataBorder);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);
				
				//曝光數
				cell = new PdfPCell(new Paragraph(vo.getPvSum(), font8));
				cell.setMinimumHeight(colDataHeight);
				cell.setBorderWidth(colDataBorder);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);

				//點選次數
				cell = new PdfPCell(new Paragraph(vo.getClkSum(), font8));
				cell.setMinimumHeight(colDataHeight);
				cell.setBorderWidth(colDataBorder);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);

				//點選率
				cell = new PdfPCell(new Paragraph(vo.getClkRate(), font8));
				cell.setMinimumHeight(colDataHeight);
				cell.setBorderWidth(colDataBorder);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);

				//無效點選次數
				cell = new PdfPCell(new Paragraph(vo.getInvalidClkSum(), font8));
				cell.setMinimumHeight(colDataHeight);
				cell.setBorderWidth(colDataBorder);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);

				//平均點選出價
				cell = new PdfPCell(new Paragraph(vo.getClkPriceAvg(), font8));
				cell.setMinimumHeight(colDataHeight);
				cell.setBorderWidth(colDataBorder);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);

				//費用
				cell = new PdfPCell(new Paragraph(vo.getPriceSum(), font8));
				cell.setMinimumHeight(colDataHeight);
				cell.setBorderWidth(colDataBorder);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);


				// 最後一筆的時候，要記得把資料加入 pdf doc 裡
				if(voNo == dataList.size()) {
					doc.add(table);
					//換頁
					doc.newPage();
					//System.out.println("pageNo = " + pageNo);
					//System.out.println();
				}
			}
			
//			// 逐筆列出資料
//			int currentPageNum = 1; //目前頁數	
//
//			int pageMaxSize = 40; //每頁最多幾筆資料
//
//			int totalDataCount = dataList.size(); //資料總筆數
//
//			int totalPage = 0; //總頁數
//			if (totalDataCount%pageMaxSize==0) {
//				totalPage = totalDataCount/pageMaxSize;
//			} else {
//				totalPage = totalDataCount/pageMaxSize + 1;
//			}
//
//			int[] colSizeArray = {30, 200, 80, 50, 40, 40, 50, 50, 40};
//			String[] colNameArray = {"序號", "帳戶名稱" ,"裝置系統", "曝光數", "點選次數", "點選率", "無效點選次數", "平均點選出價", "費用"};
//
//			int pageWidth = 0; //表格總寬度
//			for (int i=0; i<colSizeArray.length; i++) {
//				pageWidth += colSizeArray[i];
//			}
//			System.out.println("pageWidth = " + pageWidth);
//
//			for (int i=0; i<totalPage; i++) {
//
//				int currentDataAmount = 0; //目前頁數的資料筆數
//				if (currentPageNum!=totalPage) { //不是最後一頁
//					currentDataAmount = pageMaxSize;
//				} else { //最後一頁
//					currentDataAmount = totalDataCount - pageMaxSize*(totalPage-1);
//				}
//
//				//title
//				this.writeAdMobileOsReportTitle(doc, font10, fontB20,
//						Integer.toString(currentPageNum), pageWidth, colSizeArray, colNameArray);
//
//				//query condition
//				if (i==0) {
//					this.writeAdMobileOsReportQueryCondition(doc, font10, pageWidth,
//							startDate, endDate, adMobileOS, customerInfoId);
//				}
//
//				//column name
//				this.writeReportColName(doc, font10, pageWidth, colSizeArray, colNameArray);
//
//				System.out.println("pageWidth = " + pageWidth);
//				//content
//				PdfPTable table = new PdfPTable(colSizeArray.length);
//				table.setTotalWidth(pageWidth);
//				table.setWidths(colSizeArray);
//
//				for (int k=0; k<currentDataAmount; k++) {
//
//					int dataIndex = (currentPageNum-1)*pageMaxSize + k;
//					AdActionReportVO vo = dataList.get(dataIndex);
//					
//					//排名
//					System.out.println();
//					cell = new PdfPCell(new Paragraph(Integer.toString(dataIndex), font8));
//					cell.setMinimumHeight(colDataHeight);
//					cell.setBorderWidth(colDataBorder);
//					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//					table.addCell(cell);
//
//					//帳戶名稱
//					cell = new PdfPCell(new Paragraph(vo.getCustomerInfoName()+"("+vo.getCustomerInfoId()+")", font8));
//					cell.setMinimumHeight(colDataHeight);
//					cell.setBorderWidth(colDataBorder);
//					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//					table.addCell(cell);
//
//					//裝置系統
//					String adOs = vo.getAdOs();
//					if(vo.getAdOs().equals(""))		adOs = "其他";
//					cell = new PdfPCell(new Paragraph(adOs, font8));
//					cell.setMinimumHeight(colDataHeight);
//					cell.setBorderWidth(colDataBorder);
//					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//					table.addCell(cell);
//
//					//曝光數
//					cell = new PdfPCell(new Paragraph(vo.getPvSum(), font8));
//					cell.setMinimumHeight(colDataHeight);
//					cell.setBorderWidth(colDataBorder);
//					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//					table.addCell(cell);
//
//					//點選次數
//					cell = new PdfPCell(new Paragraph(vo.getClkSum(), font8));
//					cell.setMinimumHeight(colDataHeight);
//					cell.setBorderWidth(colDataBorder);
//					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//					table.addCell(cell);
//
//					//點選率
//					cell = new PdfPCell(new Paragraph(vo.getClkRate(), font8));
//					cell.setMinimumHeight(colDataHeight);
//					cell.setBorderWidth(colDataBorder);
//					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//					table.addCell(cell);
//
//					//無效點選次數
//					cell = new PdfPCell(new Paragraph(vo.getInvalidClkSum(), font8));
//					cell.setMinimumHeight(colDataHeight);
//					cell.setBorderWidth(colDataBorder);
//					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//					table.addCell(cell);
//
//					//平均點選出價
//					cell = new PdfPCell(new Paragraph(vo.getClkPriceAvg(), font8));
//					cell.setMinimumHeight(colDataHeight);
//					cell.setBorderWidth(colDataBorder);
//					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//					table.addCell(cell);
//
//					//費用
//					cell = new PdfPCell(new Paragraph(vo.getPriceSum(), font8));
//					cell.setMinimumHeight(colDataHeight);
//					cell.setBorderWidth(colDataBorder);
//					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//					table.addCell(cell);
//				}
//
//				doc.add(table);
//
//				//換頁
//				doc.newPage();
//
//				currentPageNum++;
//			}

			
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void writeAdMobileOsReportTitle(Document doc, Font font10, Font fontB20,
			String pageNum,	int pageWidth, int[] colSizeArray, String[] colNameArray) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		int[] colSizeTitle = {pageWidth-190, 100, 90};

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);
		table.setWidths(colSizeTitle);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("行動裝置成效排名", fontB20));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorderWidth(0);
		cell.setRowspan(3);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("列印日期：", font10));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(sdf.format(new Date()), font10));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("頁次：", font10));
		cell.setMinimumHeight(20);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph(pageNum, font10));
		cell.setMinimumHeight(20);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorderWidth(0);
		table.addCell(cell);

		doc.add(table);
	}

	private void writeAdMobileOsReportQueryCondition(Document doc, Font font10, int pageWidth,
			String startDate, String endDate, String adMobileOs, String customerInfoId) throws Exception {

		PdfPTable table = new PdfPTable(3);
		table.setTotalWidth(pageWidth);

		int colHeight = 20;

		int[] colSizeArray = {200, 200, 200};

		table.setWidths(colSizeArray);

		PdfPCell cell = null;

		cell = new PdfPCell(new Paragraph("裝置系統： " + adMobileOs, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("帳戶序號： " + customerInfoId, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setColspan(2);
		cell.setBorderWidth(0);
		table.addCell(cell);

		cell = new PdfPCell(new Paragraph("查詢日期： " + startDate + " ~ " + endDate, font10));
		cell.setMinimumHeight(colHeight);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setBorderWidth(0);
		cell.setColspan(3);
		table.addCell(cell);

		doc.add(table);
	}
}
