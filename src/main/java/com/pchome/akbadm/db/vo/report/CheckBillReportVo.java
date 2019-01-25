package com.pchome.akbadm.db.vo.report;

public class CheckBillReportVo {

	private String date;
	private String pfpId;
	private float pvClkPrice;			// 廣告點擊費
	private float invalidClkPrice;		// 無效點擊費
	private float transLoss;			// 交易損失費用
	private float transPrice;			// 交易費用
	private float transRemain;			// 交易餘額
	private float recognizePrice;		// 攤提花費	
	private float recognizeRemain;		// 攤提餘額

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPfpId() {
		return pfpId;
	}

	public void setPfpId(String pfpId) {
		this.pfpId = pfpId;
	}

	public float getPvClkPrice() {
		return pvClkPrice;
	}

	public void setPvClkPrice(float pvClkPrice) {
		this.pvClkPrice = pvClkPrice;
	}

	public float getInvalidClkPrice() {
		return invalidClkPrice;
	}

	public void setInvalidClkPrice(float invalidClkPrice) {
		this.invalidClkPrice = invalidClkPrice;
	}

	public float getTransLoss() {
		return transLoss;
	}

	public void setTransLoss(float transLoss) {
		this.transLoss = transLoss;
	}

	public float getTransPrice() {
		return transPrice;
	}

	public void setTransPrice(float transPrice) {
		this.transPrice = transPrice;
	}

	public float getTransRemain() {
		return transRemain;
	}

	public void setTransRemain(float transRemain) {
		this.transRemain = transRemain;
	}

	public float getRecognizePrice() {
		return recognizePrice;
	}

	public void setRecognizePrice(float recognizePrice) {
		this.recognizePrice = recognizePrice;
	}

	public float getRecognizeRemain() {
		return recognizeRemain;
	}

	public void setRecognizeRemain(float recognizeRemain) {
		this.recognizeRemain = recognizeRemain;
	}

}
