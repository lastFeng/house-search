package com.example.housesearch.service;

import com.example.housesearch.domain.HousePicture;
import com.example.housesearch.reposity.HousePictureRepository;
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
public class HousePictureService {
    @Autowired
    private HousePictureRepository housePictureRepository;

    /***
     * 根据房源id查找房源图片
     * @param id
     * @return
     */
    public List<HousePicture> findAllByHouseId(Integer id) {
        return housePictureRepository.findAllByHouseId(id);
    }

    /***
     * 批量删除房源图片
     * @param pictures
     */
    public void deleteAll(List<HousePicture> pictures) {
        housePictureRepository.deleteAll(pictures);
    }

    /***
     * 批量保存房源图片
     * @param pictures
     * @return
     */
    public List<HousePicture> saveAll(List<HousePicture> pictures) {
        return housePictureRepository.saveAll(pictures);
    }

    /**
     * 根据房源id删除
     * @param houseId
     */
    public void deleteByHouseId(Integer houseId) {
        housePictureRepository.deleteByHouseId(houseId);
    }
}
