FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=target/api_gateway-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} api_gateway.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/api_gateway.jar"]

