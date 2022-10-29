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

## Rest API end-points:

Rest APIs - so far
Documented in below URL
https://documenter.getpostman.com/view/18309852/2s8YK9KR11

Unique list of apis:
## USER
1. POST
http://localhost:8080/v1/create
2. GET
http://localhost:8080/v1/user

## List
3. POST
http://localhost:8080/v1/user/list/create
4. ET
http://localhost:8080/v1/user/lists
5. GET
http://localhost:8080/v1/user/list/ff808081841c487301841c48c2ec0002
6. PUT
http://localhost:8080/v1/user/list/update
7. DEL
http://localhost:8080/v1/user/list/delete/ff808081841d529701841d633a190005

## Task
8. POST
http://localhost:8080/v1/user/task/create
9. GET
http://localhost:8080/v1/user/task/ff808081841706600184170bd8710006
10. GET
http://localhost:8080/v1/user/task/ff808081841c487301841c48bb330001/ff808081841c487301841c48f99d0006
11. PUT
http://localhost:8080/v1/user/task/update
12. PUT
http://localhost:8080/v1/user/task/update
13. PUT
http://localhost:8080/v1/user/task/change
14. DEL
http://localhost:8080/v1/user/task/delete/ff808081841c487301841c4a319f0012

## Other GET
15. GET
http://localhost:8080/v1/user/self
16. GET
http://localhost:8080/v1/user/self/important

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

➜  webapp git:(A04) docker buildx build --load --platform=linux/amd64 -t todoketki:latest-amd64 .
➜  webapp git:(A04) docker tag todoketki:latest-amd64 csye7125fall2022group03/dockrepo:todoketki
➜  webapp git:(A04) docker images
➜  webapp git:(A04) docker push csye7125fall2022group03/dockrepo:todoketki
➜  webapp git:(A04) docker run -d -p 8080:8080 --name todoketkicontainer --link mysql-container2:mysql csye7125fall2022group03/dockrepo:todoketki
➜  webapp git:(A04) docker push csye7125fall2022group03/dockrepo:todoketki


docker tag mysql:8 csye7125fall2022group03/dockrepo:myfirstmysql9
docker push csye7125fall2022group03/dockrepo:myfirstmysql9
docker run --name mysql-container9 -e MYSQL_ROOT_PASSWORD=password123 -e MYSQL_DATABASE=todo -e -d csye7125fall2022group03/dockrepo:myfirstmysql9



### Docker private pull image

Create a secret regcred with docker private repository credentials

```
kubectl create secret docker-registry regcred --docker-username=username --docker-password=vanShi@2615 --docker-email=kule.k@northeastern.edu --docker-server=https://index.docker.io/v1/
```

### Create deployment 

Run the deployment_migration.yml file with the following command

```
kubectl apply -f deployment_migration.yml
```
### Port forwarding

Replace "service-name" with the name of the service created 

```
kubectl port-forward service/service-name 8080:8080
```

### Deployment status

```
kubectl get all
kubectl logs resource-name > logs.txt
kubectl describe resource-name
kubectl logs -f resource-name  -c container-name
```






