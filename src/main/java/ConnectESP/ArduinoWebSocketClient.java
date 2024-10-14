package ConnectESP;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;



import java.net.URI;

public class ArduinoWebSocketClient extends WebSocketClient {

    // Define an interface for message listener
    public interface MessageListener {
        void onMessageReceived(String message);
    }

    private MessageListener messageListener;

    public ArduinoWebSocketClient(URI serverURI) {
        super(serverURI);
    }

    // Method to set the message listener
    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to ESP32 WebSocket.");
    }

    @Override
    public void onMessage(String message) {
        // Notify the listener about the received message
        if (messageListener != null) {
            messageListener.onMessageReceived(message);
        }
        System.out.println("Received from ESP32: " + message);

        try {
            // Assuming the message is in the form of a JSON-like string
            // Example: temperature":30.00,"humidity":41.00,"fanSpeed":0,"ledState":"off","buzzerState":"off"
            // Remove spaces and split the message into key-value pairs
            String[] pairs = message.replace("\"", "").split(",");

            double temperature = 0.0;
            double humidity = 0.0;
            int fanSpeed = 0;
            String ledState = "";
            String buzzerState = "";

            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                String key = keyValue[0].trim();
                String value = keyValue.length > 1 ? keyValue[1].trim() : "";

                switch (key) {
                    case "temperature":
                        temperature = Double.parseDouble(value);
                        break;
                    case "humidity":
                        humidity = Double.parseDouble(value);
                        break;
                    case "fanSpeed":
                        fanSpeed = Integer.parseInt(value);
                        break;
                    case "ledState":
                        ledState = value;
                        break;
                    case "buzzerState":
                        buzzerState = value;
                        break;
                    default:
                        break;
                }
            }

            // Assuming you have a method to save this data to your database
            saveSensorData(temperature, humidity, fanSpeed, ledState, buzzerState);

            // Optionally, send this data to the frontend if necessary
            sendToFrontend(temperature, humidity, fanSpeed, ledState, buzzerState);

        } catch (Exception e) {
            System.err.println("Failed to parse message: " + message);
            e.printStackTrace();
        }
    }


    // Example method to save sensor data to the database
    private void saveSensorData(double temperature, double humidity, int fanSpeed, String ledState, String buzzerState) {
        // Implement your database saving logic here
        // e.g., insert into your database using JDBC or any ORM framework
    }

    // Example method to send data to the frontend
    private void sendToFrontend(double temperature, double humidity, int fanSpeed, String ledState, String buzzerState) {
        // Implement your logic to send this data to the frontend
        // This might involve using WebSocket, HTTP, or any other method to communicate with the frontend
    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from ESP32 WebSocket.");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        System.out.println("WebSocket error: " + ex.getMessage());
    }
    public void sendMessage(String message) {
        if (this.isOpen()) {
            send(message); // Sử dụng phương thức 'send()' của WebSocketClient để gửi tin nhắn
            System.out.println("Sent message to ESP32: " + message);
        } else {
            System.out.println("WebSocket connection is not open.");
        }
    }
}
