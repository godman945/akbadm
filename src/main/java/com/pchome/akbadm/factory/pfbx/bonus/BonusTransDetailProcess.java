package com.pchome.akbadm.factory.pfbx.bonus;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.pchome.akbadm.db.pojo.PfbxBoard;
import com.pchome.akbadm.db.pojo.PfbxBonusApply;
import com.pchome.akbadm.db.pojo.PfbxBonusBill;
import com.pchome.akbadm.db.pojo.PfbxBonusTransDetail;
import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.pojo.PfbxInvalidTraffic;
import com.pchome.akbadm.db.service.pfbx.board.IPfbxBoardService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusBillService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusDayReportService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusTransDetailService;
import com.pchome.akbadm.db.service.pfbx.invalidclick.IPfbxInvalidTrafficService;
import com.pchome.akbadm.utils.DateTimeUtils;
import com.pchome.enumerate.pfbx.board.EnumBoardContent;
import com.pchome.enumerate.pfbx.bonus.EnumPfbLimit;
import com.pchome.enumerate.pfbx.bonus.EnumPfbxBonus;
import com.pchome.enumerate.pfbx.bonus.EnumPfbxBonusTrans;
import com.pchome.rmi.board.EnumPfbBoardType;
import com.pchome.soft.util.DateValueUtil;


public class BonusTransDetailProcess {

    //spring inject
    private IPfbxBonusTransDetailService pfbxBonusTransDetailService;
    private IPfbxBonusDayReportService pfbxBonusDayReportService;
    private IPfbxBonusBillService pfbxBonusBillService;
    private IPfbxBoardService pfbxBoardService;
    private IPfbxInvalidTrafficService pfbxInvalidTrafficService;

    protected Logger log = LogManager.getRootLogger();

    public void insertItemTransDetail(PfbxCustomerInfo pfb, EnumPfbxBonusTrans enumPfbxBonusTrans, String monthValue, PfbxBonusApply pfbxBonusApply) {

        //pfbx_bonus_trans_detail 開始結束日期 取本月跟上個月
        Date thisMonthStartDate = this.getThisMonthStartDate(monthValue);
        Date thisMonthEndDate = this.getThisMonthEndDate(monthValue);
        Date preMonthStartDate = this.getPreMonthStartDate(monthValue);
        Date preMonthEndDate = this.getPreMonthEndDate(monthValue);

        //transRemain 目前可以請款的分潤金額，會隨申請單產生扣除，讓下一筆申請單不會請到已申請的金額
        switch (enumPfbxBonusTrans) {
            case BONUS_START: //每月開始收益
                log.info("bonus start");
                //transRemain 不動

                insertThisMonthStartBonusRemain(pfb, thisMonthStartDate, thisMonthEndDate);

                //更新 pfbx_bonus_bill table //未請款金額
                updatePfbxBounsBill(pfb);
                break;
            case APPLY_SUCCESS: //請款成功
                log.info("apply success");
                //insertApplyOrderSuccess(pfb,thisMonthStartDate,thisMonthEndDate);

                //更新 pfbx_bonus_bill table //未請款金額
                updatePfbxBounsBill(pfb);
                break;
            case APPLY_FALE: //請款失敗
                log.info("apply fail");
                insertApplyOrderFail(pfb, thisMonthStartDate, thisMonthEndDate, pfbxBonusApply);

                //更新 pfbx_bonus_bill table //未請款金額
                updatePfbxBounsBill(pfb);


                break;
            case ADD_EXPENSE: //新增收益
                log.info("add expense");
                //transRemain 壘加
                insertPreMonthBonusRemain(pfb, preMonthStartDate, preMonthEndDate);

                //有收益時要更新
                //更新 pfbx_bonus_bill table //未請款金額
                updatePfbxBounsBill(pfb);
                break;
            case APPLY_PROCESS: //建立請款單
                log.info("apply process");
                //transRemain 減掉
                //建立請款單
                //標記已請款的紀錄
                insertThisMonthApplyOrder(pfb, thisMonthStartDate, thisMonthEndDate, pfbxBonusApply);

                //有收益時要更新
                //更新 pfbx_bonus_bill table //未請款金額
                updatePfbxBounsBill(pfb);

                break;
            case BONUS_ADD: //收益調整增加

                break;
            case BONUS_LESS: //受益調整減少

                break;
            case INVALID_TRAFFIC: //無效流量
            	log.info("add invalid traffic");
            	
            	insertInvalidTrafficBonusRemain(pfb, preMonthStartDate, preMonthEndDate);
            	
            	//更新 pfbx_bonus_bill table //未請款金額
                updatePfbxBounsBill(pfb);
            	
                break;
        }
    }

