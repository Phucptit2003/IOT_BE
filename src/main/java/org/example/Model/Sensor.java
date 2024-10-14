package org.example.Model;
import jakarta.persistence.*;

@Entity
@Table(name = "sensors") // Adjust to your actual table name
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // You may want to set a default or retrieve this dynamically
    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;
    private String type; // Assuming you will set this based on your application logic
    private String status; // You may want to derive this from your logic or conditions
    private double temperature;
    private double humidity;
    private int fanSpeed;
    private String ledState;
    private String buzzerState;

    // Mới thêm các thuộc tính này
    private Double targetTemperature;
    private Double targetHumidity;
    private Double mediumTemperature;
    private Double mediumHumidity;

    public Double getTargetTemperature() {
        return targetTemperature;
    }

    public void setTargetTemperature(Double targetTemperature) {
        this.targetTemperature = targetTemperature;
    }

    public Double getTargetHumidity() {
        return targetHumidity;
    }

    public void setTargetHumidity(Double targetHumidity) {
        this.targetHumidity = targetHumidity;
    }

    public Double getMediumTemperature() {
        return mediumTemperature;
    }

    public void setMediumTemperature(Double mediumTemperature) {
        this.mediumTemperature = mediumTemperature;
    }

    public Double getMediumHumidity() {
        return mediumHumidity;
    }

    public void setMediumHumidity(Double mediumHumidity) {
        this.mediumHumidity = mediumHumidity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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
}
