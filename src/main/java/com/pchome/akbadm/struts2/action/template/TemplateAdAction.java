package com.pchome.akbadm.struts2.action.template;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.template.AdmRelateTadDadVO;
import com.pchome.akbadm.db.dao.template.AdmRelateTproTadVO;
import com.pchome.akbadm.db.pojo.AdmAdPool;
import com.pchome.akbadm.db.pojo.AdmDefineAd;
import com.pchome.akbadm.db.pojo.AdmDefineAdType;
import com.pchome.akbadm.db.pojo.AdmRelateTadDad;
import com.pchome.akbadm.db.pojo.AdmRelateTproTad;
import com.pchome.akbadm.db.pojo.AdmTemplateAd;
import com.pchome.akbadm.db.pojo.PfbxPositionMenu;
import com.pchome.akbadm.db.pojo.PfbxSize;
import com.pchome.akbadm.db.service.pfbx.IPfbSizeService;
import com.pchome.akbadm.db.service.pfbx.IPfbxPositionMenuService;
import com.pchome.akbadm.db.service.sequence.SequenceService;
import com.pchome.akbadm.db.service.template.DefineAdService;
import com.pchome.akbadm.db.service.template.IAdPoolService;
import com.pchome.akbadm.db.service.template.IDefineAdService;
import com.pchome.akbadm.db.service.template.IDefineAdTypeService;
import com.pchome.akbadm.db.service.template.IRelateTadDadService;
import com.pchome.akbadm.db.service.template.IRelateTproTadService;
import com.pchome.akbadm.db.service.template.ITemplateAdService;
import com.pchome.akbadm.db.service.template.ITemplateProductService;
import com.pchome.akbadm.db.service.template.RelateTadDadService;
import com.pchome.akbadm.db.service.template.TemplateAdService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.rmi.sequence.EnumSequenceTableName;
import com.pchome.rmi.sequence.ISequenceProvider;

public class TemplateAdAction extends BaseCookieAction {
	private static final long serialVersionUID = -7272688527100189595L;
	private ISequenceProvider sequenceProvider;
	private IPfbSizeService pfbSizeService;
	private ITemplateAdService templateAdService;
	private IRelateTproTadService relateTproTadService;
	private ITemplateProductService templateProductService;
	private IDefineAdService defineAdService;
	private IRelateTadDadService relateTadDadService;
	private IAdPoolService adPoolService;
	private IDefineAdTypeService defineAdTypeService;
	private SequenceService sequenceService;
	private IPfbxPositionMenuService pfbxPositionMenuService;
	private String admAddata;

	private String message = "";

	//查詢條件(查詢頁面用)
	private String queryTemplateAdSeq;		// 廣告樣板編號
	private String queryTemplateAdName;		// 廣告樣板名稱
	private String queryTemplateAdType;		// 廣告樣板類型
	private String queryTemplateAdSize;		// 廣告樣板尺寸

	//查詢結果(查詢頁面用)
	private List<AdmTemplateAd> templateAdList;
	private List<PfbxSize> pfbxSizeList;
	//廣告樣板序號(查詢頁面點修改或刪除時使用)
	private String templateAdSeq;

	//元件參數(新增及修改頁面用)
	private String paramTemplateAdSeq;		// 廣告樣板序號
	private String paramTemplateAdName;		// 廣告樣板名稱
	private String paramTemplateAdWidth;	// 廣告樣板寬度
	private String paramTemplateAdHeight;	// 廣告樣板高度
	private String paramTemplateAdContent;	// 廣告樣板內容
	private String diffCompany;             // 區分廠商
	private String paramDefineAdCount;		// 廣告定義數量
	private String paramTemplateAdType; //廣告樣版類型
	private String paramAdType; //廣告類型
	private String paramAdproperties; //樣版屬性
	private String[] sel_define_ad_seq;
	private String newPool;					// 選擇是否要新增資料來源
	private String adPoolName;				// pool name
	private String adm_ad_pool;				// 選擇資料來源
	private String diff_comapny;			// 是否區分廠商
	private String paramTemplateAdProperties;
	//Define AD List
	private List<AdmDefineAd> defineAdList;
	private List<AdmAdPool> adPoolList;
	private List<AdmDefineAdType> defineAdTypeList;
	private List<PfbxPositionMenu> pfbxPositionMenuList;
	