    private void insertThisMonthApplyOrder(PfbxCustomerInfo pfb, Date StartDate, Date EndDate, PfbxBonusApply pfbxBonusApply) {

        //取最後一筆 PFB 的分潤金額
        //transRemainMoney 目前可分潤金額
        //transTotalMoney 分潤總金額,真正撥款成功才會扣
        float transRemainMoney = pfbxBonusApply.getApplyMoney();
        //float transTotalMoney = this.getLastTransRemain(pfb);

        Date today = new Date();

        //可請款餘額大於請款門檻
        if (transRemainMoney >= (EnumPfbLimit.MIN.getMoney() - (EnumPfbLimit.MIN.getMoney() * 0.05))) {

            //建立請款單
            //PfbxBonusApply pfbxBonusApply = applyOrderProcess.doStatusProcess(pfb, EnumPfbApplyStatus.APPLY, null,transRemainMoney);

            //建立成功
            if (pfbxBonusApply != null) {

                //可請款金額歸0
                transRemainMoney = 0.0f;

                PfbxBonusTransDetail transDetail = new PfbxBonusTransDetail();

                transDetail.setPfbId(pfb.getCustomerInfoId());
                transDetail.setRecordDate(today);
                transDetail.setStartDate(today);
                transDetail.setEndDate(today);
                transDetail.setTransItem(EnumPfbxBonusTrans.APPLY_PROCESS.getType());
                transDetail.setApplyId(pfbxBonusApply.getApplyId());
                transDetail.setTransDesc(EnumPfbxBonusTrans.APPLY_PROCESS.getDesc());
                transDetail.setTransIn(0);
                transDetail.setTransOut(pfbxBonusApply.getApplyMoney());
                transDetail.setTransRemain(transRemainMoney);
                transDetail.setTotalRemain(0);
                transDetail.setCreateDate(today);

                pfbxBonusTransDetailService.saveOrUpdate(transDetail);

            }
            //標記已請款的紀錄
            markTransDetailApplyId(pfb, pfbxBonusApply);
        }
    }


    private void insertPreMonthBonusRemain(PfbxCustomerInfo pfb, Date StartDate, Date EndDate) {

        //取所有apply id =null 分潤金額大於 0 的紀錄(等於是撈錢幾個月沒有請的錢)
        //transRemainMoney 目前可分潤金額
        float transRemainMoney = this.getLastTransRemain(pfb);
        //float transTotalMoney = this.getLastTransRemain(pfb);

        //取得 pfb 上月的分潤金額
        float monthRemain = 0.0f;
        monthRemain = pfbxBonusDayReportService.findPfbxTotalMonthBonus(pfb.getCustomerInfoId(), StartDate, EndDate);

        if (monthRemain > 0) {
        	
        	monthRemain = new BigDecimal(String.valueOf(monthRemain)).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        	
            //之前沒請的錢加上這次要請的錢
        	//2017-06-13  按PM規則修改
        	//  新增分潤(先四捨五入) = 餘額
        	//  未來如果餘額被查出有落差是因為這公式的關係，工程這不負任何責任
            transRemainMoney = new BigDecimal(String.valueOf(transRemainMoney)).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
            monthRemain = new BigDecimal(String.valueOf(monthRemain)).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();

            transRemainMoney += monthRemain;
            //transTotalMoney+=monthRemain;
            

            PfbxBonusTransDetail transDetail = new PfbxBonusTransDetail();

            Date today = new Date();

            transDetail.setPfbId(pfb.getCustomerInfoId());
            transDetail.setRecordDate(today);
            transDetail.setStartDate(StartDate);
            transDetail.setEndDate(EndDate);
            transDetail.setTransItem(EnumPfbxBonusTrans.ADD_EXPENSE.getType());
            transDetail.setTransDesc(EnumPfbxBonusTrans.ADD_EXPENSE.getDesc());
            transDetail.setTransIn(monthRemain);
            transDetail.setTransOut(0);
            transDetail.setTransRemain(transRemainMoney);
            transDetail.setTotalRemain(0);
            transDetail.setCreateDate(today);

            pfbxBonusTransDetailService.saveOrUpdate(transDetail);
        }
    }

