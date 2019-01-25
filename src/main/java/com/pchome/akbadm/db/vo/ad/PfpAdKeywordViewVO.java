package com.pchome.akbadm.db.vo.ad;

public class PfpAdKeywordViewVO {

	private String adActionSeq = "";
	private String adActionName = "";
	private String adType = "";
	private String adGroupSeq = "";
	private String adGroupName = "";
	private int adGroupStatus = 0;
	private String adGroupStatusDesc = "";
	private String adKeywordSeq = "";
	private String adKeyword = "";
	private int adKeywordStatus = 0;
	private String adKeywordStatusDesc = "";
	private String adKeywordPvclkDevice = "";
	private float adKeywordChannelPrice = 0;
	private String adKeywordOrder = "";
	private String adKeywordType = "";
	
	//廣泛比對
	private float adKeywordSearchPrice = 0;
	private float suggestPrice = 0;
	private String adKeywordOpen = "";
	private Integer adKeywordPv = 0;
	private Integer adKeywordClk = 0;
	private double adKeywordClkRate = 0;
	private double adKeywordClkPrice = 0;
	private double adKeywordClkPriceAvg = 0;
	private Integer adKeywordInvalidClk = 0;
	private double adKeywordInvalidClkPrice = 0;
	private double adKeywordRankAvg = 0;
	
	//詞組比對
	private float adKeywordSearchPhrPrice = 0;
	private float phrSuggestPrice = 0;
	private String adKeywordPhrOpen = "";
	private Integer adKeywordPhrPv = 0;
	private Integer adKeywordPhrClk = 0;
	private double adKeywordPhrClkRate = 0;
	private double adKeywordPhrClkPrice = 0;
	private double adKeywordPhrClkPriceAvg = 0;
	private Integer adKeywordPhrInvalidClk = 0;
	private double adKeywordPhrInvalidClkPrice = 0;
	private double adKeywordPhrRankAvg = 0;
	
	//精準比對
	private float adKeywordSearchPrePrice = 0;
	private float preSuggestPrice = 0;
	private String adKeywordPreOpen = "";
	private Integer adKeywordPrePv = 0;
	private Integer adKeywordPreClk = 0;
	private double adKeywordPreClkRate = 0;
	private double adKeywordPreClkPrice = 0;
	private double adKeywordPreClkPriceAvg = 0;
	private Integer adKeywordPreInvalidClk = 0;
	private double adKeywordPreInvalidClkPrice = 0;
	private double adKeywordPreRankAvg = 0;
	
	//總計
	private Integer adKeywordPvSum = 0;
	private Integer adKeywordClkSum = 0;
	private double adKeywordClkRateSum = 0;
	private double adKeywordClkPriceSum = 0;
	private double adKeywordClkPriceAvgSum = 0;
	private Integer adKeywordInvalidClkSum = 0;
	private double adKeywordInvalidClkPriceSum = 0;
	
	
	private float adKeywordPvPrice = 0;
	private float adActionMax = 0;
	private String adKeywordCreateTime = "";
	private String memberId = "";
	private String customerInfoTitle = "";
	
	private Integer dataSize = 0;
	
