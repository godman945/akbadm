package com.pchome.akbadm.db.service.catalog.uploadList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mozilla.universalchardet.UniversalDetector;

import com.opencsv.CSVReader;
import com.pchome.akbadm.db.dao.catalog.uploadList.IPfpCatalogUploadListDAO;
import com.pchome.akbadm.db.pojo.PfpCatalog;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.service.catalog.IPfpCatalogService;
import com.pchome.akbadm.utils.HttpUtil;
import com.pchome.enumerate.ad.EnumPfpCatalog;

public class PfpCatalogUploadListService extends BaseService<String, String> implements IPfpCatalogUploadListService {

	protected Logger log = LogManager.getRootLogger();
	private ShoppingProd shoppingProd;
	private IPfpCatalogService pfpCatalogService;
	private IPfpCatalogUploadListDAO pfpCatalogUploadListDAO;
	
	/**
	 * 取得匯入所需要得一些資料
	 * @param catalogSeq
	 * @param fileNameAndFileExtension
	 * @return
	 */
	@Override
	public JSONObject getImportData(String catalogSeq, String fileNameAndFileExtension) {
		log.info("getImportData catalogSeq:" + catalogSeq + ", fileNameAndFileExtension:" + fileNameAndFileExtension);
		JSONObject catalogProdJsonData = new JSONObject();
		try {
			// 取得pfp_catalog "商品目錄"
			PfpCatalog pfpCatalog = pfpCatalogService.get(catalogSeq);
			catalogProdJsonData.put("catalogSeq", catalogSeq);
			catalogProdJsonData.put("catalogType", pfpCatalog.getCatalogType());
			catalogProdJsonData.put("pfpCustomerInfoId", pfpCatalog.getPfpCustomerInfoId());
			
			String catalogUploadType = pfpCatalog.getCatalogUploadType();
			catalogProdJsonData.put("catalogUploadType", catalogUploadType);

			String updateDatetime = fileNameAndFileExtension.substring(0, 14);
	
			// 取得 pfp_catalog_upload_log "商品目錄更新紀錄"
			List<Map<String, Object>> catalogProdUploadErrList = null;
			catalogProdUploadErrList = pfpCatalogUploadListDAO.getPfpCatalogUploadLogForImportData(catalogSeq, "", updateDatetime);
			
			if (catalogProdUploadErrList.size() > 0) {
				catalogProdJsonData.put("catalogUploadLogSeq", (String) catalogProdUploadErrList.get(0).get("catalog_upload_log_seq"));
				catalogProdJsonData.put("updateWay", (String) catalogProdUploadErrList.get(0).get("update_way"));
				// 將前面的錯誤訊息清除
				String updateContent = (String) catalogProdUploadErrList.get(0).get("update_content");
				if (updateContent.indexOf("_http") > -1) { // 只有自動排程和賣場網址上傳table會是存網址
					catalogProdJsonData.put("updateContent", updateContent.substring(updateContent.indexOf("http")));
				} else {
					catalogProdJsonData.put("updateContent", updateContent);
				}
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				catalogProdJsonData.put("updateDatetime", formatter.format((Date) catalogProdUploadErrList.get(0).get("update_datetime")));
			} else {
				log.info("getImportData is catalogSeq:" + catalogSeq + "catalogProdUploadErrListSize = 0");
				return catalogProdJsonData = new JSONObject();
			}
		
		} catch (Exception e) {
			log.error("目錄:" + catalogSeq + ",錯誤訊息 :" + e);
			// 有可能時間差，table還查不到資料get會出錯
			return catalogProdJsonData = new JSONObject();
		}
		
		return catalogProdJsonData;
	}

