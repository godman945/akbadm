package com.pchome.akbadm.struts2.action.template;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.json.annotations.JSON;

import com.pchome.akbadm.db.pojo.AdmAdPool;
import com.pchome.akbadm.db.pojo.AdmDefineAd;
import com.pchome.akbadm.db.pojo.AdmDefineAdType;
import com.pchome.akbadm.db.pojo.PfbxSize;
import com.pchome.akbadm.db.service.pfbx.IPfbSizeService;
import com.pchome.akbadm.db.service.template.IAdPoolService;
import com.pchome.akbadm.db.service.template.IDefineAdService;
import com.pchome.akbadm.db.service.template.IDefineAdTypeService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.rmi.sequence.EnumSequenceTableName;
import com.pchome.rmi.sequence.ISequenceProvider;

public class DefineAdAction extends BaseCookieAction {
    private static final long serialVersionUID = -5902321691788984204L;

    private ISequenceProvider sequenceProvider;

    private IAdPoolService adPoolService;
	private IDefineAdService defineAdService;
	private IDefineAdTypeService defineAdTypeService;
	private IPfbSizeService pfbSizeService;
	
    private String admAddata;

	private String message = "";

	//查詢條件(查詢頁面用)
	private String queryAdPoolSeq;      // 廣告來源序號
	private String queryDefineAdSeq;	// 廣告定義編號
	private String queryDefineAdName;	// 廣告定義名稱

	//查詢結果(查詢頁面用)
	private List<AdmDefineAd> defineAdList;
	private List<PfbxSize> pfbxSizeList;

	//指定template product Id(查詢頁面點修改或刪除時使用)
	private String defineAdSeq;
    private AdmDefineAd admDefineAd;
	private AdmDefineAdType admDefineAdType;

	//元件參數(新增及修改頁面用)
    private String paramAdPoolSeq;          // 廣告來源序號
	private String paramDefineAdSeq;		// 廣告定義序號
	private String paramDefineAdId;			// 廣告定義代碼
	private String paramDefineAdName;		// 廣告定義名稱
	private String paramDefineAdWidth;		// 廣告定義寬度
	private String paramDefineAdHeight;		// 廣告定義高度
	private String paramDefineAdType;		// 廣告定義類型
	private String paramDefineAdContent;	// 廣告定義內容

	//AD Type 下拉選單
	private List<AdmDefineAdType> defineAdTypeList;

	@Override
    public String execute() throws Exception {
		defineAdList = defineAdService.loadAll();
		return SUCCESS;
	}

	public String query() throws Exception {
		defineAdList = defineAdService.getDefineAdByCondition(queryDefineAdSeq, queryDefineAdName, queryAdPoolSeq);
		return SUCCESS;
	}

    public String defineAdList() throws Exception {
        if(StringUtils.isNotBlank(paramAdPoolSeq)) {
            defineAdList = defineAdService.getDefineAdByCondition(null, null, paramAdPoolSeq);
        }

        return SUCCESS;
    }

    public String goAddPage() throws Exception {
	defineAdTypeList = defineAdTypeService.loadAll();
	pfbxSizeList = pfbSizeService.loadAll();
	return SUCCESS;
    }

    public String goUpdatePage() throws Exception {
        if (!checkDad()) {
            return NONE;
        }
        
        defineAdTypeList = defineAdTypeService.loadAll();
        pfbxSizeList = pfbSizeService.loadAll();
        return SUCCESS;
    }

