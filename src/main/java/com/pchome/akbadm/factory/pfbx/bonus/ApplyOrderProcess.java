package com.pchome.akbadm.factory.pfbx.bonus;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.pojo.PfbxBank;
import com.pchome.akbadm.db.pojo.PfbxBoard;
import com.pchome.akbadm.db.pojo.PfbxBonusApply;
import com.pchome.akbadm.db.pojo.PfbxBonusBill;
import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.pojo.PfbxPersonal;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.akbadm.db.service.pfbx.account.PfbxCustomerInfoService;
import com.pchome.akbadm.db.service.pfbx.board.IPfbxBoardService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBankService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusApplyService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusBillService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusTransDetailService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxPersonalService;
import com.pchome.akbadm.db.service.sequence.ISequenceService;
import com.pchome.akbadm.utils.DateTimeUtils;
import com.pchome.akbadm.utils.PfbxBoardUtils;
import com.pchome.config.TestConfig;
import com.pchome.enumerate.pfbx.account.EnumPfbxAccountCategory;
import com.pchome.enumerate.pfbx.account.EnumPfbxCheckStatus;
import com.pchome.enumerate.pfbx.board.EnumBoardContent;
import com.pchome.enumerate.pfbx.board.EnumPfbBoardLimit;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyInvoiceCheckStatus;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyInvoiceStatus;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyStatus;
import com.pchome.enumerate.pfbx.bonus.EnumPfbLimit;
import com.pchome.enumerate.pfbx.bonus.EnumPfbxBonus;
import com.pchome.enumerate.pfbx.bonus.EnumPfbxBonusTrans;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;
import com.pchome.rmi.board.EnumPfbBoardType;
import com.pchome.rmi.sequence.EnumSequenceTableName;
import com.pchome.soft.depot.utils.DateValueUtil;

public class ApplyOrderProcess {

    //spring inject
    private IPfbxBonusApplyService pfbxBonusApplyService;
    private IPfbxBonusBillService pfbxBonusBillService;
    private ISequenceService sequenceService;
    private IAdmAccesslogService accesslogService;
    private IPfbxBoardService pfbxBoardService;
    private IPfbxBankService pfbxBankService;
    private IPfbxPersonalService pfbxPersonalService;
    private IPfbxBonusTransDetailService pfbxBonusTransDetailService;

    private BonusTransDetailProcess bonusTransDetailProcess;


    protected Log log = LogFactory.getLog(this.getClass().getName());

    /**
     * doInvoiceStatusProcess 呼叫狀態改變時，上面沒用到的參數傳 null
     * <p/>
     * FAIL: 預期未收到發票 pfb,enumPfbApplyInvoiceStatus,applyId,errMess
     * SUCCESS: 成功收到 pfb,enumPfbApplyInvoiceStatus,applyId
     *
     * @param pfb                       PfbxCustomerInfo
     * @param enumPfbApplyInvoiceStatus 發票收據狀態
     * @param applyId                   請款編號
     * @param errMess                   審核失敗,付款失敗訊息
     */
    public void doInvoiceStatusProcess(PfbxCustomerInfo pfb, EnumPfbApplyInvoiceStatus enumPfbApplyInvoiceStatus, String applyId, String errMess) {

        switch (enumPfbApplyInvoiceStatus) {

            case FAIL: //審核失敗
                log.info("apply order invoice fail");
                invoiveStatusFail(pfb, enumPfbApplyInvoiceStatus, applyId, errMess);

                if (StringUtils.equals(enumPfbApplyInvoiceStatus.getStatus(), "3")) {//審核失敗的時候要發公告
                    String linkUrl = "bonusPaymentView.html?applyID=" + applyId;
                    if (StringUtils.equals(pfb.getCategory(), "2")) { //公司戶
                        PfbxBoardUtils.writePfbxBoard(pfb.getCustomerInfoId(), EnumPfbBoardType.FINANCE, EnumBoardContent.BOARD_CONTENT_12, linkUrl);
                    } else if (StringUtils.equals(pfb.getCategory(), "1")) { //個人戶
                        PfbxBoardUtils.writePfbxBoard(pfb.getCustomerInfoId(), EnumPfbBoardType.FINANCE, EnumBoardContent.BOARD_CONTENT_11, linkUrl);
                    }
                }
                break;

            case SUCCESS: //審核成功
                log.info("apply order invoice success");
                invoiveStatusSuccess(pfb, enumPfbApplyInvoiceStatus, applyId);
                break;

            case VERIFY: //待補件
                log.info("apply order invoice create");
                invoiveStatusSuccess(pfb, enumPfbApplyInvoiceStatus, applyId);
                //這個狀態在申請單建立時就寫入了

                break;
        }
    }

