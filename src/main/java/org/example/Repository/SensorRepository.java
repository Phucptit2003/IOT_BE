package org.example.Repository;

import org.example.Model.Sensor;
import org.example.Model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
    List<Sensor> findByStationId(Long stationId);
    List<Sensor> findByStation(Station station);
}
