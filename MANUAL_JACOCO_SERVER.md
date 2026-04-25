# 📖 Manual de Uso - start_jacoco_server.sh

## 🎯 Visão Geral

O `start_jacoco_server.sh` é um script Bash para gerenciar facilmente um servidor HTTP local para visualizar os relatórios de cobertura de código JaCoCo do projeto Hermes Comercial.

## 🚀 Instalação e Preparação

### 1. Pré-requisitos
- **Python 3** instalado no sistema
- **Diretório target/site/jacoco** com relatórios JaCoCo gerados
- **Permissões de execução** no script

### 2. Configurar Permissões
```bash
chmod +x start_jacoco_server.sh
```

### 3. Gerar Relatórios (se necessário)
```bash
# Opção 1: Usar script completo de testes
./run_tests.sh

# Opção 2: Gerar apenas relatórios JaCoCo
mvn test jacoco:report

# Opção 3: Teste específico + relatório
mvn test -Dtest=UsuarioDaoTest jacoco:report
```

## 📋 Comandos Disponíveis

### 🟢 Iniciar Servidor
```bash
./start_jacoco_server.sh start
```

**O que acontece:**
- ✅ Verifica se o diretório `target/site/jacoco` existe
- 🔍 Tenta usar porta 8080 primeiro
- 🔄 Se 8080 ocupada, tenta porta 8081 automaticamente
- 🌐 Inicia servidor Python HTTP em background
- 💾 Salva PID do servidor em `.jacoco_server.pid`
- 📡 Exibe todos os links úteis

**Saída esperada:**
```
🌐 Iniciando servidor HTTP local...
📁 Diretório: /home/marcos/workspace/hermesComercial/target/site/jacoco
🔌 Porta: 8080
📡 URL: http://localhost:8080

✅ Servidor iniciado com sucesso!
📋 PID do servidor: 113922

📨 Links úteis:
   🏠 Página principal: http://localhost:8080
   📊 Relatório JaCoCo: http://localhost:8080/index.html
   🎨 Legenda de cores: http://localhost:8080/legenda-cores.html
   📦 Pacotes: http://localhost:8080/com.br.hermescomercial
```

### 🔴 Parar Servidor
```bash
./start_jacoco_server.sh stop
```

**O que acontece:**
- 📖 Lê PID do arquivo `.jacoco_server.pid`
- 🛑 Para o processo específico do servidor
- 🧹 Limpa arquivo PID
- 🔍 Mata todos os servidores Python HTTP restantes

**Saída esperada:**
```
✅ Servidor parado (PID: 113922)
💾 PID removido de .jacoco_server.pid
✅ Limpeza concluída
```

### 🔄 Reiniciar Servidor
```bash
./start_jacoco_server.sh restart
```

**O que acontece:**
- 1️⃣ Executa `stop` para parar servidor atual
- 2️⃣ Aguarda 1 segundo
- 3️⃣ Executa `start` para iniciar novo servidor

### 📊 Verificar Status
```bash
./start_jacoco_server.sh status
```

**O que acontece:**
- 🔍 Verifica portas 8080 e 8081
- 📋 Mostra processos usando as portas
- 💾 Verifica PID salvo
- ✅ Confirma se servidor JaCoCo está ativo

**Saída esperada:**
```
📊 Status dos Servidores HTTP Locais:

✅ Porta 8080: Ocupada
python3 113922 marcos    3u  IPv4 340087      0t0  TCP *:http-alt (LISTEN)

❌ Porta 8081: Livre

✅ Servidor JaCoCo rodando (PID: 113922)
```

### ❓ Ajuda
```bash
./start_jacoco_server.sh help
# ou
./start_jacoco_server.sh -h
# ou
./start_jacoco_server.sh --help
```

## 🌐 Acesso aos Relatórios

### Links Diretos Após Iniciar Servidor

| Descrição | Link | Finalidade |
|-----------|------|-----------|
| 🏠 **Página Principal** | `http://localhost:8080` | Visão geral do projeto |
| 📊 **Relatório JaCoCo** | `http://localhost:8080/index.html` | Relatório completo |
| 🎨 **Legenda de Cores** | `http://localhost:8080/legenda-cores.html` | Guia visual |
| 📦 **Pacotes** | `http://localhost:8080/com.br.hermescomercial` | Lista de pacotes |
| 🔍 **Busca** | `http://localhost:8080/com.br.hermescomercial.validation` | Pacotes específicos |

