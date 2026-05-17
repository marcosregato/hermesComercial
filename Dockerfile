FROM eclipse-temurin:21-jre-alpine

# Instalar dependências necessárias para Swing (se necessário)
RUN apk add --no-cache \
    libx11 \
    libxext \
    libxrender \
    libxtst \
    fontconfig \
    ttf-dejavu

# Criar diretório de trabalho
WORKDIR /app

# Copiar JAR da aplicação
COPY target/hermespdv-standalone.jar app.jar

# Criar diretório para banco de dados
RUN mkdir -p /app/data

# Variáveis de ambiente
ENV JAVA_OPTS="-Xmx1024m -Xms512m"
ENV DISPLAY=:0

# Expor porta (se necessário para futuras APIs)
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD pgrep -f "java.*app.jar" || exit 1