	public String doAdd() throws Exception {
        log.info("paramAdPoolSeq = " + paramAdPoolSeq);

        if (!checkParameter()) {
            return INPUT;
        }

        AdmAdPool admAdPool = adPoolService.getAdPoolBySeq(paramAdPoolSeq);
        if (admAdPool == null) {
            message = "廣告來源序號錯誤";
            log.info("admAdPool " + paramAdPoolSeq + " = null");
            return NONE;
        }

        if (!checkType()) {
            return NONE;
        }

		// 廣告樣板序號
		EnumSequenceTableName seqTbl_T = EnumSequenceTableName.ADM_DEFINE_AD;
		String tblShortName_T = seqTbl_T.getCharName();
		defineAdSeq = sequenceProvider.getSequenceID(seqTbl_T, "_");

		// 設定廣告樣版內容存放位置
		String defineAdFilePath = admAddata + tblShortName_T + File.separator + defineAdSeq + ".def";
        FileUtils.writeStringToFile(new File(defineAdFilePath), paramDefineAdContent, CHARSET);

        // write dad
		AdmDefineAd admDefineAd = new AdmDefineAd();
		admDefineAd.setDefineAdSeq(defineAdSeq);
		admDefineAd.setDefineAdId(paramDefineAdId);
		admDefineAd.setDefineAdName(paramDefineAdName);
		admDefineAd.setDefineAdWidth(paramDefineAdWidth);
		admDefineAd.setDefineAdHeight(paramDefineAdHeight);
		admDefineAd.setDefineAdFile(defineAdFilePath);
		admDefineAd.setAdPoolSeq(paramAdPoolSeq);

        admDefineAd.setAdmDefineAdType(admDefineAdType);
        admDefineAdType.getAdmDefineAds().add(admDefineAd);

		defineAdService.save(admDefineAd);

		// 修改adpool 中，廣告定義的數量
        List<AdmDefineAd> defineAdList = defineAdService.getDefineAdByCondition("", "", paramAdPoolSeq);
		admAdPool.setDadCount(defineAdList.size() + 1);
		adPoolService.update(admAdPool);

		return SUCCESS;
	}

    public String doUpdate() throws Exception {
        log.info("paramAdPoolSeq = " + paramAdPoolSeq);
        log.info("paramDefineAdSeq = " + paramDefineAdSeq);

        if (!checkParameter()) {
            return INPUT;
        }

        if (!checkDad()) {
            return NONE;
        }

        if (!admDefineAd.getAdPoolSeq().equals(paramAdPoolSeq)) {
            message = "廣告來源序號錯誤";
            log.info("adPoolSeq " + admDefineAd.getAdPoolSeq() + " != " + paramAdPoolSeq);
            return NONE;
        }

        if (!checkType()) {
            return NONE;
        }

        if (!checkRelateDad()) {
            return NONE;
        }

        // write file
        FileUtils.writeStringToFile(new File(admDefineAd.getDefineAdFile()), paramDefineAdContent, CHARSET);

        // write dad
        admDefineAd.setDefineAdId(paramDefineAdId);
        admDefineAd.setDefineAdName(paramDefineAdName);
        admDefineAd.setDefineAdWidth(paramDefineAdWidth);
        admDefineAd.setDefineAdHeight(paramDefineAdHeight);

        admDefineAd.setAdmDefineAdType(admDefineAdType);
        admDefineAdType.getAdmDefineAds().add(admDefineAd);

        defineAdService.update(admDefineAd);

        return SUCCESS;
    }

	public String doDelete() throws Exception {
        log.info("paramDefineAdSeq = " + paramDefineAdSeq);

        if (!checkDad()) {
            return NONE;
        }

        if (!checkRelateDad()) {
            return NONE;
        }

        // delete dad
        defineAdService.delete(admDefineAd);

		return SUCCESS;
	}

