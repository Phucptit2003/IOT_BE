package org.example.Repository;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketManager extends WebSocketClient {

    public WebSocketManager(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to ESP32 WebSocket.");
    }

    @Override
    public void onMessage(String message) {
        // Handle incoming messages from ESP32
        System.out.println("Received from ESP32: " + message);
        // You can call a method to process the message here
        processMessageFromESP(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from ESP32 WebSocket.");
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
        ex.printStackTrace(); // Log the error for debugging
    }

    // Placeholder for message processing logic
    private void processMessageFromESP(String message) {
        // Implement your message processing logic here
        System.out.println("Processing message: " + message);
    }
}