	public String getAdActionSeq() {
		return adActionSeq;
	}
	public void setAdActionSeq(String adActionSeq) {
		this.adActionSeq = adActionSeq;
	}
	public String getAdActionName() {
		return adActionName;
	}
	public void setAdActionName(String adActionName) {
		this.adActionName = adActionName;
	}
	public String getAdType() {
		return adType;
	}
	public void setAdType(String adType) {
		this.adType = adType;
	}
	public String getAdGroupSeq() {
		return adGroupSeq;
	}
	public void setAdGroupSeq(String adGroupSeq) {
		this.adGroupSeq = adGroupSeq;
	}
	public String getAdGroupName() {
		return adGroupName;
	}
	public void setAdGroupName(String adGroupName) {
		this.adGroupName = adGroupName;
	}
	public int getAdGroupStatus() {
		return adGroupStatus;
	}
	public void setAdGroupStatus(int adGroupStatus) {
		this.adGroupStatus = adGroupStatus;
	}
	public String getAdGroupStatusDesc() {
		return adGroupStatusDesc;
	}
	public void setAdGroupStatusDesc(String adGroupStatusDesc) {
		this.adGroupStatusDesc = adGroupStatusDesc;
	}
	public String getAdKeywordSeq() {
		return adKeywordSeq;
	}
	public void setAdKeywordSeq(String adKeywordSeq) {
		this.adKeywordSeq = adKeywordSeq;
	}
	public String getAdKeyword() {
		return adKeyword;
	}
	public void setAdKeyword(String adKeyword) {
		this.adKeyword = adKeyword;
	}
	public int getAdKeywordStatus() {
		return adKeywordStatus;
	}
	public void setAdKeywordStatus(int adKeywordStatus) {
		this.adKeywordStatus = adKeywordStatus;
	}
	public String getAdKeywordStatusDesc() {
		return adKeywordStatusDesc;
	}
	public void setAdKeywordStatusDesc(String adKeywordStatusDesc) {
		this.adKeywordStatusDesc = adKeywordStatusDesc;
	}
	public String getAdKeywordPvclkDevice() {
		return adKeywordPvclkDevice;
	}
	public void setAdKeywordPvclkDevice(String adKeywordPvclkDevice) {
		this.adKeywordPvclkDevice = adKeywordPvclkDevice;
	}
	public float getAdKeywordSearchPrice() {
		return adKeywordSearchPrice;
	}
	public void setAdKeywordSearchPrice(float adKeywordSearchPrice) {
		this.adKeywordSearchPrice = adKeywordSearchPrice;
	}
	public float getAdKeywordChannelPrice() {
		return adKeywordChannelPrice;
	}
	public void setAdKeywordChannelPrice(float adKeywordChannelPrice) {
		this.adKeywordChannelPrice = adKeywordChannelPrice;
	}
	public String getAdKeywordOrder() {
		return adKeywordOrder;
	}
	public void setAdKeywordOrder(String adKeywordOrder) {
		this.adKeywordOrder = adKeywordOrder;
	}
	public String getAdKeywordType() {
		return adKeywordType;
	}
	public void setAdKeywordType(String adKeywordType) {
		this.adKeywordType = adKeywordType;
	}
	public float getSuggestPrice() {
		return suggestPrice;
	}
	public void setSuggestPrice(float suggestPrice) {
		this.suggestPrice = suggestPrice;
	}
	public String getAdKeywordOpen() {
		return adKeywordOpen;
	}
	public void setAdKeywordOpen(String adKeywordOpen) {
		this.adKeywordOpen = adKeywordOpen;
	}
	public Integer getAdKeywordPv() {
		return adKeywordPv;
	}
	public void setAdKeywordPv(Integer adKeywordPv) {
		this.adKeywordPv = adKeywordPv;
	}
	public Integer getAdKeywordClk() {
		return adKeywordClk;
	}
	public void setAdKeywordClk(Integer adKeywordClk) {
		this.adKeywordClk = adKeywordClk;
	}
	public double getAdKeywordClkRate() {
		return adKeywordClkRate;
	}
	public void setAdKeywordClkRate(double adKeywordClkRate) {
		this.adKeywordClkRate = adKeywordClkRate;
	}
	public double getAdKeywordClkPrice() {
		return adKeywordClkPrice;
	}
	public void setAdKeywordClkPrice(double adKeywordClkPrice) {
		this.adKeywordClkPrice = adKeywordClkPrice;
	}
	public double getAdKeywordClkPriceAvg() {
		return adKeywordClkPriceAvg;
	}
	public void setAdKeywordClkPriceAvg(double adKeywordClkPriceAvg) {
		this.adKeywordClkPriceAvg = adKeywordClkPriceAvg;
	}
	public Integer getAdKeywordInvalidClk() {
		return adKeywordInvalidClk;
	}
	public void setAdKeywordInvalidClk(Integer adKeywordInvalidClk) {
		this.adKeywordInvalidClk = adKeywordInvalidClk;
	}
	public double getAdKeywordInvalidClkPrice() {
		return adKeywordInvalidClkPrice;
	}
	public void setAdKeywordInvalidClkPrice(double adKeywordInvalidClkPrice) {
		this.adKeywordInvalidClkPrice = adKeywordInvalidClkPrice;
	}
	public double getAdKeywordRankAvg() {
		return adKeywordRankAvg;
	}
	public void setAdKeywordRankAvg(double adKeywordRankAvg) {
		this.adKeywordRankAvg = adKeywordRankAvg;
	}
	public float getPhrSuggestPrice() {
		return phrSuggestPrice;
	}
	public void setPhrSuggestPrice(float phrSuggestPrice) {
		this.phrSuggestPrice = phrSuggestPrice;
	}
	public String getAdKeywordPhrOpen() {
		return adKeywordPhrOpen;
	}
	public void setAdKeywordPhrOpen(String adKeywordPhrOpen) {
		this.adKeywordPhrOpen = adKeywordPhrOpen;
	}
	public Integer getAdKeywordPhrPv() {
		return adKeywordPhrPv;
	}
	public void setAdKeywordPhrPv(Integer adKeywordPhrPv) {
		this.adKeywordPhrPv = adKeywordPhrPv;
	}
	public Integer getAdKeywordPhrClk() {
		return adKeywordPhrClk;
	}
	public void setAdKeywordPhrClk(Integer adKeywordPhrClk) {
		this.adKeywordPhrClk = adKeywordPhrClk;
	}
	public double getAdKeywordPhrClkRate() {
		return adKeywordPhrClkRate;
	}
	public void setAdKeywordPhrClkRate(double adKeywordPhrClkRate) {
		this.adKeywordPhrClkRate = adKeywordPhrClkRate;
	}
	public double getAdKeywordPhrClkPrice() {
		return adKeywordPhrClkPrice;
	}
	public void setAdKeywordPhrClkPrice(double adKeywordPhrClkPrice) {
		this.adKeywordPhrClkPrice = adKeywordPhrClkPrice;
	}
	public double getAdKeywordPhrClkPriceAvg() {
		return adKeywordPhrClkPriceAvg;
	}
	public void setAdKeywordPhrClkPriceAvg(double adKeywordPhrClkPriceAvg) {
		this.adKeywordPhrClkPriceAvg = adKeywordPhrClkPriceAvg;
	}
	public Integer getAdKeywordPhrInvalidClk() {
		return adKeywordPhrInvalidClk;
	}
	public void setAdKeywordPhrInvalidClk(Integer adKeywordPhrInvalidClk) {
		this.adKeywordPhrInvalidClk = adKeywordPhrInvalidClk;
	}
	public double getAdKeywordPhrInvalidClkPrice() {
		return adKeywordPhrInvalidClkPrice;
	}
	public void setAdKeywordPhrInvalidClkPrice(double adKeywordPhrInvalidClkPrice) {
		this.adKeywordPhrInvalidClkPrice = adKeywordPhrInvalidClkPrice;
	}
	public double getAdKeywordPhrRankAvg() {
		return adKeywordPhrRankAvg;
	}
	public void setAdKeywordPhrRankAvg(double adKeywordPhrRankAvg) {
		this.adKeywordPhrRankAvg = adKeywordPhrRankAvg;
	}
	public float getPreSuggestPrice() {
		return preSuggestPrice;
	}
	public void setPreSuggestPrice(float preSuggestPrice) {
		this.preSuggestPrice = preSuggestPrice;
	}
	public String getAdKeywordPreOpen() {
		return adKeywordPreOpen;
	}
	public void setAdKeywordPreOpen(String adKeywordPreOpen) {
		this.adKeywordPreOpen = adKeywordPreOpen;
	}
	public Integer getAdKeywordPrePv() {
		return adKeywordPrePv;
	}
	public void setAdKeywordPrePv(Integer adKeywordPrePv) {
		this.adKeywordPrePv = adKeywordPrePv;
	}
	public Integer getAdKeywordPreClk() {
		return adKeywordPreClk;
	}
	public void setAdKeywordPreClk(Integer adKeywordPreClk) {
		this.adKeywordPreClk = adKeywordPreClk;
	}
	public double getAdKeywordPreClkRate() {
		return adKeywordPreClkRate;
	}
	public void setAdKeywordPreClkRate(double adKeywordPreClkRate) {
		this.adKeywordPreClkRate = adKeywordPreClkRate;
	}
	public double getAdKeywordPreClkPrice() {
		return adKeywordPreClkPrice;
	}
	public void setAdKeywordPreClkPrice(double adKeywordPreClkPrice) {
		this.adKeywordPreClkPrice = adKeywordPreClkPrice;
	}
	public double getAdKeywordPreClkPriceAvg() {
		return adKeywordPreClkPriceAvg;
	}
	public void setAdKeywordPreClkPriceAvg(double adKeywordPreClkPriceAvg) {
		this.adKeywordPreClkPriceAvg = adKeywordPreClkPriceAvg;
	}
	public Integer getAdKeywordPreInvalidClk() {
		return adKeywordPreInvalidClk;
	}
	public void setAdKeywordPreInvalidClk(Integer adKeywordPreInvalidClk) {
		this.adKeywordPreInvalidClk = adKeywordPreInvalidClk;
	}
	public double getAdKeywordPreInvalidClkPrice() {
		return adKeywordPreInvalidClkPrice;
	}
	public void setAdKeywordPreInvalidClkPrice(double adKeywordPreInvalidClkPrice) {
		this.adKeywordPreInvalidClkPrice = adKeywordPreInvalidClkPrice;
	}
	public double getAdKeywordPreRankAvg() {
		return adKeywordPreRankAvg;
	}
	public void setAdKeywordPreRankAvg(double adKeywordPreRankAvg) {
		this.adKeywordPreRankAvg = adKeywordPreRankAvg;
	}
	public Integer getAdKeywordPvSum() {
		return adKeywordPvSum;
	}
	public void setAdKeywordPvSum(Integer adKeywordPvSum) {
		this.adKeywordPvSum = adKeywordPvSum;
	}
	public Integer getAdKeywordClkSum() {
		return adKeywordClkSum;
	}
	public void setAdKeywordClkSum(Integer adKeywordClkSum) {
		this.adKeywordClkSum = adKeywordClkSum;
	}
	public double getAdKeywordClkRateSum() {
		return adKeywordClkRateSum;
	}
	public void setAdKeywordClkRateSum(double adKeywordClkRateSum) {
		this.adKeywordClkRateSum = adKeywordClkRateSum;
	}
	public double getAdKeywordClkPriceSum() {
		return adKeywordClkPriceSum;
	}
	public void setAdKeywordClkPriceSum(double adKeywordClkPriceSum) {
		this.adKeywordClkPriceSum = adKeywordClkPriceSum;
	}
	public double getAdKeywordClkPriceAvgSum() {
		return adKeywordClkPriceAvgSum;
	}
	public void setAdKeywordClkPriceAvgSum(double adKeywordClkPriceAvgSum) {
		this.adKeywordClkPriceAvgSum = adKeywordClkPriceAvgSum;
	}
	public Integer getAdKeywordInvalidClkSum() {
		return adKeywordInvalidClkSum;
	}
	public void setAdKeywordInvalidClkSum(Integer adKeywordInvalidClkSum) {
		this.adKeywordInvalidClkSum = adKeywordInvalidClkSum;
	}
	public double getAdKeywordInvalidClkPriceSum() {
		return adKeywordInvalidClkPriceSum;
	}
	public void setAdKeywordInvalidClkPriceSum(double adKeywordInvalidClkPriceSum) {
		this.adKeywordInvalidClkPriceSum = adKeywordInvalidClkPriceSum;
	}
	public float getAdKeywordPvPrice() {
		return adKeywordPvPrice;
	}
	public void setAdKeywordPvPrice(float adKeywordPvPrice) {
		this.adKeywordPvPrice = adKeywordPvPrice;
	}
	public float getAdActionMax() {
		return adActionMax;
	}
	public void setAdActionMax(float adActionMax) {
		this.adActionMax = adActionMax;
	}
	public String getAdKeywordCreateTime() {
		return adKeywordCreateTime;
	}
	public void setAdKeywordCreateTime(String adKeywordCreateTime) {
		this.adKeywordCreateTime = adKeywordCreateTime;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getCustomerInfoTitle() {
		return customerInfoTitle;
	}
	public void setCustomerInfoTitle(String customerInfoTitle) {
		this.customerInfoTitle = customerInfoTitle;
	}
	public float getAdKeywordSearchPhrPrice() {
		return adKeywordSearchPhrPrice;
	}
	public void setAdKeywordSearchPhrPrice(float adKeywordSearchPhrPrice) {
		this.adKeywordSearchPhrPrice = adKeywordSearchPhrPrice;
	}
	public float getAdKeywordSearchPrePrice() {
		return adKeywordSearchPrePrice;
	}
	public void setAdKeywordSearchPrePrice(float adKeywordSearchPrePrice) {
		this.adKeywordSearchPrePrice = adKeywordSearchPrePrice;
	}
	public Integer getDataSize() {
		return dataSize;
	}
	public void setDataSize(Integer dataSize) {
		this.dataSize = dataSize;
	}
	
}
