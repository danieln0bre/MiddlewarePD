import java.io.IOException;

public class ClientExample {
    public static void main(String[] args) {
        try {
            ServiceStub stub = new ServiceStub("ExampleService");
            stub.connectToService();
            System.out.println(stub.sendMessage("Hello, Server!"));
            stub.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
