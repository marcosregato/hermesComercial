#!/bin/bash

# Script de Deploy Automatizado - Hermes Comercial
# Realiza commit, push e criação de tags de versão

# Cores para saída
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Configurações
SCRIPT_NAME="deploy.sh"
VERSION_FILE="pom.xml"
DEFAULT_MESSAGE="Atualização do sistema"

# Função para imprimir status
print_status() {
    if [ "$1" -eq 0 ]; then
        echo -e "${GREEN}✅ $2 - SUCESSO${NC}"
    else
        echo -e "${RED}❌ $2 - FALHA${NC}"
    fi
}

# Função para imprimir cabeçalho
print_header() {
    echo -e "\n${CYAN}========================================${NC}"
    echo -e "${CYAN}     $1${NC}"
    echo -e "${CYAN}========================================${NC}"
}

# Função para imprimir seção
print_section() {
    echo -e "\n${YELLOW}🔄 $1${NC}"
}

# Função para executar comando e capturar resultado
run_command() {
    local command="$1"
    local description="$2"
    
    echo -e "${BLUE}💻 Executando: $command${NC}"
    
    eval "$command"
    local result=$?
    
    if [ $result -eq 0 ]; then
        echo -e "${GREEN}✅ $description${NC}"
    else
        echo -e "${RED}❌ Falha em: $description${NC}"
    fi
    
    return $result
}

# Função para verificar se está em um repositório Git
check_git_repo() {
    if [ ! -d ".git" ]; then
        echo -e "${RED}❌ Este diretório não é um repositório Git${NC}"
        exit 1
    fi
    echo -e "${GREEN}✅ Repositório Git detectado${NC}"
}

# Função para verificar status do Git
check_git_status() {
    print_section "VERIFICANDO STATUS DO REPOSITÓRIO"
    
    local status_output
    status_output=$(git status --porcelain)
    if [ -z "$status_output" ]; then
        echo -e "${YELLOW}⚠️  Nenhuma alteração pendente encontrada${NC}"
        return 1
    else
        echo -e "${GREEN}✅ Alterações pendentes detectadas${NC}"
        echo -e "${BLUE}📋 Arquivos modificados:${NC}"
        echo "$status_output" | while read -r line; do
            echo -e "   ${CYAN}• $line${NC}"
        done
        return 0
    fi
}

# Função para obter versão atual do pom.xml
get_current_version() {
    if [ -f "$VERSION_FILE" ]; then
        local version
    version=$(grep -oP '(?<=<version>)[^<]+' "$VERSION_FILE" | head -1)
        echo "$version"
    else
        echo "1.0.0"
    fi
}

# Função para incrementar versão
increment_version() {
    local current_version="$1"
    local type="$2" # major, minor, patch
    
    IFS='.' read -ra VERSION_PARTS <<< "$current_version"
    local major=${VERSION_PARTS[0]}
    local minor=${VERSION_PARTS[1]}
    local patch=${VERSION_PARTS[2]}
    
    case "$type" in
        "major")
            major=$((major + 1))
            minor=0
            patch=0
            ;;
        "minor")
            minor=$((minor + 1))
            patch=0
            ;;
        "patch")
            patch=$((patch + 1))
            ;;
        *)
            echo -e "${RED}❌ Tipo de versão inválido: $type${NC}"
            echo -e "${BLUE}💡 Use: major, minor ou patch${NC}"
            exit 1
            ;;
    esac
    
    echo "${major}.${minor}.${patch}"
}

# Função para atualizar versão no pom.xml
update_version() {
    local new_version="$1"
    
    print_section "ATUALIZANDO VERSÃO PARA $new_version"
    
    if [ -f "$VERSION_FILE" ]; then
        sed -i "s|<version>.*</version>|<version>$new_version</version>|" "$VERSION_FILE"
        print_status $? "Versão atualizada no $VERSION_FILE"
    else
        echo -e "${YELLOW}⚠️  Arquivo $VERSION_FILE não encontrado${NC}"
    fi
}

