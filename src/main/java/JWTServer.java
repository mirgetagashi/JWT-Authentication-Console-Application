import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTServer {
    private static final KeyPair keyPair = generateRSAKeyPair();

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
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
            return JWT.create()
                    .withIssuer("auth0")
                    .withSubject(username)
                    .withIssuedAt(Date.from(Instant.now()))
                    .withExpiresAt(Date.from(Instant.now().plusSeconds(10000)))
                    .sign(algorithm);
        }

        private boolean validateJWT(String token) {
            try {
                Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("auth0")
                        .build();
                DecodedJWT jwt = ((JWTVerifier) verifier).verify(token);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    private static KeyPair generateRSAKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
