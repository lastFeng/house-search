package com.example.housesearch.service;

import com.example.housesearch.domain.*;
import com.example.housesearch.domain.base.HouseForm;
import com.example.housesearch.domain.base.PhotoForm;
import com.example.housesearch.domain.base.ServiceResult;
import com.example.housesearch.domain.dto.HouseDTO;
import com.example.housesearch.reposity.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author : guoweifeng
 * @date : 2021/4/28
 */
@Service
@Slf4j
public class HouseService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private HouseRepository houseRepository;
    @Autowired
    private HouseDetailRepository houseDetailRepository;
    @Autowired
    private HouseSubscribeRepository houseSubscribeRepository;
    @Autowired
    private HousePictureRepository housePictureRepository;
    @Autowired
    private HouseTagRepository houseTagRepository;
    @Autowired
    private SubwayRepository subwayRepository;
    @Autowired
    private SubwayStationRepository subwayStationRepository;

    /***
     * 新增房源
     * @param houseForm 房源表信息
     * @return
     */
    public ServiceResult<HouseDTO> saveOrUpdate(HouseForm houseForm) {
        // id为空为新增
        House house = modelMapper.map(houseForm, House.class);
        Date now = new Date();
        if (Objects.isNull(houseForm.getId())) {
            house.setCreateTime(now);
            house.setLastUpdateTime(now);
            house = houseRepository.save(house);
            houseForm.setId(house.getId());
        } else {
            // id不为空为修改,只针对house进行处理
            house.setLastUpdateTime(now);
            houseRepository.save(house);
        }
        return null;
    }

    private HouseDetail wrappedHouseDetail(HouseForm houseForm) {
        HouseDetail detail = modelMapper.map(houseForm, HouseDetail.class);
        detail.setHouseId(houseForm.getId());
        if (Objects.nonNull(houseForm.getSubwayLineId())) {
            Subway subway = subwayRepository.findById(houseForm.getSubwayLineId()).orElse(null);
            if (Objects.nonNull(subway)) {
                detail.setSubwayLineName(subway.getName());
            }
        }

        if (!Objects.nonNull(houseForm.getSubwayStationId())) {
            SubwayStation subwayStation = subwayStationRepository.findById(houseForm.getSubwayStationId()).orElse(null);
            if (Objects.nonNull(subwayStation)) {
                detail.setSubwayStationName(subwayStation.getName());
            }
        }
        return detail;
    }

    /**
     * 删
     */

    /***
     * 改
     */

    /***
     * 查
     */
}
