FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY stempol-keystore.jks /etc/stempol/certs/stempol-keystore.jks
ENTRYPOINT ["java","-jar","/app.jar"]
