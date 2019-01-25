package com.pchome.akbadm.db.service.ad;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.ad.PfpAdKeywordDAO;
import com.pchome.akbadm.db.pojo.PfpAdKeyword;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.ad.PfpAdKeywordViewVO;
import com.pchome.enumerate.ad.EnumAdStatus;

public class PfpAdKeywordService extends BaseService<PfpAdKeyword, String> implements IPfpAdKeywordService {
	
	private String definePrice;
	
	public PfpAdKeyword findPfpAdKeywordBySeq(String adKeywordSeq) throws Exception{
		return ((PfpAdKeywordDAO) dao).findPfpAdKeywordBySeq(adKeywordSeq);
	}
	
	public void updatePfpAdKeyword(PfpAdKeyword pfpAdKeyword) throws Exception{
		((PfpAdKeywordDAO) dao).saveOrUpdate(pfpAdKeyword);
	}
	
	public float countKeywordSysprice(String keyword, float sysprice, String today) throws Exception{

		List<Object> objects = ((PfpAdKeywordDAO) dao).getAdKeywordPriceList(keyword, sysprice, today);

		float countPrice = Float.parseFloat(definePrice);	 // 廣告預設價錢	

		if(objects != null && objects.size() < sysprice){
			countPrice += objects.size();
		}

		//log.info(" today = "+today);
		//log.info(" keyword  definePrice = "+definePrice);
		//log.info(" keyword  size = "+objects.size());
		//log.info(" keyword  countPrice = "+countPrice);

		return countPrice;
	}

