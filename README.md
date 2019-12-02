# Schoolproject

Small project for teaching purposes.

* Wildfly
* JEE 8 
* Java 11
* Git
* Maven
* MySQL

## Wildfly configuration

Install any Wildfly release you want. I use 18.

Add a user, and place the school.cli script under the bin folder.<br>
Create database school. The script will need a mysql connector under `C:\Users`
to work. 

The script is predefined for `mysql.connector-java-8.0.17.jar`. Change location and version for your own liking.

Start Wildfly, and once running, open a new prompt, and go to the bin folder.<br>
Write `jboss-cli -c --file=school.cli`

It should say outcome success. Write `jboss-cli -c --command=:reload` to restart the server.

## Deployment
With the Wildfly server running, deploy with:<br> `wildfly:undeploy clean:clean wildfly:deploy`

## Student Endpoints
localhost:8080/school/student

|Method|Path    |Request Body                     |Response Codes  | 
|------|--------| --------------------------------|----------------|
|GET   |/       |                                 |200             |
|GET   |/find   |Body: foreName, lastName         |200 / 400       |
|POST  |/add    |Body: foreName, lastName, email  |201 / 409 / 400 |
|DELETE|/remove |Body: email                      |204 / 400 / 404 |
|PUT   |/update |Body: foreName, lastName, email  |204 / 400 / 404 |

## Student Request body examples
GET /find example:<br>
<sub>Only one is required.</sub>
```
{
    "foreName" : "John",
    "lastName" : "Doe"
}
```

POST /add example:<br>
<sub>All is required.</sub>
```
{
    "foreName" : "John",
    "lastName" : "Doe",
    "email" : "example@email.com"
}
```
PUT /update example:<br>
<sub>Email and either one is required.</sub>
```
{
    "foreName" : "John",
    "lastName" : "Doe",
    "email" : "example@email.com"
}
```

DELETE /remove example:<br>
<sub>Email is required.</sub>
```
{
    "email" : "example@email.com"
}
```

## Teacher Endpoints
localhost:8080/school/teacher

|Method|Path    |Request Body                     |Response Codes  | 
|------|--------| --------------------------------|----------------|
|GET   |/       |                                 |200             |
|GET   |/find   |Body: foreName, lastName         |200 / 400       |
|POST  |/add    |Body: foreName, lastName, email  |201 / 409 / 400 |
|DELETE|/remove |Body: email                      |204 / 400 / 404 |
|PUT   |/update |Body: foreName, lastName, email  |204 / 400 / 404 |

## Teacher Request body examples
GET /find example:<br>
<sub>Only one is required.</sub>
```
{
    "foreName" : "John",
    "lastName" : "Doe"
}
```

POST /add example:<br>
<sub>All is required.</sub>
```
{
    "foreName" : "John",
    "lastName" : "Doe",
    "email" : "example@email.com"
}
```
PUT /update example:<br>
<sub>Email and either one is required.</sub>
```
{
    "foreName" : "John",
    "lastName" : "Doe",
    "email" : "example@email.com"
}
```

DELETE /remove example:<br>
<sub>Email is required.</sub>
```
{
    "email" : "example@email.com"
}
```

## Subject Endpoints
localhost:8080/school/subjects

|Method |Path            |Request Body         |Response Codes        | 
|-------|----------------|---------------------|----------------------|
|GET    |/               |                     |200                   |
|POST   |/               |Body: subject        |201 / 409 / 400       |
|DELETE |/               |Body: subject        |200 / 400 / 404       |
|PATCH  |/add/student    |Body: subject, email |200 / 400 / 404 / 409 |
|PATCH  |/add/teacher    |Body: subject, email |200 / 400 / 404 / 409 |
|DELETE |/remove/student |Body: subject, email |200 / 400 / 404       |
|DELETE |/remove/teacher |Body: subject, email |200 / 400 / 404       |

## Subject Request body examples
POST/DELETE / example:<br>
<sub>All is required.</sub>
```
{
	"subject" : "Java EE"
}
```

PATCH /add/student AND /add/teacher example:<br>
<sub>All is required.</sub>
```
{
	"subject" : "Java EE",
	"email" : "example@email.com"
}
```

DELETE /remove/student AND /remove/teacher example:<br>
<sub>All is required.</sub>
```
{
	"subject" : "Java EE",
	"email" : "example@email.com"
}
```