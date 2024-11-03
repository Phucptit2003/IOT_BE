//package org.example.Service;
//
//import org.example.Model.SensorData;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class WebSocketService {
////    private final SimpMessagingTemplate messagingTemplate;
//
////    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
////        this.messagingTemplate = messagingTemplate;
////    }
//
//    public void sendSensorData(String sessionId, SensorData data) {
//        messagingTemplate.convertAndSend("/topic/sensor/" + sessionId, data); // Gửi dữ liệu tới người dùng
//    }
//}
