# Usamos JRE 17 oficial
FROM eclipse-temurin:17-jre-alpine

# Copiamos el .jar generado por Maven
COPY ./target/WRK2025-CARRITO-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Iniciar aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]