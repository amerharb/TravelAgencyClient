import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    String ServerAddress;
    int ServerPort;

    public Client(String ServerAddress, int ServerPort){
        this.ServerAddress = ServerAddress;
        this.ServerPort = ServerPort;
    }

    public void test() {
        Socket client = null;
        for (int i = 0; i < 10; i++) {
            try {
                String msg;

                // Create a client socket
                client = new Socket(InetAddress.getByName(ServerAddress), ServerPort);
                System.out.println("Client socket is created " + client);

                // Create an output stream of the client socket
                OutputStream clientOut = client.getOutputStream();
                PrintWriter pw = new PrintWriter(clientOut, true);

                // Create an input stream of the client socket
                InputStream clientIn = client.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(clientIn));

                // Create BufferedReader for a standard input
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Enter your name. Type Bye to exit. ");

                // Read data from standard input device and write it to the output stream of the client socket.
                msg = stdIn.readLine().trim();
                pw.println(msg);

                // Read data from the input stream of the client socket.
                System.out.println("Message returned from the server = " + br.readLine());
                pw.close();
                br.close();
                client.close();

                // Stop the operation
                if (msg.equalsIgnoreCase("Bye"))
                    break;

            } catch (IOException ie) {
                System.out.println("I/O error " + ie);
            }
        }
    }

}
