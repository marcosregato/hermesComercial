package com.br.hermescomercial.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertDado {

    /**
     * Converte data em String para Date
     * @return
     */
    public Date convertData(String valor){
        try{
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            Date data = formato.parse(valor);
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
