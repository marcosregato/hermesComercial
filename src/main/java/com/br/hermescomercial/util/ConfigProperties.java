package com.br.hermescomercial.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {
	
	private String value;
	
	public ConfigProperties(String valor) {
		this.value = valor;
		Properties properties = null;
        InputStream inputStream = null;
        String path = System.getProperty("user.dir");
        try {
            inputStream = new FileInputStream(path + "/config.properties");
            properties = new Properties();
            properties.load(inputStream);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
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
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return properties.getProperty(value).trim();
    }

}