    private void insertInvalidTrafficBonusRemain(PfbxCustomerInfo pfb, Date StartDate, Date EndDate){
    	
    	float transRemainMoney = this.getLastTransRemain(pfb);
    	
    	//取得 pfb 無效流量分潤
        float invalidRemain = 0.0f;
        
        List<PfbxInvalidTraffic> dataList = pfbxInvalidTrafficService.findInvalidTraffic(pfb.getCustomerInfoId());
        
        if(!dataList.isEmpty()){
        	for(PfbxInvalidTraffic data:dataList){
        		invalidRemain += data.getInvPfbBonus();
        		data.setCloseDate(new Date());
        		data.setUpdateTime(new Date());
        	}
        	
        	pfbxInvalidTrafficService.saveOrUpdateAll(dataList);
        }	
    	
        if (invalidRemain > 0) {
            //餘額扣除無效流量分潤
        	
        	//2017-06-13  按PM規則修改
        	//  新增分潤(先四捨五入) - 無效流量(先四捨五入) = 餘額
        	//  未來如果餘額被查出有落差是因為這公式的關係，工程這不負任何責任
        	transRemainMoney = new BigDecimal(String.valueOf(transRemainMoney)).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue(); 
        	invalidRemain = new BigDecimal(String.valueOf(invalidRemain)).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        	transRemainMoney -= invalidRemain;

            PfbxBonusTransDetail transDetail = new PfbxBonusTransDetail();

            Date today = new Date();

            transDetail.setPfbId(pfb.getCustomerInfoId());
            transDetail.setRecordDate(today);
            transDetail.setStartDate(StartDate);
            transDetail.setEndDate(EndDate);
            transDetail.setTransItem(EnumPfbxBonusTrans.INVALID_TRAFFIC.getType());
            transDetail.setTransDesc(EnumPfbxBonusTrans.INVALID_TRAFFIC.getDesc());
            transDetail.setTransIn(0);
            transDetail.setTransOut(invalidRemain);
            transDetail.setTransRemain(transRemainMoney);
            transDetail.setTotalRemain(0);
            transDetail.setCreateDate(today);

            pfbxBonusTransDetailService.saveOrUpdate(transDetail);
        }
        
    }
    

