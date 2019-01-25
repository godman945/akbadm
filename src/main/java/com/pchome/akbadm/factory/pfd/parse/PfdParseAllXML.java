package com.pchome.akbadm.factory.pfd.parse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.pchome.akbadm.bean.bonus.BonusBean;

public class PfdParseAllXML extends APfdParseBonusXML{

	@Override
	public List<Object> parseXML(String filePath, int groupId) {
		
		log.info("------- Parse All XML Start -------");
		
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		BonusBean bean = null;
		List<Object> list = new ArrayList<Object>();
		List<BonusBean> bonusBean = null;
		
		boolean bTitle = false;
	    boolean bMin = false;
	    boolean bMax = false;
	    boolean bDiscount = false;
		
		try {
			XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileInputStream(filePath));
	        int event = xmlStreamReader.getEventType();
		    
	        while(xmlStreamReader.hasNext()){
				
				switch(event) {
				
				case XMLStreamConstants.START_ELEMENT:
					
					if(xmlStreamReader.getLocalName().equals(GROUP_START_TAG)){
						
						bonusBean = new ArrayList<BonusBean>();						
					}
					
					if(xmlStreamReader.getLocalName().equals(LEVEL_START_TAG)){						

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
					
					
					if(xmlStreamReader.getLocalName().equals(LEVEL_START_TAG)){
						
						bonusBean.add(bean);
					}
					
					if(xmlStreamReader.getLocalName().equals(GROUP_START_TAG)){
						
						list.add(bonusBean);
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
		
		//log.info(" listSize: "+list.size());
		/*
		for(Object ob:list){
			List<BonusBean> bonusBean1 = (List<BonusBean>)ob;
			for(BonusBean bean1:bonusBean1){
				log.info(" 1. "+bean1.getTitle());
				log.info(" 2. "+bean1.getBonus());
			}
		}
		*/
		log.info("------- Parse All XML End -------");
		
		return list;
	}

}
