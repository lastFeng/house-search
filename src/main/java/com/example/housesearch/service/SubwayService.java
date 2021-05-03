package com.example.housesearch.service;

import com.example.housesearch.domain.Subway;
import com.example.housesearch.domain.base.ServiceResult;
import com.example.housesearch.domain.dto.SubwayDTO;
import com.example.housesearch.reposity.SubwayRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
public class SubwayService {

    @Autowired
    private SubwayRepository subwayRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * 通过地铁线id查找对应地铁内容
     *
     * @param subwayLineId
     * @return
     */
    public ServiceResult<SubwayDTO> findSubwayById(Integer subwayLineId) {
        if (subwayLineId == null) {
            return ServiceResult.<SubwayDTO>builder().success(false).build();
        }
        Optional<Subway> byId = subwayRepository.findById(subwayLineId);
        if (!byId.isPresent()) {
            return ServiceResult.<SubwayDTO>builder().success(false).build();
        }
        return ServiceResult.<SubwayDTO>builder().success(true).result(modelMapper.map(byId.get(), SubwayDTO.class)).build();
    }

    /***
     * 根据城市名查找所有地铁线
     */
    public List<SubwayDTO> findAllSubwayByCity(String cityEnName) {
        List<SubwayDTO> result = new ArrayList<>();
        List<Subway> subways = subwayRepository.findAllByCityEnName(cityEnName);

        if (!CollectionUtils.isEmpty(subways)) {
            subways.forEach(subway -> result.add(modelMapper.map(subway, SubwayDTO.class)));
        }

        return result;
    }

    /**
     * 查找地铁线
     *
     * @param subwayId
     * @return
     */
    public ServiceResult<SubwayDTO> findSubway(Integer subwayId) {
        if (!StringUtils.isEmpty(subwayId)) {
            Subway subway = subwayRepository.findById(subwayId).orElse(null);

            if (Objects.nonNull(subway)) {
                return ServiceResult.<SubwayDTO>builder().success(true).message("success")
                        .result(modelMapper.map(subway, SubwayDTO.class)).build();
            }
        }
        return ServiceResult.<SubwayDTO>builder().success(false).message("Not Found").result(null).build();
    }
}
