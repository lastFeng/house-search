package com.example.housesearch.service;

import com.example.housesearch.domain.SubwayStation;
import com.example.housesearch.reposity.SubwayStationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author : guoweifeng
 * @date : 2021/4/29
 */
@Service
@Slf4j
public class SubwayStationService {
    @Autowired
    private SubwayStationRepository subwayStationRepository;

    /**
     * 通过id查找地铁站
     * @param subwayStationId
     * @return
     */
    public Optional<SubwayStation> findById(Integer subwayStationId) {
        return subwayStationRepository.findById(subwayStationId);
    }
}