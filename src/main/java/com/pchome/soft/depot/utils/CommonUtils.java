package com.pchome.soft.depot.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class CommonUtils {

	protected static final Logger log = LogManager.getRootLogger();
	
	private static CommonUtils instance = new CommonUtils();
	
	public static synchronized CommonUtils getInstance() {
		return instance;
	}
	
	/**
	 * 濾掉攻擊資料庫語法
	 */
	public static String filterSqlInjection(String str) {

		if (str!=null) {
			str = str.trim();
			str = str.replaceAll("=", "");
			str = str.replaceAll(">", "");
			str = str.replaceAll("<", "");
			str = str.replaceAll(";", "");
			str = str.replaceAll("'", "");
			str = str.replaceAll("%", "");
			str = str.replaceAll("#", "");
			str = str.replaceAll("/", "");
			str = str.replaceAll("\\(", "");
			str = str.replaceAll("\\)", "");
			str = str.replaceAll("\\[", "");
			str = str.replaceAll("\\]", "");
			str = str.replaceAll("\\\\", "");
			
		} else {
			str = "";
		}

		return str;
	}

	/*
	 * 取得影像寬高
	 * */
	public Map<String,String> getImgInfo(File file)  {
		Map<String,String> imgInfoMap = new HashMap<String, String>();
		try {
			ImageInputStream stream = new FileImageInputStream(file);
            Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
            String imgWidth = "";
            String imgHeight = "";
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                reader.setInput(stream, true);
                imgWidth = String.valueOf(reader.getWidth(0));
                imgHeight = String.valueOf(reader.getHeight(0));
                imgInfoMap.put("imgWidth", imgWidth);
                imgInfoMap.put("imgHeight", imgHeight);
                log.info(">>>>>>>>>>>>>>>>>>>>>>reader.getFormatName():"+reader.getFormatName());
                
                if(reader.getFormatName().toUpperCase().equals("JPEG")){
                	imgInfoMap.put("imgFileType", "jpg");
                }else if(reader.getFormatName().toUpperCase().equals("GIF")){
                	imgInfoMap.put("imgFileType", "gif");
                }else if(reader.getFormatName().toUpperCase().equals("PNG")){
                	imgInfoMap.put("imgFileType", "png");
                }
             }
             stream.close();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return imgInfoMap;
	}
	
	/**
	 * 取得總頁數
	 * @param totalDataSize 總筆數
	 * @param pageSize      每頁筆數
	 * @return
	 */
	public static int getTotalPage(int totalDataSize, int pageSize) {
		int totalPage = (totalDataSize / pageSize); //沒筆數則會為0
		if ((totalDataSize % pageSize) > 0) { //筆數還有剩，則再多加一頁
			totalPage += 1;
		}
		return totalPage;
	}
	
	/**
	 * 取得取代掉特殊符號的字串
	 * @param str
	 * @return
	 */
	public static String getReplaceSpecialSymbolsStr(String str) {
		return str.replaceAll("[^A-Za-z0-9]+$", "");
	}
	
	/**
	 * 取得取代掉檔案名稱不允許輸入的9個特殊符號
	 * @param fileName
	 * @return
	 */
	public static String getReplaceSpecialSymbolsThatAreNotAllowedByFileName(String fileName) {
		return fileName.replaceAll("[(/) | (\\\\) | (:) | (\\*) | (\\?) | (\") | (<) | (>)]", "");
	}
	
	/**
	 * 檢查字串是否有 Emoji圖片 字串
	 * @param checkStr
	 * @return
	 */
	public static boolean isHaveEmojiString(String str) {
		int strLength = str.length();
		int strReplaceEmojiLength = str.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "").length();
		if (strLength != strReplaceEmojiLength) { // 將Emoji字串取代完後，長度不同表示字串內有Emoji
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 包含檔名不能使用的特殊符號
	 * @param str
	 * @return true:有 false:沒有
	 */
	public static boolean containsSpecialSymbolsThatAreNotAllowedByFileName(String str) {
		return str.matches("^.*[(/) | (\\\\) | (:) | (\\*) | (\\?) | (\") | (<) | (>)].*$");
	}
	
	/**
	 * 包含中文
	 * @param str
	 * @return true:有 false:沒有
	 */
	public static boolean containsChineseStr(String str) {
		return str.matches("^.*[\u4e00-\u9fa5].*$");
	}
}
