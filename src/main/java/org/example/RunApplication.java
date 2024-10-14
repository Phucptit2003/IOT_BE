package org.example;

import org.example.Model.Sensor;
import org.example.Model.User;
import org.example.Service.SensorService;
import org.example.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class RunApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private SensorService sensorService;

    public static void main(String[] args) {
        SpringApplication.run(RunApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Tạo một số người dùng mẫu
        //createSampleUsers();

        // Lấy và in ra danh sách người dùng
        List<User> users = userService.getAllUsers();
        System.out.println("Danh sách người dùng:");
        users.forEach(user -> System.out.println(user.getUsername() + " - Level: " + user.getLevel()));


        // Lấy và in ra danh sách sensor
        List<Sensor> sensors = sensorService.getAllSensors();
        System.out.println("Danh sách sensor:");
        sensors.forEach(sensor -> System.out.println(sensor.getName() + " - Trạm: " + sensor.getStation().getId()));

        // Thực hiện các thao tác khác nếu cần
    }

//    private void createSampleUsers() {
//        User user1 = new User();
//        user1.setUsername("admin");
//        user1.setPassword("admin123");
//        user1.setLevel(1);
//
//        User user2 = new User();
//        user2.setUsername("user1");
//        user2.setPassword("user123");
//        user2.setLevel(2);
//
//        userService.createUser(user1);
//        userService.createUser(user2);
//
//        System.out.println("Đã tạo người dùng mẫu.");
//    }

}