	@Override
	public String execute() throws Exception {
	    this.templateAdList = templateAdService.getAllTemplateAd();
	    pfbxSizeList = pfbSizeService.loadAll();
	    pfbxPositionMenuList = pfbxPositionMenuService.loadAll();
	    queryTemplateAdType = "";
	    queryTemplateAdSize = "";
	    return SUCCESS;
	}

	public String query() throws Exception {
		//log.info(">>> queryTemplateAdSeq = " + queryTemplateAdSeq);
		//log.info(">>> queryTemplateAdName = " + queryTemplateAdName);
		log.info(">>> queryTemplateAdType = " + queryTemplateAdType);
		log.info(">>> queryTemplateAdSize = " + queryTemplateAdSize);
		pfbxSizeList = pfbSizeService.loadAll();
	    pfbxPositionMenuList = pfbxPositionMenuService.loadAll();
	    
		String queryTemplateAdWidth = "";
		String queryTemplateAdHeight = "";
		if (StringUtils.isNotEmpty(queryTemplateAdSize)) {
			String[] adSize = queryTemplateAdSize.split("x");
			queryTemplateAdWidth = adSize[0];
			queryTemplateAdHeight = adSize[1];
		}
		this.templateAdList = templateAdService.getTemplateAdByCondition(queryTemplateAdSeq, queryTemplateAdName,queryTemplateAdType,queryTemplateAdWidth,queryTemplateAdHeight);
		return SUCCESS;
	}

	public String goAddPage() throws Exception {
		this.adPoolList = adPoolService.loadAll();
		pfbxSizeList = pfbSizeService.loadAll();
		defineAdTypeList = defineAdTypeService.loadAll();
		pfbxPositionMenuList = pfbxPositionMenuService.loadAll();
		return SUCCESS;
	}

	public String goRelatePage() throws Exception {
		log.info("StringUtils.isEmpty(paramTemplateAdName) = " + StringUtils.isEmpty(paramTemplateAdName));
		log.info("StringUtils.isEmpty(paramTemplateAdWidth) = " + StringUtils.isEmpty(paramTemplateAdWidth));
		log.info("StringUtils.isEmpty(paramTemplateAdHeight) = " + StringUtils.isEmpty(paramTemplateAdHeight));
		log.info("StringUtils.isEmpty(paramDefineAdCount) = " + paramDefineAdCount);
		log.info(">>> templateAdSeq = " + templateAdSeq);

		this.initTemplateAdRelate();
		pfbxPositionMenuList = pfbxPositionMenuService.loadAll();
		return SUCCESS;
	}

	private void initTemplateAdRelate() throws Exception {
		this.defineAdList = defineAdService.loadAll();
		pfbxPositionMenuList = pfbxPositionMenuService.loadAll();
	}

