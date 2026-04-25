#!/bin/bash

# Script de Execução de Testes - Hermes Comercial
# Executa testes unitários, gera relatórios de cobertura e abre resultados

# Cores para saída
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Função para imprimir status
print_status() {
    if [ "$1" -eq 0 ]; then
        echo -e "${GREEN}✅ $2 - SUCESSO${NC}"
    else
        echo -e "${RED}❌ $2 - FALHA${NC}"
    fi
}

# Função para executar teste e capturar resultado
run_test() {
    local test_name="$1"
    local test_command="$2"
    
    echo -e "\n${BLUE}🧪 Executando: $test_name${NC}"
    echo "Comando: $test_command"
    
    eval "$test_command" > /dev/null 2>&1
    local result=$?
    
    print_status "$result" "$test_name"
    return "$result"
}

# Contadores
total_tests=0
passed_tests=0
failed_tests=0

echo -e "\n${YELLOW}📋 INICIANDO SUITE DE TESTES...${NC}"

# 1. Testes Unitários de DAO
echo -e "\n${YELLOW}🔄 TESTES UNITÁRIOS DE DAO${NC}"

if run_test "UsuarioDaoTest" "mvn test -Dtest=UsuarioDaoTest -DfailIfNoTests=false -q"; then 
    passed_tests=$((passed_tests + 1))
else
    failed_tests=$((failed_tests + 1))
fi
total_tests=$((total_tests + 1))

if run_test "ProdutoDaoTest" "mvn test -Dtest=ProdutoDaoTest -DfailIfNoTests=false -q"; then 
    passed_tests=$((passed_tests + 1))
else
    failed_tests=$((failed_tests + 1))
fi
total_tests=$((total_tests + 1))

if run_test "AtributoDaoTest" "mvn test -Dtest=AtributoDaoTest -DfailIfNoTests=false -q"; then 
    passed_tests=$((passed_tests + 1))
else
    failed_tests=$((failed_tests + 1))
fi
total_tests=$((total_tests + 1))

# 2. Testes de Controller
echo -e "\n${YELLOW}🔄 TESTES DE CONTROLLER${NC}"

if run_test "UsuarioControllerTest" "mvn test -Dtest=UsuarioControllerTest -DfailIfNoTests=false -q"; then 
    passed_tests=$((passed_tests + 1))
else
    failed_tests=$((failed_tests + 1))
fi
total_tests=$((total_tests + 1))

if run_test "AtributoControllerTest" "mvn test -Dtest=AtributoContrellerTest -DfailIfNoTests=false -q"; then 
    passed_tests=$((passed_tests + 1))
else
    failed_tests=$((failed_tests + 1))
fi
total_tests=$((total_tests + 1))

if run_test "CustoControllerTest" "mvn test -Dtest=CustoControllerTest -DfailIfNoTests=false -q"; then 
    passed_tests=$((passed_tests + 1))
else
    failed_tests=$((failed_tests + 1))
fi
total_tests=$((total_tests + 1))

# 3. Testes Funcionais de Integração
echo -e "\n${YELLOW}🔄 TESTES FUNCIONAIS DE INTEGRAÇÃO${NC}"

if run_test "Testes de Integração" "mvn test -Dtest=FunctionalAlternativeTest -DfailIfNoTests=false -q"; then 
    passed_tests=$((passed_tests + 1))
else
    failed_tests=$((failed_tests + 1))
fi
total_tests=$((total_tests + 1))

# 4. Teste Completo do Sistema
echo -e "\n${YELLOW}🔄 TESTE COMPLETO DO SISTEMA${NC}"

if run_test "Todos os Testes" "mvn test -DfailIfNoTests=false -q"; then 
    passed_tests=$((passed_tests + 1))
else
    failed_tests=$((failed_tests + 1))
fi
total_tests=$((total_tests + 1))

# 5. Compilação e Build
echo -e "\n${YELLOW}🔨 COMPILAÇÃO E BUILD${NC}"

if run_test "Compilação" "mvn compile -q"; then 
    passed_tests=$((passed_tests + 1))
else
    failed_tests=$((failed_tests + 1))
fi
total_tests=$((total_tests + 1))

if run_test "Compilação de Testes" "mvn test-compile -q"; then 
    passed_tests=$((passed_tests + 1))
else
    failed_tests=$((failed_tests + 1))
fi
total_tests=$((total_tests + 1))

if run_test "Build Completo" "mvn clean package -DskipTests -q"; then 
    passed_tests=$((passed_tests + 1))
else
    failed_tests=$((failed_tests + 1))
fi
total_tests=$((total_tests + 1))

# 6. Relatórios e Análise
echo -e "\n${YELLOW}📊 RELATÓRIOS E ANÁLISE${NC}"

# Primeiro executar testes para gerar dados Jacoco
echo -e "${BLUE}🧪 Executando testes para gerar dados de cobertura...${NC}"
mvn test -Dtest=UsuarioDaoTest -DfailIfNoTests=false -q

