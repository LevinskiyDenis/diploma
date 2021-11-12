FROM openjdk:8-jdk-alpine
EXPOSE 8081
COPY target/FileSharing-1.0-SNAPSHOT.jar filesharingapp.jar
ENTRYPOINT ["java", "-jar", "/filesharingapp.jar"]