    /**
     * doInvoicCheckStatusProcess 呼叫狀態改變時，上面沒用到的參數傳 null
     * <p/>
     * FAIL: 預期未收到發票 pfb,enumPfbApplyInvoiceStatus,applyId,errMess
     * SUCCESS: 成功收到 pfb,enumPfbApplyInvoiceStatus,applyId
     *
     * @param pfb                            PfbxCustomerInfo
     * @param enumPfbApplyInvoiceCheckStatus 發票收據確認狀態
     * @param applyId                        請款編號
     */
    public void doInvoiceCheckStatusProcess(PfbxCustomerInfo pfb, EnumPfbApplyInvoiceCheckStatus enumPfbApplyInvoiceCheckStatus, String applyId) {

        switch (enumPfbApplyInvoiceCheckStatus) {
            case WAIT: //待確認
                log.info("apply order invoice fail");
                invoiveCheckStatusUpdate(pfb, enumPfbApplyInvoiceCheckStatus, applyId);
                break;

            case SUCCESS: //審核成功
                log.info("apply order invoice success");
                invoiveCheckStatusUpdate(pfb, enumPfbApplyInvoiceCheckStatus, applyId);
                break;
        }

    }

    /**
     * 發票/收據確認狀態更新作業
     *
     * @param pfb
     * @param enumPfbApplyInvoiceCheckStatus
     * @param applyId
     */
    private void invoiveCheckStatusUpdate(PfbxCustomerInfo pfb, EnumPfbApplyInvoiceCheckStatus enumPfbApplyInvoiceCheckStatus, String applyId) {

        Date today = new Date();

        PfbxBonusApply pfbxBonusApply = pfbxBonusApplyService.findByApplyId(pfb.getCustomerInfoId(), applyId);

        if (pfbxBonusApply != null) {

            pfbxBonusApply.setInvoiceCheckStatus(enumPfbApplyInvoiceCheckStatus.getStatus());
            pfbxBonusApply.setUpdateDate(today);
            pfbxBonusApplyService.saveOrUpdate(pfbxBonusApply);

        } else {
            log.info("apply id not find -->" + applyId);
        }

    }

    /**
     * 發票/收據審核失敗作業
     *
     * @param pfb
     * @param enumPfbApplyInvoiceStatus
     * @param applyId
     * @param errMess
     */
    private void invoiveStatusFail(PfbxCustomerInfo pfb, EnumPfbApplyInvoiceStatus enumPfbApplyInvoiceStatus, String applyId, String errMess) {

        Date today = new Date();

        PfbxBonusApply pfbxBonusApply = pfbxBonusApplyService.findByApplyId(pfb.getCustomerInfoId(), applyId);

        if (pfbxBonusApply != null) {

            pfbxBonusApply.setInvoiceStatus(enumPfbApplyInvoiceStatus.getStatus());
            pfbxBonusApply.setInvoiceNote(errMess);
            pfbxBonusApply.setUpdateDate(today);
            pfbxBonusApplyService.saveOrUpdate(pfbxBonusApply);

        } else {
            log.info("apply id not find -->" + applyId);
        }

    }

    /**
     * 發票/收據審核成功作業
     *
     * @param pfb
     * @param enumPfbApplyInvoiceStatus
     * @param applyId
     */
    private void invoiveStatusSuccess(PfbxCustomerInfo pfb, EnumPfbApplyInvoiceStatus enumPfbApplyInvoiceStatus, String applyId) {

        Date today = new Date();

        PfbxBonusApply pfbxBonusApply = pfbxBonusApplyService.findByApplyId(pfb.getCustomerInfoId(), applyId);

        if (pfbxBonusApply != null) {

            pfbxBonusApply.setInvoiceStatus(enumPfbApplyInvoiceStatus.getStatus());
            //若審核狀態為審核成功，將確認狀態更新為待確認(第一筆)/確認成功，以配合前台作業‧
            if (StringUtils.equals(enumPfbApplyInvoiceStatus.getStatus(), EnumPfbApplyInvoiceStatus.SUCCESS.getStatus())) {
                //檢查是否為第一筆審核成功的請款單
                List<PfbxBonusApply> pbaList = pfbxBonusApplyService.findApplyByInvoiceCheckStatus(pfb.getCustomerInfoId(), EnumPfbApplyInvoiceCheckStatus.SUCCESS.getStatus(),applyId);
                //若為第一筆，將確認狀態更新為待確認
                //若為第二筆以上，將確認狀態更新為確認成功
                if (pbaList.size() > 0) {
                    pfbxBonusApply.setInvoiceCheckStatus(EnumPfbApplyInvoiceCheckStatus.SUCCESS.getStatus());
                } else {
                    pfbxBonusApply.setInvoiceCheckStatus(EnumPfbApplyInvoiceCheckStatus.WAIT.getStatus());
                }
            } else {
                pfbxBonusApply.setInvoiceCheckStatus(null);
            }
            pfbxBonusApply.setInvoiceNote("");
            pfbxBonusApply.setUpdateDate(today);

            pfbxBonusApplyService.saveOrUpdate(pfbxBonusApply);

        } else {
            log.info("apply id not find -->" + applyId);
        }

    }