    private void updatePfbxBounsBill(PfbxCustomerInfo pfb) {

    	//2017-06-13  按PM規則修改
    	//  新增分潤(先四捨五入) - 無效流量(先四捨五入) = 餘額
    	//  未來如果餘額被查出有落差是因為這公式的關係，工程這不負任何責任
    	
        float billRemain = 0.0f;
        float tranIn = 0.0f;
        float tranOut = 0.0f;
        //float totalApplyMoney=0.0f;
        //float applyMoney=0.0f;
        //float lastApplyMoney=0.0f;

        List<PfbxBonusTransDetail> pfbTransDetailList = pfbxBonusTransDetailService.findApplyNullPfbxBonusTransDetail(pfb.getCustomerInfoId());
        //找新增分潤未請款的紀錄
        if (pfbTransDetailList != null) {

            for (PfbxBonusTransDetail ptd : pfbTransDetailList) {
            	tranIn += ptd.getTransIn();
                log.info("billRemain id=" + ptd.getId() + "," + ptd.getTransIn());
            }
        }
        
        List<PfbxBonusTransDetail> pfbInvalidTransDetailList = pfbxBonusTransDetailService.findApplyNullPfbxBonusInvalidTransDetail(pfb.getCustomerInfoId());
        //找無效流量未請款的紀錄
        if (pfbInvalidTransDetailList != null) {

            for (PfbxBonusTransDetail pitd : pfbInvalidTransDetailList) {
            	tranOut += pitd.getTransOut();
            	log.info("billRemain id=" + pitd.getId() + "," + pitd.getTransOut());
            }
        }
        
        billRemain += new BigDecimal(String.valueOf(tranIn)).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        billRemain -= new BigDecimal(String.valueOf(tranOut)).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        
    	/*
    	
    	pfbTransDetailList = pfbxBonusTransDetailService.findListPfbxBonusTransDetail(pfb.getCustomerInfoId());
        
    	if(pfbTransDetailList != null){
			
			for(PfbxBonusTransDetail ptd:pfbTransDetailList){
				if(ptd.getTransItem().equals(EnumPfbxBonusTrans.APPLY_SUCCESS.getType())){
					totalApplyMoney+=ptd.getTransIn();
					log.info("totalApplyMoney="+totalApplyMoney);
				}
				if(ptd.getTransItem().equals(EnumPfbxBonusTrans.APPLY_PROCESS.getType())){
					applyMoney+=ptd.getPfbxBonusApply().getApplyMoney();
					log.info("applyMoney="+applyMoney);
				}
				if(ptd.getTransItem().equals(EnumPfbxBonusTrans.APPLY_SUCCESS.getType())){
					lastApplyMoney=ptd.getPfbxBonusApply().getApplyMoney();
					log.info("lastApplyMoney="+lastApplyMoney);
				}
			}
		}
    	
		*/
        PfbxBonusBill pfbxBonusBill = pfbxBonusBillService.findPfbxBonusBill(pfb.getCustomerInfoId());


        // 更新 pfbx_bonus_bill
        Date today = new Date();

        if (pfbxBonusBill == null) {

            pfbxBonusBill = new PfbxBonusBill();

            pfbxBonusBill.setPfbId(pfb.getCustomerInfoId());
            pfbxBonusBill.setMinLimit(EnumPfbLimit.MIN.getMoney());
            pfbxBonusBill.setPfbBonusSetStatus(EnumPfbxBonus.DEFAULT.getEnumBonusSet().getStatus());
            pfbxBonusBill.setCreateDate(today);
        }

        //這裡處理尚未請款的金額，其他金額在 ApplyOrddrProcess 處理
        pfbxBonusBill.setBillRemain(billRemain);//未請款金額
        //pfbxBonusBill.setApplyMoney(applyMoney);
        //pfbxBonusBill.setLastApplyMoney(lastApplyMoney);
        //pfbxBonusBill.setTotalApplyMoney(totalApplyMoney);
        pfbxBonusBill.setUpdateDate(today);

        pfbxBonusBillService.saveOrUpdate(pfbxBonusBill);
    }


    private void insertApplyOrderSuccess(PfbxCustomerInfo pfb, Date StartDate, Date EndDate) {

        //取最後一筆 PFB 的分潤金額
        //transRemainMoney 目前可分潤金額
        //transTotalMoney 分潤總金額,真正撥款成功才會扣
        //float transRemainMoney = this.getLastTransRemain(pfb);
        //float transTotalMoney = this.getLastTransRemain(pfb);

        PfbxBonusTransDetail transDetail = new PfbxBonusTransDetail();

        Date today = new Date();

        transDetail.setPfbId(pfb.getCustomerInfoId());
        transDetail.setRecordDate(today);
        transDetail.setStartDate(StartDate);
        transDetail.setEndDate(StartDate);
        transDetail.setTransItem(EnumPfbxBonusTrans.APPLY_SUCCESS.getType());
        transDetail.setTransDesc(EnumPfbxBonusTrans.APPLY_SUCCESS.getDesc());
        transDetail.setTransIn(0);
        transDetail.setTransOut(0);
        transDetail.setTransRemain(0);
        transDetail.setTotalRemain(0);
        transDetail.setCreateDate(today);

        pfbxBonusTransDetailService.saveOrUpdate(transDetail);

    }

