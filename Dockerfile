FROM openjdk:8-jdk-alpine
#RUN addgroup app && adduser -S -G app app
USER root
LABEL org.opencontainers.image.source="https://github.com/Globex-Trading/gt-data-fetcher-publisher"
WORKDIR /app
COPY /target/gt-data-fetcher-publisher.jar .
ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "gt-data-fetcher-publisher.jar"]