#!/bin/bash

# Hermes Comercial PDV - Script de Execução
# Versão: 1.0.1
# Data: $(date +%Y-%m-%d)

echo "=========================================="
echo "    Hermes Comercial PDV v1.0.1"
echo "=========================================="
echo ""

# Verificar Java
if ! command -v java &> /dev/null; then
    echo "ERRO: Java não encontrado. Por favor, instale o Java 17 ou superior."
    exit 1
fi

# Verificar versão do Java
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "ERRO: Java 17 ou superior é necessário. Versão atual: $JAVA_VERSION"
    exit 1
fi

# Verificar PostgreSQL
if ! command -v psql &> /dev/null; then
    echo "AVISO: PostgreSQL client não encontrado. Certifique-se de que o PostgreSQL está instalado."
fi

# Diretório do aplicativo
APP_DIR="$(dirname "$0")"

# Verificar se o Maven está disponível
if ! command -v mvn &> /dev/null; then
    echo "ERRO: Maven não encontrado. Por favor, instale o Apache Maven."
    exit 1
fi

echo "Iniciando Hermes Comercial PDV..."
echo "Java: $(java -version 2>&1 | head -n 1)"
echo "Diretório: $APP_DIR"
echo ""

# Executar aplicação usando Maven (garante JavaFX disponível)
cd "$APP_DIR" || exit 1
exec mvn javafx:run "$@"
