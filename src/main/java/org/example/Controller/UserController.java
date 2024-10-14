package org.example.Controller;

import org.example.Model.AsignStation;
import org.example.Model.User;
import org.example.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/assign-user-station")
    public ResponseEntity<Void> assignStation_User(@PathVariable Long id, @RequestBody Long stationId) {
        userService.assignStation(id, stationId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/assign-sensor-station")
    public ResponseEntity<Void> assignStation_Sensor(@RequestBody AsignStation asignStation) {
        userService.assignStation_Sensor(asignStation.getStationId(), asignStation.getUri(),asignStation.getPort());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        boolean isValidUser = userService.validateUser(user.getUsername(), user.getPassword());
        if (isValidUser) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}
