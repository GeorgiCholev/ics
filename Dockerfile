FROM amazoncorretto:17

ARG IMAGGA_API_KEY
ENV IMAGGA_API_KEY=$IMAGGA_API_KEY

ARG IMAGGA_API_SECRET
ENV IMAGGA_API_SECRET=$IMAGGA_API_SECRET

ARG DB_URL
ENV DB_URL=$DB_URL

# Set the working directory in the container
WORKDIR /app

ARG JAR_FILE=build/libs/ics-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8000

# Set the entry point for the application
ENTRYPOINT ["java", "-jar", "app.jar"]
