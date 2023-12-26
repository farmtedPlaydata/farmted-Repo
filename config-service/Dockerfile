FROM eclipse-temurin:17

ARG JAR_FILE=*.jar
ARG JASYPT_ENCRYPTOR_PASSWORD
COPY build/libs/config-service-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar", "-Djasypt.encryptor.password=${JASYPT_ENCRYPTOR_PASSWORD}", "/app.jar"]
