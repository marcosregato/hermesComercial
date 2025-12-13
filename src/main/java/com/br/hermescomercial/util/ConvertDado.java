package com.br.hermescomercial.util;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertDado {

    Logger logger = Logger.getLogger(getClass().getName());
    /**
     * Converte data em String para Date
     * @return
     */
    public Date convertData(String valor){
        try{
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            return formato.parse(valor);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return null;
    }
}
