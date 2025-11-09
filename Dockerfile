# Stage 1: Build the .war file using Maven
FROM maven:3.8-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# This runs the "package" goal to create ROOT.war
RUN mvn clean package

# Stage 2: Create the final server image
FROM tomcat:9.0-jre11-slim
# Copy the ROOT.war file from the build stage into Tomcat's webapps folder
COPY --from=build /app/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war