# Stage 1: Build the .war file using Maven
FROM maven:3.8-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# We need to run 'package' to build the .war file
RUN mvn clean package

# Stage 2: Create the final server image
FROM tomcat:9.0-jre11-slim
# Copy the .war file from the build stage into Tomcat's webapps folder
# We named it ROOT.war in the pom.xml, so it runs at the base URL
COPY --from=build /app/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war