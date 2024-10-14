package org.example.Model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "stations")
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String location;

    // URI and Port for WebSocket
    private String uri; // WebSocket URI
    private int port;   // WebSocket port

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Sensor> sensors = new ArrayList<>();


    public Station() {
    }

    public Station(String name, String location, String uri, int port, List<Sensor> sensors) {
        this.name = name;
        this.location = location;
        this.uri = uri;
        this.port = port;
        this.sensors = sensors;
    }
    public void addSensor(Sensor sensor) {
        sensors.add(sensor);
        sensor.setStation(this);  // Set the relationship in the sensor
    }
    public void removeSensor(Sensor sensor) {
        sensors.remove(sensor);
        sensor.setStation(null); // Remove the relationship in the sensor
    }
    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    // Method to manage sensors (add, remove, etc.) can be added here
}