	public void setDefinePrice(String definePrice) {
		this.definePrice = definePrice;
	}
	
	
	public List<PfpAdKeywordViewVO> getAdKeywordViewReport(Map<String,String> adKeywordViewConditionMap) throws Exception{
	    List<Object> adKeywordReportObjList = ((PfpAdKeywordDAO)dao).getAdKeywordReport(adKeywordViewConditionMap);
	    Map<String,Object> reportData = ((PfpAdKeywordDAO)dao).getAdKeywordReportData(adKeywordViewConditionMap);
	    
	    List<PfpAdKeywordViewVO> adKeywordReportViewList = new ArrayList<PfpAdKeywordViewVO>();
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    DateFormat chtDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date adCreateDate = null;
	    for (int i = 0; i < adKeywordReportObjList.size(); i++) {
		Object[] obj = (Object[]) adKeywordReportObjList.get(i);
		PfpAdKeywordViewVO pfpAdKeywordViewVO = new PfpAdKeywordViewVO();
		//關鍵字建立日期
		obj[0] = obj[0].toString().replace(".0", "");
		adCreateDate = dateFormat.parse(obj[0].toString());
		pfpAdKeywordViewVO.setAdKeywordCreateTime(chtDateFormat.format(adCreateDate).toString());
		//
		pfpAdKeywordViewVO.setMemberId(obj[1].toString());
		//
		pfpAdKeywordViewVO.setCustomerInfoTitle(obj[2].toString());
		//廣告名稱
		pfpAdKeywordViewVO.setAdActionName(obj[3].toString());
		//廣告類別
		pfpAdKeywordViewVO.setAdGroupSeq(obj[4].toString());
		//類別名稱
		pfpAdKeywordViewVO.setAdGroupName(obj[5].toString());
		//關鍵字
		pfpAdKeywordViewVO.setAdKeyword(obj[6].toString());
		//關鍵字狀態
		for(EnumAdStatus status:EnumAdStatus.values()){
		    int adKeyWordStatus = Integer.parseInt(obj[7].toString());
		    if(status.getStatusId() == adKeyWordStatus){
			pfpAdKeywordViewVO.setAdKeywordStatus(adKeyWordStatus);
			pfpAdKeywordViewVO.setAdKeywordStatusDesc(status.getStatusRemark());
		    }
		}	
		if(adKeywordViewConditionMap.get("adKeywordPvclkDevice").equals("ALL Device")){
		    pfpAdKeywordViewVO.setAdKeywordPvclkDevice("全部");
		} else if(adKeywordViewConditionMap.get("adKeywordPvclkDevice").equals("PC")){
			pfpAdKeywordViewVO.setAdKeywordPvclkDevice("電腦");
		} else  if(adKeywordViewConditionMap.get("adKeywordPvclkDevice").equals("mobile")){
			pfpAdKeywordViewVO.setAdKeywordPvclkDevice("行動裝置");
		} else {
			pfpAdKeywordViewVO.setAdKeywordPvclkDevice("全部");
		}
		
		if("1".equals(obj[8].toString())){
			pfpAdKeywordViewVO.setAdKeywordOpen("開啟");
		} else {
			pfpAdKeywordViewVO.setAdKeywordOpen("關閉");
		}
		
		if("1".equals(obj[9].toString())){
			pfpAdKeywordViewVO.setAdKeywordPhrOpen("開啟");
		} else {
			pfpAdKeywordViewVO.setAdKeywordPhrOpen("關閉");
		}
		
		if("1".equals(obj[10].toString())){
			pfpAdKeywordViewVO.setAdKeywordPreOpen("開啟");
		} else {
			pfpAdKeywordViewVO.setAdKeywordPreOpen("關閉");
		}
		
		//出價
		pfpAdKeywordViewVO.setAdKeywordSearchPrice(Float.parseFloat(obj[11].toString()));
		pfpAdKeywordViewVO.setAdKeywordSearchPhrPrice(Float.parseFloat(obj[12].toString()));
		pfpAdKeywordViewVO.setAdKeywordSearchPrePrice(Float.parseFloat(obj[13].toString()));
		
		String adKeywordSeq = obj[14].toString();
		
		//廣泛比對
		Integer pv = 0;
		Integer clk = 0;
		double clkPrice = 0;
		Integer invalidClk = 0;
		double invalidClkPrice = 0;
		double clkRate = 0;
		double adClkPriceAvg = 0;
		
		//詞組比對
		Integer phrPv = 0;
		Integer phrClk = 0;
		double phrClkPrice = 0;
		Integer phrInvalidClk = 0;
		double phrInvalidClkPrice = 0;
		double phrClkRate = 0;
		double phrAdClkPriceAvg = 0;
		
		//精準比對
		Integer prePv = 0;
		Integer preClk = 0;
		double preClkPrice = 0;
		Integer preInvalidClk = 0;
		double preInvalidClkPrice = 0;
		double preClkRate = 0;
		double preAdClkPriceAvg = 0;
		
		if(reportData.get(adKeywordSeq) != null){
			//廣泛比對
			Object[] objData = (Object[]) reportData.get(adKeywordSeq);
			pv = Integer.parseInt(objData[1].toString());
			clk = Integer.parseInt(objData[2].toString());
			clkPrice = Double.parseDouble(objData[3].toString());
			invalidClk = Integer.parseInt(objData[4].toString());
			invalidClkPrice = Double.parseDouble(objData[5].toString());
			//詞組比對
			phrPv = Integer.parseInt(objData[6].toString());
			phrClk = Integer.parseInt(objData[7].toString());
			phrClkPrice = Double.parseDouble(objData[8].toString());
			phrInvalidClk = Integer.parseInt(objData[9].toString());
			phrInvalidClkPrice = Double.parseDouble(objData[10].toString());
			//精準比對
			prePv = Integer.parseInt(objData[11].toString());
			preClk = Integer.parseInt(objData[12].toString());
			preClkPrice = Double.parseDouble(objData[13].toString());
			preInvalidClk = Integer.parseInt(objData[14].toString());
			preInvalidClkPrice = Double.parseDouble(objData[15].toString());
		}
		
		//總計
		Integer pvSum = pv + phrPv + prePv;
		Integer clkSum = clk + phrClk + preClk;
		double clkPriceSum = clkPrice + phrClkPrice + preClkPrice;
		Integer invalidClkSum = invalidClk + phrInvalidClk + preInvalidClk;
		double invalidClkPriceSum = invalidClkPrice + phrInvalidClkPrice + preInvalidClkPrice;
		double clkRateSum = 0;
		double adClkPriceAvgSum = 0;
		
		pfpAdKeywordViewVO.setAdKeywordPv(pv);
		pfpAdKeywordViewVO.setAdKeywordClk(clk);
		pfpAdKeywordViewVO.setAdKeywordClkPrice(clkPrice);
		pfpAdKeywordViewVO.setAdKeywordInvalidClk(invalidClk);
		pfpAdKeywordViewVO.setAdKeywordInvalidClkPrice(invalidClkPrice);
		pfpAdKeywordViewVO.setAdKeywordPhrPv(phrPv);
		pfpAdKeywordViewVO.setAdKeywordPhrClk(phrClk);
		pfpAdKeywordViewVO.setAdKeywordPhrClkPrice(phrClkPrice);
		pfpAdKeywordViewVO.setAdKeywordPhrInvalidClk(phrInvalidClk);
		pfpAdKeywordViewVO.setAdKeywordPhrInvalidClkPrice(phrInvalidClkPrice);
		pfpAdKeywordViewVO.setAdKeywordPrePv(prePv);
		pfpAdKeywordViewVO.setAdKeywordPreClk(preClk);
		pfpAdKeywordViewVO.setAdKeywordPreClkPrice(preClkPrice);
		pfpAdKeywordViewVO.setAdKeywordPreInvalidClk(preInvalidClk);
		pfpAdKeywordViewVO.setAdKeywordPreInvalidClkPrice(preInvalidClkPrice);
		pfpAdKeywordViewVO.setAdKeywordPvSum(pvSum);
		pfpAdKeywordViewVO.setAdKeywordClkSum(clkSum);
		pfpAdKeywordViewVO.setAdKeywordClkPriceSum(clkPriceSum);
		pfpAdKeywordViewVO.setAdKeywordInvalidClkSum(invalidClkSum);
		pfpAdKeywordViewVO.setAdKeywordInvalidClkPriceSum(invalidClkPriceSum);
		//計算點擊率
		if(pv != 0 && clk != 0){
		    clkRate = (double) clk / (double) pv * 100;
		}
		pfpAdKeywordViewVO.setAdKeywordClkRate(clkRate);
		if(phrPv != 0 && phrClk != 0){
			phrClkRate = (double) phrClk / (double) phrPv * 100;
		}
		pfpAdKeywordViewVO.setAdKeywordPhrClkRate(phrClkRate);
		if(prePv != 0 && preClk != 0){
			preClkRate = (double) preClk / (double) prePv * 100;
		}
		pfpAdKeywordViewVO.setAdKeywordPreClkRate(preClkRate);
		if(pvSum != 0 && clkSum != 0){
		    clkRateSum = (double) clkSum / (double) pvSum * 100;
		}
		pfpAdKeywordViewVO.setAdKeywordClkRateSum(clkRateSum);
		//計算平均點選費用
		if(clkPrice != (double) 0 && clk != 0){
			adClkPriceAvg = clkPrice / (double) clk;
		}
		pfpAdKeywordViewVO.setAdKeywordClkPriceAvg(adClkPriceAvg);
		if(phrClkPrice != (double) 0 && phrClk != 0){
			phrAdClkPriceAvg = phrClkPrice / (double) phrClk;
		}
		pfpAdKeywordViewVO.setAdKeywordPhrClkPriceAvg(phrAdClkPriceAvg);
		if(preClkPrice != (double) 0 && preClk != 0){
			preAdClkPriceAvg = preClkPrice / (double) preClk;
		}
		pfpAdKeywordViewVO.setAdKeywordPreClkPriceAvg(preAdClkPriceAvg);
		if(clkPriceSum != (double) 0 && clkSum != 0){
			adClkPriceAvgSum = clkPriceSum / (double) clkSum;
		}
		pfpAdKeywordViewVO.setAdKeywordClkPriceAvgSum(adClkPriceAvgSum);
		
		
		/*//關鍵字
		pfpAdKeywordViewVO.setAdKeyword(obj[3].toString());
		//關鍵字狀態
		for(EnumAdStatus status:EnumAdStatus.values()){
		    int adKeyWordStatus = Integer.parseInt(obj[4].toString());
		    int adGroupStatus = Integer.parseInt(obj[15].toString());
		    if(status.getStatusId() == adKeyWordStatus){
			pfpAdKeywordViewVO.setAdKeywordStatus(adKeyWordStatus);
			pfpAdKeywordViewVO.setAdKeywordStatusDesc(status.getStatusRemark());
		    }
		    if(status.getStatusId() == adGroupStatus){
			pfpAdKeywordViewVO.setAdGroupStatus(adGroupStatus);
			pfpAdKeywordViewVO.setAdGroupStatusDesc(status.getStatusRemark());
		    }
		}	
		if(adKeywordViewConditionMap.get("adKeywordPvclkDevice").equals("ALL Device")){
		    pfpAdKeywordViewVO.setAdKeywordPvclkDevice("ALL Device");
		}else{
			adKeywordPvclkDevice = obj[5]== null ? "" : obj[5].toString();
		    pfpAdKeywordViewVO.setAdKeywordPvclkDevice(adKeywordPvclkDevice);
		}
		
		
		pfpAdKeywordViewVO.setAdKeywordSearchPrice(Float.parseFloat(obj[6].toString()));
		pfpAdKeywordViewVO.setAdKeywordChannelPrice(Float.parseFloat(obj[7].toString()));
		pfpAdKeywordViewVO.setAdKeywordPv(Integer.valueOf(obj[8].toString()));
		pfpAdKeywordViewVO.setAdKeywordClk(Integer.valueOf(obj[9].toString()));
		//計算點擊率
		if(!StringUtils.isEmpty(obj[9].toString()) && Integer.valueOf(obj[9].toString()) > 0){
		    clkRate = (float)Integer.valueOf(obj[9].toString()) / (float) Integer.valueOf(obj[8].toString()) * 100;
		    pfpAdKeywordViewVO.setAdKeywordClkRate(clkRate);
		}
		//費用
		pfpAdKeywordViewVO.setAdKeywordClkPrice(Float.parseFloat(obj[10].toString()));
		//計算平均點選費用
		adPvPrice = Double.parseDouble(obj[10].toString());
		if(adPvPrice > 0){
		    adClkPriceAvg =(float) (Double.parseDouble(obj[10].toString()) / Integer.valueOf(obj[9].toString()));
		    pfpAdKeywordViewVO.setAdKeywordClkPriceAvg(adClkPriceAvg);
		}
		//無效點擊數
		pfpAdKeywordViewVO.setAdKeywordInvalidClk(Integer.valueOf(obj[11].toString()));
		//無效點擊費用
		adInvalidClkPrice = Double.parseDouble(obj[12].toString());
		pfpAdKeywordViewVO.setAdKeywordInvalidClkPrice(adInvalidClkPrice.intValue());
		pfpAdKeywordViewVO.setAdActionName(obj[13].toString());
		pfpAdKeywordViewVO.setAdGroupName(obj[14].toString());
		pfpAdKeywordViewVO.setAdGroupSeq(obj[16].toString());*/
		adKeywordReportViewList.add(pfpAdKeywordViewVO);
		
	    }
	    return adKeywordReportViewList;
	}

