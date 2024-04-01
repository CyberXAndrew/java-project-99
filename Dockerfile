FROM eclipse-temurin:20-jdk

ARG GRADLE_VERSION=8.6

RUN apt-get update && apt-get install -yq make unzip

WORKDIR /backend

COPY ./ /backend

RUN ./gradlew --no-daemon build

EXPOSE 8080

CMD java -jar build/libs/app-0.0.1-SNAPSHOT.jar
