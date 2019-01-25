package com.pchome.akbadm.struts2.action.template;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.template.AdmAdPoolDefVO;
import com.pchome.akbadm.db.dao.template.AdmRelateFrmDadVO;
import com.pchome.akbadm.db.dao.template.AdmRelateTproTadVO;
import com.pchome.akbadm.db.pojo.AdmAdPool;
import com.pchome.akbadm.db.pojo.AdmDefineAd;
import com.pchome.akbadm.db.pojo.AdmRelateTproTad;
import com.pchome.akbadm.db.pojo.AdmTemplateAd;
import com.pchome.akbadm.db.pojo.AdmTemplateProduct;
import com.pchome.akbadm.db.pojo.PfbxPositionMenu;
import com.pchome.akbadm.db.pojo.PfbxSize;
import com.pchome.akbadm.db.service.pfbx.IPfbSizeService;
import com.pchome.akbadm.db.service.pfbx.IPfbxPositionMenuService;
import com.pchome.akbadm.db.service.template.IAdPoolService;
import com.pchome.akbadm.db.service.template.IDefineAdTypeService;
import com.pchome.akbadm.db.service.template.IRelateTproTadService;
import com.pchome.akbadm.db.service.template.ITemplateAdService;
import com.pchome.akbadm.db.service.template.ITemplateProductService;
import com.pchome.akbadm.db.service.template.RelateTproTadService;
import com.pchome.akbadm.db.service.template.TemplateAdService;
import com.pchome.akbadm.db.service.template.TemplateProductService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.rmi.sequence.EnumSequenceTableName;
import com.pchome.rmi.sequence.ISequenceProvider;

public class TemplateProductAction extends BaseCookieAction {
    private static final long serialVersionUID = -5780848510245776139L;

    private ISequenceProvider sequenceProvider;

	private ITemplateProductService templateProductService;
	private ITemplateAdService templateAdService;
	private IRelateTproTadService relateTproTadService;
	private IAdPoolService adPoolService;
	private IPfbSizeService pfbSizeService;
	private IDefineAdTypeService defineAdTypeService;
	private String admAddata;
	private IPfbxPositionMenuService pfbxPositionMenuService;
	private String message = "";

	//查詢條件(查詢頁面用)
	private String queryTemplateProductSeq;		// 商品樣板編號
	private String queryTemplateProductName;	// 商品樣板名稱
	private String queryTemplateProductType;	// 廣告樣板類型
	private String queryTemplateProductSize;	// 廣告樣板尺寸

	//查詢結果(查詢頁面用)
	private List<AdmTemplateProduct> templateProductList;
	private List<PfbxSize> pfbxSizeList;
	private List<AdmDefineAd> DefinedAdList;

	//指定template product Id(查詢頁面點修改或刪除時使用)
	private String templateProductSeq;

	//元件參數(新增及修改頁面用)
	private String paramTemplateProductSeq;		// 商品樣板序號
	private String paramTemplateProductName;	// 商品樣板名稱
	private String paramTemplateProductWidth;	// 商品樣板寬度
	private String paramTemplateProductHeight;	// 商品樣板高度
	private String paramTemplateProductContent;	// 商品樣板內容
	private String paramTemplateAdCount;		// 廣告樣板數量
	private String auto;                        	// 自動廣告則數
	private String pageTotalNum;                	// 商品樣板每頁筆數
	private String startNum;                    	// 商品樣板開始筆數
	private String iframe;                      	// 商品樣板是否為 iframe
	private String height;                      	// 商品樣板 iframe 高度
	private String weight;                      	// 商品樣板 iframe 寬度
	private String paramTemplateAdType; 	    	// 廣告樣版類型
	private String paramAdType;		    	// 廣告類型
	private String[] sel_template_ad_seq;
	private String[] adNums;
	private String show;
	private String newPool;
	private String target;
	private String adPoolName;
	private String adm_ad_pool;
	private int dadCount;
	private String paramAdPoolSeq;
	private String paramAdPoolName;
	private String[] sel_fram_dad_seq;
	private List<AdmTemplateAd> templateAdList;	//Template AD 廣告樣版 List
	private List<AdmAdPool> adPoolList;
	private List<PfbxPositionMenu> pfbxPositionMenuList;
	private String paramTemplateAdProperties;
	@Override
	public String execute() throws Exception {
		this.templateProductList = templateProductService.getAllTemplateProduct();
		this.pfbxSizeList = pfbSizeService.loadAll();
		this.pfbxPositionMenuList = pfbxPositionMenuService.loadAll();
		queryTemplateProductType = "";
	    queryTemplateProductSize = "";
		return SUCCESS;
	}
	
