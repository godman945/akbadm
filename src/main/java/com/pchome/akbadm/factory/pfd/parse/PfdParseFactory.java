package com.pchome.akbadm.factory.pfd.parse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pchome.enumerate.pfd.bonus.EnumPfdXmlParse;

public class PfdParseFactory {

	protected Log log = LogFactory.getLog(this.getClass().getName());
	
	private APfdParseBonusXML pfdParseGroupXML;
	private APfdParseBonusXML pfdParseMonthBonusXML;
	private APfdParseBonusXML pfdParseAllXML;

	public APfdParseBonusXML getPfdParseBonusXML(EnumPfdXmlParse enumPfdXmlParse) {
		
		APfdParseBonusXML pfdParseBonusXML = null;
		
		switch(enumPfdXmlParse){
		case PARSE_GROUP_XML:
			pfdParseBonusXML = pfdParseGroupXML;
			break;
		case PARSE_MONTH_BONUS_XML:
			pfdParseBonusXML = pfdParseMonthBonusXML;
			break;
		case PARSE_ALL_XML:
			pfdParseBonusXML = pfdParseAllXML;
			break;
		default:
			break;			
		}
	
		return pfdParseBonusXML;
	}

	public void setPfdParseGroupXML(APfdParseBonusXML pfdParseGroupXML) {
		this.pfdParseGroupXML = pfdParseGroupXML;
	}

	public void setPfdParseMonthBonusXML(APfdParseBonusXML pfdParseMonthBonusXML) {
		this.pfdParseMonthBonusXML = pfdParseMonthBonusXML;
	}

	public void setPfdParseAllXML(APfdParseBonusXML pfdParseAllXML) {
		this.pfdParseAllXML = pfdParseAllXML;
	}

}
