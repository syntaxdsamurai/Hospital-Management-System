# Use a base image that has both Maven and Java
FROM maven:3.8-openjdk-11-slim

# Set a working directory inside the container
WORKDIR /app

# Copy the pom.xml file first
COPY pom.xml .

# Copy your source code
COPY src ./src

# Expose the port we defined in our pom.xml (8080)
EXPOSE 8080

# This is the command to run your server
# It's the same as "Run Maven Goal" on "tomcat7:run"
CMD ["mvn", "tomcat7:run"]