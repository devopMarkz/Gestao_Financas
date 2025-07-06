FROM maven:3.9.4-amazoncorretto-17 AS build

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests

FROM amazoncorretto:17-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar gestao-financas-api.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "gestao-financas-api.jar"]