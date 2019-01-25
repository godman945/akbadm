package com.pchome.akbadm.struts2.action.pfbx.pfbadurl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.AdmPfbxBlockUrl;
import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.akbadm.db.service.pfbx.IPfbxUserOptionService;
import com.pchome.akbadm.db.service.pfbx.account.IPfbxCustomerInfoService;
import com.pchome.akbadm.db.service.pfbx.play.IAdmPfbxBlockUrlService;
import com.pchome.akbadm.db.service.pfbx.play.IPfbxBlockCusurlService;
import com.pchome.akbadm.db.service.pfbx.quartzs.IPfbxAdUrlReportService;
import com.pchome.akbadm.db.service.sequence.ISequenceService;
import com.pchome.akbadm.db.vo.pfbx.adurl.PfbAdUrlListVO;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.akbadm.utils.PfbxBoardUtils;
import com.pchome.enumerate.pfbx.account.EnumPfbAccountStatus;
import com.pchome.enumerate.pfbx.account.EnumPfbxAccountCategory;
import com.pchome.enumerate.pfbx.board.EnumBoardContent;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;
import com.pchome.rmi.board.EnumPfbBoardType;

public class PfbAdUrlAction extends BaseCookieAction
{

	private static final long serialVersionUID = 1L;
	private String msg = "";

	// 參數
	private String keyword;
	private String category;
	private String accStatus;
	private String id;
	private String startDate;
	private String endDate;
	private String searchUrl;
	private String blockStatus;
	private String blockDesc;
	private String blockUrl;
	private String unblockId;

	// service
	private IPfbxCustomerInfoService pfbxCustomerInfoService;
	private IPfbxAdUrlReportService pfbxAdUrlReportService;
	private IPfbxUserOptionService pfbxUserOptionService;
	private IPfbxBlockCusurlService pfbxBlockCusurlService;
	private ISequenceService sequenceService;
	private IAdmAccesslogService admAccesslogService;
	private IAdmPfbxBlockUrlService admPfbxBlockUrlService;

	// view
	private List<PfbAdUrlListVO> urlVOS;
	private PfbxCustomerInfo pfbInfo;

	// 查詢選單
	private EnumPfbxAccountCategory[] enumcategory = EnumPfbxAccountCategory.values();
	private Map<String, String> queryStatusOptionsMap;

	public String pfbFastBlockurl() throws Exception
	{
		pfbInfo = pfbxCustomerInfoService.get(id);

		String blockRS = this.blockUrlDo(pfbInfo);
		log.info("...fastBlockRS=" + blockRS);

		return SUCCESS;
	}

	public String pfbUnBlockurl() throws Exception
	{
		if (StringUtils.isNotBlank(unblockId))
		{
			AdmPfbxBlockUrl bu = admPfbxBlockUrlService.get(Integer.parseInt(unblockId));
			pfbInfo = bu.getPfbxCustomerInfo();
			admPfbxBlockUrlService.delete(bu);
			
			// 紀錄AccessLog
			String accessMsg = "網址審核-->開通" + blockUrl;
			writeAccessLog(accessMsg);

			// 紀錄公告
			String delId = EnumBoardContent.BOARD_CONTENT_14.getId();
			String boaedMsg = EnumBoardContent.BOARD_CONTENT_14.getContent();
			boaedMsg = boaedMsg.replaceAll("〈網址〉", "<a href='http://" + bu.getBlockUrl() + "' target=\"_blank\" >" + bu.getBlockUrl() + "</a>");
			PfbxBoardUtils.writePfbxBoard_UserContent(id, EnumPfbBoardType.AD, delId, boaedMsg, "");
			
			/*
			//mark原因在blockDo
			PfbxBlockCusurl pfbbc = pfbxBlockCusurlService.get(unblockId);
			if (pfbbc != null)
			{
				// 紀錄AccessLog
				String accessMsg = "網址審核-->開通" + blockUrl;
				writeAccessLog(accessMsg);

				// 紀錄公告
				String delId = EnumBoardContent.BOARD_CONTENT_14.getId();
				String boaedMsg = EnumBoardContent.BOARD_CONTENT_14.getContent();
				boaedMsg = boaedMsg.replaceAll("〈網址〉", "<a href='http://" + pfbbc.getUrl() + "' target=\"_blank\" >" + pfbbc.getUrl() + "</a>");
				PfbxBoardUtils.writePfbxBoard_UserContent(id, EnumPfbBoardType.AD, delId, boaedMsg, "");

				pfbxBlockCusurlService.delete(pfbbc);
			}
			*/
		}

		return SUCCESS;
	}

