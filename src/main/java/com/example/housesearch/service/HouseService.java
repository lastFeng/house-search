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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    private HouseDetailService houseDetailService;
    @Autowired
    private HouseSubscribeService houseSubscribeService;
    @Autowired
    private HousePictureService housePictureService;
    @Autowired
    private HouseTagService houseTagService;
    @Autowired
    private SubwayService subwayService;
    @Autowired
    private SubwayStationService subwayStationService;

    /***
     * 新增房源
     * @param houseForm 房源表信息
     * @return
     */
    @Transactional
    public ServiceResult<HouseDTO> saveOrUpdate(HouseForm houseForm) {
        // id为空为新增
        House house = modelMapper.map(houseForm, House.class);
        HouseDetail detail;
        List<HousePicture> pictures;
        List<HouseTag> tags;
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

        Integer id = houseForm.getId();

        // 新增或修改HouseDetail
        detail = houseDetailService.findByHouseId(id).orElse(new HouseDetail());
        wrappedHouseDetail(detail, houseForm);
        houseDetailService.save(detail);

        // 新增或修改HousePicture
        pictures = housePictureService.findAllByHouseId(id);
        if (!CollectionUtils.isEmpty(pictures)) {
            housePictureService.deleteAll(pictures);
        }
        pictures = new ArrayList<>();
        List<PhotoForm> photos = houseForm.getPhotos();
        if (!CollectionUtils.isEmpty(photos)) {
            for (PhotoForm photo : photos) {
                pictures.add(modelMapper.map(photo, HousePicture.class));
            }
        }
        housePictureService.saveAll(pictures);

        // 新增或修改HouseTags
        tags = houseTagService.findAllByHouseId(id);
        if (!CollectionUtils.isEmpty(tags)) {
            houseTagService.deleteAll(tags);
        }
        pictures = new ArrayList<>();
        List<String> tagList = houseForm.getTags();
        if (!CollectionUtils.isEmpty(tagList)) {
            for (String name : tagList) {
                HouseTag tag = new HouseTag();
                tag.setHouseId(id);
                tag.setName(name);
                tags.add(tag);
            }
        }
        houseTagService.saveAll(tags);
        return ServiceResult.<HouseDTO>builder().success(true).build();
    }

    /***
     * 从HouseForm表中提取出HouseDetail的详细信息
     * @param detail
     * @param houseForm
     */
    private void wrappedHouseDetail(HouseDetail detail, HouseForm houseForm) {
        detail.setHouseId(houseForm.getId());
        if (Objects.nonNull(houseForm.getSubwayLineId())) {
            Subway subway = subwayService.findById(houseForm.getSubwayLineId()).orElse(null);
            if (Objects.nonNull(subway)) {
                detail.setSubwayLineId(subway.getId());
                detail.setSubwayLineName(subway.getName());
            }
        }

        if (!Objects.nonNull(houseForm.getSubwayStationId())) {
            SubwayStation subwayStation = subwayStationService.findById(houseForm.getSubwayStationId()).orElse(null);
            if (Objects.nonNull(subwayStation)) {
                detail.setSubwayStationId(subwayStation.getId());
                detail.setSubwayStationName(subwayStation.getName());
            }
        }

        detail.setDescription(houseForm.getDescription());
        detail.setDetailAddress(houseForm.getDetailAddress());
        detail.setLayoutDesc(houseForm.getLayoutDesc());
        detail.setRentWay(houseForm.getRentWay());
        detail.setRoundService(houseForm.getRoundService());
        detail.setTraffic(houseForm.getTraffic());
    }

    /**
     * 删
     */
    @Transactional
    public ServiceResult<Void> deleteHouseById(Integer houseId) {
        houseTagService.deleteByHouseId(houseId);
        housePictureService.deleteByHouseId(houseId);
        houseDetailService.deleteByHouseId(houseId);
        houseRepository.deleteById(houseId);
        return ServiceResult.<Void>builder().success(true).build();
    }

    /***
     * 查: 主页展示TODO
     */
}
