package org.example.CheckSensor;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class SensorChecker {

    private static final String ESP32_URL = "ws://192.168.1.8:81"; // Thay <ESP32_IP_ADDRESS> bằng địa chỉ IP của ESP32

    public static void main(String[] args) {
        try {
            // Create a WebSocket client and connect to the ESP32
            WebSocketClient client = new WebSocketClient(new URI(ESP32_URL)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("Connected to ESP32!");

                    // Send the check command
                    String checkCommand = "{\"checkDevices\": true}"; // Command to check all sensors
                    send(checkCommand);
                }

                @Override
                public void onMessage(String message) {
                    System.out.println("Response from ESP32: " + message);
                    parseResponse(message); // Parse the response
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Disconnected from ESP32: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };

            // Connect to the ESP32 WebSocket server
            client.connect();

            // Wait for the connection to establish
            while (!client.isOpen()) {
                Thread.sleep(100); // Sleep to prevent busy waiting
            }

            // Optionally wait for a while to receive messages
            Thread.sleep(5000); // Adjust as necessary to wait for response

            // Close the connection
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void parseResponse(String response) {
        // Kiểm tra nếu có lỗi trong phản hồi JSON
        if (response.contains("\"error\":")) {
            String errorMessage = response.substring(response.indexOf(":") + 1).trim();
            System.out.println("Error: " + errorMessage);
        } else {
            System.out.println("Response: " + response);
        }
    }
}
