package com.br.hermescomercial.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigDb {

    private static final Logger logger = LogManager.getLogger(ConfigDb.class);
    
    public String getDataBase(){
        try {
            return "hermescomercial";
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    
}
