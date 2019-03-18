package com.pchome.akbadm.factory.parse;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.pchome.akbadm.bean.bonus.BonusBean;

public abstract class AParseBonusXML {

	protected Logger log = LogManager.getRootLogger();
	
	protected final String GROUP_START_TAG = "group";
	protected final String LEVEL_START_TAG = "level";
	protected final String LEVEL_TITLE_TAG = "title";
	protected final String LEVEL_MIN_TAG = "min";
	protected final String LEVEL_MAX_TAG = "max";
	protected final String LEVEL_DISCOUNT_TAG = "discount";
	
	/**
	 * 讀取檔案分析使用
	 * 1. groupId 決定讀取某區塊資料, 如果設定 0 則讀取全部資料
	 */
	public abstract List<Object> parseXML(String filePath, int groupId);
}
