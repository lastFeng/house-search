package com.example.housesearch.service;

import com.example.housesearch.domain.SupportAddress;
import com.example.housesearch.domain.base.ServiceMultiResult;
import com.example.housesearch.domain.base.ServiceResult;
import com.example.housesearch.domain.dto.SupportAddressDTO;
import com.example.housesearch.reposity.SupportAddressRepository;
import com.example.housesearch.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    /***
     * 查找所有的城市支持
     * @return
     */
    public ServiceMultiResult<SupportAddressDTO> findAllCities() {
        List<SupportAddress> addresses = supportAddressRepository.findAllByLevel(Constant.CityLevel.CITY.getValue());
        List<SupportAddressDTO> addressDTOS = new ArrayList<>();
        addresses.forEach(item -> addressDTOS.add(modelMapper.map(item, SupportAddressDTO.class)));
        return ServiceMultiResult.<SupportAddressDTO>builder().total(Long.valueOf(addressDTOS.size())).result(addressDTOS).build();
    }

    /**
     * 通过城市名查找所有的区域支持的城市
     * @param cityName
     * @return
     */
    public ServiceMultiResult<SupportAddressDTO> findAllRegionByCityName(String cityName) {
        if (StringUtils.isBlank(cityName)) {
            return ServiceMultiResult.<SupportAddressDTO>builder().total(0L).result(null).build();
        }

        List<SupportAddress> regions = supportAddressRepository.findAllByLevelAndBelongTo(Constant.CityLevel.REGION.getValue(),
                cityName);
        List<SupportAddressDTO> result = new ArrayList<>();
        regions.forEach(item -> result.add(modelMapper.map(item, SupportAddressDTO.class)));
        return ServiceMultiResult.<SupportAddressDTO>builder().total(Long.valueOf(result.size())).result(result).build();
    }

    /**
     * 根据city名称来查找支持的地址
     * @param cityEnName
     * @return
     */
    public ServiceResult<SupportAddressDTO> findCity(String cityEnName) {
        if (StringUtils.isNotBlank(cityEnName)) {
            SupportAddress byEnNameAndLevel = supportAddressRepository.findByEnNameAndLevel(cityEnName, Constant.CityLevel.CITY.getValue());
            if (Objects.nonNull(byEnNameAndLevel)) {
                return ServiceResult.<SupportAddressDTO>builder().success(true).message("success")
                        .result(modelMapper.map(byEnNameAndLevel, SupportAddressDTO.class)).build();
            }
        }
        return ServiceResult.<SupportAddressDTO>builder().success(false).message("Not Found").result(null).build();
    }
}
