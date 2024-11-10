package org.example.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "stations")
public class StationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String location;
    private String uri;
    private int port;

    // Constructor
    public StationInfo(int id, String name, String location, String uri, int port) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.uri = uri;
        this.port = port;
    }

    public StationInfo() {
        
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
}
