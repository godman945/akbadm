package com.pchome.soft.depot.utils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ConfigUtil {
	private static final ConfigUtil configUtil=new ConfigUtil();

	private static Logger log = LogManager.getRootLogger();
	
	private ConfigUtil(){}

	public static ConfigUtil getInstance(){
		return configUtil;
	}

	private Configuration config=null;

	public String getProperties(String Path,String key){

		if(config==null){

            log.info("----------->properties config null init config");
			try {
				config = new PropertiesConfiguration(Path);
			} catch (ConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}


		String value=config.getString(key);

		return value;
	}

}