# Depois gerar relatório Jacoco
if run_test "Cobertura de Código" "mvn jacoco:report -q"; then 
    passed_tests=$((passed_tests + 1))
else
    failed_tests=$((failed_tests + 1))
fi
total_tests=$((total_tests + 1))

# 7. Abrir Relatório de Cobertura Automaticamente
echo -e "\n${BLUE}🌐 GERANDO E ABRINDO RELATÓRIO DE COBERTURA...${NC}"

# Garantir que o relatório seja gerado e aberto
if [ -f "target/jacoco.exec" ] || [ -d "target/site/jacoco" ]; then
    echo -e "${BLUE}🔄 Gerando relatório de cobertura final...${NC}"
    mvn jacoco:report -q > /dev/null 2>&1
    
    if [ -f "target/site/jacoco/index.html" ]; then
        echo -e "${GREEN}✅ Relatório encontrado, traduzindo e abrindo no navegador...${NC}"
        
        # Traduzir relatório se existir o script
        if [ -f "adicionar_legenda_classes_v2.sh" ]; then
            ./adicionar_legenda_classes_v2.sh > /dev/null 2>&1
        fi
        
        # Abrir no navegador em background
        firefox target/site/jacoco/index.html 2>/dev/null &
        echo -e "${GREEN}✅ Relatório de cobertura aberto com sucesso!${NC}"
        echo -e "${BLUE}📁 URL: file://$(pwd)/target/site/jacoco/index.html${NC}"
    else
        echo -e "${YELLOW}⚠️  Relatório não encontrado após geração${NC}"
    fi
else
    echo -e "${YELLOW}⚠️  Nenhum dado de cobertura encontrado, gerando relatório básico...${NC}"
    mvn test -Dtest=UsuarioDaoTest -DfailIfNoTests=false -q > /dev/null 2>&1
    mvn jacoco:report -q > /dev/null 2>&1
    
    if [ -f "target/site/jacoco/index.html" ]; then
        echo -e "${GREEN}✅ Relatório gerado e abrindo no navegador...${NC}"
        firefox target/site/jacoco/index.html 2>/dev/null &
    else
        echo -e "${RED}❌ Não foi possível gerar o relatório de cobertura${NC}"
    fi
fi

# 8. Resumo Final
echo -e "\n${BLUE}🎨 LEGENDA DE CORES:${NC}"
echo -e "   ${GREEN}✅ VERDE${NC} - Sucesso, testes passando, sistema funcionando"
echo -e "   ${RED}❌ VERMELHO${NC} - Falha, testes quebrando, erros críticos"
echo -e "   ${YELLOW}⚠️  AMARELO${NC} - Alerta, avisos, atenção necessária"
echo -e "   ${BLUE}🔵 AZUL${NC} - Informações, comandos, status geral"

echo -e "\n=========================================="
echo -e "           ${BLUE}RESUMO DA EXECUÇÃO${NC}"
echo "=========================================="
echo -e "Total de Testes: ${YELLOW}$total_tests${NC}"
echo -e "Testes Passaram: ${GREEN}$passed_tests${NC}"
echo -e "Testes Falharam: ${RED}$failed_tests${NC}"

if [ $failed_tests -eq 0 ]; then
    echo -e "\n${GREEN}🎉 TODOS OS TESTES PASSARAM!${NC}"
    echo -e "${GREEN}✅ Sistema está funcionando corretamente${NC}"
    exit_code=0
else
    echo -e "\n${YELLOW}⚠️  ALGUNS TESTES FALHARAM${NC}"
    echo -e "${BLUE}📋 Verifique os logs detalhados para mais informações${NC}"
    exit_code=1
fi

echo -e "\n${BLUE}📁 Relatórios gerados em:${NC}"
echo -e "   • target/surefire-reports/ - Relatórios de testes"
echo -e "   • target/site/jacoco/ - Relatório de cobertura"

echo -e "\n${BLUE}🧪 TESTES REALMENTE EXECUTADOS:${NC}"
echo -e "   📋 UsuarioDaoTest - Testes de DAO de usuários"
echo -e "   📋 ProdutoDaoTest - Testes de DAO de produtos"
echo -e "   📋 AtributoDaoTest - Testes de DAO de atributos"
echo -e "   📋 UsuarioControllerTest - Testes de controller de usuários"
echo -e "   📋 AtributoControllerTest - Testes de controller de atributos"
echo -e "   📋 CustoControllerTest - Testes de controller de custos"

echo -e "\n${BLUE}🚀 Comandos úteis:${NC}"
echo -e "   • Ver falhas: cat target/surefire-reports/*FAILED*"
echo -e "   • Ver cobertura: firefox target/site/jacoco/index.html"
echo -e "   • Teste específico: mvn test -Dtest=NomeDoTeste"

echo -e "\n=========================================="
echo -e "           ${BLUE}FIM DA EXECUÇÃO DE TESTES${NC}"
echo "=========================================="

exit $exit_code
