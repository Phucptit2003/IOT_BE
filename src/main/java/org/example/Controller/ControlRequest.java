package org.example.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public class ControlRequest {
    private int stationId; // ID của station
    private float highTemperature; // Nhiệt độ cao
    private float mediumTemperature; // Nhiệt độ trung bình
    private float highHumidity; // Độ ẩm cao
    private float mediumHumidity; // Độ ẩm trung bình
    private boolean ledState; // Trạng thái LED
    private int fanSpeed; // Tốc độ quạt
    private boolean buzzerState; // Trạng thái Buzzer

    // Getters và setters
    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public float getHighTemperature() {
        return highTemperature;
    }

    public void setHighTemperature(float highTemperature) {
        this.highTemperature = highTemperature;
    }

    public float getMediumTemperature() {
        return mediumTemperature;
    }

    public void setMediumTemperature(float mediumTemperature) {
        this.mediumTemperature = mediumTemperature;
    }

    public float getHighHumidity() {
        return highHumidity;
    }

    public void setHighHumidity(float highHumidity) {
        this.highHumidity = highHumidity;
    }

    public float getMediumHumidity() {
        return mediumHumidity;
    }

    public void setMediumHumidity(float mediumHumidity) {
        this.mediumHumidity = mediumHumidity;
    }

    public boolean isLedState() {
        return ledState;
    }

    public void setLedState(boolean ledState) {
        this.ledState = ledState;
    }

    public int getFanSpeed() {
        return fanSpeed;
    }

    public void setFanSpeed(int fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public boolean isBuzzerState() {
        return buzzerState;
    }

    public void setBuzzerState(boolean buzzerState) {
        this.buzzerState = buzzerState;
    }
}
