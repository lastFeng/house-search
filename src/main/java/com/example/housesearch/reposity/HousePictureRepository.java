package com.example.housesearch.reposity;

import com.example.housesearch.domain.HousePicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HousePictureRepository extends JpaRepository<HousePicture, Integer> {
}
