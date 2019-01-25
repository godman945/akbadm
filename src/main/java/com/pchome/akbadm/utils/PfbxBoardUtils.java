package com.pchome.akbadm.utils;

import com.pchome.akbadm.db.pojo.PfbxBoard;
import com.pchome.akbadm.db.service.pfbx.board.IPfbxBoardService;
import com.pchome.akbadm.db.service.pfbx.board.PfbxBoardService;
import com.pchome.akbadm.utils.DateTimeUtils;
import com.pchome.enumerate.pfbx.board.EnumBoardContent;
import com.pchome.enumerate.pfbx.board.EnumPfbBoardLimit;
import com.pchome.rmi.board.EnumPfbBoardType;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

public class PfbxBoardUtils {
    private static IPfbxBoardService pfbxBoardService;
    
    
    /**
     * 寫入公告2
     * @param customerInfoId 用戶ID
     * @param boardType 公告類別
     * @param boardContent 公告內容
     */
    public static void writePfbxBoard_UserContent(String customerInfoId, EnumPfbBoardType boardType, String delId , String content, String linkUrl) {
        Date now = DateTimeUtils.getDate();
        if(StringUtils.isNotBlank(linkUrl)){
            content = "<a class='boardContentLink' href='pfb/" + linkUrl + "'>" + content + "</a> ";
        }
        //寫入公告
        PfbxBoard board = new PfbxBoard();
        board.setBoardType(boardType.getType());
        board.setBoardContent(content);
        board.setPfbxCustomerInfoId(customerInfoId);
        board.setStartDate(now);
        board.setEndDate(DateTimeUtils.shiftMontth(now, EnumPfbBoardLimit.Limit.getValue()));
        board.setHasUrl("n");
        board.setUrlAddress(null);
        board.setDeleteId(delId);
        board.setCreateDate(now);
        pfbxBoardService.save(board);
    }

    /**
     * 寫入公告
     * @param customerInfoId 用戶ID
     * @param boardType 公告類別
     * @param boardContent 公告內容
     */
    public static void writePfbxBoard(String customerInfoId, EnumPfbBoardType boardType, EnumBoardContent boardContent, String linkUrl) {
        Date now = DateTimeUtils.getDate();
        String content = boardContent.getContent();
        if(StringUtils.isNotBlank(linkUrl)){
            content = "<a class='boardContentLink' href='" + linkUrl + "'>" + content + "</a> ";
        }
        //寫入公告
        PfbxBoard board = new PfbxBoard();
        board.setBoardType(boardType.getType());
        board.setBoardContent(content);
        board.setPfbxCustomerInfoId(customerInfoId);
        board.setStartDate(now);
        board.setEndDate(DateTimeUtils.shiftMontth(now, EnumPfbBoardLimit.Limit.getValue()));
        board.setHasUrl("n");
        board.setUrlAddress(null);
        board.setDeleteId(boardContent.getId());
        board.setCreateDate(now);
        pfbxBoardService.save(board);
    }

    /**
     * 寫入公告3
     * @param customerInfoId 用戶ID
     * @param boardType 公告類別
     * @param boardContent 公告內容
     */
    public static void writePfbxBoard_UserContent2(String customerInfoId, EnumPfbBoardType boardType, String delId , String content, String linkUrl) {
        Date now = DateTimeUtils.getDate();
        if(StringUtils.isNotBlank(linkUrl)){
            content = "<a class='boardContentLink' href='" + linkUrl + "'>" + content + "</a> ";
        }
        //寫入公告
        PfbxBoard board = new PfbxBoard();
        board.setBoardType(boardType.getType());
        board.setBoardContent(content);
        board.setPfbxCustomerInfoId(customerInfoId);
        board.setStartDate(now);
        board.setEndDate(DateTimeUtils.shiftMontth(now, EnumPfbBoardLimit.Limit.getValue()));
        board.setHasUrl("n");
        board.setUrlAddress(null);
        board.setDeleteId(delId);
        board.setCreateDate(now);
        pfbxBoardService.save(board);
    }
    
    public void setPfbxBoardService(IPfbxBoardService pfbxBoardService) {
        this.pfbxBoardService = pfbxBoardService;
    }
}
