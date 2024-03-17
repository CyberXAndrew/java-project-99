FROM node:20.6.1 AS frontend

WORKDIR /frontend

COPY frontend/package*.json .

RUN npm ci

COPY frontend /frontend

RUN npm run build

FROM eclipse-temurin:20-jdk

ARG GRADLE_VERSION=8.6

RUN apt-get update && apt-get install -yq make unzip

WORKDIR /backend

COPY gradle ./app/gradle
COPY build.gradle ./app
COPY settings.gradle ./app
COPY gradlew ./app

RUN ./gradlew --no-daemon dependencies

#COPY lombok.config .
COPY app/src app/src

COPY --from=frontend /frontend/dist /backend/src/main/resources/static

RUN ./gradlew --no-daemon build

ENV JAVA_OPTS "-Xmx512M -Xms512M"
EXPOSE 8080

CMD java -jar app/build/libs/HexletSpringBlog-1.0-SNAPSHOT.jar #добавил app
