package com.example.housesearch.service;

import com.example.housesearch.domain.base.ServiceResult;
import com.example.housesearch.domain.search.BaiduMapLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author : guoweifeng
 * @date : 2021/5/1
 */
@Service
@Slf4j
public class BaiduAddressService {
    public ServiceResult removeLbs(Integer houseId) {
        return null;
    }

    public ServiceResult<BaiduMapLocation> getBaiduMapLocation(String cnName, String address) {
        return null;
    }

    public ServiceResult lbsUpload(BaiduMapLocation location, String title, String address, Integer houseId, Integer price, Integer area) {
        return null;
    }
}
