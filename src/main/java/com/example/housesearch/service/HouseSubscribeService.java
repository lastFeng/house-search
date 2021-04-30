package com.example.housesearch.service;

import com.example.housesearch.domain.House;
import com.example.housesearch.domain.HouseSubscribe;
import com.example.housesearch.domain.base.ServiceMultiResult;
import com.example.housesearch.domain.base.ServiceResult;
import com.example.housesearch.domain.dto.HouseDTO;
import com.example.housesearch.domain.dto.HouseSubscribeDTO;
import com.example.housesearch.reposity.HouseSubscribeRepository;
import com.example.housesearch.utils.LoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.example.housesearch.utils.Constant.*;

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

    @Autowired
    private ModelMapper modelMapper;

    /***
     * 查找房源订阅列表
     * @param start
     * @param size
     * @return
     */
    public ServiceMultiResult<Pair<HouseDTO, HouseSubscribeDTO>> findSubscribeList(int start, int size) {
        Integer loginUserId = LoginUtils.getLoginUserId();
        Pageable pageable = PageRequest.of(start/size, size, Sort.by(Sort.Direction.DESC, "orderTime"));
        Page<HouseSubscribe> allByUserIdAndStatus = houseSubscribeRepository.findAllByUserIdAndStatus(loginUserId, SUBSCRIBE_STATUS_IN_ORDER_LIST, pageable);

        return wrapper(allByUserIdAndStatus);
    }

    public ServiceMultiResult<Pair<HouseDTO, HouseSubscribeDTO>> findSubscribeListByStatus(Integer status, int start, int size) {
        Integer loginUserId = LoginUtils.getLoginUserId();
        Pageable pageable = PageRequest.of(start / size, size, Sort.by(Sort.Direction.DESC, "createTime"));

        Page<HouseSubscribe> idAndStatus = houseSubscribeRepository.findAllByUserIdAndStatus(loginUserId, status, pageable);
        return wrapper(idAndStatus);
    }

    private ServiceMultiResult<Pair<HouseDTO, HouseSubscribeDTO>> wrapper(Page<HouseSubscribe> page) {
        List<Pair<HouseDTO, HouseSubscribeDTO>> result = new ArrayList<>();

        if (page.getSize() >= 1) {
            List<HouseSubscribeDTO> subscribeDTOS = new ArrayList<>();
            List<Integer> houseIds = new ArrayList<>();

            page.forEach(scribe -> {
                subscribeDTOS.add(modelMapper.map(scribe, HouseSubscribeDTO.class));
                houseIds.add(scribe.getHouseId());
            });

            Iterable<House> houses = houseService.findAllByIds(houseIds);
            Map<Integer, HouseDTO> houseDTOMap = new HashMap<>();

            houses.forEach(house -> {
                houseDTOMap.put(house.getId(), modelMapper.map(house, HouseDTO.class));
            });
            subscribeDTOS.forEach(subscribe -> {
                result.add(Pair.of(houseDTOMap.get(subscribe.getHouseId()), subscribe));
            });
        }

        return ServiceMultiResult.<Pair<HouseDTO, HouseSubscribeDTO>>builder().total(page.getTotalElements()).result(result).build();
    }

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
        HouseSubscribe byHouseIdAndUserId = findByHouseIdAndUserId(houseId, loginUserId);

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

        HouseSubscribe byHouseIdAndUserId = findByHouseIdAndUserId(houseId, loginUserId);

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
        HouseSubscribe byHouseIdAndUserId = findByHouseIdAndUserId(houseId, loginUserId);

        if (Objects.nonNull(byHouseIdAndUserId)) {
            houseSubscribeRepository.deleteById(byHouseIdAndUserId.getId());
        }

        return ServiceResult.builder().success(true).build();
    }

    /***
     * 完成订阅
     * @return
     */
    @Transactional
    public ServiceResult finishSubscribe(Integer houseId) {
        Integer loginUserId = LoginUtils.getLoginUserId();
        HouseSubscribe subscribe = findByHouseIdAndUserId(houseId, loginUserId);
        if (subscribe == null) {
            return  ServiceResult.builder().success(false).message("无预约记录").build();
        }

        houseSubscribeRepository.updateStatus(subscribe.getId(), SUBSCRIBE_STATUS_FINISH);
        houseService.updateWatchTimes(houseId);

        return ServiceResult.builder().success(true).message("success").build();
    }

    /***
     * 通过id与登录id查找
     * @param id
     * @param loginUserId
     * @return
     */
    public HouseSubscribe findByHouseIdAndUserId(Integer id, Integer loginUserId) {
        return houseSubscribeRepository.findByHouseIdAndUserId(id, loginUserId);
    }
}
