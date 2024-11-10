package org.example.Service;

import ConnectESP.ArduinoWebSocketClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Model.*;
import org.example.Repository.SensorRepository;
import org.example.Repository.StationRepository;
import org.example.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StationRepository stationRepository;
   private String messageToSensor="";
    @Autowired
    private SensorService sensorService;
    @Autowired
    private SensorRepository sensorRepository;

    private final WebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    public UserService(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }
    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void assignStation(Long userId, Long stationId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setAssignedStationId(stationId);
            userRepository.save(user);
        }
    }
    public void assignStation_Sensor(int stationId, String uri, int port) {
        Station station = stationRepository.findById(stationId);
        String serverUri = "ws://" + uri + ":" + port;
        System.out.println("Server URI: " + serverUri);

        if (station != null  && !uri.equals("0")) {
            station.setUri(uri);
            station.setPort(port);
            stationRepository.save(station);
            try {
                ArduinoWebSocketClient client = new ArduinoWebSocketClient(new URI(serverUri));

                // Set the message listener to handle messages from ESP32
                client.setMessageListener(new ArduinoWebSocketClient.MessageListener() {
                    @Override
                    public void onMessageReceived(String message) {
                        System.out.println("Message from ESP32 in assignStation_Sensor: " + message);
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            SensorData sensorData = objectMapper.readValue(message, SensorData.class);
                            // Gửi dữ liệu tới FE qua WebSocket
                            if (webSocketHandler != null) {
//                                webSocketHandler.broadcastMessage(message);
                                webSocketHandler.sendToStation(stationId,message);
                                Station station = stationRepository.findById(stationId);
                                List<Sensor> sensors = sensorRepository.findByStationId(Long.parseLong(String.valueOf(stationId)));
                                Sensor newSensor ;
                                if(sensors.size()==0) {
                                    newSensor = new Sensor();
                                    newSensor.setStation(station);
                                    newSensor.setName("Sensor of station " + station.getName()); // Đặt tên cho sensor nếu cần
                                    newSensor.setType("ESP32");
                                    // Gán dữ liệu từ ESP32 vào sensor
                                    newSensor.setTemperature(sensorData.getTemperature());
                                    newSensor.setHumidity(sensorData.getHumidity());
                                    newSensor.setFanSpeed(sensorData.getFanSpeed());
                                    newSensor.setLedState(sensorData.getLedState());
                                    newSensor.setBuzzerState(sensorData.getBuzzerState());
                                    newSensor.setTargetHumidity(sensorData.getTargetHumidity());
                                    newSensor.setTargetTemperature(sensorData.getTargetTemperature());
                                    newSensor.setMediumHumidity(sensorData.getMediumHumidity());
                                    newSensor.setMediumTemperature(sensorData.getMediumTemperature());
                                    // Bạn có thể thêm logic xử lý thêm nếu cần, ví dụ: tính toán trạng thái của sensor
                                    if (sensorData.getFanSpeed() > 0) {
                                        newSensor.setStatus("active");
                                    } else {
                                        newSensor.setStatus("idle");
                                    }
                                    station.addSensor(newSensor);
                                }
                                else {
                                    newSensor = sensors.get(0);
                                    // Gán dữ liệu từ ESP32 vào sensor
                                    newSensor.setTemperature(sensorData.getTemperature());
                                    newSensor.setHumidity(sensorData.getHumidity());
                                    newSensor.setFanSpeed(sensorData.getFanSpeed());
                                    newSensor.setLedState(sensorData.getLedState());
                                    newSensor.setBuzzerState(sensorData.getBuzzerState());
                                    newSensor.setTargetHumidity(sensorData.getTargetHumidity());
                                    newSensor.setTargetTemperature(sensorData.getTargetTemperature());
                                    newSensor.setMediumHumidity(sensorData.getMediumHumidity());
                                    newSensor.setMediumTemperature(sensorData.getMediumTemperature());
                                    // Bạn có thể thêm logic xử lý thêm nếu cần, ví dụ: tính toán trạng thái của sensor
                                    if (sensorData.getFanSpeed() > 0) {
                                        newSensor.setStatus("active");
                                    } else {
                                        newSensor.setStatus("idle");
                                    }
                                }

                                sensorRepository.save(newSensor);

                                // Gửi dữ liệu tới FE qua WebSocket (nếu cần)
                                if (webSocketHandler != null) {
                                    //webSocketHandler.broadcastMessage(message);
                                    webSocketHandler.sendToStation(stationId,message);
                                } else {
                                    System.out.println("webSocketHandler is null");
                                }
                            } else {
                                System.out.println("webSocketHandler is null");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                client.connect();

                // Keep the connection alive
                while (true) {
                    Thread.sleep(1000); // Continuous loop
                }

            } catch (URISyntaxException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }




//    private void processMessageFromESP(String message,int stationId) {
//        try {
//            // Chuyển đổi thông điệp JSON thành đối tượng SensorData
//            Sensor sensorData = objectMapper.readValue(message, Sensor.class);
//
//            System.out.println("SensorData: " + sensorData.getBuzzerState());
//            // Gửi dữ liệu cảm biến đến topic theo stationId
//            messagingTemplate.convertAndSend("/topic/sensors/" + stationId, sensorData);
//        } catch (Exception e) {
//            e.printStackTrace(); // Xử lý lỗi nếu có
//        }
//    }

    public User validateUser(String username, String password) {
        System.out.println(username+" : "+password);
        User user = userRepository.findByUsername(username);
        System.out.println("user: "+user.getUsername()+" pasword:"+user.getPassword());
        if(user!=null && user.getPassword().equals(password)){
            return user;
        }
        else return null;
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    public User updateUser(Long id,User user) {

        User existUser = userRepository.findById(id).orElse(null);
        existUser.setUsername(user.getUsername());
        existUser.setPassword(user.getPassword());
        existUser.setLevel(user.getLevel());
        existUser.setAssignedStationId(user.getAssignedStationId());
            return userRepository.save(existUser);


    }
}