	/**
	 * 將csv檔案內容轉成json格式
	 * @param filePath
	 * @param catalogProdJsonData
	 * @return
	 */
	@Override
	public JSONObject getCSVFileDataToJson(String filePath, JSONObject catalogProdJsonData) {
		JSONArray prdItemArray = new JSONArray();
		try{
			String fileEncode = getFileEncode(filePath);
			
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, fileEncode);
			CSVReader reader = new CSVReader(isr);
			String [] csvTitleArray;
			
			if (catalogProdJsonData.getString("catalogType").equals(EnumPfpCatalog.CATALOG_SHOPPING.getType())) {
				int row = 0;
				Integer idArrayIndex = null;
				Integer ecNameArrayIndex = null;
				Integer ecPriceArrayIndex = null;
				Integer ecDiscountPriceArrayIndex = null;
				Integer ecStockStatusArrayIndex = null;
				Integer ecUseStatusArrayIndex = null;
				Integer ecImgUrlArrayIndex = null;
				Integer ecUrlArrayIndex = null;
				Integer ecCategoryArrayIndex = null;
				
				while ((csvTitleArray = reader.readNext()) != null) {
					// 第一列檢查
					if (row == 0) {
						// 檢查是否有董哥放的錯誤訊息
						if (csvTitleArray[0].indexOf("error") > -1) {
							catalogProdJsonData.put("errMsg", csvTitleArray[0].replace("error:", ""));
							break;
						}
						
						for (int i = 0; i < csvTitleArray.length; i++) {
							switch (csvTitleArray[i]) {
							case "id*":
								idArrayIndex = i;
								break;
							case "商品名稱*":
								ecNameArrayIndex = i;
								break;
							case "促銷價*":
								ecDiscountPriceArrayIndex = i;
								break;
							case "商品供應情況*":
								ecStockStatusArrayIndex = i;
								break;
							case "商品使用狀況*":
								ecUseStatusArrayIndex = i;
								break;
							case "廣告圖像網址*":
								ecImgUrlArrayIndex = i;
								break;
							case "連結網址*":
								ecUrlArrayIndex = i;
								break;
							case "原價":
								ecPriceArrayIndex = i;
								break;
							case "商品類別":
								ecCategoryArrayIndex = i;
								break;
							}
						}
						
						// 檢查必填欄位是否都有
						if (idArrayIndex == null || ecNameArrayIndex == null || ecDiscountPriceArrayIndex == null
								|| ecStockStatusArrayIndex == null || ecUseStatusArrayIndex == null
								|| ecImgUrlArrayIndex == null || ecUrlArrayIndex == null) {
							catalogProdJsonData.put("errMsg", "檔案格式錯誤");
							break;
						}
						
						row = row + 1;
						continue;
					}
					
					if (csvTitleArray.length == 1) { // 其中被插入空白行，該行無資料則略過
						continue;
					}
					
					JSONObject prdItemObject = new JSONObject();
					if (idArrayIndex != null && idArrayIndex < csvTitleArray.length) {
						prdItemObject.put("id", csvTitleArray[idArrayIndex]);
					} else {
						prdItemObject.put("id", " ");
					}
					
					if (ecNameArrayIndex != null && ecNameArrayIndex < csvTitleArray.length) {
						prdItemObject.put("ec_name", csvTitleArray[ecNameArrayIndex]);
					} else {
						prdItemObject.put("ec_name", " ");
					}
					
					if (ecPriceArrayIndex != null && ecPriceArrayIndex < csvTitleArray.length) {
						prdItemObject.put("ec_price", csvTitleArray[ecPriceArrayIndex].replaceAll(",", ""));
					} else {
						prdItemObject.put("ec_price", "");
					}
					
					if (ecDiscountPriceArrayIndex != null && ecDiscountPriceArrayIndex < csvTitleArray.length) {
						prdItemObject.put("ec_discount_price", csvTitleArray[ecDiscountPriceArrayIndex].replaceAll(",", ""));
					} else {
						prdItemObject.put("ec_discount_price", " ");
					}
					
					if (ecStockStatusArrayIndex != null && ecStockStatusArrayIndex < csvTitleArray.length) {
						prdItemObject.put("ec_stock_status", csvTitleArray[ecStockStatusArrayIndex]);
					} else {
						prdItemObject.put("ec_stock_status", " ");
					}
					
					if (ecUseStatusArrayIndex != null && ecUseStatusArrayIndex < csvTitleArray.length) {
						prdItemObject.put("ec_use_status", csvTitleArray[ecUseStatusArrayIndex]);
					} else {
						prdItemObject.put("ec_use_status", " ");
					}
					
					if (ecImgUrlArrayIndex != null && ecImgUrlArrayIndex < csvTitleArray.length) {
						prdItemObject.put("ec_img_url", csvTitleArray[ecImgUrlArrayIndex]);
					} else {
						prdItemObject.put("ec_img_url", " ");
					}
					
					if (ecUrlArrayIndex != null && ecUrlArrayIndex < csvTitleArray.length) {
						prdItemObject.put("ec_url", csvTitleArray[ecUrlArrayIndex]);
					} else {
						prdItemObject.put("ec_url", " ");
					}
					
					if (ecCategoryArrayIndex != null && ecCategoryArrayIndex < csvTitleArray.length) {
						prdItemObject.put("ec_category", csvTitleArray[ecCategoryArrayIndex]);
					} else {
						prdItemObject.put("ec_category", " ");
					}
					
					prdItemArray.put(prdItemObject);
				}
				
			}
			
			catalogProdJsonData.put("catalogProdItem", prdItemArray);
			
			fis.close();
			isr.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			return catalogProdJsonData;
		}
		
