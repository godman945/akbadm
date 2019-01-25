package com.pchome.akbadm.factory.parse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pchome.enumerate.bonus.EnumParseType;

public class ParseFactory {

	protected Log log = LogFactory.getLog(this.getClass().getName());
	
	private AParseBonusXML parseGroupXML;
	private AParseBonusXML parseMonthBonusXML;
	private AParseBonusXML parseAllXML;

	public AParseBonusXML getParseBonusXML(EnumParseType enumParseType) {
		
		AParseBonusXML parseBonusXML = null;
		
		switch(enumParseType){
		case PARSE_GROUP_XML:
			parseBonusXML = parseGroupXML;
			break;
		case PARSE_MONTH_BONUS_XML:
			parseBonusXML = parseMonthBonusXML;
			break;
		case PARSE_ALL_XML:
			parseBonusXML = parseAllXML;
			break;
		default:
			break;			
		}
	
		return parseBonusXML;
	}

	public void setParseGroupXML(AParseBonusXML parseGroupXML) {
		this.parseGroupXML = parseGroupXML;
	}

	public void setParseMonthBonusXML(AParseBonusXML parseMonthBonusXML) {
		this.parseMonthBonusXML = parseMonthBonusXML;
	}

	public void setParseAllXML(AParseBonusXML parseAllXML) {
		this.parseAllXML = parseAllXML;
	}

}
