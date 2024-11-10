package org.example.Controller;

import org.example.Model.AsignStation;
import org.example.Model.Station;
import org.example.Model.User;
import org.example.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        for(User user : userService.getAllUsers()) {
            System.out.println(user.getUsername());
        }
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
    @GetMapping("/deleteUser/{id}")
    public void  deleteUser(@PathVariable int id) {
         userService.deleteUser(Long.parseLong(String.valueOf(id)));
    }
    @PostMapping("/update/{id}")
    public User updateStation(@PathVariable int id, @RequestBody User user) {
        return userService.updateUser(Long.parseLong(String.valueOf(id)), user);
    }

    @PutMapping("/assign-sensor-station")
    public ResponseEntity<Void> assignStation_Sensor(@RequestBody AsignStation asignStation) {
        userService.assignStation_Sensor(asignStation.getStationId(), asignStation.getUri(),asignStation.getPort());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        User isValidUser = userService.validateUser(user.getUsername(), user.getPassword());

        if (isValidUser != null) {
            int role = isValidUser.getLevel();
            Long assignedStationId = isValidUser.getAssignedStationId(); // Get assigned station ID

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("role", role);
            response.put("assignedStationId", assignedStationId); // Include it in the response

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid username or password"));
        }
    }


}
