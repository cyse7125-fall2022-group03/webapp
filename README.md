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



1. create user (by default = 1 list for that user will be created as well) (only non-auth endpoint)
2. get user details
3. create a new list
4. create a task (task request body should have for tags, comment, remainders = so tag, comment, remainder will also be created along with task, if available in request body) [also: TAG will have check if user already have that tag previously it gets refered to that tag]
{in short: tag is 'user' level;
comments/remainders are 'task' level
tasks are 'list' level}
5. view all lists (of user)
6. view particular list (of user)
7. view all tasks (the main page of todo app - aka v1/user/self) under all lists (of user)
8. view all tasks under a list (of user)
9. view a particular task under a list (of user)


1. create user
http://localhost:8080/v1/create (only non auth end point)
Post request
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

2. get user details
http://localhost:8080/v1/user
GET  request: no
Basic auth: vig7@gmail.com/password123
response:
{
    "firstName": "Vignesh",
    "lastName": "Gunasekaran",
    "accountCreated": "Wed Oct 26 18:51:18 EDT 2022",
    "middleName": "G",
    "id": "ff80808184167dbf0184167e2e1b0000",
    "email": "vig7@gmail.com",
    "accountUpdated": "Wed Oct 26 18:51:18 EDT 2022"
}

7. view all tasks aka home page
http://localhost:8080/v1/user/self
GET  request: no
Basic auth: vig7@gmail.com/password123
response:
{
    "success": "You have no tasks, start creating"
}

3. create a list
http://localhost:8080/v1/user/list/create
POST request Basic auth: vig7@gmail.com/password123
{
    "name" : "list4"
}
response:
{
    "listId": "ff80808184167dbf0184168fc6fb0009",
    "accountCreated": "Wed Oct 26 19:10:32 EDT 2022",
    "name": "list4",
    "userId": "ff80808184167dbf0184167e2e1b0000",
    "accountUpdated": "Wed Oct 26 19:10:32 EDT 2022"
}

5. get all lists
http://localhost:8080/v1/user/lists
GET  request: no
Basic auth: vig7@gmail.com/password123
response:
{
    "List": [
        {
            "listId": "ff80808184167dbf0184167e2e3e0001",
            "accountCreated": "Wed Oct 26 18:51:18 EDT 2022",
            "name": "List1",
            "userId": "ff80808184167dbf0184167e2e1b0000",
            "accountUpdated": "Wed Oct 26 18:51:18 EDT 2022"
        },
        {
            "listId": "ff80808184167dbf0184167e58960002",
            "accountCreated": "Wed Oct 26 18:51:29 EDT 2022",
            "name": "list3",
            "userId": "ff80808184167dbf0184167e2e1b0000",
            "accountUpdated": "Wed Oct 26 18:51:29 EDT 2022"
        },
        {
            "listId": "ff80808184167dbf0184168fc6fb0009",
            "accountCreated": "Wed Oct 26 19:10:32 EDT 2022",
            "name": "list4",
            "userId": "ff80808184167dbf0184167e2e1b0000",
            "accountUpdated": "Wed Oct 26 19:10:32 EDT 2022"
        }
    ]
}

6. get a list - same as 5 but just 1 is displayed
http://localhost:8080/v1/user/list/ff80808184167dbf0184167e58960002

8. get all tasks under a list 
http://localhost:8080/v1/user/task/ff80808183fa3f6f0183fa453e3f0002
{
    "error": "You dont have such a list"
}
http://localhost:8080/v1/user/task/ff80808184167dbf0184167e58960002
{
    "List": [
        {
            "commentList": [
                {
                    "commentCreated": "Wed Oct 26 18:53:12 EDT 2022",
                    "commentId": "ff80808184167dbf0184167fe8c50003",
                    "comment": "have to do",
                    "commentUpdated": "Wed Oct 26 18:53:12 EDT 2022"
                }
            ],
            "listId": "ff80808184167dbf0184167e58960002",
            "summary": "walks are good",
            "tagList": [],
            "accountCreated": "Wed Oct 26 18:53:12 EDT 2022",
            "dueDate": "Fri Oct 23 07:19:45 EDT 2022",
            "name": "walk",
            "remainderList": [
                {
                    "remainderUpdated": "Wed Oct 26 18:53:12 EDT 2022",
                    "remainderId": "ff80808184167dbf0184167fe8c90004",
                    "remainderCreated": "Wed Oct 26 18:53:12 EDT 2022"
                }
            ],
            "userId": "ff80808184167dbf0184167e2e1b0000",
            "taskId": "ff80808184167dbf0184167fe8d30005",
            "accountUpdated": "Wed Oct 26 18:53:12 EDT 2022"
        }
    ]
}
9. get a particular task under a list - same as 7 but just 1 is displayed
http://localhost:8080/v1/user/task/ff80808184167dbf0184167e58960002/ff80808184167dbf0184167fe8d30005
{
    "List": [
        {
            "commentList": [
                {
                    "commentCreated": "Wed Oct 26 18:53:12 EDT 2022",
                    "commentId": "ff80808184167dbf0184167fe8c50003",
                    "comment": "have to do",
                    "commentUpdated": "Wed Oct 26 18:53:12 EDT 2022"
                }
            ],
            "listId": "ff80808184167dbf0184167e58960002",
            "summary": "walks are good",
            "tagList": [],
            "accountCreated": "Wed Oct 26 18:53:12 EDT 2022",
            "dueDate": "Fri Oct 23 07:19:45 EDT 2022",
            "name": "walk",
            "remainderList": [
                {
                    "remainderUpdated": "Wed Oct 26 18:53:12 EDT 2022",
                    "remainderId": "ff80808184167dbf0184167fe8c90004",
                    "remainderCreated": "Wed Oct 26 18:53:12 EDT 2022"
                }
            ],
            "userId": "ff80808184167dbf0184167e2e1b0000",
            "taskId": "ff80808184167dbf0184167fe8d30005",
            "accountUpdated": "Wed Oct 26 18:53:12 EDT 2022"
        }
    ]
}
another request: http://localhost:8080/v1/user/task/ff80808184167dbf0184167e58960002/abc
{
    "error": "You have no tasks or You dont have such a list/task"
}



