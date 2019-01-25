package com.pchome.akbadm.quartzs;



import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.factory.pfbx.bonus.EveryMonthPfbBonus;
import com.pchome.config.TestConfig;
import com.pchome.soft.util.DateValueUtil;


@Transactional
public class PfbBonusMonthJob {

	protected Log log = LogFactory.getLog(this.getClass().getName());


	private EveryMonthPfbBonus everyMonthPfbBonus;	// 每月25 日計算獎金




    public void monthAuto(){
        log.info("====PfbBonusMonthJob.monthAuto() start====");

    	String monthValue=DateValueUtil.getInstance().getDateMonth();
    	
    	//每月1日跑，所以取得的月份要再減1
    	monthValue = String.valueOf((Integer.parseInt(monthValue) -1));
    	//送入月份
		this.monthBonusProcess(monthValue);

        log.info("====PfbBonusMonthJob.monthAuto() end====");
	}

    public void manual(String monthValue){
        log.info("====PfbBonusMonthJob.manual() start====");

    	this.monthBonusProcess(monthValue);

        log.info("====PfbBonusMonthJob.manual() end====");
	}


	private void monthBonusProcess(String monthValue){

		//這裡要補一些檢查
		//月份不能刪除重建，資料寫入要驗證多一點

		if(StringUtils.isNotEmpty(monthValue)){

			everyMonthPfbBonus.bonusConutProcess(monthValue);

		}

	}




	public void setEveryMonthPfbBonus(EveryMonthPfbBonus everyMonthPfbBonus) {
		this.everyMonthPfbBonus = everyMonthPfbBonus;
	}


	public static void main(String[] args) throws Exception {

		ApplicationContext context = null;
		String month = null;


		if(args.length < 2){

			System.out.println("args < 2 [local/stg/prd][start process month ex 6] ");

			//String[] test = {"local"};
			//context = new FileSystemXmlApplicationContext(TestConfig.getPath(test));
			//month = "6";

		}else{
			context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
			month = args[1].toString();
			PfbBonusMonthJob job = context.getBean(PfbBonusMonthJob.class);
			job.manual(month);
		}





    }
}
