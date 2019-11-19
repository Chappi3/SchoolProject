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

The script is predefined for `mysql.connector-java-8.0.12.jar`. Change location and version for your own liking.

Start Wildfly, and once running, open a new prompt, and go to the bin folder.<br>
Write `jboss-cli -c --file=school.cli`

It should say outcome success. Write `jboss-cli -c --command=:reload` to restart the server.

## Deployment
With the Wildfly server running, deploy with:<br> `wildfly:undeploy clean:clean wildfly:deploy`

## Endpoints
localhost:8080/school/student

|Method|Path    |Request Body                     |Response Codes  | 
|------|--------| --------------------------------|----------------|
|GET   |/       |                                 |200 / 400       |
|GET   |/find   |Body: foreName, lastName         |200 / 400       |
|POST  |/add    |Body: foreName, lastName, email  |201 / 400       |
|DELETE|/remove |Body: email                      |204 / 400 / 404 |
|PUT   |/update |Body: foreName, lastName, email  |200 / 400 / 404 |

## Request body examples
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