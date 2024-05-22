import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTServer {
    private static final String SECRET_KEY = "secret";
    private static final Map<String, String> userDatabase = new HashMap<>();

    static {
        userDatabase.put("data_security", "password");
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is listening on port 12345");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

                String username = in.readUTF();
                String password = in.readUTF();
                System.out.println("Received credentials - Username: " + username);

                if (authenticate(username, password)) {
                    String token = createJWT(username);
                    out.writeUTF(token);
                    out.flush();
                    System.out.println("JWT issued to " + username);
                } else {
                    out.writeUTF("Invalid credentials");
                    out.flush();
                }

                while (true) {
                    String command = in.readUTF();
                    if (command.equals("logout")) {
                        System.out.println(username + " logged out");
                        break;
                    } else if (command.equals("request_data")) {
                        String token = in.readUTF();
                        if (validateJWT(token)) {
                            out.writeUTF("Protected data: This is protected data.");
                            out.flush();
                        } else {
                            out.writeUTF("401 Unauthorized");
                            out.flush();
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private boolean authenticate(String username, String password) {
            return userDatabase.containsKey(username) && userDatabase.get(username).equals(password);
        }

        private String createJWT(String username) {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.create()
                    .withIssuer("auth0")
                    .withSubject(username)
                    .withIssuedAt(Date.from(Instant.now()))
                    .withExpiresAt(Date.from(Instant.now().plusSeconds(60)))
                    .sign(algorithm);
        }

//        private static String createJWT(String username) {
//            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//
//            long nowMillis = System.currentTimeMillis();
//            Date now = new Date(nowMillis);
//
//            byte[] apiKeySecretBytes = Base64.getEncoder().encode(SECRET_KEY.getBytes());
//            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
//
//            JwtBuilder builder = Jwts.builder()
//                    .setSubject(username)
//                    .setIssuedAt(now)
//                    .signWith(signingKey, signatureAlgorithm);
//
//            return builder.compact();
//        }

        private boolean validateJWT(String token) {
            try {
                Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("auth0")
                        .build();
                DecodedJWT jwt = verifier.verify(token);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
