package cliente;
import java.io.IOException;

import middleware.ServiceStub;

public class ClientExample {
    public static void main(String[] args) {
        try {
            ServiceStub stub = new ServiceStub("ExampleService");
            stub.connectToService();
            System.out.println(stub.sendMessage("Hello, Server!"));
            stub.close();
            
            
        	stub = new ServiceStub("ServiceChatbot");
            stub.connectToService();
            System.out.println(stub.sendMessage("Hi"));
            stub.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