    /**
     * doApplyStatusProcess 呼叫狀態改變時，沒用到的參數傳 null
     *
     * @param pfb                PfbxCustomerInfo
     * @param enumPfbApplyStatus 請款單狀態
     * @param applyId            請款編號
     * @param applyMoney         請款金額
     * @param errMess            審核失敗,付款失敗訊息
     * @param payDate            財務付款日
     * @return APPLY: 申請中建立申請單 pfb,enumPfbApplyStatus,applyMoney
     * WAIT_PAY: 等待付款 pfb,enumPfbApplyStatus,applyId,payDate
     * SUCCESS: 成功 pfb,enumPfbApplyStatus,applyId
     * PAYFAIL: 財務付款失敗 pfb,enumPfbApplyStatus,applyId,errMess
     * APPLYFAIL: 申請失敗 pfb,enumPfbApplyStatus,applyId,errMess
     */
    public String doApplyStatusProcess(PfbxCustomerInfo pfb, EnumPfbApplyStatus enumPfbApplyStatus, String applyId,
                                       float applyMoney, String errMess, Date payDate, String memberId, String address) {

        PfbxBonusApply pfbxBonusApply = null;

        String returnVlaue = "";

        switch (enumPfbApplyStatus) {

            case APPLY: //申請中建立申請單

                log.info("apply order status APPLY");

                pfbxBonusApply = createApplyOrder(pfb, enumPfbApplyStatus, applyMoney);

                if (pfbxBonusApply != null) {

                    //新增 bonusTransDetailProcess
                    writeBonusTransDetail(pfb, EnumPfbxBonusTrans.APPLY_PROCESS, pfbxBonusApply);

                    //更新 pfbx_bonus_bill --> apply_money 請款中金額(累加)
                    updatePfbxBounsBillyMoney(pfb);

                    //寫 accesslog 紀錄狀態改變
                    writeAccessLog("請款單開立", enumPfbApplyStatus, pfbxBonusApply.getApplyId(), pfb, memberId, address);

                    //刪除提醒請款的公告
                    deleteBoard(pfb, EnumBoardContent.BOARD_CONTENT_4);

                    //寫申請單的公告
                    writeBoard(pfb, EnumBoardContent.BOARD_CONTENT_5, pfbxBonusApply);

                    returnVlaue = pfbxBonusApply.getApplyId();

                } else {
                    log.info("pfbxBonusApply is null");
                    returnVlaue = "ERROR";
                }
                break;

            case WAIT_PAY: //等待付款

                log.info("apply order status WAIT_PAY");
                pfbxBonusApply = applyOrderWaitPay(pfb, enumPfbApplyStatus, applyId, payDate);

                if (pfbxBonusApply == null) {

                    log.info("pfbxBonusApply is null");

                    returnVlaue = "ERROR";

                } else {

                    //寫 accesslog 紀錄狀態改變
                    writeAccessLog("請款狀態", enumPfbApplyStatus, applyId, pfb, memberId, address);

                    returnVlaue = pfbxBonusApply.getApplyId();

                }

                break;

            case SUCCESS: //成功
                //更新 pfbx_bonus_bill --> last_apply_money 上次請款金額   -->total_apply_money 總請款金額
                log.info("apply order status DONE");

                pfbxBonusApply = applyOrderDone(pfb, enumPfbApplyStatus, applyId);

                if (pfbxBonusApply == null) {

                    log.info("pfbxBonusApply is null");

                    returnVlaue = "ERROR";
                } else {

                    //更新 pfbx_bonus_bill
                    updatePfbxBounsBillyMoney(pfb);


                    //新增 bonusTransDetailProcess
                    writeBonusTransDetail(pfb, EnumPfbxBonusTrans.APPLY_SUCCESS, null);

                    //寫 accesslog 紀錄狀態改變
                    writeAccessLog("請款狀態", enumPfbApplyStatus, applyId, pfb, memberId, address);

                    //寫公告
                    writeBoard(pfb, EnumBoardContent.BOARD_CONTENT_6, pfbxBonusApply);

                    returnVlaue = pfbxBonusApply.getApplyId();

                }
                break;

            case PAYFAIL: //財務付款失敗
                log.info("apply order status PAYFAIL");
                pfbxBonusApply = applyOrderPayFail(pfb, enumPfbApplyStatus, applyId, errMess);

                if (pfbxBonusApply == null) {

                    log.info("pfbxBonusApply is null");

                    returnVlaue = "ERROR";

                } else {

                    //新增 bonusTransDetailProcess
                    writeBonusTransDetail(pfb, EnumPfbxBonusTrans.APPLY_FALE, pfbxBonusApply);

                    //更新 pfbx_bonus_bill
                    updatePfbxBounsBillyMoney(pfb);

                    //寫 accesslog 紀錄狀態改變
                    writeAccessLog("請款狀態", enumPfbApplyStatus, applyId, pfb, memberId, address);

                    //寫公告
                    writeBoard(pfb, EnumBoardContent.BOARD_CONTENT_7, pfbxBonusApply);

                    returnVlaue = pfbxBonusApply.getApplyId();
                }
                break;

            case APPLYFAIL: //申請失敗
                log.info("apply order status APPLYFAIL");

                pfbxBonusApply = applyOrderApplyFail(pfb, enumPfbApplyStatus, applyId, errMess);

                if (pfbxBonusApply == null) {

                    log.info("pfbxBonusApply is null");

                    returnVlaue = "ERROR";

                } else {

                    //更新 pfbx_bonus_bill
                    updatePfbxBounsBillyMoney(pfb);

                    //新增 bonusTransDetailProcess
                    writeBonusTransDetail(pfb, EnumPfbxBonusTrans.APPLY_FALE, pfbxBonusApply);

                    //寫 accesslog 紀錄狀態改變
                    writeAccessLog("請款狀態", enumPfbApplyStatus, applyId, pfb, memberId, address);

                    //寫公告
                    writeBoard(pfb, EnumBoardContent.BOARD_CONTENT_7, pfbxBonusApply);

                    returnVlaue = pfbxBonusApply.getApplyId();

                }

                break;
        }
        return returnVlaue;
    }

