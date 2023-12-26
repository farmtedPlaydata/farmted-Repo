FROM eclipse-temurin:17

ARG JAR_FILE=*.jar
COPY build/libs/$JAR_FILE app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]