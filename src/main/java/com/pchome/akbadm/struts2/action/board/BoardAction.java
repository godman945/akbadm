package com.pchome.akbadm.struts2.action.board;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.PfpBoard;
import com.pchome.akbadm.db.service.board.IPfpBoardService;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.rmi.board.EnumBoardType;
import com.pchome.rmi.mailbox.EnumCategory;
import com.pchome.soft.util.DateValueUtil;

public class BoardAction extends BaseAction {

	private static final long serialVersionUID = -9173984080724343924L;

	private IPfpBoardService pfpBoardService;

	private int pageNo = 1;       			// 目前頁數
	private int pageSize = 20;     			// 每頁幾筆
	private int pageCount = 0;    			// 共幾頁
	private int totalCount = 0;   			// 共幾筆

	private List<PfpBoard> boardList;
    private EnumBoardType[] enumBoardType = EnumBoardType.values();
    private EnumCategory[] enumCategories = EnumCategory.values();
	private String startDate;
	private String endDate;

	private String boardType;				// 公告屬性
    private String category;                // 公告種類
	private String content;					// 公告內容
	private String urlAddress;				// 公告連結網址
	private String editor;					// 公告修改者
	private String author;					// 公告建立者
	private String boardId;					// 公告Id
	private PfpBoard boardDetail;			// 公告細節


	@Override
    public String execute() throws Exception {
		boardList = pfpBoardService.findSystemBoards(pageNo, pageSize);

		return SUCCESS;
	}


	public String boardAddAction() throws Exception {

		Date today = new Date();
		startDate = DateValueUtil.getInstance().dateToString(today);
		endDate = DateValueUtil.getInstance().dateToString(today);

		return SUCCESS;
	}

	public String boardSaveAction() throws Exception {

		//boardFactory.createBoard(EnumBoardType.SYSTEM, null, content, userId);

		Date today = new Date();

		PfpBoard board = new PfpBoard();
		board.setBoardType(boardType);
		board.setCategory(category);
		board.setCustomerInfoId("ALL");
		board.setContent(content);
		board.setStartDate(startDate);
		board.setEndDate(endDate);
        board.setDisplay("Y");
		board.setAuthor(author);
		board.setEditor(author);
		board.setUpdateDate(today);
		board.setCreateDate(today);
		if(StringUtils.isNotEmpty(urlAddress)){
			board.setHasUrl("Y");
			board.setUrlAddress(urlAddress);
		}else{
			board.setHasUrl("N");
		}
		pfpBoardService.addPfpBoard(board);

		return SUCCESS;
	}

	public String boardModifyAction() throws Exception {

		//boardSystem = boardFactory.findBoardSystem(boardId);
		//log.info(">> boardId  = "+boardId);
		boardDetail = pfpBoardService.findPfpBoard(boardId);

		return SUCCESS;
	}

	public String boardUpdateAction() throws Exception {

		//boardFactory.updateBoard(EnumBoardType.SYSTEM, boardId, content, userId);
		PfpBoard board = pfpBoardService.findPfpBoard(boardId);

		board.setBoardType(boardType);
        board.setCategory(category);
		board.setContent(content);
		board.setStartDate(startDate);
		board.setEndDate(endDate);
		board.setEditor(editor);
		board.setUpdateDate(new Date());
		if(StringUtils.isNotEmpty(urlAddress)){
			board.setHasUrl("Y");
			board.setUrlAddress(urlAddress);
		}else{
			board.setHasUrl("N");
			board.setUrlAddress("");
		}

		pfpBoardService.updatePfpBoard(board);

		return SUCCESS;
	}

	public String boardDeleteAction() throws Exception {

		pfpBoardService.deletePfpBoard(boardId);

		return SUCCESS;
	}

    public EnumBoardType[] getEnumBoardType() {
        return enumBoardType;
    }

    public EnumCategory[] getEnumCategories() {
        return enumCategories;
    }

	public void setPfpBoardService(IPfpBoardService pfpBoardService) {
		this.pfpBoardService = pfpBoardService;
	}

	public List<PfpBoard> getBoardList() {
		return boardList;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public void setBoardType(String boardType) {
		this.boardType = boardType;
	}

    public void setCategory(String category) {
        this.category = category;
    }

	public void setContent(String content) {
		this.content = content;
	}

	public void setUrlAddress(String urlAddress) {
		this.urlAddress = urlAddress;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}

	public PfpBoard getBoardDetail() {
		return boardDetail;
	}

}
