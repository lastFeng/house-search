package com.example.housesearch.service;

import com.example.housesearch.domain.HouseTag;
import com.example.housesearch.reposity.HouseTagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : guoweifeng
 * @date : 2021/4/29
 */
@Service
@Slf4j
public class HouseTagService {
    @Autowired
    private HouseTagRepository houseTagRepository;

    /***
     * 通过房源id查找所有Tag
     * @param id
     * @return
     */
    public List<HouseTag> findAllByHouseId(Integer id) {
        return houseTagRepository.findAllByHouseId(id);
    }

    /***
     * 批量删除
     * @param tags
     */
    public void deleteAll(List<HouseTag> tags) {
        houseTagRepository.deleteAll(tags);
    }

    public List<HouseTag> saveAll(List<HouseTag> tags) {
        return houseTagRepository.saveAll(tags);
    }

    /**
     * 根据房源id删除
     * @param houseId
     */
    public void deleteByHouseId(Integer houseId) {
        houseTagRepository.deleteByHouseId(houseId);
    }
}
