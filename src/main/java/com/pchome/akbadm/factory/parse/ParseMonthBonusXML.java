package com.pchome.akbadm.factory.parse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.pchome.akbadm.bean.bonus.BonusBean;

public class ParseMonthBonusXML extends AParseBonusXML{

	@Override
	public List<Object> parseXML(String filePath, int groupId) {
		
		log.info("------- Parse Month Bonus Start -------");
		
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		BonusBean bean = null;
		List<Object> list = null;
		
		boolean bTitle = false;
	    boolean bMin = false;
	    boolean bMax = false;
	    boolean bDiscount = false;

		try {
			XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileInputStream(filePath));
	        int event = xmlStreamReader.getEventType();
	        
	        list = new ArrayList<Object>();
	        
	        while(xmlStreamReader.hasNext()){
				
				switch(event) {
				
				case XMLStreamConstants.START_ELEMENT:
										
					if(xmlStreamReader.getLocalName().equals(GROUP_START_TAG)){						
						bean = new BonusBean();
					}
					
					if(xmlStreamReader.getLocalName().equals(LEVEL_TITLE_TAG)){
						bTitle = true;
					}
					
					if(xmlStreamReader.getLocalName().equals(LEVEL_MIN_TAG)){
						bMin = true;
					}
					
					if(xmlStreamReader.getLocalName().equals(LEVEL_MAX_TAG)){
						bMax = true;
					}
					
					if(xmlStreamReader.getLocalName().equals(LEVEL_DISCOUNT_TAG)){
						bDiscount = true;
					}
					
	                break;
	                
				case XMLStreamConstants.CHARACTERS:
					
										
					if(bTitle){
						bean.setTitle(xmlStreamReader.getText());
						bTitle = false;
					}
					
					if(bMin){	
						bean.setMin(Float.parseFloat(xmlStreamReader.getText()));
						bMin = false;
					}
					
					if(bMax){	
						bean.setMax(Float.parseFloat(xmlStreamReader.getText()));
						bMax = false;
					}
					
					if(bDiscount){	
						bean.setBonus(Float.parseFloat(xmlStreamReader.getText()));
						bDiscount = false;
					}
					
	                break;
	                
				case XMLStreamConstants.END_ELEMENT:
					
					
					if(xmlStreamReader.getLocalName().equals(GROUP_START_TAG)){
						list.add(bean);
					}					
					
					break;
				}
	
				event = xmlStreamReader.next();
			}
	        
		} catch (FileNotFoundException e) {
			
			log.info(" fileError = "+e);
			
		} catch (XMLStreamException e) {
			
			log.info(" xmlError = "+e);
		}
		
		//log.info(" list: "+list.size());
		/*
		for(Object ob:list){
			BonusBean bean1 = (BonusBean)ob;
			log.info(" ob: "+bean1.getTitle());
			log.info(" ob: "+bean1.getBonus());
		}
		*/
		log.info("------- Parse Month Bonus End -------");
		
		return list;
	}

}
