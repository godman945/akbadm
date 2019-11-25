package com.pchome.soft.depot.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pchome.akbadm.db.vo.catalog.uploadList.ShoppingProdVO;
import com.pchome.akbadm.utils.HttpUtil;

public class ImgUtil {
	private static final Logger log = LogManager.getRootLogger();
	
	private static Process process = null;
	
	private static String result = "";
	
	private static ImgUtil instance = new ImgUtil();
	
	private static StringBuffer imgPathBuffer = new StringBuffer();
	
	public static synchronized ImgUtil getInstance() {
		return instance;
	}
	
	
	/**
	 * 使用mozjpeg進行壓縮
	 * */
	public synchronized static void processMozjoeg(String photoPath,String imgFileName)  {
		try {
			log.info("***** MOZJPEG　PROCESS START *****");
			File file = new File(photoPath+imgFileName+".jpg");
			log.info(">>>>[file path]:"+photoPath+imgFileName+".jpg");
			log.info(">>>>Before size:"+(file.length()/1024)+"kb");
			result = "";
			imgPathBuffer.setLength(0);
			imgPathBuffer.append(" /opt/mozjpeg/bin/cjpeg  -quality 80 -tune-ms-ssim  -sample 1x1 -quant-table 0      ").append(photoPath+imgFileName+".jpg").append(" >").append(photoPath+imgFileName+"[RESIZE].jpg");
			process = Runtime.getRuntime().exec(new String[] { "bash", "-c", imgPathBuffer.toString()  });
			result = IOUtils.toString(process.getInputStream(), "UTF-8");
			log.info(">>>>>>>>>>command:"+imgPathBuffer.toString());
			
			imgPathBuffer.setLength(0);
			imgPathBuffer.append(" mv ").append(photoPath+imgFileName+"[RESIZE].jpg").append(" ").append(photoPath+imgFileName+".jpg");
			process = Runtime.getRuntime().exec(new String[] { "bash", "-c", imgPathBuffer.toString()  });
			result = IOUtils.toString(process.getInputStream(), "UTF-8");
			log.info(">>>>>>>>>>command:"+imgPathBuffer.toString());
			File fileNew = new File(photoPath+imgFileName+".jpg");
			log.info(">>>>After Size:"+fileNew.length()/1024+"kb");
			log.info("***** MOZJPEG　PROCESS END *****");
		}catch(Exception e) {
			log.error(e.getMessage());
			log.error("*****MOZJPEG　PROCESS END*****");
		}
	}
	
	/**
	 * 處理廣告商品下載圖片
	 * @param imgURL 下載的圖片網址
	 * @param photoPath 資料夾位置
	 * @param imgFileName 檔名
	 * @return 正常下載圖片:取得圖片存放路徑 出錯:無圖片路徑，非jpg、gif、png回傳"檔案格式錯誤"
	 */
	public static String processImgPathForCatalogProd(String imgURL, String photoPath, String imgFileName) {
		return processImgPathForCatalogProd(imgURL, photoPath, imgFileName, null);
	}
	
