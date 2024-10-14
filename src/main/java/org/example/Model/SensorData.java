package org.example.Model;

public class SensorData {
    private double temperature;
    private double humidity;
    private int fanSpeed;
    private String ledState;
    private String buzzerState;
    private double targetTemperature;
    private double targetHumidity;
    private double mediumTemperature;
    private double mediumHumidity;

    // Getters và Setters cho các trường
    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public int getFanSpeed() {
        return fanSpeed;
    }

    public void setFanSpeed(int fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public String getLedState() {
        return ledState;
    }

    public void setLedState(String ledState) {
        this.ledState = ledState;
    }

    public String getBuzzerState() {
        return buzzerState;
    }

    public void setBuzzerState(String buzzerState) {
        this.buzzerState = buzzerState;
    }

    public double getTargetTemperature() {
        return targetTemperature;
    }

    public void setTargetTemperature(double targetTemperature) {
        this.targetTemperature = targetTemperature;
    }

    public double getTargetHumidity() {
        return targetHumidity;
    }

    public void setTargetHumidity(double targetHumidity) {
        this.targetHumidity = targetHumidity;
    }

    public double getMediumTemperature() {
        return mediumTemperature;
    }

    public void setMediumTemperature(double mediumTemperature) {
        this.mediumTemperature = mediumTemperature;
    }

    public double getMediumHumidity() {
        return mediumHumidity;
    }

    public void setMediumHumidity(double mediumHumidity) {
        this.mediumHumidity = mediumHumidity;
    }
}
