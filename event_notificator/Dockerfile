FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=target/event_notificator-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} event_notificator.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/event_notificator.jar"]

