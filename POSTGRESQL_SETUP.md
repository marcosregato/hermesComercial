# 🗄️ Configuração PostgreSQL para Hermes Comercial PDV

## 📋 Pré-requisitos

### 1. Instalar PostgreSQL
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install postgresql postgresql-contrib

# CentOS/RHEL
sudo yum install postgresql-server postgresql-contrib
sudo postgresql-setup initdb
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Windows
# Baixar e instalar do site oficial: https://www.postgresql.org/download/windows/
```

### 2. Configurar PostgreSQL
```bash
# Acessar PostgreSQL
sudo -u postgres psql

# Criar banco de dados
CREATE DATABASE hermescomercialdb;

# Criar usuário (opcional, se não usar postgres)
CREATE USER hermesuser WITH PASSWORD 'hermespass';
GRANT ALL PRIVILEGES ON DATABASE hermescomercialdb TO hermesuser;

# Sair do PostgreSQL
\q
```

### 3. Configurar Autenticação
Editar arquivo `pg_hba.conf` (geralmente em `/etc/postgresql/*/main/pg_hba.conf`):
```bash
# Adicionar ou modificar linha para conexão local
local   all             all                                     md5
host    all             all             127.0.0.1/32            md5
```

Reiniciar PostgreSQL:
```bash
sudo systemctl restart postgresql
```

## 🏗️ Estrutura do Banco de Dados

### Tabelas Necessárias
```sql
-- Tabela de Usuários
CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    telefone VARCHAR(20),
    cpf_cnpj VARCHAR(20),
    data_cadastro DATE DEFAULT CURRENT_DATE
);

-- Tabela de Login
CREATE TABLE login (
    id BIGSERIAL PRIMARY KEY,
    fk_usuario BIGINT REFERENCES usuario(id),
    login VARCHAR(50) NOT NULL,
    senha VARCHAR(100) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE
);

-- Criar usuário administrador padrão
INSERT INTO usuario (nome, email, telefone, cpf_cnpj) 
VALUES ('Administrador', 'admin@hermescomercial.com', '00000000000', '00000000000');

-- Obter ID do usuário criado
DO $$
DECLARE
    admin_id BIGINT;
BEGIN
    SELECT id INTO admin_id FROM usuario WHERE nome = 'Administrador';
    
    INSERT INTO login (fk_usuario, login, senha, ativo) 
    VALUES (admin_id, 'admin', 'admin123', TRUE);
END $$;
```

## 🔧 Configuração do Sistema

### 1. Verificar Configuração
Arquivo: `database-config.properties`
```properties
#Hermes Comercial - Configuração de Banco de Dados
database.description=Banco de dados PostgreSQL
database.name=hermescomercialdb
database.type=POSTGRESQL
database.host=localhost
database.port=5432
database.user=postgres
database.password=postgres
```

### 2. Testar Conexão
```bash
# Compilar e executar o sistema
javac -cp "src/main/java" src/main/java/com/br/hermescomercial/pdv/controller/PDVLoginSimpleControllerPostgreSQL.java
java -cp "src/main/java" com.br.hermescomercial.pdv.controller.PDVLoginSimpleControllerPostgreSQL
```

## 🚀 Como Usar

### 1. Iniciar o Sistema
```bash
java -cp "src/main/java" com.br.hermescomercial.pdv.controller.PDVLoginSimpleControllerPostgreSQL
```

### 2. Primeiro Acesso
1. Clique em **"Testar BD"** para verificar conexão
2. Se necessário, clique em **"Criar Admin"** para criar usuário
3. Faça login com: **admin / admin123**

### 3. Logs do Sistema
O sistema cria logs automáticos no diretório `logs/`:
- `logs/app.log` - Logs gerais
- `logs/auth.log` - Logs de autenticação
- `logs/database.log` - Logs do banco de dados
- `logs/error.log` - Logs de erros
- `logs/audit.log` - Logs de auditoria

## 🔍 Solução de Problemas

### Problema: "Falha na conexão PostgreSQL"
**Soluções:**
1. Verificar se PostgreSQL está rodando:
   ```bash
   sudo systemctl status postgresql
   ```

2. Verificar se banco existe:
   ```bash
   sudo -u postgres psql -l
   ```

3. Verificar configuração em `database-config.properties`

4. Testar conexão manual:
   ```bash
   psql -h localhost -U postgres -d hermescomercialdb
   ```

### Problema: "Driver PostgreSQL não encontrado"
**Solução:**
1. Adicionar driver PostgreSQL ao classpath
2. Verificar se arquivo `postgresql-*.jar` está disponível

### Problema: "Permissão negada"
**Solução:**
1. Verificar permissões do usuário PostgreSQL
2. Configurar corretamente `pg_hba.conf`

## 📊 Monitoramento

### Verificar Logs
```bash
# Verificar logs do sistema
tail -f logs/app.log
tail -f logs/database.log
tail -f logs/auth.log

# Verificar logs do PostgreSQL
sudo tail -f /var/log/postgresql/postgresql-*-main.log
```

### Estatísticas do Sistema
O sistema fornece estatísticas detalhadas via `SystemLogger.getLogStatistics()`

## 🎯 Benefícios do PostgreSQL

- ✅ **Robustez**: Banco de dados empresarial maduro
- ✅ **Performance**: Alto desempenho para grandes volumes
- ✅ **Segurança**: Autenticação robusta e criptografia
- ✅ **Escalabilidade**: Suporte a grandes volumes de dados
- ✅ **ACID**: Transações confiáveis
- ✅ **JSON/NoSQL**: Suporte a dados semi-estruturados

## 🔄 Backup e Recuperação

### Backup
```bash
# Backup completo
pg_dump -h localhost -U postgres hermescomercialdb > backup_hermes.sql

# Backup compactado
pg_dump -h localhost -U postgres hermescomercialdb | gzip > backup_hermes.sql.gz
```

### Restauração
```bash
# Restaurar backup
psql -h localhost -U postgres hermescomercialdb < backup_hermes.sql

# Restaurar backup compactado
gunzip -c backup_hermes.sql.gz | psql -h localhost -U postgres hermescomercialdb
```

---
*Configuração PostgreSQL para Hermes Comercial PDV v3.1.0*
