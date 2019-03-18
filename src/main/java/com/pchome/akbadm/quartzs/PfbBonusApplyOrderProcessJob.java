package com.pchome.akbadm.quartzs;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.pojo.PfbxBank;
import com.pchome.akbadm.db.pojo.PfbxBonusApply;
import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.pojo.PfbxPersonal;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusApplyService;
import com.pchome.akbadm.factory.pfbx.bonus.ApplyOrderProcess;
import com.pchome.config.TestConfig;
import com.pchome.enumerate.pfbx.account.EnumPfbxAccountCategory;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyInvoiceCheckStatus;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyInvoiceStatus;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyStatus;
import com.pchome.enumerate.pfbx.bonus.EnumPfbBankStatus;
import com.pchome.enumerate.pfbx.bonus.EnumPfbPersonalStatus;


@Transactional
public class PfbBonusApplyOrderProcessJob {

    protected Logger log = LogManager.getRootLogger();

    private ApplyOrderProcess applyOrderProcess;
    private IPfbxBonusApplyService pfbxBonusApplyService;

    /**
     * 每個月的20號執行，將待補件、審核失敗的請款案件，更新為請款失敗
     */
    public void autoMonthDay20ChangeInvoiceFail() {
        log.info("====PfbBonusApplyOrderProcessJob.autoMonthDay20ChangeInvoiceFail() start====");

        log.info("Step 1 Start");
        //Step 1 找出狀態為待補件的的請款案件
        List<PfbxBonusApply> pfbxBonusApplyList = pfbxBonusApplyService.findApplyOrderByInvoiceStatus(EnumPfbApplyInvoiceStatus.VERIFY);

        if (pfbxBonusApplyList == null) {
            log.info("apply order invoice verify not find");
        } else {
            for (PfbxBonusApply pfbxBonusApply : pfbxBonusApplyList) {
                //將發票/收據，狀態更新為[審核失敗]
                applyOrderProcess.doInvoiceStatusProcess(pfbxBonusApply.getPfbxCustomerInfo(), EnumPfbApplyInvoiceStatus.FAIL, pfbxBonusApply.getApplyId(), "請款發票/單據未收到");
                //將請款狀態更新為[請款失敗]
                applyOrderProcess.doApplyStatusProcess(pfbxBonusApply.getPfbxCustomerInfo(), EnumPfbApplyStatus.APPLYFAIL, pfbxBonusApply.getApplyId(), 0f, "單據未收到請款失敗", null,null,null);
            }
        }
        log.info("Step 1 End");
        
        log.info("Step 2 Start");
        //Step 2
        // a.找出請款狀態為[申請中]的請款案件
        // b.銀行帳戶狀態為[待補件]、[審核失敗]，請款狀態不為[請款失敗]、[付款失敗]的的請款案件
        // c.請款人狀態為[待補件]、[審核失敗]，請款狀態不為[請款失敗]、[付款失敗]的的請款案件
        pfbxBonusApplyList = pfbxBonusApplyService.findApplyOrderByFailStatus();
        if (pfbxBonusApplyList == null) {
            log.info("apply order ApplyStatus verify not find");
        } else {
            for (PfbxBonusApply pfbxBonusApply : pfbxBonusApplyList) {
            	
            	/*跑單筆請款狀態用
            	 * log.info(">>>>>>>>>>>>>>>>ApplyId:"+pfbxBonusApply.getApplyId());
            	if(!pfbxBonusApply.getPfbxCustomerInfo().getCustomerInfoId().equals("PFBC20170920001")){
            		continue;
            	}
            	*/
                boolean needChange = false;
                PfbxBank pfbxBank = pfbxBonusApply.getPfbxBank();
                
                PfbxPersonal pfbxPersonal = pfbxBonusApply.getPfbxPersonal();

                //檢查銀行帳戶的部分是否已審核成功
                if(pfbxBank == null){
                    needChange = true;
                }else{
                    if(StringUtils.equals(pfbxBank.getCheckStatus(),EnumPfbBankStatus.FAIL.getStatus()) || StringUtils.equals(pfbxBank.getCheckStatus(),EnumPfbBankStatus.VERIFY.getStatus())){
                        needChange = true;
                    }
                }
                PfbxCustomerInfo pfbxCustomerInfo = pfbxBonusApply.getPfbxCustomerInfo();
                //若身分別為個人戶，需要多檢查收款人資料是否已審核成功
                if(StringUtils.equals(pfbxCustomerInfo.getCategory() , "1")) {
                    //檢查收款人資料的部分
                    if (pfbxPersonal == null) {
                        needChange = true;
                    } else {
                        if (StringUtils.equals(pfbxPersonal.getCheckStatus(), EnumPfbPersonalStatus.FAIL.getStatus()) || StringUtils.equals(pfbxPersonal.getCheckStatus(), EnumPfbPersonalStatus.VERIFY.getStatus())) {
                            needChange = true;
                        }else if(pfbxBonusApply.getInvoiceCheckStatus() == null || !pfbxBonusApply.getInvoiceCheckStatus().equals(EnumPfbApplyInvoiceCheckStatus.SUCCESS.getStatus())){
                        	needChange = true;
                        }
                    }
                }
                if(needChange) {
                    String noteStr ="單據逾期未收到";
                    if(StringUtils.isNotBlank(pfbxBonusApply.getApplyNote())){
                        noteStr = pfbxBonusApply.getApplyNote();
                    }
                    //將請款狀態更新為[請款失敗]
                    applyOrderProcess.doApplyStatusProcess(pfbxBonusApply.getPfbxCustomerInfo(),
                            EnumPfbApplyStatus.APPLYFAIL, pfbxBonusApply.getApplyId(), 0f, noteStr , null,null,null);
                }
            }
        }
        log.info("Step 2 End");

        log.info("====PfbBonusApplyOrderProcessJob.autoMonthDay20ChangeInvoiceFail() end====");
    }

