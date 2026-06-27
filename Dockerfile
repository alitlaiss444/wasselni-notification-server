FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY . .

RUN mvn install:install-file \
    -Dfile=/app/libraries/external-jars/text-table-formatter-1.1.2.jar \
    -DgroupId=com.emcrey \
    -DartifactId=text-table-formatter \
    -Dversion=1.1.2 \
    -Dpackaging=jar

RUN mvn -f libraries/wasselni-parent/pom.xml clean install -DskipTests
RUN mvn -f libraries/wasselni-parent-compile/pom.xml clean install -DskipTests
RUN mvn -f notification-server/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/notification-server/target/*.jar notification-server.jar
COPY --from=build /app/configuration /app/configuration

EXPOSE 8080

CMD sh -c 'echo "Starting notification-server..." && \
exec java -jar notification-server.jar --spring.config.location=file:/app/configuration/processes/notification-server/application.properties 2>&1 | sed "s/^/[notification-server] /"'