	public String pfbBlockurl() throws Exception
	{
		pfbInfo = pfbxCustomerInfoService.get(id);

		if (StringUtils.isBlank(blockDesc))
		{
			msg = "";
			return SUCCESS;
		}

		String blockRS = this.blockUrlDo(pfbInfo);
		log.info("...blockRS=" + blockRS);

		return SUCCESS;
	}

	public String pfbBlockUrlDetail() throws Exception
	{
		pfbInfo = pfbxCustomerInfoService.get(id);
		Set<AdmPfbxBlockUrl> blockeds = pfbInfo.getAdmPfbxBlockUrls();
		
		urlVOS = new ArrayList<PfbAdUrlListVO>();
		for(AdmPfbxBlockUrl blocked : blockeds)
		{
			PfbAdUrlListVO vo = new PfbAdUrlListVO();
			vo.setDetailurl(blocked.getBlockUrl());
			vo.setDetailid(blocked.getId().toString());
			vo.setDetaildesc(blocked.getBlockNote());
			vo.setDetaildate(blocked.getCreateDate());
			urlVOS.add(vo);
		}
		
//		urlVOS = pfbxBlockCusurlService.getBlockurls(id, searchUrl);

		return SUCCESS;
	}

	public String pfbErrUrlDetail() throws Exception
	{
		pfbInfo = pfbxCustomerInfoService.get(id);

		// start 處理domain
		String domain = pfbInfo.getWebsiteDisplayUrl();
		if (StringUtils.indexOf(domain, "http://") != -1)
		{
			domain = domain.replaceAll("http://", "");
		}
		if (StringUtils.indexOf(domain, "https://") != -1)
		{
			domain = domain.replaceAll("https://", "");
		}
		if (StringUtils.indexOf(domain, "/") != -1)
		{
			domain = domain.substring(0, domain.indexOf("/"));
		}
		domain = domain.substring(domain.indexOf(".") + 1);
		log.info("...domain:" + domain);
		// end 處理domain

		urlVOS = pfbxAdUrlReportService.getErrorUrlByPfbId_V20170307(pfbInfo, startDate, endDate, searchUrl, domain);

		return SUCCESS;
	}

	public String searchAdUrlList() throws Exception
	{
		urlVOS = pfbxCustomerInfoService.getPfbAdUrlList(keyword, category, accStatus);

		return SUCCESS;
	}

	private String blockUrlDo(PfbxCustomerInfo pfbInfo) throws Exception
	{
		String msg = "";
		if (StringUtils.equals("block", blockStatus))
		{
			Date today = new Date();
			
			AdmPfbxBlockUrl bu = new AdmPfbxBlockUrl();
			bu.setPfbxCustomerInfo(pfbInfo);
			bu.setBlockUrl(blockUrl);
			bu.setBlockNote(blockDesc);
			bu.setUpdateDate(today);
			bu.setCreateDate(today);
			admPfbxBlockUrlService.save(bu);
			
			msg = "block done";
			
			//20170302 修改此功能為黑名單功能，以下為廢棄的原功能
//			PfbxUserOption pfbuo = null;
//			Set<PfbxBlockCusurl> pfbbcs = null;
//			String cuid = sequenceService.getSerialNumber(EnumSequenceTableName.PFBX_BLOCK_CUSURL);
//			String oid = "";
//			Map<String, String> blockInfo = new HashMap<String, String>();
//
//			List<PfbxUserOption> pfbuos = pfbxUserOptionService.getSYSBypfbId(id);
//			if (pfbuos.size() > 0)
//			{
//				msg = "ins";
//				pfbuo = pfbuos.get(0);
//				pfbbcs = pfbuo.getPfbxBlockCusurls();
//
//				oid = pfbuo.getOId();
//				pfbxBlockCusurlService.ins_oid_url(cuid, oid, blockUrl, blockDesc);
//			}
//			else
//			{
//				msg = "no useroption ins";
//				oid = sequenceService.getSerialNumber(EnumSequenceTableName.PFBX_USER_OPTION);
//				
//				blockInfo.put("oid", oid);
//				blockInfo.put("cuid", cuid);
//				blockInfo.put("blockUrl", blockUrl);
//				blockInfo.put("blockDesc", blockDesc);
//				blockInfo.put("pfbId", id);
//				
//				pfbxBlockCusurlService.insPfbxBlockCusurl(blockInfo);
//			}

			// 紀錄AccessLog
			String accessMsg = "網址審核-->封鎖" + blockUrl;
			writeAccessLog(accessMsg);

			// 紀錄公告
			String delId = EnumBoardContent.BOARD_CONTENT_13.getId();
			String boardMsg = EnumBoardContent.BOARD_CONTENT_13.getContent();
			boardMsg = boardMsg.replaceAll("〈網址〉", "<a href='http://" + blockUrl + "' target=\"_blank\" >" + blockUrl + "</a>").replaceAll("〈封鎖原因〉", blockDesc);
			PfbxBoardUtils.writePfbxBoard_UserContent(id, EnumPfbBoardType.AD, delId, boardMsg, "");
		}

		return msg;
	}