    private boolean checkParameter() {
        if (StringUtils.isBlank(paramAdPoolSeq)) {
            message = "請輸入廣告來源序號！";
            return false;
        } else {
            paramAdPoolSeq = paramAdPoolSeq.trim();
            if (paramAdPoolSeq.length() > 20) {
                message = "廣告來源序號不可超過 20 字元！";
                return false;
            }
        }

        if (StringUtils.isBlank(paramDefineAdId)) {
            message = "請輸入廣告定義代碼！";
            return false;
        } else {
            paramDefineAdId = paramDefineAdId.trim();
            if (paramDefineAdId.length() > 30) {
                message = "廣告定義代碼不可超過 30 字元！";
                return false;
            }
        }

        if (StringUtils.isBlank(paramDefineAdName)) {
            message = "請輸入廣告定義名稱！";
            return false;
        } else {
            paramDefineAdName = paramDefineAdName.trim();
            if (paramDefineAdName.length() > 50) {
                message = "廣告定義名稱不可超過 50 字元！";
                return false;
            }
        }

        if (StringUtils.isBlank(paramDefineAdType) || !StringUtils.isNumeric(paramDefineAdType)) {
            message = "請選擇廣告類型代碼！";
            return false;
        }

        if (StringUtils.isBlank(paramDefineAdWidth)) {
            message = "請輸入廣告定義寬度！";
            return false;
        } else {
            paramDefineAdWidth = paramDefineAdWidth.trim();
            try {
                if (paramDefineAdWidth.length() > 4) {
                    message = "廣告定義寬度不可超過 4 字元！";
                    return false;
                } else if (Integer.parseInt(paramDefineAdWidth) < 0) {
                    message = "廣告定義寬度不可小於0";
                    return false;
                } else if (Integer.parseInt(paramDefineAdWidth) > 3000) {
                    message = "廣告定義寬度不可大於3000";
                    return false;
                }
            } catch(Exception ex) {
                message = "廣告定義寬度輸入的格式錯誤";
                return false;
            }
        }

        if (StringUtils.isBlank(paramDefineAdHeight)) {
            message = "請輸入廣告定義高度！";
            return false;
        } else {
            paramDefineAdHeight = paramDefineAdHeight.trim();
            try {
                if (paramDefineAdHeight.length() > 4) {
                    message = "廣告定義高度不可超過 4 字元！";
                    return false;
                } else if (Integer.parseInt(paramDefineAdHeight) < 0) {
                    message = "廣告定義高度不可小於0";
                    return false;
                } else if (Integer.parseInt(paramDefineAdHeight) > 2000) {
                    message = "廣告定義高度不可大於2000";
                    return false;
                }
            } catch(Exception ex) {
                message = "廣告定義高度輸入的格式錯誤";
                return false;
            }
        }

        if (StringUtils.isBlank(paramDefineAdContent)) {
            message = "請輸入廣告定義內容！";
            return false;
        }

        return true;
    }

    private boolean checkDad() {
        if (StringUtils.isBlank(paramDefineAdSeq)) {
            message = "廣告定義序號錯誤";
            return false;
        }

        admDefineAd = defineAdService.get(paramDefineAdSeq);
        if (admDefineAd == null) {
            message = "廣告定義序號錯誤";
            log.info("admDefineAd " + paramDefineAdSeq + " = null");
            return false;
        }

        return true;
    }

    private boolean checkType() {
        admDefineAdType = defineAdTypeService.get(Integer.parseInt(paramDefineAdType));
        if (admDefineAdType == null) {
            message = "廣告類型序號錯誤";
            log.info("admDefineAdType " + paramDefineAdType + " = null");
            return false;
        }

        return true;
    }

    private boolean checkRelateDad() {
        if (admDefineAd.getAdmRelateTadDads().size() > 0) {
            message = "廣告定義使用中";
            log.info("admRelateTadDads " + admDefineAd.getAdmRelateTadDads().size());
            return false;
        }

        return true;
    }

    public void setSequenceProvider(ISequenceProvider sequenceProvider) {
        this.sequenceProvider = sequenceProvider;
    }

    public void setAdPoolService(IAdPoolService adPoolService) {
        this.adPoolService = adPoolService;
    }

    public void setDefineAdService(IDefineAdService defineAdService) {
        this.defineAdService = defineAdService;
    }

    public void setDefineAdTypeService(IDefineAdTypeService defineAdTypeService) {
        this.defineAdTypeService = defineAdTypeService;
    }

    public void setAdmAddata(String admAddata) {
        this.admAddata = admAddata;
    }

	public String getMessage() {
		return message;
	}

    @JSON(serialize = false)
	public List<AdmDefineAd> getDefineAdList() {
		return defineAdList;
	}

