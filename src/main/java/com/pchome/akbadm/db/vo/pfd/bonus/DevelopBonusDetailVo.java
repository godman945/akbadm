package com.pchome.akbadm.db.vo.pfd.bonus;

import java.util.Date;

public class DevelopBonusDetailVo {

	private Date startDate;				// 開始時間
	private Date endDate;				// 結束時間
	private String bonusItemName;		// 獎金名稱
	private String bonusNote;			// 獎金說明
	private int developAmount;			// 開發間數	
	private float bonusMoney;			// 領取獎金

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public String getBonusItemName() {
		return bonusItemName;
	}

	public void setBonusItemName(String bonusItemName) {
		this.bonusItemName = bonusItemName;
	}

	public String getBonusNote() {
		return bonusNote;
	}

	public void setBonusNote(String bonusNote) {
		this.bonusNote = bonusNote;
	}

	public int getDevelopAmount() {
		return developAmount;
	}

	public void setDevelopAmount(int developAmount) {
		this.developAmount = developAmount;
	}

	public float getBonusMoney() {
		return bonusMoney;
	}

	public void setBonusMoney(float bonusMoney) {
		this.bonusMoney = bonusMoney;
	}
	
	
}
