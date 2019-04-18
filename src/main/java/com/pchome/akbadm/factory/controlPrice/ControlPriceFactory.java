package com.pchome.akbadm.factory.controlPrice;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.enumerate.factory.EnumControlPriceItem;

@Transactional
public class ControlPriceFactory {
	
	protected Logger log = LogManager.getRootLogger();
	
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
