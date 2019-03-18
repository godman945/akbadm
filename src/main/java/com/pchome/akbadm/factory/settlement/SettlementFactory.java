package com.pchome.akbadm.factory.settlement;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.enumerate.factory.EnumSettlementItem;

@Transactional
public class SettlementFactory {
	
	protected Logger log = LogManager.getRootLogger();

	private ASettlement settlementSaveMoney;
	private ASettlement settlementInvalidCost;
	private ASettlement settlementSpendCost;
	private ASettlement settlementFeedbackMoney;
	private ASettlement setlementGiftSnoMoney;
	private ASettlement settlementLaterSave;
	private ASettlement setlementLaterInvalid;
	private ASettlement settlementLaterSpend;
	private ASettlement settlementRetrieve;
	private ASettlement settlementLaterRefund;
	private ASettlement settlementAdvanceRefund;

	public ASettlement get(EnumSettlementItem enumSettlementItem){
		
		ASettlement settlementItem = null;
		
		switch(enumSettlementItem){
		
		case SAVE_MONEY:
			settlementItem = settlementSaveMoney;
			break;
		case FEEDBACK_MONEY:
			settlementItem = settlementFeedbackMoney;
			break;
		case INVALID_COST:
			settlementItem = settlementInvalidCost;
			break;
		case SPEND_COST:
			settlementItem = settlementSpendCost;
			break;
		case LATER_SAVE:
			settlementItem = settlementLaterSave;
			break;
		case LATER_INVALID:
			settlementItem = setlementLaterInvalid;
			break;
		case LATER_SPEND:
			settlementItem = settlementLaterSpend;
			break;
		case GIFT_SNO_MONEY:
			settlementItem = setlementGiftSnoMoney;
			break;
		case LATER_REFUND:
			settlementItem = settlementLaterRefund;
			break;
		case ADVANCE_REFUND:
			settlementItem = settlementAdvanceRefund;
			break;
//		case RETRIEVE: 有問題，暫時註解掉
//			settlementItem = settlementRetrieve;
//			break;
		default:
			break;
		}
		
		return settlementItem;
	}

	public void setSettlementSaveMoney(ASettlement settlementSaveMoney) {
		this.settlementSaveMoney = settlementSaveMoney;
	}

	public void setSettlementInvalidCost(ASettlement settlementInvalidCost) {
		this.settlementInvalidCost = settlementInvalidCost;
	}

	public void setSettlementSpendCost(ASettlement settlementSpendCost) {
		this.settlementSpendCost = settlementSpendCost;
	}

	public void setSettlementFeedbackMoney(ASettlement settlementFeedbackMoney) {
		this.settlementFeedbackMoney = settlementFeedbackMoney;
	}

	public void setSetlementGiftSnoMoney(ASettlement setlementGiftSnoMoney) {
		this.setlementGiftSnoMoney = setlementGiftSnoMoney;
	}

	public void setSettlementLaterSave(ASettlement settlementLaterSave) {
		this.settlementLaterSave = settlementLaterSave;
	}

	public void setSetlementLaterInvalid(ASettlement setlementLaterInvalid) {
		this.setlementLaterInvalid = setlementLaterInvalid;
	}

	public void setSettlementLaterSpend(ASettlement settlementLaterSpend) {
		this.settlementLaterSpend = settlementLaterSpend;
	}

	public void setSettlementRetrieve(ASettlement settlementRetrieve) {
		this.settlementRetrieve = settlementRetrieve;
	}

	public void setSettlementLaterRefund(ASettlement settlementLaterRefund) {
		this.settlementLaterRefund = settlementLaterRefund;
	}

	public void setSettlementAdvanceRefund(ASettlement settlementAdvanceRefund) {
		this.settlementAdvanceRefund = settlementAdvanceRefund;
	}
	
}
