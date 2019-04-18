package com.pchome.akbadm.db.service.catalog.uploadList;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pchome.akbadm.db.vo.catalog.uploadList.ShoppingProdVO;
import com.pchome.akbadm.struts2.ajax.ad.AdUtilAjax;
import com.pchome.enumerate.ad.EnumPfpCatalog;
import com.pchome.enumerate.catalogprod.EnumEcStockStatusType;
import com.pchome.enumerate.catalogprod.EnumEcUseStatusType;
import com.pchome.soft.depot.utils.CommonUtils;
import com.pchome.soft.depot.utils.ImgUtil;

public abstract class APfpCatalogUploadListData {

	public static final Logger log = LogManager.getRootLogger();
	private String akbPfpServer;
	private final int imgSizeLimit = (180 * 1024); // 圖片限制180K
	private final int imgWidthHeightLimit = 320; // 圖片寬高限制320(長或寬只要有一邊大於320就可以)
	public abstract Object processCatalogProdJsonData(JSONObject catalogProdJsonData) throws Exception;

	/**
	 * 檢查商品類別(非必填)
	 * @param errorPrdItemArray
	 * @param itemSeq
	 * @param ecCategory
	 * @return
	 * @throws JSONException 
	 */
	public JSONArray checkEcCategory(JSONArray errorPrdItemArray, int itemSeq, String ecCategory) throws JSONException {
		String prodItemErrorMsg = "";
		int ecCategoryLimit = 20;

		if (ecCategory.length() > ecCategoryLimit) {
			prodItemErrorMsg = "字數限制" + ecCategoryLimit + "字";
		}
		
		if (StringUtils.isNotBlank(prodItemErrorMsg)) { // 錯誤訊息非空值則記錄
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("catalog_prod_err_item", itemSeq);
			jsonObject.put("catalog_err_item", "ec_category");
			jsonObject.put("catalog_err_reason", prodItemErrorMsg);
			errorPrdItemArray.put(jsonObject);
		}
		return errorPrdItemArray;
	}

	/**
	 * 檢查連結網址
	 * @param errorPrdItemArray
	 * @param itemSeq
	 * @param ecUrl
	 * @return
	 * @throws Exception 
	 */
	public JSONArray checkEcUrl(JSONArray errorPrdItemArray, int itemSeq, String ecUrl) throws Exception {
		String prodItemErrorMsg = "";

		if (StringUtils.isBlank(ecUrl)) {
			prodItemErrorMsg = "必填欄位";
		} else {
			AdUtilAjax adUtilAjax = new AdUtilAjax();
			String checkResultMsg = adUtilAjax.checkAdShowUrl(ecUrl, akbPfpServer);
			if (StringUtils.isNotBlank(checkResultMsg)) {
				prodItemErrorMsg = "連結錯誤";
			}
		}
		
		if (StringUtils.isNotBlank(prodItemErrorMsg)) { // 錯誤訊息非空值則記錄
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("catalog_prod_err_item", itemSeq);
			jsonObject.put("catalog_err_item", "ec_url");
			jsonObject.put("catalog_err_reason", prodItemErrorMsg);
			errorPrdItemArray.put(jsonObject);
		}
		return errorPrdItemArray;
	}

