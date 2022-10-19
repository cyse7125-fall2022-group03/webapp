FROM openjdk:17
COPY target/todo-webapp.jar todo-webapp.jar
ENTRYPOINT ["java", "-jar", "todo-webapp.jar"]

EXPOSE 8080/tcp