    /**
     * 每個月的16號執行，將狀態為申請中，且文件審核皆成功的申請案，狀態改為付款中
     */
    public void autoMonthDay16ChangeInvoiceWaitPay() {
        log.info("====PfbBonusApplyOrderProcessJob.autoMonthDay16ChangeInvoiceWaitPay() start====");

        log.info("Step 1 Start");
        //Step 1 找出狀態為申請中的的請款案件
        List<PfbxBonusApply> pfbxBonusApplyList = pfbxBonusApplyService.findApplyOrderByApplyStatus(EnumPfbApplyStatus.APPLY);
        if (pfbxBonusApplyList == null) {
            log.info("apply order status Apply not find");
        } else {
            for (PfbxBonusApply pfbxBonusApply : pfbxBonusApplyList) {
                Boolean chkDocStatus = true;
                PfbxCustomerInfo pfbxCustomerInfo = pfbxBonusApply.getPfbxCustomerInfo();

                PfbxBank pfbxBank = pfbxBonusApply.getPfbxBank();
                //檢查帳號資料是否審核成功
				if (pfbxBank == null
						|| !StringUtils.equals(pfbxBank.getCheckStatus(), EnumPfbBankStatus.SUCCESS.getStatus())) {
					log.info(">>>>>>>>>>>>>>>>>pfb帳號:" + pfbxCustomerInfo.getCustomerInfoId() + ",無銀行帳戶資料或審合失敗");
					chkDocStatus = false;
				}

                //檢查單據狀態是否審核成功
				if (chkDocStatus) {
                    if (!StringUtils.equals(pfbxBonusApply.getInvoiceStatus(), EnumPfbApplyInvoiceStatus.SUCCESS.getStatus())) {
                    	log.info(">>>>>>>>>>>>>>>>>pfb帳號:" + pfbxCustomerInfo.getCustomerInfoId() + ",單據審合失敗");
                    	chkDocStatus = false;
                    }
                    
                    //個人戶的部分要多判斷是否確認收據狀態
					if (StringUtils.equals(pfbxCustomerInfo.getCategory(), EnumPfbxAccountCategory.PERSONAL.getCategory())) {
						if (chkDocStatus) {
							List<PfbxBonusApply> pbaList = pfbxBonusApplyService.findApplyByInvoiceCheckStatus(pfbxCustomerInfo.getCustomerInfoId(), EnumPfbApplyInvoiceCheckStatus.SUCCESS.getStatus(), pfbxBonusApply.getApplyId());
							if (pbaList == null || pbaList.size() == 0) {
								log.info(">>>>>>>>>>>>>>>>>pfb帳號:" + pfbxCustomerInfo.getCustomerInfoId() + ",個人戶未確認收據");
								chkDocStatus = false;
							}
						}
					}
                }

                //個人戶的部分，需要多檢查收款人的審核狀況
				if (StringUtils.equals(pfbxCustomerInfo.getCategory(), EnumPfbxAccountCategory.PERSONAL.getCategory())) {
					PfbxPersonal pfbxPersonal = pfbxBonusApply.getPfbxPersonal();
					if (pfbxPersonal == null || 
							!StringUtils.equals(pfbxPersonal.getCheckStatus(), EnumPfbPersonalStatus.SUCCESS.getStatus())) {
						log.info(">>>>>>>>>>>>>>>>>pfb帳號:" + pfbxCustomerInfo.getCustomerInfoId() + ",無個人戶收款人資料或審合失敗");
						chkDocStatus = false;
					}
                }
                
				if (chkDocStatus) {
                    //將請款狀態更新為[付款中]
                    applyOrderProcess.doApplyStatusProcess(pfbxBonusApply.getPfbxCustomerInfo(), EnumPfbApplyStatus.WAIT_PAY, pfbxBonusApply.getApplyId(), 0f, "排程變更為付款中", null, null, null);
                }
            }
        }
        log.info("Step 1 End");

        log.info("====PfbBonusApplyOrderProcessJob.autoMonthDay16ChangeInvoiceWaitPay() end====");
    }

