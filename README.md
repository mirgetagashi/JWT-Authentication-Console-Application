# Data Security - Phase 3
JWT Authentication Console Application(console-based client-server model in Java)

Professor [Arbena Musa](https://github.com/ArbenaMusa)

## Language
This project is developed in Java language.

## Description of the algorithm

The JWT Authentication Console Application is a Java-based client-server model tailored for secure authentication processes utilizing JSON Web Tokens (JWT). Employing a console interface, it enables users to interact with the authentication server seamlessly. JWTs, acting as digitally signed tokens, securely transmit user information between the client and server. The algorithm utilizes cryptographic techniques to ensure the confidentiality and integrity of the tokens exchanged. Through a series of protocols and algorithms, including HMAC or RSA for token signing, the application verifies user identities reliably. Its console-based architecture guarantees user-friendly operation and cross-platform compatibility, making it an efficient solution for authentication requirements across diverse environments.

 Key Components of the program:

1. JWTClient:
  - Socket Communication: Establishes a connection with the server using a socket.
  - User Input Handling: Prompts the user for username and password via console input.
  - Data Exchange: Sends the username and password to the server for authentication.
  - Response Handling: Receives and processes the server's response, including the JWT token upon successful authentication.
  - User Interaction Loop: Allows the user to perform actions such as requesting protected data or logging out through console commands.
2. JWTServer:
  - Socket Server: Listens for client connections on a specified port.
  - Client Handling: Accepts client connections and creates a new thread to handle each client separately.
  - Authentication: Validates user credentials against a predefined user database.
  - JWT Generation: Creates a JWT token upon successful authentication, incorporating user     
    information and expiration time.
  - JWT Validation: Verifies the received JWT token's authenticity and integrity using RSA256  
    algorithm.
  - Data Exchange: Responds to client requests for protected data or logout commands.
  - RSA Key Pair Generation: Generates RSA public-private key pair used for JWT signing and   
  verification.

## How to run the program:
1. Start the Server.

2. Start the Client.

3. Username and Password.

4. Select an operation: "Reques data" or  "Log Out".


## Results 

Server:

![Screenshot 2024-05-22 233529](https://github.com/mirgetagashi/JWT-Authentication-Console-Application/assets/154754089/48323b70-b847-428c-bae9-61063950cfc3)


Client:

![Screenshot 2024-05-22 233647](https://github.com/mirgetagashi/JWT-Authentication-Console-Application/assets/154754089/1d91e334-6e85-4e00-8254-a182aa3366ef)



