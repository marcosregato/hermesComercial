package com.br.hermescomercial.util;

import com.br.hermescomercial.controller.FluxoCaixaController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertDado {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(ConvertDado.class);
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
