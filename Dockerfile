FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
RUN apk update && apk upgrade --no-cache 
#from trivy
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar
EXPOSE 9000

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
