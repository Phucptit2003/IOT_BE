package org.example.Model;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    // Map để lưu các WebSocket session của mỗi trạm theo stationId
    private final Map<Integer, Set<WebSocketSession>> stationSessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Integer stationId = (Integer) session.getAttributes().get("stationId");

        if (stationId != null) {
            stationSessions.computeIfAbsent(stationId, k -> new HashSet<>()).add(session);
        } else {
            session.close();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Integer stationId = (Integer) session.getAttributes().get("stationId");
        if (stationId != null) {
            Set<WebSocketSession> sessions = stationSessions.get(stationId);
            if (sessions != null) {
                sessions.remove(session);
                if (sessions.isEmpty()) {
                    stationSessions.remove(stationId);
                }
            }
        }
    }

    // Phương thức để gửi dữ liệu từ ESP đến đúng trạm dựa trên stationId
    public void sendToStation(int stationId, String message) {
        Set<WebSocketSession> sessions = stationSessions.get(stationId);
        if (sessions != null) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(new TextMessage(message));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