    private void deleteBoard(PfbxCustomerInfo pfb, EnumBoardContent boardContent4) {
        String customerInfoId = pfb.getCustomerInfoId();
        //查詢是否寫過公告了，寫過就刪掉
        Map<String, String> conditionMap2 = new HashMap<String, String>();
        conditionMap2.put("pfbxCustomerInfoId", customerInfoId);
        conditionMap2.put("deleteId", boardContent4.getId());

        try {
            List<PfbxBoard> pfbxBoards = pfbxBoardService.findPfbxBoard(conditionMap2);
            if (pfbxBoards.size() > 0) {
                for (PfbxBoard pfbxBoard : pfbxBoards) {
                    pfbxBoardService.delete(pfbxBoard);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private PfbxBonusApply createApplyOrder(PfbxCustomerInfo pfb, EnumPfbApplyStatus enumPfbApplyStatus, float applyMoney) {

        PfbxBonusBill pfbxBonusBill = pfbxBonusBillService.findPfbxBonusBill(pfb.getCustomerInfoId());

        float billMoney = pfbxBonusBill.getBillRemain();

        //請款金額
        float transRemainMoney = applyMoney;

        PfbxBonusApply pfbxBonusApply = null;

        if (billMoney == transRemainMoney) {

            //可請款餘額大於請款門檻,這裡先用大家統一的最低請款門檻，還沒開放自行指定
            if (transRemainMoney >= EnumPfbLimit.MIN.getMoney()) {

                Date today = new Date();

                String applyId = sequenceService.getId(EnumSequenceTableName.PFBX_APPLY_ORDER);

                //計算稅率公司戶個人戶不一樣
                //公司戶 每次請款金額 外加5% =實際付款金額=發票開立金額
                //公司戶 實際支付金額=請款金額+(請款金額*5%)

                //個人戶 每次請款金額達
                //NT$5,000(含)以上 扣2%(二代健保)、NT$20,000(含)以上 扣10%(代扣所得稅)
                //105/1/1更改 給付額在$20,000(含)以上，代扣1.91%二代健保補充保費、給付額在NT$20,000(含)以上，代扣10%稅金

                //個人戶 實際支付金額=請款金額-二代健保-代扣所得稅

                PfbxBank pfbxBank = null;
                PfbxPersonal PfbxPersonal = null;

                float tax = 0.0f;
                //個人
                if (pfb.getCategory().equals(EnumPfbxAccountCategory.PERSONAL.getCategory())) {
                    tax = 0.0f;

                    if (transRemainMoney >= 20000) {
                        tax = (transRemainMoney * 0.0191f) + (transRemainMoney * 0.1f);
                    } //else {
                        //if (transRemainMoney >= 5000) {
                           // tax = (transRemainMoney * 0.02f);
                       // }
                    //}

                    pfbxBank = pfbxBankService.getPfbxMainUseBank(pfb.getCustomerInfoId());
                    PfbxPersonal = pfbxPersonalService.getPfbxMainUsePersonal(pfb.getCustomerInfoId());

                } else {
                    //float tm = 0.0f;
                	float noTaxMoney = new BigDecimal(String.valueOf((transRemainMoney / 1.05f))).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
                	tax = transRemainMoney - noTaxMoney;
                	
                    //tax = Math.round((transRemainMoney / 1.05f) * 0.05f);

                    //tm=transRemainMoney - tax;

                    //transRemainMoney=tm;
                    //tax=transRemainMoney*0.05f;

                    pfbxBank = pfbxBankService.getPfbxMainUseBank(pfb.getCustomerInfoId());
                    PfbxPersonal = null;
                }

                pfbxBonusApply = new PfbxBonusApply();
                //開出請款單
                pfbxBonusApply.setApplyId(applyId);
                pfbxBonusApply.setPfbxCustomerInfo(pfb);
                pfbxBonusApply.setPfbxBank(pfbxBank);
                pfbxBonusApply.setPfbxPersonal(PfbxPersonal);
                pfbxBonusApply.setApplyDate(today);
                pfbxBonusApply.setApplyMoney(transRemainMoney);
                pfbxBonusApply.setTax(tax);

                //若是個人戶的話，需檢查是否為第一筆的請款單
                if (pfb.getCategory().equals(EnumPfbxAccountCategory.PERSONAL.getCategory())) {
                    //檢查是否為第一筆審核成功的請款單
                	Integer bankId = null;
                	String bankCheckStatus = EnumPfbxCheckStatus.ING.getStatus();
                	if(pfbxBank != null){
                    	bankId = pfbxBank.getId();
                    	bankCheckStatus = pfbxBank.getCheckStatus();
                    }
                    
                    Integer personalId = null;
                    String personalCheckStatus = EnumPfbxCheckStatus.ING.getStatus();
                    if(PfbxPersonal != null){
                    	personalId = PfbxPersonal.getId();
                    	personalCheckStatus = PfbxPersonal.getCheckStatus();
                    }
                    
                    List<PfbxBonusApply> pbaList = pfbxBonusApplyService.findOldDetalByInvoiceCheckStatus(pfb.getCustomerInfoId(), EnumPfbApplyInvoiceCheckStatus.SUCCESS.getStatus(),bankId,personalId);
                    //若為第一筆，將確認狀態更新為待確認
                    //若為第二筆以上，將確認狀態更新為確認成功
                    if (pbaList.size() > 0 && StringUtils.equals(EnumPfbxCheckStatus.OK.getStatus(), bankCheckStatus) && StringUtils.equals(EnumPfbxCheckStatus.OK.getStatus(), personalCheckStatus)) {
                        pfbxBonusApply.setInvoiceStatus(EnumPfbApplyInvoiceStatus.SUCCESS.getStatus());//審核成功
                    } else {
                        pfbxBonusApply.setInvoiceCheckStatus(EnumPfbApplyInvoiceCheckStatus.WAIT.getStatus());
                        pfbxBonusApply.setInvoiceStatus(EnumPfbApplyInvoiceStatus.VERIFY.getStatus());//待補件
                    }
                } else {
                    pfbxBonusApply.setInvoiceStatus(EnumPfbApplyInvoiceStatus.VERIFY.getStatus());//資料審核
                }
                pfbxBonusApply.setApplyStatus(enumPfbApplyStatus.getStatus());
                pfbxBonusApply.setUpdateDate(today);
                pfbxBonusApply.setCreateDate(today);

                pfbxBonusApplyService.saveOrUpdate(pfbxBonusApply);

            } else {
                log.info("transRemainMoney not enough-->" + transRemainMoney);
            }

        }
        return pfbxBonusApply;
    }

    private PfbxBonusApply applyOrderDone(PfbxCustomerInfo pfb, EnumPfbApplyStatus enumPfbApplyStatus, String applyId) {

        Date today = new Date();

        PfbxBonusApply pfbxBonusApply = pfbxBonusApplyService.findByApplyId(pfb.getCustomerInfoId(), applyId);

        if (pfbxBonusApply != null) {

            pfbxBonusApply.setApplyStatus(enumPfbApplyStatus.getStatus());
            pfbxBonusApply.setApplyNote("");
            pfbxBonusApply.setUpdateDate(today);
            pfbxBonusApplyService.saveOrUpdate(pfbxBonusApply);

        } else {
            log.info("apply id not find -->" + applyId);
        }

        return pfbxBonusApply;
    }

    private PfbxBonusApply applyOrderWaitPay(PfbxCustomerInfo pfb, EnumPfbApplyStatus enumPfbApplyStatus, String applyId, Date payDate) {
        Date today = new Date();

        PfbxBonusApply pfbxBonusApply = pfbxBonusApplyService.findByApplyId(pfb.getCustomerInfoId(), applyId);

        if (pfbxBonusApply != null) {

            pfbxBonusApply.setApplyStatus(enumPfbApplyStatus.getStatus());
            pfbxBonusApply.setPayDate(payDate);
            pfbxBonusApply.setUpdateDate(today);
            pfbxBonusApplyService.saveOrUpdate(pfbxBonusApply);

        } else {
            log.info("apply id not find -->" + applyId);
        }

        return pfbxBonusApply;
    }

    public PfbxBonusApply applyOrderPayFail(PfbxCustomerInfo pfb, EnumPfbApplyStatus enumPfbApplyStatus, String applyId, String errMess) {
        Date today = new Date();

        PfbxBonusApply pfbxBonusApply = pfbxBonusApplyService.findByApplyId(pfb.getCustomerInfoId(), applyId);

        if (pfbxBonusApply != null) {

            pfbxBonusApply.setApplyStatus(enumPfbApplyStatus.getStatus());
            pfbxBonusApply.setApplyNote(errMess);
            pfbxBonusApply.setUpdateDate(today);
            pfbxBonusApplyService.saveOrUpdate(pfbxBonusApply);

        } else {
            log.info("apply id not find -->" + applyId);
        }

        return pfbxBonusApply;
    }

	/**
	 * @param pfb
	 * @param enumPfbApplyStatus 申請單狀態
	 * @param applyId 請款編號
	 * @param errMess 申請單退回原因
	 * @return
	 */
    public PfbxBonusApply applyOrderApplyFail(PfbxCustomerInfo pfb, EnumPfbApplyStatus enumPfbApplyStatus, String applyId, String errMess) {
        Date today = new Date();

        PfbxBonusApply pfbxBonusApply = pfbxBonusApplyService.findByApplyId(pfb.getCustomerInfoId(), applyId);

        if (pfbxBonusApply != null) {

            pfbxBonusApply.setApplyStatus(enumPfbApplyStatus.getStatus());
            pfbxBonusApply.setApplyNote(errMess);
            pfbxBonusApply.setUpdateDate(today);
            pfbxBonusApplyService.saveOrUpdate(pfbxBonusApply);

        } else {
            log.info("apply id not find -->" + applyId);
        }
        return pfbxBonusApply;
    }

    /**
     * 更新 pfbx_bonus_apply pfb請款單資料
     * @param pfb
     * @param applyId 請款編號
     * @param invoiceNote 發票收據退回原因
     * @param enumPfbApplyStatus 申請單狀態
     * @param applyNote 申請單退回原因
     * @return
     */
    private PfbxBonusApply applyOrderApplyFailUpdate(PfbxCustomerInfo pfb, String applyId, String invoiceNote, EnumPfbApplyStatus enumPfbApplyStatus, String applyNote) {
        PfbxBonusApply pfbxBonusApply = pfbxBonusApplyService.findByApplyId(pfb.getCustomerInfoId(), applyId);

        if (pfbxBonusApply != null) {
        	Date today = new Date();
			log.info("Update applyId=" + applyId + ", InvoiceNote=" + invoiceNote + ", ApplyStatus="
					+ enumPfbApplyStatus.getStatus() + ", ApplyNote=" + applyNote + ", UpdateDate=" + today);
			
        	pfbxBonusApply.setInvoiceNote(invoiceNote);
            pfbxBonusApply.setApplyStatus(enumPfbApplyStatus.getStatus());
            pfbxBonusApply.setApplyNote(applyNote);
            pfbxBonusApply.setUpdateDate(today);
            pfbxBonusApplyService.saveOrUpdate(pfbxBonusApply);
        } else {
            log.info("apply id not find -->" + applyId);
        }
        return pfbxBonusApply;
	}
    
    /**
     * 將查詢到的申請失敗資料還原
     * @param pfbxCustomerInfo
     * @param applyId 請款編號
     * @param memberId 會員帳號
     * @param address IP位置
     */
    public void doApplyFailResetProcess(PfbxCustomerInfo pfb, String applyId, String memberId, String address) {

    	// 還原 pfbx_bonus_apply資料
    	log.info("1.reset applyOrderApplyFail");
    	PfbxBonusApply pfbxBonusApply = applyOrderApplyFailUpdate(pfb, applyId, "", EnumPfbApplyStatus.APPLY, "");

		// 還原 pfbx_bonus_bill資料
    	log.info("2.reset PfbxBounsBillyMoney");
    	resetUpdatePfbxBounsBillyMoney(pfb, pfbxBonusApply.getApplyMoney());
    	
		// 寫 accesslog 紀錄狀態改變
    	log.info("3.add AccessLog");
		writeAccessLog("還原請款狀態", EnumPfbApplyStatus.APPLY, applyId, pfb, memberId, address);

		// 刪除當月16號排程所產生的請款失敗相關訊息
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String startDate = sdf.format(d) + "-01";
		String endDate = startDate;
		
		// 刪除 pfbx_bonus_trans_detail 獎金交易明細 新增的一筆請款失敗紀錄
		log.info("4.delete pfbxBonusTransDetail CustomerInfoId=" + pfb.getCustomerInfoId() + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", transItem=5" + ", applyId=" + applyId);
		pfbxBonusTransDetailService.deleteItemTransDetail(pfb.getCustomerInfoId(), startDate, endDate, "5", applyId);
		
		String createDate = sdf.format(d) + "-16 05:00:00"; // 刪除16號之後產生的delete_id為7、11、12的失敗公告
		
		// 刪除申請失敗公告
		log.info("5.delete pfbxBoard CustomerInfoId=" + pfb.getCustomerInfoId() + ", deleteId=" + EnumBoardContent.BOARD_CONTENT_7.getId() + ", createDate=" + createDate);
		pfbxBoardService.delectBoardFindLatestContent(pfb.getCustomerInfoId(), EnumBoardContent.BOARD_CONTENT_7.getId(), createDate);
		log.info("6.delete pfbxBoard CustomerInfoId=" + pfb.getCustomerInfoId() + ", deleteId=" + EnumBoardContent.BOARD_CONTENT_11.getId() + ", createDate=" + createDate);
		pfbxBoardService.delectBoardFindLatestContent(pfb.getCustomerInfoId(), EnumBoardContent.BOARD_CONTENT_11.getId(), createDate);
		log.info("7.delete pfbxBoard CustomerInfoId=" + pfb.getCustomerInfoId() + ", deleteId=" + EnumBoardContent.BOARD_CONTENT_12.getId() + ", createDate=" + createDate);
		pfbxBoardService.delectBoardFindLatestContent(pfb.getCustomerInfoId(), EnumBoardContent.BOARD_CONTENT_12.getId(), createDate);
	}

	/**
	 * 還原 pfbx_bonus_bill pfb獎金帳單資料
	 * @param pfb
	 * @param applyMoney 請款金額
	 */
	private void resetUpdatePfbxBounsBillyMoney(PfbxCustomerInfo pfb, float applyMoney) {
		PfbxBonusBill pfbxBonusBill = pfbxBonusBillService.findPfbxBonusBill(pfb.getCustomerInfoId());
		
		// 請款金額
		BigDecimal BigDecimalApplyMoney = new BigDecimal(String.valueOf(applyMoney));
		// 尚未請款金額
		BigDecimal BigDecimalBillRemain = new BigDecimal(String.valueOf(pfbxBonusBill.getBillRemain()));
		
		// 更新 pfbx_bonus_bill
		Date today = new Date();
		pfbxBonusBill.setApplyMoney(applyMoney);
		// 尚未請款金額 - 請款金額
		pfbxBonusBill.setBillRemain(BigDecimalBillRemain.subtract(BigDecimalApplyMoney).floatValue());
		pfbxBonusBill.setUpdateDate(today);
		log.info("Update PfbId=" + pfbxBonusBill.getPfbId() + ", ApplyMoney=" + applyMoney + ", BillRemain=" + pfbxBonusBill.getBillRemain() + ", UpdateDate=" + today);
		pfbxBonusBillService.saveOrUpdate(pfbxBonusBill);
	}

	private void updatePfbxBounsBillyMoney(PfbxCustomerInfo pfb) {

        float apply_money = 0.0f;//正在請款的金額
        float total_apply_money = 0.0f;//全部請過款的金額
        float last_apply_money = 0.0f;//上一次請款的金額

        //尚未請款的金額在 BonusTransDetailProcess 裡面處理

        //撈請款單狀態  請款中的訂單金額,付款中的金額
        //正在請款的金額
        List<PfbxBonusApply> pfbxBonusApplyList = pfbxBonusApplyService.findNotDoneFailOrder(pfb.getCustomerInfoId());
        if (pfbxBonusApplyList != null) {
            for (PfbxBonusApply pba : pfbxBonusApplyList) {
                apply_money += pba.getApplyMoney();
            }
        }

        //撈請款單狀態  請款中的訂單金額 等於 DONE:
        //全部請過款的金額
        List<PfbxBonusApply> totalpfbxBonusApplyList = pfbxBonusApplyService.findAllDoneOrder(pfb.getCustomerInfoId());
        if (totalpfbxBonusApplyList != null) {
            for (PfbxBonusApply pba : totalpfbxBonusApplyList) {
                total_apply_money += pba.getApplyMoney();
            }
        }

        //撈請款單狀態  請款中的訂單金額 等於 DONE: 日期最後一筆
        //上一次請款的金額
        PfbxBonusApply pfbxBonusApply = pfbxBonusApplyService.findLastDoneOrder(pfb.getCustomerInfoId());
        if (pfbxBonusApply != null) {
            last_apply_money += pfbxBonusApply.getApplyMoney();
        }

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

        pfbxBonusBill.setApplyMoney(apply_money);
        pfbxBonusBill.setTotalApplyMoney(total_apply_money);
        pfbxBonusBill.setLastApplyMoney(last_apply_money);
        pfbxBonusBill.setUpdateDate(today);

        pfbxBonusBillService.saveOrUpdate(pfbxBonusBill);
    }

    private void writeBoard(PfbxCustomerInfo pfb, EnumBoardContent enumBoardContent, PfbxBonusApply pfbxBonusApply) {
        //寫公告
        Date today = new Date();
        StringBuffer str = new StringBuffer();
        str.append("請款單-<a href='pfb/bonusPaymentView.html?applyID=" + pfbxBonusApply.getApplyId() + "'>" + pfbxBonusApply.getApplyId() + "</a> ");
        str.append(enumBoardContent.getContent());

        PfbxBoard board = new PfbxBoard();
        board.setBoardType(EnumPfbBoardType.FINANCE.getType());
        board.setBoardContent(str.toString());
        board.setPfbxCustomerInfoId(pfb.getCustomerInfoId());
        board.setStartDate(today);
//		board.setEndDate(new Date(today.getTime() + (1000 * 60 * 60 * 24 * 7)));//7 天
        board.setEndDate(DateTimeUtils.shiftMontth(today, EnumPfbBoardLimit.Limit.getValue()));
        board.setHasUrl("n");
        board.setUrlAddress(null);
        board.setDeleteId(enumBoardContent.getId());
        board.setUpdateDate(today);
        board.setCreateDate(today);

        pfbxBoardService.save(board);
    }

    private void writeBonusTransDetail(PfbxCustomerInfo pfb, EnumPfbxBonusTrans enumPfbxBonusTrans, PfbxBonusApply pfbxBonusApply) {
        //新增 bonusTransDetailProcess
        String monthValue = DateValueUtil.getInstance().getDateMonth();
        bonusTransDetailProcess.insertItemTransDetail(pfb, enumPfbxBonusTrans, monthValue, pfbxBonusApply);
    }

    /**
     * 寫log
     * @param mesage 訊息
     * @param enumPfbApplyStatus
     * @param applyId
     * @param pfb
     * @param memberId
     * @param address
     */
    private void writeAccessLog(String mesage, EnumPfbApplyStatus enumPfbApplyStatus, String applyId, PfbxCustomerInfo pfb, String memberId, String address) {
        if (StringUtils.isBlank(memberId)) {
            memberId = "System";
        }

        if (StringUtils.isBlank(address)) {
            address = "127.0.0.1";
        }

        String customerId = "";
        if (pfb != null) {
            customerId = pfb.getCustomerInfoId();
        }

        accesslogService.addAdmAccesslog(EnumAccesslogChannel.PFB,
                EnumAccesslogAction.STATUS_NOTIFY,
                mesage + "-->" + enumPfbApplyStatus.getChName(),
                memberId,
                applyId,
                customerId,
                null,
                address,
                EnumAccesslogEmailStatus.NO);
    }

    public void setSequenceService(ISequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    public void setPfbxBonusBillService(IPfbxBonusBillService pfbxBonusBillService) {
        this.pfbxBonusBillService = pfbxBonusBillService;
    }

	public void setPfbxBonusApplyService(IPfbxBonusApplyService pfbxBonusApplyService) {
		this.pfbxBonusApplyService = pfbxBonusApplyService;
	}

    public void setAccesslogService(IAdmAccesslogService accesslogService) {
        this.accesslogService = accesslogService;
    }

    public void setPfbxBoardService(IPfbxBoardService pfbxBoardService) {
        this.pfbxBoardService = pfbxBoardService;
    }

	public void setBonusTransDetailProcess(BonusTransDetailProcess bonusTransDetailProcess) {
		this.bonusTransDetailProcess = bonusTransDetailProcess;
	}

    public void setPfbxBankService(IPfbxBankService pfbxBankService) {
        this.pfbxBankService = pfbxBankService;
    }

    public void setPfbxPersonalService(IPfbxPersonalService pfbxPersonalService) {
        this.pfbxPersonalService = pfbxPersonalService;
    }

    public void setPfbxBonusTransDetailService(IPfbxBonusTransDetailService pfbxBonusTransDetailService) {
		this.pfbxBonusTransDetailService = pfbxBonusTransDetailService;
	}

	public static void main(String[] args) throws Exception {

        ApplicationContext context = null;

        Date today = new Date();
        if (args.length < 1) {

            System.out.println("args < 2 [local/stg/prd][start process month ex 6] ");

        } else {
            context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));

            PfbxCustomerInfoService pfbxCustomerInfoService = context.getBean(PfbxCustomerInfoService.class);
            ApplyOrderProcess applyOrderProcess = context.getBean(ApplyOrderProcess.class);

            List<PfbxCustomerInfo> pfbxs = pfbxCustomerInfoService.findValidPfbxCustomerInfo();

            //
            for (PfbxCustomerInfo pfb : pfbxs) {

                if (pfb.getCustomerInfoId().equals("PFBC20150519001")) {
                    applyOrderProcess.doApplyStatusProcess(pfb, EnumPfbApplyStatus.APPLY, null, 600f, null, today, null, null);
                }
                //applyOrderProcess.doApplyStatusProcess(pfb, EnumPfbApplyStatus.APPLY, "AO201507150001", 5000f ,null,today);

                //applyOrderProcess.doInvoiceStatusProcess(pfb, EnumPfbApplyInvoiceStatus.SUCCESS, "AO201507150001", "收據未收到");
            }
        }

        //pfb PfbxCustomerInfo
        //enumPfbApplyStatus 請款單狀態
        //applyId 請款編號
        //applyMoney 請款金額
        //errMess 審核失敗,付款失敗訊息
        //payDate 財務付款日

        // APPLY: 申請中建立申請單 pfb,enumPfbApplyStatus,applyMoney
        // WAIT_PAY: 等待付款 pfb,enumPfbApplyStatus,applyId,payDate
        // SUCCESS: 成功 pfb,enumPfbApplyStatus,applyId
        // PAYFAIL: 財務付款失敗 pfb,enumPfbApplyStatus,applyId,errMess
        // APPLYFAIL: 申請失敗 pfb,enumPfbApplyStatus,applyId,errMess

    }

}
