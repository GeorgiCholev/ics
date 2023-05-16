FROM amazoncorretto:17

ARG IMAGGA_API_KEY
ENV IMAGGA_API_KEY=$IMAGGA_API_KEY

ARG IMAGGA_API_SECRET
ENV IMAGGA_API_SECRET=$IMAGGA_API_SECRET

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file built by Gradle into the container
COPY build/libs/ app.jar

EXPOSE 8000

# Set the entry point for the application
ENTRYPOINT ["java", "-jar", "app.jar"]