	public String doAdd() throws Exception {
	    	if(!doCheckTemplareInfo()){
	    	    return INPUT;
	    	}
		this.adPoolList = adPoolService.loadAll();
		AdmTemplateAd admTemplateAd = new AdmTemplateAd();
		admTemplateAd.setTemplateAdName(paramTemplateAdName);
		admTemplateAd.setTemplateAdWidth(paramTemplateAdWidth);
		admTemplateAd.setTemplateAdHeight(paramTemplateAdHeight);
		admTemplateAd.setAuto("");
		admTemplateAd.setDefineAdCount(Integer.parseInt(paramDefineAdCount));
		defineAdList = defineAdService.getDefineAdByCondition("", "", adm_ad_pool);
		log.info("paramDefineAdCount = " + paramDefineAdCount);
		log.info("adm_ad_pool = " + adm_ad_pool);
		log.info("defineAdList.size() = " + defineAdList.size());
		pfbxSizeList = pfbSizeService.loadAll();
		defineAdTypeList = defineAdTypeService.loadAll();
		pfbxPositionMenuList = pfbxPositionMenuService.loadAll();
		return SUCCESS;
	}
	
	
	//檢查樣版資訊
	public boolean doCheckTemplareInfo(){
	    boolean checkFlag = true;
	    if(StringUtils.isBlank(paramTemplateAdType)){
		message = "請輸入廣告樣板類型！";
		checkFlag = false;
	    }else if(StringUtils.isBlank(paramAdType)){
		message = "請輸入廣告類型！";
		checkFlag = false;
	    }else if(StringUtils.isBlank(paramTemplateAdHeight) || StringUtils.isBlank(paramTemplateAdWidth)){
		message = "請輸入樣版尺寸！";
		checkFlag = false;
	    }else if(StringUtils.isBlank(paramAdType)){
		message = "請輸入廣告類型！";
		checkFlag = false;
	    }
	    if (StringUtils.isEmpty(paramTemplateAdName)) {
		message = "請輸入廣告樣板名稱！";
		checkFlag = false;
	    } else {
		paramTemplateAdName = paramTemplateAdName.trim();
		if (paramTemplateAdName.length() > 20) {
		    message = "廣告樣板名稱不可超過 20 字元！";
		    checkFlag = false;
		}
	    }
	    if (StringUtils.isEmpty(paramTemplateAdContent)) {
		message = "請輸入廣告樣板內容！";
		checkFlag = false;
	    }
	    if (StringUtils.isBlank(diffCompany) || (!"Y".equals(diffCompany) && !"N".equals(diffCompany))) {
		message = "請選擇是否區分廠商！";
		checkFlag = false;
	    }
	    if (StringUtils.isEmpty(paramDefineAdCount) || paramDefineAdCount.equals("0")) {
		message = "請選擇廣告定義數量！";
		
	    }
	    if (StringUtils.isEmpty(adm_ad_pool) || adm_ad_pool.equals("0")) {
		message = "請選擇資料來源！";
		checkFlag = false;
	    }
	    return checkFlag;
	}
	
	
	
