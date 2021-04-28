package com.example.housesearch.reposity;

import com.example.housesearch.domain.HouseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HouseDetailRepository extends JpaRepository<HouseDetail, Integer> {
    Optional<HouseDetail> findByHouseId(Integer id);

    void deleteByHouseId(Integer houseId);
}