	/**
	 * 處理廣告商品下載圖片
	 * @param imgURL 下載的圖片網址
	 * @param photoPath 資料夾位置
	 * @param imgFileName 檔名
	 * @param shoppingProdItemVO 購物商品用來紀錄圖片副檔名用
	 * @return 正常下載圖片:取得圖片存放路徑 出錯:無圖片路徑，非jpg、gif、png回傳"檔案格式錯誤"
	 */
	public static String processImgPathForCatalogProd(String imgURL, String photoPath, String imgFileName, ShoppingProdVO shoppingProdItemVO) {
		log.info(">>>>Start downloading images.");
		String imgPath = "";
		InputStream in = null;
    	try {
			createFolder(photoPath);
			
			// 將特殊符號取代為空，處理圖片時才不會因為有特殊符號而出錯
			imgFileName = CommonUtils.getReplaceSpecialSymbolsStr(imgFileName);
			imgFileName = CommonUtils.getReplaceSpecialSymbolsThatAreNotAllowedByFileName(imgFileName);
			
			log.info(">>>>Download image URL:" + imgURL);
			// 加System.setProperty的原因，參考https://blog.csdn.net/gege87417376/article/details/77936507
			System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1,SSLv3");
			HttpUtil.disableCertificateValidation();
			
//			舊code如果處理跳轉及中文部分的新code可正常運作，此段舊code舊可拔除
//	        URL url = new URL(imgURL);
//	        // 增加User-Agent，避免被發現是機器人被阻擋掉
//			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//			urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//			urlConnection.setRequestMethod("GET");
			
			URL url = null;
			HttpURLConnection urlConnection = null;
			int whileRunCount = 10; // 設定最多跳轉10次
			do { // 處理網址跳轉
				imgURL = com.pchome.soft.depot.utils.HttpUtil.getInstance().enCode(imgURL); // 將網址後面的中文檔名編碼
				url = new URL(imgURL);
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
				urlConnection.setInstanceFollowRedirects(false); // 設定關閉自動轉址

				String locationURL = urlConnection.getHeaderField("Location");
				if (locationURL != null) {
					imgURL = new String(locationURL.getBytes("iso-8859-1"), "UTF-8"); // 處理網址中文，避免亂碼
					System.out.println("redirects URL: " + imgURL); // 接下來要跳轉到的圖片url
				} else {
					imgURL = null;
				}

				whileRunCount--;
			} while (imgURL != null && whileRunCount >= 0);

			// Header內容取得副檔名，避免輸入的網址沒有副檔名無法判斷的問題
			String contentType = urlConnection.getHeaderField("Content-Type");
			log.info(">>>>contentType:" + contentType);
			// jpeg及png都改為jpg
			String filenameExtension = contentType.replace("jpeg", "jpg").substring(contentType.indexOf("/") + 1);
			log.info(">>>>filenameExtension:" + filenameExtension);
			if (shoppingProdItemVO != null) { // 購物商品用來紀錄圖片副檔名用
				shoppingProdItemVO.setEcImgFilenameExtension(filenameExtension);
			}
			
			in = urlConnection.getInputStream();
	        String imgPathAndName = photoPath + "/" + imgFileName + "." + filenameExtension; // 存放路徑 + 檔名
	
	        // 處理圖片下載
	        if ("gif".equalsIgnoreCase(filenameExtension)) { // gif圖片下載方式，此方式圖片才有動畫
	        	Files.copy(in, new File(imgPathAndName).toPath(), StandardCopyOption.REPLACE_EXISTING);
			} else if("jpg".equalsIgnoreCase(filenameExtension) || "png".equalsIgnoreCase(filenameExtension)) { // jpg、png圖片下載方式
				BufferedImage img = ImageIO.read(in);
				if ("png".equalsIgnoreCase(filenameExtension)) {
					filenameExtension = "jpg"; // 將png轉成jpg
					shoppingProdItemVO.setEcImgFilenameExtension(filenameExtension);
					BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		            result.createGraphics().drawImage(img, 0, 0, img.getGraphics().getColor(), null);
		            ImageIO.write(result, filenameExtension, new File(StringUtils.replace(imgPathAndName, "png", filenameExtension, imgPathAndName.length() - 3)));
				} else {
					ImageIO.write(img, filenameExtension, new File(imgPathAndName));
				}
			} else {
				in.close();
				return "檔案格式錯誤";
			}
	        in.close();
	        
	        imgPath = photoPath.substring(photoPath.indexOf("img/")) + "/" + imgFileName + "." + filenameExtension;
			log.info(">>>>Download image ends, imgPath:" + imgPath);
			return imgPath;
        } catch (Exception e) {
        	log.error(">>>>processImgPath error:" + e.getMessage(), e);
        	return imgPath;
		}
	}