	public String changeTemplateProductSizeView() throws Exception {
	    Map<String,String> sizeMap = new HashMap<String,String>();
	    sizeMap.put("width", paramTemplateProductWidth);
	    sizeMap.put("height", paramTemplateProductHeight);
	    this.pfbxSizeList = pfbSizeService.loadAll();
	    this.templateProductList = templateProductService.getTemplateProductBySize(sizeMap);
	    return SUCCESS;
	}

	public String query() throws Exception {
		log.info(">>> do query ");
	    this.pfbxSizeList = pfbSizeService.loadAll();
	    this.pfbxPositionMenuList = pfbxPositionMenuService.loadAll();
	    
	    String queryTemplateProductWidth = "";
		String queryTemplateProductHeight = "";
		if (StringUtils.isNotEmpty(queryTemplateProductSize)) {
			String[] adSize = queryTemplateProductSize.split("x");
			queryTemplateProductWidth = adSize[0];
			queryTemplateProductHeight = adSize[1];
		}
		this.templateProductList = templateProductService.getTemplateProductByCondition(queryTemplateProductSeq, queryTemplateProductName,queryTemplateProductType,queryTemplateProductWidth,queryTemplateProductHeight);
		return SUCCESS;
	}

	public String goAddPage() throws Exception {
	    pfbxSizeList = pfbSizeService.loadAll();
	    pfbxPositionMenuList = pfbxPositionMenuService.loadAll();
	    return SUCCESS;
	}

	public String goRelatePage() throws Exception {
		log.info("StringUtils.isEmpty(paramTemplateProductName) = " + StringUtils.isEmpty(paramTemplateProductName));
		log.info("StringUtils.isEmpty(paramTemplateProductWidth) = " + StringUtils.isEmpty(paramTemplateProductWidth));
		log.info("StringUtils.isEmpty(paramTemplateProductHeight) = " + StringUtils.isEmpty(paramTemplateProductHeight));
		log.info("StringUtils.isEmpty(paramTemplateAdCount) = " + paramTemplateAdCount);
		log.info(">>> templateProductSeq = ");
		log.info(">>> templateProductSeq = " + templateProductSeq);
		this.initTemplateAdRelate();

		return SUCCESS;
	}

