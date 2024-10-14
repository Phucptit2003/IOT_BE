//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.handshake.ServerHandshake;
//
//import java.net.URI;
//
//public class Esp32WebSocketClient extends WebSocketClient {
//
//    public Esp32WebSocketClient(URI serverURI) {
//        super(serverURI);
//    }
//
//    @Override
//    public void onOpen(ServerHandshake handshakedata) {
//        System.out.println("Connected to ESP32 WebSocket.");
//    }
//
//    @Override
//    public void onMessage(String message) {
//        // Handle incoming messages from ESP32
//        System.out.println("Received from ESP32: " + message);
//        // Process the message as needed
//    }
//
//    @Override
//    public void onClose(int code, String reason, boolean remote) {
//        System.out.println("Disconnected from ESP32 WebSocket.");
//    }
//
//    @Override
//    public void onError(Exception ex) {
//        ex.printStackTrace();
//        System.out.println("WebSocket error: " + ex.getMessage());
//    }
//}