	public String doRelate() throws Exception {
	    log.info("adm_ad_pool = " + adm_ad_pool);
	    if(!doCheckTemplareInfo()){
		return INPUT;
	    }
	    if(StringUtils.isBlank(paramTemplateAdProperties)){
		message = "請選擇廣告樣板屬性！";
		return INPUT;
	    }
	    log.info("adm_ad_pool >>> " + adm_ad_pool);
	    AdmAdPool admAdPool = adPoolService.getAdPoolBySeq(adm_ad_pool);
	    log.info("adpoolname = " + admAdPool.getAdPoolName());
	    //1.取得序號
	    String templateSeq = getTemplateAdDbNameSeq(EnumSequenceTableName.ADM_TEMPLATE_AD);
	    EnumSequenceTableName seqTbl_T = EnumSequenceTableName.ADM_TEMPLATE_AD;
	    String tblShortName_T = seqTbl_T.getCharName();
	    String templateadFilePath = admAddata + tblShortName_T + File.separator + templateSeq + ".def";
	    log.info("templateadFilePath = " + templateadFilePath);
	    
	    //2.新增樣版
	    AdmTemplateAd admTemplateAd = new AdmTemplateAd();
	    admTemplateAd.setTemplateAdSeq(templateSeq);
	    admTemplateAd.setTemplateAdName(paramTemplateAdName);
	    admTemplateAd.setTemplateAdWidth(paramTemplateAdWidth);
	    admTemplateAd.setTemplateAdHeight(paramTemplateAdHeight);
	    admTemplateAd.setAuto("");
	    admTemplateAd.setDefineAdCount(Integer.parseInt(paramDefineAdCount));
	    admTemplateAd.setTemplateAdFile(templateadFilePath);
	    admTemplateAd.setAdmAdPool(admAdPool);
	    admTemplateAd.setAdmTemplateAdType(Integer.parseInt(paramTemplateAdType));
	    admTemplateAd.setAdType(Integer.parseInt(paramAdType));
	    String pdbxXtype = "";
	    pdbxXtype  = pfbxPositionMenuService.get(Integer.parseInt(paramTemplateAdProperties)).getPfbxAdTemplateProductXType();
	    admTemplateAd.setAdTemplateProductXType(pdbxXtype);
	    templateAdService.insertAdmTemplateAd(admTemplateAd);
	    
	    //3.樣版設定值寫入檔案
	    StringBuffer def = new StringBuffer();
	    def.append("PoolSeq:").append(admAdPool.getAdPoolSeq()).append("\n");
	    def.append("DiffCompany:").append(diffCompany).append("\n\n");
	    def.append("html:\n"); 
	    FileUtils.writeStringToFile(new File(templateadFilePath), def.toString(), CHARSET);
	    
	    //4.取得tad序號
	    EnumSequenceTableName seqTbl_R = EnumSequenceTableName.ADM_RELATE_TAD_DAD;
	    //5.建立關聯資料tad to tad
	    for(int i = 0; i < sel_define_ad_seq.length; i++) {
		String relateTadDadSeq = sequenceProvider.getSequenceID(seqTbl_R);
		String sDefineAdSeq = sel_define_ad_seq[i];
		AdmRelateTadDadVO admRelateTadDadVO = new AdmRelateTadDadVO();
		admRelateTadDadVO.setRelateTadDadSeq(relateTadDadSeq);
		admRelateTadDadVO.setTemplateAdSeq(templateSeq);
		admRelateTadDadVO.setDefineAdOrder(i + 1);
		admRelateTadDadVO.setDefineAdSeq(sDefineAdSeq);
		admRelateTadDadVO.setAdPoolSeq(adm_ad_pool);
		relateTadDadService.saveAdmRelateTadDad(admRelateTadDadVO);
	    }
	    message = "新增成功！";
	    return SUCCESS;
	}

	
	//產生樣版名稱序號
	public String getTemplateAdDbNameSeq(EnumSequenceTableName tableName) throws Exception{
	   String seq = "";
	   if(paramAdType.equals("1")){
	       seq = "s_";
	   }else {
	       seq = "c_";
	   } 
	   pfbxPositionMenuList = pfbxPositionMenuService.loadAll();
	   String adType = "";
	   boolean paramTemplateAdPropertiesFlag = false;
	   boolean paramTemplateAdTypeFlag = false;
	   for (PfbxPositionMenu  pfbxPositionMenu : pfbxPositionMenuList) {
	       if(String.valueOf(pfbxPositionMenu.getId()).equals(paramTemplateAdProperties)){
		   seq = seq+pfbxPositionMenu.getPfbxAdTemplateProductXType().toLowerCase() + "_";
		   paramTemplateAdPropertiesFlag = true;
	       }
	       if(String.valueOf(pfbxPositionMenu.getId()).equals(paramTemplateAdType)){
		   adType = pfbxPositionMenu.getPfbxAdTemplateProductXType().toLowerCase()+"_";
		   paramTemplateAdTypeFlag = true;
		}
	       if(paramTemplateAdPropertiesFlag && paramTemplateAdTypeFlag){
		   break;
	       }
	    }
	   String index = "";
	   index = sequenceProvider.getSequenceID(tableName,"_");
	   index = index.substring(0, 4) + index.substring(index.length() - 4, index.length());
	   return seq = seq + adType + index;
	}
	public String goUpdatePage() throws Exception {
		log.info(">>> templateAdSeq = " + templateAdSeq);
		pfbxPositionMenuList = pfbxPositionMenuService.loadAll();
		pfbxSizeList = pfbSizeService.loadAll();
		AdmTemplateAd admTemplateAd = templateAdService.getTemplateAdBySeq(templateAdSeq);
		this.paramTemplateAdSeq = admTemplateAd.getTemplateAdSeq();
		this.paramTemplateAdName = admTemplateAd.getTemplateAdName();
		this.paramTemplateAdWidth = admTemplateAd.getTemplateAdWidth();
		this.paramTemplateAdHeight = admTemplateAd.getTemplateAdHeight();
		String templateproductFilePath = admTemplateAd.getTemplateAdFile();
		this.paramAdType = String.valueOf(admTemplateAd.getAdType());
		this.paramTemplateAdType = String.valueOf(admTemplateAd.getAdmTemplateAdType());
		this.paramTemplateAdContent =  FileUtils.readFileToString(new File(templateproductFilePath), CHARSET);
		for (PfbxPositionMenu pfbxPositionMenu : pfbxPositionMenuList) {
		    if(pfbxPositionMenu.getPfbxAdTemplateProductXType().equals(admTemplateAd.getAdTemplateProductXType())){
			this.paramTemplateAdProperties = String.valueOf(pfbxPositionMenu.getId());
			break;
		    }
		}
		return SUCCESS;
	}
	