    private void insertApplyOrderFail(PfbxCustomerInfo pfb, Date StartDate, Date EndDate, PfbxBonusApply pfbxBonusApply) {

        //取最後一筆 PFB 的分潤金額
        //transRemainMoney 目前可分潤金額
        //transTotalMoney 分潤總金額,真正撥款成功才會扣
        float transRemainMoney = this.getLastTransRemain(pfb);
        //float transTotalMoney = this.getLastTransRemain(pfb);

        transRemainMoney += pfbxBonusApply.getApplyMoney();

        PfbxBonusTransDetail transDetail = new PfbxBonusTransDetail();

        Date today = new Date();

        transDetail.setPfbId(pfb.getCustomerInfoId());
        transDetail.setRecordDate(today);
        transDetail.setStartDate(StartDate);
        transDetail.setEndDate(StartDate);
        transDetail.setTransItem(EnumPfbxBonusTrans.APPLY_FALE.getType());
        transDetail.setTransDesc(EnumPfbxBonusTrans.APPLY_FALE.getDesc());
        transDetail.setTransIn(pfbxBonusApply.getApplyMoney());
        transDetail.setTransOut(0);
        transDetail.setPfbxBonusApply(pfbxBonusApply);
        transDetail.setTransRemain(transRemainMoney);
        transDetail.setTotalRemain(0);
        transDetail.setCreateDate(today);

        pfbxBonusTransDetailService.saveOrUpdate(transDetail);
    }

    private void insertThisMonthStartBonusRemain(PfbxCustomerInfo pfb, Date StartDate, Date EndDate) {

        //取最後一筆 PFB 的分潤金額
        //transRemainMoney 目前可分潤金額
        //transTotalMoney 分潤總金額,真正撥款成功才會扣
        float transRemainMoney = this.getLastTransRemain(pfb);
        transRemainMoney = new BigDecimal(String.valueOf(transRemainMoney)).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
        //float transTotalMoney = this.getLastTransRemain(pfb);

        PfbxBonusTransDetail transDetail = new PfbxBonusTransDetail();

        Date today = new Date();

        transDetail.setPfbId(pfb.getCustomerInfoId());
        transDetail.setRecordDate(today);
        transDetail.setStartDate(StartDate);
        transDetail.setEndDate(StartDate);
        transDetail.setTransItem(EnumPfbxBonusTrans.BONUS_START.getType());
        transDetail.setTransDesc(EnumPfbxBonusTrans.BONUS_START.getDesc());
        transDetail.setTransIn(0);
        transDetail.setTransOut(0);
        transDetail.setTransRemain(transRemainMoney);
        transDetail.setTotalRemain(0);
        transDetail.setCreateDate(today);

        pfbxBonusTransDetailService.saveOrUpdate(transDetail);

    }


    private void markTransDetailApplyId(PfbxCustomerInfo pfb, PfbxBonusApply pfbxBonusApply) {

        List<PfbxBonusTransDetail> pfbTransDetailList = pfbxBonusTransDetailService.findApplyNullPfbxBonusTransDetail(pfb.getCustomerInfoId());

        if (pfbTransDetailList != null) {

            for (PfbxBonusTransDetail ptd : pfbTransDetailList) {
                ptd.setApplyId(pfbxBonusApply.getApplyId());
                pfbxBonusTransDetailService.saveOrUpdate(ptd);
            }
        }
        
        List<PfbxBonusTransDetail> pfbInvalidTransDetailList = pfbxBonusTransDetailService.findApplyNullPfbxBonusInvalidTransDetail(pfb.getCustomerInfoId());

        if (pfbInvalidTransDetailList != null) {

            for (PfbxBonusTransDetail pitd : pfbInvalidTransDetailList) {
            	pitd.setApplyId(pfbxBonusApply.getApplyId());
                pfbxBonusTransDetailService.saveOrUpdate(pitd);
            }
        }
    }


