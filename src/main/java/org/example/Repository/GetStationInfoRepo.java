package org.example.Repository;

import org.example.Model.StationInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GetStationInfoRepo extends CrudRepository<StationInfo, Long> {

    @Query(value = "SELECT s.id, s.name, s.location, s.uri, s.port FROM stations s", nativeQuery = true)
    List<StationInfo> findAllStationsInfo();
}
