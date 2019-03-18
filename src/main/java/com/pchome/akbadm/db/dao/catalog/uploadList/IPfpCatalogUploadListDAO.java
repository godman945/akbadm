package com.pchome.akbadm.db.dao.catalog.uploadList;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpCatalogProdEc;
import com.pchome.akbadm.db.pojo.PfpCatalogProdEcError;
import com.pchome.akbadm.db.pojo.PfpCatalogUploadErrLog;
import com.pchome.akbadm.db.pojo.PfpCatalogUploadLog;
import com.pchome.akbadm.db.vo.catalog.PfpCatalogVO;

public interface IPfpCatalogUploadListDAO extends IBaseDAO<String, String> {

	/**
	 * 從商品目錄更新紀錄 給匯入資料判斷用
	 * @param catalogSeq
	 * @param catalogUploadContent
	 * @param updateDatetime
	 * @return
	 */
	List<Map<String, Object>> getPfpCatalogUploadLogForImportData(String catalogSeq, String catalogUploadContent,
			String updateDatetime);

	/**
	 * 取得一般購物類商品清單 資料
	 * @param catalogSeq 商品目錄
	 * @param catalogProdSeq 商品編號
	 * @return
	 */
	List<Map<String, Object>> getPfpCatalogProdEc(String catalogSeq, String catalogProdSeq);
	
	/**
	 * 取得一般購物類商品清單 資料
	 * @param catalogSeq
	 * @param pfpCatalogProdEc
	 * @return
	 */
//	List<Map<String, Object>> getPfpCatalogProdEc(String catalogSeq, PfpCatalogProdEc pfpCatalogProdEc);
	
	/**
	 * 更新一般購物類資料
	 * @param pfpCatalogProdEc
	 * @return 更新筆數
	 */
	int updatePfpCatalogProdEc(PfpCatalogProdEc pfpCatalogProdEc);
	
	/**
	 * 新增一般購物類資料
	 * @param pfpCatalogProdEc
	 */
	void savePfpCatalogProdEc(PfpCatalogProdEc pfpCatalogProdEc);
	
	/**
	 * 刪除不在catalogProdEcSeqList列表內的資料
	 * @param catalogSeq 商品目錄ID
	 * @param catalogProdEcSeqList 不被刪除的名單
	 */
	void deleteNotInPfpCatalogProdEc(String catalogSeq, List<String> catalogProdEcSeqList);
	
	/**
	 * 新增商品目錄更新紀錄
	 * @param pfpCatalogUploadLog
	 */
	void savePfpCatalogUploadLog(PfpCatalogUploadLog pfpCatalogUploadLog);
	
	/**
	 * 更新商品目錄更新紀錄
	 * @param pfpCatalogUploadLog
	 */
	void updatePfpCatalogProdEc(PfpCatalogUploadLog pfpCatalogUploadLog);
	
	/**
	 * 刪除 商品目錄更新錯誤紀錄
	 * @param vo
	 */
	void deletePfpCatalogUploadErrLog(PfpCatalogVO vo);
	
	/**
	 * 刪除 一般購物類商品上傳錯誤清單
	 * @param vo
	 * @throws Exception 
	 */
	void deletePfpCatalogProdEcError(PfpCatalogVO vo);
	
	/**
	 * 新增商品目錄更新錯誤紀錄
	 * @param pfpCatalogUploadErrLog
	 */
	void savePfpCatalogUploadErrLog(PfpCatalogUploadErrLog pfpCatalogUploadErrLog);
	
	/**
	 * 新增一般購物類商品上傳錯誤清單
	 * @param pfpCatalogProdEcError
	 */
	void savePfpCatalogProdEcError(PfpCatalogProdEcError pfpCatalogProdEcError);

	/**
	 * 自動排程下載失敗更新 商品目錄更新紀錄
	 * @param catalogSeq
	 * @param updateContent
	 * @param updateDatetime
	 */
	void updatePfpCatalogUploadLogForAutoJobDownloadFail(String catalogSeq, String updateContent, String updateDatetime);

	/**
	 * 取得自動排程上傳要建立url.txt的清單
	 * @param type 上傳方式 autoJobAndStoreUrl(2, 3) 或 (1:檔案上傳, 2:自動排程上傳, 3:賣場網址上傳, 4:手動上傳)
	 * @return
	 */
	List<Object> getCreateUrlTxtList(String type);

	/**
	 * 下載的csv格式錯誤 更新 商品目錄更新紀錄
	 * @param catalogUploadLogSeq
	 * @param errMsgUpdateContent
	 */
	void updatePfpCatalogUploadLogForCsvFileFail(String catalogUploadLogSeq, String errMsgUpdateContent);

}