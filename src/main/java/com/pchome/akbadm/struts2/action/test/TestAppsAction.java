package com.pchome.akbadm.struts2.action.test;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.pojo.PfdUser;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.pojo.PfpUser;
import com.pchome.akbadm.db.service.contract.IPfdContractService;
import com.pchome.akbadm.db.service.customerInfo.IPfdCustomerInfoService;
import com.pchome.akbadm.db.service.customerInfo.IPfdUserAdAccountRefService;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusInvoiceService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusItemSetService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusRecordService;
import com.pchome.akbadm.db.service.pfd.user.IPfdUserService;
import com.pchome.akbadm.db.service.user.IPfpUserService;
import com.pchome.akbadm.factory.pfd.bonus.PfdBonusItemFactory;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

public class TestAppsAction extends BaseAction
{

	private IPfdContractService pfdContractService;
	private IPfdBonusItemSetService pfdBonusItemSetService;
	private IPfdBonusInvoiceService pfdBonusInvoiceService;
	private IPfdBonusRecordService pfdBonusRecordService;
	private PfdBonusItemFactory pfdBonusItemFactory;

	private IPfpUserService pfpUserService;
	private IPfpCustomerInfoService pfpCustomerInfoService;

	private IPfdUserService pfdUserService;
	private IPfdCustomerInfoService pfdCustomerInfoService;

	private IPfdUserAdAccountRefService pfdUserAdAccountRefService;

	// 返回字串
	private String returnMsg = "";

	public String execute() throws Exception
	{
		return SUCCESS;
	}

	public String pfp2pfd() throws Exception
	{
		List<PfdUserAdAccountRef> pfdR = pfdUserAdAccountRefService.loadAll();
		if(pfdR.size() > 0)
		{
			returnMsg = "資料庫中尚有資料，請先delete後再執行";
			return SUCCESS;
		}
		
		List<PfpUser> list = pfpUserService.loadAll();
		log.info("...pfpUser Size=" + list.size());

		returnMsg = "Error";
		if (list.size() > 0)
		{
			for (PfpUser pfp : list)
			{
				// 慮掉PrivilegeId 不為 0 (非主要使用者)
				if (pfp.getPrivilegeId() != 0)
				{
					continue;
				}

				String payType = pfp.getPfpCustomerInfo().getPayType();
				PfpCustomerInfo pfpInfo = pfp.getPfpCustomerInfo();

				PfdUser pfdUser = new PfdUser();
				PfdCustomerInfo pfdInfo = new PfdCustomerInfo();
				if (StringUtils.equals(payType, EnumPfdAccountPayType.ADVANCE.getPayType()))
				{
					pfdUser = pfdUserService.get("PFDU20140520001");
					pfdInfo = pfdCustomerInfoService.get("PFDC20140520001");
				}
				else if (StringUtils.equals(payType, EnumPfdAccountPayType.LATER.getPayType()))
				{
					pfdUser = pfdUserService.get("PFDU20141024003");
					pfdInfo = pfdCustomerInfoService.get("PFDC20141024003");
				}

				PfdUserAdAccountRef pfd = new PfdUserAdAccountRef();
				pfd.setPfpCustomerInfo(pfpInfo);
				pfd.setPfpUser(pfp);
				pfd.setPfdCustomerInfo(pfdInfo);
				pfd.setPfdUser(pfdUser);
				pfd.setPfpPayType(payType);

				pfdUserAdAccountRefService.save(pfd);
			}
			returnMsg = "Success";
		}
		else
		{
			returnMsg = "Error -- Source No Row";
		}

		return SUCCESS;
	}

	public void setPfdContractService(IPfdContractService pfdContractService)
	{
		this.pfdContractService = pfdContractService;
	}

	public void setPfdBonusItemSetService(IPfdBonusItemSetService pfdBonusItemSetService)
	{
		this.pfdBonusItemSetService = pfdBonusItemSetService;
	}

	public void setPfdBonusInvoiceService(IPfdBonusInvoiceService pfdBonusInvoiceService)
	{
		this.pfdBonusInvoiceService = pfdBonusInvoiceService;
	}

	public void setPfdBonusRecordService(IPfdBonusRecordService pfdBonusRecordService)
	{
		this.pfdBonusRecordService = pfdBonusRecordService;
	}

	public void setPfdBonusItemFactory(PfdBonusItemFactory pfdBonusItemFactory)
	{
		this.pfdBonusItemFactory = pfdBonusItemFactory;
	}

	public void setPfpUserService(IPfpUserService pfpUserService)
	{
		this.pfpUserService = pfpUserService;
	}

	public void setPfpCustomerInfoService(IPfpCustomerInfoService pfpCustomerInfoService)
	{
		this.pfpCustomerInfoService = pfpCustomerInfoService;
	}

	public void setPfdUserService(IPfdUserService pfdUserService)
	{
		this.pfdUserService = pfdUserService;
	}

	public void setPfdCustomerInfoService(IPfdCustomerInfoService pfdCustomerInfoService)
	{
		this.pfdCustomerInfoService = pfdCustomerInfoService;
	}

	public void setPfdUserAdAccountRefService(IPfdUserAdAccountRefService pfdUserAdAccountRefService)
	{
		this.pfdUserAdAccountRefService = pfdUserAdAccountRefService;
	}

	public String getReturnMsg()
	{
		return returnMsg;
	}

}
