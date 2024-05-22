import java.io.*;
import java.net.Socket;

public class JWTClient {
    private static String token;

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Enter username: ");
            String username = reader.readLine();
            System.out.println("Enter password: ");
            String password = reader.readLine();

            out.writeUTF(username);
            out.writeUTF(password);
            out.flush();

            String response = in.readUTF();
            if (response.startsWith("Invalid")) {
                System.out.println("Authentication failed: " + response);
                return;
            }

            token = response;
            System.out.println("Logged in. JWT token is: " + token);

            while (true) {
                System.out.println("Enter command ('request_data' or 'logout'):");
                String command = reader.readLine();

                if (command.equals("logout")) {
                    out.writeUTF("logout");
                    out.flush();
                    System.out.println("Logged out");
                    break;
                } else if (command.equals("request_data")) {
                    out.writeUTF("request_data");
                    out.writeUTF(token);
                    out.flush();

                    String protectedData = in.readUTF();
                    System.out.println(protectedData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}