FROM maven:3.9-eclipse-temurin-21@sha256:99e61abcff91a9b1333463bd8451fb18495d6eba9250ac66a338b518f8278320 AS build
WORKDIR /build
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -B dependency:go-offline
COPY src src
RUN --mount=type=cache,target=/root/.m2 mvn -B package -DskipTests

FROM eclipse-temurin:21-jre@sha256:0b99b3376fbb50dbf9c0af8535fdd5b8eb0d3fdfc8b1e4967f383b6919f6901a
COPY --from=build /build/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