	/**
	 * 檢查廣告圖像網址及圖片是否符合規範
	 * @param errorPrdItemArray
	 * @param itemSeq // 項目序號
	 * @param photoPath // 圖片路徑
	 * @param ecImgUrl // 圖片網址
	 * @param ecImgBase64 // 圖片Base64編碼
	 * @param catalogUploadType // 上傳方式
	 * @param shoppingProdItemVO 
	 * @return
	 * @throws Exception 
	 */
	public JSONArray checkEcImgUrl(JSONArray errorPrdItemArray, int itemSeq, String photoPath, String ecImgUrl, String ecImgBase64, String catalogUploadType, ShoppingProdVO shoppingProdItemVO) throws Exception {
		String prodItemErrorMsg = ""; // 檢查到一個有錯誤，剩下檢查則略過
		
		// 將圖片下載至每個user自己的暫存圖片資料夾檢查
		String imgTempPath = photoPath + "/temp";
		String imgPath = "";
		int imageWidth = 0; // 圖片寬
		int imageHeight = 0; // 圖片高

		if (StringUtils.isBlank(ecImgBase64)) {
			// 沒有Base64，有圖片網址則是用檔案上傳、自動排程上傳、PChome賣場上傳
			if (StringUtils.isBlank(prodItemErrorMsg)) {
				if (StringUtils.isBlank(ecImgUrl)) {
					prodItemErrorMsg = "必填欄位";
				}
			}
			
			if (StringUtils.isBlank(prodItemErrorMsg)) {
				AdUtilAjax adUtilAjax = new AdUtilAjax();
				String checkResultMsg = adUtilAjax.checkAdShowUrl(ecImgUrl, akbPfpServer);
				if (StringUtils.isNotBlank(checkResultMsg)) {
					prodItemErrorMsg = "連結錯誤";
				}
			}
			
			File imgFile = null;
			if (StringUtils.isBlank(prodItemErrorMsg)) {
				imgPath = ImgUtil.processImgPathForCatalogProd(ecImgUrl, imgTempPath, String.valueOf(itemSeq), shoppingProdItemVO);
				if (StringUtils.isBlank(imgPath)) {
					// 空的表示連不到或是HTTP 403沒有權限訪問此站，伺服器收到請求但拒絕提供服務。
					prodItemErrorMsg = "連結錯誤";
				} else if ("檔案格式錯誤".equals(imgPath)) { // 非jpg、gif、png
					prodItemErrorMsg = "檔案格式錯誤";
				}
			}
			
			if (StringUtils.isBlank(prodItemErrorMsg)) {
				String completePath = imgTempPath.substring(0, imgTempPath.indexOf("img/user/")) + imgPath;
				imgFile = new File(completePath);
				if (imgFile.length() > imgSizeLimit) {
					prodItemErrorMsg = "檔案過大";
					imgFile.delete(); // 刪除檔案
				}
			}
			
			if (StringUtils.isBlank(prodItemErrorMsg)) {
				FileInputStream fis = new FileInputStream(imgFile);
				BufferedImage bufferedImage = ImageIO.read(fis);
				if (!EnumPfpCatalog.CATALOG_UPLOAD_STORE_URL.getType().equals(catalogUploadType)
						&& bufferedImage.getWidth() < imgWidthHeightLimit && bufferedImage.getHeight() < imgWidthHeightLimit) {
					// 賣場網址上傳不檢查解析度
					prodItemErrorMsg = "解析度不足";
					imgFile.delete(); // 刪除檔案
				} else {
					imageWidth = bufferedImage.getWidth();
					imageHeight = bufferedImage.getHeight();
				}
				fis.close();
			}
			
		} else {
			// 有Base64則用手動上傳
			if (StringUtils.isBlank(prodItemErrorMsg)) {
				String filenameExtension = ImgUtil.getImgBase64FilenameExtension(ecImgBase64);
				if (!"jpg".equalsIgnoreCase(filenameExtension) && !"gif".equalsIgnoreCase(filenameExtension)
						&& !"png".equalsIgnoreCase(filenameExtension)) {
					prodItemErrorMsg = "檔案格式錯誤";
				} else {
					// 紀錄檔案副檔名
					shoppingProdItemVO.setEcImgFilenameExtension(filenameExtension);
				}
			}
			
			File imgFile = null;
			if (StringUtils.isBlank(prodItemErrorMsg)) {
				imgPath = ImgUtil.processImgBase64StringToImage(ecImgBase64, imgTempPath, String.valueOf(itemSeq));
				String completePath = imgTempPath.substring(0, imgTempPath.indexOf("img/user/")) + imgPath;
				imgFile = new File(completePath);
				if (imgFile.length() > imgSizeLimit) {
					prodItemErrorMsg = "檔案過大";
					imgFile.delete(); // 刪除檔案
				}
			}
			
			if (StringUtils.isBlank(prodItemErrorMsg)) {
				FileInputStream fis = new FileInputStream(imgFile);
				BufferedImage bufferedImage = ImageIO.read(fis);
				if (bufferedImage.getWidth() < imgWidthHeightLimit 
						&& bufferedImage.getHeight() < imgWidthHeightLimit) {
					prodItemErrorMsg = "解析度不足";
					imgFile.delete(); // 刪除檔案
				} else {
					imageWidth = bufferedImage.getWidth();
					imageHeight = bufferedImage.getHeight();
				}
				fis.close();
			}
			
		}

		if (StringUtils.isNotBlank(prodItemErrorMsg)) { // 錯誤訊息非空值則記錄
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("catalog_prod_err_item", itemSeq);
			jsonObject.put("catalog_err_item", "ec_img_url");
			jsonObject.put("catalog_err_reason", prodItemErrorMsg);
			errorPrdItemArray.put(jsonObject);
		} else {
			// 非gif再調整圖片大小，此方法gif無法使用，且gif不做處理
			if (!"gif".equalsIgnoreCase(FilenameUtils.getExtension(imgPath))) {
				// 圖檔沒錯誤則按比例調整圖片大小
				String completePath = imgTempPath.substring(0, imgTempPath.indexOf("img/user/")) + imgPath;
				String resizeImgCompletePath = imgTempPath + "/resize_" + itemSeq + "." + FilenameUtils.getExtension(imgPath);

				// 有些可能圖片不擋解析度，會有未滿長寬320，避免暫存圖片被刪除設立flag
				boolean resizeFlag = false;
				// 長寬都大於320限制，做短邊圖片調整
				if (imageWidth > imgWidthHeightLimit && imageHeight > imgWidthHeightLimit) {
					ImgUtil.getInstance().imgMinSizeProportionResize(completePath, resizeImgCompletePath, 320);
					resizeFlag = true;
				} else if (imageWidth > imgWidthHeightLimit || imageHeight > imgWidthHeightLimit) {
					// 其中一邊大於320限制，做長邊圖片調整
					ImgUtil.getInstance().imgMaxSizeProportionResize(completePath, resizeImgCompletePath, 320);
					resizeFlag = true;
				}
				
				if (resizeFlag) {
					// 將resize的圖檔檔名，改回舊的檔名
					File oldFile = new File(completePath);
					oldFile.delete();
					File newFile = new File(resizeImgCompletePath);
					newFile.renameTo(oldFile);
				}
			}
		}
		return errorPrdItemArray;
	}

