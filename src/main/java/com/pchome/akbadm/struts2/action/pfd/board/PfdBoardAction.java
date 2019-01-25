package com.pchome.akbadm.struts2.action.pfd.board;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.pchome.akbadm.db.pojo.PfdBoard;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.pojo.PfdUser;
import com.pchome.akbadm.db.service.pfd.account.IPfdAccountService;
import com.pchome.akbadm.db.service.pfd.board.IPfdBoardService;
import com.pchome.akbadm.db.service.pfd.user.IPfdUserService;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.rmi.board.EnumPfdBoardType;

public class PfdBoardAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IPfdBoardService pfdBoardService;
	private IPfdAccountService pfdAccountService;
	private IPfdUserService pfdUserService;

    private EnumPfdBoardType[] enumBoardType = EnumPfdBoardType.values();

    //輸入參數
	private String boardType;
	private String content;
	private String pfdCustomerInfoId;
	private String pfdUserId;
	private String urlAddress;
	private String startDate;
	private String endDate;

	//查詢指標
	private String boardId;
	private String paramPfdCustomerInfoId;

	//查詢結果
	private List<PfdBoard> boardList;
	private PfdBoard board;
	private String jsonData;

	//init
	private Map<String, String> pfdCustomerInfoMap = new HashMap<String, String>(); //id mapping name
	private Map<String, String> pfdUserMap = new HashMap<String, String>(); //id mapping name

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private void initPfdCustomerInfoMap() {

		try {

			List<PfdCustomerInfo> list = pfdAccountService.getPfdCustomerInfoByCondition(new HashMap<String, String>());
			for (int i=0; i<list.size(); i++) {
				PfdCustomerInfo pojo = list.get(i);
				pfdCustomerInfoMap.put(pojo.getCustomerInfoId(), pojo.getCompanyName());
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void initPfdUserMap() {

		try {

			List<PfdUser> list = pfdUserService.getPfdUserByCondition(new HashMap<String, String>());
			for (int i=0; i<list.size(); i++) {
				PfdUser pojo = list.get(i);
				pfdUserMap.put(pojo.getUserId(), pojo.getUserName());
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

    public String execute() throws Exception {

    	initPfdCustomerInfoMap();

    	initPfdUserMap();

    	Map<String, String> conditionsMap = new HashMap<String, String>();
    	conditionsMap.put("orderBy", "boardId");
    	conditionsMap.put("desc","desc");
    	
		boardList = pfdBoardService.findPfdBoard(conditionsMap);

		return SUCCESS;
	}

    public String findPfdUserIdAjax() throws Exception {

    	log.info(">>> paramPfdCustomerInfoId = " + paramPfdCustomerInfoId);

		try {

			Map<String, String> conditionMap = new HashMap<String, String>();
			conditionMap.put("pfdCustomerInfoId", paramPfdCustomerInfoId);
			List<PfdUser> list = pfdUserService.getPfdUserByCondition(conditionMap);
			for (int i=0; i<list.size(); i++) {
				PfdUser pojo = list.get(i);
				pfdUserMap.put(pojo.getUserId(), pojo.getUserName());
			}
			JSONObject jsonObject = new JSONObject(pfdUserMap);

			jsonData = jsonObject.toString();
			log.info(">>> jsonData = " + jsonData);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

    	return SUCCESS;
    }

	public String add() throws Exception {

		initPfdCustomerInfoMap();

		return SUCCESS;
	}

	public String doAdd() throws Exception {

		PfdBoard board = new PfdBoard();
		board.setBoardType(boardType);
		board.setBoardContent(content);
		//board.setBoardContent(content);
		if (boardType.equals(EnumPfdBoardType.SYSTEM.getType())) {
			board.setPfdCustomerInfoId(null);
			board.setPfdUserId(null);
			board.setIsSysBoard("y");
		} else {
			board.setPfdCustomerInfoId(pfdCustomerInfoId);
			board.setPfdUserId(pfdUserId);
			board.setIsSysBoard("n");
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
		board.setCreateDate(new Date());

		pfdBoardService.save(board);

		return SUCCESS;
	}

	public String update() throws Exception {

		log.info(">>> boardId  = " + boardId);

    	Map<String, String> conditionsMap = new HashMap<String, String>();
    	conditionsMap.put("boardId", boardId);

		board = pfdBoardService.findPfdBoard(conditionsMap).get(0);
		boardId = board.getBoardId().toString();
		boardType = board.getBoardType();
		content = board.getBoardContent();
		pfdCustomerInfoId = board.getPfdCustomerInfoId();
		pfdUserId = board.getPfdUserId();
		urlAddress = board.getUrlAddress();
		startDate = sdf.format(board.getStartDate());
		endDate = sdf.format(board.getEndDate());

		initPfdCustomerInfoMap();

		if (StringUtils.isNotBlank(pfdCustomerInfoId)) {
			Map<String, String> conditionMap = new HashMap<String, String>();
			conditionMap.put("pfdCustomerInfoId", pfdCustomerInfoId);

			List<PfdUser> list = pfdUserService.getPfdUserByCondition(conditionMap);

			for (int i=0; i<list.size(); i++) {
				PfdUser pojo = list.get(i);
				pfdUserMap.put(pojo.getUserName(), pojo.getUserId());
			}
		}

		return SUCCESS;
	}

	public String doUpdate() throws Exception {

		log.info(">>> boardId  = " + boardId);

    	Map<String, String> conditionsMap = new HashMap<String, String>();
    	conditionsMap.put("boardId", boardId);

		board = pfdBoardService.findPfdBoard(conditionsMap).get(0);

		board.setBoardType(boardType);
		board.setBoardContent(content);
		if (boardType.equals(EnumPfdBoardType.SYSTEM.getType())) {
			board.setPfdCustomerInfoId(null);
			board.setPfdUserId(null);
			board.setIsSysBoard("y");
		} else {
			board.setPfdCustomerInfoId(pfdCustomerInfoId);
			board.setPfdUserId(pfdUserId);
			board.setIsSysBoard("n");
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

		pfdBoardService.saveOrUpdate(board);

		return SUCCESS;
	}

	public String doDelete() throws Exception {

		pfdBoardService.deletePfdBoardById(boardId);

		return SUCCESS;
	}

    public EnumPfdBoardType[] getEnumBoardType() {
        return enumBoardType;
    }

	public void setPfdBoardService(IPfdBoardService pfdBoardService) {
		this.pfdBoardService = pfdBoardService;
	}

	public void setPfdAccountService(IPfdAccountService pfdAccountService) {
		this.pfdAccountService = pfdAccountService;
	}

	public void setPfdUserService(IPfdUserService pfdUserService) {
		this.pfdUserService = pfdUserService;
	}

	public List<PfdBoard> getBoardList() {
		return boardList;
	}

	public PfdBoard getBoard() {
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

	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}

	public String getPfdUserId() {
		return pfdUserId;
	}

	public void setPfdUserId(String pfdUserId) {
		this.pfdUserId = pfdUserId;
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

	public Map<String, String> getPfdCustomerInfoMap() {
		return pfdCustomerInfoMap;
	}

	public Map<String, String> getPfdUserMap() {
		return pfdUserMap;
	}

	public String getParamPfdCustomerInfoId() {
		return paramPfdCustomerInfoId;
	}

	public void setParamPfdCustomerInfoId(String paramPfdCustomerInfoId) {
		this.paramPfdCustomerInfoId = paramPfdCustomerInfoId;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}
}
