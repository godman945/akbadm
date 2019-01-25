package com.pchome.akbadm.db.service.ad;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.ad.IPfpAdActionDAO;
import com.pchome.akbadm.db.dao.ad.IPfpAdGroupDAO;
//import com.pchome.akbadm.api.SyspriceOperaterAPI;
import com.pchome.akbadm.db.dao.ad.PfpAdGroupDAO;
import com.pchome.akbadm.db.pojo.PfpAdGroup;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.ad.PfpAdActionViewVO;
import com.pchome.akbadm.db.vo.ad.PfpAdGroupViewVO;
import com.pchome.enumerate.ad.EnumAdStatus;
import com.pchome.enumerate.ad.EnumAdType;

public class PfpAdGroupService extends BaseService<PfpAdGroup,String> implements IPfpAdGroupService{

	
	public List<PfpAdGroup> getAllPfpAdGroups() throws Exception{
		return ((PfpAdGroupDAO)dao).loadAll();
	}
	
	public List<Object> findAdGroupView(String adActionSeq, String adType, String adGroupName, String startDate, String endDate, int page, int pageSize, String customerInfoId) throws Exception{
		return findAdGroupView(adActionSeq, adType, null, adGroupName, null, startDate, endDate, page, pageSize, customerInfoId);
		
	}
	
	public List<Object> findAdGroupView(String adActionSeq, String adType, String adGroupSeq, String adGroupName, String adGroupStatus, String startDate, String endDate, int page, int pageSize, String customerInfoId) throws Exception{
		return ((PfpAdGroupDAO)dao).findAdGroupView(adActionSeq, adType, adGroupSeq, adGroupName, adGroupStatus, startDate, endDate, page, pageSize, customerInfoId);
	}

	public String getCount(String adActionSeq, String adType, String adGroupSeq, String adGroupName, String adGroupStatus, String startDate, String endDate, int page, int pageSize, String customerInfoId) throws Exception {
		return ((PfpAdGroupDAO)dao).getCount(adActionSeq, adType, adGroupSeq, adGroupName, adGroupStatus, startDate, endDate, page, pageSize, customerInfoId);
	}

	public List<PfpAdGroup> getPfpAdGroups(String adGroupSeq, String adActionSeq, String adGroupName, String adGroupSearchPrice, String adGroupChannelPrice, String adGroupStatus) throws Exception{
		return ((PfpAdGroupDAO)dao).getPfpAdGroups(adGroupSeq, adActionSeq, adGroupName, adGroupSearchPrice, adGroupChannelPrice, adGroupStatus);
	}

	public boolean chkAdGroupNameByAdActionSeq(String adGroupName, String adGroupSeq, String adActionSeq) throws Exception {
		return ((PfpAdGroupDAO)dao).chkAdGroupNameByAdActionSeq(adGroupName, adGroupSeq, adActionSeq);
	}
	
	public PfpAdGroup getPfpAdGroupBySeq(String adGroupSeq) throws Exception {

		return ((PfpAdGroupDAO)dao).getPfpAdGroupBySeq(adGroupSeq);
	}

	public void insertPfpAdGroup(PfpAdGroup pfpAdGroup) throws Exception {
		((PfpAdGroupDAO)dao).insertPfpAdGroup(pfpAdGroup);
	}

	public void updatePfpAdGroup(PfpAdGroup pfpAdGroup) throws Exception {
		((PfpAdGroupDAO)dao).updatePfpAdGroup(pfpAdGroup);
	}

	public void updatePfpAdGroupStatus(String pfpAdGroupStatus, String adGroupSeq) throws Exception {
		((PfpAdGroupDAO)dao).updatePfpAdGroupStatus(pfpAdGroupStatus, adGroupSeq);
	}

	public void savePfpAdGroup(PfpAdGroup adGroup) throws Exception {
		((PfpAdGroupDAO)dao).saveOrUpdatePfpAdGroup(adGroup);
	}
	
