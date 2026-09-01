FROM eclipse-temurin:21-jre

RUN groupadd --system appgroup && \
    useradd --system \
    --gid appgroup \
    appuser

WORKDIR /app

COPY target/order-service-1.0.0.jar app.jar

RUN chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]