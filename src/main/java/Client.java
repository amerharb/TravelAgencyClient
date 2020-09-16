import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class Client {
    String ServerAddress;
    int ServerPort;

    public Client(String ServerAddress, int ServerPort) {
        this.ServerAddress = ServerAddress;
        this.ServerPort = ServerPort;
    }

    public void connect() {
        Socket client = null;
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
            System.out.println("You are now connected to vacation planner Server");
            System.out.println("Address:" + ServerAddress + ":" + ServerPort);

            for (int i = 0; i < 7; i++) {
                // Read Welcome message.
                while (!br.ready()) {
                    sleep(100);
                }
                while (br.ready()) {
                    System.out.println(br.readLine());
                }

                msg = stdIn.readLine().trim();

                pw.println(msg);
                pw.flush();

                // Stop the operation
                if (msg.equalsIgnoreCase("Bye"))
                    break;
            }

            // Read data from the input stream of the client socket.
            while (!br.ready()) { // TODO: or timeout
                sleep(50);
            }
            while (br.ready()) {
                int bytesRead = 0;
                String l = br.readLine();
                if (l.equals("FILE BEGIN ...")) {
                    //download file
                    String filename = br.readLine();
                    int fileSize = Integer.parseInt(br.readLine());
                    byte[] bytes = new byte[fileSize];
                    System.out.println("downloading file: " + filename + " size (" + fileSize + ")");
                    FileOutputStream fos = new FileOutputStream(filename);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int readCount;
                    while ((readCount = clientIn.read(bytes, 0, bytes.length)) != -1) {
                        bos.write(bytes, 0, readCount);
                    }

                    bytesRead = clientIn.read(bytes, 0, bytes.length);
                    bos.write(bytes, 0 , bytesRead);
                    bos.flush();
                    fos.close();
                    bos.close();
                } else {
                    System.out.println(l);
                }
            }
            pw.close();
            br.close();
            client.close();
        } catch (InterruptedException ie) {
            System.out.println("InterruptedException error " + ie);
        } catch (IOException ie) {
            System.out.println("I/O error " + ie);
        }
    }

    private boolean invalidNumber(String s) {
        if (s.equalsIgnoreCase("bye") || isInteger(s))
            return false;

        System.out.println("INVALID NUMBER");
        return true;
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