# Função para adicionar arquivos ao Git
add_files() {
    print_section "ADICIONANDO ARQUIVOS AO GIT"
    
    # Adiciona todos os arquivos modificados
    run_command "git add ." "Adicionando arquivos modificados"
    return $?
}

# Função para realizar commit
commit_changes() {
    local message="$1"
    
    print_section "REALIZANDO COMMIT"
    
    if [ -z "$message" ]; then
        message="$DEFAULT_MESSAGE"
    fi
    
    run_command "git commit -m \"$message\"" "Commit realizado"
    return $?
}

# Função para realizar push
push_changes() {
    print_section "ENVIANDO ALTERAÇÕES PARA REPOSITÓRIO REMOTO"
    
    run_command "git push" "Push enviado"
    return $?
}

# Função para criar tag
create_tag() {
    local version="$1"
    local tag_name="v$version"
    local tag_message="$2"
    
    print_section "CRIANDO TAG $tag_name"
    
    if [ -z "$tag_message" ]; then
        tag_message="Versão $version - Hermes Comercial"
    fi
    
    run_command "git tag -a \"$tag_name\" -m \"$tag_message\"" "Tag criada"
    return $?
}

# Função para enviar tag
push_tag() {
    local version="$1"
    local tag_name="v$version"
    
    print_section "ENVIANDO TAG PARA REPOSITÓRIO REMOTO"
    
    run_command "git push origin \"$tag_name\"" "Tag enviada"
    return $?
}

# Função para mostrar ajuda
show_help() {
    echo -e "${CYAN}Uso: $SCRIPT_NAME [OPÇÕES]${NC}"
    echo -e "\n${YELLOW}OPÇÕES:${NC}"
    echo -e "  ${BLUE}-h, --help${NC}          Mostra esta ajuda"
    echo -e "  ${BLUE}-m, --message${NC}       Mensagem do commit (padrão: '$DEFAULT_MESSAGE')"
    echo -e "  ${BLUE}-v, --version${NC}       Versão específica para tag"
    echo -e "  ${BLUE}-t, --type${NC}         Tipo de incremento (major|minor|patch)"
    echo -e "  ${BLUE}-a, --auto${NC}         Incremento automático de versão (patch)"
    echo -e "  ${BLUE}-s, --skip-tests${NC}   Pular execução de testes"
    echo -e "  ${BLUE}-c, --commit-only${NC}   Apenas commit e push (sem tag)"
    echo -e "\n${YELLOW}EXEMPLOS:${NC}"
    echo -e "  ${GREEN}$SCRIPT_NAME${NC}                           Deploy completo"
    echo -e "  ${GREEN}$SCRIPT_NAME -m 'Nova funcionalidade'${NC}  Deploy com mensagem personalizada"
    echo -e "  ${GREEN}$SCRIPT_NAME -v 1.2.0${NC}                  Deploy com versão específica"
    echo -e "  ${GREEN}$SCRIPT_NAME -t minor${NC}                 Deploy com incremento de versão"
    echo -e "  ${GREEN}$SCRIPT_NAME -a${NC}                       Deploy com auto incremento"
    echo -e "  ${GREEN}$SCRIPT_NAME -c${NC}                       Apenas commit e push"
    echo -e "\n${YELLOW}FLUXO DE TRABALHO:${NC}"
    echo -e "  1. Verifica status do repositório"
    echo -e "  2. (Opcional) Atualiza versão"
    echo -e "  3. Adiciona arquivos ao Git"
    echo -e "  4. Realiza commit"
    echo -e "  5. Envia para repositório remoto"
    echo -e "  6. (Opcional) Cria e envia tag"
}

