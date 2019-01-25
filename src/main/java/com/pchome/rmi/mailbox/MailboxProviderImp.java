package com.pchome.rmi.mailbox;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pchome.akbadm.db.pojo.PfpMailbox;
import com.pchome.akbadm.db.service.mailbox.IPfpMailboxService;
import com.pchome.rmi.mailbox.EnumCategory;

public class MailboxProviderImp implements IMailboxProvider {
    private Log log = LogFactory.getLog(getClass().getName());

	private IPfpMailboxService pfpMailboxService;

	public Integer add(String customerInfoId, String receiver, EnumCategory category) throws Exception {
	    Calendar calendar = Calendar.getInstance();

	    PfpMailbox pfpMailbox = new PfpMailbox();
	    pfpMailbox.setCustomerInfoId(customerInfoId);
	    pfpMailbox.setReceiver(receiver);
        pfpMailbox.setCategory(category.getCategory());
	    pfpMailbox.setSend("N");
	    pfpMailbox.setUpdateDate(calendar.getTime());
	    pfpMailbox.setCreateDate(calendar.getTime());

        log.info("add " + customerInfoId + " " + category);
	    return pfpMailboxService.save(pfpMailbox);
	}

	public Integer delete(String customerInfoId, EnumCategory category) throws Exception {
        log.info("delete " + customerInfoId + " " + category);
        return pfpMailboxService.deletePfpMailbox(customerInfoId, category.getCategory());
	}

    public void setPfpMailboxService(IPfpMailboxService pfpMailboxService) throws Exception {
        this.pfpMailboxService = pfpMailboxService;
    }
}