FROM gradle:9.1.0-jdk21-ubi-minimal AS builder

WORKDIR /app

COPY . .

RUN --mount=type=cache,id=gradle-cache,target=/home/gradle/.gradle,uid=1000,gid=1000,sharing=locked \
  ./gradlew build -x test

FROM eclipse-temurin:21-jre-alpine-3.22

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]