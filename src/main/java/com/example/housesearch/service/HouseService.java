package com.example.housesearch.service;

import com.example.housesearch.domain.*;
import com.example.housesearch.domain.base.HouseForm;
import com.example.housesearch.domain.base.PhotoForm;
import com.example.housesearch.domain.base.ServiceResult;
import com.example.housesearch.domain.dto.*;
import com.example.housesearch.reposity.*;
import com.example.housesearch.utils.Constant;
import com.example.housesearch.utils.LoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

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
        if (LoginUtils.getLoginUserId() == -1) {
            return ServiceResult.<HouseDTO>builder().success(false).message("用户未登录").build();
        }
        house.setAdminId(LoginUtils.getLoginUserId());
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
            ServiceResult<SubwayDTO> subway = subwayService.findSubwayById(houseForm.getSubwayLineId());
            if (subway.isSuccess()) {
                detail.setSubwayLineId(subway.getResult().getId());
                detail.setSubwayLineName(subway.getResult().getName());
            }
        }

        if (!Objects.nonNull(houseForm.getSubwayStationId())) {
            ServiceResult<SubwayStationDTO> station = subwayStationService.findSubwayStationById(houseForm.getSubwayStationId());
            if (station.isSuccess()) {
                detail.setSubwayStationId(station.getResult().getId());
                detail.setSubwayStationName(station.getResult().getName());
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

    /**
     * 通过id查找房源
     * @param houseId
     * @return
     */
    public House findById(Integer houseId) {
        return houseRepository.findById(houseId).orElse(null);
    }

    public Iterable<House> findAllByIds(List<Integer> ids) {
        List<House> allById = houseRepository.findAllById(ids);
        return (Iterable<House>) allById.iterator();
    }

    /***
     * 更新房源观看记录
     * @param houseId
     */
    @Transactional
    public void updateWatchTimes(Integer houseId) {
        House house = houseRepository.findById(houseId).get();
        house.setWatchTimes(house.getWatchTimes() + 1);
        houseRepository.save(house);
    }

    public ServiceResult<HouseDTO> findCompleteOne(Integer id) {
        House house = houseRepository.findById(id).orElse(null);
        if (house == null) {
            return ServiceResult.<HouseDTO>builder().success(false).build();
        }

        HouseDetail detail = houseDetailService.findByHouseId(id).get();
        List<HousePicture> pictures = housePictureService.findAllByHouseId(id);
        List<HousePictureDTO> pictureDTOS = new ArrayList<>();
        pictures.forEach(p -> pictureDTOS.add(modelMapper.map(p, HousePictureDTO.class)));
        List<HouseTag> tags = houseTagService.findAllByHouseId(id);
        List<String> tagNames = new ArrayList<>();
        tags.forEach(name -> tagNames.add(name.getName()));

        HouseDTO houseDTO = modelMapper.map(house, HouseDTO.class);
        houseDTO.setHouseDetail(modelMapper.map(detail, HouseDetailDTO.class));
        houseDTO.setPictures(pictureDTOS);
        houseDTO.setTags(tagNames);

        if (LoginUtils.getLoginUserId() > 0) {
            HouseSubscribe subscribe = houseSubscribeService.findByHouseIdAndUserId(house.getId(), LoginUtils.getLoginUserId());
            if (subscribe != null) {
                houseDTO.setSubscribeStatus(subscribe.getStatus());
            }
        }

        return ServiceResult.<HouseDTO>builder().success(true).message("success").result(houseDTO).build();
    }

    /***
     * ge更新房源状态
     * @param id
     * @param status
     * @return
     */
    public ServiceResult updateStatus(Integer id, int status) {
        House house = houseRepository.findById(id).orElse(null);
        if (house == null) {
            return ServiceResult.builder().success(false).message("House Not Found").build();
        }

        if (status == house.getStatus()) {
            return ServiceResult.builder().success(false).message("Status Not Change").build();
        }

        if (house.getStatus() == Constant.HouseStatus.RENTED.getValue() ||
                house.getStatus() == Constant.HouseStatus.DELETED.getValue()) {
            return ServiceResult.builder().success(false).message("No Permission").build();
        }

        houseRepository.updateStatus(id, status);

        return ServiceResult.builder().success(true).message("success").build();
    }

    /***
     * 查: 主页展示TODO
     */
}