	/**
	 * 檢查商品使用狀況
	 * 0:全新,1:二手,2:福利品
	 * @param errorPrdItemArray
	 * @param itemSeq
	 * @param ecUseStatus
	 * @return
	 * @throws JSONException 
	 */
	public JSONArray checkEcUseStatus(JSONArray errorPrdItemArray, int itemSeq, String ecUseStatus) throws JSONException {
		String prodItemErrorMsg = "";
		
		if (StringUtils.isBlank(ecUseStatus)) {
			prodItemErrorMsg = "必填欄位";
		} else if (!EnumEcUseStatusType.New_Goods.getChName().equals(ecUseStatus)
				&& !EnumEcUseStatusType.Used_Goods.getChName().equals(ecUseStatus)
				&& !EnumEcUseStatusType.Welfare_Goods.getChName().equals(ecUseStatus)) {
			prodItemErrorMsg = "格式錯誤";
		}
		
		if (StringUtils.isNotBlank(prodItemErrorMsg)) { // 錯誤訊息非空值則記錄
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("catalog_prod_err_item", itemSeq);
			jsonObject.put("catalog_err_item", "ec_use_status");
			jsonObject.put("catalog_err_reason", prodItemErrorMsg);
			errorPrdItemArray.put(jsonObject);
		}
		return errorPrdItemArray;
	}

