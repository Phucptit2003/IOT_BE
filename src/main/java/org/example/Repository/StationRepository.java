package org.example.Repository;


import org.example.Model.Station;
import org.example.Model.StationInfo;
import org.example.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, String> {
    Station findById(int stationId);

}
