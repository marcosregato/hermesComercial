# Distribuição com jpackage - Guia de Instaladores Nativos

## Visão Geral

O sistema Hermes Comercial utiliza **jpackage** (Java 14+) para criar instaladores nativos para distribuição em produção. Esta abordagem é ideal para sistemas desktop Swing.

## Pré-requisitos

- JDK 14 ou superior (jpackage incluído)
- Maven 3.8+
- Sistema operacional alvo para build:
  - Windows: Windows 10+
  - macOS: macOS 10.13+
  - Linux: Ubuntu 18.04+, Debian 10+, RHEL 8+

## Estrutura

```
.
├── build-installer.sh      # Script para Linux/macOS
├── build-installer.bat     # Script para Windows
├── pom.xml                 # Plugin jpackage-maven-plugin
└── docs/JPACKAGE.md        # Este documento
```

## Build de Instaladores

### Linux/macOS

```bash
# Dar permissão de execução
chmod +x build-installer.sh

# Executar build
./build-installer.sh
```

### Windows

```cmd
# Executar script
build-installer.bat
```

### Manual com jpackage

```bash
# Criar instalador para Windows
jpackage \
  --name "Hermes PDV" \
  --app-version "3.2.0" \
  --vendor "Hermes Comercial" \
  --main-class "com.br.hermescomercial.MainApplication" \
  --main-jar target/hermespdv-standalone.jar \
  --type exe \
  --dest target/dist \
  --input target \
  --java-options "-Xmx1024m" \
  --java-options "-Xms512m" \
  --win-menu \
  --win-dir-chooser \
  --win-shortcut

# Criar instalador para Linux (DEB)
jpackage \
  --name "Hermes PDV" \
  --app-version "3.2.0" \
  --vendor "Hermes Comercial" \
  --main-class "com.br.hermescomercial.MainApplication" \
  --main-jar target/hermespdv-standalone.jar \
  --type deb \
  --dest target/dist \
  --input target \
  --java-options "-Xmx1024m" \
  --java-options "-Xms512m" \
  --linux-package-name hermespdv \
  --linux-menu-group "Office" \
  --linux-shortcut

# Criar instalador para macOS (DMG)
jpackage \
  --name "Hermes PDV" \
  --app-version "3.2.0" \
  --vendor "Hermes Comercial" \
  --main-class "com.br.hermescomercial.MainApplication" \
  --main-jar target/hermespdv-standalone.jar \
  --type dmg \
  --dest target/dist \
  --input target \
  --java-options "-Xmx1024m" \
  --java-options "-Xms512m" \
  --mac-package-name "HermesPDV" \
  --mac-package-identifier "com.hermescomercial.pdv"
```

## Tipos de Instaladores

### Windows

- **EXE**: Instalador nativo Windows
- **MSI**: Pacote Windows Installer (opcional)

**Recursos:**
- Menu Iniciar
- Atalho na área de trabalho
- Escolha de diretório de instalação
- Associação de arquivos (.hermes)
- Desinstalação via Painel de Controle

### Linux

- **DEB**: Debian/Ubuntu
- **RPM**: Red Hat/CentOS/Fedora

**Recursos:**
- Menu de aplicativos
- Atalhos
- Integração com sistema de pacotes
- Gerenciamento de dependências

### macOS

- **DMG**: Imagem de disco macOS
- **PKG**: Pacote de instalação (opcional)

**Recursos:**
- Arrastar para Applications
- Integração com Launchpad
- Assinatura de código (opcional)

## Configuração do jpackage

### Opções Principais

| Opção | Descrição | Padrão |
|-------|-----------|--------|
| `--name` | Nome da aplicação | Hermes PDV |
| `--app-version` | Versão da aplicação | 3.2.0 |
| `--vendor` | Nome do fornecedor | Hermes Comercial |
| `--main-class` | Classe principal | MainApplication |
| `--main-jar` | JAR principal | hermespdv-standalone.jar |
| `--type` | Tipo de instalador | exe/deb/dmg |
| `--dest` | Diretório de saída | target/dist |
| `--input` | Diretório de entrada | target |

### Opções de JVM

```bash
--java-options "-Xmx1024m"  # Heap máximo 1GB
--java-options "-Xms512m"   # Heap inicial 512MB
--java-options "-Dfile.encoding=UTF-8"
```

### Opções Específicas por Plataforma

#### Windows
```bash
--win-menu              # Adicionar ao Menu Iniciar
--win-dir-chooser       # Permitir escolher diretório
--win-shortcut          # Criar atalho
--win-menu-group        # Grupo do menu
--win-per-user-install  # Instalação por usuário
```

