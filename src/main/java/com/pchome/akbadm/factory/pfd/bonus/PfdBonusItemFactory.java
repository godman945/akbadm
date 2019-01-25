package com.pchome.akbadm.factory.pfd.bonus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pchome.enumerate.pfd.bonus.EnumPfdBonusItem;

public class PfdBonusItemFactory {

	protected Log log = LogFactory.getLog(this.getClass().getName());
	
	private APfdBonusItem pfdMonthBonus;
	private APfdBonusItem pfdQuarterBonus;
	private APfdBonusItem pfdYearBonus;
	private APfdBonusItem pfdMonthDevelopBonus;
	private APfdBonusItem pfdQuarterDevelopBonus;
	private APfdBonusItem pfdYearDevelopBonus;
	private APfdBonusItem pfdBonusYearEndSaleA;
	private APfdBonusItem pfdBonusYearEndSaleB;
	private APfdBonusItem pfdBonusYearEndSaleC;
	private APfdBonusItem pfdBonusYearEndSaleD;
	private APfdBonusItem pfdBonusYearEndSaleE;
	private APfdBonusItem pfdBonusYearEndSaleF;
	
	public APfdBonusItem getPfdBonusItem(EnumPfdBonusItem enumPfdBonusItem) {
		
		APfdBonusItem pfdBonusItem = null;
		
		switch(enumPfdBonusItem){
			case EVERY_MONTH_BONUS:
				pfdBonusItem = pfdMonthBonus;
				break;
			case EVERY_QUARTER_BONUS:
				pfdBonusItem = pfdQuarterBonus;
				break;
			case EVERY_YEAR_BONUS:
				pfdBonusItem = pfdYearBonus;
				break;
//			case EVERY_MONTH_DEVELOP_BONUS:
//				pfdBonusItem = pfdMonthDevelopBonus;
//				break;
//			case EVERY_QUARTER_DEVELOP_BONUS:
//				pfdBonusItem = pfdQuarterDevelopBonus;
//				break;			
//			case EVERY_YEAR_DEVELOP_BONUS:
//				pfdBonusItem = pfdYearDevelopBonus;
//				break;
//			case CASE_BONUS_YEAR_END_SALE_A:
//				pfdBonusItem = pfdBonusYearEndSaleA;
//				break;
//			case CASE_BONUS_YEAR_END_SALE_B:
//				pfdBonusItem = pfdBonusYearEndSaleB;
//				break;
//			case CASE_BONUS_YEAR_END_SALE_C:
//				pfdBonusItem = pfdBonusYearEndSaleC;
//				break;
//			case CASE_BONUS_YEAR_END_SALE_D:
//				pfdBonusItem = pfdBonusYearEndSaleD;
//				break;
//			case CASE_BONUS_YEAR_END_SALE_E:
//				pfdBonusItem = pfdBonusYearEndSaleE;
//				break;
//			case CASE_BONUS_YEAR_END_SALE_F:
//				pfdBonusItem = pfdBonusYearEndSaleF;
//				break;
			default:
				break;			
		}
		
		return pfdBonusItem;
	}

	public void setPfdMonthBonus(APfdBonusItem pfdMonthBonus) {
		this.pfdMonthBonus = pfdMonthBonus;
	}

	public void setPfdQuarterBonus(APfdBonusItem pfdQuarterBonus) {
		this.pfdQuarterBonus = pfdQuarterBonus;
	}

	public void setPfdYearBonus(APfdBonusItem pfdYearBonus) {
		this.pfdYearBonus = pfdYearBonus;
	}

	public void setPfdMonthDevelopBonus(APfdBonusItem pfdMonthDevelopBonus) {
		this.pfdMonthDevelopBonus = pfdMonthDevelopBonus;
	}

	public void setPfdQuarterDevelopBonus(APfdBonusItem pfdQuarterDevelopBonus) {
		this.pfdQuarterDevelopBonus = pfdQuarterDevelopBonus;
	}

	public void setPfdYearDevelopBonus(APfdBonusItem pfdYearDevelopBonus) {
		this.pfdYearDevelopBonus = pfdYearDevelopBonus;
	}

	public void setPfdBonusYearEndSaleA(APfdBonusItem pfdBonusYearEndSaleA) {
		this.pfdBonusYearEndSaleA = pfdBonusYearEndSaleA;
	}

	public void setPfdBonusYearEndSaleB(APfdBonusItem pfdBonusYearEndSaleB) {
		this.pfdBonusYearEndSaleB = pfdBonusYearEndSaleB;
	}

	public void setPfdBonusYearEndSaleC(APfdBonusItem pfdBonusYearEndSaleC) {
		this.pfdBonusYearEndSaleC = pfdBonusYearEndSaleC;
	}

	public void setPfdBonusYearEndSaleD(APfdBonusItem pfdBonusYearEndSaleD) {
		this.pfdBonusYearEndSaleD = pfdBonusYearEndSaleD;
	}

	public void setPfdBonusYearEndSaleE(APfdBonusItem pfdBonusYearEndSaleE) {
		this.pfdBonusYearEndSaleE = pfdBonusYearEndSaleE;
	}

	public void setPfdBonusYearEndSaleF(APfdBonusItem pfdBonusYearEndSaleF) {
		this.pfdBonusYearEndSaleF = pfdBonusYearEndSaleF;
	}

	
	
}
