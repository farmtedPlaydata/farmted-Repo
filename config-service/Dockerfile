FROM eclipse-temurin:17

ARG JAR_FILE=*.jar
ARG JASYPT_ENCRYPTOR_PASSWORD
COPY build/libs/$JAR_FILE app.jar

ENTRYPOINT ["java","-jar", "-Djasypt.encryptor.password=${JASYPT_ENCRYPTOR_PASSWORD}", "/app.jar"]