#### Linux
```bash
--linux-package-name    # Nome do pacote
--linux-deb-maintainer  # Mantenedor do DEB
--linux-menu-group      # Grupo do menu
--linux-shortcut        # Criar atalho
--linux-rpm-license-type # Tipo de licença RPM
```

#### macOS
```bash
--mac-package-name           # Nome do pacote
--mac-package-identifier     # Identificador do bundle
--mac-sign                   # Assinar aplicação
--mac-signing-keychain       # Keychain para assinatura
```

## Customização

### Ícone da Aplicação

```bash
--icon resources/icon.ico    # Windows
--icon resources/icon.png    # Linux/macOS
```

### Splash Screen

```bash
--icon resources/splash.png
```

### Arquivos Adicionais

```bash
--add-launcher custom=resources/custom-launcher.properties
```

## Assinatura de Código

### Windows

```bash
# Requer certificado de código
--win-console
--win-dir-chooser
```

### macOS

```bash
# Requer certificado Apple Developer
--mac-sign
--mac-signing-keychain login.keychain
--mac-signing-identity "Developer ID Application: Your Name"
```

## Distribuição

### Windows

```bash
# Upload para servidor
scp target/dist/Hermes\ PDV-3.2.0.exe user@server:/releases/

# Criar checksum
sha256sum target/dist/Hermes\ PDV-3.2.0.exe > Hermes-PDV-3.2.0.exe.sha256
```

### Linux

```bash
# Upload para repositório APT (DEB)
scp target/dist/hermespdv_3.2.0_amd64.deb repo@server:/var/www/apt/pool/

# Upload para repositório YUM (RPM)
scp target/dist/hermespdv-3.2.0-1.x86_64.rpm repo@server:/var/www/yum/
```

### macOS

```bash
# Upload para servidor
scp target/dist/Hermes\ PDV-3.2.0.dmg user@server:/releases/

# Criar checksum
shasum -a 256 target/dist/Hermes\ PDV-3.2.0.dmg > Hermes-PDV-3.2.0.dmg.sha256
```

## Auto-Update

### Implementação

```java
// Verificar atualizações ao iniciar
public class UpdateChecker {
    public void checkForUpdates() {
        String currentVersion = "3.2.0";
        String latestVersion = fetchLatestVersion();
        
        if (!currentVersion.equals(latestVersion)) {
            showUpdateNotification(latestVersion);
        }
    }
}
```

### Download e Instalação

```java
// Baixar e executar instalador
public class Updater {
    public void downloadAndInstall(String url) {
        Path installer = downloadFile(url);
        runInstaller(installer);
    }
}
```

## Troubleshooting

### jpackage não encontrado

```bash
# Verificar versão do Java
java -version

# Deve ser JDK 14 ou superior
# jpackage está incluído no JDK, não no JRE
```

### Erro de memória

```bash
# Aumentar memória do Maven
export MAVEN_OPTS="-Xmx2048m"
```

### Problemas de permissão (Linux)

```bash
# Dar permissão ao script
chmod +x build-installer.sh

# Executar com sudo se necessário
sudo ./build-installer.sh
```

### Assinatura de código falhando

```bash
# Verificar certificado
# macOS
security find-identity -v -p codesigning

# Windows
certutil -store MY
```

## CI/CD Integration

### GitHub Actions

```yaml
- name: Build Windows Installer
  if: runner.os == 'Windows'
  run: ./build-installer.bat

- name: Build Linux Installer
  if: runner.os == 'Linux'
  run: ./build-installer.sh

- name: Upload Installers
  uses: actions/upload-artifact@v4
  with:
    name: installers
    path: target/dist/*
```

## Melhores Práticas

1. **Testar instalador** em ambiente limpo antes de distribuir
2. **Assinar código** para evitar avisos de segurança
3. **Criar checksums** para verificar integridade
4. **Documentar requisitos** mínimos (Java, memória, disco)
5. **Fornecer instruções** de desinstalação
6. **Testar auto-update** regularmente
7. **Manter histórico** de versões para rollback
8. **Monitorar instalações** com analytics (opcional)

## Referências

- [jpackage Documentation](https://docs.oracle.com/en/java/javase/21/docs/specs/man/jpackage.html)
- [jpackage Maven Plugin](https://github.com/panteleyev/jpackage-maven-plugin)
- [Java Deployment Guide](https://docs.oracle.com/javase/tutorial/deployment/)
