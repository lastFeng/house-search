package com.example.housesearch.service;

import com.example.housesearch.domain.dto.SupportAddressDTO;
import com.example.housesearch.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author : guoweifeng
 * @date : 2021/4/30
 */
@Service
@Slf4j
public class SupportAddressService {

    public Map<Constant.CityLevel, SupportAddressDTO> findCityAndRegion(String cityEnName, String regionEnName) {
        return null;
    }
}