	private void initTemplateAdRelate() throws Exception {
		this.templateProductList = templateProductService.getAllTemplateProduct();
		this.templateAdList = templateAdService.getAllTemplateAd();
		pfbxPositionMenuList = pfbxPositionMenuService.loadAll();
	}

	
	
	
	//檢查商品資訊
	public boolean doCheckTemplareInfo(){
	    log.info("start >>>>");
	    boolean checkFlag = true;
	    if (StringUtils.isEmpty(paramTemplateProductName)) {
		message = "請輸入商品樣板名稱！";
		checkFlag = false;
	    } 
	    paramTemplateProductName = paramTemplateProductName.trim();
	    if (paramTemplateProductName.length() > 20) {
		message = "商品樣板名稱不可超過 20 字元！";
		checkFlag = false;
	    }
	    if (StringUtils.isEmpty(paramTemplateProductWidth) || StringUtils.isEmpty(paramTemplateProductHeight)) {
		message = "請輸入商品樣板尺寸！";
		checkFlag = false;
	    }
	    if (StringUtils.isEmpty(paramTemplateProductContent)) {
		message = "請輸入商品樣板內容！";
		checkFlag = false;
	    }
	    if (StringUtils.isEmpty(paramTemplateAdCount)) {
		message = "請選擇廣告樣版數量！";
		checkFlag = false;
	    }
	    if (StringUtils.isBlank(auto) || (!"Y".equals(auto) && !"N".equals(auto))) {
		message = "請選擇自動廣告則數！";
		checkFlag = false;
	    }
	    if (!"1".equals(paramTemplateAdCount)) {
                message = "設定自動廣告則數，廣告樣板數量需為1";
                
            }
	    if (StringUtils.isBlank(pageTotalNum)) {
	            message = "請輸入每頁筆數！";
	            checkFlag = false;
	    }	else if (!StringUtils.isNumeric(pageTotalNum)) {
		message = "每頁筆數需為數字！";
		checkFlag = false;
	    	}else if (pageTotalNum.length() > 4) {
	    	    message = "每頁筆數不可超過 4 字元！";
	    	    checkFlag = false;
	        }
	    if (StringUtils.isBlank(startNum)) {
		message = "請輸入開始筆數！";
		checkFlag = false;
	    }	else if (!StringUtils.isNumeric(startNum)) {
	            message = "開始筆數需為數字！";
	            checkFlag = false;
	        } else if (startNum.length() > 4) {
	            message = "開始筆數不可超過 4 字元！";
	            checkFlag = false;
	        }
	    if (StringUtils.isBlank(iframe) || (!"Y".equals(iframe) && !"N".equals(iframe))) {
		message = "請選擇是否為 iframe！";
		checkFlag = false;
	    }
	    
	    if (StringUtils.isBlank(height) || StringUtils.isBlank(weight)) {
		message = "請輸入 iframe 尺寸！";
		checkFlag = false;
	    }
	    return checkFlag;
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
	    index = index.substring(0, 5) + index.substring(index.length() - 4, index.length());
	    return seq = seq + adType + index;
	}
	
	
	public String doAdd() throws Exception {
	    log.info("Start>>>>");
	    if(!doCheckTemplareInfo()){
		return INPUT;
	    }
	    pfbxPositionMenuList = pfbxPositionMenuService.loadAll();
	    pfbxSizeList = pfbSizeService.loadAll();
	    
	    AdmTemplateProduct admTemplateProduct = new AdmTemplateProduct();
	    admTemplateProduct.setTemplateProductName(paramTemplateProductName);
	    admTemplateProduct.setTemplateProductWidth(paramTemplateProductWidth);
	    admTemplateProduct.setTemplateProductHeight(paramTemplateProductHeight);
	    admTemplateProduct.setTemplateAdCount(Integer.parseInt(paramTemplateAdCount));
	    this.templateProductList = templateProductService.getAllTemplateProduct();
	    this.templateAdList = templateAdService.getAllTemplateAd();
	    return SUCCESS;
	}

