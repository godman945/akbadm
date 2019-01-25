package com.pchome.akbadm.struts2.action.pfd.bonus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

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

public class CaseBonusItemSetAction extends BaseAction{

	private IPfdBonusItemSetService pfdBonusItemSetService;
	private IPfdContractService pfdContractService;
	private PfdParseFactory pfdParseFactory;
	private IAdmAccesslogService admAccesslogService;
	private String parsePath;								// 讀檔案路徑
	private String bonusStartDate;							// 獎金計算開始日期
	private String bonusEndDate;							// 獎金計算結束日期
	
	private String pfdContractId;							// 合約編號	
	private PfdContract pfdContract;						// 經銷商合約

	private List<Map<String, Object>> bonusCaseFileSet;		// 專案獎金計算項目
	private List<PfdBonusItemSet> bonusCaseDbSet;			// 資料庫裡專案獎金的設定
	private String selCaseBonusSet;							// 挑選的專案獎金計算方式
	
	private EnumPfdBonusItem[] enumPfdBonusItem = EnumPfdBonusItem.values();
	private EnumContractStatus[] enumContractStatus = EnumContractStatus.values();
	
	private Map<String,String> bonusCaseFileMap;

	public String caseBonusSetAction(){
		
		log.info(" pfdContractId: "+pfdContractId);
		log.info(" bonusStartDate: "+bonusStartDate);
		log.info(" bonusEndDate: "+bonusEndDate);
		
		pfdContract = pfdContractService.findPfdContract(pfdContractId);
		
		loadXMLFile();
		
		return SUCCESS;
	}
	
