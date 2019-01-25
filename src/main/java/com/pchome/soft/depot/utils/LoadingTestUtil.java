package com.pchome.soft.depot.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadingTestUtil {

    
	
	
	
	
    	
	public static void main(String avg[]){
		
		int threadRunTime=0;
		int totalTestTime=0;
		int threadNum=0;
		
		threadRunTime=Integer.parseInt(ConfigUtil.getInstance().getProperties("config/prop/LoadingTestData.properties", "threadRunTime"));
		totalTestTime=Integer.parseInt(ConfigUtil.getInstance().getProperties("onfig/prop/LoadingTestData.properties", "totalTestTime"));
		threadNum=Integer.parseInt(ConfigUtil.getInstance().getProperties("onfig/prop/LoadingTestData.properties", "threadNum"));
		
		System.out.println("----------Properties-------");
		System.out.println("threadRunTime ==> "+threadRunTime);
		System.out.println("threadRunTime ==> "+totalTestTime);
		System.out.println("threadRunTime ==> "+threadNum);

		Timer timer = new Timer();
		timer.schedule(new DateTask(threadNum), 3000,threadRunTime);

		System.out.println("----------Thraed start-------" + new Date());

		try {
			Thread.sleep(1000*60*totalTestTime);
		}
		catch(InterruptedException e) {
		}

		timer.cancel(); 
	}


}

class DateTask extends TimerTask {
	
	private int threadNum=5;
	
	public DateTask(int threadNum){
		this.threadNum=threadNum;
	}
	
	public void run() {
		//System.out.println("任務時間：" + new Date());


		String[] source=new String[]{
				"http://news.pchome.com.tw/science/tvbs/20111126/index-13223065217238339005.html",
				"http://news.pchome.com.tw/living/cna/20111128/index-13224413185050318009.html",
				"http://news.pchome.com.tw/sport/nextmedia/20111128/index-13224330001561916007.html",
				"http://news.pchome.com.tw/healthcare/uho/20111128/index-13224240002809559012.html",
		"http://news.pchome.com.tw/science/cna/20111128/index-13224411601812418005.html"};

		String keyString=HttpUtil.getInstance().getResultNoHtml(source[(int)(Math.random()*4)], "UTF-8");  


		//System.out.println("keyString="+keyString);

		final List<String> wordList=AnalyzerUtil.getInstance().getAnalyerList(keyString);



		ExecutorService service=Executors.newFixedThreadPool(20);  

		for(int i=1;i<=threadNum;i++){  
			final int count=i;  
			service.submit(new Runnable(){  

				@Override  
				public void run() { 

					int  tmp=(int)((Math.random()*wordList.size()));
					// System.out.println("word：" + wordList.get(tmp));

					String url="http://kwstg1.pchome.com.tw/search/?q="+wordList.get(tmp);
					//String url="http://news.pchome.com.tw/science/cna/20111128/index-13224411601812418005.html";
					//System.out.println("url：" + url);
					String result="";
					long startDate = Calendar.getInstance().getTimeInMillis();



					result=HttpUtil.getInstance().getResultNoHtml(url, "UTF-8");  

					//System.out.println("result："+result);
					long endDate = Calendar.getInstance().getTimeInMillis();

					double def=((endDate - startDate) / (double)1000 );
					//System.out.println("task"+count+": on "+Thread.currentThread().getName());
					//System.out.println("result：" + def+","+Thread.currentThread().getName()+","+wordList.get(tmp)+","+result.length()+","+result.substring(0,30));
					System.out.println("result====> " + def + " <==,"+Thread.currentThread().getName()+","+result.length());
				}  

			});  
		}  
		service.shutdown();  



	}
}