    private float getLastTransRemain(PfbxCustomerInfo pfb) {

        //取最後一筆 PFB 的總餘額紀錄當下個月的開始分潤餘額
        //getTransRemain 目前可申請餘額
        //申請產出 TransRemain 就會扣 TotalRemain 不會扣

        float transRemain = 0.0f;

        List<PfbxBonusTransDetail> pfbTransDetailList = pfbxBonusTransDetailService.findApplyNullPfbxBonusTransDetail(pfb.getCustomerInfoId());

        if (pfbTransDetailList != null) {

            for (PfbxBonusTransDetail ptd : pfbTransDetailList) {
                transRemain += ptd.getTransIn();
            }
        }

        List<PfbxBonusTransDetail> pfbInvalidTransDetailList = pfbxBonusTransDetailService.findApplyNullPfbxBonusInvalidTransDetail(pfb.getCustomerInfoId());
        
        if (pfbInvalidTransDetailList != null) {

            for (PfbxBonusTransDetail pitd : pfbInvalidTransDetailList) {
                transRemain -= pitd.getTransOut();
            }
        }

        return transRemain;
    }
	/*
	private float getLastTotalRemain(PfbxCustomerInfo pfb){
        
		//取最後一筆 PFB 的總餘額紀錄當下個月的開始分潤餘額
		//getTransRemain 目前可申請餘額
		//申請產出 TransRemain 就會扣 TotalRemain 不會扣
		PfbxBonusTransDetail lastPfbTransDetail = pfbxBonusTransDetailService.findLastPfbxBonusTransDetail(pfb.getCustomerInfoId());
				
		float totalRemain = 0.0f;
				
		if(lastPfbTransDetail != null){
			totalRemain= lastPfbTransDetail.getTotalRemain();
		}
				
		return totalRemain;
	}
	*/


    private Date getPreMonthEndDate(String monthValue) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Date dt = null;

        try {
            //傳回每個月 1 號的日期
            dt = sdf.parse(this.getProcessate(monthValue));
        } catch (Exception e) {

        }
        Calendar rightNow = Calendar.getInstance();
        //跨年份問題1月計算的12月為上一個年度
        if(monthValue.equals("12")) {
        	rightNow.add(Calendar.YEAR,-1);
        }
        rightNow.setTime(dt);

        //rightNow.add(Calendar.YEAR,-1);
        //rightNow.add(Calendar.MONTH,-1);
        //rightNow.add(Calendar.DAY_OF_YEAR, -1);//減一天
        rightNow.set(Calendar.DATE, rightNow.getActualMaximum(Calendar.DATE));	//取當月最後一天

        Date dt1 = rightNow.getTime();

        String reStr = sdf.format(dt1);

        log.info("PreMonthEndDate=" + reStr);

