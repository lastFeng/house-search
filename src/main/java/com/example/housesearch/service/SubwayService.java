package com.example.housesearch.service;

import com.example.housesearch.domain.Subway;
import com.example.housesearch.domain.base.ServiceResult;
import com.example.housesearch.domain.dto.SubwayDTO;
import com.example.housesearch.reposity.SubwayRepository;
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
public class SubwayService {

    @Autowired
    private SubwayRepository subwayRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * 通过地铁线id查找对应地铁内容
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
}
