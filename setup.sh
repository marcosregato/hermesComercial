#!/bin/bash

echo "Iniciando limpeza e configuração do ambiente..."

# 1. Limpar a pasta de migração (mantendo apenas V1 e V3 corretos)
echo "Limpando arquivos de migração antigos..."
cd src/main/resources/db/migration/
rm -f V1__insert_data.sql
rm -f V2__update_comercialHermesDB.sql
rm -f V3__Insert_initial_data.sql
echo "Pasta de migração limpa."
cd ../../../../../

# 2. Executar Flyway Clean e Migrate
echo "Executando limpeza e migração do Flyway..."
mvn flyway:clean flyway:migrate

echo "Processo concluído."
