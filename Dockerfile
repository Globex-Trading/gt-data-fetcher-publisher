FROM openjdk:8-jdk-alpine
RUN addgroup app && adduser -S -G app app
USER app
LABEL org.opencontainers.image.source="https://github.com/Globex-Trading/gt-data-fetcher-publisher"
WORKDIR /app
COPY /target/gt-data-fetcher-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-Xms256m", "-Xmx256m", "-jar", "gt-data-fetcher-0.0.1-SNAPSHOT.jar"]