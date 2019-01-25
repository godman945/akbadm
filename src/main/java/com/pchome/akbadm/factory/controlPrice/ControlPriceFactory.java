package com.pchome.akbadm.factory.controlPrice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.enumerate.factory.EnumControlPriceItem;

@Transactional
public class ControlPriceFactory {
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	private AControlPrice controlPriceEveryDay;
	
	public AControlPrice get(EnumControlPriceItem enumControlPriceItem) {
		
		AControlPrice controlPriceItem = null;
		
		switch(enumControlPriceItem){
		
		case EVERYDAY:
			controlPriceItem = controlPriceEveryDay;
			break;
		}
		
		return controlPriceItem;
	}
	
	public void setControlPriceEveryDay(AControlPrice controlPriceEveryDay) {
		this.controlPriceEveryDay = controlPriceEveryDay;
	}
	
}
