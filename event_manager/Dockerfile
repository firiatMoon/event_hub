FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=target/event_manager-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} event_manager.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/event_manager.jar"]


