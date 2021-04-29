package com.example.housesearch.service;

import com.example.housesearch.domain.House;
import com.example.housesearch.domain.HouseSubscribe;
import com.example.housesearch.domain.base.ServiceResult;
import com.example.housesearch.reposity.HouseSubscribeRepository;
import com.example.housesearch.utils.LoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

import static com.example.housesearch.utils.Contant.*;

/**
 * @author : guoweifeng
 * @date : 2021/4/29
 */
@Service
@Slf4j
public class HouseSubscribeService {
    @Autowired
    private HouseSubscribeRepository houseSubscribeRepository;

    @Autowired
    private HouseService houseService;

    /***
     * 添加房源订阅到待订阅区
     * @param houseId 房源Id
     * @return
     */
    @Transactional
    public ServiceResult addSubscribeOrder(Integer houseId) {
        Integer loginUserId = LoginUtils.getLoginUserId();
        House house = houseService.findById(houseId);
        Date now = new Date();
        HouseSubscribe byHouseIdAndUserId = houseSubscribeRepository.findByHouseIdAndUserId(houseId, loginUserId);

        if (Objects.nonNull(byHouseIdAndUserId) && SUBSCRIBE_STATUS_FINISH.equals(byHouseIdAndUserId.getStatus())) {
            byHouseIdAndUserId.setStatus(SUBSCRIBE_STATUS_IN_ORDER_LIST);
            byHouseIdAndUserId.setOrderTime(null);
        }

        if (Objects.isNull(byHouseIdAndUserId)) {
            byHouseIdAndUserId = new HouseSubscribe();
            byHouseIdAndUserId.setUserId(loginUserId);
            byHouseIdAndUserId.setHouseId(houseId);
            byHouseIdAndUserId.setStatus(SUBSCRIBE_STATUS_IN_ORDER_LIST);
            byHouseIdAndUserId.setCreateTime(now);
            byHouseIdAndUserId.setAdminId(house.getAdminId());
        }
        byHouseIdAndUserId.setLastUpdateTime(now);
        houseSubscribeRepository.save(byHouseIdAndUserId);
        return ServiceResult.builder().success(true).build();
    }

    /***
     * 订阅房源，设置相应内容
     * @param houseId
     * @param orderTime
     * @param desc
     * @param telephone
     * @return
     */
    @Transactional
    public ServiceResult subscribe(Integer houseId, Date orderTime, String desc, String telephone) {
        Integer loginUserId = LoginUtils.getLoginUserId();
        House house = houseService.findById(houseId);
        Date now = new Date();

        HouseSubscribe byHouseIdAndUserId = houseSubscribeRepository.findByHouseIdAndUserId(houseId, loginUserId);

        if (Objects.nonNull(byHouseIdAndUserId) && (SUBSCRIBE_STATUS_IN_ORDER_LIST.equals(byHouseIdAndUserId.getStatus())
                ||SUBSCRIBE_STATUS_FINISH.equals(byHouseIdAndUserId.getStatus()))) {
            byHouseIdAndUserId.setStatus(SUBSCRIBE_STATUS_IN_ORDER_TIME);
        }

        if (Objects.isNull(byHouseIdAndUserId)) {
            byHouseIdAndUserId = new HouseSubscribe();
            byHouseIdAndUserId.setUserId(loginUserId);
            byHouseIdAndUserId.setHouseId(houseId);
            byHouseIdAndUserId.setStatus(SUBSCRIBE_STATUS_IN_ORDER_TIME);
            byHouseIdAndUserId.setCreateTime(now);
            byHouseIdAndUserId.setAdminId(house.getAdminId());
        }
        byHouseIdAndUserId.setOrderTime(orderTime);
        byHouseIdAndUserId.setDesc(desc);
        byHouseIdAndUserId.setTelephone(telephone);
        byHouseIdAndUserId.setLastUpdateTime(now);
        houseSubscribeRepository.save(byHouseIdAndUserId);
        return ServiceResult.builder().success(true).build();
    }

    /**
     * 取消订阅，删除订阅数据
     * @param houseId
     * @return
     */
    @Transactional
    public ServiceResult cancelSubscribe(Integer houseId) {
        Integer loginUserId = LoginUtils.getLoginUserId();
        HouseSubscribe byHouseIdAndUserId = houseSubscribeRepository.findByHouseIdAndUserId(houseId, loginUserId);

        if (Objects.nonNull(byHouseIdAndUserId)) {
            houseSubscribeRepository.deleteById(byHouseIdAndUserId.getId());
        }

        return ServiceResult.builder().success(true).build();
    }
}
