package org.example.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Model.SensorData;
import org.example.Model.StationWebSocketManager;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@RestController
public class WebSocketController {

    private final StationWebSocketManager stationWebSocketManager;
//    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(StationWebSocketManager stationWebSocketManager) {
        this.stationWebSocketManager = stationWebSocketManager;
//        this.messagingTemplate = messagingTemplate;
    }

    // Phương thức để gửi dữ liệu đến station cụ thể
    @MessageMapping("/sendData/{stationId}")
    public void sendDataToStation(String stationId, SensorData data) {
        List<WebSocketSession> sessions = stationWebSocketManager.getSessionsForStation(stationId);
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    // Gửi dữ liệu đến session, bạn có thể sử dụng ObjectMapper để chuyển đổi data thành JSON
                    session.sendMessage(new TextMessage(convertDataToJson(data)));
                } catch (IOException e) {
                    e.printStackTrace(); // Xử lý ngoại lệ nếu cần
                }
            }
        }
    }

    // Phương thức này chuyển đổi SensorData thành JSON (bạn có thể sử dụng thư viện Jackson)
    private String convertDataToJson(SensorData data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Xử lý ngoại lệ nếu cần
            return "{}"; // Trả về JSON rỗng nếu có lỗi
        }
    }
}
