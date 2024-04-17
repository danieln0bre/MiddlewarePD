import java.io.*;
import java.net.*;

public class ServiceStub {
    private String serviceName;
    private Socket serviceSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ServiceStub(String serviceName) {
        this.serviceName = serviceName;
    }

    public void connectToService() throws IOException {
        Socket nameServiceSocket = new Socket("localhost", 5050);
        ObjectOutputStream nameOut = new ObjectOutputStream(nameServiceSocket.getOutputStream());
        ObjectInputStream nameIn = new ObjectInputStream(nameServiceSocket.getInputStream());

        nameOut.writeUTF("lookup");
        nameOut.writeUTF(serviceName);
        nameOut.flush();

        String url = nameIn.readUTF();
        if (url != null) {
            int port = nameIn.readInt();
            serviceSocket = new Socket(url, port);
            out = new ObjectOutputStream(serviceSocket.getOutputStream());
            in = new ObjectInputStream(serviceSocket.getInputStream());
        }

        nameOut.close();
        nameIn.close();
        nameServiceSocket.close();
    }

    public String sendMessage(String message) throws IOException {
        out.writeUTF(message);
        out.flush();
        return in.readUTF();
    }

    public void close() throws IOException {
        serviceSocket.close();
    }
}