	public void setDefineAdList(List<AdmDefineAd>defineAdList) {
		this.defineAdList = defineAdList;
	}

    @JSON(serialize = false)
	public String getDefineAdSeq() {
		return defineAdSeq;
	}

	public void setDefineAdSeq(String defineAdSeq) {
		this.defineAdSeq = defineAdSeq;
	}

    @JSON(serialize = false)
    public AdmDefineAd getAdmDefineAd() {
        return admDefineAd;
    }

    @JSON(serialize = false)
    public String getQueryAdPoolSeq() {
        return queryAdPoolSeq;
    }

    public void setQueryAdPoolSeq(String queryAdPoolSeq) {
        this.queryAdPoolSeq = queryAdPoolSeq;
    }

    @JSON(serialize = false)
	public String getQueryDefineAdSeq() {
		return queryDefineAdSeq;
	}

	public void setQueryDefineAdSeq(String queryDefineAdSeq) {
		this.queryDefineAdSeq = queryDefineAdSeq;
	}

    @JSON(serialize = false)
	public String getQueryDefineAdName() {
		return queryDefineAdName;
	}

	public void setQueryDefineAdName(String queryDefineAdName) {
		this.queryDefineAdName = queryDefineAdName;
	}

    @JSON(serialize = false)
	public String getParamDefineAdSeq() {
		return paramDefineAdSeq;
	}

	public void setParamDefineAdSeq(String paramDefineAdSeq) {
		this.paramDefineAdSeq = paramDefineAdSeq;
	}

    @JSON(serialize = false)
	public String getParamDefineAdId() {
		return paramDefineAdId;
	}

	public void setParamDefineAdId(String paramDefineAdId) {
		this.paramDefineAdId = paramDefineAdId;
	}

    @JSON(serialize = false)
	public String getParamDefineAdName() {
		return paramDefineAdName;
	}

	public void setParamDefineAdName(String paramDefineAdName) {
		this.paramDefineAdName = paramDefineAdName;
	}

    @JSON(serialize = false)
	public String getParamDefineAdWidth() {
		return paramDefineAdWidth;
	}

	public void setParamDefineAdWidth(String paramDefineAdWidth) {
		this.paramDefineAdWidth = paramDefineAdWidth;
	}

    @JSON(serialize = false)
	public String getParamDefineAdHeight() {
		return paramDefineAdHeight;
	}

	public void setParamDefineAdHeight(String paramDefineAdHeight) {
		this.paramDefineAdHeight = paramDefineAdHeight;
	}

    @JSON(serialize = false)
	public String getParamDefineAdType() {
		return paramDefineAdType;
	}

	public void setParamDefineAdType(String paramDefineAdType) {
		this.paramDefineAdType = paramDefineAdType;
	}

    @JSON(serialize = false)
	public String getParamDefineAdContent() {
		return paramDefineAdContent;
	}

	public void setParamDefineAdContent(String paramDefineAdContent) {
		this.paramDefineAdContent = paramDefineAdContent;
	}

    @JSON(serialize = false)
	public String getParamAdPoolSeq() {
		return paramAdPoolSeq;
	}

	public void setParamAdPoolSeq(String paramAdPoolSeq) {
		this.paramAdPoolSeq = paramAdPoolSeq;
	}

	@JSON(serialize = false)
	public List<AdmDefineAdType> getDefineAdTypeList() {
		return defineAdTypeList;
	}
	@JSON(serialize = false)
	public IPfbSizeService getPfbSizeService() {
	    return pfbSizeService;
	}

	public void setPfbSizeService(IPfbSizeService pfbSizeService) {
	    this.pfbSizeService = pfbSizeService;
	}
	@JSON(serialize = false)
	public List<PfbxSize> getPfbxSizeList() {
	    return pfbxSizeList;
	}

	public void setPfbxSizeList(List<PfbxSize> pfbxSizeList) {
	    this.pfbxSizeList = pfbxSizeList;
	}
	
	
}
