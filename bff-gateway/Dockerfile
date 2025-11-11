FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .
COPY bff-gateway/pom.xml bff-gateway/
COPY core-app/pom.xml core-app/
COPY shared-kernel/pom.xml shared-kernel/
RUN mvn -pl bff-gateway -am dependency:go-offline

COPY . .
RUN mvn -pl bff-gateway -am package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/bff-gateway/target/bff-gateway-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

