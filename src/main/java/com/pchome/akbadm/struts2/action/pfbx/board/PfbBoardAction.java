package com.pchome.akbadm.struts2.action.pfbx.board;

import com.pchome.akbadm.db.pojo.PfbxBoard;
import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.service.pfbx.account.IPfbxCustomerInfoService;
import com.pchome.akbadm.db.service.pfbx.board.IPfbxBoardService;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.enumerate.pfbx.account.EnumPfbAccountStatus;
import com.pchome.rmi.board.EnumPfbBoardType;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PfbBoardAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IPfbxBoardService pfbxBoardService;
	private IPfbxCustomerInfoService pfbxCustomerInfoService;

    private EnumPfbBoardType[] enumBoardType = EnumPfbBoardType.values();

    //輸入參數
	private String boardType;
	private String content;
	private String pfbCustomerInfoId;
	private String urlAddress;
	private String startDate;
	private String endDate;

	//查詢指標
	private String boardId;
	private String paramPfbCustomerInfoId;

	//查詢結果
	private List<PfbxBoard> boardList;
	private PfbxBoard board;
	private String jsonData;

	//init
	private Map<String, String> pfbxCustomerInfoMap = new HashMap<String, String>(); //id mapping name

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private void initPfbxCustomerInfoMap() {

		try {
			
			List<PfbxCustomerInfo> list = pfbxCustomerInfoService.getPfbxCustomerInfoByCondition(new HashMap<String, String>());
			for (int i=0; i<list.size(); i++) {
				PfbxCustomerInfo pojo = list.get(i);
				if (pojo.getCategory().equals("2")) {
					pfbxCustomerInfoMap.put(pojo.getCustomerInfoId(), pojo.getCompanyName());
				} else {
					pfbxCustomerInfoMap.put(pojo.getCustomerInfoId(), pojo.getContactName());
				}
				
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void initPfbxCustomerInfoAddMap() {

		try {

			HashMap<String, String> conditionMap = new HashMap<String, String>();
			String status = "'" + EnumPfbAccountStatus.START.getStatus() + "','" + EnumPfbAccountStatus.CLOSE.getStatus() + "','" + EnumPfbAccountStatus.STOP.getStatus() + "'";
			conditionMap.put("status", status);
			
			List<PfbxCustomerInfo> list = pfbxCustomerInfoService.getPfbxCustomerInfoByCondition(conditionMap);
			for (int i=0; i<list.size(); i++) {
				PfbxCustomerInfo pojo = list.get(i);
				if (pojo.getCategory().equals("2")) {
					pfbxCustomerInfoMap.put(pojo.getCustomerInfoId(), pojo.getCompanyName());
				} else {
					pfbxCustomerInfoMap.put(pojo.getCustomerInfoId(), pojo.getContactName());
				}
				
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
    public String execute() throws Exception {

    	initPfbxCustomerInfoMap();

		boardList = pfbxBoardService.findPfbxBoard(new HashMap<String, String>());

		return SUCCESS;
	}

	public String add() throws Exception {

		initPfbxCustomerInfoAddMap();

		return SUCCESS;
	}

	public String doAdd() throws Exception {

		List<PfbxBoard> list = new ArrayList<PfbxBoard>();
		list = getAddPfbxBoardList();

		pfbxBoardService.saveOrUpdateAll(list);

		return SUCCESS;
	}

	private List<PfbxBoard> getAddPfbxBoardList() throws Exception{
		
		List<PfbxBoard> list = new ArrayList<PfbxBoard>();
		
		if(boardType.equals(EnumPfbBoardType.ALL.getType()) || StringUtils.isNotEmpty(pfbCustomerInfoId)){
			PfbxBoard board = new PfbxBoard();
			board.setBoardType(boardType);
			board.setBoardContent(content);
			if (boardType.equals(EnumPfbBoardType.ALL.getType())) {
				board.setPfbxCustomerInfoId(null);
			} else {
				board.setPfbxCustomerInfoId(pfbCustomerInfoId);
			}
			
			board.setStartDate(sdf.parse(startDate));
			board.setEndDate(sdf.parse(endDate));
			if(StringUtils.isNotEmpty(urlAddress)){
				board.setHasUrl("y");
				board.setUrlAddress(urlAddress);
			}else{
				board.setHasUrl("n");
				board.setUrlAddress(null);
			}
			board.setCreateDate(new Date());
			
			list.add(board);
		} else {
			HashMap<String, String> conditionMap = new HashMap<String, String>();
			String status = "'" + EnumPfbAccountStatus.START.getStatus() + "','" + EnumPfbAccountStatus.CLOSE.getStatus() + "','" + EnumPfbAccountStatus.STOP.getStatus() + "'";
			conditionMap.put("status", status);
			
			List<PfbxCustomerInfo> pfbxCustomerInfolist = pfbxCustomerInfoService.getPfbxCustomerInfoByCondition(conditionMap);
			for (int i=0; i<pfbxCustomerInfolist.size(); i++) {
				PfbxCustomerInfo pojo = pfbxCustomerInfolist.get(i);
				
				PfbxBoard board = new PfbxBoard();
				board.setBoardType(boardType);
				board.setBoardContent(content);
				board.setPfbxCustomerInfoId(pojo.getCustomerInfoId());
				board.setStartDate(sdf.parse(startDate));
				board.setEndDate(sdf.parse(endDate));
				if(StringUtils.isNotEmpty(urlAddress)){
					board.setHasUrl("y");
					board.setUrlAddress(urlAddress);
				}else{
					board.setHasUrl("n");
					board.setUrlAddress(null);
				}
				board.setCreateDate(new Date());
				
				list.add(board);
			}
		}
		
		
		return list;
	}
	
	public String update() throws Exception {

		log.info(">>> boardId  = " + boardId);

    	Map<String, String> conditionsMap = new HashMap<String, String>();
    	conditionsMap.put("boardId", boardId);

		board = pfbxBoardService.findPfbxBoard(conditionsMap).get(0);
		boardId = board.getBoardId().toString();
		boardType = board.getBoardType();
		content = board.getBoardContent();
		pfbCustomerInfoId = board.getPfbxCustomerInfoId();
		urlAddress = board.getUrlAddress();
		startDate = sdf.format(board.getStartDate());
		endDate = sdf.format(board.getEndDate());

		initPfbxCustomerInfoMap();

		return SUCCESS;
	}

	public String doUpdate() throws Exception {

		log.info(">>> boardId  = " + boardId);

    	Map<String, String> conditionsMap = new HashMap<String, String>();
    	conditionsMap.put("boardId", boardId);

		board = pfbxBoardService.findPfbxBoard(conditionsMap).get(0);

		board.setBoardType(boardType);
		board.setBoardContent(content);
		if (boardType.equals(EnumPfbBoardType.ALL.getType())) {
			board.setPfbxCustomerInfoId(null);
		} else {
			board.setPfbxCustomerInfoId(pfbCustomerInfoId);
		}
		board.setStartDate(sdf.parse(startDate));
		board.setEndDate(sdf.parse(endDate));
		if(StringUtils.isNotEmpty(urlAddress)){
			board.setHasUrl("y");
			board.setUrlAddress(urlAddress);
		}else{
			board.setHasUrl("n");
			board.setUrlAddress(null);
		}
		board.setUpdateDate(new Date());

		pfbxBoardService.saveOrUpdate(board);

		return SUCCESS;
	}

	public String doDelete() throws Exception {

		pfbxBoardService.deletePfbxBoardById(boardId);

		return SUCCESS;
	}

    public EnumPfbBoardType[] getEnumBoardType() {
        return enumBoardType;
    }

	public void setPfbxBoardService(IPfbxBoardService pfbxBoardService) {
		this.pfbxBoardService = pfbxBoardService;
	}

	public void setPfbxCustomerInfoService(IPfbxCustomerInfoService pfbxCustomerInfoService) {
		this.pfbxCustomerInfoService = pfbxCustomerInfoService;
	}

	public List<PfbxBoard> getBoardList() {
		return boardList;
	}

	public PfbxBoard getBoard() {
		return board;
	}

	public String getBoardType() {
		return boardType;
	}

	public void setBoardType(String boardType) {
		this.boardType = boardType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPfbCustomerInfoId() {
		return pfbCustomerInfoId;
	}

	public void setPfbCustomerInfoId(String pfbCustomerInfoId) {
		this.pfbCustomerInfoId = pfbCustomerInfoId;
	}

	public String getUrlAddress() {
		return urlAddress;
	}

	public void setUrlAddress(String urlAddress) {
		this.urlAddress = urlAddress;
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

	public String getBoardId() {
		return boardId;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}

	public Map<String, String> getPfbCustomerInfoMap() {
		return pfbxCustomerInfoMap;
	}

	public String getParamPfbCustomerInfoId() {
		return paramPfbCustomerInfoId;
	}

	public void setParamPfbCustomerInfoId(String paramPfbCustomerInfoId) {
		this.paramPfbCustomerInfoId = paramPfbCustomerInfoId;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}
}