        return dt1;
    }


    private Date getPreMonthStartDate(String monthValue) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Date dt = null;

        try {
            //傳回每個月 1 號的日期
            dt = sdf.parse(this.getProcessate(monthValue));
        } catch (Exception e) {

        }
        
        Calendar rightNow = Calendar.getInstance();
        //跨年份問題1月計算的12月為上一個年度
        if(monthValue.equals("12")) {
        	rightNow.add(Calendar.YEAR,-1);
        }
        rightNow.setTime(dt);

        //rightNow.add(Calendar.YEAR,-1);
        //rightNow.add(Calendar.MONTH, -1);//減一個月 上個月 1
        //rightNow.add(Calendar.DAY_OF_YEAR,-1);

        rightNow.set(Calendar.DATE, 1);	//取當月最第一天
        
        Date dt1 = rightNow.getTime();

        String reStr = sdf.format(dt1);

        log.info("ThisPreMonthStartDate=" + reStr);


        return dt1;
    }


    private Date getThisMonthEndDate(String monthValue) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Date dt = null;

        try {
            //傳回每個月 1 號的日期
            dt = sdf.parse(this.getProcessate(monthValue));
        } catch (Exception e) {

        }
        Calendar rightNow = Calendar.getInstance();
        //跨年份問題1月計算的12月為上一個年度
        if(monthValue.equals("12")) {
        	rightNow.add(Calendar.YEAR,-1);
        }
        rightNow.setTime(dt);

        //rightNow.add(Calendar.YEAR,-1);
        //rightNow.add(Calendar.MONTH, 1);//加一個月
        //rightNow.add(Calendar.DAY_OF_YEAR, -1);//減一天 到下個月25
        rightNow.set(Calendar.DATE, rightNow.getActualMaximum(Calendar.DATE));	//取當月最後一天

        Date dt1 = rightNow.getTime();

        String reStr = sdf.format(dt1);

        log.info("ThisMonthEndDate=" + reStr);


        return dt1;

    }


    private Date getThisMonthStartDate(String monthValue) {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Date dt = null;

        try {
            //傳回每個月 1 號的日期
            dt = sdf.parse(this.getProcessate(monthValue));
        } catch (Exception e) {

        }
        Calendar rightNow = Calendar.getInstance();
        
        //跨年份問題1月計算的12月為上一個年度
        if(monthValue.equals("12")) {
        	rightNow.add(Calendar.YEAR,-1);
        }
        rightNow.setTime(dt);

        Date dt1 = rightNow.getTime();

        String reStr = sdf.format(dt1);

        log.info("ThisMonthStartDate=" + reStr);

        return dt1;
    }

    //傳回每個月 1 號的日期
    public String getProcessate(String monthValue) {
        if (monthValue.length() == 1) {
            monthValue = "0" + monthValue;
        }
        
        String sd = "";
        //跨年份問題1月計算的12月為上一個年度
        if(monthValue.equals("12")) {
        	String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)-1);
        	sd = year + monthValue + "01";
        }else {
        	sd = DateValueUtil.getInstance().getDateYear() + monthValue + "01";
        }

        return sd;
    }

    /**
     * 偵測是否已達可申請款項門檻，並建立公告
     * @param pfb
     */
    public void buildBonusBoard(PfbxCustomerInfo pfb) {
        String customerInfoId = pfb.getCustomerInfoId();
        Date now = DateTimeUtils.getDate();
        //檢查分潤數字，若大於 1000 寫公告
        PfbxBonusBill pfbxBonusBill = pfbxBonusBillService.findPfbxBonusBill(customerInfoId);

        if (pfbxBonusBill != null
                && pfbxBonusBill.getBillRemain() > pfbxBonusBill.getMinLimit()) {

            //查詢是否寫過公告了，寫過就不再寫了
            Map<String, String> conditionMap2 = new HashMap<String, String>();
            conditionMap2.put("pfbxCustomerInfoId", customerInfoId);
            conditionMap2.put("deleteId", EnumBoardContent.BOARD_CONTENT_4.getId()); //todo 尚待確認如何判斷
            try {
                List<PfbxBoard> pfbxBoards = pfbxBoardService.findPfbxBoard(conditionMap2);
                if (pfbxBoards == null
                        || pfbxBoards.size() == 0) {
                    PfbxBoard board = new PfbxBoard();
                    board.setBoardType(EnumPfbBoardType.REMIND.getType());
                    String content = "<a class='boardContentLink' href='pfb/bonusTransDetail.html'>" + EnumBoardContent.BOARD_CONTENT_4.getContent() + "</a> ";
                    board.setBoardContent(content);
                    board.setPfbxCustomerInfoId(customerInfoId);
                    board.setStartDate(now);
                    board.setEndDate(new Date(now.getTime() + (1000 * 60 * 60 * 24 * 365 * 10)));
                    board.setHasUrl("n");
                    board.setUrlAddress(null);
                    board.setDeleteId(EnumBoardContent.BOARD_CONTENT_4.getId());
                    board.setCreateDate(now);
                    pfbxBoardService.save(board);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public void setPfbxBonusTransDetailService(
            IPfbxBonusTransDetailService pfbxBonusTransDetailService) {
        this.pfbxBonusTransDetailService = pfbxBonusTransDetailService;
    }

    public void setPfbxBonusDayReportService(
            IPfbxBonusDayReportService pfbxBonusDayReportService) {
        this.pfbxBonusDayReportService = pfbxBonusDayReportService;
    }

    public void setPfbxBonusBillService(IPfbxBonusBillService pfbxBonusBillService) {
        this.pfbxBonusBillService = pfbxBonusBillService;
    }

    public void setPfbxBoardService(IPfbxBoardService pfbxBoardService) {
        this.pfbxBoardService = pfbxBoardService;
    }

	public void setPfbxInvalidTrafficService(IPfbxInvalidTrafficService pfbxInvalidTrafficService) {
		this.pfbxInvalidTrafficService = pfbxInvalidTrafficService;
	}
    
}
