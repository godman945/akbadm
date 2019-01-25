package com.pchome.akbadm.struts2.action.enterprise;

import java.io.*;
import java.util.*;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.config.session.SessionConstants;
import com.pchome.akbadm.db.pojo.PfdApplyForBusiness;
import com.pchome.akbadm.db.pojo.PfpEnterprise;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.akbadm.db.service.applyFor.IPfdApplyForBusinessService;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.akbadm.utils.UnicodeReader;
import com.pchome.akbadm.db.service.enterprise.IPfpEnterpriseService;
import com.pchome.enumerate.applyFor.EnumPfdApplyForBusiness;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;

public class EnterpriseAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IPfpEnterpriseService enterpriseService;
	private IPfdApplyForBusinessService applyForBusinessService;
	private IAdmAccesslogService admAccesslogService;

	private File fileUpload;

	private String message = "";

	private Map<String, String> addMap = new HashMap<String, String>(); //新增清單
	private Map<String, String> removeMap = new HashMap<String, String>(); //移除清單
	private List<String> errorList = new ArrayList<String>(); //錯誤資料清單

	public String execute() throws Exception {
		return SUCCESS;
	}

	public String upload() throws Exception {

		if (fileUpload==null) {
    		this.message = "請選擇上傳檔案！";
    		return INPUT;
    	}

		FileInputStream fis = null;
		UnicodeReader ur = null;
		BufferedReader br = null;

		try {

			//新資料 map
			Map<String, String> newEnterpriseMap = new HashMap<String, String>();

			fis = new FileInputStream(fileUpload);
			ur = new UnicodeReader(fis, "utf-8");
			br = new BufferedReader(ur);

			String line;
			List<String> dataList = new ArrayList<String>();
			 while ((line = br.readLine()) != null) {
				 dataList.add(line);
			 }
			log.info(">>> dataList.size() = " + dataList.size());

			String data = "";
			String[] tmpArray = null;
			for (int i=0; i<dataList.size(); i++) {
				data = dataList.get(i);
				tmpArray = data.split(",");
				if (tmpArray.length != 2) {
					errorList.add(data);
					continue;
				} else {
					newEnterpriseMap.put(tmpArray[0], tmpArray[1]);
				}
			}
			log.info(">>> newEnterpriseMap.size() = " + newEnterpriseMap.size());

			//舊資料 map
			Map<String, String> oldEnterpriseMap = new HashMap<String, String>();

			List<PfpEnterprise> oldEnterpriseList = enterpriseService.findPfpEnterprise(new HashMap<String, String>());
			PfpEnterprise pojo = null;
			for (int i=0; i<oldEnterpriseList.size(); i++) {
				pojo = oldEnterpriseList.get(i);
				oldEnterpriseMap.put(pojo.getTaxId(), pojo.getCompanyName());
			}
			log.info(">>> oldEnterpriseMap.size() = " + oldEnterpriseMap.size());

			//比對出新增清單
			Iterator<String> it_new = newEnterpriseMap.keySet().iterator();
			String key_new;
			String value_new;
			while (it_new.hasNext()) {
				key_new = it_new.next();
				if (!oldEnterpriseMap.containsKey(key_new)) {
					value_new = newEnterpriseMap.get(key_new);
					addMap.put(key_new, value_new);
				}
			}
			log.info(">>> addMap.size() = " + addMap.size());

			//比對出移除清單
			Iterator<String> it_old = oldEnterpriseMap.keySet().iterator();
			String key_old;
			String value_old;
			while (it_old.hasNext()) {
				key_old = it_old.next();
				if (!newEnterpriseMap.containsKey(key_old)) {
					value_old = oldEnterpriseMap.get(key_old);
					removeMap.put(key_old, value_old);
				}
			}
			log.info(">>> removeMap.size() = " + removeMap.size());

			//處理新增清單
			Iterator<String> it_add = addMap.keySet().iterator();
			String taxId_add;
			String companyName_add;
			Date now = new Date();
			while (it_add.hasNext()) {
				taxId_add = it_add.next();
				log.info("+++ taxId_add = " + taxId_add);
				companyName_add = addMap.get(taxId_add);
				PfpEnterprise pfpEnterprise = new PfpEnterprise();
				pfpEnterprise.setTaxId(taxId_add);
				pfpEnterprise.setCompanyName(companyName_add);
				pfpEnterprise.setCreateDate(now);
				enterpriseService.save(pfpEnterprise);
			}
			
			//處理移除清單
			Iterator<String> it_remove = removeMap.keySet().iterator();
			List<String> removeTaxIdList = new ArrayList<String>();
			String taxId_remove;
			while (it_remove.hasNext()) {
				taxId_remove = it_remove.next();
				log.info("--- taxId_remove = " + taxId_remove);
				enterpriseService.deletePfpEnterprise(taxId_remove);
				removeTaxIdList.add(taxId_remove);
			}

			//放行審核名單(只有統編衝到百大企業的才放行, 若有其他原因不放行, 包含過去退件的)
			List<PfdApplyForBusiness> applyForBusinessList = applyForBusinessService.findPfdApplyForBusiness(new HashMap<String, String>(), -1, 0);
			PfdApplyForBusiness applyForBusiness = null;
			String illegalReason;
			String[] array = null;
			String type;
			String taxId;
			for (int i=0; i<applyForBusinessList.size(); i++) {
				applyForBusiness = applyForBusinessList.get(i);
				if (!applyForBusiness.getStatus().equals(EnumPfdApplyForBusiness.PASS.getType())) {
					illegalReason = applyForBusiness.getIllegalReason();
					if (StringUtils.isNotEmpty(illegalReason)) {
						if (illegalReason.indexOf(";")==-1) { //唯一一個違規條件
							array = illegalReason.split(",");
							type = array[0];
							taxId = array[2];

							if (type.equals("enterprise")) { //違反百大客戶
								for (int k=0; k<removeTaxIdList.size(); k++) {
									if (taxId.equals(removeTaxIdList.get(k))) { //在移除名單內

										log.info(">>> applyForBusiness.getSeq() = " + applyForBusiness.getSeq());

										//改變狀態
										applyForBusiness.setStatus(EnumPfdApplyForBusiness.PASS.getType());
										applyForBusiness.setSysPassTime(now);
										applyForBusinessService.saveOrUpdate(applyForBusiness);

										//access log
										String logMsg = "系統放行開發，統編：" + taxId + "(" + applyForBusiness.getSeq().toString() + ")";
										admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.ADM, EnumAccesslogAction.AD_STATUS_MODIFY, logMsg, 
																		super.getSession().get(SessionConstants.SESSION_USER_ID).toString(), null, null, 
																		null, request.getRemoteAddr(), EnumAccesslogEmailStatus.NO);

									}
								}
							}
						}
					}
				}
			}

			this.message = "上傳成功！";

    	} catch (Exception e) {
    		log.error(e.getMessage(), e);
    	} finally {
    		try {
    			br.close();
    			ur.close();
    			fis.close();
    		} catch (Exception ioe) {
    			log.error(ioe.getMessage(), ioe);
    		}
    	}

		return SUCCESS;
	}

	public File getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, String> getAddMap() {
		return addMap;
	}

	public void setAddMap(Map<String, String> addMap) {
		this.addMap = addMap;
	}

	public Map<String, String> getRemoveMap() {
		return removeMap;
	}

	public void setRemoveMap(Map<String, String> removeMap) {
		this.removeMap = removeMap;
	}

	public List<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}

	public void setApplyForBusinessService(IPfdApplyForBusinessService applyForBusinessService) {
		this.applyForBusinessService = applyForBusinessService;
	}

	public void setEnterpriseService(IPfpEnterpriseService enterpriseService) {
		this.enterpriseService = enterpriseService;
	}

	public void setAdmAccesslogService(IAdmAccesslogService admAccesslogService) {
		this.admAccesslogService = admAccesslogService;
	}
}
