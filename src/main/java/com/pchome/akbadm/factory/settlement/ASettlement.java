package com.pchome.akbadm.factory.settlement;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.pojo.PfpTransDetail;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.trans.IPfpTransDetailService;
import com.pchome.akbadm.db.vo.SettlementVO;

public abstract class ASettlement {

	protected Log log = LogFactory.getLog(this.getClass());

	protected IPfpTransDetailService transDetailService;
	protected IPfpCustomerInfoService customerInfoService;

	public abstract void updateSettlementVO(SettlementVO vo, Date transDate);

	public abstract void updateTransDetail(SettlementVO vo, PfpTransDetail transDetail);

    @Deprecated
	public abstract void updateCustomerInfo(PfpCustomerInfo customerInfo, PfpTransDetail transDetail);

    public abstract PfpCustomerInfo updateCustomerInfo2(PfpCustomerInfo customerInfo, PfpTransDetail transDetail);

	/**
	 * 結算項目
	 * 1. 建立交易明細
	 * 2. 建立 VO
	 * 3. 更新 VO
	 * 4. 更新交易明細資料
	 * 5. 更新帳戶資料
	 */
    @Deprecated
	public SettlementVO startProcess(PfpCustomerInfo customerInfo, SettlementVO vo, Date transDate) {

//		log.info(">>> customerInfoId: "+customerInfo.getCustomerInfoId());
//		log.info(">>> transDate: "+DateValueUtil.getInstance().dateToString(transDate));
//		log.info(">>> vo: "+vo);

		PfpTransDetail transDetail = this.createTransDetail(customerInfo, vo, transDate);

		vo = this.createSettlementVO(vo, customerInfo);

		this.updateSettlementVO(vo, transDate);

		this.updateTransDetail(vo, transDetail);

//		log.info(">>> transPrice: "+vo.getTransPrice());
//		log.info(">>> Retrieve1: "+customerInfo.getTotalLaterRetrieve());

		this.updateCustomerInfo(customerInfo, transDetail);

//		log.info(">>> remain: "+customerInfo.getRemain());
//		log.info(">>> Retrieve2: "+customerInfo.getTotalLaterRetrieve());

		return vo;
	}

    /**
     * 結算項目
     * 1. 建立交易明細
     * 2. 建立 VO
     * 3. 更新 VO
     * 4. 更新交易明細資料
     * 5. 更新帳戶資料
     */
    public SettlementVO startProcess2(PfpCustomerInfo customerInfo, SettlementVO vo, Date transDate) {

//      log.info(">>> customerInfoId: "+customerInfo.getCustomerInfoId());
//      log.info(">>> transDate: "+DateValueUtil.getInstance().dateToString(transDate));
//      log.info(">>> vo: "+vo);

        PfpTransDetail transDetail = this.createTransDetail(customerInfo, vo, transDate);

        vo = this.createSettlementVO(vo, customerInfo);

        this.updateSettlementVO(vo, transDate);

        this.updateTransDetail(vo, transDetail);

//      log.info(">>> transPrice: "+vo.getTransPrice());
//      log.info(">>> Retrieve1: "+customerInfo.getTotalLaterRetrieve());

        customerInfo = this.updateCustomerInfo2(customerInfo, transDetail);

//      log.info(">>> remain: "+customerInfo.getRemain());
//      log.info(">>> Retrieve2: "+customerInfo.getTotalLaterRetrieve());

        return vo;
    }

	private PfpTransDetail createTransDetail(PfpCustomerInfo customerInfo, SettlementVO vo, Date transDate){
		PfpTransDetail transDetail = new PfpTransDetail();

		transDetail.setPfpCustomerInfo(customerInfo);
		transDetail.setTransDate(transDate);

		if (vo != null) {
			transDetail.setTotalSavePrice(vo.getTotalAddMoney());
			transDetail.setTotalSpendPrice(vo.getTotalSpend());
			transDetail.setTotalRetrievePrice(vo.getTotalRetrieve());
			transDetail.setRemain(vo.getRemain());
			transDetail.setTotalLaterSave(vo.getTotalLaterAddMoney());
			transDetail.setTotalLaterSpend(vo.getTotalLaterSpend());
			transDetail.setTotalLaterRetrieve(vo.getTotalLaterRetrieve());
			//transDetail.setLaterRemain(vo.getLaterRemain());
		}
		else {
			transDetail.setTotalSavePrice(0);
			transDetail.setTotalSpendPrice(0);
			transDetail.setTotalRetrievePrice(0);
			transDetail.setRemain(0);
			transDetail.setTotalLaterSave(0);
			transDetail.setTotalLaterSpend(0);
			//transDetail.setLaterRemain(0);
		}

		transDetail.setTax(0);

		Date today = new Date();
		transDetail.setUpdateDate(today);
		transDetail.setCreateDate(today);

		return transDetail;
	}

	private SettlementVO createSettlementVO (SettlementVO vo, PfpCustomerInfo customerInfo){
		if(vo == null){
			vo = new SettlementVO();
			vo.setCustomerInfoId(customerInfo.getCustomerInfoId());
		}else{
			vo.setTransPrice(0);
		}

		return vo;
	}
}