	public String doRelate() throws Exception {
	    log.info("Start");
	    if(!doCheckTemplareInfo()){
		return INPUT;
	    }
	    String templateProductXtype = "";
	    pfbxPositionMenuList = pfbxPositionMenuService.loadAll();
	    for (PfbxPositionMenu PfbxPositionMenu : pfbxPositionMenuList) {
		if(paramTemplateAdProperties.equals(String.valueOf(PfbxPositionMenu.getId()))){
		    templateProductXtype = PfbxPositionMenu.getPfbxAdTemplateProductXType();
		}
	    }
	    
	    
	    // 商品樣板序號
	    templateProductSeq = getTemplateAdDbNameSeq(EnumSequenceTableName.ADM_TEMPLATE_PRODUCT);
	    EnumSequenceTableName seqTbl_T = EnumSequenceTableName.ADM_TEMPLATE_PRODUCT;
	    String tblShortName_T = seqTbl_T.getCharName();
	    String templateProductFilePath = admAddata + tblShortName_T + File.separator + templateProductSeq + ".def"; 
	    AdmTemplateProduct admTemplateProduct = new AdmTemplateProduct();
	    admTemplateProduct.setTemplateProductSeq(templateProductSeq);
	    admTemplateProduct.setTemplateProductName(paramTemplateProductName);
	    admTemplateProduct.setTemplateProductWidth(paramTemplateProductWidth);
	    admTemplateProduct.setTemplateProductHeight(paramTemplateProductHeight);
	    admTemplateProduct.setTemplateProductFile(templateProductFilePath);
	    admTemplateProduct.setTemplateAdCount(Integer.parseInt(paramTemplateAdCount));
	    admTemplateProduct.setTemplateProductShow(show);
	    admTemplateProduct.setAdmTemplateAdType(Integer.parseInt(paramTemplateAdType));
	    admTemplateProduct.setAdType(Integer.parseInt(paramAdType));
	    admTemplateProduct.setIframeWidth(weight);
	    admTemplateProduct.setIframeHeight(height);
	    admTemplateProduct.setTemplateProductXtype(templateProductXtype);
	    admTemplateProduct.setPageTotalNum(Integer.parseInt(pageTotalNum));
	    admTemplateProduct.setStartNum(Integer.parseInt(startNum));
	    StringBuffer def = new StringBuffer();
	    TreeMap<String, AdmAdPoolDefVO> adnum = new TreeMap<String, AdmAdPoolDefVO>();
	    ArrayList<String> prod = new ArrayList<String>();
	    for(int i = 0; i < sel_template_ad_seq.length; i++) {
		String sSeq = sel_template_ad_seq[i];
		String originalWord = "{#tpro_" + (i + 1) + "}";	// 被替換的字串，如 {#1}
		String adContent = "";		// 替換的內容
		if(sSeq.indexOf("tad") >= 0) {
		    AdmAdPoolDefVO adPoolDef = new AdmAdPoolDefVO();
		    AdmTemplateAd templatead = templateAdService.getTemplateAdBySeq(sSeq);
		    if(adnum.containsKey(templatead.getAdmAdPool().getAdPoolSeq())) {
			adPoolDef = adnum.get(templatead.getAdmAdPool().getAdPoolSeq());
			adPoolDef.setAdNum(Integer.valueOf(adNums[i]));
		    } else {
			adPoolDef.setAdPoolSeq(templatead.getAdmAdPool().getAdPoolSeq());
			adPoolDef.setAdNum(Integer.valueOf(adNums[i]));
			adPoolDef.setTemplateAdSeq(sSeq);
			adPoolDef.setAuto(templatead.getAuto());
			adPoolDef.setDiffCompany(templatead.getAdmAdPool().getDiffCompany());
			adnum.put(templatead.getAdmAdPool().getAdPoolSeq(), adPoolDef);
		    }
		} else {
		    prod.add(sSeq);
		}
		adContent = "<#" + sSeq + ">";
		paramTemplateProductContent = paramTemplateProductContent.replace(originalWord, adContent);
		}
		AdmAdPoolDefVO adpooldef = null;
		for (String poolId: adnum.keySet()) {
		    adpooldef = adnum.get(poolId);
		    def.append("TemplateAdSeq:").append(adpooldef.getTemplateAdSeq()).append("\n");
		    def.append("AdNum:").append(adpooldef.getAdNum()).append("\n");
		}
		def.append("Auto:").append(auto).append("\n");
		def.append("PageTotalNum:").append(pageTotalNum).append("\n");
		def.append("StartNum:").append(startNum).append("\n");
		def.append("adType:").append(paramAdType).append("\n");
		def.append("templateAdType:").append(paramTemplateAdType).append("\n");
		def.append("Iframe:").append(iframe).append("\n");
		def.append("xType:").append(templateProductXtype.toUpperCase()).append("\n");
		def.append("PositionHeight:").append(paramTemplateProductHeight).append("\n");
		def.append("PositionWidth:").append(paramTemplateProductWidth).append("\n");
		def.append("Height:").append(height).append("\n");
		def.append("Weight:").append(weight).append("\n\n");
		def.append("html:\n").append(paramTemplateProductContent).append("\n");
		// 將商品樣板的內容寫入檔案中
		FileUtils.writeStringToFile(new File(templateProductFilePath), def.toString(), CHARSET);
		// 將商品樣板存入DB中
		templateProductService.saveAdmTemplateProduct(admTemplateProduct);
		// 商品樣板廣告樣板關聯序號
		EnumSequenceTableName seqTbl_R = EnumSequenceTableName.ADM_RELATE_TPRO_TAD;
		for(int i = 0; i < sel_template_ad_seq.length; i++) {
		    String relateTproTadSeq = sequenceProvider.getSequenceID(seqTbl_R);
		    String sSeq = sel_template_ad_seq[i];
		    AdmRelateTproTadVO admRelateTproTadVO = new AdmRelateTproTadVO();
		    admRelateTproTadVO.setRelateTproTadSeq(relateTproTadSeq);
		    admRelateTproTadVO.setTemplateProductSeq(templateProductSeq);
		    admRelateTproTadVO.setTemplateAdOrder(i + 1);
		    // 判斷使用的是廣告樣板還是商品樣板
		    if(sSeq.indexOf("tad") >= 0) {
			admRelateTproTadVO.setTemplateAdSeq(sSeq);
		    } else {
			admRelateTproTadVO.setTemplateProductSeqSub(sSeq);
		    }
		    relateTproTadService.saveAdmRelateTproTad(admRelateTproTadVO);
		}
		message = "新增成功！";
		this.DefinedAdList = templateProductService.getAllDefineAdBySeq(relateTproTadService, admTemplateProduct);
		dadCount = DefinedAdList.size();
		log.info("dad_count = " + dadCount);
		this.templateProductList = templateProductService.getAllTemplateProduct();
		this.templateAdList = templateAdService.getAllTemplateAd();
		this.adPoolList = adPoolService.loadAll();
		return SUCCESS;
	}