    public void autoMonthDay5ChangeApplyOrderPaySuccess() {
        log.info("====PfbBonusApplyOrderProcessJob.autoMonthDay5ChangeApplyOrderPaySuccess() start====");

        List<PfbxBonusApply> pfbxBonusApplyList = pfbxBonusApplyService.findApplyOrderByApplyStatus(EnumPfbApplyStatus.WAIT_PAY);

        if (pfbxBonusApplyList == null) {
            log.info("apply order apply wait pay  not find");
        } else {
            for (PfbxBonusApply pfbxBonusApply : pfbxBonusApplyList) {
                applyOrderProcess.doApplyStatusProcess(pfbxBonusApply.getPfbxCustomerInfo(), EnumPfbApplyStatus.SUCCESS, pfbxBonusApply.getApplyId(), 0f, null, null,null,null);
            }
        }

        log.info("====PfbBonusApplyOrderProcessJob.autoMonthDay5ChangeApplyOrderPaySuccess() end====");
    }

	public void manual(String value) {
		if (value.equals("invoice")) {
			this.autoMonthDay20ChangeInvoiceFail();
		} else if (value.equals("apply")) {
			this.autoMonthDay5ChangeApplyOrderPaySuccess();
		} else {
			log.info("value not find invoice / apply");
		}
	}

    public static void main(String[] args) throws Exception {

        ApplicationContext context = null;
        String value = null;

        if (args.length < 2) {

            System.out.println("args < 2 [local/stg/prd][invoice/apply] ");
            context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
            PfbBonusApplyOrderProcessJob job = context.getBean(PfbBonusApplyOrderProcessJob.class);
            job.autoMonthDay16ChangeInvoiceWaitPay();
            job.autoMonthDay20ChangeInvoiceFail();
            //String[] test = {"local"};
            //context = new FileSystemXmlApplicationContext(TestConfig.getPath(test));
            //date = "2015-6-09";

        } else {

            context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
            value = args[1].toString();
            PfbBonusApplyOrderProcessJob job = context.getBean(PfbBonusApplyOrderProcessJob.class);
            job.manual(value);

        }

    }

    public void setApplyOrderProcess(ApplyOrderProcess applyOrderProcess) {
        this.applyOrderProcess = applyOrderProcess;
    }

    public void setPfbxBonusApplyService(
            IPfbxBonusApplyService pfbxBonusApplyService) {
        this.pfbxBonusApplyService = pfbxBonusApplyService;
    }
}
