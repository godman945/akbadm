package com.pchome.akbadm;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.pojo.PfdBonusItemSet;
import com.pchome.akbadm.db.pojo.PfdContract;
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
import com.pchome.config.TestConfig;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.soft.util.DateValueUtil;
//test0427
public class ktest
{ 
	protected Logger log = LogManager.getRootLogger();

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
	
	public void test2()
	{
		List<PfpUser> list = pfpUserService.loadAll();
		log.info("...pfpUser Size=" + list.size());
		
		if(list.size() > 0)
		{
			for(PfpUser pfp : list)
			{
				String payType = pfp.getPfpCustomerInfo().getPayType();
				log.info("...payType=" + payType);
				
				//PfpCustomerInfo pfpInfo = pfp.getPfpCustomerInfo();
//				
//				PfdUser pfdUser = new PfdUser();
//				PfdCustomerInfo pfdInfo = new PfdCustomerInfo();
//				if(StringUtils.equals(payType, EnumPfdAccountPayType.ADVANCE.getPayType()))
//				{
//					pfdUser = pfdUserService.get("PFDU20140520001");
//					pfdInfo = pfdCustomerInfoService.get("PFDC20140520001");
//				}
//				else if(StringUtils.equals(payType, EnumPfdAccountPayType.LATER.getPayType()))
//				{
//					pfdUser = pfdUserService.get("PFDU20141024003");
//					pfdInfo = pfdCustomerInfoService.get("PFDC201410240003");
//				}
//				
//				PfdUserAdAccountRef pfd = new PfdUserAdAccountRef();
//				pfd.setPfpCustomerInfo(pfpInfo);
//				pfd.setPfpUser(pfp);
//				pfd.setPfdCustomerInfo(pfdInfo);
//				pfd.setPfdUser(pfdUser);
//				pfd.setPfpPayType(payType);
//				
//				pfdUserAdAccountRefService.save(pfd);
			}
		}
	}

	public void test1(Date recordDate)
	{
		List<PfdContract> pfdContracts = pfdContractService.findValidPfdContract(recordDate);
		for(PfdContract contract : pfdContracts)
		{
			log.info("...pfdContractId=" + contract.getPfdContractId());
			List<PfdBonusItemSet> pfdBonusItemSets = pfdBonusItemSetService.findPfdBonusItemSets(contract.getPfdContractId());
			log.info("...itemSize=" + pfdBonusItemSets.size());
			for(PfdBonusItemSet set : pfdBonusItemSets)
			{
				log.info("...getBonusType=" + set.getBonusType());
				log.info("...getBonusItem=" + set.getBonusItem());
				log.info("...getSubItemId=" + set.getSubItemId());
			}
		}

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
	
	public void setPfdUserAdAccountRefService(IPfdUserAdAccountRefService pfdUserAdAccountRefService)
	{
		this.pfdUserAdAccountRefService = pfdUserAdAccountRefService;
	}
	
	public void setPfdUserService(IPfdUserService pfdUserService)
	{
		this.pfdUserService = pfdUserService;
	}

	public void setPfdCustomerInfoService(IPfdCustomerInfoService pfdCustomerInfoService)
	{
		this.pfdCustomerInfoService = pfdCustomerInfoService;
	}

	public void setPfpCustomerInfoService(IPfpCustomerInfoService pfpCustomerInfoService)
	{
		this.pfpCustomerInfoService = pfpCustomerInfoService;
	}

	public static void main(String[] args) throws Exception
	{

		String[] test = { "local" };
		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(test));

		ktest job = context.getBean(ktest.class);

		String date = "2015-04-30";
		Date recordDate = DateValueUtil.getInstance().stringToDate(date);

//		job.test1(recordDate);
		job.test2();

	}
}
