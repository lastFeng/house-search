package com.example.housesearch.reposity;

import com.example.housesearch.domain.HouseSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseSubscribeRepository extends JpaRepository<HouseSubscribe, Integer> {
}
