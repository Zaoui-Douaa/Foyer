FROM openjdk:8-jdk-alpine
EXPOSE 8089
ADD target/tp-foyer-5.0.0.jar tp-foyer.jar
ENTRYPOINT ["java", "-jar", "/tp-foyer.jar"]

