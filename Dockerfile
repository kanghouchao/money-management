FROM gradle:9.1.0-jdk21-ubi-minimal AS builder

WORKDIR /app

COPY . .

RUN ./gradlew build -x test

FROM eclipse-temurin:21-jre-alpine-3.22

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]