4. create task
http://localhost:8080/v1/user/task/create
POST request Basic auth: vig7@gmail.com/password123
{
    "summary" : "walks are good",
    "name" : "walk",
    "dueDate" : "Fri Oct 23 07:19:45 EDT 2022",
    "listId" : "ff80808184167dbf0184167e58960002",
    "tagList" : [
        {
            "tagname" : "important"
        }
    ],
    "commentList" : [
        {
            "comment" : "have to do"
        }
    ],
    "remainderList" : [
        {
        }
    ]
}

response:
{
    "commentList": [
        {
            "commentCreated": "Wed Oct 26 18:53:12 EDT 2022",
            "commentId": "ff80808184167dbf0184167fe8c50003",
            "comment": "have to do",
            "commentUpdated": "Wed Oct 26 18:53:12 EDT 2022"
        }
    ],
    "listId": "ff80808184167dbf0184167e58960002",
    "summary": "walks are good",
    "tagList": [
        {
            "tagname": "important",
            "useri": "ff80808184167dbf0184167e2e1b0000",
            "tagCreated": "Wed Oct 26 18:53:12 EDT 2022",
            "tagUpdated": "Wed Oct 26 18:53:12 EDT 2022"
        }
    ],
    "accountCreated": "Wed Oct 26 18:53:12 EDT 2022",
    "dueDate": "Fri Oct 23 07:19:45 EDT 2022",
    "name": "walk",
    "remainderList": [
        {
            "remainderUpdated": "Wed Oct 26 18:53:12 EDT 2022",
            "remainderId": "ff80808184167dbf0184167fe8c90004",
            "remainderCreated": "Wed Oct 26 18:53:12 EDT 2022"
        }
    ],
    "userId": "ff80808184167dbf0184167e2e1b0000",
    "taskId": "ff80808184167dbf0184167fe8d30005",
    "accountUpdated": "Wed Oct 26 18:53:12 EDT 2022"
}

4. create task
http://localhost:8080/v1/user/task/create
Description: 
a. so first create the (above) task for a list. and again create (this) new task with another list, but with same tagname.
b. here we are trying to use same tag - "important", which created for another task. in db will not create a tag again for this user. and tag is being updated with new time. (see created/updated time difference)

POST request: Basic auth: vig7@gmail.com/password123
{
    "summary" : "dances are good",
    "name" : "dance",
    "dueDate" : "Fri Oct 23 07:19:45 EDT 2022",
    "listId" : "ff80808184167dbf0184167e2e3e0001",
    "tagList" : [
        {
            "tagname" : "important"
        }
    ],
    "commentList" : [
        {
            "comment" : "have to do"
        }
    ],
    "remainderList" : [
        {
        }
    ]
}
rresponse: 
{
    "commentList": [
        {
            "commentCreated": "Wed Oct 26 18:55:36 EDT 2022",
            "commentId": "ff80808184167dbf018416821c440006",
            "comment": "have to do",
            "commentUpdated": "Wed Oct 26 18:55:36 EDT 2022"
        }
    ],
    "listId": "ff80808184167dbf0184167e2e3e0001",
    "summary": "dances are good",
    "tagList": [
        {
            "tagname": "important",
            "tagCreated": "Wed Oct 26 18:53:12 EDT 2022",
            "tagUpdated": "Wed Oct 26 18:55:36 EDT 2022"
        }
    ],
    "accountCreated": "Wed Oct 26 18:55:36 EDT 2022",
    "dueDate": "Fri Oct 23 07:19:45 EDT 2022",
    "name": "dance",
    "remainderList": [
        {
            "remainderUpdated": "Wed Oct 26 18:55:36 EDT 2022",
            "remainderId": "ff80808184167dbf018416821c490007",
            "remainderCreated": "Wed Oct 26 18:55:36 EDT 2022"
        }
    ],
    "userId": "ff80808184167dbf0184167e2e1b0000",
    "taskId": "ff80808184167dbf018416821c580008",
    "accountUpdated": "Wed Oct 26 18:55:36 EDT 2022"
}


