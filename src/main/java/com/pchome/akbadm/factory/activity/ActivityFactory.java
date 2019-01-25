package com.pchome.akbadm.factory.activity;

import com.pchome.enumerate.factory.EnumActivityType;

public class ActivityFactory {

	private IActivity newCustomerActivity;

	
	public IActivity getActivity(EnumActivityType enumActivityType){
		
		IActivity activity = null;
		
		switch(enumActivityType){
		case NEW_CUSTOMER:
			activity = newCustomerActivity;
			break;
			
		}
		
		return activity;
	}


	public void setNewCustomerActivity(IActivity newCustomerActivity) {
		this.newCustomerActivity = newCustomerActivity;
	}
	
}
