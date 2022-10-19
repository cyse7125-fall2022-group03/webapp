# webapp

| Name                | NUID      | Email                          |
| ------------------- | --------- | ------------------------------ |
| Ketki Kule          | 001549838 | kule.k@northeastern.edu        |
| Sandeep Wagh        | 001839964 | wagh.sn@northeastern.edu       |
| Vignesh Gunasekaran | 001029530 | gunasekaran.v@northeastern.edu |

## Spring Boot Hibernate Microservice

Steps:
1. Setting up MySQL on your local machine. Check file application.properties file for MySQL connection properties.
2. Create a Database 'todo' in your local MySQL. Code available in schema.sql file
3. Run WebappApplication.java file as Java Application
4. In Postman, hit url: http://localhost:8080/user/create with POST as method and in request body with type as raw - JSON
{
    "firstName" : "Vignesh",
    "middleName" : "G",
    "lastName" : "Gunasekaran",
    "email" : "vig@gmail.com",
    "password" : "password123"
}
5. A user record with auto ID is generated in user table of todo db. Check your MySQL client application like TablePlus.


Rest APIs - so far
## User
1. POST - /user/create - created
2. GET - /user/login - in progress

## List

## List/Task

## Email

## Docker:
Note:
webapp is of JRE JavaSE-17
Docker is also of openjdk 17

docker pull openjdk
docker pull mysql
- creates respective images

Create mysql container:
``docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=password123 -e MYSQL_DATABASE=todo -e MYSQL_USER=root -e MYSQL_PASSWORD=password123 -d mysql:8 tail -f /dev/null``
(tail -f /dev/null  to make it run in foreground so container doesn’t exists immediately)

in application.properties: jdbc.url – should point to above container name and not localhost

To list images:
docker images

To list containers:
docker container ls

Create docker image for webapplication: (inside ur project repo -where Dockerfile exists; ie: webapp/) 
``docker build . -t todo-webapp``

create docker containerization: 
``docker run -d -p 8080:8080 --name webapp-container --link mysql-container:mysql todo-webapp tail -f /dev/null``

our jar name is also todo-webapp


To setup MySQL in your local machine (mac):
1. Install MySQL from official website. version 8.0.31 or any.
2. Find location where it is installed: using below command
find where mysql is installed:  / -iname "mysql" -print -quit
output will be: /usr/local/mysql-8.0.31-macos12-arm64/bin/mysql
3. Add this to PATH variable: (in cmd - ohmyzsh)
a. nano ~/.zshrc
b. enter
export PATH="$PATH:/usr/local/mysql-8.0.31-macos12-arm64/bin/mysql"
c. Press Control + X, followed by Y to save the file, press enter to exit Nano
d. Now type command source ~/.zshrc to apply changes.
