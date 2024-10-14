package org.example.Service;

import org.example.Model.Sensor;
import org.example.Repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorService {

    @Autowired
    private SensorRepository sensorRepository;

    // Lấy tất cả cảm biến
    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    // Lấy cảm biến theo ID
    public Sensor getSensorById(Long id) {
        return sensorRepository.findById(id).orElse(null);
    }

    // Thêm cảm biến
    public Sensor addSensor(Sensor sensor) {
        return sensorRepository.save(sensor);
    }

    // Cập nhật cảm biến
    public Sensor updateSensor(Long id, Sensor sensor) {
        Sensor existingSensor = sensorRepository.findById(id).orElse(null);
        if (existingSensor != null) {
            existingSensor.setName(sensor.getName());
            existingSensor.setStation(sensor.getStation());
            existingSensor.setType(sensor.getType());
            existingSensor.setStatus(sensor.getStatus());
            existingSensor.setTemperature(sensor.getTemperature());
            existingSensor.setHumidity(sensor.getHumidity());
            existingSensor.setFanSpeed(sensor.getFanSpeed());
            existingSensor.setLedState(sensor.getLedState());
            existingSensor.setBuzzerState(sensor.getBuzzerState());
            return sensorRepository.save(existingSensor);
        }
        return null;
    }

    // Xóa cảm biến
    public void deleteSensor(Long id) {
        sensorRepository.deleteById(id);
    }
    public List<Sensor> findByStationId(Long stationId) {
        return sensorRepository.findByStationId(stationId);
    }
    public void updateSensorData(Sensor sensor) {
        // Save or update sensor data
        sensorRepository.save(sensor);

        // Notify the controller to send updates
        sendSensorDataUpdate(sensor, sensor.getStation().getId());
    }

    // Add the same method here as in the controller to send data updates
    public void sendSensorDataUpdate(Sensor sensor, int stationId) {
        // You can either call the controller's method here or handle it differently
    }
}
