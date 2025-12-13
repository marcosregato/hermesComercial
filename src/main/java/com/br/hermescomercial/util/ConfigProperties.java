package com.br.hermescomercial.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

public class ConfigProperties {
	
	private String value;
    static Logger logger = Logger.getLogger(ConfigProperties.class.getName());
	
	public ConfigProperties(String valor) {
		this.value = valor;
		Properties properties = null;
        InputStream inputStream = null;
        String path = System.getProperty("user.dir");
        try {
            inputStream = new FileInputStream(path + "/config.properties");
            properties = new Properties();
            properties.load(inputStream);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        assert properties != null;
        properties.getProperty(this.value).trim();
	}
	
	public static String getProperty(String value) {
        Properties properties = null;
        InputStream inputStream = null;
        String path = System.getProperty("user.dir");
        try {

            inputStream = new FileInputStream(path + "/config.properties");
            properties = new Properties();
            properties.load(inputStream);
            return properties.getProperty(value).trim();

        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return Collections.emptyList().toString();
    }

}
