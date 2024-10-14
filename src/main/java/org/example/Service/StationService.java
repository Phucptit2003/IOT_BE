package org.example.Service;

import ConnectESP.ArduinoWebSocketClient;
import org.example.Controller.ControlRequest;
import org.example.Model.Station;
import org.example.Repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.net.URI;
import java.util.List;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    /**
     * Lấy danh sách tất cả các trạm
     */
    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    /**
     * Tạo một trạm mới
     */
    public Station createStation(Station station) {
        return stationRepository.save(station);
    }

    /**
     * Cập nhật thông tin của một trạm theo ID
     */
    public Station updateStation(int id, Station station) {
        Station existingStation = stationRepository.findById(id);
        existingStation.setName(station.getName());
        existingStation.setLocation(station.getLocation());
        existingStation.setUri(station.getUri());
        existingStation.setPort(station.getPort());
        return stationRepository.save(existingStation);
    }

    /**
     * Xóa một trạm theo ID
     */
    public void deleteStation(int id) {
        stationRepository.deleteById(String.valueOf(id));
    }

    /**
     * Điều khiển một trạm
     */
    public void controlStation(ControlRequest request) {
        Station station = stationRepository.findById(request.getStationId());

        // Gửi dữ liệu đến ESP32
        sendDataToESP(station, request);
    }

    /**
     * Gửi dữ liệu đến ESP32 qua WebSocket
     */
    public void sendDataToESP(Station station, ControlRequest request) {
        try {
            String uri = station.getUri(); // Ensure this includes "ws://" or "wss://"
            int port = station.getPort();

            // Create a WebSocket client
            ArduinoWebSocketClient webSocketClient = new ArduinoWebSocketClient(new URI(uri + ":" + port));
            webSocketClient.connectBlocking(); // Wait for the connection to be established

            // Create JSON data
            String jsonData = String.format(
                    "{\"stationId\": %d, \"highTemperature\": %.2f, \"mediumTemperature\": %.2f, \"highHumidity\": %.2f, \"mediumHumidity\": %.2f, \"ledState\": %b, \"fanSpeed\": %d, \"buzzerState\": %b}",
                    request.getStationId(),
                    request.getHighTemperature(),
                    request.getMediumTemperature(),
                    request.getHighHumidity(),
                    request.getMediumHumidity(),
                    request.isLedState(),
                    request.getFanSpeed(),
                    request.isBuzzerState()
            );

            // Send data to ESP32
            webSocketClient.send(jsonData);
            // Optionally, you can wait for a response from ESP32 if needed
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error sending data to ESP32: " + e.getMessage());
        }
    }


    /**
     * Kết nối đến WebSocket của ESP32
     */
    private WebSocketSession connectToESPWebSocket(String uri, int port) {
        // Phương thức để thiết lập kết nối WebSocket tới ESP32
        // Bạn cần sử dụng một WebSocket client như Spring WebSocket hoặc Java-WebSocket
        // Trả về WebSocketSession nếu kết nối thành công, ngược lại trả về null
        // Ví dụ dưới đây chỉ là placeholder và cần được triển khai theo nhu cầu của bạn

        // ...
        return null; // Placeholder
    }
}
