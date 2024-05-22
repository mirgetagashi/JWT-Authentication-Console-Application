# JWT Authentication Console Application(console-based client-server model in Java)

## Overview
This console application demonstrates the use of JSON Web Tokens (JWT) for authenticating users. The application allows users to register, log in, and access protected resources using JWTs.

## Features
- User Registration
- User Login
- JWT Generation
- JWT Validation
- Access to Protected Resources

## Prerequisites
- Java 8 or higher
- [Maven](https://maven.apache.org/) or [Gradle](https://gradle.org/)
- [JWT Library](https://github.com/jwtk/jjwt) (included in `pom.xml` or `build.gradle`)



## 2 main components are needed: 
-the server
-the client

The server will handle user registration, login, JWT generation, and validation, while the client will interact with the server to perform these operations.

## Server-Side
1.Dependencies: Use Maven to manage dependencies, including the JWT library (jjwt).
2.Server Implementation: Create classes for user management, JWT handling, and server logic.

## Client-Side


## HOW TO RUN:
1.Compile the Server and Client:
mvn clean install
2.Run the Server:
java -cp target/jwt-auth-server-1.0-SNAPSHOT.jar org.example.Main
3.Run the Client:
java -cp target/jwt-auth-server-1.0-SNAPSHOT.jar org.example.Client


## Usage
1.Register a User:
-Action: 'register'
-Enter username and password when prompted.

2.Login:
-Action: login
-Enter username and password when prompted.
-A JWT token will be provided upon successful login.

3.Access Protected Resource:
-Action: access
-Enter the JWT token when prompted.
-Access will be granted if the token is valid.

## Acknowledgments
https://jwt.io/
https://jwt.io/libraries