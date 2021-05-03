package com.example.housesearch.service;

import com.example.housesearch.domain.Subway;
import com.example.housesearch.domain.SubwayStation;
import com.example.housesearch.domain.base.ServiceResult;
import com.example.housesearch.domain.dto.SubwayDTO;
import com.example.housesearch.domain.dto.SubwayStationDTO;
import com.example.housesearch.reposity.SubwayStationRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @Autowired
    private ModelMapper modelMapper;

    /**
     * 通过id查找地铁站
     * @param subwayStationId
     * @return
     */
    public ServiceResult<SubwayStationDTO> findSubwayStationById(Integer subwayStationId) {
        if (subwayStationId == null) {
            return ServiceResult.<SubwayStationDTO>builder().success(false).build();
        }
        Optional<SubwayStation> byId = subwayStationRepository.findById(subwayStationId);
        if (!byId.isPresent()) {
            return ServiceResult.<SubwayStationDTO>builder().success(false).build();
        }
        return ServiceResult.<SubwayStationDTO>builder().success(true).result(modelMapper.map(byId.get(), SubwayStationDTO.class)).build();
    }

    /**
     * 根据地铁id查找所有地铁站信息
     * @param subwayId
     * @return
     */
    public List<SubwayStationDTO> findAllStationBySubway(Integer subwayId) {
        List<SubwayStation> subwayStations = subwayStationRepository.findAllBySubwayId(subwayId);
        List<SubwayStationDTO> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(subwayStations)) {
            subwayStations.forEach(item -> result.add(modelMapper.map(item, SubwayStationDTO.class)));
        }

        return result;
    }

    /***
     * 根据地铁站id查找地铁站信息
     * @param stationId
     * @return
     */
    public ServiceResult<SubwayStationDTO> findById(Integer stationId) {
        if (Objects.nonNull(stationId)) {
            SubwayStation subwayStation = subwayStationRepository.findById(stationId).orElse(null);
            if (Objects.nonNull(subwayStation)) {
                return ServiceResult.<SubwayStationDTO>builder().success(true).message("success")
                        .result(modelMapper.map(subwayStation, SubwayStationDTO.class)).build();
            }
        }
        return ServiceResult.<SubwayStationDTO>builder().success(false).message("Not Found").result(null).build();
    }
}
