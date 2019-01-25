package com.pchome.akbadm.db.service.pfbx.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.pfbx.account.IPfbxCustomerInfoDAO;
import com.pchome.akbadm.db.dao.pfbx.play.IPfbxBlockCusurlDAO;
import com.pchome.akbadm.db.dao.pfbx.report.quartz.IPfbAdUrlReportDAO;
import com.pchome.akbadm.db.pojo.AdmPfbxBlockUrl;
import com.pchome.akbadm.db.pojo.PfbxAdUrlReport;
import com.pchome.akbadm.db.pojo.PfbxBlockCusurl;
import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.pojo.PfbxUser;
import com.pchome.akbadm.db.pojo.PfbxUserOption;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.pfbx.adurl.PfbAdUrlListVO;
import com.pchome.akbadm.utils.KTools;
import com.pchome.enumerate.pfbx.account.EnumPfbxAccountCategory;

public class PfbxCustomerInfoService extends BaseService<PfbxCustomerInfo, String> implements IPfbxCustomerInfoService
{

	private IPfbAdUrlReportDAO pfbAdUrlReportDAO;
	private IPfbxBlockCusurlDAO pfbxBlockCusurlDAO;

	public List<PfbAdUrlListVO> getPfbAdUrlList(String keyword, String category, String accStatus) throws Exception
	{
		List<PfbAdUrlListVO> vos = new ArrayList<PfbAdUrlListVO>();
		List<PfbxCustomerInfo> pfbs = ((IPfbxCustomerInfoDAO) dao).getList_Bykey(keyword, category, accStatus);

		for (PfbxCustomerInfo pfb : pfbs)
		{
			PfbAdUrlListVO vo = new PfbAdUrlListVO();

			// 已被block列表
			Set<AdmPfbxBlockUrl> blockeds = pfb.getAdmPfbxBlockUrls();
			int blockSize = blockeds.size();
//			List<PfbxBlockCusurl> pfbbcs = pfbxBlockCusurlDAO.getSYSList_By_PfbId(pfb.getCustomerInfoId(), "");
//			List<String> bcurls = new ArrayList<String>();
//			for (PfbxBlockCusurl pfbbc : pfbbcs)
//			{
//				bcurls.add(pfbbc.getUrl());
//			}

			// 不符網址數 start
			boolean chkUrlOK = true;
			String domainUrl = "";
			String ipUrl = "";
			if (pfb.getWebsiteDisplayUrl().indexOf(".") < 0)
			{
				chkUrlOK = false;
			}
			else
			{
				domainUrl = KTools.getInstance().getDomainByUrl(pfb.getWebsiteDisplayUrl());
			}

			if (chkUrlOK)
			{
				int notIncount = 0;
				String pfbcId = pfb.getCustomerInfoId();
				String domain = pfb.getWebsiteDisplayUrl();

				// start 處理domain
				if(StringUtils.indexOf(domain, "http://") != -1)
				{
					domain = domain.replaceAll("http://", "");
				}
				if(StringUtils.indexOf(domain, "https://") != -1)
				{
					domain = domain.replaceAll("https://", "");
				}
				if(StringUtils.indexOf(domain, "/") != -1)
				{
					domain = domain.substring(0, domain.indexOf("/"));
				}
				domain = domain.substring(domain.indexOf(".") + 1);
				//log.info("...domain:" + domain);
				// end 處理domain

				List<String> pvclks = pfbAdUrlReportDAO.getListByPfbId2(pfbcId, domain);
				notIncount = pvclks.size() - blockeds.size();
				vo.setNotincount(Integer.toString(notIncount));
			}
			else
			{
				vo.setNotincount("error");
			}
			// 不符合網址數 end

			// 濾掉不符網址為0的帳戶
			if (StringUtils.equals(vo.getNotincount(), "0"))
			{
				continue;
			}

			// 已封鎖數量 start
//			List<PfbxUserOption> listOption = new ArrayList<PfbxUserOption>(pfb.getPfbxUserOptions());
//			if (listOption.size() > 0)
//			{
//				for (PfbxUserOption useroption : listOption)
//				{
//					if (StringUtils.equals("SYS", useroption.getOptionName()))
//					{
//						blockSize = useroption.getPfbxBlockCusurls().size();
//						break;
//					}
//				}
//			}
			vo.setBlockcount(Integer.toString(blockSize));
			// 已封鎖數量 end

			// 狀態代號轉換中文
			if (StringUtils.equals(pfb.getCategory(), EnumPfbxAccountCategory.FIRM.getCategory()))
			{
				vo.setCategory(EnumPfbxAccountCategory.FIRM.getChName());
				vo.setCompanyname(pfb.getCompanyName());
			}
			else if (StringUtils.equals(pfb.getCategory(), EnumPfbxAccountCategory.PERSONAL.getCategory()))
			{
				vo.setCategory(EnumPfbxAccountCategory.PERSONAL.getChName());

				Set<PfbxUser> pfbxUsers = pfb.getPfbxUsers();
				for (PfbxUser pu : pfbxUsers)
				{
					if (pu.getPrivilegeId() == 0)
					{
						vo.setCompanyname(pu.getUserName());
						break;
					}
				}
			}

			vo.setPfbid(pfb.getCustomerInfoId());
			vo.setMemberid(pfb.getMemberId());
			vo.setUrl(pfb.getWebsiteDisplayUrl());
			vo.setTaxid(pfb.getTaxId());
			vo.setStatus(pfb.getStatus());
			vos.add(vo);
		}

		return vos;
	}

	public List<PfbxCustomerInfo> getPfbxCustomerInfoByCondition(Map<String, String> conditionMap) throws Exception
	{
		return ((IPfbxCustomerInfoDAO) dao).getPfbxCustomerInfoByCondition(conditionMap);
	}

	public PfbxCustomerInfo findPfbxCustomerInfo(String pfbId)
	{
		List<PfbxCustomerInfo> list = ((IPfbxCustomerInfoDAO) dao).findPfbxCustomerInfo(pfbId);

		if (!list.isEmpty())
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
	}

	public List<PfbxCustomerInfo> findValidPfbxCustomerInfo()
	{
		return ((IPfbxCustomerInfoDAO) dao).findQuartzsPfbxCustomerInfo();
	}

	public void setPfbAdUrlReportDAO(IPfbAdUrlReportDAO pfbAdUrlReportDAO)
	{
		this.pfbAdUrlReportDAO = pfbAdUrlReportDAO;
	}

	public void setPfbxBlockCusurlDAO(IPfbxBlockCusurlDAO pfbxBlockCusurlDAO)
	{
		this.pfbxBlockCusurlDAO = pfbxBlockCusurlDAO;
	}

	public List<PfbxCustomerInfo> getDemoPfbxCustomerInfo(){
		return ((IPfbxCustomerInfoDAO) dao).getDemoPfbxCustomerInfo();
	}
	
}
