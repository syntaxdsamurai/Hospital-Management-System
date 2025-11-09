# Stage 1: Build the .war file using Maven
FROM maven:3.8-openjdk-8-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Stage 2: Create the final server image
FROM tomcat:9.0-jre8-slim

# Remove default Tomcat webapps (removes welcome page)
RUN rm -rf /usr/local/tomcat/webapps/*

# Fix the AJP connector issue (comment out port 8009)
RUN sed -i '/<Connector protocol="AJP/d' /usr/local/tomcat/conf/server.xml

# Copy the ROOT.war file into Tomcat's webapps folder
COPY --from=build /app/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war

# Create data directory for XML files
RUN mkdir -p /usr/local/tomcat/webapps/ROOT/data

# Expose port 8080
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]