	public PfpAdKeywordViewVO getAdKeywordViewReportSize(Map<String,String> adKeywordViewConditionMap) throws Exception{
		
		PfpAdKeywordViewVO totalVo = new PfpAdKeywordViewVO();
		adKeywordViewConditionMap.put("pageNo", "-1");
		List<Object> adKeywordReportObjList = ((PfpAdKeywordDAO)dao).getAdKeywordReportSeq(adKeywordViewConditionMap);
		Map<String,Object> reportData = ((PfpAdKeywordDAO)dao).getAdKeywordReportData(adKeywordViewConditionMap);
	    
	    //廣泛比對
		Integer pv = 0;
		Integer clk = 0;
		double clkPrice = 0;
		Integer invalidClk = 0;
		double invalidClkPrice = 0;
		double clkRate = 0;
		double adClkPriceAvg = 0;
		
		//詞組比對
		Integer phrPv = 0;
		Integer phrClk = 0;
		double phrClkPrice = 0;
		Integer phrInvalidClk = 0;
		double phrInvalidClkPrice = 0;
		double phrClkRate = 0;
		double phrAdClkPriceAvg = 0;
		
		//精準比對
		Integer prePv = 0;
		Integer preClk = 0;
		double preClkPrice = 0;
		Integer preInvalidClk = 0;
		double preInvalidClkPrice = 0;
		double preClkRate = 0;
		double preAdClkPriceAvg = 0;
		
		//筆數
		Integer totalSize = 0;
		
		if(!adKeywordReportObjList.isEmpty()){
			totalSize = adKeywordReportObjList.size();
			for (int i = 0; i < adKeywordReportObjList.size(); i++) {
				Object obj = (Object) adKeywordReportObjList.get(i);
				String adKeywordSeq = obj.toString();
				
				if(reportData.get(adKeywordSeq) != null){
					//廣泛比對
					Object[] objData = (Object[]) reportData.get(adKeywordSeq);
					pv += Integer.parseInt(objData[1].toString());
					clk += Integer.parseInt(objData[2].toString());
					clkPrice += Double.parseDouble(objData[3].toString());
					invalidClk += Integer.parseInt(objData[4].toString());
					invalidClkPrice += Double.parseDouble(objData[5].toString());
					//詞組比對
					phrPv += Integer.parseInt(objData[6].toString());
					phrClk += Integer.parseInt(objData[7].toString());
					phrClkPrice += Double.parseDouble(objData[8].toString());
					phrInvalidClk += Integer.parseInt(objData[9].toString());
					phrInvalidClkPrice += Double.parseDouble(objData[10].toString());
					//精準比對
					prePv += Integer.parseInt(objData[11].toString());
					preClk += Integer.parseInt(objData[12].toString());
					preClkPrice += Double.parseDouble(objData[13].toString());
					preInvalidClk += Integer.parseInt(objData[14].toString());
					preInvalidClkPrice += Double.parseDouble(objData[15].toString());
				}
				
			}
		}
		
		//總計
		Integer pvSum = pv + phrPv + prePv;
		Integer clkSum = clk + phrClk + preClk;
		double clkPriceSum = clkPrice + phrClkPrice + preClkPrice;
		Integer invalidClkSum = invalidClk + phrInvalidClk + preInvalidClk;
		double invalidClkPriceSum = invalidClkPrice + phrInvalidClkPrice + preInvalidClkPrice;
		double clkRateSum = 0;
		double adClkPriceAvgSum = 0;
		
		totalVo.setAdKeywordPv(pv);
		totalVo.setAdKeywordClk(clk);
		totalVo.setAdKeywordClkPrice(clkPrice);
		totalVo.setAdKeywordInvalidClk(invalidClk);
		totalVo.setAdKeywordInvalidClkPrice(invalidClkPrice);
		totalVo.setAdKeywordPhrPv(phrPv);
		totalVo.setAdKeywordPhrClk(phrClk);
		totalVo.setAdKeywordPhrClkPrice(phrClkPrice);
		totalVo.setAdKeywordPhrInvalidClk(phrInvalidClk);
		totalVo.setAdKeywordPhrInvalidClkPrice(phrInvalidClkPrice);
		totalVo.setAdKeywordPrePv(prePv);
		totalVo.setAdKeywordPreClk(preClk);
		totalVo.setAdKeywordPreClkPrice(preClkPrice);
		totalVo.setAdKeywordPreInvalidClk(preInvalidClk);
		totalVo.setAdKeywordPreInvalidClkPrice(preInvalidClkPrice);
		totalVo.setAdKeywordPvSum(pvSum);
		totalVo.setAdKeywordClkSum(clkSum);
		totalVo.setAdKeywordClkPriceSum(clkPriceSum);
		totalVo.setAdKeywordInvalidClkSum(invalidClkSum);
		totalVo.setAdKeywordInvalidClkPriceSum(invalidClkPriceSum);
		//計算點擊率
		if(pv != 0 && clk != 0){
		    clkRate = (double) clk / (double) pv * 100;
		}
		totalVo.setAdKeywordClkRate(clkRate);
		if(phrPv != 0 && phrClk != 0){
			phrClkRate = (double) phrClk / (double) phrPv * 100;
		}
		totalVo.setAdKeywordPhrClkRate(phrClkRate);
		if(prePv != 0 && preClk != 0){
			preClkRate = (double) preClk / (double) prePv * 100;
		}
		totalVo.setAdKeywordPreClkRate(preClkRate);
		if(pvSum != 0 && clkSum != 0){
		    clkRateSum = (double) clkSum / (double) pvSum * 100;
		}
		totalVo.setAdKeywordClkRateSum(clkRateSum);
		//計算平均點選費用
		if(clkPrice != (double) 0 && clk != 0){
			adClkPriceAvg = clkPrice / (double) clk;
		}
		totalVo.setAdKeywordClkPriceAvg(adClkPriceAvg);
		if(phrClkPrice != (double) 0 && phrClk != 0){
			phrAdClkPriceAvg = phrClkPrice / (double) phrClk;
		}
		totalVo.setAdKeywordPhrClkPriceAvg(phrAdClkPriceAvg);
		if(preClkPrice != (double) 0 && preClk != 0){
			preAdClkPriceAvg = preClkPrice / (double) preClk;
		}
		totalVo.setAdKeywordPreClkPriceAvg(preAdClkPriceAvg);
		if(clkPriceSum != (double) 0 && clkSum != 0){
			adClkPriceAvgSum = clkPriceSum / (double) clkSum;
		}
		totalVo.setAdKeywordClkPriceAvgSum(adClkPriceAvgSum);
		
		totalVo.setDataSize(totalSize);
		
		return totalVo;
	    
	}

