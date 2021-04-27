package com.example.housesearch.reposity;

import com.example.housesearch.domain.House;
import org.springframework.data.repository.CrudRepository;

public interface HouseRepository extends CrudRepository<House, Long> {
}
