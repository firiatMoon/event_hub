FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=target/telegram_bot_service-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} telegram_bot_service.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/telegram_bot_service.jar"]

