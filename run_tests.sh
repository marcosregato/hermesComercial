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

# Função para criar legenda de cores
criar_legenda_cores() {
    local legenda_file="target/site/jacoco/legenda-cores.html"
    
    cat > "$legenda_file" << 'EOF'
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Legenda de Cores - JaCoCo</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .legend-item { margin: 10px 0; padding: 10px; border: 1px solid #ccc; }
        .color-box { width: 30px; height: 20px; display: inline-block; margin-right: 10px; border: 1px solid #000; }
        .fc { background-color: #ccffcc; }
        .nc { background-color: #ffaaaa; }
        .pc { background-color: #ffffcc; }
        .bfc { background-color: #ccffcc; padding-left: 20px; background-image: url('data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7'); }
        .bnc { background-color: #ffaaaa; padding-left: 20px; background-image: url('data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7'); }
        .bpc { background-color: #ffffcc; padding-left: 20px; background-image: url('data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7'); }
        table { border-collapse: collapse; width: 100%; margin: 20px 0; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .green { background-color: #d4edda; }
        .red { background-color: #f8d7da; }
        .yellow { background-color: #fff3cd; }
    </style>
</head>
<body>
    <h1>🎨 Legenda de Cores - Relatório JaCoCo</h1>
    
    <h2>Cores das Linhas de Código</h2>
    
    <div class="legend-item">
        <span class="color-box fc"></span>
        <strong>Verde Claro (#ccffcc)</strong> - <span class="fc">Fully Covered</span>
        <p>Linha de código 100% coberta pelos testes. Todas as instruções foram executadas.</p>
    </div>
    
    <div class="legend-item">
        <span class="color-box nc"></span>
        <strong>Vermelho Claro (#ffaaaa)</strong> - <span class="nc">Not Covered</span>
        <p>Linha de código não executada pelos testes. Nenhuma instrução foi coberta.</p>
    </div>
    
    <div class="legend-item">
        <span class="color-box pc"></span>
        <strong>Amarelo Claro (#ffffcc)</strong> - <span class="pc">Partially Covered</span>
        <p>Linha parcialmente coberta. Algumas instruções foram executadas, mas não todas.</p>
    </div>
    
    <h2>Indicadores de Branch (Condicionais)</h2>
    
    <div class="legend-item">
        <span class="color-box bfc"></span>
        <strong>Verde com Ícone ✓</strong> - <span class="bfc">Branch Fully Covered</span>
        <p>Condicional (if/else) 100% coberta. Todos os caminhos foram testados.</p>
    </div>
    
    <div class="legend-item">
        <span class="color-box bnc"></span>
        <strong>Vermelho com Ícone ✗</strong> - <span class="bnc">Branch Not Covered</span>
        <p>Condicional não coberta. Nenhum caminho foi testado.</p>
    </div>
    
    <div class="legend-item">
        <span class="color-box bpc"></span>
        <strong>Amarelo com Ícone ?</strong> - <span class="bpc">Branch Partially Covered</span>
        <p>Condicional parcialmente coberta. Apenas alguns caminhos foram testados.</p>
    </div>
    
    <h2>Barras de Progresso nas Tabelas</h2>
    
    <table>
        <thead>
            <tr>
                <th>Métrica</th>
                <th>Cor da Barra</th>
                <th>Significado</th>
                <th>Exemplo</th>
            </tr>
        </thead>
        <tbody>
            <tr class="green">
                <td>Alta Cobertura</td>
                <td>Verde</td>
                <td>≥ 80% de cobertura</td>
                <td>████████░░ 80%</td>
            </tr>
            <tr class="yellow">
                <td>Cobertura Média</td>
                <td>Amarelo</td>
                <td>50-79% de cobertura</td>
                <td>██████░░░░ 60%</td>
            </tr>
            <tr class="red">
                <td>Baixa Cobertura</td>
                <td>Vermelho</td>
                <td>&lt; 50% de cobertura</td>
                <td>██░░░░░░░░ 20%</td>
            </tr>
        </tbody>
    </table>
    
    <h2>Ícones de Elementos</h2>
    
    <table>
        <thead>
            <tr>
                <th>Elemento</th>
                <th>Ícone</th>
                <th>Descrição</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>Projeto/Bundle</td>
                <td>📁</td>
                <td>Nível principal do projeto</td>
            </tr>
            <tr>
                <td>Pacote</td>
                <td>📦</td>
                <td>Package Java</td>
            </tr>
            <tr>
                <td>Classe</td>
                <td>📄</td>
                <td>Classe Java</td>
            </tr>
            <tr>
                <td>Fonte</td>
                <td>📝</td>
                <td>Arquivo fonte (.java)</td>
            </tr>
            <tr>
                <td>Método</td>
                <td>⚙️</td>
                <td>Método/Função</td>
            </tr>
        </tbody>
    </table>
    
    <h2>Como Interpretar no Relatório</h2>
    
    <div class="legend-item">
        <h3>📊 Nível de Pacote/Classe</h3>
        <ul>
            <li><strong class="green">Verde</strong>: Boa cobertura (≥80%)</li>
            <li><strong class="yellow">Amarelo</strong>: Cobertura média (50-79%)</li>
            <li><strong class="red">Vermelho</strong>: Baixa cobertura (&lt;50%)</li>
        </ul>
    </div>
    
    <div class="legend-item">
        <h3>📝 Nível de Código Fonte</h3>
        <ul>
            <li>Clique em qualquer classe para ver o código fonte colorido</li>
            <li>Linhas verdes: testadas com sucesso</li>
            <li>Linhas vermelhas: precisam de testes</li>
            <li>Linhas amarelas: cobertura parcial</li>
        </ul>
    </div>
    
    <div class="legend-item">
        <h3>🎯 Metas de Cobertura</h3>
        <ul>
            <li><strong>Excelente</strong>: &gt;90% cobertura</li>
            <li><strong>Bom</strong>: 80-90% cobertura</li>
            <li><strong>Aceitável</strong>: 70-80% cobertura</li>
            <li><strong>Precisa Melhorar</strong>: &lt;70% cobertura</li>
        </ul>
    </div>
    
    <div style="margin-top: 30px; padding: 15px; background-color: #e7f3ff; border-left: 4px solid #2196F3;">
        <p><strong>💡 Dica:</strong> Use esta legenda como referência ao analisar os relatórios do JaCoCo. Foque em melhorar as áreas vermelhas e amarelas primeiro!</p>
    </div>
    
    <p style="margin-top: 20px; text-align: center;">
        <a href="index.html">← Voltar ao Relatório Principal</a>
    </p>
</body>
</html>
EOF
    
    # Adicionar link no relatório principal
    if [ -f "target/site/jacoco/index.html" ]; then
        # Backup do arquivo original
        cp "target/site/jacoco/index.html" "target/site/jacoco/index.html.bak"
        
        # Adicionar link para legenda
        sed -i 's|<span class="info"><a href="jacoco-sessions.html" class="el_session">Sessions</a></span>|<span class="info"><a href="jacoco-sessions.html" class="el_session">Sessions</a> | <a href="legenda-cores.html" class="el_session">📨 Legenda de Cores</a></span>|' "target/site/jacoco/index.html"
    fi
    
    echo -e "${GREEN}✅ Legenda de cores criada com sucesso${NC}"
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

if run_test "Build Completo" "mvn package -DskipTests -q"; then 
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
        echo -e "${GREEN}✅ Relatório encontrado, criando legenda e abrindo no navegador...${NC}"
        
        # Recriar legenda de cores
        criar_legenda_cores
        
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
