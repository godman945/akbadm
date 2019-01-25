package com.pchome.akbadm.struts2.action.check;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.PfpIllegalKeyword;
import com.pchome.akbadm.db.service.check.PfpIllegalKeywordService;
import com.pchome.akbadm.struts2.BaseAction;

public class IllegalKeywordAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private PfpIllegalKeywordService pfpIllegalKeywordService;

	private List<PfpIllegalKeyword> illegalKeywordList;

	private String message;

	//頁面操作參數
	private String queryString; //查詢字串
	private String addContent; //新增內容
	private String targetSeq; //要修改或刪除的seq
	private String modifyContent; //要修改的內容

	//換頁參數
	private int pageNo = 1; //目前頁數
	private int pageSize = 10; //每頁幾筆
	private int pageCount = 0; //共幾頁
	private int totalCount = 0; //共幾筆

	public String execute() throws Exception{

		this.init();
		
		return SUCCESS;
	}

	public String doAdd() throws Exception{

		if (StringUtils.isEmpty(this.addContent)) {
			this.message = "請輸入要新增的禁用字詞！";
		} else {
			Date now = new Date();
			PfpIllegalKeyword vo = new PfpIllegalKeyword();
			vo.setContent(this.addContent);
			vo.setCreateDate(now);
			vo.setUpdateDate(now);
			pfpIllegalKeywordService.save(vo);
			this.message = "新增成功！";
		}

		this.init();

		return SUCCESS;
	}

	public String doUpdate() throws Exception{

		if (StringUtils.isEmpty(this.targetSeq) || StringUtils.isEmpty(this.modifyContent)) {
			this.message = "請輸入要修改的禁用字詞！";
		} else {
			pfpIllegalKeywordService.updateIllegalKeywordBySeq(this.targetSeq, this.modifyContent);
			this.message = "修改成功！";
		}

		this.init();

		return SUCCESS;
	}

	public String doDelete() throws Exception{

		if (StringUtils.isEmpty(this.targetSeq)) {
			this.message = "請選擇要刪除的禁用字詞！";
		} else {
			pfpIllegalKeywordService.deleteIllegalKeywordBySeq(this.targetSeq);
			this.message = "刪除成功！";
		}

		this.init();

		return SUCCESS;
	}

	private void init() throws Exception {

		this.totalCount = pfpIllegalKeywordService.getIllegalKeywordCountByCondition(queryString);
		this.pageCount = (int) Math.ceil(((float)totalCount / pageSize));

		//數量有變時，更新目前所在頁碼
		if (totalCount <= (pageNo-1)*pageSize) {
			this.pageNo = 1;
		}

		this.illegalKeywordList = pfpIllegalKeywordService.getIllegalKeywordByCondition(queryString, pageNo, pageSize);
	}

	public void setPfpIllegalKeywordService(
			PfpIllegalKeywordService pfpIllegalKeywordService) {
		this.pfpIllegalKeywordService = pfpIllegalKeywordService;
	}

	public List<PfpIllegalKeyword> getIllegalKeywordList() {
		return illegalKeywordList;
	}

	public void setIllegalKeywordList(List<PfpIllegalKeyword> illegalKeywordList) {
		this.illegalKeywordList = illegalKeywordList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getAddContent() {
		return addContent;
	}

	public void setAddContent(String addContent) {
		this.addContent = addContent;
	}

	public String getTargetSeq() {
		return targetSeq;
	}

	public void setTargetSeq(String targetSeq) {
		this.targetSeq = targetSeq;
	}

	public String getModifyContent() {
		return modifyContent;
	}

	public void setModifyContent(String modifyContent) {
		this.modifyContent = modifyContent;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
