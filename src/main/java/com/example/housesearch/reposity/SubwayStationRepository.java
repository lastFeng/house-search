package com.example.housesearch.reposity;

import com.example.housesearch.domain.SubwayStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubwayStationRepository extends JpaRepository<SubwayStation, Integer> {
}
