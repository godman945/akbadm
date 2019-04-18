package com.pchome.akbadm.struts2.action.test;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;

import com.pchome.akbadm.struts2.BaseCookieAction;

public class ProdAdTrackDemo extends BaseCookieAction {
	 
	public String execute() throws Exception {
		 try{
//			 String image = "http://image.genquo.tw/product/G4000029501/G40000295-11.jpg";
//				URL url = new URL(image); // 將https網址改成http
//				HttpURLConnection urlConnection= (HttpURLConnection)url.openConnection();
//				urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//				urlConnection.setRequestMethod("GET");  
//		        String filenameExtension = "jpg";
//		        System.out.println(urlConnection.getResponseCode());
////		        // 處理圖片下載
////		        if ("gif".equalsIgnoreCase(filenameExtension)) { // gif圖片下載方式，此方式圖片才有動畫
////		            InputStream in = url.openStream();
//////		            Files.copy(in, new File(imgPathAndName).toPath(), StandardCopyOption.REPLACE_EXISTING);
////		            in.close();
////		        } else { // jpg、png圖片下載方式
//		            BufferedImage img = ImageIO.read(urlConnection.getInputStream());
//		            ImageIO.write(img, filenameExtension, new File("D:\\test_img\\test\\alex.jpg"));
////		        }
		        return SUCCESS;
		 }catch(Exception e){
			 e.printStackTrace();
			 return SUCCESS;
		 }
		
		
		 
	 }

	public static void main(String args[]) throws Exception{
		
		 StringBuffer buffer = null;  
	        try {  
	            // 建立连接  
	            URL url = new URL("http://image.genquo.tw/product/G4000029501/G40000295-11.jpg");  
	            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();  
	            httpUrlConn.setDoInput(true);  
	            httpUrlConn.setRequestMethod("GET");  
	            if(true){
	            	httpUrlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
	            }
	            System.out.println(httpUrlConn.getResponseCode());
//	            // 获取输入流  
//	            InputStream inputStream = httpUrlConn.getInputStream();  
//	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
//	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
//	  
//	            // 读取返回结果  
//	            buffer = new StringBuffer();  
//	            String str = null;  
//	            while ((str = bufferedReader.readLine()) != null) {  
//	                buffer.append(str);  
//	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }


	}
}
