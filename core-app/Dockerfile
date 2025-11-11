FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .
COPY core-app/pom.xml core-app/
COPY bff-gateway/pom.xml bff-gateway/
COPY shared-kernel/pom.xml shared-kernel/
RUN mvn -pl core-app -am dependency:go-offline

COPY . .
RUN mvn -pl core-app -am package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/core-app/target/core-app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