	public String goPoolPage2() throws Exception {
		return SUCCESS;
	}

	public String doPool2() throws Exception {
		EnumSequenceTableName seqTbl_R = EnumSequenceTableName.ADM_RELATE_FRM_DAD;

		AdmTemplateProduct admTemplateProduct = templateProductService.getTemplateProductBySeq(paramTemplateProductSeq);
		DefinedAdList = templateProductService.getAllDefineAdBySeq(relateTproTadService, admTemplateProduct);

		for(int i = 0; i < sel_fram_dad_seq.length; i++) {
			String relateFrmDadSeq = sequenceProvider.getSequenceID(seqTbl_R);
			String sSeq = sel_fram_dad_seq[i];
			AdmRelateFrmDadVO admRelateFrmDadVO = new AdmRelateFrmDadVO();
			admRelateFrmDadVO.setRelateFrmDadSeq(relateFrmDadSeq);
			admRelateFrmDadVO.setAdFrameSeq(sSeq);
			admRelateFrmDadVO.setDefineAdSeq(DefinedAdList.get(i).getDefineAdSeq());
//			relateFrmDadService.saveAdmRelateFrmDad(admRelateFrmDadVO);
		}
		message = "新增成功！";

		return SUCCESS;
	}

	public String goUpdatePage() throws Exception {
		//log.info(">>> templateProductSeq = " + templateProductSeq);
		AdmTemplateProduct admTemplateproduct = templateProductService.getTemplateProductBySeq(templateProductSeq);
		this.paramTemplateProductSeq = admTemplateproduct.getTemplateProductSeq();
		this.paramTemplateProductName = admTemplateproduct.getTemplateProductName();
		this.paramTemplateProductWidth = admTemplateproduct.getTemplateProductWidth();
		this.paramTemplateProductHeight = admTemplateproduct.getTemplateProductHeight();
		
		weight = admTemplateproduct.getIframeWidth();
		height = admTemplateproduct.getIframeHeight();
		startNum = String.valueOf(admTemplateproduct.getStartNum());
		pageTotalNum = String.valueOf(admTemplateproduct.getPageTotalNum());
		String templateproductFilePath = admTemplateproduct.getTemplateProductFile();
		this.paramTemplateProductContent = FileUtils.readFileToString(new File(templateproductFilePath), CHARSET);
		pfbxSizeList = pfbSizeService.loadAll();
		return SUCCESS;
	}