		return catalogProdJsonData;
	}
	
	/**
	 * 判斷檔案編碼格式
	 * @param filePath
	 * @return
	 */
	private String getFileEncode(String filePath) {
		try {
			FileInputStream fis = new FileInputStream(filePath); // 讀取檔案
			UniversalDetector detector = new UniversalDetector(null); // 建立分析器

			int nread;
			byte[] buf = new byte[4096];
			while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
				// 分析資料
				detector.handleData(buf, 0, nread);
			}
			
			fis.close();
			detector.dataEnd(); // Marks end of data reading. Finish calculations.

			// 取得編碼格式
			String encode = detector.getDetectedCharset();
			return encode;
		} catch (Exception e) {
			log.error(">>>>getFileType error:" + e.getMessage(), e);
			return "UTF-8";
		}
	}

	/**
	 * 從網址下載檔案
	 * @param url 下載網址
	 * @param savePath 完整儲存路徑
	 * @return
	 */
	@Override
	public boolean loadURLFile(String url, String savePath) {
		try {
			log.info("load start");
			HttpUtil.disableCertificateValidation();
			
			log.info("load url:" + url);
			URL zeroFile = new URL(url);
			// 增加User-Agent，避免被發現是機器人被阻擋掉
			HttpURLConnection urlConnection = (HttpURLConnection) zeroFile.openConnection();
			urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
			urlConnection.setRequestMethod("GET");
			log.info("load urlConnection:" + urlConnection);
			
			InputStream is = zeroFile.openStream();
			log.info("load InputStream:" + is);
			Files.copy(is, new File(savePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
			
			log.info("load end");
			
			/** 
			 * 讀取資料使用的buffer大小(預設8KB) 
			 * 用此方式檔案要先改為tmp
			 * */
//			int bufferReaderKB = 8;
//			BufferedInputStream bs = new BufferedInputStream(is, bufferReaderKB * 1024);
//			byte[] b = new byte[1024];// 一次取得 1024 個 bytes
//			FileOutputStream fs = new FileOutputStream(savePath);
//			int len;
//			int bits = 0; // 累計目前下載kb
//			while ((len = bs.read(b, 0, b.length)) != -1) {
//				fs.write(b, 0, len);
//				bits += len;
//			}
//
//			bs.close();
//			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	/**
	 * 自動排程下載失敗
	 * @param catalogSeq
	 * @param contentUrl
	 */
	@Override
	public void autoJobDownloadFail(String catalogSeq, String contentUrl, String updateDatetime) {
		// 更新 pfp_catalog_upload_log "商品目錄更新紀錄"
		pfpCatalogUploadListDAO.updatePfpCatalogUploadLogForAutoJobDownloadFail(catalogSeq, "下載失敗_" + contentUrl, updateDatetime);
	}
	
	/**
	 * 依照商品目錄類別，處理相對應的部分
	 * @throws Exception 
	 */
	@Override
	public Map<String, Object> processCatalogProdJsonData(JSONObject catalogProdJsonData) throws Exception {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		String catalogType = catalogProdJsonData.optString("catalogType");
		
		if (catalogType.isEmpty() || 
				(!EnumPfpCatalog.CATALOG_SHOPPING.getType().equals(catalogType)
				&& !EnumPfpCatalog.CATALOG_BOOKING.getType().equals(catalogType)
				&& !EnumPfpCatalog.CATALOG_TRAFFIC.getType().equals(catalogType)
				&& !EnumPfpCatalog.CATALOG_RENT.getType().equals(catalogType))) {
			dataMap.put("status", "ERROR");
			dataMap.put("msg", "商品目錄類型資料錯誤!");
			return dataMap;
		}
		
		if (EnumPfpCatalog.CATALOG_SHOPPING.getType().equals(catalogType)) { // 1:一般購物類
			dataMap = shoppingProd.processCatalogProdJsonData(catalogProdJsonData);
		} else if (EnumPfpCatalog.CATALOG_BOOKING.getType().equals(catalogType)) { // 2:訂房住宿類
			// 功能待開發
		} else if (EnumPfpCatalog.CATALOG_TRAFFIC.getType().equals(catalogType)) { // 3:交通航班類
			// 功能待開發
		} else if (EnumPfpCatalog.CATALOG_RENT.getType().equals(catalogType)) { // 4:房產租售類
			// 功能待開發
		}
		
		return dataMap;
	}
	
	/**
	 * 自動排程上傳、PChome賣場網址上傳-每天凌晨1點執行的排程
	 * 產生urltxt檔案
	 * @param catalogProdCsvFilePath 
	 */
	@Override
	public void createUrlTxt(String catalogProdCsvFilePath) {
		List<Object> createUrlTxtList = pfpCatalogUploadListDAO.getCreateUrlTxtList("autoJobAndStoreUrl");
		
		for (Object object : createUrlTxtList) {
			Object[] objArray = (Object[]) object;
			
			// 更新紀錄id
//			String catalogUploadLogSeq = (String) objArray[0];
			// 商品目錄ID
			String catalogSeq = (String) objArray[1];
			// 更新時間
			String updateDatetime = (String) objArray[2];
			// pfp_id
			String pfpCustomerInfoId = (String) objArray[3];
			// 上傳內容(檔名OR網址)
			String catalogUploadContent = (String) objArray[4];
			// 上傳方式(1:檔案上傳, 2:自動排程上傳, 3:賣場網址上傳, 4:手動上傳)
			String catalogUploadType = (String) objArray[5];
			
			// url.txt建立路徑 (路徑+客編+目錄)
			String urlTxtPath = catalogProdCsvFilePath + pfpCustomerInfoId + "/" + catalogSeq + "/url.txt";
			log.info(">>>>>>>>> addUrlTxt path:" + urlTxtPath);
			try {
				FileWriter fw = new FileWriter(urlTxtPath);
				if (catalogUploadType.equals(EnumPfpCatalog.CATALOG_UPLOAD_AUTOMATIC_SCHEDULING.getType())) {
					fw.write(catalogUploadContent);
					fw.write("\r\n" + updateDatetime);
				} else if (catalogUploadType.equals(EnumPfpCatalog.CATALOG_UPLOAD_STORE_URL.getType())) {
					// 賣場網址上傳則改為董哥那個網址
					fw.write("http://spradmprd.mypchome.com.tw/csv_data/" + catalogSeq + ".csv");
					fw.write("\r\n" + updateDatetime);
					fw.write("\r\n" + catalogUploadContent);
				}
				
				fw.flush();
				fw.close();
				
				// 將目錄狀態改為上傳中
				PfpCatalog pfpCatalog = pfpCatalogService.get(catalogSeq);
				pfpCatalog.setUploadStatus("1");
				pfpCatalogService.update(pfpCatalog);
			} catch (Exception e) {
				log.error("此目錄:" + catalogSeq + "已不存在。" + e);
			}
		}
	}
	
	/**
	 * PChome賣場網址上傳-每天早上10點執行的排程
	 * 產生urllist.txt檔案，沒有則用產生，有則用累加網址
	 */
	@Override
	public void createStoreURLlistTxt(String catalogProdStoreURLlistPath) {
		List<Object> createUrlTxtList = pfpCatalogUploadListDAO.getCreateUrlTxtList(EnumPfpCatalog.CATALOG_UPLOAD_STORE_URL.getType());
		for (Object object : createUrlTxtList) {
			Object[] objArray = (Object[]) object;
			
			// 更新紀錄id
//			String catalogUploadLogSeq = (String) objArray[0];
			// 商品目錄ID
			String catalogSeq = (String) objArray[1];
			// 更新時間
//			String updateDatetime = (String) objArray[2];
			// pfp_id
//			String pfpCustomerInfoId = (String) objArray[3];
			// 上傳內容(檔名OR網址)
			String catalogUploadContent = (String) objArray[4];
			
			try {
				// 如果文件存在，則追加內容；如果文件不存在，則創建文件
				File file = new File(catalogProdStoreURLlistPath + "urllist.txt");
				FileWriter fileWriter = new FileWriter(file, true); // true,進行追加寫。
				
				PrintWriter printWriter = new PrintWriter(fileWriter); 
				printWriter.println(catalogSeq + "," + catalogUploadContent); 
				printWriter.flush();
				fileWriter.flush();
				printWriter.close();
				fileWriter.close();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * 排程上傳csv或賣場網址有問題，更新pfp_catalog_upload_log "商品目錄更新紀錄"
	 * @param catalogUploadLogSeq 更新紀錄id
	 * @param errMsgUpdateContent 加上錯誤訊息的更新內容(檔名或網址)
	 */
	@Override
	public void csvFileFail(String catalogUploadLogSeq, String errMsgUpdateContent) {
		// 更新 pfp_catalog_upload_log "商品目錄更新紀錄"
		pfpCatalogUploadListDAO.updatePfpCatalogUploadLogForCsvFileFail(catalogUploadLogSeq, errMsgUpdateContent);	
	}
	
	public void setPfpCatalogService(IPfpCatalogService pfpCatalogService) {
		this.pfpCatalogService = pfpCatalogService;
	}

	public void setPfpCatalogUploadListDAO(IPfpCatalogUploadListDAO pfpCatalogUploadListDAO) {
		this.pfpCatalogUploadListDAO = pfpCatalogUploadListDAO;
	}

	public void setShoppingProd(ShoppingProd shoppingProd) {
		this.shoppingProd = shoppingProd;
	}

}