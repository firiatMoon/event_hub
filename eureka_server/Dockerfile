FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=target/eureka_server-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} eureka_server.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/eureka_server.jar"]