	/**
	 * 檢查商品供應情況
	 * 0:無庫存,1:有庫存,2:預購,3:停售
	 * @param errorPrdItemArray
	 * @param itemSeq
	 * @param ecStockStatus
	 * @return
	 * @throws JSONException 
	 */
	public JSONArray checkEcStockStatus(JSONArray errorPrdItemArray, int itemSeq, String ecStockStatus) throws JSONException {
		String prodItemErrorMsg = "";
		
		if (StringUtils.isBlank(ecStockStatus)) {
			prodItemErrorMsg = "必填欄位";
		} else if (!EnumEcStockStatusType.Out_Of_Stock.getChName().equals(ecStockStatus)
				&& !EnumEcStockStatusType.In_Stock.getChName().equals(ecStockStatus)
				&& !EnumEcStockStatusType.Pre_Order.getChName().equals(ecStockStatus)
				&& !EnumEcStockStatusType.Discontinued.getChName().equals(ecStockStatus)) {
			prodItemErrorMsg = "格式錯誤";
		}
		
		if (StringUtils.isNotBlank(prodItemErrorMsg)) { // 錯誤訊息非空值則記錄
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("catalog_prod_err_item", itemSeq);
			jsonObject.put("catalog_err_item", "ec_stock_status");
			jsonObject.put("catalog_err_reason", prodItemErrorMsg);
			errorPrdItemArray.put(jsonObject);
		}
		return errorPrdItemArray;
	}

	/**
	 * 檢查促銷價
	 * @param errorPrdItemArray
	 * @param itemSeq 項目序號
	 * @param ecDiscountPrice 促銷價
	 * @param ecPrice 原價
	 * @return
	 * @throws JSONException 
	 */
	public JSONArray checkEcDiscountPrice(JSONArray errorPrdItemArray, int itemSeq, String ecDiscountPrice, String ecPrice) throws JSONException {
		String prodItemErrorMsg = "";
		int ecDiscountPriceLimit = 6;

		if (StringUtils.isBlank(ecDiscountPrice)) {
			prodItemErrorMsg = "必填欄位";
		} else if (!StringUtils.isNumeric(ecDiscountPrice)) {
			prodItemErrorMsg = "格式錯誤";
		} else if (ecDiscountPrice.length() > ecDiscountPriceLimit) {
			prodItemErrorMsg = "超出金額位數限制";
		} else if (StringUtils.isNumeric(ecPrice) && Integer.parseInt(ecDiscountPrice) > Integer.parseInt(ecPrice)) {
			prodItemErrorMsg = "特價大於原價";
		}

		if (StringUtils.isNotBlank(prodItemErrorMsg)) { // 錯誤訊息非空值則記錄
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("catalog_prod_err_item", itemSeq);
			jsonObject.put("catalog_err_item", "ec_discount_price");
			jsonObject.put("catalog_err_reason", prodItemErrorMsg);
			errorPrdItemArray.put(jsonObject);
		}
		return errorPrdItemArray;
	}

	/**
	 * 檢查原價(非必填欄位)
	 * @param errorPrdItemArray
	 * @param itemSeq
	 * @param ecPrice
	 * @return
	 * @throws JSONException 
	 */
	public JSONArray checkEcPrice(JSONArray errorPrdItemArray, int itemSeq, String ecPrice) throws JSONException {
		String prodItemErrorMsg = "";
		int ecPriceLimit = 6;
		
		if (StringUtils.isNotBlank(ecPrice)) { // 只有空格也當沒輸入值  StringUtils.isNotBlank(" ") false
			if (!StringUtils.isNumeric(ecPrice)) {
				prodItemErrorMsg = "格式錯誤";
			} else if (ecPrice.length() > ecPriceLimit) {
				prodItemErrorMsg = "超出金額位數限制";
			}
		}

		if (StringUtils.isNotBlank(prodItemErrorMsg)) { // 錯誤訊息非空值則記錄
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("catalog_prod_err_item", itemSeq);
			jsonObject.put("catalog_err_item", "ec_price");
			jsonObject.put("catalog_err_reason", prodItemErrorMsg);
			errorPrdItemArray.put(jsonObject);
		}
		return errorPrdItemArray;
	}

