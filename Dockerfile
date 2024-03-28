FROM node:20.6.1 AS frontend

WORKDIR /frontend

#COPY frontend/package*.json .
COPY package*.json .

RUN npm ci

#COPY frontend /frontend
COPY ./ .

RUN npm run build

FROM eclipse-temurin:20-jdk

ARG GRADLE_VERSION=8.6

RUN apt-get update && apt-get install -yq make unzip

WORKDIR /backend

COPY app/gradle gradle
COPY app/build.gradle .
COPY app/settings.gradle .
COPY app/gradlew .

RUN app/gradlew --no-daemon dependencies

#COPY lombok.config .
COPY app/src app/src

COPY --from=frontend /frontend/dist /backend/src/main/resources/static

RUN app/gradlew --no-daemon build

ENV JAVA_OPTS "-Xmx512M -Xms512M"
EXPOSE 8080

CMD java -jar app/build/libs/app-0.0.1-SNAPSHOT.jar #добавил app

#FROM eclipse-temurin:20-jdk
#
#ARG GRADLE_VERSION=8.6
#
#RUN apt-get update && apt-get install -yq make unzip
#
#WORKDIR /backend
#
#COPY ./ .
#
#RUN app/gradlew --no-daemon build
#
#EXPOSE 8080
#
#CMD java -jar build/libs/app-0.0.1-SNAPSHOT.jar