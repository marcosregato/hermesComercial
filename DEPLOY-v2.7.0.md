# HERMES COMERCIAL PDV v2.7.0 - Guia de Deploy

## 📋 Visão Geral

O **Hermes Comercial PDV v2.7.0** é um sistema completo de ponto de venda desenvolvido em Java Swing com banco de dados SQLite. Esta versão inclui todas as correções e melhorias implementadas durante o desenvolvimento.

## 🎯 Novidades da v2.7.0

- ✅ **Tela de login simplificada** - Interface moderna e responsiva
- ✅ **Suporte a modo headless** - Funciona em servidores sem interface gráfica
- ✅ **Fallback automático** - Alterna entre modo gráfico e console
- ✅ **Banco de dados corrigido** - Estrutura otimizada de tabelas
- ✅ **Sistema de autenticação robusto** - Login admin/admin funcional
- ✅ **Executável nativo** - Script de inicialização automática
- ✅ **Instalação automática** - Script de deploy completo

## 📦 Pacote de Deploy

O pacote `deploy-v2.7.0` contém:

```
deploy-v2.7.0/
├── hermesComercial-2.7.0.jar          # Aplicação principal
├── hermes-pdv-v2.7.0.sh              # Executável Linux
├── install.sh                        # Script de instalação
├── database-config.properties         # Configuração do banco
├── hermescomercial.db                 # Banco de dados com dados iniciais
└── DEPLOY-v2.7.0.md                 # Este documento
```

## 🚀 Instalação Rápida

### Método 1: Instalação Automática (Recomendado)

```bash
# 1. Extraia o pacote
tar -xzf hermes-pdv-swing-v2.7.0.tar.gz
cd hermes-pdv-swing

# 2. Execute o instalador
./install.sh

# 3. Execute o sistema
~/hermes-pdv-swing/hermes-pdv-v2.7.0.sh
```

### Método 2: Instalação Manual

```bash
# 1. Crie diretório
mkdir -p ~/hermes-pdv-swing
cd ~/hermes-pdv-swing

# 2. Copie os arquivos
cp deploy-v2.7.0/* .

# 3. Torne executável
chmod +x hermes-pdv-v2.7.0.sh

# 4. Execute
./hermes-pdv-v2.7.0.sh
```

## 🔐 Credenciais Padrão

```
Usuário: admin
Senha: admin
Perfil: ADMIN
```

## 💻 Modos de Execução

### Modo Gráfico (Padrão)
```bash
./hermes-pdv-v2.7.0.sh
```

### Modo Headless (Servidores)
```bash
./hermes-pdv-v2.7.0.sh --headless
```

### Modo Console (Login Automático)
```bash
./hermes-pdv-v2.7.0.sh --console
```

### Ajuda
```bash
./hermes-pdv-v2.7.0.sh --help
```

## 🖥️ Requisitos do Sistema

### Mínimos
- **Sistema Operacional:** Linux (Ubuntu 18.04+, CentOS 7+, Fedora 30+)
- **Java:** OpenJDK 21 ou superior
- **Memória:** 512MB RAM
- **Espaço:** 100MB disco livre
- **Processador:** 1GHz ou superior

### Recomendados
- **Sistema Operacional:** Ubuntu 20.04+ ou CentOS 8+
- **Java:** OpenJDK 21
- **Memória:** 2GB RAM
- **Espaço:** 1GB disco livre
- **Processador:** 2GHz ou superior

## 🗄️ Banco de Dados

O sistema utiliza **SQLite** com as seguintes tabelas principais:

- `usuario` - Dados dos usuários
- `login` - Credenciais de autenticação
- `produto` - Cadastro de produtos
- `cliente` - Cadastro de clientes
- `venda` - Registros de vendas
- `notificacao` - Sistema de notificações

## 🔧 Configuração

### Banco de Dados
Edite `database-config.properties`:
```properties
database.type=SQLITE
database.name=hermescomercial.db
database.host=localhost
database.user=
database.password=
```

### Logs
Os logs são salvos em:
- `logs/application.log` - Logs da aplicação
- `logs/error.log` - Logs de erro

## 📱 Funcionalidades

### Módulos Disponíveis
- 🏪 **PDV** - Ponto de venda
- 📦 **Produtos** - Gestão de produtos
- 👥 **Clientes** - Gestão de clientes
- 💰 **Caixa** - Operações de caixa
- 📊 **Relatórios** - Relatórios e análises
- ⚙️ **Configurações** - Configurações do sistema

### Recursos
- ✅ Interface Swing moderna
- ✅ Banco de dados SQLite integrado
- ✅ Sistema de autenticação
- ✅ Relatórios em tempo real
- ✅ Backup automático
- ✅ Modo offline
- ✅ Multiusuário

## 🚨 Solução de Problemas

### Problemas Comuns

#### 1. Java não encontrado
```bash
# Ubuntu/Debian
sudo apt update && sudo apt install openjdk-21-jdk

# CentOS/RHEL
sudo yum install java-21-openjdk-devel

# Fedora
sudo dnf install java-21-openjdk-devel
```

#### 2. Permissão negada
```bash
chmod +x hermes-pdv-v2.7.0.sh
```

#### 3. Interface gráfica não abre
```bash
# Use modo headless
./hermes-pdv-v2.7.0.sh --headless
```

#### 4. Erro de conexão com banco
```bash
# Verifique se o arquivo hermescomercial.db existe
ls -la hermescomercial.db

# Verifique permissões
chmod 644 hermescomercial.db
```

### Logs de Erro
Verifique os logs em `logs/error.log` para diagnóstico detalhado.

## 🔄 Backup e Restauração

### Backup Automático
```bash
# Backup completo
./hermes-pdv-v2.7.0.sh --backup

# Backup apenas do banco
cp hermescomercial.db backups/hermescomercial-$(date +%Y%m%d).db
```

### Restauração
```bash
# Parar o sistema
pkill -f hermes-pdv-v2.7.0

# Restaurar banco
cp backups/hermescomercial-20240429.db hermescomercial.db

# Iniciar novamente
./hermes-pdv-v2.7.0.sh
```

## 📞 Suporte

### Canais de Suporte
- 📧 **Email:** support@hermescomercial.com
- 📚 **Documentação:** [Link para documentação online]
- 🐛 **Issues:** [Link para repositório GitHub]

### Informações para Suporte
Ao solicitar suporte, inclua:
- Versão do sistema: v2.7.0
- Sistema operacional
- Versão do Java
- Logs de erro relevantes
- Descrição detalhada do problema

## 📈 Atualizações

### Como Atualizar
```bash
# 1. Faça backup
./hermes-pdv-v2.7.0.sh --backup

# 2. Baixe nova versão
wget https://releases.hermescomercial.com/hermes-pdv-v2.7.1.tar.gz

# 3. Extraia e substitua
tar -xzf hermes-pdv-v2.7.1.tar.gz
cp hermes-pdv-v2.7.1/* .

# 4. Execute
./hermes-pdv-v2.7.1.sh
```

### Notas de Versão
- **v2.7.0** - Versão estável com todas as correções
- **v2.6.0** - Versão de desenvolvimento
- **v2.5.0** - Versão inicial

## 📄 Licença

Este software está licenciado sob a **Licença MIT**. Veja o arquivo `LICENSE.md` para detalhes.

---

**Hermes Comercial PDV v2.7.0**  
*Sistema de Ponto de Venda Completo*  
© 2026 Hermes Comercial. Todos os direitos reservados.
