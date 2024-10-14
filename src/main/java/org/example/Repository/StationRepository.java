package org.example.Repository;


import org.example.Model.Station;
import org.example.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<Station, String> {
    Station findById(int stationId);
}
