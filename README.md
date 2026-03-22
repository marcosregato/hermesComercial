# Hermes Comercial

Este é um sistema de gestão comercial construído com JavaFX e Maven. Ele usa um banco de dados PostgreSQL e Flyway para migrações de banco de dados.

## Versionamento

Este projeto usa o [Versionamento Semântico](https://semver.org/lang/pt-BR/). A versão é gerenciada no arquivo `pom.xml` e pode ser atualizada usando o plugin `build-helper-maven-plugin`.

Para atualizar a versão do projeto, você pode usar os seguintes comandos Maven:

*   Para atualizar a versão de patch (ex: de 1.0.0 para 1.0.1):
    ```sh
    mvn build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.nextIncrementalVersion}
    ```
*   Para atualizar a versão menor (ex: de 1.0.1 para 1.1.0):
    ```sh
    mvn build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.majorVersion}.\${parsedVersion.nextMinorVersion}.0
    ```
*   Para atualizar a versão principal (ex: de 1.1.0 para 2.0.0):
    ```sh
    mvn build-helper:parse-version versions:set -DnewVersion=\${parsedVersion.nextMajorVersion}.0.0
    ```

## Tecnologias Utilizadas

*   Java 17
*   JavaFX 17.0.2
*   Maven
*   PostgreSQL
*   Flyway
*   Log4j 2
*   JUnit 5

## Configuração do Banco de Dados

1.  Certifique-se de que você tem o PostgreSQL instalado e em execução.
2.  Crie um banco de dados chamado `hermescomercialdb`.
3.  Atualize o arquivo `flyway.conf` com seu nome de usuário e senha do PostgreSQL.
4.  Execute a migração do Flyway para criar o esquema do banco de dados e preenchê-lo com dados iniciais:

    ```sh
    mvn flyway:migrate
    ```

## Automação com setup.sh

Para facilitar a configuração inicial e a limpeza do ambiente de desenvolvimento, foi criado um script `setup.sh` na raiz do projeto.

Este script realiza as seguintes ações:
1.  Limpa arquivos de migração antigos ou conflitantes.
2.  Reseta o banco de dados PostgreSQL `hermescomercialdb` (apaga e recria).
3.  Executa as migrações do Flyway (`clean` e `migrate`).

**Como usar:**

1.  Dê permissão de execução ao script:
    ```sh
    chmod +x setup.sh
    ```
2.  Execute o script:
    ```sh
    ./setup.sh
    ```

**Nota:** O script assume que o usuário `postgres` tem a senha `postgres123` (configurável no script) e que o cliente `psql` está instalado e acessível no PATH.

## Como Executar

1.  Compile o projeto usando o Maven:

    ```sh
    mvn clean install
    ```

2.  Execute a aplicação:

    ```sh
    mvn javafx:run
    ```

## Manutenção do Banco de Dados

Caso precise resetar o banco de dados completamente (cuidado: isso apaga todos os dados!), você pode usar os seguintes comandos SQL no PostgreSQL:

```sql
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
```

Em seguida, execute `mvn flyway:migrate` novamente para recriar as tabelas e reinserir os dados iniciais.

## Estrutura do Projeto

*   `src/main/java`: Contém o código-fonte Java da aplicação.
    *   `com.br.hermescomercial.controller`: Contém os controladores JavaFX.
    *   `com.br.hermescomercial.dao`: Contém os Data Access Objects (DAOs) para interação com o banco de dados.
    *   `com.br.hermescomercial.model`: Contém as classes de modelo.
    *   `com.br.hermescomercial.util`: Contém classes utilitárias.
    *   `com.br.hermescomercial.connectionDB`: Contém a classe de conexão com o banco de dados.
    *   `com.br.hermescomercial.Repository`: Contém as interfaces do repositório.
*   `src/main/resources`: Contém os recursos da aplicação.
    *   `css`: Contém as folhas de estilo CSS.
    *   `db/migration`: Contém os scripts de migração de banco de dados do Flyway.
    *   `img`: Contém as imagens usadas na aplicação.
    *   `view`: Contém os arquivos FXML para a interface do usuário.
*   `src/test/java`: Contém o código-fonte de teste.
*   `pom.xml`: O arquivo de configuração do projeto Maven.
*   `flyway.conf`: O arquivo de configuração do Flyway.

## Testes

Para executar os testes, execute o seguinte comando Maven:

```sh
mvn test
```

## Contribuição

Pull requests são bem-vindos. Para alterações importantes, abra uma issue primeiro para discutir o que você gostaria de mudar.

## Licença

[MIT](https://choosealicense.com/licenses/mit/)