	/**
	 * 將Base64圖片產生後，取得圖片存放路徑
	 * @param imgBase64String 圖片的Base64字串
	 * @param photoPath 資料夾位置
	 * @param imgFileName 檔名
	 * @return
	 * @throws IOException 
	 */
	public static String processImgBase64StringToImage(String imgBase64String, String photoPath, String imgFileName) throws IOException {
		log.info("開始將Base64圖片產生存放。");
		createFolder(photoPath);
		
		// 將特殊符號取代為空，處理圖片時才不會因為有特殊符號而出錯
		imgFileName = CommonUtils.getReplaceSpecialSymbolsStr(imgFileName);
		imgFileName = CommonUtils.getReplaceSpecialSymbolsThatAreNotAllowedByFileName(imgFileName);
		String filenameExtension = getImgBase64FilenameExtension(imgBase64String);
		String imgPathAndName = photoPath + "/" + imgFileName + "." + filenameExtension; // 存放路徑 + 檔名

		byte[] bytes = Base64.decodeBase64(imgBase64String.split(",")[1].getBytes());
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		if ("gif".equalsIgnoreCase(filenameExtension)) {// gif圖片產生方式，此方式圖片才有動畫
			Files.copy(byteArrayInputStream, new File(imgPathAndName).toPath(), StandardCopyOption.REPLACE_EXISTING);
		} else { // jpg、png圖片產生方式
			BufferedImage img = ImageIO.read(byteArrayInputStream);
			if ("png".equalsIgnoreCase(filenameExtension)) {
				filenameExtension = "jpg"; // 將png轉成jpg
				BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
				result.createGraphics().drawImage(img, 0, 0, img.getGraphics().getColor(), null);
				ImageIO.write(result, filenameExtension, new File(StringUtils.replace(imgPathAndName, "png", filenameExtension, imgPathAndName.length() - 3)));
			} else {
				ImageIO.write(img, filenameExtension, new File(imgPathAndName));
			}
		}
		byteArrayInputStream.close();
		
		String imgPath = photoPath.substring(photoPath.indexOf("img/")) + "/" + imgFileName + "." + filenameExtension;
		log.info("Base64圖片存檔完成。");
		return imgPath;
	}