	/**
	 * 將操作紀錄寫入AccessLog
	 * 
	 * @param message
	 */
	private void writeAccessLog(String message)
	{
		admAccesslogService.addAdmAccesslog(
				EnumAccesslogChannel.PFB,
				EnumAccesslogAction.PLAY_MODIFY,
				message,
				String.valueOf(session.get("session_user_id")),
				null,
				pfbInfo.getCustomerInfoId(),
				// super.getSession().get(SessionConstants.SESSION_USER_ID).toString(),
				null,
				request.getRemoteAddr(),
				EnumAccesslogEmailStatus.NO);
	}

	public String execute() throws Exception
	{
		return SUCCESS;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public void setPfbxCustomerInfoService(IPfbxCustomerInfoService pfbxCustomerInfoService)
	{
		this.pfbxCustomerInfoService = pfbxCustomerInfoService;
	}

	public String getKeyword()
	{
		return keyword;
	}

	public void setKeyword(String keyword)
	{
		this.keyword = keyword;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public EnumPfbxAccountCategory[] getEnumcategory()
	{
		return enumcategory;
	}

	public List<PfbAdUrlListVO> getUrlVOS()
	{
		return urlVOS;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setPfbxAdUrlReportService(IPfbxAdUrlReportService pfbxAdUrlReportService)
	{
		this.pfbxAdUrlReportService = pfbxAdUrlReportService;
	}

	public String getStartDate()
	{
		return startDate;
	}

	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	public String getEndDate()
	{
		return endDate;
	}

	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * 狀態清單
	 * 
	 * @return
	 */
	public Map<String, String> getQueryStatusOptionsMap()
	{
		Map<String, String> map = new LinkedHashMap<String, String>();

		for (EnumPfbAccountStatus s : EnumPfbAccountStatus.values())
		{
			// 過濾要顯示的選項
			if (StringUtils.equals(s.getStatus(), EnumPfbAccountStatus.START.getStatus()))
			{
				map.put(s.getStatus(), s.getDescription());
			}
			else if (StringUtils.equals(s.getStatus(), EnumPfbAccountStatus.CLOSE.getStatus()))
			{
				map.put(s.getStatus(), s.getDescription());
			}
			else if (StringUtils.equals(s.getStatus(), EnumPfbAccountStatus.STOP.getStatus()))
			{
				map.put(s.getStatus(), s.getDescription());
			}
		}

		return map;
	}

	public String getAccStatus()
	{
		return accStatus;
	}

	public void setAccStatus(String accStatus)
	{
		this.accStatus = accStatus;
	}

	public String getBlockStatus()
	{
		return blockStatus;
	}

	public void setBlockStatus(String blockStatus)
	{
		this.blockStatus = blockStatus;
	}

	public String getBlockUrl()
	{
		return blockUrl;
	}

	public void setBlockUrl(String blockUrl)
	{
		this.blockUrl = blockUrl;
	}

	public String getBlockDesc()
	{
		return blockDesc;
	}

	public void setBlockDesc(String blockDesc)
	{
		this.blockDesc = blockDesc;
	}

	public void setPfbxUserOptionService(IPfbxUserOptionService pfbxUserOptionService)
	{
		this.pfbxUserOptionService = pfbxUserOptionService;
	}

	public void setSequenceService(ISequenceService sequenceService)
	{
		this.sequenceService = sequenceService;
	}

	public void setPfbxBlockCusurlService(IPfbxBlockCusurlService pfbxBlockCusurlService)
	{
		this.pfbxBlockCusurlService = pfbxBlockCusurlService;
	}

	public String getSearchUrl()
	{
		return searchUrl;
	}

	public void setSearchUrl(String searchUrl)
	{
		this.searchUrl = searchUrl;
	}

	public String getUnblockId()
	{
		return unblockId;
	}

	public void setUnblockId(String unblockId)
	{
		this.unblockId = unblockId;
	}

	public PfbxCustomerInfo getPfbInfo()
	{
		return pfbInfo;
	}

	public void setAdmAccesslogService(IAdmAccesslogService admAccesslogService)
	{
		this.admAccesslogService = admAccesslogService;
	}

	public void setAdmPfbxBlockUrlService(IAdmPfbxBlockUrlService admPfbxBlockUrlService)
	{
		this.admPfbxBlockUrlService = admPfbxBlockUrlService;
	}

}
