FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY mvnw .
COPY .mvn .mvn

RUN chmod 777 mvnw
RUN ./mvnw package -DskipTests

CMD ["java", "-jar" ,"target/SiteLinkinPark-0.0.1-SNAPSHOT.jar"]