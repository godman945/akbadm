package com.pchome.akbadm.quartzs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.service.catalog.IPfpCatalogService;
import com.pchome.akbadm.db.service.catalog.uploadList.IPfpCatalogUploadListService;
import com.pchome.akbadm.db.vo.catalog.PfpCatalogVO;
import com.pchome.config.TestConfig;

public class PfpCatalogUploadJob {

	protected Logger log = LogManager.getRootLogger();
	private IPfpCatalogService pfpCatalogService;
	private IPfpCatalogUploadListService pfpCatalogUploadListService;
	
	private String catalogProdCsvFilePath;
	private String catalogProdCsvFileBackupPath;
	private String catalogProdStoreURLlistPath;

	/**
	 * crontab排程用
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
	    ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
	    PfpCatalogUploadJob job = context.getBean(PfpCatalogUploadJob.class);
	    job.readUrlTxtDownloadCsvFile();
	    job.init();
	    
	    // 本機開排程用
//	    do {
//			job.readUrlTxtDownloadCsvFile();
//			job.init();
//			System.out.println("執行結束。");
//			
//			Thread.currentThread().sleep(60000); //毫秒 每分鐘跑一次測試
//		} while (true);
	}
	
	/**
	 * 每分鐘將url.txt改為read_url.txt，再從read_url.txt將內容網址下載csv檔，下載失敗則紀錄log
	 * @throws Exception
	 */
	private void readUrlTxtDownloadCsvFile() throws Exception {
		log.info(">>>>readUrlTxtDownloadCsvFile Start");
		getDirForUrlDownload(catalogProdCsvFilePath);
		log.info(">>>>readUrlTxtDownloadCsvFile End");
	}

	private synchronized void getDirForUrlDownload(String path) throws Exception {
		File file = new File(path);
		if (file.isDirectory()) {
			File[] filelist = file.listFiles();
			for (int i = 0; i < filelist.length; i++) {
				if (filelist[i].isDirectory()) {
					// 是子目錄則繼續看更深層
					getDirForUrlDownload(filelist[i].getPath());
				} else if (filelist[i].isFile()) {

					if ("url.txt".equals(filelist[i].getName())) {
						// 目錄路徑
						String catalogPath = processReplaceSlash(filelist[i].getParent());
						// 目錄編號
						String catalogSeq = catalogPath.substring(catalogPath.lastIndexOf("/") + 1, catalogPath.length());
						
						// 判斷是否正在讀取下載檔案
						File readUrlFile = new File(catalogPath + "/read_url.txt");
						if (readUrlFile.exists()) {
							log.info("catalog:" + catalogSeq + "downloading CSV。");
							break;
						} else {
							log.info("catalog:" + catalogSeq + "ready dowlond CSV。");
							File urlFile = new File(catalogPath + "/" + filelist[i].getName());
							boolean rename = urlFile.renameTo(readUrlFile);
							if (rename) { // 建立準備下載的read_url.txt後開始讀檔下載
								
								String contentUrl = ""; // 網址
								String fileUpdateDatetime = ""; // 跟log table 比對的UpdateDatetime
								String storeURL = ""; // 賣場網址(沒被改過打董哥的原始網址)
								FileReader fr = new FileReader(readUrlFile.getPath());
								BufferedReader br = new BufferedReader(fr);
								int row = 1;
								while (br.ready()) {
									if (row == 1) {
										contentUrl = br.readLine();
									} else if (row == 2) {
										fileUpdateDatetime = br.readLine();
									} else if (row == 3) {
										storeURL = br.readLine();
									}
									row++;
								}

								// pfp編號
								String pfpCustomerInfoId = processReplaceSlash(file.getParent()).replace(catalogProdCsvFilePath, "");

								// 檔名改用目錄編號，前面有日期年月日時分秒，同一目錄下不太可能重複
								String downloadPath = catalogProdCsvFilePath + pfpCustomerInfoId + "/" + catalogSeq + "/" + fileUpdateDatetime + "_" + catalogSeq + ".csv";
								
								log.info(">>>>>>  CSV downloadPath:" + downloadPath);
								boolean loadFile = pfpCatalogUploadListService.loadURLFile(contentUrl, downloadPath);
								if (!loadFile) {
									
									if (storeURL.length() > 0) { // 賣場網址下載有錯誤時，將原始網址蓋掉下載網址
										contentUrl = storeURL;
									}
									// 下載失敗，紀錄log，更新目錄狀態
									pfpCatalogUploadListService.autoJobDownloadFail(catalogSeq, contentUrl, fileUpdateDatetime);
									if (file.listFiles().length == 1) { // 只剩一個，表示沒有再異動url了，將狀態改為已完成
										updatePfpCatalogProdNumAndUploadStatus(catalogSeq);
										log.error("catalog:" + catalogSeq + "url.txt csv URL download failed");
									}
								}
								
								br.close();
								fr.close();
								
								readUrlFile.delete(); // 不管下載成功或失敗皆刪除txt
							}
						}
						
					}
				}
			}
		}
	}
	
