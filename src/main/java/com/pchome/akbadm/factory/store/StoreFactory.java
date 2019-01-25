package com.pchome.akbadm.factory.store;

import com.pchome.enumerate.factory.EnumStore;

public class StoreFactory {
    private AbstractStore allLog;

	public AbstractStore getStoreObject(EnumStore enumStore) {
	    AbstractStore abstractStore = null;

		switch(enumStore){
            case ALL_LOG:
                abstractStore = allLog;
                break;
		}

		return abstractStore;
	}

    public void setAllLog(AbstractStore allLog) {
        this.allLog = allLog;
    }
}