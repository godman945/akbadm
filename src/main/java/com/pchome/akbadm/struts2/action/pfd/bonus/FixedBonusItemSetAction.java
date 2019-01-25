package com.pchome.akbadm.struts2.action.pfd.bonus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.bean.bonus.BonusBean;
import com.pchome.akbadm.config.session.SessionConstants;
import com.pchome.akbadm.db.pojo.PfdBonusItemSet;
import com.pchome.akbadm.db.pojo.PfdContract;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.akbadm.db.service.contract.IPfdContractService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusItemSetService;
import com.pchome.akbadm.factory.pfd.parse.APfdParseBonusXML;
import com.pchome.akbadm.factory.pfd.parse.PfdParseFactory;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.enumerate.contract.EnumContractStatus;
import com.pchome.enumerate.pfd.bonus.EnumPfdBonusItem;
import com.pchome.enumerate.pfd.bonus.EnumPfdBonusType;
import com.pchome.enumerate.pfd.bonus.EnumPfdXmlParse;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;

public class FixedBonusItemSetAction extends BaseAction{

	private IPfdBonusItemSetService pfdBonusItemSetService;
	private IPfdContractService pfdContractService;
	private IAdmAccesslogService admAccesslogService;
	private PfdParseFactory pfdParseFactory;
	private String parsePath;								// 讀檔案路徑
	private String bonusStartDate;							// 獎金計算開始日期
	private String bonusEndDate;							// 獎金計算結束日期
	
	private String pfdContractId;							// 合約編號	
	private PfdContract pfdContract;						// 經銷商合約
	
	private List<Map<String, Object>> bonusFixedFileSet;	// 達成獎金計算項目
	private List<PfdBonusItemSet> bonusFixedDbSet;			// 資料庫裡達成獎金的設定
	private String selFixedBonusSet;						// 挑選的達成獎金計算方式
	
	
	private EnumPfdBonusItem[] enumPfdBonusItem = EnumPfdBonusItem.values();
	private EnumContractStatus[] enumContractStatus = EnumContractStatus.values();
	
	private String closeFlag;
	
	private Map<String,String> bonusFixedFileMap;

	public String fixedBonusSetAction(){
		
		log.info(" pfdContractId: "+pfdContractId);
		log.info(" bonusStartDate: "+bonusStartDate);
		log.info(" bonusEndDate: "+bonusEndDate);
		
		pfdContract = pfdContractService.findPfdContract(pfdContractId);
		
		loadXMLFile();
		
		return SUCCESS;
	}
	
	public String updateFixedBonusSetAction(){
		
		log.info(" selFixedBonusSet: "+selFixedBonusSet);
		log.info(" pfdContractId: "+pfdContractId);
		
		loadXMLFile();
		setAccessLogMap();
		
		// 先刪掉獎金設定
		pfdBonusItemSetService.deletePfdBonusItemSet(pfdContractId, EnumPfdBonusType.BONUS_FIXED);
		
		
		if(!selFixedBonusSet.isEmpty()){
			
			String fixedBonusSet[] = selFixedBonusSet.split(",");
			
			PfdContract pfdContract = pfdContractService.findPfdContract(pfdContractId);
			
			if(pfdContract != null){
				
				Date today = new Date();
							
				for(int i=0;i<fixedBonusSet.length;i++){

					String[] fixedSet = fixedBonusSet[i].split("-");
					
					log.info(" fixedSet: "+fixedSet);
					
					for(EnumPfdBonusItem item:EnumPfdBonusItem.values()){
						
						// 新增獎金設定
						if(fixedSet[0].equals(item.getItemType())){
							
							PfdBonusItemSet itemSet = new PfdBonusItemSet();
							
							itemSet.setBonusItem(item.getItemType());
							itemSet.setBonusType(item.getPfdBonusType().getType());
							itemSet.setPfdContract(pfdContract);
							itemSet.setSubItemId(Integer.parseInt(fixedSet[1]));							
							itemSet.setCreateDate(today);
							itemSet.setUpdateDate(today);
							pfdBonusItemSetService.saveOrUpdate(itemSet);
							
							//access log
							if(bonusFixedFileMap.get(item.getItemType() + "_" + item.getPfdBonusType().getType() + "_" + Integer.parseInt(fixedSet[1])) != null){
								String logMsg = bonusFixedFileMap.get(item.getItemType() + "_" + item.getPfdBonusType().getType() + "_" + Integer.parseInt(fixedSet[1]));
								admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.PFD, EnumAccesslogAction.ACCOUNT_MODIFY, logMsg, 
										super.getSession().get(SessionConstants.SESSION_USER_ID).toString(), null, pfdContract.getPfdCustomerInfo().getCustomerInfoId(), 
										null, request.getRemoteAddr(), EnumAccesslogEmailStatus.NO);
							}
							
							break;
						}

					}
				}
			}
			
		}
		
