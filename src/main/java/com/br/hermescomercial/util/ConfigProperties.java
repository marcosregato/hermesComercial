package com.br.hermescomercial.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {

    private static final Logger logger = LogManager.getLogger(ConfigProperties.class);
    private static final Properties properties = new Properties();

    static {
        // Tenta carregar do diretório do usuário (onde o jar roda) ou do classpath (para desenvolvimento)
        String userDir = System.getProperty("user.dir");
        String path = userDir + "/config.properties";
        
        try (InputStream inputStream = new FileInputStream(path)) {
            properties.load(inputStream);
            logger.info("Arquivo config.properties carregado de: " + path);
        } catch (Exception e) {
            logger.warn("Não foi possível carregar config.properties de " + path + ". Tentando carregar do classpath...");
            try (InputStream cpStream = ConfigProperties.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (cpStream != null) {
                    properties.load(cpStream);
                    logger.info("Arquivo config.properties carregado do classpath.");
                } else {
                    logger.error("Arquivo config.properties não encontrado em lugar nenhum!");
                }
            } catch (Exception ex) {
                logger.error("Erro fatal ao carregar configurações", ex);
            }
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            return value.trim();
        } else {
            logger.error("Propriedade '" + key + "' não encontrada no arquivo de configuração.");
            return null;
        }
    }
}