	/**
	 * 檢查商品名稱
	 * @param errorPrdItemArray 
	 * @param itemSeq
	 * @param ecName
	 * @return
	 * @throws JSONException 
	 */
	public JSONArray checkEcName(JSONArray errorPrdItemArray, int itemSeq, String ecName) throws JSONException {
		String prodItemErrorMsg = "";
		int ecNameLimit = 20;
		
		if (StringUtils.isBlank(ecName)) {
			prodItemErrorMsg = "必填欄位";
		} else if (ecName.length() > ecNameLimit) {
			prodItemErrorMsg = "字數限制" + ecNameLimit + "字";
		} else if (CommonUtils.isHaveEmojiString(ecName)) {
			prodItemErrorMsg = "內含特殊字元";
		}
	    
		if (StringUtils.isNotBlank(prodItemErrorMsg)) { // 錯誤訊息非空值則記錄
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("catalog_prod_err_item", itemSeq);
			jsonObject.put("catalog_err_item", "ec_name");
			jsonObject.put("catalog_err_reason", prodItemErrorMsg);
			errorPrdItemArray.put(jsonObject);
		}
		return errorPrdItemArray;
	}

	/**
	 * 檢查商品id
	 * @param errorPrdItemArray
	 * @param catalogProdSeq 商品ID
	 * @param itemSeq 項目序號
	 * @return
	 * @throws JSONException 
	 */
	public JSONArray checkCatalogProdSeq(JSONArray errorPrdItemArray, int itemSeq, String catalogProdSeq) throws JSONException {
		String prodItemErrorMsg = "";
		int catalogProdSeqLimit = 20;
		
		if (StringUtils.isBlank(catalogProdSeq)) {
			prodItemErrorMsg = "必填欄位";
		} else if (catalogProdSeq.length() > catalogProdSeqLimit) {
			prodItemErrorMsg = "字數限制" + catalogProdSeqLimit + "字";
		} else if (CommonUtils.isHaveEmojiString(catalogProdSeq)
				|| CommonUtils.containsSpecialSymbolsThatAreNotAllowedByFileName(catalogProdSeq)
				|| CommonUtils.containsChineseStr(catalogProdSeq)) {
			prodItemErrorMsg = "內含特殊字元";
		}
		
		if (StringUtils.isNotBlank(prodItemErrorMsg)) { // 錯誤訊息非空值則記錄
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("catalog_prod_err_item", itemSeq);
			jsonObject.put("catalog_err_item", "id");
			jsonObject.put("catalog_err_reason", prodItemErrorMsg);
			errorPrdItemArray.put(jsonObject);
		}
		return errorPrdItemArray;
	}
	
	/**
	 * 網址前面沒有協定的話，先補上https，測試連線若無法則再改補http
	 * @param url
	 * @return "https://" + url  OR  "http://" + url
	 * @throws Exception 
	 */
	public String urlAddHttpOrHttps(String url) throws Exception {
		String tempUrl = url.substring(0, 5);
		
		if (tempUrl.indexOf("http") == -1 && tempUrl.indexOf("https") == -1) {
			tempUrl = "https://" + url;

			AdUtilAjax adUtilAjax = new AdUtilAjax();
			String checkResultMsg = adUtilAjax.checkAdShowUrl(tempUrl, akbPfpServer);
			if (StringUtils.isNotBlank(checkResultMsg)) {
				tempUrl = "http://" + url;
			}
			return tempUrl;
		}
		
		return url;
	}
	
	public void setAkbPfpServer(String akbPfpServer) {
		this.akbPfpServer = akbPfpServer;
	}

}