	public String doUpdate() throws Exception {
		if (StringUtils.isEmpty(paramTemplateProductWidth)) {
			message = "請輸入商品樣板寬度！";
			return INPUT;
		} else {
			paramTemplateProductWidth = paramTemplateProductWidth.trim();
			try {
				if (paramTemplateProductWidth.length() > 4) {
					message = "商品樣板寬度不可超過 4 字元！";
					return INPUT;
				} else if (Integer.parseInt(paramTemplateProductWidth) < 0) {
					message = "商品樣板寬度不可小於0";
					return INPUT;
				} else if (Integer.parseInt(paramTemplateProductWidth) > 3000) {
					message = "商品樣板寬度不可大於3000";
					return INPUT;
				}
			} catch(Exception ex) {
				message = "商品樣板寬度輸入的格式錯誤";
				return INPUT;
			}
		}

		if (StringUtils.isEmpty(paramTemplateProductHeight)) {
			message = "請輸入商品樣板高度！";
			return INPUT;
		} else {
			paramTemplateProductHeight = paramTemplateProductHeight.trim();
			try {
				if (paramTemplateProductHeight.length() > 4) {
					message = "商品樣板高度不可超過 4 字元！";
					return INPUT;
				} else if (Integer.parseInt(paramTemplateProductHeight) < 0) {
					message = "商品樣板高度不可小於0";
					return INPUT;
				} else if (Integer.parseInt(paramTemplateProductHeight) > 2000) {
					message = "商品樣板高度不可大於2000";
					return INPUT;
				}
			} catch(Exception ex) {
				message = "商品樣板高度輸入的格式錯誤";
				return INPUT;
			}
		}

		if (StringUtils.isEmpty(paramTemplateProductContent)) {
			message = "請輸入商品樣板內容！";
			return INPUT;
		}

        String templateProductFilePath = admAddata + EnumSequenceTableName.ADM_TEMPLATE_PRODUCT.getCharName() + File.separator + templateProductSeq + ".def";
		log.info("templateProductSeq = " + templateProductSeq);
		log.info("paramTemplateProductName = " + paramTemplateProductName);
		log.info("paramTemplateProductWidth = " + paramTemplateProductWidth);
		log.info("paramTemplateProductHeight = " + paramTemplateProductHeight);
		log.info("templateproductFilePath = " + templateProductFilePath);
		AdmTemplateProduct admTemplateProduct = templateProductService.getTemplateProductBySeq(templateProductSeq);
		admTemplateProduct.setTemplateProductWidth(paramTemplateProductWidth);
		admTemplateProduct.setTemplateProductHeight(paramTemplateProductHeight);
		admTemplateProduct.setTemplateProductFile(templateProductFilePath);
		admTemplateProduct.setIframeWidth(weight);
		admTemplateProduct.setIframeHeight(height);
		admTemplateProduct.setPageTotalNum(Integer.parseInt(pageTotalNum));
		admTemplateProduct.setStartNum(Integer.parseInt(startNum));
		FileUtils.writeStringToFile(new File(templateProductFilePath), paramTemplateProductContent, CHARSET);
		log.info("admTemplateproduct = " + admTemplateProduct);
		templateProductService.updateAdmTemplateProduct(admTemplateProduct);

		message = "修改成功！";

		return SUCCESS;
	}

