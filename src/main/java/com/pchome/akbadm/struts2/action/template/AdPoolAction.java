package com.pchome.akbadm.struts2.action.template;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.pojo.AdmAdPool;
import com.pchome.akbadm.db.pojo.AdmDefineAd;
import com.pchome.akbadm.db.service.template.IAdPoolService;
import com.pchome.akbadm.db.service.template.IDefineAdService;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.rmi.sequence.EnumSequenceTableName;
import com.pchome.rmi.sequence.ISequenceProvider;

public class AdPoolAction extends BaseAction {
    private static final long serialVersionUID = -4718852597152568182L;

    private ISequenceProvider sequenceProvider;

	private IAdPoolService adPoolService;
	private IDefineAdService defineAdService;

	private String message = "";

	//查詢條件(查詢頁面用)
	private String queryAdPoolSeq;		// 廣告樣板編號
	private String queryAdPoolName;		// 廣告樣板名稱
	private String queryDiffComapny;	// 區分廠商

	//廣告樣板序號(查詢頁面點修改或刪除時使用)
	private String adPoolSeq;
    private AdmAdPool admAdPool;

	//元件參數(新增及修改頁面用)
	private String paramAdPoolSeq;		// Ad Pool 序號
	private String paramAdPoolName;		// Ad pool 名稱
	private String paramDiffCompany;	// 區分廠商
	private String paramDadCount;		// 廣告定義數量

	//Define AD List
	private List<AdmDefineAd> defineAdList;
	private List<AdmAdPool> adPoolList;

	@Override
    public String execute() throws Exception {
		adPoolList = adPoolService.loadAll();
		return SUCCESS;
	}

	public String query() throws Exception {
		adPoolList = adPoolService.getAdPoolByCondition(queryAdPoolSeq, queryAdPoolName, queryDiffComapny);
		return SUCCESS;
	}

	public String goAddPage() throws Exception {
		return SUCCESS;
	}

    public String goUpdatePage() throws Exception {
        if (!checkPool()) {
            return NONE;
        }

        return SUCCESS;
    }

	public String detailPage() throws Exception {
		return SUCCESS;
	}

    @Transactional
	public String doAdd() throws Exception {
        if (!checkParameter()) {
            return INPUT;
        }

        paramAdPoolSeq = sequenceProvider.getSequenceID(EnumSequenceTableName.ADM_AD_POOL, "_");

		AdmAdPool admAdPool = new AdmAdPool();
		admAdPool.setAdPoolSeq(paramAdPoolSeq);
		admAdPool.setAdPoolName(paramAdPoolName);
		admAdPool.setDiffCompany(paramDiffCompany);
		admAdPool.setDadCount(0);

		adPoolService.save(admAdPool);

		return SUCCESS;
	}

    @Transactional
    public String doUpdate() throws Exception {
        log.info("paramAdPoolSeq = " + paramAdPoolSeq);

        if (!checkParameter()) {
            return INPUT;
        }

        if (!checkPool()) {
            return NONE;
        }

        // update pool
        admAdPool.setAdPoolName(paramAdPoolName);
        admAdPool.setDiffCompany(paramDiffCompany);
        admAdPool.setDadCount(defineAdService.getDefineAdByCondition(null, null, paramAdPoolSeq).size());

        adPoolService.update(admAdPool);

        return SUCCESS;
    }

    @Transactional
	public String doDelete() throws Exception {
        log.info("paramAdPoolSeq = " + paramAdPoolSeq);

        if (!checkPool()) {
            return NONE;
        }

        // delete dad
        int count = defineAdService.deleteDefineAdByPoolSeq(paramAdPoolSeq);
        log.info("delete definaAd " + count);

        // update pool
        adPoolService.delete(admAdPool);

        return SUCCESS;
	}

    private boolean checkParameter() {
        if (StringUtils.isBlank(paramAdPoolName)) {
            message = "請輸入資料來源名稱！";
            return false;
        } else {
            paramAdPoolName = paramAdPoolName.trim();
            if (paramAdPoolName.length() > 20) {
                message = "資料來源名稱不可超過 20 字元！";
                return false;
            }
        }

        if (StringUtils.isBlank(paramDiffCompany)) {
            message = "請選擇是否區分廠商！";
            return false;
        } else {
            paramDiffCompany = paramDiffCompany.trim();
        }

        return true;
    }

    private boolean checkPool() {
        if (StringUtils.isBlank(paramAdPoolSeq)) {
            message = "廣告來源序號錯誤";
            return false;
        }

        admAdPool = adPoolService.get(paramAdPoolSeq);
        if (admAdPool == null) {
            message = "廣告來源序號錯誤";
            log.info("admAdPool " + paramAdPoolSeq + " = null");
            return false;
        }

        if (admAdPool.getAdmTemplateAds().size() > 0) {
            message = "資料來源使用中";
            log.info("admTemplateAds " + admAdPool.getAdmTemplateAds().size());
            return false;
        }

        if (admAdPool.getAdmRelateTadDads().size() > 0) {
            message = "資料來源使用中";
            log.info("admRelateTadDads " + admAdPool.getAdmRelateTadDads().size());
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

	public String getMessage() {
		return message;
	}

	public String getQueryAdPoolSeq() {
		return queryAdPoolSeq;
	}

	public void setQueryAdPoolSeq(String queryAdPoolSeq) {
		this.queryAdPoolSeq = queryAdPoolSeq;
	}

	public String getQueryAdPoolName() {
		return queryAdPoolName;
	}

	public void setQueryAdPoolName(String queryAdPoolName) {
		this.queryAdPoolName = queryAdPoolName;
	}

	public String getQueryDiffComapny() {
		return queryDiffComapny;
	}

	public void setQueryDiffComapny(String queryDiffComapny) {
		this.queryDiffComapny = queryDiffComapny;
	}

    public AdmAdPool getAdmAdPool() {
        return admAdPool;
    }

	public List<AdmAdPool> getAdPoolList() {
		return adPoolList;
	}

	public List<AdmDefineAd> getDefineAdList() {
		return defineAdList;
	}

	public String getTemplateAdSeq() {
		return adPoolSeq;
	}

	public String getParamAdPoolSeq() {
		return paramAdPoolSeq;
	}

	public void setParamAdPoolSeq(String paramAdPoolSeq) {
		this.paramAdPoolSeq = paramAdPoolSeq;
	}

	public String getParamAdPoolName() {
		return paramAdPoolName;
	}

	public void setParamAdPoolName(String paramAdPoolName) {
		this.paramAdPoolName = paramAdPoolName;
	}

	public String getParamDiffCompany() {
		return paramDiffCompany;
	}

	public void setParamDiffCompany(String paramDiffCompany) {
		this.paramDiffCompany = paramDiffCompany;
	}

	public String getParamDadCount() {
		return paramDadCount;
	}

	public void setParamDadCount(String paramDadCount) {
		this.paramDadCount = paramDadCount;
	}
}
