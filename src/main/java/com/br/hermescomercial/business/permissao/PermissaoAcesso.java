package com.br.hermescomercial.business.permissao;

import com.br.hermescomercial.dao.PessoaDao;
import com.br.hermescomercial.dao.UsuarioDao;
import com.br.hermescomercial.model.Pessoa;
import com.br.hermescomercial.model.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PermissaoAcesso {

    private static final Logger logger = LogManager.getLogger(PermissaoAcesso.class);
    PessoaDao dao;

    public void acessoAcao(Pessoa pessoa){
        try {
            if(pessoa != null){
                 dao.buscar(pessoa.getTipoPessoa());

            }
        } catch (Exception e) {
            logger.error("Error saving alert", e);

        }
    }

}