	public void saveOrUpdateWithCommit(PfpAdGroup adGroup) throws Exception{
		((PfpAdGroupDAO)dao).saveOrUpdateWithCommit(adGroup);
	}
	public List<PfpAdGroupViewVO> getAdGroupReport(Map<String,String> adGroupConditionMap) throws Exception{
	    List<Object> adGroupReportObjList = ((PfpAdGroupDAO) dao).getAdGroupReport(adGroupConditionMap);
	    Double adInvalidClkPrice;
	    Double adPvPrice;
	    float clkRate = 0;
	    float adClkPriceAvg = 0;
	    List<PfpAdGroupViewVO> adGroupViewList = new ArrayList<PfpAdGroupViewVO>();
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    DateFormat chtDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date adCreateDate = null;
	    for (int i = 0; i < adGroupReportObjList.size(); i++) {
		Object[] obj = (Object[]) adGroupReportObjList.get(i);
		PfpAdGroupViewVO pfpAdGroupViewVO = new PfpAdGroupViewVO();
		//廣告群組建立時間
		obj[0] = obj[0].toString().replace(".0", "");
		adCreateDate = dateFormat.parse(obj[0].toString());
		pfpAdGroupViewVO.setAdGroupCreatTime(chtDateFormat.format(adCreateDate).toString());
		pfpAdGroupViewVO.setMemberId(obj[1].toString());
		//帳戶
		pfpAdGroupViewVO.setCustomerInfoTitle(obj[2].toString());
		//群組名稱
		pfpAdGroupViewVO.setAdGroupName(obj[3].toString());
		//裝置
		if(StringUtils.isEmpty(adGroupConditionMap.get("adPvclkDevice"))){
		    pfpAdGroupViewVO.setAdPvclkDevice("ALL Device");
		}
		if(adGroupConditionMap.get("adPvclkDevice")!= null && adGroupConditionMap.get("adPvclkDevice").trim().length()>0){
		    pfpAdGroupViewVO.setAdPvclkDevice(adGroupConditionMap.get("adPvclkDevice"));
		}
		//廣告群組,廣告狀態
		for(EnumAdStatus adActionStatus:EnumAdStatus.values()){
		    if(adActionStatus.getStatusId()== Integer.valueOf(obj[14].toString())){
			pfpAdGroupViewVO.setAdActionStatusDesc(adActionStatus.getStatusDesc());
		    }
		    if(adActionStatus.getStatusId()== Integer.valueOf(obj[5].toString())){
			pfpAdGroupViewVO.setAdGroupStatusDesc(adActionStatus.getStatusDesc());
		    }
		}
		pfpAdGroupViewVO.setAdGroupSearchPrice(Float.parseFloat(obj[6].toString()));
		pfpAdGroupViewVO.setAdGroupChannelPrice(Float.parseFloat(obj[7].toString()));
		//曝光數
		pfpAdGroupViewVO.setAdPv(Integer.valueOf(obj[8].toString()));
		//點擊數
		pfpAdGroupViewVO.setAdClk(Integer.valueOf(obj[9].toString()));
		//計算廣告點擊率
		if(!StringUtils.isEmpty(obj[9].toString()) && Integer.valueOf(obj[9].toString()) > 0){
		    clkRate = (float)Integer.valueOf(obj[9].toString()) / (float) Integer.valueOf(obj[8].toString()) * 100;
		    pfpAdGroupViewVO.setAdClkRate(clkRate);
		}
		//費用
		pfpAdGroupViewVO.setAdClkPrice(Float.parseFloat(obj[10].toString()));
		//計算平均點選費用
		adPvPrice = Double.parseDouble(obj[10].toString());
		if(adPvPrice > 0){
		    adClkPriceAvg =(float) (Double.parseDouble(obj[10].toString()) / Integer.valueOf(obj[9].toString()));
		    pfpAdGroupViewVO.setAdClkPriceAvg(adClkPriceAvg);
		}
		//廣告曝光數總費用
		pfpAdGroupViewVO.setAdPvPrice(Float.parseFloat(obj[10].toString()));
		//無效點擊數
		pfpAdGroupViewVO.setAdInvalidClk(Integer.valueOf(obj[11].toString()));
		//無效點擊費用
		adInvalidClkPrice = Double.parseDouble(obj[12].toString());
		pfpAdGroupViewVO.setAdInvalidClkPrice(adInvalidClkPrice.intValue());
		pfpAdGroupViewVO.setAdGroupSearchPriceType(Integer.valueOf(obj[15].toString()));
		pfpAdGroupViewVO.setAdGroupSeq(obj[13].toString());
		pfpAdGroupViewVO.setAdActionName(obj[16].toString());
		adGroupViewList.add(pfpAdGroupViewVO);
	    }
	    return adGroupViewList;
	}
	    
	public int getAdGroupReportSize(Map<String,String> adGroupConditionMap) throws Exception{
	    return  ((PfpAdGroupDAO)dao).getAdGroupReportSize(adGroupConditionMap);
	}

