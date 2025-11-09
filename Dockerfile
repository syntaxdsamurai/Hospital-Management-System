# Stage 1: Build the .war file using Maven
FROM maven:3.8-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# This runs the "package" goal to create ROOT.war
RUN mvn clean package

# Stage 2: Create the final server image
FROM tomcat:9.0-jre11-slim

# ---- THIS IS THE FIX ----
# This command finds the AJP connector (port 8009) in server.xml and comments it out.
# This stops the "Invalid message" errors and fixes the 502 error.
RUN sed -i 's/\(<Connector.*port="8009".*\/>\)//' /usr/local/tomcat/conf/server.xml

# Copy the ROOT.war file into Tomcat's webapps folder
COPY --from=build /app/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war