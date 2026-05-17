@echo off
REM Script para criar instalador nativo para Windows usando jpackage
REM Hermes Comercial - Sistema PDV

set VERSION=3.2.0
set APP_NAME=Hermes PDV
set VENDOR=Hermes Comercial
set MAIN_CLASS=com.br.hermescomercial.MainApplication

echo 🔨 Criando instalador nativo para %APP_NAME% v%VERSION%

REM Limpar build anterior
echo 🧹 Limpando build anterior...
call mvn clean

REM Criar JAR standalone
echo 📦 Criando JAR standalone...
call mvn package -DskipTests

REM Criar instalador EXE
echo 🪟 Criando instalador para Windows (EXE)...

jpackage ^
    --name "%APP_NAME%" ^
    --app-version "%VERSION%" ^
    --vendor "%VENDOR%" ^
    --main-class "%MAIN_CLASS%" ^
    --main-jar target\hermespdv-standalone.jar ^
    --type exe ^
    --dest target\dist ^
    --input target ^
    --java-options "-Xmx1024m" ^
    --java-options "-Xms512m" ^
    --description "Sistema PDV Hermes Comercial" ^
    --win-menu ^
    --win-dir-chooser ^
    --win-shortcut ^
    --win-menu-group "Hermes Comercial" ^
    --file-association "hermes=Hermes Database File:db"

if %ERRORLEVEL% EQU 0 (
    echo ✅ Build concluído!
    echo 📁 Instalador disponível em: target\dist\
    dir target\dist\
) else (
    echo ❌ Erro ao criar instalador
    echo ⚠️  Certifique-se de que o jpackage está disponível no PATH
    echo ⚠️  jpackage está incluído no JDK 14+
)

pause
