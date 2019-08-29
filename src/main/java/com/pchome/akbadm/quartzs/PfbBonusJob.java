package com.pchome.akbadm.quartzs;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.factory.pfbx.bonus.DeletePfbBonus;
import com.pchome.akbadm.factory.pfbx.bonus.EveryDayPfbBonus;
import com.pchome.config.TestConfig;
import com.pchome.soft.util.DateValueUtil;


@Transactional
public class PfbBonusJob {

	protected Logger log = LogManager.getRootLogger();

	private DeletePfbBonus deletePfbBonus;						// 刪除日期後的資料
	private EveryDayPfbBonus everyDayPfbBonus;					// 每天計算獎金


	public void bonusEstimatedProcess(String date){
        log.info("====PfbBonusJob.bonusEstimatedProcess() start====");

		String today="";

		if(StringUtils.isBlank(date)){
	        today = DateValueUtil.getInstance().getDateValue(DateValueUtil.TODAY, DateValueUtil.DBPATH);
		}else{
			today=date;
		}

		log.info("process date ="+today);
		//String today="2015-06-09";
		// 刪除日期之後的資料
		deletePfbBonus.deleteProcess(today);
		log.info("deleteProcess success");
		everyDayPfbBonus.bonusEstimatedProcess(today);

        log.info("====PfbBonusJob.bonusEstimatedProcess() end====");
	}

	public void auto(){
        log.info("====PfbBonusJob.auto() start====");

		String yesterday = DateValueUtil.getInstance().getDateValue(DateValueUtil.YESTERDAY, DateValueUtil.DBPATH);
		log.info("Process date="+yesterday);
		this.bonusProcess(yesterday);

        log.info("====PfbBonusJob.auto() end====");
	}


	public void manual(String startDate){
        log.info("====PfbBonusJob.manual() start====");

		this.bonusProcess(startDate);

        log.info("====PfbBonusJob.manual() end====");
	}

	public void autoEstimated(){
        log.info("====PfbBonusJob.autoEstimated() start====");

        String today = DateValueUtil.getInstance().getDateValue(DateValueUtil.TODAY, DateValueUtil.DBPATH);
		this.bonusEstimatedProcess(today);

        log.info("====PfbBonusJob.autoEstimated() end====");
	}






	private void bonusProcess(String startDate){



		// 刪除日期之後的資料
		deletePfbBonus.deleteProcess(startDate);

		// 結束日期
		String yesterday = DateValueUtil.getInstance().getDateValue(DateValueUtil.YESTERDAY, DateValueUtil.DBPATH);

		// 開始日期
		String sDate = this.getStartDate(startDate);
		// 差幾天
		long diffyDay = DateValueUtil.getInstance().getDateDiffDay(startDate, yesterday);
		// 計算日期
		Date cDate = null;
		int countDay = 0;

		// 逐天處理
		while(diffyDay > 0){

			cDate = DateValueUtil.getInstance().getDateForStartDateAddDay(sDate, countDay);

			log.info("cDate="+cDate);

			// 每天計算 Pfb 獎金
			// 佣金大表計算
			everyDayPfbBonus.bonusConutProcess(cDate);


			diffyDay--;
			countDay++;
		}

	}

	private String getStartDate(String startDate){

		String sDate = null;

		if(StringUtils.isBlank(startDate)){
			sDate = DateValueUtil.getInstance().getDateValue(DateValueUtil.YESTERDAY, DateValueUtil.DBPATH);
		}else{
			sDate = startDate;
		}

		return sDate;
	}


	public void setDeletePfbBonus(DeletePfbBonus deletePfbBonus) {
		this.deletePfbBonus = deletePfbBonus;
	}



	public static void main(String[] args) throws Exception {

		ApplicationContext context = null;
		String date = null;

		if(args.length < 3){

			System.out.println("args < 3 [local/stg/prd][bonus/todaybonus][start process date] ");


			//String[] test = {"local"};
			//context = new FileSystemXmlApplicationContext(TestConfig.getPath(test));
			//date = "2015-6-09";

		}else{

			context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
			date = args[2].toString();
			PfbBonusJob job = context.getBean(PfbBonusJob.class);
			if(args[1].equals("bonus")){
				job.manual(date);
			}else{
				if(args[1].equals("todaybonus")){
					job.bonusEstimatedProcess(date);
				}
			}



		}




    }


	public void setEveryDayPfbBonus(EveryDayPfbBonus everyDayPfbBonus) {
		this.everyDayPfbBonus = everyDayPfbBonus;
	}



}
