
package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class client {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             OutputStream out = socket.getOutputStream()) {

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Enter action (register, login, access):");
            String action = consoleReader.readLine();

            Map<String, String> requestMap = new HashMap<>();
            requestMap.put("action", action);

            if (action.equals("register") || action.equals("login")) {
                System.out.println("Enter username:");
                String username = consoleReader.readLine();
                System.out.println("Enter password:");
                String password = consoleReader.readLine();

                requestMap.put("username", username);
                requestMap.put("password", password);
            } else if (action.equals("access")) {
                System.out.println("Enter token:");
                String token = consoleReader.readLine();
                requestMap.put("token", token);
            } else {
                System.out.println("Invalid action");
                return;
            }

            String request = objectMapper.writeValueAsString(requestMap);
            out.write((request + "\n").getBytes());

            String response = in.readLine();
            System.out.println("Response: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
