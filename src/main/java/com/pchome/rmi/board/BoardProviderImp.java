package com.pchome.rmi.board;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pchome.akbadm.db.pojo.PfbxBoard;
import com.pchome.akbadm.db.pojo.PfdBoard;
import com.pchome.akbadm.db.pojo.PfpBoard;
import com.pchome.akbadm.db.service.board.IPfpBoardService;
import com.pchome.akbadm.db.service.pfbx.board.IPfbxBoardService;
import com.pchome.akbadm.db.service.pfd.board.IPfdBoardService;
import com.pchome.rmi.mailbox.EnumCategory;

public class BoardProviderImp implements IBoardProvider {
    private Log log = LogFactory.getLog(getClass().getName());

	private IPfpBoardService pfpBoardService;
	private IPfdBoardService pfdBoardService;
	private IPfbxBoardService pfbBoardService;

	public Integer add(String customerInfoId, EnumBoardType boardType, EnumCategory category) {
		return add(customerInfoId, category.getBoardContent(), category.getUrlAddress(), boardType, category, null);
	}
	
	public Integer add(String customerInfoId, String content, EnumBoardType boardType, EnumCategory category) {
	    return add(customerInfoId, content, boardType, category, null);
	}

	public Integer add(String customerInfoId, String content, EnumBoardType boardType, EnumCategory category, String deleteId) {
	    return add(customerInfoId, content, null, boardType, category, deleteId);
	}

    public Integer add(String customerInfoId, String content, String urlAddress, EnumBoardType boardType, EnumCategory category, String deleteId) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        calendar.add(Calendar.MONTH, 6);
        Date endDate = calendar.getTime();

        PfpBoard board = new PfpBoard();
        board.setCustomerInfoId(customerInfoId);
        board.setBoardType(boardType.getType());
        board.setCategory(category.getCategory());
        board.setDeleteId(deleteId);
        board.setContent(content);
        board.setStartDate(df.format(date));
        board.setEndDate(df.format(endDate));
        board.setDisplay("Y");
        board.setUrlAddress(urlAddress);
        board.setAuthor("SYSTEM");
        board.setEditor("SYSTEM");
        board.setUpdateDate(date);
        board.setCreateDate(date);

        if (StringUtils.isBlank(urlAddress)) {
            board.setHasUrl("N");
        }
        else {
            board.setHasUrl("Y");
        }

        log.info("add " + customerInfoId + " " + category);
        return pfpBoardService.save(board);
	}

    public Integer addPfdBoard(String isSysBoard, String pfdCustomerInfoId, String pfdUserId,
    		String content, String urlAddress, String boardType) {

        Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();

        calendar.add(Calendar.MONTH, 6);
        Date endDate = calendar.getTime();

        PfdBoard board = new PfdBoard();
		board.setBoardType(boardType);
		board.setBoardContent(content);
		board.setPfdCustomerInfoId(pfdCustomerInfoId);
		board.setPfdUserId(pfdUserId);
		board.setStartDate(startDate);
		board.setEndDate(endDate);
		if(StringUtils.isNotEmpty(urlAddress)){
			board.setHasUrl("y");
			board.setUrlAddress(urlAddress);
		}else{
			board.setHasUrl("n");
			board.setUrlAddress(null);
		}
		board.setCreateDate(new Date());

        return pfdBoardService.save(board);
	}

    public Integer addPfbBoard(String isSysBoard, String pfbxCustomerInfoId,
    		String content, String urlAddress, String boardType) {

        Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();

        calendar.add(Calendar.MONTH, 6);
        Date endDate = calendar.getTime();

        PfbxBoard board = new PfbxBoard();
		board.setBoardType(boardType);
		board.setBoardContent(content);
		board.setPfbxCustomerInfoId(pfbxCustomerInfoId);
		board.setStartDate(startDate);
		board.setEndDate(endDate);
		if(StringUtils.isNotEmpty(urlAddress)){
			board.setHasUrl("y");
			board.setUrlAddress(urlAddress);
		}else{
			board.setHasUrl("n");
			board.setUrlAddress(null);
		}
		board.setCreateDate(new Date());

        return pfbBoardService.save(board);
	}

    public Integer hide(String customerInfoId, EnumCategory category) throws Exception {
        log.info("hide " + customerInfoId + " " + category);
        return pfpBoardService.displayPfpBoard(customerInfoId, category.getCategory(), "N");
    }

    public Integer delete(String customerInfoId, EnumCategory category) {
        return pfpBoardService.deletePfpBoard(customerInfoId, category.getCategory());
    }

    public Integer delete(String customerInfoId, EnumCategory category, String deleteId) throws Exception {
        return pfpBoardService.deletePfpBoard(customerInfoId, category.getCategory(), deleteId);
    }

	public void setPfpBoardService(IPfpBoardService pfpBoardService) {
		this.pfpBoardService = pfpBoardService;
	}

	public void setPfdBoardService(IPfdBoardService pfdBoardService) {
		this.pfdBoardService = pfdBoardService;
	}

	public void setPfbBoardService(IPfbxBoardService pfbBoardService) {
		this.pfbBoardService = pfbBoardService;
	}
}
