package com.pchome.akbadm.db.service.catalog.uploadList;

import java.util.Map;

import org.json.JSONObject;

import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpCatalogUploadListService extends IBaseService<String, String> {

	/**
	 * 取得匯入所需要得一些資料
	 * @param catalogSeq
	 * @param fileNameAndFileExtension
	 * @return
	 */
	JSONObject getImportData(String catalogSeq, String fileNameAndFileExtension);

	/**
	 * 將csv檔案內容轉成json格式
	 * @param filePath
	 * @param catalogProdJsonData
	 * @return
	 */
	JSONObject getCSVFileDataToJson(String filePath, JSONObject catalogProdJsonData);

	/**
	 * 依照商品目錄類別，處理相對應的部分
	 * @throws Exception 
	 */
	Map<String, Object> processCatalogProdJsonData(JSONObject catalogProdJsonData) throws Exception;

	/**
	 * 從網址下載檔案
	 * @param url 下載網址
	 * @param savePath 完整儲存路徑
	 * @return
	 */
	boolean loadURLFile(String url, String savePath);

	/**
	 * 自動排程下載失敗
	 * @param catalogSeq
	 * @param contentUrl
	 * @param fileUpdateDatetime 
	 */
	void autoJobDownloadFail(String catalogSeq, String contentUrl, String fileUpdateDatetime);

	/**
	 * 自動排程上傳、PChome賣場網址上傳-每天凌晨1點執行的排程
	 * 產生urltxt檔案
	 * @param catalogProdCsvFilePath 
	 */
	void createUrlTxt(String catalogProdCsvFilePath);

	/**
	 * PChome賣場網址上傳-每天早上10點執行的排程
	 * 產生urllist.txt檔案，沒有則用產生，有則用累加網址
	 */
	void createStoreURLlistTxt(String catalogProdStoreURLlistPath);

	/**
	 * 排程上傳csv或賣場網址有問題，更新pfp_catalog_upload_log "商品目錄更新紀錄"
	 * @param catalogUploadLogSeq 更新紀錄id
	 * @param errMsgUpdateContent 加上錯誤訊息的更新內容(檔名或網址)
	 */
	void csvFileFail(String catalogUploadLogSeq, String errMsgUpdateContent);
}