### Navegação Típica

1. **Acessar relatório principal**: http://localhost:8080/index.html
2. **Clicar em pacote desejado** (ex: `com.br.hermescomercial.validation`)
3. **Visualizar classe específica** (ex: `Validator.html`)
4. **Ver código fonte colorido** (ex: `Validator.java.html`)
5. **Consultar legenda** se necessário: http://localhost:8080/legenda-cores.html

## 🔧 Solução de Problemas

### ❌ "Diretório não encontrado"
```
❌ Diretório target/site/jacoco não encontrado!
```

**Solução:**
```bash
# Gerar relatórios primeiro
mvn test jacoco:report
# ou
./run_tests.sh
```

### ❌ "Porta ocupada"
```
⚠️  Porta 8080 ocupada, tentando porta 8081...
❌ Ambas portas 8080 e 8081 estão ocupadas
```

**Solução:**
```bash
# Parar servidores existentes
./start_jacoco_server.sh stop

# Ou manualmente
pkill -f "python3 -m http.server"

# Tentar novamente
./start_jacoco_server.sh start
```

### ❌ "Permissão negada"
```
bash: ./start_jacoco_server.sh: Permissão negada
```

**Solução:**
```bash
chmod +x start_jacoco_server.sh
```

### ❌ Servidor não inicia
```
❌ Falha ao iniciar servidor na porta 8080
```

**Solução:**
```bash
# Verificar se Python está instalado
python3 --version

# Tentar porta diferente manualmente
python3 -m http.server 8082 --directory target/site/jacoco
```

## 📝 Dicas e Boas Práticas

### ✅ Boas Práticas
1. **Sempare parar o servidor** após usar: `./start_jacoco_server.sh stop`
2. **Verificar status** antes de iniciar: `./start_jacoco_server.sh status`
3. **Usar restart** para atualizar após novos testes: `./start_jacoco_server.sh restart`
4. **Manter o script** no diretório raiz do projeto

### 🔄 Fluxo de Trabalho Típico
```bash
# 1. Executar testes e gerar relatórios
./run_tests.sh

# 2. Iniciar servidor para visualizar
./start_jacoco_server.sh start

# 3. Acessar http://localhost:8080 no navegador

# 4. Analisar cobertura de código

# 5. Parar servidor quando terminar
./start_jacoco_server.sh stop
```

### 📊 Interpretação dos Relatórios
- **🟢 Verde**: Código bem coberto (>80%)
- **🟡 Amarelo**: Cobertura média (50-79%)
- **🔴 Vermelho**: Baixa cobertura (<50%)
- **📋 Consulte a legenda**: http://localhost:8080/legenda-cores.html

## 🛠️ Arquivos e Configuração

### 📁 Arquivos Criados
- `.jacoco_server.pid` - PID do servidor em execução
- `target/site/jacoco/` - Diretório com relatórios JaCoCo
- `target/site/jacoco/legenda-cores.html` - Legenda de cores

### ⚙️ Configurações
- **Porta padrão**: 8080
- **Porta alternativa**: 8081
- **Diretório base**: `target/site/jacoco`
- **Tempo de espera**: 2 segundos para inicialização

## 📞 Suporte e Ajuda

### Comandos de Debug
```bash
# Ver todos os processos Python HTTP
ps aux | grep "python3 -m http.server"

# Ver portas em uso
netstat -tlnp | grep :808
# ou
lsof -i :8080

# Ver logs do servidor (se executado em foreground)
python3 -m http.server 8080 --directory target/site/jacoco
```

### Limpeza Completa
```bash
# Parar todos os servidores
pkill -f "python3 -m http.server"

# Remover arquivo PID
rm -f .jacoco_server.pid

# Verificar limpeza
./start_jacoco_server.sh status
```

---

## 🎉 Conclusão

O `start_jacoco_server.sh` facilita muito a visualização dos relatórios JaCoCo, permitindo análise rápida da cobertura de código do projeto Hermes Comercial.

**Lembre-se sempre:**
1. 🧪 Execute os testes primeiro
2. 🌐 Inicie o servidor
3. 📊 Analise os relatórios
4. 🛑 Pare o servidor ao final

Para dúvidas, execute `./start_jacoco_server.sh help` ou consulte este manual.
