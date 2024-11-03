package org.example.Model;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StationWebSocketManager {
    // Lưu trữ các phiên WebSocket theo stationId
    private final Map<String, List<WebSocketSession>> stationSessions = new ConcurrentHashMap<>();

    public void addSession(String stationId, WebSocketSession session) {
        stationSessions.computeIfAbsent(stationId, k -> new ArrayList<>()).add(session);
    }

    public void removeSession(String stationId, WebSocketSession session) {
        List<WebSocketSession> sessions = stationSessions.get(stationId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                stationSessions.remove(stationId); // Xóa station nếu không còn kết nối nào
            }
        }
    }

    public List<WebSocketSession> getSessionsForStation(String stationId) {
        return stationSessions.getOrDefault(stationId, Collections.emptyList());
    }
}
