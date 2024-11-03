package org.example.Controller;

import ConnectESP.ArduinoWebSocketClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.example.Model.*;
import org.example.Repository.SensorRepository;
import org.example.Repository.StationRepository;
import org.example.Service.SensorService;
import org.example.Service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sensors")
@CrossOrigin
public class SensorController {

    @Autowired
    private SensorService sensorService;
    @Autowired
    private StationService stationService;
    @Autowired
    private StationRepository stationRepository;
    private final SensorRepository sensorRepository;

    private String ESP32_URL ;
    public SensorController(SensorRepository sensorRepository, StationRepository stationRepository) {
        this.sensorRepository = sensorRepository;
        this.stationRepository = stationRepository;
    }
    // Lấy tất cả cảm biến
    @GetMapping
    public List<Sensor> getAllSensors() {
        return sensorService.getAllSensors();
    }

    // Lấy cảm biến theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Sensor> getSensorById(@PathVariable Long id) {
        Sensor sensor = sensorService.getSensorById(id);
        return (sensor != null) ? ResponseEntity.ok(sensor) : ResponseEntity.notFound().build();
    }

    // Thêm cảm biến
    @PostMapping
    public Sensor addSensor(@RequestBody Sensor sensor) {
        return sensorService.addSensor(sensor);
    }

    // Cập nhật cảm biến
    @PutMapping("/{id}")
    public ResponseEntity<Sensor> updateSensor(@PathVariable Long id, @RequestBody Sensor sensor) {
        Sensor updatedSensor = sensorService.updateSensor(id, sensor);
        return (updatedSensor != null) ? ResponseEntity.ok(updatedSensor) : ResponseEntity.notFound().build();
    }

    // Xóa cảm biến
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable Long id) {
        sensorService.deleteSensor(id);
        return ResponseEntity.noContent().build();
    }

//    @Scheduled(fixedRate = 5000) // Gửi dữ liệu mỗi 5 giây
//    public void sendSensorData() {
//        // Lấy tất cả cảm biến từ cơ sở dữ liệu
//        List<Sensor> sensors = sensorService.getAllSensors(); // Hoặc findByStationId nếu bạn muốn lọc theo stationId
//
//        for (Sensor sensor : sensors) {
//            // Gửi dữ liệu của cảm biến đến topic theo stationId
//            System.out.println("sensor: "+ sensor);
//            messagingTemplate.convertAndSend("/topic/sensors/" + sensor.getStation().getId(), sensor);
//        }
//    }

    // Phương thức gửi dữ liệu theo stationId khi nhận được từ FE
    @GetMapping("/sensors/{stationId}")
    public List<Sensor> getSensorsByStationId(@PathVariable int stationId) {
        Station station = stationRepository.findById(stationId);
        if (station != null) {
            return sensorRepository.findByStationId(Long.parseLong(String.valueOf(station.getId())));
        }
        return new ArrayList<>();  // Hoặc có thể trả về mã lỗi nếu station không tồn tại
    }

    // API để cập nhật Sensor theo Station ID
    @PutMapping("/update/{stationId}")
    public ResponseEntity<Map<String, Object>> updateSensor(@PathVariable int stationId, @RequestBody SensorData request) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Tìm Station theo stationId
            Station station = stationRepository.findById(stationId);
            ESP32_URL = "http://" + station.getUri() + ":" + 80 + "/update";
            System.out.println("ESP32_URL: " + ESP32_URL);

            // Tìm sensor theo stationId
            List<Sensor> sensors = sensorRepository.findByStationId(Long.parseLong(String.valueOf(stationId)));

            if (sensors == null || sensors.isEmpty()) {
                response.put("status", "error");
                response.put("message", "Sensor for the given stationId not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else {
                Sensor sensor = sensors.get(0);
                // Cập nhật các trường dữ liệu của sensor với dữ liệu mới từ FE
                sensor.setFanSpeed(request.getFanSpeed());
                sensor.setLedState(request.getLedState());
                sensor.setBuzzerState(request.getBuzzerState());
                sensor.setTargetTemperature(request.getTargetTemperature());
                sensor.setTargetHumidity(request.getTargetHumidity());
                sensor.setMediumTemperature(request.getMediumTemperature());
                sensor.setMediumHumidity(request.getMediumHumidity());

                sensorRepository.save(sensor);

                // Tạo JSON chỉ với các giá trị cần thiết
                JsonObject jsonToSend = new JsonObject();
                jsonToSend.addProperty("fanSpeed", sensor.getFanSpeed());
                jsonToSend.addProperty("ledState", sensor.getLedState());
                jsonToSend.addProperty("buzzerState", sensor.getBuzzerState());
                jsonToSend.addProperty("targetTemperature", sensor.getTargetTemperature());
                jsonToSend.addProperty("targetHumidity", sensor.getTargetHumidity());
                jsonToSend.addProperty("mediumTemperature", sensor.getMediumTemperature());
                jsonToSend.addProperty("mediumHumidity", sensor.getMediumHumidity());
                String sensorDataJson = jsonToSend.toString();
                String serverUri = "ws://" + station.getUri() + ":" + station.getPort();
                ArduinoWebSocketClient client = new ArduinoWebSocketClient(new URI(serverUri));

                // Kết nối đến WebSocket
                client.connectBlocking(); // Đảm bảo kết nối được thiết lập

                // Gửi thông điệp tới ESP32 chỉ khi đã kết nối thành công
                if (client.isOpen()) {
                    client.send(sensorDataJson);
                    System.out.println("Message sent to ESP32 to send: " + sensorDataJson);
                } else {
                    System.out.println("WebSocket not open. Cannot send message.");
                    response.put("status", "error");
                    response.put("message", "WebSocket not open. Cannot send message.");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                }

                // Thành công
                response.put("status", "success");
                response.put("message", "Update successful.");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "Failed to update sensor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/sendSensorData")
    public void sendSensorData(@RequestBody User sensorData) {
        Long assignedStationId = sensorData.getAssignedStationId(); // Lấy assignedStationId từ dữ liệu

        // Gửi dữ liệu đến tất cả client đang lắng nghe với assignedStationId
//        messagingTemplate.convertAndSend("/topic/sensorData/" + assignedStationId, sensorData);
    }

}
