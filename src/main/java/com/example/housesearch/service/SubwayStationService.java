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
}
