package com.pchome.akbadm.db.vo;


public class AdPvclkVO implements Comparable{

	Object[] pfpAdPvclk;
	float sortPrice = 0;	
	float adClickRate = 0;
	float averageCost = 0;
	float shortCost = 0;
	float adPvclkCost = 0;
	
	
	public AdPvclkVO(Object[] pfpAdPvclk, float sortPrice) {
		this.pfpAdPvclk = pfpAdPvclk;
		this.sortPrice = sortPrice;
		adClickRate = Float.parseFloat(this.pfpAdPvclk[1].toString()) / Float.parseFloat(this.pfpAdPvclk[0].toString()) * 100;
		averageCost = Float.parseFloat(this.pfpAdPvclk[2].toString()) / Float.parseFloat(this.pfpAdPvclk[1].toString());
		shortCost = Float.parseFloat(this.pfpAdPvclk[7].toString()) - Float.parseFloat(this.pfpAdPvclk[2].toString());
	}


	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		AdPvclkVO otherAdPvclkVO = (AdPvclkVO)o;
	
		if(this.sortPrice < otherAdPvclkVO.sortPrice){
			return 1;
		}
		else if(this.sortPrice == otherAdPvclkVO.sortPrice){
			return 0;
		}
		else{
			return -1;
		}
		
	}

	public Object[] getPfpAdPvclk() {
		return pfpAdPvclk;
	}
	

	public float getSortPrice() {
		return sortPrice;
	}


	public float getAdPvclkCost() {
		return adPvclkCost;
	}


	public float getAdClickRate() {
		return adClickRate;
	}

	public float getAverageCost() {
		return averageCost;
	}


	public float getShortCost() {
		return shortCost;
	}
	
	

}