	//修改樣版設定後變更樣版名稱
	public String goUpdateTemplateName(String templateAdSeq) throws Exception {
	    String seqNew = "";
	    if(paramAdType.equals("1")){
		seqNew = "s_";
	    }else {
		seqNew = "c_";
	    }
	    pfbxPositionMenuList = pfbxPositionMenuService.loadAll();
	    String adType = "";
	    for (PfbxPositionMenu pfbxPositionMenu : pfbxPositionMenuList) {
		if(String.valueOf(pfbxPositionMenu.getId()).equals(paramTemplateAdProperties)){
		    seqNew = seqNew+pfbxPositionMenu.getPfbxAdTemplateProductXType().toLowerCase();
		}
		if(String.valueOf(pfbxPositionMenu.getId()).equals(paramTemplateAdType)){
		    adType = pfbxPositionMenu.getPfbxAdTemplateProductXType().toLowerCase();
		}
	    }
	    String name =   templateAdSeq.substring(templateAdSeq.length()-8, templateAdSeq.length());
	    seqNew = seqNew +"_"+adType +"_"+ name;
	    return seqNew;
	}
	
	public String doUpdate() throws Exception {
	    log.info("doUpdate = " + templateAdSeq);
	    AdmTemplateAd admTemplateAd = templateAdService.getTemplateAdBySeq(templateAdSeq);
	    paramTemplateAdName = admTemplateAd.getTemplateAdName();
	    paramDefineAdCount = String.valueOf(admTemplateAd.getDefineAdCount());
	    diffCompany =  admTemplateAd.getAdmAdPool().getDiffCompany();
	    adm_ad_pool = admTemplateAd.getAdmAdPool().getAdPoolName();
	    String auto = admTemplateAd.getAuto();
	    String filePath =  admTemplateAd.getTemplateAdFile();
	    filePath = filePath.substring(0, 31);
	    if(!doCheckTemplareInfo()){
		return INPUT;
	    }
	    //1.存入原始資料
	    AdmAdPool admAdPool = admTemplateAd.getAdmAdPool();
	    String newTemplateSeq = goUpdateTemplateName(templateAdSeq);
	    if(templateAdSeq.trim().equals(newTemplateSeq.trim())){
		FileUtils.writeStringToFile(new File(filePath+newTemplateSeq+".def"), paramTemplateAdContent, CHARSET);
	    }
	    AdmTemplateAd admTemplateAdUpdate = new AdmTemplateAd();
	    admTemplateAdUpdate.setTemplateAdSeq(newTemplateSeq);
	    admTemplateAdUpdate.setTemplateAdName(paramTemplateAdName);
	    admTemplateAdUpdate.setAdmTemplateAdType(Integer.parseInt(paramTemplateAdType));
	    admTemplateAdUpdate.setTemplateAdHeight(paramTemplateAdHeight);
	    admTemplateAdUpdate.setTemplateAdWidth(paramTemplateAdWidth);
	    String pdbxXtype= "";
	    pdbxXtype  = pfbxPositionMenuService.get(Integer.parseInt(paramTemplateAdProperties)).getPfbxAdTemplateProductXType();
	    admTemplateAdUpdate.setAdTemplateProductXType(pdbxXtype);
	    admTemplateAdUpdate.setAdType(Integer.parseInt(paramAdType));
	    admTemplateAdUpdate.setDefineAdCount(Integer.parseInt(paramDefineAdCount));
	    admTemplateAdUpdate.setAdmAdPool(admAdPool);
	    admTemplateAdUpdate.setAuto("");
	    admTemplateAdUpdate.setTemplateAdFile(filePath+newTemplateSeq+".def");
	   
	    if(!templateAdSeq.equals(newTemplateSeq)){
		templateAdService.insertAdmTemplateAd(admTemplateAdUpdate);
		
		Set<AdmRelateTadDad> admRelateTadDadSet = admTemplateAd.getAdmRelateTadDads();
		List<AdmRelateTadDad> admRelateTadDadListOriginalList = new ArrayList<AdmRelateTadDad>();
		for (AdmRelateTadDad admRelateTadDad : admRelateTadDadSet) {
		    admRelateTadDadListOriginalList.add(admRelateTadDad);
		    relateTadDadService.deleteAdmRelateTadDad(admRelateTadDad.getRelateTadDadSeq());
		}
		    
		List<AdmRelateTproTad> admRelateTproTadList = relateTproTadService.getAllRelateTproTad();
		List<AdmRelateTproTad> admRelateTproOriginalList = new ArrayList<AdmRelateTproTad>();
		for (AdmRelateTproTad admRelateTproTad : admRelateTproTadList) {
		    if(admRelateTproTad.getAdmTemplateAd().getTemplateAdSeq().equals(templateAdSeq)){
			admRelateTproOriginalList.add(admRelateTproTad);
			relateTproTadService.deleteAdmRelateTproTad(admRelateTproTad.getRelateTproTadSeq());
		    }
		}
		//更新關聯tad
		EnumSequenceTableName seqTbl_R = EnumSequenceTableName.ADM_RELATE_TAD_DAD;
		for (AdmRelateTadDad admRelateTadDad : admRelateTadDadListOriginalList) {
		    AdmRelateTadDad admRelateTadDadObj = new AdmRelateTadDad();
		    admRelateTadDadObj.setAdmAdPool(admTemplateAdUpdate.getAdmAdPool());
		    admRelateTadDadObj.setAdmDefineAd(admRelateTadDad.getAdmDefineAd());
		    admRelateTadDadObj.setAdmTemplateAd(admTemplateAdUpdate);
		    admRelateTadDadObj.setDefineAdOrder(admRelateTadDad.getDefineAdOrder());
		    String relateTadDadSeq = sequenceProvider.getSequenceID(seqTbl_R);
		    admRelateTadDadObj.setRelateTadDadSeq(relateTadDadSeq);
		    admRelateTadDadObj.setAdmTemplateAd(admTemplateAdUpdate);
		    relateTadDadService.insertAdmRelateTadDad(admRelateTadDadObj);
		}
		    
		//更新關聯tpro
		EnumSequenceTableName seqTpro = EnumSequenceTableName.ADM_RELATE_TPRO_TAD;
		for (AdmRelateTproTad admRelateTproTad : admRelateTproOriginalList) {
		    String relateTproSeq = sequenceProvider.getSequenceID(seqTpro);
		    AdmRelateTproTad admRelateTproTadVO = new AdmRelateTproTad();
		    admRelateTproTadVO.setAdmTemplateAd(admTemplateAdUpdate);
		    admRelateTproTadVO.setRelateTproTadSeq(relateTproSeq);
		    admRelateTproTadVO.setTemplateAdOrder(admRelateTproTad.getTemplateAdOrder());
		    admRelateTproTadVO.setTemplateProductSeqSub(admRelateTproTad.getTemplateProductSeqSub());
		    admRelateTproTadVO.setAdmTemplateProduct(admRelateTproTad.getAdmTemplateProduct());
		    relateTproTadService.insertAdmRelateTproTad(admRelateTproTadVO);
		}
		templateAdService.deleteAdmTemplateAd(templateAdSeq);
	    }
	  String templateadFilePath = admAddata + EnumSequenceTableName.ADM_TEMPLATE_AD.getCharName() + File.separator + templateAdSeq + ".def";
	  File tadFile = new File(templateadFilePath); 
	  if(tadFile.exists()){
	      tadFile.delete();
	  }
	  FileUtils.writeStringToFile(new File(filePath+newTemplateSeq+".def"), paramTemplateAdContent, CHARSET);	    
	  message = "修改成功！";
	  return SUCCESS;
	}

	
	//刪除廣告樣版
	public String doDelete() throws Exception {
	    log.info(">>> templateAdSeq = " + templateAdSeq);
	    List<AdmRelateTadDad> admRelateTadDadList = new ArrayList<AdmRelateTadDad>();
	    admRelateTadDadList = relateTadDadService.getAllRelateTadDad();
	    for (AdmRelateTadDad admRelateTadDad : admRelateTadDadList) {
		if(admRelateTadDad.getAdmTemplateAd().getTemplateAdSeq().equals(templateAdSeq)){
		    relateTadDadService.deleteAdmRelateTadDad(admRelateTadDad.getRelateTadDadSeq());
		}
	    }
	    
	    List<AdmRelateTproTad> admRelateTproTadList = relateTproTadService.getAllRelateTproTad();
	    for (AdmRelateTproTad admRelateTproTad : admRelateTproTadList) {
		if(admRelateTproTad.getAdmTemplateAd().getTemplateAdSeq().equals(templateAdSeq)){
		    relateTproTadService.deleteAdmRelateTproTad(admRelateTproTad.getRelateTproTadSeq());
		    break;
		}
	    }
	    String templateadFilePath = admAddata + EnumSequenceTableName.ADM_TEMPLATE_AD.getCharName() + File.separator + templateAdSeq + ".def";
	    File tadFile = new File(templateadFilePath); 
	    if(tadFile.exists()){
		tadFile.delete();
	    }
	    System.out.println(templateAdSeq);
	    templateAdService.deleteAdmTemplateAd(templateAdSeq);
	    message = "刪除成功！";
	    return SUCCESS;
	}

