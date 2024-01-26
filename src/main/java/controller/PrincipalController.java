package controller;

public class PrincipalController {
    private static String TIPO_DE_ACESSO;
    LoginController loginController;
    public PrincipalController(String tipoAcesso){
        this.TIPO_DE_ACESSO=tipoAcesso;

    }
    public void acessoVenda(){
        if(TIPO_DE_ACESSO.equals("vendedor") || TIPO_DE_ACESSO.equals("root")){
            //acessar o menu Vendedor
        }else{
            // não foi criado as mensagens de alerta
            // colocar mensagem de "Você não acesso a essa ação. "
        }
    }
}