	public String updateCaseBonusSetAction(){
		
		//log.info(" selBonusCaseSet: "+selBonusCaseSet);
		log.info(" pfdContractId: "+pfdContractId);
		
		loadXMLFile();
		setAccessLogMap();
		
		// 先刪掉獎金設定
		pfdBonusItemSetService.deletePfdBonusItemSet(pfdContractId, EnumPfdBonusType.BONUS_CASE);
		
		pfdBonusItemSetService.deletePfdBonusItemSet(pfdContractId, EnumPfdBonusType.BONUS_DEVELOP);

		if(!selCaseBonusSet.isEmpty()){
			
			String caseBonusSet[] = selCaseBonusSet.split(",");
			
			Date today = new Date();
			
			PfdContract pfdContract = pfdContractService.findPfdContract(pfdContractId);
			
			if(pfdContract != null){
				
				for(int i=0;i<caseBonusSet.length;i++){

					String[] caseSet = caseBonusSet[i].split("-");
					
					for(EnumPfdBonusItem item:EnumPfdBonusItem.values()){
						
						// 新增獎金設定
						if(caseSet[0].equals(item.getItemType())){
							
							PfdBonusItemSet itemSet = new PfdBonusItemSet();
							
							itemSet.setBonusItem(item.getItemType());
							itemSet.setBonusType(item.getPfdBonusType().getType());
							itemSet.setPfdContract(pfdContract);
							itemSet.setSubItemId(Integer.parseInt(caseSet[1]));							
							itemSet.setCreateDate(today);
							itemSet.setUpdateDate(today);
							pfdBonusItemSetService.saveOrUpdate(itemSet);
							
							//access log
							if(bonusCaseFileMap.get(item.getItemType() + "_" + item.getPfdBonusType().getType() + "_" + Integer.parseInt(caseSet[1])) != null){
								String logMsg = bonusCaseFileMap.get(item.getItemType() + "_" + item.getPfdBonusType().getType() + "_" + Integer.parseInt(caseSet[1]));
								admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.PFD, EnumAccesslogAction.ACCOUNT_MODIFY, logMsg, 
										super.getSession().get(SessionConstants.SESSION_USER_ID).toString(), null, pfdContract.getPfdCustomerInfo().getCustomerInfoId(), 
										null, request.getRemoteAddr(), EnumAccesslogEmailStatus.NO);
							}
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
		
		bonusCaseFileSet = new ArrayList<Map<String, Object>>();
		
		
		for(EnumPfdBonusItem bonusItem:EnumPfdBonusItem.values()){
			
			if(bonusItem.getPfdBonusType().equals(EnumPfdBonusType.BONUS_CASE) ||
					bonusItem.getPfdBonusType().equals(EnumPfdBonusType.BONUS_DEVELOP)){
				
				// 讀取 XML 檔案
				StringBuffer filePath = new StringBuffer();
				filePath.append(this.parsePath);
				filePath.append(bonusItem.getFileName());
				
				APfdParseBonusXML bonusFixedXML = null;
				
				bonusFixedXML = pfdParseFactory.getPfdParseBonusXML(EnumPfdXmlParse.PARSE_ALL_XML);
				
				List<Object> bonusBean = bonusFixedXML.parseXML(filePath.toString(), 0);
				
				Map<String, Object> bonusFileSetMap = new HashMap<String, Object>();
				bonusFileSetMap.put("title", bonusItem.getItemChName());
				bonusFileSetMap.put("startDate", this.bonusStartDate);
				bonusFileSetMap.put("endDate", this.bonusEndDate);
				bonusFileSetMap.put("bonusItemId", bonusItem.getItemType());	
				bonusFileSetMap.put("bonusType", bonusItem.getPfdBonusType().getType());	
				bonusFileSetMap.put("data", bonusBean);				
				
				bonusCaseFileSet.add(bonusFileSetMap);
			}
		}
		
		this.getCaseBonusDbSet();
		
		log.info(" bonusCaseDbSet: "+bonusCaseDbSet);
		
		// 和 DB 設定比對 
		if(!bonusCaseDbSet.isEmpty()){
			
			for(PfdBonusItemSet dbSet:bonusCaseDbSet){
				
				for(Map<String, Object> fixedSet:bonusCaseFileSet){
					
					if(fixedSet.get("bonusItemId").equals(dbSet.getBonusItem())){
						fixedSet.put("checked", "checked");						
					}	
					
				}					
							
			}
		}
	
		log.info(" bonusCaseFileSet: "+bonusCaseFileSet);
		
	}
	
	private void setAccessLogMap(){
		DecimalFormat df1 = new DecimalFormat("###,###,###,###");
		DecimalFormat df2 = new DecimalFormat("###,###,###,###.##");
		bonusCaseFileMap = new LinkedHashMap<String,String>();
		
		for(Object object:bonusCaseFileSet){
			Map<String, Object> bonusFileSetMap = new HashMap<String, Object>();
			bonusFileSetMap = (HashMap<String, Object>) object;
			
			String itemType = bonusFileSetMap.get("bonusItemId").toString();
			String bonusType = bonusFileSetMap.get("bonusType").toString();
			List<Object> bonusBean = (List<Object>) bonusFileSetMap.get("data");
			
			String itemChName = "";
			for(EnumPfdBonusItem enumPfdBonusItem:EnumPfdBonusItem.values()){
				if(StringUtils.equals(itemType, enumPfdBonusItem.getItemType())){
					itemChName = enumPfdBonusItem.getItemChName();
					break;
				}
				
			}
			
			if(itemType.equals(EnumPfdBonusItem.EVERY_MONTH_DEVELOP_BONUS.getItemType()) || 
					itemType.equals(EnumPfdBonusItem.EVERY_QUARTER_DEVELOP_BONUS.getItemType()) ||
					itemType.equals(EnumPfdBonusItem.EVERY_YEAR_DEVELOP_BONUS.getItemType())){
				
				int monthDevelopSubItemid = 1;
				for(Object object2:bonusBean){
					
					String note = "佣金設定-->專案獎金/" + itemChName + "/";
					List<Object> list = (ArrayList<Object>) object2;
					for(Object object3:list){
						BonusBean data = (BonusBean) object3;
						float bonus = data.getBonus();
						note += "(" + df1.format(data.getMin()) + "間~" + df1.format(data.getMax()) + "間) $" + df1.format(bonus) + "；";
					}
					
					bonusCaseFileMap.put(itemType + "_" + bonusType + "_" + String.valueOf(monthDevelopSubItemid), note);
					monthDevelopSubItemid++;
				}
			}
			
			if(itemType.equals(EnumPfdBonusItem.CASE_BONUS_YEAR_END_SALE_A.getItemType()) || 
					itemType.equals(EnumPfdBonusItem.CASE_BONUS_YEAR_END_SALE_B.getItemType()) ||
					itemType.equals(EnumPfdBonusItem.CASE_BONUS_YEAR_END_SALE_C.getItemType()) ||
					itemType.equals(EnumPfdBonusItem.CASE_BONUS_YEAR_END_SALE_D.getItemType()) ||
					itemType.equals(EnumPfdBonusItem.CASE_BONUS_YEAR_END_SALE_E.getItemType()) ||
					itemType.equals(EnumPfdBonusItem.CASE_BONUS_YEAR_END_SALE_F.getItemType())){
				
				int monthDevelopSubItemid = 1;
				for(Object object2:bonusBean){
					
					String note = "佣金設定-->專案獎金/" + itemChName + "/";
					List<Object> list = (ArrayList<Object>) object2;
					for(Object object3:list){
						BonusBean data = (BonusBean) object3;
						float bonus = data.getBonus()*100;
						note += "$" + df1.format(data.getMin()) + "~$" + df1.format(data.getMax()) + "(" + df2.format(bonus) + "%);";
					}
					
					bonusCaseFileMap.put(itemType + "_" + bonusType + "_" + String.valueOf(monthDevelopSubItemid), note);
					monthDevelopSubItemid++;
				}
			}
			
		}
		
		/*for(String key:bonusCaseFileMap.keySet()){
			log.info(" ---------------->   " + key + " : "+ bonusCaseFileMap.get(key));
		}*/
	}
	
	private void getCaseBonusDbSet(){
		
		// 判斷是新增還是編輯
		List<PfdBonusItemSet> bonusCase = pfdBonusItemSetService.findPfdBonusItemSets(pfdContractId, EnumPfdBonusType.BONUS_CASE);
		
		List<PfdBonusItemSet> bonusDevelop = pfdBonusItemSetService.findPfdBonusItemSets(pfdContractId, EnumPfdBonusType.BONUS_DEVELOP);
		
		bonusCaseDbSet = new ArrayList<PfdBonusItemSet>();
		
		for(PfdBonusItemSet set:bonusCase){
			
			bonusCaseDbSet.add(set);
		}
		
		for(PfdBonusItemSet set:bonusDevelop){

			bonusCaseDbSet.add(set);
		}
	}
	
	public void setPfdBonusItemSetService(
			IPfdBonusItemSetService pfdBonusItemSetService) {
		this.pfdBonusItemSetService = pfdBonusItemSetService;
	}

	public void setPfdContractService(IPfdContractService pfdContractService) {
		this.pfdContractService = pfdContractService;
	}

	public void setPfdContractId(String pfdContractId) {
		this.pfdContractId = pfdContractId;
	}

	public PfdContract getPfdContract() {
		return pfdContract;
	}

	public List<Map<String, Object>> getBonusCaseFileSet() {
		return bonusCaseFileSet;
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

	public List<PfdBonusItemSet> getBonusCaseDbSet() {
		return bonusCaseDbSet;
	}

	public void setSelCaseBonusSet(String selCaseBonusSet) {
		this.selCaseBonusSet = selCaseBonusSet;
	}

	public void setAdmAccesslogService(IAdmAccesslogService admAccesslogService) {
		this.admAccesslogService = admAccesslogService;
	}
	
}
