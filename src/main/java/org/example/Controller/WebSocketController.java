package org.example.Controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.example.Model.SensorData;

@Controller
public class WebSocketController {

    // Xử lý các tin nhắn gửi tới từ client qua "/app/sendData"
    @MessageMapping("/sendData")
    @SendTo("/topic/sensorData")
    public SensorData processSensorData(SensorData data) {
        // Xử lý logic (ví dụ như xử lý dữ liệu cảm biến)
        return data;  // Trả về dữ liệu đã xử lý cho tất cả các client subscribe vào "/topic/sensorData"
    }
}
