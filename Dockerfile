# Dockerfile para Hermes Comercial PDV com JavaFX
FROM eclipse-temurin:21-jdk-jammy

# Instalar dependências do sistema para JavaFX
RUN apt-get update && apt-get install -y \
    libgtk-3-0 \
    libgconf-2-4 \
    libasound2 \
    libxss1 \
    libxtst6 \
    libxrandr2 \
    libxinerama1 \
    libcairo-gobject2 \
    libatk-bridge2.0-0 \
    libgdk-pixbuf2.0-0 \
    libgtk-3-0 \
    libatspi2.0-0 \
    libdrm2 \
    libxcomposite1 \
    libxcursor1 \
    libxdamage1 \
    libxi6 \
    libxtst6 \
    libgconf-2-4 \
    && rm -rf /var/lib/apt/lists/*

# Criar diretório da aplicação
WORKDIR /app

# Copiar arquivos da aplicação
COPY target/hermesComercial-1.2.0.jar app.jar
COPY target/dependency/ lib/

# Criar script de execução
RUN echo '#!/bin/bash\n\
cd /app\n\
echo "=== Hermes Comercial PDV - Docker ==="\n\
echo "Java: $(java -version 2>&1 | head -n1)"\n\
echo "JAR: app.jar"\n\
echo "Dependências: $(ls lib/ | wc -l) arquivos"\n\
echo ""\n\
# Construir classpath\n\
CLASSPATH="app.jar"\n\
for jar in lib/*.jar; do\n\
    CLASSPATH="$CLASSPATH:$jar"\n\
done\n\
echo "Executando com JavaFX..." \n\
java -Xms512m -Xmx2048m -cp "$CLASSPATH" com.br.hermescomercial.MainApplication\n\
' > /app/run.sh && chmod +x /app/run.sh

# Expor porta (se necessário para algum serviço)
EXPOSE 8080

# Comando padrão
CMD ["/app/run.sh"]
