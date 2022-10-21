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
Notes:
webapp is of JRE JavaSE-17 <br/>
Docker is also of openjdk 17 <br/>
No need to create todo database or user table manually. All will be done automatically. <br/>

docker pull openjdk <br/>
docker pull mysql <br/>
- creates respective images

Create mysql container:
```
docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=password123 -e MYSQL_DATABASE=todo -e -d mysql:8
```
(appending "tail -f /dev/null" - to make it run in foreground so container doesn’t exists immediately. But dont append. Both containers are supposed to run in background.)

in application.properties: jdbc.url – should point to above container name and not localhost <br/>
(jdbc.url = jdbc:mysql://mysql-container:3306/todo)

To list images: <br/>
docker images

To list containers: <br/>
docker container ls

Create docker image for webapplication: (inside ur project repo -where Dockerfile exists; ie: webapp/) 
```
docker build . -t todo-webapp
```

create docker containerization: 
```
docker run -d -p 8080:8080 --name webapp-container --link mysql-container:mysql todo-webapp
```

our jar name is also todo-webapp

now, hit http://localhost:8080/user/create, data will be saved into db inside that docker container

Create Docker Tags:
```
docker tag todo-webapp:latest csye7125fall2022group03/dockrepo:myfirsttodowebapp
```
```
docker tag mysql:8 csye7125fall2022group03/dockrepo:myfirstmysql
```

Push images into Docker hub repositories:
```
docker push csye7125fall2022group03/dockrepo:myfirsttodowebapp
```
```
docker push csye7125fall2022group03/dockrepo:myfirstmysql
```


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

## Docker final commands:

1. docker buildx build --platform=linux/arm64 -t todo-webapp6:latest-arm64 .
2. docker tag todo-webapp6:latest-arm64 csye7125fall2022group03/dockrepo:myfirsttodowebapp6
3. docker push csye7125fall2022group03/dockrepo:myfirsttodowebapp6
4. docker run -d -p 8080:8080 --name webapp-container6 --link mysql-container2:mysql csye7125fall2022group03/dockrepo:myfirsttodowebapp6
5. docker push csye7125fall2022group03/dockrepo:myfirsttodowebapp6
(1st one from webapp dir, while others home dir)

## Rest API end-points:

1. http://localhost:8081/v1/create - create a user
Post
{
    "firstName" : "Vignesh",
    "middleName" : "G",
    "lastName" : "Gunasekaran",
    "email" : "vig7@gmail.com",
    "password" : "password123"
}
response:
{
    "firstName": "Vignesh",
    "lastName": "Gunasekaran",
    "accountCreated": "Fri Oct 21 07:13:37 EDT 2022",
    "password": "$2a$10$92VoyxKcVMFAHBLNm.UCRu836AI8S/ADmpkrnABhkqOa.CHMsu3dW",
    "middleName": "G",
    "id": "ff80808183fa3f6f0183fa3fa0f90000",
    "email": "vig7@gmail.com",
    "accountUpdated": "Fri Oct 21 07:13:37 EDT 2022"
}

2. http://localhost:8081/v1/user/self - all tasks - kinda main page
GET
Basic auth: vig7@gmail.com/password123
response:
{
    "List": [
        {
            "listId": "ff80808183fa3f6f0183fa453e3f0002",
            "summary": "apples are good",
            "accountCreated": "Fri Oct 21 09:50:36 EDT 2022",
            "dueDate": "Fri Oct 21 07:19:45 EDT 2022",
            "name": "eat",
            "userId": "ff80808183fa3f6f0183fa3fa0f90000",
            "taskId": "ff80808183fac2b50183facf5a390000",
            "accountUpdated": "Fri Oct 21 09:50:36 EDT 2022"
        },
        {
            "listId": "ff80808183fa3f6f0183fa453e3f0002",
            "summary": "grapes are good",
            "accountCreated": "Fri Oct 21 09:53:59 EDT 2022",
            "dueDate": "Fri Oct 24 07:19:45 EDT 2022",
            "name": "drink",
            "userId": "ff80808183fa3f6f0183fa3fa0f90000",
            "taskId": "ff80808183fac2b50183fad271560001",
            "accountUpdated": "Fri Oct 21 09:53:59 EDT 2022"
        },
        {
            "listId": "ff80808183fac2b50183fae7de120002",
            "summary": "walks are good",
            "accountCreated": "Fri Oct 21 10:20:34 EDT 2022",
            "dueDate": "Fri Oct 23 07:19:45 EDT 2022",
            "name": "walk",
            "userId": "ff80808183fa3f6f0183fa3fa0f90000",
            "taskId": "ff80808183fac2b50183faeaca180003",
            "accountUpdated": "Fri Oct 21 10:20:34 EDT 2022"
        }
    ]
}
//pending is need to include empty lists in this api.

3. http://localhost:8081/v1/user/list/create - create a list
POST
Basic auth: vig7@gmail.com/password123
{
    "name" : "list3"
}
response:
{
    "listId": "ff80808183fac2b50183fae7de120002",
    "accountCreated": "Fri Oct 21 10:17:23 EDT 2022",
    "name": "list3",
    "userId": "ff80808183fa3f6f0183fa3fa0f90000",
    "accountUpdated": "Fri Oct 21 10:17:23 EDT 2022"
}

4. http://localhost:8081/v1/user/list/ff80808183fac2b50183fae7de120002
GET - view a list
Basic auth: vig7@gmail.com/password123
response:
{
    "List": [
        {
            "listId": "ff80808183fac2b50183fae7de120002",
            "accountCreated": "Fri Oct 21 10:17:23 EDT 2022",
            "name": "list3",
            "userId": "ff80808183fa3f6f0183fa3fa0f90000",
            "accountUpdated": "Fri Oct 21 10:17:23 EDT 2022"
        }
    ]
}

5. http://localhost:8081/v1/user/lists
GET- view all lists
Basic auth: vig7@gmail.com/password123
response:
{
    "List": [
        {
            "listId": "ff80808183fa3f6f0183fa3fa1380001",
            "accountCreated": "Fri Oct 21 07:13:37 EDT 2022",
            "name": "List1",
            "userId": "ff80808183fa3f6f0183fa3fa0f90000",
            "accountUpdated": "Fri Oct 21 07:13:37 EDT 2022"
        },
        {
            "listId": "ff80808183fa3f6f0183fa453e3f0002",
            "accountCreated": "Fri Oct 21 07:19:45 EDT 2022",
            "name": "list2",
            "userId": "ff80808183fa3f6f0183fa3fa0f90000",
            "accountUpdated": "Fri Oct 21 07:19:45 EDT 2022"
        },
        {
            "listId": "ff80808183fac2b50183fae7de120002",
            "accountCreated": "Fri Oct 21 10:17:23 EDT 2022",
            "name": "list3",
            "userId": "ff80808183fa3f6f0183fa3fa0f90000",
            "accountUpdated": "Fri Oct 21 10:17:23 EDT 2022"
        }
    ]
}

6. http://localhost:8081/v1/user/task/create
POST - create a task
Basic auth: vig7@gmail.com/password123
{
    "summary" : "walks are good",
    "name" : "walk",
    "dueDate" : "Fri Oct 23 07:19:45 EDT 2022",
    "listId" : "ff80808183fac2b50183fae7de120002"
}
response:
{
    "listId": "ff80808183fac2b50183fae7de120002",
    "summary": "walks are good",
    "accountCreated": "Fri Oct 21 10:20:34 EDT 2022",
    "dueDate": "Fri Oct 23 07:19:45 EDT 2022",
    "name": "walk",
    "userId": "ff80808183fa3f6f0183fa3fa0f90000",
    "taskId": "ff80808183fac2b50183faeaca180003",
    "accountUpdated": "Fri Oct 21 10:20:34 EDT 2022"
}

7. http://localhost:8081/v1/user/task/ff80808183fa3f6f0183fa453e3f0002
GET - list of tasks in a list
Basic auth: vig7@gmail.com/password123
response:
{
    "List": [
        {
            "listId": "ff80808183fa3f6f0183fa453e3f0002",
            "summary": "apples are good",
            "accountCreated": "Fri Oct 21 09:50:36 EDT 2022",
            "dueDate": "Fri Oct 21 07:19:45 EDT 2022",
            "name": "eat",
            "userId": "ff80808183fa3f6f0183fa3fa0f90000",
            "taskId": "ff80808183fac2b50183facf5a390000",
            "accountUpdated": "Fri Oct 21 09:50:36 EDT 2022"
        },
        {
            "listId": "ff80808183fa3f6f0183fa453e3f0002",
            "summary": "grapes are good",
            "accountCreated": "Fri Oct 21 09:53:59 EDT 2022",
            "dueDate": "Fri Oct 24 07:19:45 EDT 2022",
            "name": "drink",
            "userId": "ff80808183fa3f6f0183fa3fa0f90000",
            "taskId": "ff80808183fac2b50183fad271560001",
            "accountUpdated": "Fri Oct 21 09:53:59 EDT 2022"
        }
    ]
}

8. http://localhost:8081/v1/user/task/ff80808183fa3f6f0183fa453e3f0002/ff80808183fac2b50183facf5a390000
GET - get a task in a list
Basic auth: vig7@gmail.com/password123
response:
{
    "List": [
        {
            "listId": "ff80808183fa3f6f0183fa453e3f0002",
            "summary": "apples are good",
            "accountCreated": "Fri Oct 21 09:50:36 EDT 2022",
            "dueDate": "Fri Oct 21 07:19:45 EDT 2022",
            "name": "eat",
            "userId": "ff80808183fa3f6f0183fa3fa0f90000",
            "taskId": "ff80808183fac2b50183facf5a390000",
            "accountUpdated": "Fri Oct 21 09:50:36 EDT 2022"
        }
    ]
}

// there are failure responses as well for all above cases


