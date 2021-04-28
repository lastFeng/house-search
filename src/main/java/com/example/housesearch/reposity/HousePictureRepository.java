package com.example.housesearch.reposity;

import com.example.housesearch.domain.HousePicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HousePictureRepository extends JpaRepository<HousePicture, Integer> {
    List<HousePicture> findAllByHouseId(Integer id);

    void deleteByHouseId(Integer houseId);
}