	public List<PfpAdGroupViewVO> getAdGroupView(String userAccount, String adStatus, int adType, String keyword, String adActionSeq, String adPvclkDevice, String dateType, Date startDate, Date endDate, int page, int pageSize) throws Exception{
		
		List<PfpAdGroupViewVO> adGroupViewVOs = null;
		List<Object> objects = ((PfpAdGroupDAO)dao).getAdGroupPvclk(userAccount, adStatus, adType, keyword, adActionSeq, adPvclkDevice, dateType, startDate, endDate, page, pageSize);
		
		for(Object object:objects){
			
			Object[] ob = (Object[])object;
			
			if(ob[0] != null){
				
				if(adGroupViewVOs == null){
					adGroupViewVOs = new ArrayList<PfpAdGroupViewVO>();
				}
				
				PfpAdGroupViewVO adGroupViewVO = new PfpAdGroupViewVO();
				adGroupViewVO.setAdActionSeq(ob[0].toString());
				adGroupViewVO.setAdActionName(ob[1].toString());

				// 廣告類型
				for(EnumAdType type:EnumAdType.values()){
					int ad_type = Integer.parseInt(ob[2].toString());					
					if(type.getType() == ad_type){
						adGroupViewVO.setAdType(type.getChName());
					}					
				}

				// 廣告狀態
				for(EnumAdStatus status:EnumAdStatus.values()){
					int ad_action_status = Integer.parseInt(ob[3].toString());
					if(status.getStatusId() == ad_action_status){
						adGroupViewVO.setAdActionStatus(ad_action_status);
						adGroupViewVO.setAdActionStatusDesc(status.getStatusDesc());
					}
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
				adGroupViewVO.setMemberId(ob[4].toString());			
				adGroupViewVO.setCustomerInfoTitle(ob[5].toString());

				adGroupViewVO.setAdGroupSeq(ob[6].toString());
				adGroupViewVO.setAdGroupName(ob[7].toString());

				// 分類狀態
				for(EnumAdStatus status:EnumAdStatus.values()){
					int ad_group_status = Integer.parseInt(ob[8].toString());
					
					if(status.getStatusId() == ad_group_status){
						adGroupViewVO.setAdGroupStatus(ad_group_status);
						adGroupViewVO.setAdGroupStatusDesc(status.getStatusDesc());
					}
				}
				
				adGroupViewVO.setAdGroupSearchPriceType(Integer.parseInt(ob[9].toString()));
				
				float searchPrice = Float.parseFloat(ob[10].toString());
				float channelPrice = Float.parseFloat(ob[11].toString());
				adGroupViewVO.setAdGroupSearchPrice(searchPrice);
				adGroupViewVO.setAdGroupChannelPrice(channelPrice);
				adGroupViewVO.setAdGroupCreatTime(sdf.format(ob[12]));
				if(ob[13] != null) {
					adGroupViewVO.setAdPvclkDevice(ob[13].toString());
				}
				
				// 求點閱率
				int pv = Integer.parseInt(ob[14].toString());
				int clk = Integer.parseInt(ob[15].toString());
				float clkPrice = Float.parseFloat(ob[16].toString());
				adGroupViewVO.setAdPv(pv);
				adGroupViewVO.setAdClk(clk);
				adGroupViewVO.setAdClkPrice(clkPrice);
				
				
				float clkRate = 0;
				float clkPriceAvg = 0;			
				
				if(clk > 0 || pv > 0){
					clkRate = (float)clk / (float)pv*100;
					
				}
				
				if(clkPrice > 0 || clk > 0){
					clkPriceAvg = clkPrice / (float)clk;
				}
				
				adGroupViewVO.setAdClkRate(clkRate);
				adGroupViewVO.setAdClkPriceAvg(clkPriceAvg);
				
//				// 求播出率
//				float adAsideRate = syspriceOperaterAPI.getAdAsideRate(channelPrice);
//				adGroupViewVO.setAdAsideRate(adAsideRate);

				// 無效點擊
				int invalidClk = Integer.parseInt(ob[17].toString());
				float invalidClkPrice = Float.parseFloat(ob[18].toString());
				adGroupViewVO.setAdInvalidClk(invalidClk);
				adGroupViewVO.setAdInvalidClkPrice(invalidClkPrice);
				
				adGroupViewVOs.add(adGroupViewVO);
			}
		}
		
		return adGroupViewVOs;		
	}
	
	public List<Object> getAllAdGroupView(String userAccount, String adStatus, int adType, String keyword, String adActionSeq, String adPvclkDevice, String dateType, Date startDate, Date endDate) throws Exception{
		return ((PfpAdGroupDAO)dao).getAdGroupPvclk(userAccount, adStatus, adType, keyword, adActionSeq, adPvclkDevice, dateType, startDate, endDate, -1, -1);
	}
	
	public List<PfpAdGroup> validAdGroup(String adActionSeq) throws Exception{
		return ((PfpAdGroupDAO)dao).validAdGroup(adActionSeq);
	}
	
	public List<PfpAdGroup> findAdGroup(EnumAdStatus enumAdStatus){
		return ((IPfpAdGroupDAO)dao).findAdGroup(enumAdStatus.getStatusId());
	}
}