	public List<PfpAdKeywordViewVO> getAdKeywordView(String userAccount, String searchAdStatus, int adType, String keyword, String adGroupSeq, String adKeywordPvclkDevice, String dateType, Date startDate, Date endDate, int page, int pageSize) throws Exception{
		
		List<PfpAdKeywordViewVO> adKeywordViewVOs = null;
		List<Object> pvclks = ((PfpAdKeywordDAO)dao).getAdKeywordPvclk(userAccount, searchAdStatus, adType, keyword, adGroupSeq, adKeywordPvclkDevice, dateType, startDate, endDate, page, pageSize);
		List<Object> adRanks = ((PfpAdKeywordDAO)dao).getAdRank(userAccount, searchAdStatus, adType, keyword, adGroupSeq, dateType, startDate, endDate);
		
		for(Object object:pvclks){
			
			Object[] ob = (Object[])object;
			
			if(ob[0] != null){
				
				if(adKeywordViewVOs == null){
					adKeywordViewVOs = new ArrayList<PfpAdKeywordViewVO>();
				}
				
				PfpAdKeywordViewVO adKeywordViewVO = new PfpAdKeywordViewVO();
				adKeywordViewVO.setAdActionSeq(ob[0].toString());
				adKeywordViewVO.setAdActionName(ob[1].toString());
				adKeywordViewVO.setAdActionMax(Float.parseFloat(ob[2].toString()));				
				adKeywordViewVO.setAdGroupSeq(ob[3].toString());		
				adKeywordViewVO.setAdGroupName(ob[4].toString());
				// 關鍵字狀態
				for(EnumAdStatus status:EnumAdStatus.values()){
					int adGroupStatus = Integer.parseInt(ob[5].toString());
					
					if(status.getStatusId() == adGroupStatus){
						adKeywordViewVO.setAdGroupStatus(adGroupStatus);
						adKeywordViewVO.setAdGroupStatusDesc(status.getStatusRemark());
					}
				}	

				adKeywordViewVO.setAdKeywordSeq(ob[6].toString());
				adKeywordViewVO.setAdKeyword(ob[7].toString());
				// 關鍵字狀態
				for(EnumAdStatus status:EnumAdStatus.values()){
					int adStatus = Integer.parseInt(ob[8].toString());
					
					if(status.getStatusId() == adStatus){
						adKeywordViewVO.setAdKeywordStatus(adStatus);
						adKeywordViewVO.setAdKeywordStatusDesc(status.getStatusRemark());
					}
				}	
				
				float searchPrice = Float.parseFloat(ob[9].toString());
				float channelPrice = Float.parseFloat(ob[10].toString());
				adKeywordViewVO.setAdKeywordSearchPrice(searchPrice);
				adKeywordViewVO.setAdKeywordChannelPrice(channelPrice);
								
				// 平均排名
				adKeywordViewVO.setAdKeywordRankAvg(0);
				
				for(Object adRank:adRanks){
					
					Object[] rank = (Object[])adRank;
					
					if(rank[0] != null && adKeywordViewVO.getAdKeywordSeq().equals(rank[0].toString())){
						adKeywordViewVO.setAdKeywordRankAvg(Float.parseFloat(rank[1].toString()));
					}
				}
				
				if(ob[11] != null) {
					adKeywordViewVO.setAdKeywordPvclkDevice(ob[11].toString());
				}
					
				
				// 求點閱率
				int pv = Integer.parseInt(ob[12].toString());
				int clk = Integer.parseInt(ob[13].toString());
				float clkPrice = Float.parseFloat(ob[14].toString());
				adKeywordViewVO.setAdKeywordPv(pv);
				adKeywordViewVO.setAdKeywordClk(clk);
				adKeywordViewVO.setAdKeywordClkPrice(clkPrice);
								
				float clkRate = 0;
				float clkPriceAvg = 0;			
				
				if(clk > 0 || pv > 0){
					clkRate = (float)clk / (float)pv*100;
					
				}
				
				if(clkPrice > 0 || clk > 0){
					clkPriceAvg = clkPrice / (float)clk;
				}
				
				adKeywordViewVO.setAdKeywordClkRate(clkRate);
				adKeywordViewVO.setAdKeywordClkPriceAvg(clkPriceAvg);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
				adKeywordViewVO.setAdKeywordCreateTime(sdf.format(ob[15]));
				adKeywordViewVO.setMemberId(ob[16].toString());
				adKeywordViewVO.setCustomerInfoTitle(ob[17].toString());			
				
				//float suggestPrice = syspriceOperaterAPI.getKeywordSuggesPrice(adKeywordViewVO.getAdKeyword());
				//adKeywordViewVO.setSuggestPrice(suggestPrice);

				// 無效點擊
				int invalidClk = Integer.parseInt(ob[18].toString());
				float invalidClkPrice = Float.parseFloat(ob[19].toString());
				adKeywordViewVO.setAdKeywordInvalidClk(invalidClk);
				adKeywordViewVO.setAdKeywordInvalidClkPrice(invalidClkPrice);
							
				adKeywordViewVOs.add(adKeywordViewVO);
			}
		}
		
		return adKeywordViewVOs;
	}

	public List<Object> getAllAdKeywordView(String userAccount, String searchAdStatus, int adType, String keyword, String adGroupSeq, String adKeywordPvclkDevice, String dateType, Date startDate, Date endDate) throws Exception{
		return ((PfpAdKeywordDAO)dao).getAdKeywordPvclk(userAccount, searchAdStatus, adType, keyword, adGroupSeq, adKeywordPvclkDevice, dateType, startDate, endDate, -1, -1);
	}
	
	public List<PfpAdKeyword> validAdKeyword(String adGroupSeq) throws Exception{
		return ((PfpAdKeywordDAO)dao).validAdKeyword(adGroupSeq);
	}
	
}
