package com.example.housesearch.service;

import com.example.housesearch.domain.SupportAddress;
import com.example.housesearch.domain.dto.SupportAddressDTO;
import com.example.housesearch.reposity.SupportAddressRepository;
import com.example.housesearch.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author : guoweifeng
 * @date : 2021/4/30
 */
@Service
@Slf4j
public class SupportAddressService {

    @Autowired
    private SupportAddressRepository supportAddressRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Map<Constant.CityLevel, SupportAddressDTO> findCityAndRegion(String cityEnName, String regionEnName) {
        Map<Constant.CityLevel, SupportAddressDTO> result = new EnumMap<Constant.CityLevel, SupportAddressDTO>(Constant.CityLevel.class);

        SupportAddress city = findByEnNameAndLevel(cityEnName, Constant.CityLevel.CITY.getValue());
        SupportAddress region = findByEnNameAndLevel(cityEnName, Constant.CityLevel.REGION.getValue());
        result.put(Constant.CityLevel.CITY, modelMapper.map(city, SupportAddressDTO.class));
        result.put(Constant.CityLevel.REGION, modelMapper.map(region, SupportAddressDTO.class));
        return result;
    }

    public SupportAddress findByEnNameAndLevel(String cityEnName, String value) {
        return supportAddressRepository.findByEnNameAndLevel(cityEnName, value);
    }
}
