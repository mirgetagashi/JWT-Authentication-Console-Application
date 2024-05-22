package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final String SECRET_KEY = "your_secret_key";

    private static Map<String, String> users = new HashMap<>();
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server is running...");
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    String request = in.readLine();
                    String response = handleRequest(request);
                    clientSocket.getOutputStream().write((response + "\n").getBytes());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String handleRequest(String request) {
        try {
            Map<String, String> requestMap = objectMapper.readValue(request, Map.class);
            String action = requestMap.get("action");

            switch (action) {
                case "register":
                    return registerUser(requestMap.get("username"), requestMap.get("password"));
                case "login":
                    return loginUser(requestMap.get("username"), requestMap.get("password"));
                case "access":
                    return accessResource(requestMap.get("token"));
                default:
                    return "Invalid action";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error processing request";
        }
    }

    private static String registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return "User already exists";
        }
        users.put(username, password);
        return "User registered successfully";
    }

    private static String loginUser(String username, String password) {
        if (!users.containsKey(username) || !users.get(username).equals(password)) {
            return "Invalid username or password";
        }

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 600000)) // 10 minutes
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        return "Login successful, token: " + token;
    }

    private static String accessResource(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            return "Access granted for user: " + username;
        } catch (Exception e) {
            return "Invalid token";
        }
    }
}