	/**
	 * 判斷圖片長寬，回傳相對應代碼
	 * @param imgPath 圖片完整路徑
	 * ex:
	 * 本機 D:/home/webuser/akb/pfp/img/user/img/user/AC2013071700005/catalogProd/PC201810050000000001/ccc.gif
	 * stg、prd /export/home/webuser/akb/pfp/img/user/AC2013071700005/catalogProd/PC201810050000000001/ccc.gif
	 * @return 橫&正方形:H、直:V
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static String getImgLongWidthCode(String imgPath) throws FileNotFoundException, IOException {
		FileInputStream stream = new FileInputStream(new File(imgPath));
		BufferedImage bufferedImage = ImageIO.read(stream);
		if (bufferedImage.getWidth() >= bufferedImage.getHeight()) {
			stream.close();
			return "H";
		} else {
			stream.close();
			return "V";
		}
	}
	
	/**
	 * 取得圖片MD5值
	 * @param imgPath
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException 
	 */
	public static String getImgMD5Code(String imgPath) throws NoSuchAlgorithmException, IOException {
		log.info("***** START PROCESS MD5 ,imgPath:" + imgPath);
		File file = new File(imgPath);
		FileInputStream fis = new FileInputStream(file);
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] buffer = new byte[1024];
		int length = -1;
		while ((length = fis.read(buffer, 0, 1024)) != -1) {
			md.update(buffer, 0, length);
		}
		BigInteger bigInt = new BigInteger(1, md.digest());
		log.info("***** END PROCESS MD5");
		fis.close();
		return bigInt.toString(16);
	}
	
	/**
	 * 圖片resize
	 * */
	public BufferedImage imgResize(BufferedImage img, int resizeWidth, int resizeHeight) throws Exception {
		Image image = img.getScaledInstance(resizeWidth, resizeHeight, Transparency.OPAQUE);
		BufferedImage resized = new BufferedImage(resizeWidth, resizeHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resized.createGraphics();
		graphics2D.drawImage(image, 0, 0, null);
		graphics2D.dispose();
		return resized;
	}
	
	/**
	 * 從圖片網址取得副檔名
	 * @param imgURL
	 * @return
	 */
	public static String getImgURLFilenameExtension(String imgURL) {
		// 處理圖片如果有被加timestamp等參數從?位置抓取副檔名，沒被加參數則直接依長度取最後3碼
		int startLength = (imgURL.indexOf("?") > -1 ? imgURL.indexOf("?") - 3 : imgURL.length() - 3);
		int endLength = (imgURL.indexOf("?") > -1 ? imgURL.indexOf("?") : imgURL.length());
		return imgURL.substring(startLength, endLength).toLowerCase();
	}
	
	/**
	 * 從圖片Base64取得副檔名
	 * @param imgBase64String
	 * @return
	 */
	public static String getImgBase64FilenameExtension(String imgBase64String) {
		String filenameExtension = imgBase64String.substring(imgBase64String.indexOf("/") + 1, imgBase64String.indexOf(";"));
		if ("jpeg".equalsIgnoreCase(filenameExtension)) {
			filenameExtension = "jpg";
		}
		return filenameExtension;
	}
	
	/**
	 * 從圖片網址取得附檔名或圖片Base64取得附檔名
	 * @param ecImgBase64
	 * @param ecImgUrl
	 * @return
	 */
	public static String getImgFilenameExtensionFromImgBase64OrImgURL(String ecImgBase64, String ecImgUrl) {
		if (StringUtils.isBlank(ecImgBase64)) {
			return getImgURLFilenameExtension(ecImgUrl);
		} else {
			return getImgBase64FilenameExtension(ecImgBase64);
		}
	}
	
	/**
	 * 如果此目錄路徑沒有資料夾，則建立資料夾
	 * @param path
	 */
	private static void createFolder(String path) {
		File file = new File(path);
		log.info("圖片建立資料夾路徑:" + path);
		if (!file.exists()) {
			log.info("建立資料夾");
			file.mkdirs(); // 建立資料夾
		}
	}

	/**
	 * 圖片最大尺寸比例壓縮
	 * 長或寬大於限制尺寸則等比壓縮至限制尺寸內
	 * @param imgPath 圖片完整路徑
	 * @param resizeImgPath 調整後圖片完整路徑
	 * @param maxSize 最大尺寸
	 * @throws Exception 
	 */
	public void imgMaxSizeProportionResize(String imgPath, String resizeImgPath, int maxSize) throws Exception {
		FileInputStream stream = new FileInputStream(new File(imgPath));
		BufferedImage bufferedImage = ImageIO.read(stream);
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		int limitSize = maxSize; // 限制尺寸

		if (width >= height && width > limitSize) { // 橫圖
			// 計算高的比例:限制 * 高 / 寬
			height = limitSize * height / width;
			// 寬等於限制值結果為 限制尺寸 X 比例高
			width = limitSize;
		} else if (width <= height && height > limitSize) { // 直圖
			// 計算寬的比例:限制 * 高 / 寬
			width = limitSize * width / height;
			// 高等於限制值結果為 比例寬 X 限制尺寸
			height = limitSize;
		}
		
		BufferedImage resizeBufferedImage = imgResize(bufferedImage, width, height);
		File file = new File(resizeImgPath);
        ImageIO.write(resizeBufferedImage, FilenameUtils.getExtension(file.getName()), file);
		stream.close();
	}
	
	/**
	 * 圖片最小尺寸比例壓縮
	 * 長或寬大於限制尺寸則等比壓縮至限制尺寸內
	 * @param imgPath 圖片完整路徑
	 * @param resizeImgPath 調整後圖片完整路徑
	 * @param minSize 最小尺寸
	 * @throws Exception 
	 */
	public void imgMinSizeProportionResize(String imgPath, String resizeImgPath, int minSize) throws Exception {
		FileInputStream stream = new FileInputStream(new File(imgPath));
		BufferedImage bufferedImage = ImageIO.read(stream);
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		int limitSize = minSize; // 限制尺寸

		// 已短邊固定限制尺寸，長邊按比例
		if (width >= height) { // 橫圖
			// 計算寬的比例:限制值 * 寬 / 高
			width = limitSize * width / height;
			// 高等於限制值結果為 比例寬 X 限制尺寸
			height = limitSize;
		} else if (width <= height) { // 直圖
			// 計算高的比例:限制值 * 高 / 寬
			height = limitSize * height / width;
			// 寬等於限制值結果為 限制尺寸 X 比例高
			width = limitSize;
		}
		
		BufferedImage resizeBufferedImage = imgResize(bufferedImage, width, height);
		File file = new File(resizeImgPath);
        ImageIO.write(resizeBufferedImage, FilenameUtils.getExtension(file.getName()), file);
		stream.close();
	}
}