		return SUCCESS;
	}
	
	/**
	 * 讀取 奬金設定條件
	 */
	private void loadXMLFile() {
		
		bonusFixedFileSet = new ArrayList<Map<String, Object>>();
		closeFlag = "N";
		
		// 達成獎金設定
		bonusFixedDbSet = pfdBonusItemSetService.findPfdBonusItemSets(pfdContractId, EnumPfdBonusType.BONUS_FIXED);
		
		for(EnumPfdBonusItem bonusItem:EnumPfdBonusItem.values()){
			
			if(bonusItem.getPfdBonusType().equals(EnumPfdBonusType.BONUS_FIXED)){
				
				// 讀取 XML 檔案
				StringBuffer filePath = new StringBuffer();
				filePath.append(this.parsePath);
				filePath.append(bonusItem.getFileName());
				
				APfdParseBonusXML bonusFixedXML = null;
				
				if(bonusItem.getItemType().equals(EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType())){
					bonusFixedXML = pfdParseFactory.getPfdParseBonusXML(EnumPfdXmlParse.PARSE_MONTH_BONUS_XML);
				}else{
					bonusFixedXML = pfdParseFactory.getPfdParseBonusXML(EnumPfdXmlParse.PARSE_ALL_XML);
				}
				
				String titleName = "";
				if(bonusItem.getItemType().equals(EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType()) || 
						bonusItem.getItemType().equals(EnumPfdBonusItem.EVERY_QUARTER_BONUS.getItemType()) ||
						bonusItem.getItemType().equals(EnumPfdBonusItem.EVERY_YEAR_BONUS.getItemType())){
					titleName = bonusItem.getItemChName() + "比率";
				} else {
					titleName = bonusItem.getItemChName();
				}
				
				
				
				List<Object> bonusBean = bonusFixedXML.parseXML(filePath.toString(), 0);
				
				Map<String, Object> bonusFileSetMap = new HashMap<String, Object>();
				bonusFileSetMap.put("title", titleName);
				bonusFileSetMap.put("startDate", this.bonusStartDate);
				bonusFileSetMap.put("endDate", this.bonusEndDate);
				bonusFileSetMap.put("bonusItemId", bonusItem.getItemType());	
				bonusFileSetMap.put("bonusType", bonusItem.getPfdBonusType().getType());	
				bonusFileSetMap.put("data", bonusBean);				
				
				bonusFixedFileSet.add(bonusFileSetMap);
				
			}
		}
		
		// 和 DB 設定比對 
		if(!bonusFixedDbSet.isEmpty()){
			closeFlag = "Y";
			for(PfdBonusItemSet dbSet:bonusFixedDbSet){
				
				for(Map<String, Object> fixedSet:bonusFixedFileSet){
					
					if(fixedSet.get("bonusItemId").equals(dbSet.getBonusItem())){
						fixedSet.put("checked", "checked");						
					}	
					
				}					
							
			}
		}
	
		log.info(" bonusFixedFileSet: "+bonusFixedFileSet);
		
	}
	
	private void setAccessLogMap(){
		DecimalFormat df1 = new DecimalFormat("###,###,###,###");
		DecimalFormat df2 = new DecimalFormat("###,###,###,###.##");
		bonusFixedFileMap = new LinkedHashMap<String,String>();
		
		for(Object object:bonusFixedFileSet){
			Map<String, Object> bonusFileSetMap = new HashMap<String, Object>();
			bonusFileSetMap = (HashMap<String, Object>) object;
			
			String itemType = bonusFileSetMap.get("bonusItemId").toString();
			String bonusType = bonusFileSetMap.get("bonusType").toString();
			List<Object> bonusBean = (List<Object>) bonusFileSetMap.get("data");
			
			if(itemType.equals(EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType())){
				int i = 1;
				for(Object object2:bonusBean){
					BonusBean data = (BonusBean) object2;
					float bonus = data.getBonus()*100;
					String note = "佣金設定-->固定獎金/" + EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemChName() + "比率/" + df2.format(bonus) + "%";
					bonusFixedFileMap.put(itemType + "_" + bonusType + "_" + String.valueOf(i), note);
					i++;
				}
			}
			
			if(itemType.equals(EnumPfdBonusItem.EVERY_QUARTER_BONUS.getItemType())){
				
				int quarterSubItemid = 1;
				for(Object object2:bonusBean){
					String note = "佣金設定-->固定獎金/" + EnumPfdBonusItem.EVERY_QUARTER_BONUS.getItemChName() + "比率/";
					List<Object> list = (ArrayList<Object>) object2;
					for(Object object3:list){
						BonusBean data = (BonusBean) object3;
						float bonus = data.getBonus()*100;
						note += "累計廣告費用 $" + df1.format(data.getMin()) + "~" + df1.format(data.getMax()) + "，獎金比率" + df2.format(bonus) + "%；";
					}
					
					bonusFixedFileMap.put(itemType + "_" + bonusType + "_" + String.valueOf(quarterSubItemid), note);
					quarterSubItemid++;
				}
			}
			
			if(itemType.equals(EnumPfdBonusItem.EVERY_YEAR_BONUS.getItemType())){
				
				int yearSubItemid = 1;
				for(Object object2:bonusBean){
					String note = "佣金設定-->固定獎金/" + EnumPfdBonusItem.EVERY_YEAR_BONUS.getItemChName() + "比率/";
					List<Object> list = (ArrayList<Object>) object2;
					for(Object object3:list){
						BonusBean data = (BonusBean) object3;
						float bonus = data.getBonus()*100;
						note += "累計廣告費用 $" + df1.format(data.getMin()) + "~" + df1.format(data.getMax()) + "，獎金比率" + df2.format(bonus) + "%；";
					}
					
					bonusFixedFileMap.put(itemType + "_" + bonusType + "_" + String.valueOf(yearSubItemid), note);
					yearSubItemid++;
				}
			}
			
		}
		
		/*for(String key:bonusFixedFileMap.keySet()){
			log.info(" ---------------->   " + key + " : "+ bonusFixedFileMap.get(key));
		}*/
	}
	
	public void setPfdBonusItemSetService(
			IPfdBonusItemSetService pfdBonusItemSetService) {
		this.pfdBonusItemSetService = pfdBonusItemSetService;
	}

	public void setPfdContractService(IPfdContractService pfdContractService) {
		this.pfdContractService = pfdContractService;
	}

	public void setAdmAccesslogService(IAdmAccesslogService admAccesslogService) {
		this.admAccesslogService = admAccesslogService;
	}

	public void setPfdContractId(String pfdContractId) {
		this.pfdContractId = pfdContractId;
	}

	public PfdContract getPfdContract() {
		return pfdContract;
	}

	public List<Map<String, Object>> getBonusFixedFileSet() {
		return bonusFixedFileSet;
	}

	public EnumContractStatus[] getEnumContractStatus() {
		return enumContractStatus;
	}
	
	public void setParsePath(String parsePath) {
		this.parsePath = parsePath;
	}

	public void setPfdParseFactory(PfdParseFactory pfdParseFactory) {
		this.pfdParseFactory = pfdParseFactory;
	}

	public void setBonusStartDate(String bonusStartDate) {
		this.bonusStartDate = bonusStartDate;
	}

	public void setBonusEndDate(String bonusEndDate) {
		this.bonusEndDate = bonusEndDate;
	}

	public EnumPfdBonusItem[] getEnumPfdBonusItem() {
		return enumPfdBonusItem;
	}

	public List<PfdBonusItemSet> getBonusFixedDbSet() {
		return bonusFixedDbSet;
	}

	public void setSelFixedBonusSet(String selFixedBonusSet) {
		this.selFixedBonusSet = selFixedBonusSet;
	}

	public String getCloseFlag() {
		return closeFlag;
	}
	
}