7. view all tasks aka home page
http://localhost:8080/v1/user/self
GET  request: no
Basic auth: vig7@gmail.com/password123
response:
{
    "List": [
        {
            "commentList": [
                {
                    "commentCreated": "Wed Oct 26 18:55:36 EDT 2022",
                    "commentId": "ff80808184167dbf018416821c440006",
                    "comment": "have to do",
                    "commentUpdated": "Wed Oct 26 18:55:36 EDT 2022"
                }
            ],
            "listId": "ff80808184167dbf0184167e2e3e0001",
            "summary": "dances are good",
            "tagList": [
                {
                    "tagname": "important",
                    "tagCreated": "Wed Oct 26 18:53:12 EDT 2022",
                    "tagUpdated": "Wed Oct 26 18:55:36 EDT 2022"
                }
            ],
            "accountCreated": "Wed Oct 26 18:55:36 EDT 2022",
            "dueDate": "Fri Oct 23 07:19:45 EDT 2022",
            "name": "dance",
            "remainderList": [
                {
                    "remainderUpdated": "Wed Oct 26 18:55:36 EDT 2022",
                    "remainderId": "ff80808184167dbf018416821c490007",
                    "remainderCreated": "Wed Oct 26 18:55:36 EDT 2022"
                }
            ],
            "userId": "ff80808184167dbf0184167e2e1b0000",
            "taskId": "ff80808184167dbf018416821c580008",
            "accountUpdated": "Wed Oct 26 18:55:36 EDT 2022"
        },
        {
            "commentList": [
                {
                    "commentCreated": "Wed Oct 26 18:53:12 EDT 2022",
                    "commentId": "ff80808184167dbf0184167fe8c50003",
                    "comment": "have to do",
                    "commentUpdated": "Wed Oct 26 18:53:12 EDT 2022"
                }
            ],
            "listId": "ff80808184167dbf0184167e58960002",
            "summary": "walks are good",
            "tagList": [],
            "accountCreated": "Wed Oct 26 18:53:12 EDT 2022",
            "dueDate": "Fri Oct 23 07:19:45 EDT 2022",
            "name": "walk",
            "remainderList": [
                {
                    "remainderUpdated": "Wed Oct 26 18:53:12 EDT 2022",
                    "remainderId": "ff80808184167dbf0184167fe8c90004",
                    "remainderCreated": "Wed Oct 26 18:53:12 EDT 2022"
                }
            ],
            "userId": "ff80808184167dbf0184167e2e1b0000",
            "taskId": "ff80808184167dbf0184167fe8d30005",
            "accountUpdated": "Wed Oct 26 18:53:12 EDT 2022"
        },
        {
            "commentList": [
                {
                    "commentCreated": "Wed Oct 26 19:11:01 EDT 2022",
                    "commentId": "ff80808184167dbf018416903aa0000a",
                    "comment": "have to do",
                    "commentUpdated": "Wed Oct 26 19:11:01 EDT 2022"
                }
            ],
            "listId": "ff80808184167dbf0184168fc6fb0009",
            "summary": "dances are good",
            "tagList": [
                {
                    "tagname": "apples",
                    "useri": "ff80808184167dbf0184167e2e1b0000",
                    "tagCreated": "Wed Oct 26 19:11:01 EDT 2022",
                    "tagUpdated": "Wed Oct 26 19:11:01 EDT 2022"
                }
            ],
            "accountCreated": "Wed Oct 26 19:11:01 EDT 2022",
            "dueDate": "Fri Oct 23 07:19:45 EDT 2022",
            "name": "dance",
            "remainderList": [
                {
                    "remainderUpdated": "Wed Oct 26 19:11:01 EDT 2022",
                    "remainderId": "ff80808184167dbf018416903aa5000b",
                    "remainderCreated": "Wed Oct 26 19:11:01 EDT 2022"
                }
            ],
            "userId": "ff80808184167dbf0184167e2e1b0000",
            "taskId": "ff80808184167dbf018416903aac000c",
            "accountUpdated": "Wed Oct 26 19:11:01 EDT 2022"
        }
    ]
}
 



// there are failure responses as well for all above cases


