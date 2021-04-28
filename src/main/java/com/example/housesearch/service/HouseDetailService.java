package com.example.housesearch.service;

import com.example.housesearch.domain.HouseDetail;
import com.example.housesearch.reposity.HouseDetailRepository;
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
public class HouseDetailService {
    @Autowired
    private HouseDetailRepository houseDetailRepository;

    /***
     * 通过房源id查找房源详情
     * @param id
     * @return
     */
    public Optional<HouseDetail> findByHouseId(Integer id) {
        return houseDetailRepository.findByHouseId(id);
    }

    public HouseDetail save(HouseDetail detail) {
        return houseDetailRepository.save(detail);
    }

    /**
     * 根据房源id删除
     * @param houseId
     */
    public void deleteByHouseId(Integer houseId) {
        houseDetailRepository.deleteByHouseId(houseId);
    }
}