# Função para mostrar resumo final
show_summary() {
    local version="$1"
    local commit_only="$2"
    
    echo -e "\n${CYAN}========================================${NC}"
    echo -e "${CYAN}           RESUMO DO DEPLOY${NC}"
    echo -e "${CYAN}========================================${NC}"
    
    if [ "$commit_only" = "true" ]; then
        echo -e "${GREEN}✅ Commit e Push realizados${NC}"
    else
        echo -e "${GREEN}✅ Deploy completo realizado${NC}"
        echo -e "${BLUE}📦 Versão: $version${NC}"
        echo -e "${BLUE}🏷️  Tag: v$version${NC}"
    fi
    
    echo -e "\n${YELLOW}🚀 Próximos passos:${NC}"
    echo -e "  • Verificar no repositório remoto"
    echo -e "  • Executar testes: ./run_tests.sh"
    echo -e "  • Verificar tags: git tag -l"
    
    echo -e "\n${CYAN}========================================${NC}"
}

# Função principal
main() {
    # Variáveis padrão
    message=""
    version=""
    version_type=""
    auto_increment=false
    skip_tests=false
    commit_only=false
    
    # Parse de argumentos
    while [[ $# -gt 0 ]]; do
        case $1 in
            -h|--help)
                show_help
                exit 0
                ;;
            -m|--message)
                message="$2"
                shift 2
                ;;
            -v|--version)
                version="$2"
                shift 2
                ;;
            -t|--type)
                version_type="$2"
                shift 2
                ;;
            -a|--auto)
                auto_increment=true
                shift
                ;;
            -s|--skip-tests)
                skip_tests=true
                shift
                ;;
            -c|--commit-only)
                commit_only=true
                shift
                ;;
            *)
                echo -e "${RED}❌ Opção desconhecida: $1${NC}"
                show_help
                exit 1
                ;;
        esac
    done
    
    # Cabeçalho
    print_header "🚀 DEPLOY AUTOMATIZADO - HERMES COMERCIAL"
    
    # Verificações iniciais
    check_git_repo
    
    # Executar testes (se não for pular)
    if [ "$skip_tests" = false ]; then
        print_section "EXECUTANDO TESTES"
        if [ -f "run_tests.sh" ]; then
            if ! run_command "./run_tests.sh" "Testes executados"; then
                echo -e "${YELLOW}⚠️  Testes falharam, mas continuando deploy...${NC}"
            fi
        else
            echo -e "${YELLOW}⚠️  Script de testes não encontrado${NC}"
        fi
    fi
    
    # Verificar status
    if ! check_git_status; then
        echo -e "${YELLOW}⚠️  Nenhuma alteração para commit${NC}"
        echo -e "${BLUE}💡 Use 'git status' para verificar${NC}"
        exit 0
    fi
    
    # Lógica de versionamento
    if [ "$commit_only" = false ]; then
        if [ -z "$version" ]; then
            if [ "$auto_increment" = true ]; then
                version_type="patch"
            fi
            
            if [ -n "$version_type" ]; then
                current_version=$(get_current_version)
                version=$(increment_version "$current_version" "$version_type")
                update_version "$version"
                echo -e "${GREEN}✅ Versão incrementada: $current_version → $version${NC}"
            else
                version=$(get_current_version)
            fi
        fi
    fi
    
    # Fluxo de deploy
    local success=true
    
    # 1. Adicionar arquivos
    if ! add_files; then
        success=false
    fi
    
    # 2. Commit
    if [ "$success" = true ]; then
        if ! commit_changes "$message"; then
            success=false
        fi
    fi
    
    # 3. Push
    if [ "$success" = true ]; then
        if ! push_changes; then
            success=false
        fi
    fi
    
    # 4. Tag (se não for commit-only)
    if [ "$success" = true ] && [ "$commit_only" = false ] && [ -n "$version" ]; then
        if ! create_tag "$version" "$message"; then
            success=false
        fi
        
        if [ "$success" = true ]; then
            if ! push_tag "$version"; then
                success=false
            fi
        fi
    fi
    
    # Resumo final
    if [ "$success" = true ]; then
        show_summary "$version" "$commit_only"
        echo -e "\n${GREEN}🎉 DEPLOY REALIZADO COM SUCESSO!${NC}"
        exit 0
    else
        echo -e "\n${RED}❌ FALHA NO DEPLOY${NC}"
        echo -e "${BLUE}💡 Verifique os erros acima e tente novamente${NC}"
        exit 1
    fi
}

# Executa função principal
main "$@"
