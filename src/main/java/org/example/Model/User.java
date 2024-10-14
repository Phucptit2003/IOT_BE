package org.example.Model;

import jakarta.persistence.*;
import jakarta.persistence.Id;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private int level; // User level: 1 or 2

    @Column(name = "assigned_station_id")
    private Long assignedStationId; // The station assigned to the user, null if not assigned

    // Constructors
    public User() {}

    public User(String username, String password, int level, Long assignedStationId) {
        this.username = username;
        this.password = password;
        this.level = level;
        this.assignedStationId = assignedStationId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Long getAssignedStationId() {
        return assignedStationId;
    }

    public void setAssignedStationId(Long assignedStationId) {
        this.assignedStationId = assignedStationId;
    }
}
