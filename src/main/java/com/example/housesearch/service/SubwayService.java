package com.example.housesearch.service;

import com.example.housesearch.domain.Subway;
import com.example.housesearch.reposity.SubwayRepository;
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
public class SubwayService {

    @Autowired
    private SubwayRepository subwayRepository;

    /***
     * 通过id查找地铁线
     * @param subwayLineId
     * @return
     */
    public Optional<Subway> findById(Integer subwayLineId) {
        return subwayRepository.findById(subwayLineId);
    }
}