	public String doDelete() throws Exception {
	    log.info(">>> templateProductSeq = " + templateProductSeq);
	    AdmTemplateProduct admTemplateProduct = new AdmTemplateProduct();
	    admTemplateProduct =  templateProductService.getTemplateProductBySeq(templateProductSeq);
	    Set<AdmRelateTproTad>  admRelateTproTadSet  = admTemplateProduct.getAdmRelateTproTads();
	    for (AdmRelateTproTad admRelateTproTad : admRelateTproTadSet) {
		System.out.println(">>>"+admRelateTproTad.getRelateTproTadSeq());
		relateTproTadService.deleteAdmRelateTproTad(admRelateTproTad.getRelateTproTadSeq());
	    }
	    templateProductService.deleteAdmTemplateProduct(templateProductSeq);
	    
	    
	    File tadFile = new File(admTemplateProduct.getTemplateProductFile()); 
	    if(tadFile.exists()){
		tadFile.delete();
	    }
	    
	    message = "刪除成功！";
	    return SUCCESS;
	}

	public void setTemplateProductService(TemplateProductService templateProductService) {
		this.templateProductService = templateProductService;
	}

	public void setTemplateAdService(TemplateAdService templateAdService) {
		this.templateAdService = templateAdService;
	}

	public void setRelateTproTadService(RelateTproTadService relateTproTadService) {
		this.relateTproTadService = relateTproTadService;
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

	public List<AdmTemplateProduct> getTemplateProductList() {
		return templateProductList;
	}

	public void setTemplateProductList(List<AdmTemplateProduct> templateproductList) {
		this.templateProductList = templateproductList;
	}

	public List<AdmTemplateAd> getTemplateAdList() {
		return templateAdList;
	}

	public List<AdmDefineAd> getDefinedAdList() {
		return DefinedAdList;
	}

	public void setDefinedAdList(List<AdmDefineAd> definedAdList) {
		DefinedAdList = definedAdList;
	}

	public List<AdmAdPool> getAdPoolList() {
		return adPoolList;
	}

	public void setAdPoolList(List<AdmAdPool> adPoolList) {
		this.adPoolList = adPoolList;
	}

//	public List<AdmAdFrame> getAdFrameList() {
//		return adFrameList;
//	}
//
//	public void setAdFrameList(List<AdmAdFrame> adFrameList) {
//		this.adFrameList = adFrameList;
//	}

	public String getTemplateProductSeq() {
		return templateProductSeq;
	}

	public void setTemplateProductSeq(String templateProductSeq) {
		this.templateProductSeq = templateProductSeq;
	}

	public String getQueryTemplateProductSeq() {
		return queryTemplateProductSeq;
	}

	public void setQueryTemplateProductSeq(String queryTemplateProductSeq) {
		this.queryTemplateProductSeq = queryTemplateProductSeq;
	}

	public String getQueryTemplateProductName() {
		return queryTemplateProductName;
	}

	public void setQueryTemplateProductName(String queryTemplateProductName) {
		this.queryTemplateProductName = queryTemplateProductName;
	}

	public String getParamTemplateProductSeq() {
		return paramTemplateProductSeq;
	}

	public void setParamTemplateProductSeq(String paramTemplateProductSeq) {
		this.paramTemplateProductSeq = paramTemplateProductSeq;
	}

	public String getParamTemplateProductName() {
		return paramTemplateProductName;
	}

	public void setParamTemplateProductName(String paramTemplateProductName) {
		this.paramTemplateProductName = paramTemplateProductName;
	}

	public String getParamTemplateProductWidth() {
		return paramTemplateProductWidth;
	}

	public void setParamTemplateProductWidth(String paramTemplateProductWidth) {
		this.paramTemplateProductWidth = paramTemplateProductWidth;
	}

	public String getParamTemplateProductHeight() {
		return paramTemplateProductHeight;
	}

	public void setParamTemplateProductHeight(String paramTemplateProductHeight) {
		this.paramTemplateProductHeight = paramTemplateProductHeight;
	}

	public String getParamTemplateProductContent() {
		return paramTemplateProductContent;
	}

	public void setParamTemplateProductContent(String paramTemplateProductContent) {
		this.paramTemplateProductContent = paramTemplateProductContent;
	}

	public String getParamTemplateAdCount() {
		return paramTemplateAdCount;
	}

	public void setParamTemplateAdCount(String paramTemplateAdCount) {
		this.paramTemplateAdCount = paramTemplateAdCount;
	}

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    public String getPageTotalNum() {
        return pageTotalNum;
    }

    public void setPageTotalNum(String pageTotalNum) {
        this.pageTotalNum = pageTotalNum;
    }

    public String getStartNum() {
        return startNum;
    }

    public void setStartNum(String startNum) {
        this.startNum = startNum;
    }

    public String getIframe() {
        return iframe;
    }

    public void setIframe(String iframe) {
        this.iframe = iframe;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

	public String[] getSel_template_ad_seq() {
		return sel_template_ad_seq;
	}

	public void setSel_template_ad_seq(String[] sel_template_ad_seq) {
		this.sel_template_ad_seq = sel_template_ad_seq;
	}

    public String[] getAdNums() {
        return adNums;
    }

    public void setAdNums(String[] adNums) {
        this.adNums = adNums;
    }

	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
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

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public int getDadCount() {
		return dadCount;
	}

	public String[] getSel_fram_dad_seq() {
		return sel_fram_dad_seq;
	}

	public void setSel_fram_dad_seq(String[] sel_fram_dad_seq) {
		this.sel_fram_dad_seq = sel_fram_dad_seq;
	}

	public void setDadCount(int dadCount) {
		this.dadCount = dadCount;
	}

	public String getParamAdPoolSeq() {
		return paramAdPoolSeq;
	}

	public String getParamAdPoolName() {
		return paramAdPoolName;
	}

	public void setParamAdPoolName(String paramAdPoolName) {
		this.paramAdPoolName = paramAdPoolName;
	}

	public void setParamAdPoolSeq(String paramAdPoolSeq) {
		this.paramAdPoolSeq = paramAdPoolSeq;
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

	public IPfbxPositionMenuService getPfbxPositionMenuService() {
	    return pfbxPositionMenuService;
	}

	public void setPfbxPositionMenuService(
		IPfbxPositionMenuService pfbxPositionMenuService) {
	    this.pfbxPositionMenuService = pfbxPositionMenuService;
	}

	public List<PfbxPositionMenu> getPfbxPositionMenuList() {
	    return pfbxPositionMenuList;
	}

	public void setPfbxPositionMenuList(List<PfbxPositionMenu> pfbxPositionMenuList) {
	    this.pfbxPositionMenuList = pfbxPositionMenuList;
	}

	public String getParamTemplateAdProperties() {
	    return paramTemplateAdProperties;
	}

	public void setParamTemplateAdProperties(String paramTemplateAdProperties) {
	    this.paramTemplateAdProperties = paramTemplateAdProperties;
	}

	public String getQueryTemplateProductType() {
		return queryTemplateProductType;
	}

	public void setQueryTemplateProductType(String queryTemplateProductType) {
		this.queryTemplateProductType = queryTemplateProductType;
	}

	public String getQueryTemplateProductSize() {
		return queryTemplateProductSize;
	}

	public void setQueryTemplateProductSize(String queryTemplateProductSize) {
		this.queryTemplateProductSize = queryTemplateProductSize;
	}

}