package serviconomes;
import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class NameService {
    private ConcurrentHashMap<String, ServiceInfo> services;
    private ServerSocket serverSocket;

    public NameService(int port) throws IOException {
        services = new ConcurrentHashMap<>();
        serverSocket = new ServerSocket(port);
        System.out.println("Name Service running on port " + port);
    }

    public void start() throws IOException {
        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(new ServiceHandler(clientSocket)).start();
        }
    }

    private class ServiceHandler implements Runnable {
        private Socket clientSocket;

        public ServiceHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                 ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream())) {

                String operation = input.readUTF();
                if ("register".equals(operation)) {
                    String name = input.readUTF();
                    String url = input.readUTF();
                    int port = input.readInt();
                    services.put(name, new ServiceInfo(url, port));
                    output.writeUTF("Service registered successfully");
                } else if ("lookup".equals(operation)) {
                    String name = input.readUTF();
                    ServiceInfo info = services.get(name);
                    if (info != null) {
                        output.writeUTF(info.url);
                        output.writeInt(info.port);
                    } else {
                        output.writeUTF(null);
                    }
                }
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        NameService nameService = new NameService(5050);
        nameService.start();
    }
}

class ServiceInfo {
    String url;
    int port;

    ServiceInfo(String url, int port) {
        this.url = url;
        this.port = port;
    }
}