	/**
	 * 預設先每分鐘執行的排程
	 * @throws Exception 
	 */
	private void init() throws Exception {
		log.info(">>>>init Start");
		getDir(catalogProdCsvFilePath);
		log.info(">>>>init End");
	}
	
	/**
	 * 讀取目錄資料
	 * @param path 開始路徑
	 * @throws Exception
	 */
	private synchronized void getDir(String path) throws Exception {
		File file = new File(path);
		if (file.isDirectory()) {
			File[] filelist = file.listFiles();
			for (int i = 0; i < filelist.length; i++) {
				
				String filePath = processReplaceSlash(file.getPath());
				String dirNameTwoStr = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("/") + 3);
				if ("PC".equals(dirNameTwoStr)) { // PC開頭為目錄資料夾
					// 目錄路徑
					String catalogPath = processReplaceSlash(filelist[i].getParent());
					// 目錄編號
					String catalogSeq = catalogPath.substring(catalogPath.lastIndexOf("/") + 1, catalogPath.length());
					File lockFile = new File(catalogPath + "/" + catalogSeq + "_lock.txt");
					log.info(">>>>>>>>>>catalogSeq:" + catalogSeq + ", lockFile.exists():" + lockFile.exists());
					if (lockFile.exists()) {
						log.info(FilenameUtils.getBaseName(lockFile.getName()) + " is Processing");
						// 檢查lockFile是否超過2小時
						checkFile(lockFile);
						break;
					} else {
						// 此目錄內 有csv檔才建立lock檔案
						for (int y = 0; y < filelist.length; y++) {
							if ("csv".equals(FilenameUtils.getExtension(filelist[y].getName()))) {
								FileUtils.touch(lockFile);
								break;
							}
						}
						
						// 先將檔案依文件修改日期递增
						filelistSort(filelist);
						
						// 目錄內上傳的多個檔案，依照上傳的先後開始執行
						for (int j = 0; j < filelist.length; j++) {
							log.info("==============filelist[" + j + "]:" + filelist[j].getName());
							// 是檔案則檢查檔案
							if (filelist[j].isFile()) {
								checkFile(filelist[j]);
							}
							
							// 最後一個檔案時處理
							if (j == filelist.length - 1) {
								// 1.重新檢查此資料夾是否還有2個檔案已上，理論上應該只剩lock檔，只剩1個lock檔則將資料上傳狀態改成上傳完成，還有2個以上則不修改上傳狀態
								if (file.listFiles().length == 1) {
									File[] fileFinishlist = file.listFiles();
									if (lockFile.getName().equals(fileFinishlist[0].getName())) {
										updatePfpCatalogProdNumAndUploadStatus(catalogSeq);
									}
								}
								
								// 2.固定lock檔刪除，剩下的由下一分排程執行
								lockFile.delete();
							}
						}
					}
				}
				
				// 是子目錄則繼續看更深層
				if (filelist[i].isDirectory()) {
					getDir(filelist[i].getPath());
				}
			}
		}
	}

	/**
	 * 檢查檔案，依csv或lock.txt做相對應處理
	 * @param file
	 */
	private void checkFile(File file) {
		try {
			// 檔案完整路徑
			String filePath = processReplaceSlash(file.getPath());
			// pfp編號
			String pfpCustomerInfoId = filePath.replace(catalogProdCsvFilePath, "").substring(0, 15);
			// 這份檔案在的目錄路徑
			String fileParentPath = processReplaceSlash(file.getParent());
			// 目錄編號
			String catalogSeq = fileParentPath.substring(fileParentPath.lastIndexOf("/") + 1, fileParentPath.length());
			// 檔名含副檔名
			String fileNameAndFileExtension = file.getName();
			// 副檔名
			String fileExtension = FilenameUtils.getExtension(file.getName());
			// 純檔名
			String fileName = FilenameUtils.getBaseName(file.getName());
			
			if ("csv".equalsIgnoreCase(fileExtension)) {
				// 1.查詢table是否有這個csv資料，有可能是時間差，檔案已上傳，table還沒寫好，table查不到資料此csv先不匯入
				JSONObject catalogProdJsonData = pfpCatalogUploadListService.getImportData(catalogSeq, fileNameAndFileExtension);
				if (catalogProdJsonData.length() > 0) {
					log.info("start catalogSeq:" + catalogSeq + ", fileName:" + fileNameAndFileExtension);
					
					// 2.讀取csv
					catalogProdJsonData = pfpCatalogUploadListService.getCSVFileDataToJson(filePath, catalogProdJsonData);
					
					if (StringUtils.isBlank(catalogProdJsonData.optString("errMsg"))) {
						// 3.正確，匯入資料
						pfpCatalogUploadListService.processCatalogProdJsonData(catalogProdJsonData);
					} else {
						// 3.錯誤，log更新錯誤訊息
						pfpCatalogUploadListService.csvFileFail(catalogProdJsonData.optString("catalogUploadLogSeq"), catalogProdJsonData.optString("errMsg") + "_" + catalogProdJsonData.optString("updateContent"));
					}
					
					// 4.檔案移動至備份資料夾
					File backupFile = new File(catalogProdCsvFileBackupPath + pfpCustomerInfoId + "/" + catalogSeq + "/" + fileNameAndFileExtension);
					FileUtils.moveFile(file, backupFile);
					
					log.info("import end");
				} else {
					log.info("table is null");
				}
				
			} else if (fileNameAndFileExtension.indexOf("_lock.txt") > -1) {
				// 檢查lock.txt 建立時間是否大於2小時，超過2小時則刪除lock.txt
				Path path = Paths.get(filePath);
				BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
	
				Calendar c = Calendar.getInstance();
				// 建立時間加上超過兩小時時間
				c.setTimeInMillis(attrs.creationTime().toMillis() + (1000 * 60 * 60 * 2));
				Calendar now = Calendar.getInstance();
				// 目前時間 大於 lock建立超過兩小時則刪除
				if (now.getTimeInMillis() > c.getTimeInMillis()) {
					
					File folderPath = new File(fileParentPath);
					if (folderPath.listFiles().length == 1) {
						// 目錄內檔案只剩lock檔的話，則改變狀態，重新算商品count量，若還剩多個僅刪除lock檔讓下一分鐘排程執行
						updatePfpCatalogProdNumAndUploadStatus(catalogSeq);
					}
					
					file.delete();
					log.info(fileNameAndFileExtension + " is lock more than two hours。");
				}
			}
		
	    } catch (Exception e) {
			log.error(">>>>checkFile error:" + e.getMessage(), e);
		}
	}
	
	/**
	 * 自動排程上傳、PChome賣場網址上傳-每天凌晨1點執行的排程
	 * 產生urltxt檔案
	 */
	private void autoJobCreateUrlTxt() {
		try {
			log.info(">>>>autoJobCreateUrlTxt Start");
			pfpCatalogUploadListService.createUrlTxt(catalogProdCsvFilePath);
			log.info(">>>>autoJobCreateUrlTxt End");
		} catch (Exception e) {
			log.error(">>>>autoJobCreateUrlTxt error:" + e.getMessage(), e);
		}
	}
	
	/**
	 * PChome賣場網址上傳-每天早上10點執行的排程
	 * 產生urllist.txt檔案，沒有則用產生，有則用累加網址
	 */
	private void pchomeStoreURLCreateTxt() {
		try {
			log.info(">>>>pchomeStoreURLCreateTxt Start");
			pfpCatalogUploadListService.createStoreURLlistTxt(catalogProdStoreURLlistPath);
			log.info(">>>>pchomeStoreURLCreateTxt End");
		} catch (Exception e) {
			log.error(">>>>pchomeStoreURLCreateTxt error:" + e.getMessage(), e);
		}
	}
	
	/**
	 * 更新 pfp_catalog "商品目錄" 資料
	 * 狀態更新為已完成，重新算商品count量
	 * @param catalogSeq
	 */
	private void updatePfpCatalogProdNumAndUploadStatus(String catalogSeq) {
		PfpCatalogVO pfpCatalogVO = new PfpCatalogVO();
		pfpCatalogVO.setCatalogSeq(catalogSeq);
		pfpCatalogVO.setUploadStatus("2");
		pfpCatalogService.updatePfpCatalogProdNumAndUploadStatus(pfpCatalogVO);
	}
	
	/**
	 * linux是往左下斜，win是右下斜，先將斜線處理，不論在哪個作業系統就不會有問題
	 * @param path
	 * @return
	 */
	private String processReplaceSlash(String path) {
		return path.replace("\\", "/");
	}
	
	/**
	 * 檔案列表依文件修改日期递增
	 * @param filelist
	 */
	private void filelistSort(File[] filelist) {
		Arrays.sort(filelist, new Comparator<File>() {
			public int compare(File f1, File f2) {
				long diff = f1.lastModified() - f2.lastModified();
				if (diff > 0) {
					return 1;
				} else if (diff == 0) {
					return 0;
				} else {
					return -1;
				}
			}
        });
	}
	
	/**
	 * 由網址判斷是否為csv檔
	 * @param url
	 * @return 正確回傳檔名fileName及副檔名filenameExtension  錯誤則檔名副檔名皆為空
	 */
