/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pchome.akbadm.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.fileupload.FileItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 *
 * @author Kyle
 */
public class KTools
{

	private Logger log = LogManager.getRootLogger();

	private static final KTools instance = new KTools();

	private KTools()
	{
	}

	public static KTools getInstance()
	{
		return instance;
	}

	// 根據url取得IP
	public String getIpByUrlString(String surl) throws Exception
	{
		String domainUrl = null;
		if (surl == null)
		{
			return null;
		}
		else
		{
			URL url = new URL(surl);
			InetAddress address = InetAddress.getByName(url.getHost());
			return address.toString();
		}
	}

	/**
	 * 根据URL获取domain
	 * 
	 * @param url
	 * @return
	 */
	public String getDomainByUrl(String url)
	{

		String domainUrl = null;
		if (url == null)
		{
			return null;
		}
		else
		{
			Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv|tw)", Pattern.CASE_INSENSITIVE);
			Matcher matcher = p.matcher(url);
			while (matcher.find())
			{
				domainUrl = matcher.group();
			}
			return domainUrl;
		}
	}

	public String HtmlToSql(String str)// tosql
	{
		str = str.replaceAll("\r\n", "\n");
		str = str.replaceAll("'", "&#39;");
		str = str.replaceAll("\"", "&#34;");
		str = str.replaceAll("<", "&#60;");
		str = str.replaceAll(">", "&#62;");
		return str;
	}

	public String SqlToHtml(String str)// toHTML
	{
		str = str.replaceAll("\n", "\r\n");
		str = str.replaceAll("&#39;", "'");
		str = str.replaceAll("&#34;", "\"");
		str = str.replaceAll("&#60;", "<");
		str = str.replaceAll("&#62;", ">");
		return str;
	}

	// 字串前補0
	// 原字串 ， 總共位數
	public String add0(String Source, int i_0Count)
	{
		// int i_0Count = Integer.parseInt(_0Count);
		if (Source.length() < i_0Count)
		{
			int SourceLength = Source.length();

			for (int i = SourceLength; i < i_0Count; i++)
			{
				Source = "0" + Source;
			}
		}
		return Source;
	}

	// 取得今天日期
	public String getTodayFormat(String formattype, String dashtype)
	{
		return this.FormatDate(this.getDayTime(formattype), dashtype);
	}

	// 取得檔案的副檔名
	public String getAuxName(String FileAllName, String hasDot)
	{
		String auxFileName = "";

		if ("Y".equals(hasDot))
		{
			auxFileName = FileAllName.substring(FileAllName.indexOf('.'), FileAllName.length());
		}
		else
		{
			auxFileName = FileAllName.substring(FileAllName.indexOf('.') + 1, FileAllName.length());
		}

		return auxFileName;
	}

	// FORMAT日期格式
	public String FormatDate(String date, String dashtype)
	{
		String returner = "";

		if ("".equals(date) || date == null)
		{
			returner = "";
		}
		else
		{
			returner = date.substring(0, 4) + dashtype + date.substring(4, 6) + dashtype + date.substring(6, 8);
		}

		return returner;
	}

	// PASSNULL...BJ4
	public String passNull(String str)
	{
		String returner = str != null ? str : "";
		return returner;
	}

	/**
	 * 網頁版多目標資料庫異動 取得COLS名稱List
	 *
	 * @param sqltext
	 * @return
	 */
	public List<String> getMutiDBQryColsList(String sqltext)
	{
		List<String> titleList = new ArrayList<String>();
		int selint = sqltext.indexOf("select");
		int fromint = sqltext.indexOf("from");
		String cols = sqltext.substring(selint + 7, fromint - 1);
		cols = cols.toLowerCase().trim();
		String[] arrCols = cols.split(",");

		if (arrCols[0].indexOf("top ") == 0)
		{
			arrCols[0] = arrCols[0].substring(arrCols[0].trim().lastIndexOf(" "));
		}

		for (String str : arrCols)
		{
			titleList.add(str.trim());
		}

		return titleList;
	}

	/**
	 * 日期加N天
	 *
	 * @param srcDate
	 * @param adds
	 * @return
	 */
	public String AddDays(String srcDate, int adds)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		String rs = "";

		if (srcDate.indexOf("/") < 0)
		{
			srcDate = this.FormatDate(srcDate, "/");
		}

		try
		{
			DateFormat df = DateFormat.getDateInstance();
			Date date = df.parse(srcDate);
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_YEAR, adds);

			rs = sdf.format(calendar.getTime());
		}
		catch (Exception ex)
		{
			log.error(ex);
		}

		return rs;
	}

	/**
	 * 日期加N月
	 *
	 * @param srcDate
	 * @param addDays
	 * @return
	 */
	public String AddMonth(String srcDate, int adds)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		String rs = "";

		if (srcDate.indexOf("/") < 0)
		{
			srcDate = this.FormatDate(srcDate, "/");
		}

		try
		{
			DateFormat df = DateFormat.getDateInstance();
			Date date = df.parse(srcDate);
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, adds);

			rs = sdf.format(calendar.getTime());
		}
		catch (Exception ex)
		{
			log.error(ex);
		}

		return rs;
	}

	/**
	 * 日期加N年
	 *
	 * @param srcDate
	 * @param addDays
	 * @return
	 */
	public String AddYear(String srcDate, int adds)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		String rs = "";

		if (srcDate.indexOf("/") < 0)
		{
			srcDate = this.FormatDate(srcDate, "/");
		}

		try
		{
			DateFormat df = DateFormat.getDateInstance();
			Date date = df.parse(srcDate);
			calendar.setTime(date);
			calendar.add(Calendar.YEAR, adds);

			rs = sdf.format(calendar.getTime());
		}
		catch (Exception ex)
		{
			log.error(ex);
		}

		return rs;
	}

	// check是否為數字
	public boolean isNum(String str)
	{
		for (int i = str.length(); --i >= 0;)
		{
			if (!Character.isDigit(str.charAt(i)))
			{
				return false;
			}
		}
		return true;
	}

	// 取得現在日期時間
	// 參數:
	// yyyyMMdd
	// yyyy/MM/dd
	// HHmmssSSS
	// HH:mm:ss:SSS
	public String getDayTime(String formattype)
	{
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(formattype);
		String today = sdf.format(date);

		return today;
	}

	public int checkFileCount(String fromPath)
	{
		return new File(fromPath).listFiles().length;
	}

	public String Array2String(String[] arr)
	{
		String str = "";

		if (arr.length > 0)
		{
			for (int i = 0; i < arr.length; i++)
			{
				if (i != 0)
				{
					str += ",";
				}
				str += arr[i];
			}
		}

		return str;
	}

	public String List2String(List<Object> arr)
	{
		String str = "";

		if (arr.size() > 0)
		{
			for (int i = 0; i < arr.size(); i++)
			{
				if (i != 0)
				{
					str += ",";
				}
				str += arr.get(i);
			}
		}

		return str;
	}

	public String ConvertImg2Any(String fromPath, String savePath, int newWidth, int newHeight, boolean chkRealSize, String imgType)
	{
		InputStream Uploadimg = null;
		BufferedImage imageS = null;
		BufferedImage imageR = null;
		File fileOut = null;
		String resultMsg = "";

		try
		{
			Uploadimg = new FileInputStream(fromPath);
			imageS = ImageIO.read(Uploadimg);

			double sw = imageS.getWidth();
			double sh = imageS.getHeight();
			double rw, rh;

			if (chkRealSize == true)
			{
				if (sw > sh)
				{
					rw = (newWidth / sw) * sw;
					rh = (newWidth / sw) * sh;
				}
				else
				{
					rw = (newHeight / sh) * sw;
					rh = (newHeight / sh) * sh;
				}
			}
			else
			{
				rw = newWidth;
				rh = newHeight;
			}

			Image new_img = imageS.getScaledInstance((int) rw, (int) rh, Image.SCALE_SMOOTH);

			imageR = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

			Graphics2D g = imageR.createGraphics();
			imageR = g.getDeviceConfiguration().createCompatibleImage(newWidth, newHeight, Transparency.TRANSLUCENT);
			g = imageR.createGraphics();

			g.drawImage(new_img, (int) ((newWidth / 2) - (rw / 2)), (int) ((newHeight / 2) - (rh / 2)), (int) rw, (int) rh, null);

			// PNG轉JPG 才會用到Alpha圖層
			// g.setComposite(AlphaComposite.Src);
			// g.drawImage(new_img, 0, 0, (int) rw, (int) rh, null);

			// 關閉圖片
			g.dispose();
			imageS.flush();
			imageR.flush();
			new_img.flush();
			Uploadimg.close();

			String FileName = fromPath.substring(fromPath.lastIndexOf("/") + 1).trim();
			String headFileName = FileName.substring(0, FileName.lastIndexOf("."));
			String auxFileName = imgType;

			fileOut = new File(savePath);
			if (!fileOut.exists())
			{
				fileOut.mkdirs();
				log.info("資料匣" + savePath + "已創建");
			}

			fileOut = new File(savePath + headFileName + auxFileName);
			ImageIO.write(imageR, imgType, fileOut);

			log.debug("Change Img Size OK");
		}
		catch (Exception ex)
		{
			log.error("chgImagePx Has Error = " + ex);
			resultMsg = "" + ex;
		}
		return resultMsg;
	}

	public boolean ConvertImg2PNG(String fromPath, String savePath, int newWidth, int newHeight, boolean chkRealSize)
	{
		BufferedImage imageS = null;
		BufferedImage imageR = null;
		File fileOut = null;
		boolean check = true;

		try
		{
			InputStream Uploadimg = new FileInputStream(fromPath);
			imageS = ImageIO.read(Uploadimg);

			double sw = imageS.getWidth();
			double sh = imageS.getHeight();
			double rw, rh;

			if (chkRealSize == true)
			{
				if (sw > sh)
				{
					rw = (newWidth / sw) * sw;
					rh = (newWidth / sw) * sh;
				}
				else
				{
					rw = (newHeight / sh) * sw;
					rh = (newHeight / sh) * sh;
				}
			}
			else
			{
				rw = newWidth;
				rh = newHeight;
			}

			Image new_img = imageS.getScaledInstance((int) rw, (int) rh, Image.SCALE_SMOOTH);

			imageR = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

			Graphics2D g = imageR.createGraphics();
			imageR = g.getDeviceConfiguration().createCompatibleImage(newWidth, newHeight, Transparency.TRANSLUCENT);
			g = imageR.createGraphics();

			g.drawImage(new_img, (int) ((newWidth / 2) - (rw / 2)), (int) ((newHeight / 2) - (rh / 2)), (int) rw, (int) rh, null);

			// PNG轉JPG 才會用到Alpha圖層
			// g.setComposite(AlphaComposite.Src);
			// g.drawImage(new_img, 0, 0, (int) rw, (int) rh, null);

			// 關閉圖片
			g.dispose();
			imageS.flush();
			imageR.flush();
			new_img.flush();
			Uploadimg.close();

			String FileName = fromPath.substring(fromPath.lastIndexOf("/") + 1).trim();
			String headFileName = FileName.substring(0, FileName.lastIndexOf("."));
			String auxFileName = ".PNG";

			fileOut = new File(savePath);
			if (!fileOut.exists())
			{
				fileOut.mkdirs();
				log.info("資料匣" + savePath + "已創建");
			}

			fileOut = new File(savePath + "SQr" + newWidth + "_" + headFileName + auxFileName);
			ImageIO.write(imageR, "PNG", fileOut);

			log.debug("Change Img Size OK");
		}
		catch (Exception ex)
		{
			log.error("chgImagePx Has Error = " + ex);
			check = false;
		}
		return check;
	}

	// 改變圖片大小
	// 上傳檔案用
	// 直接寫入
	// fromPath : 原始圖檔路徑
	// savePath : 輸出檔案路徑(不含檔名)
	// 希望改變的寬高
	// igoRealSize : 判斷圖片比例? Y/N
	public boolean chgImagePx2(String fromPath, String savePath, int newWidth, int newHeight, boolean chkRealSize)
	{
		BufferedImage imageS = null;
		BufferedImage imageR = null;
		File fileOut = null;
		Color backColor = Color.white;
		boolean check = true;

		try
		{
			InputStream Uploadimg = new FileInputStream(fromPath);
			imageS = ImageIO.read(Uploadimg);

			double sw = imageS.getWidth();
			double sh = imageS.getHeight();
			double rw, rh;

			if (chkRealSize == true)
			{
				if (sw > sh)
				{
					rw = (newWidth / sw) * sw;
					rh = (newWidth / sw) * sh;
				}
				else
				{
					rw = (newHeight / sh) * sw;
					rh = (newHeight / sh) * sh;
				}
			}
			else
			{
				rw = newWidth;
				rh = newHeight;
			}

			// 放大图像不会导致失真，而缩小图像将不可避免的失真。
			// Java中也同样是这样。
			// 但java提供了4个缩放的微调选项。
			// image.SCALE_SMOOTH //平滑优先
			// image.SCALE_FAST//速度优先
			// image.SCALE_AREA_AVERAGING //区域均值
			// image.SCALE_REPLICATE //像素复制型缩放
			// image.SCALE_DEFAULT //默认缩放模式
			Image new_img = imageS.getScaledInstance((int) rw, (int) rh, Image.SCALE_SMOOTH);
			imageR = new BufferedImage((int) rw, (int) rh, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = (Graphics2D) imageR.getGraphics();
			g.setBackground(backColor);
			g.clearRect(0, 0, (int) rw, (int) rh);
			g.drawImage(new_img, 0, 0, (int) rw, (int) rh, null);

			// 關閉圖片
			g.dispose();
			imageS.flush();
			imageR.flush();
			new_img.flush();
			Uploadimg.close();

			String FileName = fromPath.substring(fromPath.lastIndexOf("/") + 1).trim();
			String auxFileName = FileName.substring(FileName.lastIndexOf(".") + 1, FileName.length());

			fileOut = new File(savePath);
			if (!fileOut.exists())
			{
				fileOut.mkdirs();
				log.info("資料匣" + savePath + "已創建");
			}

			fileOut = new File(savePath + "r" + newWidth + "_" + FileName);
			ImageIO.write(imageR, auxFileName, fileOut);

			log.debug("Change Img Size OK");
		}
		catch (Exception ex)
		{
			log.error("chgImagePx Has Error = " + ex);
			check = false;
		}
		return check;
	}

	// 改變圖片大小
	// 輸出 BufferedImage
	// 後續呼叫者處理
	// file : 要改變的圖檔
	// FilePath : 輸出檔案路徑(不含檔名)
	// 希望改變的寬高
	public BufferedImage chgImagePx(FileItem file, int newWidth, int newHeight)
	{
		BufferedImage imageS = null;
		BufferedImage imageR = null;
		File fileOut = null;
		Color backColor = Color.white;
		boolean check = true;

		try
		{
			InputStream Uploadimg = file.getInputStream();
			imageS = ImageIO.read(Uploadimg);

			double sw = imageS.getWidth();
			double sh = imageS.getHeight();
			double rw, rh;

			if (sw > sh)
			{
				rw = (newWidth / sw) * sw;
				rh = (newWidth / sw) * sh;
			}
			else
			{
				rw = (newHeight / sh) * sw;
				rh = (newHeight / sh) * sh;
			}

			imageR = new BufferedImage((int) rw, (int) rh, BufferedImage.TYPE_INT_RGB);

			Graphics2D g = (Graphics2D) imageR.getGraphics();

			g.setBackground(backColor);
			g.clearRect(0, 0, (int) rw, (int) rh);

			g.drawImage(imageS, 0, 0, (int) rw, (int) rh, null);

			// 關閉圖片
			g.dispose();
			imageS.flush();
			imageR.flush();

			log.debug("OK");
		}
		catch (Exception ex)
		{
			log.error("chgImagePx Has Error = " + ex);
			check = false;
		}
		return imageR;
	}

	/**
	 * 將Document輸出為XML FILE
	 *
	 * @param doc
	 * @param filePath
	 */
	public void crtXMLFile(Document doc, String filePathName)
	{
		try
		{
			// 实例化输出格式对象
			// 下面這行：預設自動換行、Tab 為 2 個空白
			// OutputFormat of = OutputFormat.createPrettyPrint();
			OutputFormat of = new OutputFormat(); // 格式化XML
			of.setEncoding("UTF-8"); // 设置输出编码
			of.setIndentSize(4); // 設定 Tab 為 4 個空白
			of.setNewlines(true);// 設定 自動換行

			// 檢查路徑是否存在
			String filePath = filePathName.substring(0, filePathName.lastIndexOf("/") + 1);
			// log.debug(filePath);
			File f = new File(filePath);
			if (!f.exists())
			{
				f.mkdirs();
				log.debug("資料夾不存在，已自動建立");
			}

			// 创建需要写入的File对象
			// FileWriter fw = new FileWriter(filePathName);
			// XMLWriter xw = new XMLWriter(fw , of);
			// FileOutputStream fos = new FileOutputStream(new
			// File(filePathName));
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(new File(filePathName)), Charset.forName("utf-8"));
			XMLWriter xw = new XMLWriter(osw, of);

			xw.write(doc);
			xw.close();
		}
		catch (Exception ex)
		{
			System.err.println("err:" + ex);
		}
	}
}
