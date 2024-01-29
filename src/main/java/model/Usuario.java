package model;

public class Usuario {

    private Long id;

    private String login;
    private String senha;

    private String tipoAcesso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin(String login) {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha(String senha) {
        return this.senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTipoAcesso() {
        return tipoAcesso;
    }

    public void setTipoAcesso(String tipoAcesso) {
        this.tipoAcesso = tipoAcesso;
    }
}