//	public static Map<String, String> getDataFromUrl(String url) {
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("fileName", "");
//		map.put("filenameExtension", "");
//
//		// 由網址判斷取得檔名副檔名
//		int startLength = url.lastIndexOf("/") + 1;
//		int endLength = (url.indexOf("?") > -1 ? url.indexOf("?") : url.length());
//		String fileName = url.substring(startLength, endLength);
//		String filenameExtension = "";
//		if (fileName.length() >= 4) {
//			filenameExtension = fileName.substring(fileName.length() - 4);
//		}
//		
//		if (".csv".equalsIgnoreCase(filenameExtension)) {
//			map.put("fileName", fileName);
//			map.put("filenameExtension", "csv");
//		}
//		return map;
//	}
	
	public void setPfpCatalogService(IPfpCatalogService pfpCatalogService) {
		this.pfpCatalogService = pfpCatalogService;
	}

	public void setPfpCatalogUploadListService(IPfpCatalogUploadListService pfpCatalogUploadListService) {
		this.pfpCatalogUploadListService = pfpCatalogUploadListService;
	}

	public void setCatalogProdCsvFilePath(String catalogProdCsvFilePath) {
		this.catalogProdCsvFilePath = catalogProdCsvFilePath;
	}

	public void setCatalogProdCsvFileBackupPath(String catalogProdCsvFileBackupPath) {
		this.catalogProdCsvFileBackupPath = catalogProdCsvFileBackupPath;
	}

	public void setCatalogProdStoreURLlistPath(String catalogProdStoreURLlistPath) {
		this.catalogProdStoreURLlistPath = catalogProdStoreURLlistPath;
	}

}