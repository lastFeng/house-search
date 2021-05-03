package com.example.housesearch.reposity;

import com.example.housesearch.domain.Subway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubwayRepository extends JpaRepository<Subway, Integer> {
    /***
     * 根据城市名查找所有地铁线
     * @param cityEnName
     * @return
     */
    List<Subway> findAllByCityEnName(String cityEnName);
}
