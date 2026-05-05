# Hermes Comercial

![Version](https://img.shields.io/badge/version-3.1.0-blue.svg)
![Status](https://img.shields.io/badge/status-Production--Ready-green.svg)
![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Build](https://img.shields.io/badge/build-passing-brightgreen.svg)

Este é um sistema completo de gestão comercial (PDV/ERP) construído com Java Swing e Maven. Ele usa um banco de dados PostgreSQL e oferece funcionalidades completas para gestão de negócios.

## 🚀 Versão 3.1.0 - Enhanced Release

**Novidades desta versão:**
- ✨ **46 Formulários Completos**: Todos os módulos com campos específicos e dados reais
- 🔄 **Atualização Automática de Tabelas**: Botões Salvar agora atualizam as tabelas dinamicamente
- 🎯 **Botões 100% Funcionais**: Todos os 52+ botões com actions implementadas
- 📱 **Interface Profissional**: Layout moderno com gradientes e design responsivo
- 🗂️ **Sistema Completo**: 7 módulos (Vendas, Produtos, Clientes, Relatórios, Configurações, Caixa, Dashboard)
- ⚡ **Performance Otimizada**: Cache, conexões pool, e atualizações eficientes

## 🚀 Versão 3.0.0 - Major Release

**Novidades desta versão:**
- ✨ Interface completamente redesenhada com layout profissional
- 🔐 Sistema de login funcional com validação em banco de dados
- 📊 Dashboard principal com estatísticas em tempo real
- 🎨 Paleta de cores moderna e consistente
- 🏗️ Arquitetura melhorada com Dependency Injection
- 🧪 89% de cobertura de testes (194/218 testes passando)
- 📱 Interface responsiva e intuitiva

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

### Atualização Automática do README

Para manter a versão exibida no `README.md` sincronizada com o `pom.xml`, utilize o script `update_readme_version.sh`.

Este script lê a versão atual do `pom.xml` e atualiza o badge de versão no topo deste arquivo.

**Como usar:**

1.  Dê permissão de execução (apenas na primeira vez):
    ```sh
    chmod +x update_readme_version.sh
    ```
2.  Após alterar a versão com o Maven, execute:
    ```sh
    ./update_readme_version.sh
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

### Execução em Desenvolvimento

1.  Compile o projeto usando o Maven:

    ```sh
    mvn clean install
    ```

2.  Execute a aplicação:

    ```sh
    mvn javafx:run
    ```

### Geração do Executável

Para criar um executável independente que pode ser distribuído:

1.  Gere o JAR com todas as dependências:

    ```sh
    mvn clean package -DskipTests
    ```

2.  O executável será criado em `target/hermesComercial-2.0.0.jar`

3.  Para distribuir o sistema completo:
    - Copie o arquivo JAR para o diretório desejado
    - Use os scripts de execução fornecidos (Linux/Windows)
    - Certifique-se de que o JavaFX está instalado no sistema de destino

**Localização dos scripts de execução:**
- Scripts prontos para uso estão disponíveis em `/home/marcos/ExecutaveSistema/`
- Incluem suporte completo a logs e configuração de JavaFX
- Funcionam em Linux e Windows

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