	public void setTemplateAdService(TemplateAdService templateAdService) {
		this.templateAdService = templateAdService;
	}

	public void setDefineAdService(DefineAdService defineAdService) {
		this.defineAdService = defineAdService;
	}

	public void setRelateTadDadService(RelateTadDadService relateTadDadService) {
		this.relateTadDadService = relateTadDadService;
	}

	public void setAdPoolService(IAdPoolService adPoolService) {
		this.adPoolService = adPoolService;
	}

	public void setAdmAddata(String admAddata) {
	    this.admAddata = admAddata;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<AdmTemplateAd> getTemplateAdList() {
		return templateAdList;
	}

	public void setTemplateAdList(List<AdmTemplateAd> templateadList) {
		this.templateAdList = templateadList;
	}

	public List<AdmDefineAd> getDefineAdList() {
		return defineAdList;
	}

	public void setAdPoolList(List<AdmAdPool> adPoolList) {
		this.adPoolList = adPoolList;
	}

	public List<AdmAdPool> getAdPoolList() {
		return adPoolList;
	}

	public String getTemplateAdSeq() {
		return templateAdSeq;
	}

	public void setTemplateAdSeq(String templateAdSeq) {
		this.templateAdSeq = templateAdSeq;
	}

	public String getQueryTemplateAdSeq() {
		return queryTemplateAdSeq;
	}

	public void setQueryTemplateAdSeq(String queryTemplateAdSeq) {
		this.queryTemplateAdSeq = queryTemplateAdSeq;
	}

	public String getQueryTemplateAdName() {
		return queryTemplateAdName;
	}

	public void setQueryTemplateAdName(String queryTemplateAdName) {
		this.queryTemplateAdName = queryTemplateAdName;
	}

	public String getQueryTemplateAdType() {
		return queryTemplateAdType;
	}

	public void setQueryTemplateAdType(String queryTemplateAdType) {
		this.queryTemplateAdType = queryTemplateAdType;
	}

	public String getQueryTemplateAdSize() {
		return queryTemplateAdSize;
	}

	public void setQueryTemplateAdSize(String queryTemplateAdSize) {
		this.queryTemplateAdSize = queryTemplateAdSize;
	}

	public String getParamTemplateAdSeq() {
		return paramTemplateAdSeq;
	}

	public void setParamTemplateAdSeq(String paramTemplateAdSeq) {
		this.paramTemplateAdSeq = paramTemplateAdSeq;
	}

	public String getParamTemplateAdName() {
		return paramTemplateAdName;
	}

	public void setParamTemplateAdName(String paramTemplateAdName) {
		this.paramTemplateAdName = paramTemplateAdName;
	}

	public String getParamTemplateAdWidth() {
		return paramTemplateAdWidth;
	}

	public void setParamTemplateAdWidth(String paramTemplateAdWidth) {
		this.paramTemplateAdWidth = paramTemplateAdWidth;
	}

	public String getParamTemplateAdHeight() {
		return paramTemplateAdHeight;
	}

	public void setParamTemplateAdHeight(String paramTemplateAdHeight) {
		this.paramTemplateAdHeight = paramTemplateAdHeight;
	}

	public String getParamTemplateAdContent() {
		return paramTemplateAdContent;
	}

	public void setParamTemplateAdContent(String paramTemplateAdContent) {
		this.paramTemplateAdContent = paramTemplateAdContent;
	}

	public String getDiffCompany() {
	    return diffCompany;
	}

	public void setDiffCompany(String diffCompany) {
	    this.diffCompany = diffCompany;
	}

	public String getParamDefineAdCount() {
		return paramDefineAdCount;
	}

	public void setParamDefineAdCount(String paramDefineAdCount) {
		this.paramDefineAdCount = paramDefineAdCount;
	}

	public String[] getSel_define_ad_seq() {
		return sel_define_ad_seq;
	}

	public void setSel_define_ad_seq(String[] sel_define_ad_seq) {
		this.sel_define_ad_seq = sel_define_ad_seq;
	}

	public String getNewPool() {
		return newPool;
	}

	public void setNewPool(String newPool) {
		this.newPool = newPool;
	}

	public String getAdPoolName() {
		return adPoolName;
	}

	public void setAdPoolName(String adPoolName) {
		this.adPoolName = adPoolName;
	}

	public String getAdm_ad_pool() {
		return adm_ad_pool;
	}

	public void setAdm_ad_pool(String adm_ad_pool) {
		this.adm_ad_pool = adm_ad_pool;
	}

	public String getDiff_comapny() {
		return diff_comapny;
	}

	public void setDiff_comapny(String diff_comapny) {
		this.diff_comapny = diff_comapny;
	}

	public IAdPoolService getAdPoolService() {
		return adPoolService;
	}

	public ISequenceProvider getSequenceProvider() {
		return sequenceProvider;
	}

	public void setSequenceProvider(ISequenceProvider sequenceProvider) {
		this.sequenceProvider = sequenceProvider;
	}

	public IPfbSizeService getPfbSizeService() {
	    return pfbSizeService;
	}

	public void setPfbSizeService(IPfbSizeService pfbSizeService) {
	    this.pfbSizeService = pfbSizeService;
	}

	public List<PfbxSize> getPfbxSizeList() {
	    return pfbxSizeList;
	}

	public void setPfbxSizeList(List<PfbxSize> pfbxSizeList) {
	    this.pfbxSizeList = pfbxSizeList;
	}

	public IDefineAdTypeService getDefineAdTypeService() {
	    return defineAdTypeService;
	}

	public void setDefineAdTypeService(IDefineAdTypeService defineAdTypeService) {
	    this.defineAdTypeService = defineAdTypeService;
	}

	public List<AdmDefineAdType> getDefineAdTypeList() {
	    return defineAdTypeList;
	}

	public void setDefineAdTypeList(List<AdmDefineAdType> defineAdTypeList) {
	    this.defineAdTypeList = defineAdTypeList;
	}

	public String getParamTemplateAdType() {
	    return paramTemplateAdType;
	}

	public void setParamTemplateAdType(String paramTemplateAdType) {
	    this.paramTemplateAdType = paramTemplateAdType;
	}

	public String getParamAdType() {
	    return paramAdType;
	}

	public void setParamAdType(String paramAdType) {
	    this.paramAdType = paramAdType;
	}

	public String getParamAdproperties() {
	    return paramAdproperties;
	}

	public void setParamAdproperties(String paramAdproperties) {
	    this.paramAdproperties = paramAdproperties;
	}

	public SequenceService getSequenceService() {
	    return sequenceService;
	}

	public void setSequenceService(SequenceService sequenceService) {
	    this.sequenceService = sequenceService;
	}

	public String getParamTemplateAdProperties() {
	    return paramTemplateAdProperties;
	}

	public void setParamTemplateAdProperties(String paramTemplateAdProperties) {
	    this.paramTemplateAdProperties = paramTemplateAdProperties;
	}

	public List<PfbxPositionMenu> getPfbxPositionMenuList() {
	    return pfbxPositionMenuList;
	}

	public void setPfbxPositionMenuList(List<PfbxPositionMenu> pfbxPositionMenuList) {
	    this.pfbxPositionMenuList = pfbxPositionMenuList;
	}

	public IPfbxPositionMenuService getPfbxPositionMenuService() {
	    return pfbxPositionMenuService;
	}

	public void setPfbxPositionMenuService(
		IPfbxPositionMenuService pfbxPositionMenuService) {
	    this.pfbxPositionMenuService = pfbxPositionMenuService;
	}

	public ITemplateProductService getTemplateProductService() {
	    return templateProductService;
	}

	public void setTemplateProductService(
		ITemplateProductService templateProductService) {
	    this.templateProductService = templateProductService;
	}

	public IRelateTproTadService getRelateTproTadService() {
	    return relateTproTadService;
	}

	public void setRelateTproTadService(IRelateTproTadService relateTproTadService) {
	    this.relateTproTadService = relateTproTadService;
	}

	
}
