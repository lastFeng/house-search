package com.example.housesearch.reposity;

import com.example.housesearch.domain.HouseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseDetailRepository extends JpaRepository<HouseDetail, Integer> {
}
