package org.example.Service;

import ConnectESP.ArduinoWebSocketClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketService {
    private final Map<String, ArduinoWebSocketClient> clientMap = new ConcurrentHashMap<>();

    @Async
    public void sendMessage(String serverUri, String message) {
        try {
            ArduinoWebSocketClient client = clientMap.computeIfAbsent(serverUri, uri -> {
                try {
                    ArduinoWebSocketClient newClient = new ArduinoWebSocketClient(new URI(uri));
                    newClient.connectBlocking();
                    return newClient;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            });

            if (client != null && client.isOpen()) {
                client.send(message);
                System.out.println("Message sent to " + serverUri + ": " + message);
            } else {
                System.err.println("WebSocket not open. Cannot send message to " + serverUri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
