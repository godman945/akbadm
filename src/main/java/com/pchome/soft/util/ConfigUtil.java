package com.pchome.soft.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigUtil {
    private static final Log log = LogFactory.getLog(ConfigUtil.class);

    private static final ConfigUtil configUtil=new ConfigUtil();

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
                log.error(e.getMessage(), e);
            }
        }

        String value=config.getString(key);

        return value;
    }
}