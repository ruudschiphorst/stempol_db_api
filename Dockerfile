FROM maven:3.5.2-jdk-8-alpine AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn clean install

FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/*.jar app.jar
COPY stempol-keystore.jks /etc/stempol/certs/stempol-keystore.jks
ENTRYPOINT ["java","-jar","/app.jar"]
