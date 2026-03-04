package com.br.hermescomercial.business.permissao;

import com.br.hermescomercial.dao.UsuarioDao;
import com.br.hermescomercial.model.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PermissaoAcesso {

    private static final Logger logger = LogManager.getLogger(PermissaoAcesso.class);
    UsuarioDao dao;

    public void acessoAcao(Usuario usuario){
        try {
            if(usuario != null){
                 dao.buscar(usuario.getTipousuario());

            }
        } catch (Exception e) {
            logger.error("Error saving alert", e);

        }
    }

}
