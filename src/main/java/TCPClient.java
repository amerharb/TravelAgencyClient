
public class TCPClient {
    static String ServerAddress = "localhost";
    static int ServerPort = 1234;

    public static void main(String[] args) {
        Client client = new Client(ServerAddress, ServerPort);
        client.connect();
    }
}
