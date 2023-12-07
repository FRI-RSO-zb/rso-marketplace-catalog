# Image builder
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
ADD . /app
RUN --mount=type=cache,target=/root/.m2 mvn -f /app/pom.xml clean package


# Clean image runtime
FROM eclipse-temurin:17-jre
RUN mkdir /app
WORKDIR /app
COPY --from=build /app/api/target/marketplace-catalog-api.jar /app
EXPOSE 8801
CMD ["java", "--add-opens", "java.base/java.lang=ALL-UNNAMED","-jar", "marketplace-catalog-api.jar"]
