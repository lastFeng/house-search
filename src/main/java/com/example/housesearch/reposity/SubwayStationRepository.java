package com.example.housesearch.reposity;

import com.example.housesearch.domain.SubwayStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubwayStationRepository extends JpaRepository<SubwayStation, Integer> {
    /**
     * 根据subwayId查找所有地铁站
     * @param subwayId
     * @return
     */
    List<SubwayStation> findAllBySubwayId(Integer subwayId);
}
