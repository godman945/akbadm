package com.pchome.akbadm.quartzs;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.factory.controlPrice.AControlPrice;
import com.pchome.akbadm.factory.controlPrice.ControlPriceFactory;
import com.pchome.akbadm.factory.recognize.RecognizeProcess;
import com.pchome.akbadm.factory.settlement.SettlementProcess;
import com.pchome.config.TestConfig;
import com.pchome.enumerate.factory.EnumControlPriceItem;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;
import com.pchome.soft.util.DateValueUtil;

/**
 * 調整帳戶金額排程
 * 1. 結算帳戶
 * 2. 攤提帳戶
 * 3. 計算調控金額
 */
@Transactional
public class AdjustAccountJob {

	protected Logger log = LogManager.getRootLogger();

	private IPfpCustomerInfoService customerInfoService;
	private SettlementProcess settlementProcess;
	private RecognizeProcess recognizeProcess;
	private ControlPriceFactory controlPriceFactory;
	private IAdmAccesslogService accesslogService;

	/**
	 * 排程起啟點
	 */
	public void  process() {
	    process(null);
	}

    /**
     * 手動執行排程
     */
    public void  process(String date) {
        log.info("====AdjustAccountJob.process() start====");
        
		try {
			
			String yesterday="";
			if (StringUtils.isBlank(date)) {
				yesterday = DateValueUtil.getInstance().getDateValue(DateValueUtil.YESTERDAY, DateValueUtil.DBPATH);
			}else{
				yesterday = date;
			}
			String today = DateValueUtil.getInstance().getDateValue(DateValueUtil.TODAY, DateValueUtil.DBPATH);
			String tomorrow = DateValueUtil.getInstance().getDateValue(DateValueUtil.TOMORROW, DateValueUtil.DBPATH);
			
			// 先取出昨日開始至今有交易的pfp，撈此區間有交易pfp_trans_detail的PFP
			List<String> transDetailPfpList = customerInfoService.findTransDetailPfp(yesterday , today , tomorrow);
			log.info("transDetailPfpList size " + transDetailPfpList.size());
			
			List<PfpCustomerInfo> transDetailPfps = customerInfoService.findCustomerInfoIds(transDetailPfpList);
			log.info("transDetailPfps size " + transDetailPfps.size());
			
			log.info("date >>> " + date);
			log.info("yesterday >>> " + yesterday);
			log.info("today >>> " + today);
			log.info("tomorrow >>> " + tomorrow);
			
			
			
			long start = 0l;
			long end = 0l;

			// 調整儲值金、禮金.......等，並寫入交易明細(會把之前的資料先清掉)
            start = Calendar.getInstance().getTimeInMillis();
			settlementProcess.startProcess(transDetailPfps, date);
            end = Calendar.getInstance().getTimeInMillis();
            log.info("settlementProcess.startProcess2 " + (end - start)/1000);

			// 調整計算每日攤提
            start = Calendar.getInstance().getTimeInMillis();
			recognizeProcess.startProcess(transDetailPfps, date);
            end = Calendar.getInstance().getTimeInMillis();
            log.info("recognizeProcess.startProcess " + (end - start)/1000);

            
            // 廣告調控金額PFP要全跑
            List<PfpCustomerInfo> validCustomerInfos = customerInfoService.findValidCustomerInfo();
			// 調整計算調控金額
            start = Calendar.getInstance().getTimeInMillis();
			this.controlPriceProcess(validCustomerInfos);
            end = Calendar.getInstance().getTimeInMillis();
            log.info("controlPriceProcess " + (end - start)/1000);
            
		}
		catch (Exception e) {
		    log.error(date, e);

			accesslogService.addAdmAccesslog(EnumAccesslogChannel.ADM,
												EnumAccesslogAction.ERROR,
												"排程檢查 - 每日01:30排程發生錯誤",
												"system",
												null,
												"",
												null,
												"127.0.0.1",
												EnumAccesslogEmailStatus.YES);
		}

        log.info("====AdjustAccountJob.process() end====");
	}

	/**
	 * 調控金額
	 */
	private void controlPriceProcess(List<PfpCustomerInfo> customerInfos) {
		for (PfpCustomerInfo customerInfo : customerInfos) {

			AControlPrice controlPriceItem = controlPriceFactory.get(EnumControlPriceItem.EVERYDAY);

			if(controlPriceItem != null){
				controlPriceItem.contrlPrice(customerInfo);
			}
		}
	}

	public void setCustomerInfoService(IPfpCustomerInfoService customerInfoService) {
		this.customerInfoService = customerInfoService;
	}

	public void setSettlementProcess(SettlementProcess settlementProcess) {
		this.settlementProcess = settlementProcess;
	}

	public void setRecognizeProcess(RecognizeProcess recognizeProcess) {
		this.recognizeProcess = recognizeProcess;
	}

	public void setControlPriceFactory(ControlPriceFactory controlPriceFactory) {
		this.controlPriceFactory = controlPriceFactory;
	}

	public void setAccesslogService(IAdmAccesslogService accesslogService) {
		this.accesslogService = accesslogService;
	}

	public static void main(String[] args) throws Exception {
	    String date = null;

		if(args == null) {
			return;
		}
		else if (args.length == 1) {
			date = null;
		}
		else if (args.length == 2) {
		    date = args[1];
		}
		else {
		    return;
		}

		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
		AdjustAccountJob job = context.getBean(AdjustAccountJob.class);
		job.process(date);

    }
}
