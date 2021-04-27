package com.example.housesearch.reposity;

import com.example.housesearch.domain.HouseTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseTagRepository extends JpaRepository<HouseTag, Integer> {
}
